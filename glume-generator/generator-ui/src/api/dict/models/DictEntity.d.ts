export interface DictEntity extends BaseEntity {
  dictLabel: string;
  dictValue: string;
  listClass: string;
  cssClass: string;
}

export type DictEntityList = BaseEntityList<DictEntity>;
