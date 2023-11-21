import { FormRules } from "element-plus";
import { DatasourceEntity } from "@/api/generator/models/DatasourceEntity";

interface IData {
  form: DatasourceEntity;
  queryParams: Record<string, any>;
  rules: FormRules;
}

export const DEFAULT_FORM: DatasourceEntity = {
  id: -1,
  dbType: '',
  connName: '',
  connUrl: '',
  username: '',
  password: '',
}

export const DATA: IData = {
  form: DEFAULT_FORM,
  queryParams: {
    pageNum: 1,
    limit: 10,
  },
  rules: {
    dbType: [{ required: true, message: "数据库类型不能为空", trigger: "blur" }],
    connName: [{ required: true, message: "连接名不能为空", trigger: "blur" }],
    connUrl: [{ required: true, message: "数据库URL不能为空", trigger: "blur" }],
    username: [{ required: true, message: "用户名不能为空", trigger: "blur" }],
    password: [{ required: true, message: "密码不能为空", trigger: "blur" }],
  }
}

export interface Props {
  title: string;
  formData: DatasourceEntity;
}

export const dbTypeOptions = [
    { id: 1, label: 'MySQL' },
    { id: 2, label: 'Oracle' },
    { id: 3, label: 'PostgreSQL' },
    { id: 4, label: 'SQLServer' },
    { id: 5, label: '达梦8' },
    { id: 6, label: 'Clickhouse' },
];
