import axios, { AxiosInstance, AxiosResponse, AxiosError, AxiosRequestConfig, AxiosInterceptorManager } from 'axios';
import { stringify } from 'qs';
import isFunction from 'lodash/isFunction';
import cloneDeep from 'lodash/cloneDeep';
import { AxiosCanceler } from './AxiosCancel';
import { ContentTypeEnum, HttpMethodsEnum } from "@/utils/request/AxiosConstants";
import { AxiosTransformImpl } from "@/utils/request/AxiosTransform";
import { Log } from "@/utils/request/utils/Descriptors";
import {
  IAxiosRequestConfig,
  AxiosResponseError,
  AxiosResponseResult,
  AxiosTransform,
  BaseResponseResult,
  RequestConfigWithOptional,
  AxiosOptionsConfig
} from '@/type/axios';

/**
 * Axios模块
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:10:19
 * @Version: v1.0.0
*/
export class VAxios {

  /** axios句柄 */
  private instance: AxiosInstance;

  /** axios选项 */
  private readonly options: IAxiosRequestConfig;

  /** 请求拦截器 */
  private REQUEST_INTERCEPTORS: number | undefined;

  /** 响应拦截器 */
  private RESPONSE_INTERCEPTORS: number | undefined;

  constructor(options: IAxiosRequestConfig) {
    this.options = options;
    this.instance = axios.create(options);
    const useInterceptors = this.setupInterceptors();
    if (useInterceptors) {
      const { useRequestInterceptor, useResponseInterceptor} = useInterceptors;
      this.REQUEST_INTERCEPTORS = useRequestInterceptor;
      this.RESPONSE_INTERCEPTORS = useResponseInterceptor;
    }
  }

  /** 创建axios实例 */
  private createAxios(options: IAxiosRequestConfig): VAxios {
    this.instance = axios.create(options);
    return this;
  }

  /** 获取axios中央控制器 */
  private getTransform() {
    const { transform } = this.options;
    return transform;
  }

  /** 获取axios实例 */
  getAxios(): AxiosInstance {
    return this.instance;
  }

  /** 配置axios */
  configAxios(options: IAxiosRequestConfig): VAxios {
    return this.createAxios(options);
  }

  /** 设置通用头信息 */
  setHeader(headers: Record<string, string>): VAxios {
    Object.assign(this.instance.defaults.headers, headers);
    return this;
  }

  /** 删除请求拦截器 */
  removeRequestInterceptors(): VAxios {
    if (this.REQUEST_INTERCEPTORS) {
      this.removeInterceptors(this.instance.interceptors.request, this.REQUEST_INTERCEPTORS);
    }
    return this;
  }

  /** 删除响应拦截器 */
  removeResponseInterceptors(): VAxios {
    if (this.RESPONSE_INTERCEPTORS) {
      this.removeInterceptors(this.instance.interceptors.response, this.RESPONSE_INTERCEPTORS);
    }
    return this;
  }

  /** 删除拦截器 */
  private removeInterceptors(axiosInterceptors: AxiosInterceptorManager<RequestConfigWithOptional>, interceptors: number) {
    axiosInterceptors.eject(interceptors);
  }

  /** 设置拦截器 */
  private setupInterceptors() {
    const transform = this.getTransform();
    if (!transform) return;
    const { requestInterceptors, requestInterceptorsCatch, responseInterceptors, responseInterceptorsCatch } =
      transform;
    const axiosCanceler = new AxiosCanceler();
    // 请求前处理
    const beforeRequestHandler = (config: RequestConfigWithOptional) => {
      // @ts-ignore
      const { headers: { ignoreRepeatRequest } } = config;
      const ignoreRepeat = ignoreRepeatRequest ?? this.options.requestOptions?.ignoreRepeatRequest;
      if (!ignoreRepeat) axiosCanceler.addPending(config);
      if (requestInterceptors && isFunction(requestInterceptors)) {
        config = requestInterceptors(config, this.options);
      }
      return config;
    };
    // 请求前错误处理
    const beforeRequestCatchHandler = (error: AxiosError) =>
        requestInterceptorsCatch && isFunction(requestInterceptorsCatch) ? requestInterceptorsCatch(error) : undefined;
    // 设置请求前拦截器
    const useRequestInterceptor = this.instance.interceptors.request.use(beforeRequestHandler, beforeRequestCatchHandler);

    // 响应结果处理
    const responseHandler = (res: AxiosResponse) => {
      if (res) axiosCanceler.removePending(res.config);
      if (responseInterceptors && isFunction(responseInterceptors)) {
        return responseInterceptors(res, this.options.requestOptions);
      }
      return res;
    };
    // 响应错误处理
    const responseCatchHandler = (error: AxiosError) =>
        responseInterceptorsCatch && isFunction(responseInterceptorsCatch) ? responseInterceptorsCatch(error) : undefined;
    // 响应结果处理
    const useResponseInterceptor = this.instance.interceptors.response.use(responseHandler, responseCatchHandler);
    return { useRequestInterceptor, useResponseInterceptor };
  }

