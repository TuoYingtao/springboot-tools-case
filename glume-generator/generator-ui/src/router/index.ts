import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

// 自动导入modules文件夹下所有ts文件
const modules: Record<string, any> = import.meta.glob('./modules/**/*.ts', { eager: true });

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes: LayoutRoutes[] = [];

// 系统工具页 如：首页、个人中心、401等
export const constantRoutes: LayoutRoutes[] = [];

// 静态路由页面
export const baseRoutes: LayoutRoutes[] = [];

Object.keys(modules).forEach((key) => {
  const mod = modules[key].default || {};
  const modList = Array.isArray(mod) ? [...mod] : [mod];
  const keyArray = key.match(/(?<=\/)[^/]+(?=\.ts$)/);
  if (keyArray) {
    if (keyArray[0] === 'Component' || keyArray[0] === 'Fixed') {
      constantRoutes.push(...modList);
    } else if (keyArray[0] === 'Dynamic') {
      dynamicRoutes.push(...modList);
    } else {
      baseRoutes.push(...modList);
    }
  }
});

const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_BASE_URL),
  routes: constantRoutes as RouteRecordRaw[],
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  },
});

export default router;
