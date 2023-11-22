import { IServiceApi } from "@/utils/request/abstract/IServiceApi";
import { TableEntity, TableEntityList, TableField } from "@/api/generator/models/TableEntity";
import request from "@/utils/request/index";

export class TableApiService extends IServiceApi<TableEntity, TableEntityList> {

  page(param: Params): Promise<Result<TableEntityList>> {
    return request.get<Result<TableEntityList>>({
      url: '/table/page',
      params: param,
    });
  }

  detail(id: Params): Promise<Result<TableEntity>> {
    return request.get<Result<TableEntity>>({
      url: `/table/${id}`,
    });
  }

  save(entity: TableEntity): Promise<Result<TableEntity>> {
    return request.post<Result<TableEntity>>({
      url: '/table',
      params: entity,
    });
  }

  update(entity: TableEntity): Promise<Result<TableEntity>> {
    return request.put<Result<TableEntity>>({
      url: '/table',
      params: entity,
    });
  }

  delete(ids: Params): Promise<Result> {
    return request.delete<Result>({
      url: '/table',
      params: ids,
    });
  }

  sync(id: Params) {
    return request.post<Result<string>>({
      url: `/table/sync/${id}`,
    });
  }

  importTable(dataSourceId: Params, tables: String[]) {
    return request.post<Result<string>>({
      url: `/table/import/${dataSourceId}`,
      params: tables,
    })
  }

  updateTableField(tableFields: TableField[]) {
    return request.put<Result<string>>({
      url: `/table/field/batch/update`,
      params: tableFields,
    })
  }
}
