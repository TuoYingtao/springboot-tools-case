import { AxiosError, AxiosResponse } from "axios";
import isString from "lodash/isString";
import { AxiosTransform, IAxiosRequestConfig, AxiosOptionsConfig, RequestConfigWithOptional, AxiosResponseResult, BaseResponseResult } from "@/type/axios";
import type { AxiosResponseError } from "@/type/axios";
import router from "@/router";
import { AxiosCanceler } from "@/utils/request/AxiosCancel";
import { HttpMethodsEnum, CacheConstants } from "@/utils/request/AxiosConstants";
import {
  formatRequestDate,
  getPropertyNames,
  getPropertyType,
  joinTimestamp,
  setObjToUrlParams
} from "@/utils/request/utils";
import request from "@/utils/request/index";
import { CookiesUtils } from "@/utils/request/utils/Cookies";
import Cache from '@/utils/request/utils/Cache';
import { ElMessage } from "element-plus";
import { Log } from "@/utils/request/utils/Descriptors";

const axiosCanceler = new AxiosCanceler();

const resultKey = {
  codeKey: '',
  resultKey: '',
  messageKey: '',
}

/** 动态获取响应体属性字段名 */
function resultKeyHandler(data: Result) {
  if (resultKey.codeKey == '' || resultKey.resultKey == '' || resultKey.messageKey == '') {
    // @ts-ignore
    const propertyNames = getPropertyNames<Result>(data);
    for (let key of propertyNames) {
      const propertyKey = getPropertyType(data, key);
      if (typeof propertyKey == 'number') {
        resultKey.codeKey = key as string;
      }
      if (typeof propertyKey == 'string') {
        resultKey.messageKey = key as string;
      }
      if (typeof propertyKey == 'object') {
        resultKey.resultKey = key as string;
      }
    }
  }
  return resultKey;
}

/**
 * Axios 中央控制器
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:17:39
 * @Version: v1.0.0
*/
export class AxiosTransformImpl implements AxiosTransform {

  @Log
  beforeRequestHook(config: RequestConfigWithOptional, options: AxiosOptionsConfig): RequestConfigWithOptional {
    const { apiUrl, isJoinPrefix, urlPrefix, joinParamsToUrl, formatDate, joinTime = true } = options;
    // 添加接口前缀
    if (isJoinPrefix) {
      config.url = `${urlPrefix}${config.url}`;
    }
    // 将baseUrl拼接
    if (apiUrl && isString(apiUrl)) {
      config.url = `${apiUrl}${config.url}`;
    }
    const params = config.params || {};
    const data = config.data || {};

    if (formatDate && data && !isString(data)) {
      formatRequestDate(data);
    }
    if (config.method?.toUpperCase() === HttpMethodsEnum.GET) {
      if (!isString(params)) {
        // 给 get 请求加上时间戳参数，避免从缓存中拿数据。
        config.params = Object.assign(params || {}, joinTimestamp(joinTime, false));
      } else {
        // 兼容restful风格
        config.url = `${config.url + params}${joinTimestamp(joinTime, true)}`;
        config.params = undefined;
      }
    } else if (!isString(params)) {
      if (formatDate) {
        formatRequestDate(params);
      }
      if (Reflect.has(config, 'data') && config.data && Object.keys(config.data).length > 0) {
        config.data = data;
        config.params = params;
      } else {
        // 非GET请求如果没有提供data，则将params视为data
        config.data = params;
        config.params = undefined;
      }
      if (joinParamsToUrl) {
        config.url = setObjToUrlParams(config.url as string, Object.assign({}, config.params, config.data));
      }
    } else {
      // 兼容restful风格
      config.url += params;
      config.params = undefined;
    }
    return config;
  }

