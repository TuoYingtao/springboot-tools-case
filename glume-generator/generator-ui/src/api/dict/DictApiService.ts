import { IServiceApi } from "@/utils/request/abstract/IServiceApi";
import { DictEntity, DictEntityList } from "@/api/dict/models/DictEntity";
import request from "@/utils/request/index";

export class DictApiService extends IServiceApi<DictEntity, DictEntityList> {

  getDicts(dictType: string): Promise<Result<DictEntityList>> {
    return request.get<Result<DictEntityList>>({
      url: '/dicts/list',
      params: dictType,
    })
  }
}
