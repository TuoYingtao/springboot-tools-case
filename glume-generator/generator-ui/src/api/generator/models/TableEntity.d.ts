export interface TableEntity extends BaseEntity {
  id?: number;
  author?: string;
  backendPath: string;
  className: string;
  datasourceId: number;
  email: string;
  formLayout: number;
  frontendPath: string;
  functionName: string;
  generatorType: number;
  moduleName: string;
  packageName: string;
  tableComment: string;
  tableName: string;
  version: string;
  baseclassId?: number;
  enableBaseService: number;
  commonPackagePath: string;
  fieldList?: TableField[];
}
export interface TableField extends BaseEntity {
  attrName: string;
  attrType: string;
  autoFill: string;
  dateFill: string;
  dateFormat: string;
  timeZone: string;
  baseField: boolean;
  fieldComment: string;
  fieldName: string;
  fieldType: string;
  formDict: string;
  formItem: boolean;
  formRequired: boolean;
  formType: string;
  formValidator: string;
  gridItem: boolean;
  gridSort: boolean;
  id?: number;
  packageName: string;
  primaryPk: boolean;
  queryFormType: string;
  queryItem: boolean;
  queryType: string;
  sort: number;
  tableId: number;
}

export type TableEntityList<T extends typeof BaseEntity = TableEntity> = BaseEntityList<T>;
