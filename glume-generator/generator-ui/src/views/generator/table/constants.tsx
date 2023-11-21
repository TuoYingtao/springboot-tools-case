import { FormRules } from "element-plus";
import { TableEntity } from "@/api/generator/models/TableEntity";
import { DatasourceEntity } from "@/api/generator/models/DatasourceEntity";
import { BaseClassEntity } from "@/api/generator/models/BaseClassEntity";

interface IData {
  form: TableEntity;
  queryParams: Record<string, any>;
  rules: FormRules;
  fill: Record<string, string>[];
  dateFills: Record<string, string>[];
  dateFormats: Record<string, string>[];
  timeZones: Record<string, string>[];
  query: Record<string, string>[];
  formType: Record<string, string>[];
}

export const DEFAULT_FORM: TableEntity = {
  id: -1,
  author: '',
  backendPath: '',
  className: '',
  datasourceId: -1,
  email: '',
  formLayout: 0,
  frontendPath: '',
  functionName: '',
  generatorType: 0,
  moduleName: '',
  packageName: '',
  tableComment: '',
  tableName: '',
  version: '',
  baseclassId: 0,
  enableBaseService: 0,
  commonPackagePath: '',
  fieldList: [],
}

export const DATA: IData = {
  form: DEFAULT_FORM,
  queryParams: {
    pageNum: 1,
    limit: 10,
  },
  rules: {
    className: [{ required: true, message: "类名不能为空", trigger: "blur" }],
    moduleName: [{ required: true, message: "模块名不能为空", trigger: "blur" }],
    functionName: [{ required: true, message: "功能名不能为空", trigger: "blur" }],
    packageName: [{ required: true, message: "项目包名不能为空", trigger: "blur" }],
    version: [{ required: true, message: "版本号不能为空", trigger: "blur" }],
    backendPath: [{ required: true, message: "后端生成路径不能为空", trigger: "blur" }],
    frontendPath: [{ required: true, message: "前端生成路径不能为空", trigger: "blur" }],
    columnType: [{ required: true, message: "字段类型不能为空", trigger: "blur" }],
    attrType: [{ required: true, message: "属性类型不能为空", trigger: "blur" }],
  },
  fill: [
    { label: 'DEFAULT', value: 'DEFAULT' },
    { label: 'INSERT', value: 'INSERT' },
    { label: 'UPDATE', value: 'UPDATE' },
    { label: 'INSERT_UPDATE', value: 'INSERT_UPDATE' }
  ],
  dateFills: [
    { label: 'DEFAULT', value: 'DEFAULT' },
    { label: 'JSON_FORMAT', value: 'JSON_FORMAT' },
    { label: 'DATE_FORMAT', value: 'DATE_FORMAT' },
    { label: 'JSON_DATE_FORMAT', value: 'JSON_DATE_FORMAT' },
  ],
  dateFormats: [
    { label: 'yyyy-MM-dd HH:mm:ss', value: 'yyyy-MM-dd HH:mm:ss' },
    { label: 'yyyy/MM/dd HH:mm:ss', value: 'yyyy/MM/dd HH:mm:ss' },
    { label: 'yyyy.MM.dd HH:mm:ss', value: 'yyyy.MM.dd HH:mm:ss' },
    { label: 'yyyy-MM-dd HH:mm', value: 'yyyy-MM-dd HH:mm' },
    { label: 'yyyy/MM/dd HH:mm', value: 'yyyy/MM/dd HH:mm' },
    { label: 'yyyy.MM.dd HH:mm', value: 'yyyy.MM.dd HH:mm' },
    { label: 'yyyy-MM-dd', value: 'yyyy-MM-dd' },
    { label: 'yyyy/MM/dd', value: 'yyyy/MM/dd' },
    { label: 'yyyy.MM.dd', value: 'yyyy.MM.dd' },
    { label: 'yyyy-MM', value: 'yyyy-MM' },
    { label: 'yyyy/MM', value: 'yyyy/MM' },
    { label: 'yyyy.MM', value: 'yyyy.MM' },
  ],
  timeZones: [
    { label: 'GMT+8', value: 'GMT+8' },
    { label: 'GMT', value: 'GMT' },
    { label: 'UTC', value: 'UTC' },
  ],
  query: [
    { label: '=', value: '=' },
    { label: '!=', value: '!=' },
    { label: '>', value: '>' },
    { label: '>=', value: '>=' },
    { label: '<', value: '<' },
    { label: '<=', value: '<=' },
    { label: 'like', value: 'like' },
    { label: 'left like', value: 'left like' },
    { label: 'right like', value: 'right like' }
  ],
  formType: [
    { label: '单行文本', value: 'text' },
    { label: '多行文本', value: 'textarea' },
    { label: '富文本编辑器', value: 'editor' },
    { label: '下拉框', value: 'select' },
    { label: '单选按钮', value: 'radio' },
    { label: '复选框', value: 'checkbox' },
    { label: '日期', value: 'date' },
    { label: '日期时间', value: 'datetime' }
  ],
}

export interface Props {
  title: string;
  formData: TableEntity;
  typeList?: string[];
  baseClassList?: BaseClassEntity[];
}

export interface ImportProps {
  title: string;
  datasourceList: DatasourceEntity[];
}

export interface PreviewProps {
  title: string;
  previewCodeMap: Record<string, string>;
}