  @Log
  requestInterceptors(config: RequestConfigWithOptional, options: IAxiosRequestConfig): RequestConfigWithOptional {
    // jwt token
    const token = CookiesUtils.get();
    if (token && config?.requestOptions?.withToken !== false) {
      config.headers = config.headers ? config.headers : {};
      const fieldToken = options.requestOptions?.fieldToken || 'Authorization';
      config.headers[fieldToken] = options.authenticationScheme
          ? `${options.authenticationScheme} ${token}`
          : token;
    }
    // 请求防重提交
    if (options.requestOptions.isRepeatSubmit
        && (config.method?.toUpperCase() === HttpMethodsEnum.POST || config.method?.toUpperCase() === HttpMethodsEnum.PUT)) {
      // 请求重试放过，不做放重提交
      if (config.retryCount !== undefined) return config;
      const requestObj = {
        url: config.url,
        data: Object.prototype.toString.call(Number(config.data)).slice(8, -1) === 'Object' ? JSON.stringify(config.data) : config.data,
        params: Object.prototype.toString.call(Number(config.params)).slice(8, -1) === 'Object' ? JSON.stringify(config.params) : config.params,
        time: new Date().getTime(),
      }
      // 请求数据大小
      const requestSize = Object.keys(JSON.stringify(requestObj)).length;
      const limitSize = 5 * 1024 * 1024;
      if (limitSize <= requestSize) {
        console.warn(`[${config.url}]: ` + '请求数据大小超出允许的5M限制，无法进行防重复提交验证。')
        return config;
      }
      const sessionObj = Cache.session.getJSON(CacheConstants.SESSION_OBJ);
      if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
        Cache.session.setJSON(CacheConstants.SESSION_OBJ, requestObj);
      } else {
        // 间隔时间(ms)，小于此时间视为重复提交
        let interval = 10000;
        if (requestObj.url === sessionObj.url
            && requestObj.data === sessionObj.data
            && requestObj.params === sessionObj.params
            && requestObj.time - sessionObj.time < interval) {
          const message = '数据正在处理，请勿重复提交';
          console.warn(`[${sessionObj.url}]: ` + message)
          throw new Error(message)
        } else {
          Cache.session.setJSON(CacheConstants.SESSION_OBJ, requestObj)
        }
      }
    }
    return config;
  }

  @Log
  responseInterceptors(res: AxiosResponse, options: AxiosOptionsConfig): Promise<AxiosResponse> {
    if (!res.data) return Promise.reject(new Error('请求接口错误'));
    const { isTransformResponse } = options;
    if (isTransformResponse) return Promise.resolve(res);
    resultKeyHandler(res.data);
    const [code, msg] = [ res.data[resultKey.codeKey], res.data[resultKey.messageKey]];
    if (code === 401) {
      const cookieToken = CookiesUtils.getCooliesUtilsInstance();
      cookieToken.remove();
      axiosCanceler.removeAllPending();
      router.push('/login');
      return Promise.reject(new Error(msg))
    }
    if (code === 500) {
      return Promise.reject(new Error(msg))
    }
    return Promise.resolve(res);
  }

  @Log
  transformResponseHook(res: AxiosResponseResult, options: AxiosOptionsConfig): BaseResponseResult {
    if (!res.data) throw new Error('请求接口错误');
    const { isTransformResponse, isReturnNativeResponse } = options;
    // 是否返回原生响应头 比如：需要获取响应头时使用该属性
    if (isReturnNativeResponse) return res;
    // 如果204无内容直接返回
    const method = res.config.method?.toUpperCase();
    if (res.status === 204 || method === HttpMethodsEnum.PATCH) return res.data;
    if (res.data && res.request.responseType === 'blob') return res.data;
    //  这里 code为 后台统一的字段，需要在 axios.d.ts 内修改为项目自己的接口返回格式
    resultKeyHandler(res.data);
    const [code, msg] = [ res.data[resultKey.codeKey], res.data[resultKey.messageKey]];
    // 不进行任何处理，直接返回 Response 数据 code，data，message 信息
    if (isTransformResponse) return res.data;
    // 这里逻辑可以根据项目进行修改
    const hasSuccess = res.data && code === 200;
    if (hasSuccess) {
      return res.data;
    }
    throw new Error(`${msg}`);
  }

  @Log
  async responseInterceptorsCatch(error: AxiosError): Promise<AxiosResponseError> {
    if (error.message == "Network Error") {
      error.message = "后端接口连接异常";
    } else if (error.message.includes("timeout")) {
      error.message = "系统接口请求超时";
    } else if (error.message.includes("Request failed with status code")) {
      error.message = "系统接口" + error.message.substring(error.message.length - 3) + "异常";
    }
    const {config} = error as AxiosResponseError;
    if (!config || !config.requestOptions?.retry) return Promise.reject(error);
    config.retryCount = config.retryCount || 0;
    if (config.retryCount >= config.requestOptions.retry.count) return Promise.reject(error);
    config.retryCount += 1;
    const backoff = new Promise((resolve) => {
      setTimeout(() => {
        resolve(config);
      }, config.requestOptions?.retry?.delay || 1500);
    });
    let backoffConfig = (await backoff) as RequestConfigWithOptional;
    return await request.request(backoffConfig, backoffConfig.requestOptions);
  }

  @Log
  requestCatchHook(error: AxiosResponseError, options: AxiosOptionsConfig): AxiosResponseError {
    if (error.config?.retryCount && error.config.retryCount === (options.retry?.count || 3)) {
      error.config.retryCount += 1;
      ElMessage({ message: error.message, type: 'error', duration: 5 * 1000 })
    }
    return error;
  }

  @Log
  requestInterceptorsCatch(error: AxiosError): Promise<AxiosError> {
    console.error('[requestInterceptorsCatch]', error);
    return Promise.resolve(error);
  }

}
