export interface DatasourceEntity extends BaseEntity {
  id?: number;
  dbType: string;
  connName: string;
  connUrl: string;
  username: string;
  password: string;
}

export type DatasourceEntityList<T extends typeof BaseEntity = DatasourceEntity> = BaseEntityList<T>;

export interface DBTableEntity {
  tableName: string;
  tableComment: string;
  datasourceId: number;
}
