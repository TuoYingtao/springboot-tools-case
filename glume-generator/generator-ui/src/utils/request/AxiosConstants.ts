export const AxiosFieldToken = 'Authorization';

export const CookieCacheFieldToken = 'Admin-Token';

export const AuthScheme = 'Bearer'

export const CacheConstants = {
  SESSION_OBJ: 'sessionObj',
}

export enum AxiosUrlPrefixEnum {
  DEFAULT = '/generator',
  SYSTEM = '/system',
  ADMIN = '/admin',
}

export enum HttpMethodsEnum {
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUt',
  DELETE = 'DELETE',
  PATCH = 'PATCH',
}

export enum ContentTypeEnum {
  FORM_DATA= 'application/x-www-form-urlencoded;charset=UTF-8',
  JSON = 'application/json;charset=utf-8',
  ZIP = 'application/zip',
  MULTIPART = 'multipart/form-data;charset=utf-8',
  OCTET_STREAM = 'application/octet-stream;charset=UTF-8',
}
