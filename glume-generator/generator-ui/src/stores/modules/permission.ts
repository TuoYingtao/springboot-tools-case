import auth from "@/plugins/auth";
import router, { baseRoutes, constantRoutes, dynamicRoutes } from '@/router'
import Layout from '@/layout/index.vue'
import ParentView from '@/components/Framework/ParentView/index.vue'
import InnerLink from '@/layout/components/InnerLink/index.vue'
import { RouteRecordRaw } from "vue-router";
import { MenuApiService } from "@/api/menu/MenuApiService";

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('./../../views/**/*.vue')

const menuApiService = new MenuApiService();

const usePermissionStore = defineStore('permission', {
  state: () => ({
    routes: [],
    addRoutes: [],
    defaultRoutes: [],
    topbarRouters: [],
    sidebarRouters: []
  } as Permission),
  actions: {
    setRoutes(routes: LayoutRoutes[]) {
      this.addRoutes = routes
      this.routes = constantRoutes.concat(routes)
    },
    setDefaultRoutes(routes: LayoutRoutes[]) {
      this.defaultRoutes = constantRoutes.concat(routes)
    },
    setTopbarRoutes(routes: LayoutRoutes[]) {
      this.topbarRouters = routes
    },
    setSidebarRouters(routes: LayoutRoutes[]) {
      this.sidebarRouters = routes
    },
    generateRoutes(roles?: string[]): Promise<LayoutRoutes[]> {
      const routersInfo: Record<string, LayoutRoutes[]> = {
        defaultRoutes: [],
        rewriteRoutes: [],
        asyncRoutes: [],
        sidebarRoutes: [],
      };
      return new Promise(async resolve => {
        if (roles && roles.length > 1) {
          // 向后端请求路由数据
          const res = await menuApiService.getRouters();
          const sdata = JSON.parse(JSON.stringify(res.data))
          const rdata = JSON.parse(JSON.stringify(res.data))
          const defaultData = JSON.parse(JSON.stringify(res.data))
          routersInfo.sidebarRoutes = filterAsyncRouter(sdata)
          routersInfo.rewriteRoutes = filterAsyncRouter(rdata, false, true)
          routersInfo.defaultRoutes = filterAsyncRouter(defaultData)
        }
        // 静态路由
        routersInfo.sidebarRoutes.push(...filterDynamicRoutes(baseRoutes))
        routersInfo.rewriteRoutes.push(...filterDynamicRoutes(baseRoutes))
        routersInfo.defaultRoutes.push(...filterDynamicRoutes(baseRoutes))
        // 与动态路由联动的静态路由，只有在动态路由有权限时才去添加有所关联的静态路由
        routersInfo.asyncRoutes.push(...filterDynamicRoutes(dynamicRoutes))
        routersInfo.asyncRoutes.forEach(route => {
          router.addRoute(route as RouteRecordRaw)
        })
        this.setRoutes(routersInfo.rewriteRoutes)
        this.setDefaultRoutes(routersInfo.sidebarRoutes)
        this.setSidebarRouters(constantRoutes.concat(routersInfo.sidebarRoutes))
        this.setTopbarRoutes(routersInfo.defaultRoutes)
        resolve(routersInfo.rewriteRoutes)
      })
    }
  }
})

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap: LayoutRoutes[], lastRouter: LayoutRoutes | boolean = false, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, lastRouter, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap: LayoutRoutes[], lastRouter: LayoutRoutes | boolean = false) {
  var children: LayoutRoutes[] = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = (lastRouter as LayoutRoutes).path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes: LayoutRoutes[]) {
  const res: LayoutRoutes[] = []
  routes.forEach(route => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

export const loadView = (view: string) => {
  let res;
  for (const path in modules) {
    const dir = path.split('views/')[1].split('.vue')[0];
    if (dir === view) {
      res = () => modules[path]();
    }
  }
  return res;
}

export default usePermissionStore
