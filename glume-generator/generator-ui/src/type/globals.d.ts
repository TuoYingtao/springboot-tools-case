// 通用声明
declare type ClassName = { [className: string]: any } | ClassName[] | string;

declare type Recordable<T = any> = Record<string, T>;

// Vue
declare module '*.vue' {
  import { DefineComponent } from 'vue';

  const component: DefineComponent<{}, {}, any>;
  export default component;
}

declare module '*.svg' {
  const CONTENT: string;
  export default CONTENT;
}

// vite plugins js导入问题解决
declare module '*.js' {
  import { createVitePlugins } from '../vite/plugins/index.js';

  const VITE_PLUGINS: createVitePlugins
  export default VITE_PLUGINS;
}

declare module "*.scss" {
  const classes: { readonly [key: string]: string };
  export default classes;
}

declare module "*.less" {
  const classes: { readonly [key: string]: string };
  export default classes;
}
