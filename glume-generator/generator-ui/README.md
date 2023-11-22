# vue3-system-starter-template

##  项目结构

```txt
vue3-system-starter-template
├── public -- 公开文件
├── src -- 核心模块
├	├── api -- 业务请求
├	├── assets -- 静态资源
├	├── components -- 公共组件
├	├── config -- 配置文件
├	├── directive -- 自定义指令
├	├── enum -- 枚举类
├	├── hooks -- Hooks 工具
├	├── layout -- 框架布局组件
├	├── plugins -- 框架业务插件
├	├── router -- 路由菜单
├	├── stores -- 缓存工具
├	├── type -- 全局类型引用
├	├── utils -- 框架工具
├	├── views -- 视图页面
├	├── App.vue -- 视图主入口
├	├── main.ts -- 业务主入口
├	├── env.d.ts -- 系统环境类型
├	├── permission.ts -- 路由菜单权限转发
├	└── settings.ts -- 框架设置文件
├── vite -- vite 插件工具
├── .env -- 默认环境配置
├── .env.development -- 开发环境配置 （环境内的变量会覆盖.env中相同的变量）
├── .env.production -- 生成环境配置（环境内的变量会覆盖.env中相同的变量）
├── .env.staging -- 演示环境配置（环境内的变量会覆盖.env中相同的变量）
├── package.json -- npm 构建配置
├── tsconfig.json -- TS 配置文具
└── vite.config.ts -- vite 配置文件
```

## 如何使用

### 强类型

框架采用强类型，需要对TS有一定了解，全局类型引用配置在`type`文件夹下定义，用户在里面添加自己的全局类型。

* `globals.d.ts` 全局类型：配置了`vue`、`js`、`scss`、`less`、`svg`模块类型，防止插件工具无法识别，
* `axios.d.ts` axios 请求类型：请求响应体、请求参数、分页、实体基类等
* `layout.d.ts` 框架布局类型：路由菜单、系统设置等

### axios请求

一、在`utils.request`文件夹下封装了Axios请求工具，此工具对原有的`axios`做了方法增强、可以配置化请求。之所以要做方法增强是为了更好的处理Request的请求与响应，可配置化请求是对每一个API的定制跟灵活的请求服务端。

`AxiosTransform.ts`方法增强：

* `beforeRequestHook` 请求前置处理器：请求接口参数拼接。
* `requestInterceptors` 请求拦截器：为请求接口做相关的权限认证参数、请求防重校验。
* `responseInterceptors` 响应拦截器：过滤HTTP请求状态。
* `transformResponseHook` 响应后置处理器：对Request请求最终的响应体做处理。
* `responseInterceptorsCatch` 响应异常拦截器：HTTP请求异常重试。
* `requestCatchHook` 请求异常处理
* `requestInterceptorsCatch` 请求异常拦截处理

`IAxiosRequestConfig`配置化请求：所有API请求都可以定制自己的配置信息，详细信息请看`IAxiosRequestConfig`类。

二、如果你要做业务工具请求如下载文件、上传文件等工具，你只需要三步就能实现定制Request请求。

```ts
// 初始化请求配置类
const axiosOptionsConfig: IAxiosRequestConfig = new AxiosOptionsConfigImpl();
// 创建Reqeust实例对象 接收类型为IAxiosRequestConfig的请求配置参数
const requestInstance = new VAxios(axiosOptionsConfig);
// 使用requestInstance实例发起Reqeust请求
const result = await requestInstance.request<AxiosResponseResult<Result>>(config, options);
```

三、API 业务请求

所有API业务请求都需要继承`IServiceApi`抽象类，在这个抽象类中定义了常用的业务请求方法（`list、page、save、update、delete、sort`），并且对它们重写，设置请求配置信息。`IServiceApi`是泛型抽象类，默认接收的两个泛型类型是：`BaseEntity`、`BaseEntityList`。

所有API业务请求都需要定义响应体实体类，并且响应实体类都需要继承实体基类（`BaseEntity`）分页响应体需要继承`BaseEntityList`基类。

```ts
// Entity.d.ts
// 业务实体类
export interface BaseClassEntity extends BaseEntity {
  id?: number;
  packageName: string;
  code: string;
  fields: string;
  remark: string;
}
// 业务分页实体类
export type BaseClassEntityList<T extends typeof BaseEntity = BaseClassEntity> = BaseEntityList<T>;
```

```ts
// ApiService.ts
export class BaseClassApiService extends IServiceApi<BaseClassEntity, BaseClassEntityList> {
  // 列表
  list(param?: Params): Promise<Result<BaseClassEntity[]>> {
    return request.get<Result<BaseClassEntity[]>>({
      url: '/baseclass/list',
      params: param,
    });
  }   
  // 省略其它业务请求方法....
}
```

```ts
// view.vue
const baseClassApi = new BaseClassApiService();
baseClassApi.list();
```

**重要的：在实际开发中，不同项目的请求响应体也是不同的，可能项目A它的响应体是data作为主要字段，而项目B却以Result作为主要字段，还有就是分页的响应体也会略有不同，这时为了适配自己的项目，你需要在`axios.d.ts`全局类型文件修改`PageInfo、Result`接口 如下**

```typescript
// 项目A
interface Result<T extends typeof Entity = Entity> {
    code: number;
    data: T;
    msg: string;
}
interface PageInfo {
    totalCount: number;
    currPage: number;
    prePage: number;
    lastPage: number;
}
// 项目B
interface Result<T extends typeof Entity = Entity> {
    code: number;
    result: T;
    msg: string;
}
interface PageInfo {
    total: number;
    current: number;
	limit: number;
}
```

### 自动导入

本框架采用`unplugin-auto-import`、`unplugin-vue-components`、`unplugin-vue-setup-extend-plus`自动导入相关函数、组件，如：`ref`、`toRefs`、`onMounted`与`components`文件夹下的所有组件等。无需在手动`import`。

并且在`type`文件夹下生成两个文件，这两个文件的内容则是我们配置不需要手动`import`的工具。

*注意：并非所有的东西都是自动导入，若需配置自己的导入文件，请查看`vite`文件夹下的`auto-components.js`、`auto-import.js`、`auto-setup-extend.js`配置*

## Customize configuration

See [Vite Configuration Reference](https://vitejs.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Type-Check, Compile and Minify for Production

```sh
npm run build
```

### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
```