  /** 支持Form Data */
  supportFormData(config: RequestConfigWithOptional) {
    const headers = config.headers || this.options.headers;
    const contentType = headers?.['Content-Type'] || headers?.['content-type'];
    if (contentType !== ContentTypeEnum.FORM_DATA
        || !Reflect.has(config, 'data')
        || config.method?.toUpperCase() === HttpMethodsEnum.GET) {
      return config;
    }
    return {
      ...config,
      data: stringify(config.data, { arrayFormat: 'brackets' }),
    };
  }

  @Log
  get<R = BaseResponseResult>(config: RequestConfigWithOptional, options?: AxiosOptionsConfig): Promise<R> {
    return this.request<R>({ ...config, method: HttpMethodsEnum.GET }, options);
  }

  @Log
  post<R = BaseResponseResult>(config: RequestConfigWithOptional, options?: AxiosOptionsConfig): Promise<R> {
    return this.request<R>({ ...config, method: HttpMethodsEnum.POST }, options);
  }

  @Log
  put<R = BaseResponseResult>(config: RequestConfigWithOptional, options?: AxiosOptionsConfig): Promise<R> {
    return this.request<R>({ ...config, method: HttpMethodsEnum.PUT }, options);
  }

  @Log
  delete<R = BaseResponseResult>(config: RequestConfigWithOptional, options?: AxiosOptionsConfig): Promise<R> {
    return this.request<R>({ ...config, method: HttpMethodsEnum.DELETE }, options);
  }

  @Log
  patch<R = BaseResponseResult>(config: RequestConfigWithOptional, options?: AxiosOptionsConfig): Promise<R> {
    return this.request<R>({ ...config, method: HttpMethodsEnum.PATCH }, options);
  }

  /** 请求 */
  @Log
  async request<R = BaseResponseResult>(config: RequestConfigWithOptional, options?: AxiosOptionsConfig): Promise<R> {
    let conf = cloneDeep(config);
    const transform = this.getTransform();
    const { requestOptions } = this.options;
    const opt: AxiosOptionsConfig = { ...requestOptions, ...options };
    const { beforeRequestHook, requestCatchHook, transformResponseHook } = transform || {};
    if (beforeRequestHook && isFunction(beforeRequestHook)) {
      conf = beforeRequestHook(conf, opt);
    }
    conf = this.supportFormData(conf) as RequestConfigWithOptional;
    return new Promise((resolve, reject) => {
      this.instance
        .request<Result, AxiosResponse<Result>, RequestConfigWithOptional>((!config.retryCount ? conf : config) as AxiosRequestConfig)
        .then((res: AxiosResponseResult) => {
          if (transformResponseHook && isFunction(transformResponseHook)) {
            try {
              resolve(transformResponseHook(res, opt) as unknown as Promise<R>);
            } catch (err) {
              reject(err || new Error('请求错误!'));
            }
          }
          resolve(res as unknown as Promise<R>);
        })
        .catch((e: AxiosResponseError) => {
          if (axios.isAxiosError(e)) {
            // 在这里重写Axios的错误信息
          }
          if (requestCatchHook && isFunction(requestCatchHook)) {
            reject(requestCatchHook(e, opt));
          }
          reject(e);
        });
    });
  }
}

