import type { RouteMeta, RouteRecordRaw } from 'vue-router';
import type { DefineComponent } from 'vue';
import { RouteLocationNormalizedLoaded } from 'vue-router';

declare global {
  /** 侧边栏配置 */
  type Sidebar = {
    /** 侧边栏状态 */
    opened: boolean;
    /** 动画效果 */
    withoutAnimation: boolean;
    /** 隐藏状态 */
    hide: boolean;
  };

  /** 应用程序 */
  type App = {
    /** 侧边栏配置 */
    sidebar: Sidebar;
    /** 设备名称 */
    device: string;
    /** 全局样式大小 */
    size: string;
  };

  /** 框架系统设置 */
  type Settings = {
    /** 网页标题 */
    title: string;
    /** 主题颜色 默认：#409EFF */
    theme: string;
    /** 侧边栏主题 深色主题theme-dark，浅色主题theme-light 默认：深色主题 */
    sideTheme: string;
    /** 是否系统布局配置 */
    showSettings: boolean;
    /** 顶部导航栏状态 */
    topNav: boolean;
    /** 标签状态 */
    tagsView: boolean;
    /** 固定头部状态 */
    fixedHeader: boolean;
    /** 侧边栏LOGO状态 */
    sidebarLogo: boolean;
    /** 动态标题 */
    dynamicTitle: string;
    /**
     * @type {string | array} 'production' | ['production', 'development']
     * @description Need show err logs component.
     * The default is only used in the production env
     * If you want to also use it in dev, you can pass ['production', 'development']
     */
    errorLog?: string;
    [settingsName: string]: any;
  };

  /** 权限 */
  type Permission = {
    /** 路由 */
    routes: LayoutRoutes[];
    /** 添加路由 */
    addRoutes: LayoutRoutes[];
    /** 默认路由 */
    defaultRoutes: LayoutRoutes[];
    /** topbar路由 */
    topbarRouters: LayoutRoutes[];
    /** 侧边栏路由 */
    sidebarRouters: LayoutRoutes[];
  };

  /** 路由配置项 */
  interface LayoutRoutes extends RouteRecordRaw {
    /** 路由路径 */
    path: string;
    /** 组件 */
    component: (() => Promise<typeof import('*.vue')>) | DefineComponent<{}, {}, any> | string | Promise;
    /** 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1 */
    hidden?: boolean;
    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     * 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
     * 若你想不管路由下面的 children 声明的个数都显示你的根路由
     * 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
     */
    alwaysShow?: boolean;
    /** 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击 */
    redirect?: string;
    /** 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题 */
    name?: string;
    /** 访问路由的默认传递参数 */
    query?: string;
    /** 访问路由的角色权限 */
    roles?: string[];
    /** 访问路由的菜单权限 */
    permissions?: string[];
    /** 元数据信息 */
    meta?: LayoutRouteMeta;
    /** 嵌套路由 */
    children?: LayoutRoutes[];
    /** 不显示子集路由 */
    noShowingChildren?: boolean;
  }

  /** 元数据信息 */
  interface LayoutRouteMeta extends RouteMeta {
    /** 如果设置为true，则不会被 <keep-alive> 缓存(默认 false) */
    noCache?: boolean;
    /** 设置该路由在侧边栏和面包屑中展示的名字 */
    title?: string;
    /** 设置该路由的图标，对应路径src/assets/icons/svg */
    icon?: string;
    /** 如果设置为false，则不会在breadcrumb面包屑中显示 */
    breadcrumb?: boolean;
    /** 当路由设置了该属性，则会高亮相对应的侧边栏。 值：高了的路由地址 */
    activeMenu?: string;
    /** tab标签是否固定 */
    affix?: boolean;
    /** 外链接 */
    link?: string;
  }

  /** 标签 */
  type TagsView = {
    /** 历史访问标签 */
    visitedViews: RouteLocationNormalizedLoaded[];
    /** 允许缓存页标签 */
    cachedViews: string[];
    /** 外联页标签 */
    iframeViews: RouteLocationNormalizedLoaded[];
  };

  /** 用户信息 */
  type User = {
    /** 令牌凭证 */
    token: string;
    /** 用户名 */
    name: string;
    /** 头像 */
    avatar: string;
    /** 角色 */
    roles: string[];
    /** 权限 */
    permissions: string[];
  };

  /** 字典数据 */
  type Dict = {
    /** 字典数据 */
    dict: DictMap[];
  };

  /** 字典数据映射 */
  type DictMap = {
    /** 字典数据 Key */
    key: string;
    /** 字典数据信息集合 */
    value: DictInfo[];
  };

  /** 字典数据信息 */
  type DictInfo = {
    /** 标题 */
    label: string;
    /** 值 */
    value: string;
    /** tag标签主题类型 */
    elTagType: string;
    /** tag标签Class名 */
    elTagClass: string;
  };
}
