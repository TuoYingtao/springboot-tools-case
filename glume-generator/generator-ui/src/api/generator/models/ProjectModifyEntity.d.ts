export interface ProjectModifyEntity extends BaseEntity {
  id?: number;
  projectName: string;
  projectCode: string;
  projectPackage: string;
  projectPath: string;
  modifyProjectName: string;
  modifyProjectCode: string;
  modifyProjectPackage: string;
  exclusions: string;
}

export type ProjectModifyEntityList<T extends typeof BaseEntity = ProjectModifyEntity > = BaseEntityList<T>;
