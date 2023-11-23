import { IServiceApi } from '@/utils/request/abstract/IServiceApi';
import { LoginEntity, LoginInfoEntity } from '@/api/logins/models/LoginEntity';
import request from '@/utils/request/index';

export class LoginApiService extends IServiceApi<LoginEntity, any> {
  login(username: string, password: string, code: number, uuid: number): Promise<Result<LoginEntity>> {
    return request.get<Result<LoginEntity>>({
      url: '/login',
      params: { username, password, code, uuid },
    });
  }

  info(): Promise<Result<LoginInfoEntity>> {
    return request.get<Result<LoginInfoEntity>>({
      url: '/info',
    });
  }

  logout(params: Params): Promise<Result> {
    return request.get<Result>({
      url: '/logout',
      params: params,
    });
  }
}
