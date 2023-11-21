import { FormRules } from "element-plus";
import { ProjectModifyEntity } from "@/api/generator/models/ProjectModifyEntity";

interface IData {
  form: ProjectModifyEntity;
  queryParams: Record<string, any>;
  rules: FormRules;
}

export const DEFAULT_FORM: ProjectModifyEntity = {
  id: -1,
  projectName: '',
  projectCode: '',
  projectPackage: '',
  projectPath: '',
  modifyProjectName: '',
  modifyProjectCode: '',
  modifyProjectPackage: '',
  exclusions: '',
  modifySuffix: '',
  modifyTmpPath: '',
}

export const DATA: IData = {
  form: DEFAULT_FORM,
  queryParams: {
    pageNum: 1,
    limit: 10,
  },
  rules: {
    code: [{ required: true, message: "基类编码不能为空", trigger: "blur" }],
    fields: [{ required: true, message: "字段不能为空", trigger: "blur" }],
    packageName: [{ required: true, message: "包名不能为空", trigger: "blur" }]
  }
}

export interface Props {
  title: string;
  formData: ProjectModifyEntity;
}
