import Layout from '@/layout/index.vue';

export default [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/common/redirect/index.vue'),
      },
    ],
  },
  {
    path: '/login',
    component: () => import('@/views/logins/login.vue'),
    hidden: true,
  },
  {
    path: '/register',
    component: () => import('@/views/logins/register.vue'),
    hidden: true,
  },
  {
    path: '/:pathMatch(.*)*',
    component: () => import('@/views/common/error/404.vue'),
    hidden: true,
  },
] as LayoutRoutes[];
