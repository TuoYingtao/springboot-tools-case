export type BaseClassEntityList<T extends typeof BaseEntity = BaseClassEntity> = BaseEntityList<T>;

export interface BaseClassEntity extends BaseEntity {
  id?: number;
  packageName: string;
  code: string;
  fields: string;
  remark: string;
  aa: unknown
}
