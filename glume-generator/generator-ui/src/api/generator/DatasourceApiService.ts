import { IServiceApi } from "@/api/abstract/IServiceApi";
import { DatasourceEntity, DatasourceEntityList, DBTableEntity } from "@/api/generator/models/DatasourceEntity";
import request from "@/utils/request/index";

export class DatasourceApiService extends IServiceApi<DatasourceEntity, DatasourceEntityList> {

  list(param?: Params): Promise<Result<DatasourceEntity[]>> {
    return request.get<Result<DatasourceEntity[]>>({
      url: '/datasource/list',
      params: param,
    });
  }

  page(param: Params): Promise<Result<DatasourceEntityList>> {
    return request.get<Result<DatasourceEntityList>>({
      url: '/datasource/page',
      params: param,
    });
  }

  detail(id: Params): Promise<Result<DatasourceEntity>> {
    return request.get<Result<DatasourceEntity>>({
      url: `/datasource/${id}`,
    });
  }

  save(entity: DatasourceEntity): Promise<Result<DatasourceEntity>> {
    return request.post<Result<DatasourceEntity>>({
      url: '/datasource',
      params: entity,
    });
  }

  update(entity: DatasourceEntity): Promise<Result<DatasourceEntity>> {
    return request.put<Result<DatasourceEntity>>({
      url: '/datasource',
      params: entity,
    });
  }

  delete(ids: Params): Promise<Result> {
    return request.delete<Result>({
      url: '/datasource',
      params: ids,
    });
  }

  testConnect(id: Params) {
    return request.get<Result<string>>({
      url: `/datasource/test_db/connect/${id}`,
    });
  }

  tableList(id: Params) {
    return request.get<Result<DBTableEntity[]>>({
      url: `/datasource/table/list/${id}`,
    });
  }
}