/**
 * Request 可选项配置实现类
 * 类中的属性是默认配置信息，不建议修改属性默认值，您可以通过setXXX(XXX)方法去设置
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:09:58
 * @Version: v1.0.0
*/
export class AxiosOptionsImpl implements AxiosOptionsConfig {
  apiUrl: string = 'http://localhost:8080';
  fieldToken: string = 'Authorization';
  formatDate: boolean = true;
  ignoreRepeatRequest: boolean = false;
  isJoinPrefix: boolean = false;
  isRepeatSubmit: boolean = false;
  isReturnNativeResponse: boolean = false;
  isTransformResponse: boolean = false;
  joinParamsToUrl: boolean = false;
  joinTime: boolean = false;
  retry: { count: number; delay: number } = {
    count: 3,
    delay: 1000
  };
  urlPrefix: string = '/api';
  withToken: boolean = false;
  isDebugger: boolean = false;

  setApiUrl(apiUrl: string): AxiosOptionsConfig {
    this.apiUrl = apiUrl;
    return this;
  }

  setFieldToken(fieldToken: string): AxiosOptionsConfig {
    this.fieldToken = fieldToken;
    return this;
  }

  setFormatDate(formatDate: boolean): AxiosOptionsConfig {
    this.formatDate = formatDate;
    return this;
  }

  setIgnoreRepeatRequest(ignoreRepeatRequest: boolean): AxiosOptionsConfig {
    this.ignoreRepeatRequest = ignoreRepeatRequest;
    return this;
  }

  setIsJoinPrefix(isJoinPrefix: boolean): AxiosOptionsConfig {
    this.isJoinPrefix = isJoinPrefix;
    return this;
  }

  setIsRepeatSubmit(isRepeatSubmit: boolean): AxiosOptionsConfig {
    this.isRepeatSubmit = isRepeatSubmit;
    return this;
  }

  setIsReturnNativeResponse(isReturnNativeResponse: boolean): AxiosOptionsConfig {
    this.isReturnNativeResponse = isReturnNativeResponse;
    return this;
  }

  setIsTransformResponse(isTransformResponse: boolean): AxiosOptionsConfig {
    this.isTransformResponse = isTransformResponse;
    return this;
  }

  setJoinParamsToUrl(joinParamsToUrl: boolean): AxiosOptionsConfig {
    this.joinParamsToUrl = joinParamsToUrl;
    return this;
  }

  setJoinTime(joinTime: boolean): AxiosOptionsConfig {
    this.joinTime = joinTime;
    return this;
  }

  setRetry(retry: { count: number; delay: number }): AxiosOptionsConfig {
    this.retry = retry;
    return this;
  }

  setUrlPrefix(urlPrefix: string): AxiosOptionsConfig {
    this.urlPrefix = urlPrefix;
    return this;
  }

  setWithToken(withToken: boolean): AxiosOptionsConfig {
    this.withToken = withToken;
    return this;
  }

  setIsDebugger(isDebugger: boolean): AxiosOptionsConfig {
    this.isDebugger = isDebugger;
    return this;
  }
}

/**
 * Axios 配置项实现类
 * 类中的属性是默认配置信息，不建议修改属性默认值，您可以通过setXXX(XXX)方法去设置
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:09:25
 * @Version: v1.0.0
*/
export class AxiosOptionsConfigImpl implements IAxiosRequestConfig {
  authenticationScheme: string = 'Bearer';
  headers: Record<string, string> = { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' };
  requestOptions: AxiosOptionsConfig = new AxiosOptionsImpl();
  timeout: number = 10 * 1000;
  transform: AxiosTransform = new AxiosTransformImpl();
  withCredentials: boolean = false;

  setAuthenticationScheme(authenticationScheme: string): IAxiosRequestConfig {
    this.authenticationScheme = authenticationScheme;
    return this;
  }

  setHeaders(headers: Record<string, string>): IAxiosRequestConfig {
    this.headers = headers;
    return this;
  }

  setRequestOptions(requestOptions: AxiosOptionsConfig): IAxiosRequestConfig {
    this.requestOptions = requestOptions;
    return this;
  }

  setTimeout(timeout: number): IAxiosRequestConfig {
    this.timeout = timeout;
    return this;
  }

  setTransform(transform: AxiosTransform): IAxiosRequestConfig {
    this.transform = transform;
    return this;
  }

  setWithCredentials(withCredentials: boolean): IAxiosRequestConfig {
    this.withCredentials = withCredentials;
    return this;
  }
}
