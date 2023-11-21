import { IServiceApi } from "@/api/abstract/IServiceApi";
import request from "@/utils/request/index";

export class GeneratorApiService extends IServiceApi<BaseEntity, BaseEntityList<BaseEntity>> {

  download(tableIds: string) {
    return request.get({
      url: '/generator/download',
      params: { tableIds: tableIds },
    })
  }

  code(param: number[]) {
    return request.post<Result<string>>({
      url: '/generator/code',
      params: param,
    })
  }

  previewCode(tableId: number) {
    return request.get<Result<Record<string, string>>>({
      url: '/generator/preview',
      params: { tableId: tableId },
    })
  }
}
