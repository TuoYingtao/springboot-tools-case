/**
 * Request 请求基类 一般不做修改
 *
 * @Author: TuoYingtao
 * @Date: 2023-11-02 14:45:33
 * @Version: v1.0.0
 */

const RESULT = { msg: '', code: '', data: [] } as unknown;

export abstract class IServiceApi<T extends BaseEntity, L extends BaseEntityList<T>> {

  /**
   * 列表
   * @param param
   */
  list(param?: Params): Promise<Result<T[]>> {
    return Promise.resolve<Result<T[]>>(RESULT as Result<T[]>);
  }

  /**
   * 分页
   * @param param
   */
  page(param: Params): Promise<Result<L>> {
    return Promise.resolve<Result<L>>(RESULT as Result<L>);
  }

  /**
   * 详情
   * @param id
   */
  detail(id: Params): Promise<Result<T>> {
    return Promise.resolve<Result<T>>(RESULT as Result<T>);
  }

  /**
   * 保存
   * @param entity
   */
  save(entity: T): Promise<Result<T>> {
    return Promise.resolve<Result<T>>(RESULT as Result<T>);
  }

  /**
   * 修改
   * @param entity
   */
  update(entity: T): Promise<Result<T>> {
    return Promise.resolve<Result<T>>(RESULT as Result<T>);
  }

  /**
   * 删除
   * @param ids
   */
  delete(ids: Params): Promise<Result> {
    return Promise.resolve<Result>(RESULT as Result);
  }

  /**
   * 排序
   * @param id
   * @param param
   */
  sort(id: Params, param: Params): Promise<Result> {
    return Promise.resolve<Result>(RESULT as Result);
  }

}
