import { saveAs } from 'file-saver';
import { AxiosOptionsConfigImpl, VAxios } from '@/utils/request/Axios';
import { IAxiosRequestConfig, AxiosOptionsConfig, AxiosResponseResult, RequestConfigWithOptional } from '@/type/axios';
import * as AxiosConstants from './AxiosConstants';
import { HttpMethodsEnum } from './AxiosConstants';

const FILE_NAME_KEY = 'download-filename';

/**
 * 下载方法
 */
export function downloadGet(
  url: string,
  params?: Params,
  fileName?: string,
  options?: AxiosOptionsConfig,
): Promise<void> {
  return download(HttpMethodsEnum.GET, url, params, fileName, options);
}

/**
 * 下载方法
 */
export function downloadPost(
  url: string,
  params?: Params,
  fileName?: string,
  options?: AxiosOptionsConfig,
): Promise<void> {
  return download(HttpMethodsEnum.POST, url, params, fileName, options);
}

/**
 * 下载方法
 */
async function download(
  method: string,
  url: string,
  params?: Params,
  fileName?: string,
  options?: AxiosOptionsConfig,
): Promise<void> {
  console.log(`正在下载数据，请稍后`);
  // 初始化请求对象
  const axiosOptionsConfig: IAxiosRequestConfig = new AxiosOptionsConfigImpl();
  axiosOptionsConfig.authenticationScheme = AxiosConstants.AuthScheme;
  axiosOptionsConfig.responseType = 'blob';
  const requestInstance = new VAxios(axiosOptionsConfig);
  // 处理请求参数
  options = { ...options, isReturnNativeResponse: true };
  const config = {
    url: url,
    method: method,
    params: params,
    headers: { [FILE_NAME_KEY]: fileName || 'download.zip' },
  } as RequestConfigWithOptional;
  // 发起请求
  try {
    let result = await requestInstance.request<AxiosResponseResult<BlobPart>>(config, options);
    if (result.request.responseType === 'blob' && result.request.type !== 'application/json') {
      const blob = new Blob([result.data], { type: 'application/zip' });
      saveAs(blob, result.config.headers![FILE_NAME_KEY] as string);
    }
  } catch (err) {
    console.error('下载文件出现错误', err);
  }
}
