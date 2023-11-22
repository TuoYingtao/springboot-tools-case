import { IServiceApi } from "@/utils/request/abstract/IServiceApi";
import { ProjectModifyEntity, ProjectModifyEntityList } from "@/api/generator/models/ProjectModifyEntity";
import request from "@/utils/request/index";

export class ProjectModifyApiService extends IServiceApi<ProjectModifyEntity, ProjectModifyEntityList> {

  page(param: Params): Promise<Result<ProjectModifyEntityList>> {
    return request.get<Result<ProjectModifyEntityList>>({
      url: '/project/page',
      params: param,
    });
  }

  detail(id: Params): Promise<Result<ProjectModifyEntity>> {
    return request.get<Result<ProjectModifyEntity>>({
      url: `/project/${id}`,
    });
  }

  save(entity: ProjectModifyEntity): Promise<Result<ProjectModifyEntity>> {
    return request.post<Result<ProjectModifyEntity>>({
      url: '/project',
      params: entity,
    });
  }

  update(entity: ProjectModifyEntity): Promise<Result<ProjectModifyEntity>> {
    return request.put<Result<ProjectModifyEntity>>({
      url: '/project',
      params: entity,
    });
  }

  delete(ids: Params): Promise<Result> {
    return request.delete<Result>({
      url: '/project',
      params: ids,
    });
  }

  download(id: Params): Promise<Result> {
    return request.get<Result>({
      url: `/project/download/${id}`,
    });
  }
}
