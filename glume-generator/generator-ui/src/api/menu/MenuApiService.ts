import { IServiceApi } from '@/utils/request/abstract/IServiceApi';
import { MenuEntity, MenuEntityList } from '@/api/menu/models/MenuEntity';
import request from '@/utils/request/index';

export class MenuApiService extends IServiceApi<MenuEntity, MenuEntityList> {
  /**
   * 动态路由菜单
   */
  getRouters(): Promise<Result<MenuEntityList>> {
    return request.get<Result<MenuEntityList>>({
      url: '/menu/list',
    });
  }
}
