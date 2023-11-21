import { FormRules } from "element-plus";
import { BaseClassEntity } from "@/api/generator/models/BaseClassEntity";

interface IData {
  form: BaseClassEntity;
  queryParams: Record<string, any>;
  rules: FormRules;
}

export const DEFAULT_FORM: BaseClassEntity = {
  id: -1,
  packageName: '',
  code: '',
  fields: '',
  remark: '',
}

export const DATA: IData = {
  form: DEFAULT_FORM,
  queryParams: {
    pageNum: 1,
    limit: 10,
  },
  rules: {
    code: [{ required: true, message: "基类编码不能为空", trigger: "blur" }],
    packageName: [{ required: true, message: "包名不能为空", trigger: "blur" }]
  }
}

export interface Props {
  title: string;
  formData: BaseClassEntity;
}
