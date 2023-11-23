import merge from 'lodash/merge';
import { AxiosOptionsConfigImpl, VAxios } from './Axios';
import { IAxiosRequestConfig } from '@/type/axios';
import { AxiosTransformImpl } from '@/utils/request/AxiosTransform';
import * as AxiosConstants from '@/utils/request/AxiosConstants';

const VITE = import.meta.env;
// 如果是mock模式 或 没启用直连代理 就不配置host 会走本地Mock拦截 或 Vite 代理
const host = VITE.VITE_REQUEST_PROXY !== 'true' ? '' : VITE.VITE_APP_BASE_API;

export const config: IAxiosRequestConfig = merge(<AxiosOptionsConfigImpl>new AxiosOptionsConfigImpl(), {
  authenticationScheme: AxiosConstants.AuthScheme,
  timeout: 10 * 1000,
  withCredentials: false,
  headers: { 'Content-Type': AxiosConstants.ContentTypeEnum.JSON },
  transform: new AxiosTransformImpl(),
  requestOptions: {
    apiUrl: host,
    isJoinPrefix: true,
    urlPrefix: AxiosConstants.AxiosUrlPrefixEnum.DEFAULT,
    isReturnNativeResponse: false,
    isTransformResponse: false,
    joinParamsToUrl: false,
    formatDate: true,
    joinTime: true,
    ignoreRepeatRequest: true,
    withToken: true,
    fieldToken: AxiosConstants.AxiosFieldToken,
    isRepeatSubmit: true,
    retry: {
      count: 3,
      delay: 1000,
    },
    isDebugger: true,
  },
} as IAxiosRequestConfig);

function createAxios(opt?: Partial<IAxiosRequestConfig>) {
  const vAxios = new VAxios(merge(<IAxiosRequestConfig>config, opt || {}));
  return new VAxios(merge(<IAxiosRequestConfig>config, opt || {}));
}

const request = createAxios();
export default request;
