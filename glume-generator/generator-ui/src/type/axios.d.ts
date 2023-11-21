import { AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios';

declare global {

  /** 请求参数类型 */
  type Params = number | string | boolean | Record<string, any> | Recordable
      | number[] | string[] | boolean[] | Record<string, any>[] | Recordable[];

  /** FormData 类型 */
  type FormDataParams = Record<any, any> | Recordable;

  /** 分页 */
  interface PageInfo {
    /** 总页数 */
    totalCount: number;
    /** 当前页数 */
    currPage: number;
    /** 上一页 */
    prePage: number;
    /** 下一页 */
    lastPage: number;
  }

  /** 基本实体类型 */
  interface BaseEntity {
    createTime?: unknown;
    updateTime?: unknown;
    [key: string]: any;
  }

  /** 基本分页列表实体类型 */
  interface BaseEntityList<T extends BaseEntity> extends PageInfo {
    list: T[];
  }

  /** 实体顶类型 */
  type Entity = BaseEntity | BaseEntityList<BaseEntity>;

  /** 响应实体 */
  interface Result<T extends typeof Entity = Entity> {
    code: number;
    data: T;
    msg: string;
  }

}

/**
 * 创建Axios选项
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:14:00
 * @Version: v1.0.0
*/
export interface IAxiosRequestConfig<D = FormDataParams> extends AxiosRequestConfig<D> {
  /**
   * 令牌前缀 默认：'Bearer'
   * https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication#authentication_schemes
   * */
  authenticationScheme?: string;
  /** 超时时间 默认：10S */
  timeout: number;
  /** 是否允许携带Cookie 默认：false */
  withCredentials: boolean;
  /** 请求头 默认：{ 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' } */
  headers: Record<string, string>;
  /** 数据处理 */
  transform?: AxiosTransform;
  /** 中央控制器配置选项 */
  requestOptions: AxiosOptionsConfig;
}

/**
 * Axios 中央控制器
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:14:06
 * @Version: v1.0.0
*/
export interface AxiosTransform {

  /** 请求前处理配置 Hook */
  beforeRequestHook?: (config: RequestConfigWithOptional, options: AxiosOptionsConfig) => RequestConfigWithOptional;

  /** 请求前的拦截器 */
  requestInterceptors?: (config: RequestConfigWithOptional, options: IAxiosRequestConfig) => RequestConfigWithOptional;

  /** 请求响应拦截器处理 */
  responseInterceptors?: (res: AxiosResponse, options: AxiosOptionsConfig) => Promise<AxiosResponse>;

  /** 转换前 Hook；处理请求数据。如果数据不是预期格式，可直接抛出错误 */
  transformResponseHook?: (res: AxiosResponseResult, options: AxiosOptionsConfig) => BaseResponseResult;

  /** 请求响应的拦截器错误处理 */
  responseInterceptorsCatch?: (error: AxiosError) => Promise<AxiosResponseError>;

  /** 请求失败处理 */
  requestCatchHook?: (e: AxiosResponseError, options: AxiosOptionsConfig) => AxiosResponseError;

  /** 请求前的拦截器错误处理 */
  requestInterceptorsCatch?: (error: AxiosError) => Promise<AxiosError>;
}

/**
 * Request 可选项配置
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:14:13
 * @Version: v1.0.0
*/
export interface AxiosOptionsConfig {
  /** 接口地址 默认：http://localhost:8080 */
  apiUrl?: string;
  /** 是否自动添加接口前缀 默认：false */
  isJoinPrefix?: boolean;
  /** 接口前缀 默认：/api */
  urlPrefix?: string;
  /** post请求的时候添加参数到url 默认：false */
  joinParamsToUrl?: boolean;
  /** 格式化提交参数时间 默认：true */
  formatDate?: boolean;
  /** 不进行任何处理，直接返回 Response 数据 默认：false */
  isTransformResponse?: boolean;
  /** 是否返回原生响应头 比如：需要获取响应头时使用该属性 默认：false */
  isReturnNativeResponse?: boolean;
  /** 是否加入时间戳 默认：false */
  joinTime?: boolean;
  /** 是否携带token 默认：false */
  withToken?: boolean;
  /** 令牌在Header头部的字段。 若未设置 fieldToken 则默认：Authorization */
  fieldToken?: string;
  /** 忽略重复请求 默认：false */
  ignoreRepeatRequest?: boolean;
  /** 是否需要防止数据重复提交 默认：false */
  isRepeatSubmit?: boolean;
  /** 重试 */
  retry?: {
    /** 重试次数 默认3次 */
    count: number;
    /** 重试间隔时间 1S */
    delay: number;
  };
  /** debugger模式 默认：false */
  isDebugger?: boolean;
}

/**
 * Axios 响应结果集
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:14:58
 * @Version: v1.0.0
*/
export type AxiosResponseResult<E = Result<Entity>> = AxiosResponse<E, RequestConfigWithOptional>;

/**
 * Axios 响应结果集的基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:15:25
 * @Version: v1.0.0
*/
export type BaseResponseResult<E = Result<Entity>> = AxiosResponse<E, RequestConfigWithOptional> | E;

/**
 * 请求配置信息
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:14:20
 * @Version: v1.0.0
*/
export interface RequestConfigWithOptional<D = FormDataParams> extends AxiosRequestConfig<D> {
  /** 重试次数 */
  retryCount?: number;
  /** 请求选项 */
  requestOptions?: AxiosOptionsConfig;
}

/**
 * Axios 请求异常响应信息
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:14:28
 * @Version: v1.0.0
*/
export class AxiosResponseError<T = Result<Entity>, D = FormDataParams> extends AxiosError<T, D> {
  /** 请求配置信息 */
  config?: RequestConfigWithOptional<D>;
  response?: AxiosResponseResult;
}
