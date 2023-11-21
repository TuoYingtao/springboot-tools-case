import Layout from '@/layout/index.vue';

export default [
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
        hidden: true
      },
      {
        path: '/404',
        component: () => import('@/views/common/error/404.vue'),
        hidden: true
      },
    ]
  },
] as LayoutRoutes[]
