import Layout from '@/layout/index.vue';
import { ROLE_DEFAULT } from '@/config/global';

// 静态路由
export default [
  {
    path: '/api_document',
    component: Layout,
    name: 'document',
    redirect: 'noRedirect',
    meta: { title: 'Api文档', icon: 'documentation', noCache: true },
    roles: [ROLE_DEFAULT],
    hidden: false,
    alwaysShow: false,
    children: [
      {
        path: 'index',
        component: () => import('@/views/tool/swagger/index.vue'),
        name: 'ApiDocument',
        meta: { title: '接口文档', icon: 'swagger', noCache: true, affix: false },
        hidden: false,
      },
    ],
  },
  {
    path: '/generator',
    component: Layout,
    name: 'Generator',
    redirect: 'noRedirect',
    meta: { title: '代码生成器', icon: 'code', noCache: true },
    roles: [ROLE_DEFAULT],
    hidden: false,
    alwaysShow: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/generator/table/index.vue'),
        name: 'GeneratorCode',
        meta: { title: '代码生成', icon: 'table', noCache: true, affix: false },
        hidden: false,
      },
      {
        path: 'project_modify',
        component: () => import('@/views/generator/projectModify/index.vue'),
        name: 'ProjectModify',
        meta: { title: '项目名变更', icon: 'edit', noCache: true, affix: false },
        hidden: false,
      },
      {
        path: 'base_class',
        component: () => import('@/views/generator/baseClass/index.vue'),
        name: 'BaseClass',
        meta: { title: '基类管理', icon: 'class', noCache: true, affix: false },
        hidden: false,
      },
      {
        path: 'field_type',
        component: () => import('@/views/generator/fieldType/index.vue'),
        name: 'FieldType',
        meta: { title: '字段类型映射', icon: 'list', noCache: true, affix: false },
        hidden: false,
      },
      {
        path: 'datasource',
        component: () => import('@/views/generator/datasource/index.vue'),
        name: 'Datasource',
        meta: { title: '数据源管理', icon: 'datasource', noCache: true, affix: false },
        hidden: false,
      },
    ],
  },
] as LayoutRoutes[];
