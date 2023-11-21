import { IServiceApi } from "@/api/abstract/IServiceApi";
import { FieldTypeEntity, FieldTypeEntityList } from "@/api/generator/models/FieldTypeEntity";
import request from "@/utils/request/index";

export class FieldTypeApiService extends IServiceApi<FieldTypeEntity, FieldTypeEntityList> {


  list(param?: Params): Promise<Result<FieldTypeEntity[]>> {
    return request.get<Result<FieldTypeEntity[]>>({
      url: '/field_type/list',
      params: param,
    });
  }

  delete(ids: Params): Promise<Result> {
    return request.delete<Result>({
      url: '/field_type',
      params: ids,
    });
  }

  detail(id: Params): Promise<Result<FieldTypeEntity>> {
    return request.get<Result<FieldTypeEntity>>({
      url: `/field_type/${id}`,
    });
  }

  page(param: Params): Promise<Result<FieldTypeEntityList>> {
    return request.get<Result<FieldTypeEntityList>>({
      url: '/field_type/page',
      params: param,
    });
  }

  save(entity: FieldTypeEntity): Promise<Result<FieldTypeEntity>> {
    return request.post<Result<FieldTypeEntity>>({
      url: '/field_type',
      params: entity,
    });
  }

  update(entity: FieldTypeEntity): Promise<Result<FieldTypeEntity>> {
    return request.put<Result<FieldTypeEntity>>({
      url: '/field_type',
      params: entity,
    });
  }

}
