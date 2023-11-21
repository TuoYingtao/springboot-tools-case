import router from './router'
import { ElMessage } from 'element-plus'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { isHttp } from '@/utils/validate'
import useUserStore from '@/stores/modules/user'
import useSettingsStore from '@/stores/modules/settings'
import usePermissionStore from '@/stores/modules/permission'
import useAppStore from '@/stores/modules/app'
import { RouteRecordRaw } from "vue-router";
import { CookiesUtils } from "@/utils/request/utils/Cookies";
import { IS_TOKEN_AUTH } from "@/config/global";

NProgress.configure({ showSpinner: false });

const whiteList = ['/login', '/register'];

function initStorage() {
  // 持久化初始加载
  useAppStore();
}

router.beforeEach(async (to, from, next) => {
  initStorage();
  NProgress.start();
  if (CookiesUtils.get() || !IS_TOKEN_AUTH) {
    to.meta.title && useSettingsStore().setTitle(to.meta.title as string)
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (useUserStore().roles.length === 1) {
        try { // 判断当前用户是否已拉取完user_info信息
          let roles: string[] = useUserStore().roles;
          if (IS_TOKEN_AUTH) {
            const info: any = await useUserStore().getInfo();
            roles = info.roles;
          }
          usePermissionStore().generateRoutes(roles).then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            accessRoutes.forEach(route => {
              if (!isHttp(route.path)) {
                router.addRoute(route as RouteRecordRaw) // 动态添加可访问路由表
              }
            })
            if (!router.hasRoute(to.name!)) {
              next({ ...to, replace: true }); // hack方法 确保addRoutes已完成
            } else {
              next();
            }
          })
        } catch (err: any) {
          useUserStore().logOut().then(() => {
            ElMessage.error(err)
            next({path: '/'})
          })
        }
      } else {
        next()
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
