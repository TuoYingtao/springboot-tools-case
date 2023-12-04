import Layout from '@/layout/index.vue';

export default [
  {
    path: '',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: '/index',
        component: () => import('@/views/index.vue'),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true },
      },
    ],
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index.vue'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' },
      },
    ],
  },
  {
    path: '/result',
    name: 'Result',
    component: Layout,
    hidden: true,
    meta: { title: '结果页' },
    children: [
      {
        path: '/401',
        component: () => import('@/views/common/error/401.vue'),
        hidden: true,
      },
      {
        path: '/404',
        component: () => import('@/views/common/error/404.vue'),
        hidden: true,
      },
    ],
  },
] as LayoutRoutes[];
