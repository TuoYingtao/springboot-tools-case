import { FormRules } from 'element-plus';
import { FieldTypeEntity } from '@/api/generator/models/FieldTypeEntity';

interface IData {
  form: FieldTypeEntity;
  queryParams: Record<string, any>;
  rules: FormRules;
}

export const DEFAULT_FORM: FieldTypeEntity = {
  id: -1,
  packageName: '',
  columnType: '',
  attrType: '',
};

export const DATA: IData = {
  form: DEFAULT_FORM,
  queryParams: {
    pageNum: 1,
    limit: 10,
  },
  rules: {
    columnType: [{ required: true, message: '字段类型不能为空', trigger: 'blur' }],
    attrType: [{ required: true, message: '属性类型不能为空', trigger: 'blur' }],
  },
};

export interface Props {
  title: string;
  formData: FieldTypeEntity;
}
