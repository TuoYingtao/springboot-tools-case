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

  getCodeImg(): Promise<Result> {
    return request.get<Result>({
      url: '/code_image',
    });
  }

  register(params: Params): Promise<Result> {
    return request.post<Result>({
      url: '/register',
      params: params,
    });
  }

  getUserProfile(): Promise<Result> {
    return request.get<Result>({
      url: '/user_profile',
    });
  }

  updateUserPwd(params: Params): Promise<Result> {
    return request.put<Result>({
      url: '/update_user_pwd',
      params: params,
    });
  }

  uploadAvatar(params: Params): Promise<Result> {
    return request.put<Result>({
      url: '/upload_avatar',
      params: params,
    });
  }

  updateUserProfile(params: Params): Promise<Result> {
    return request.put<Result>({
      url: '/update_user_profile',
      params: params,
    });
  }
}
