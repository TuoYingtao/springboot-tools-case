export interface FieldTypeEntity extends BaseEntity {
  id?: number;
  columnType: string;
  attrType: string;
  packageName: string;
}

export type FieldTypeEntityList<T extends typeof BaseEntity = FieldTypeEntity> = BaseEntityList<T>;
