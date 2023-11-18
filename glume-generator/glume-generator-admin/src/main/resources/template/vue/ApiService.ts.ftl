import { IServiceApi } from "@/api/abstract/IServiceApi";
import { ${ClassName}Entity, ${ClassName}EntityList } from "@/api/${className}/models/${ClassName}Entity";
import request from "@/utils/request/index";

export class ${ClassName}ApiService extends IServiceApi<${ClassName}Entity, ${ClassName}EntityList> {

  list(param?: Params): Promise<Result<${ClassName}Entity[]>> {
    return request.get<Result<${ClassName}Entity[]>>({
      url: '/${functionName}/list',
      params: param,
    });
  }

  delete(ids: Params): Promise<Result> {
    return request.delete<Result>({
      url: '/${functionName}',
      params: ids,
    });
  }

  detail(id: Params): Promise<Result<${ClassName}Entity>> {
    return request.get<Result<${ClassName}Entity>>({
      url: `/${functionName}/<#noparse>${id}</#noparse>`,
    });
  }

  page(param: Params): Promise<Result<${ClassName}EntityList>> {
    return request.get<Result<${ClassName}EntityList>>({
      url: '/${functionName}/page',
      params: param,
    });
  }

  update(entity: ${ClassName}Entity): Promise<Result<${ClassName}Entity>> {
    return request.put<Result<${ClassName}Entity>>({
      url: '/${functionName}',
      params: entity,
    });
  }

  save(entity: ${ClassName}Entity): Promise<Result<${ClassName}Entity>> {
    return request.post<Result<${ClassName}Entity>>({
      url: '/${functionName}',
      params: entity,
    });
  }
}
