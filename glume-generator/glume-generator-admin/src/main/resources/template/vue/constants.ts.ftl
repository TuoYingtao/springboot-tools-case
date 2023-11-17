import { FormRules } from "element-plus";
import { FieldTypeEntity } from "@/api/${className}/models/${ClassName}Entity";

interface IData {
  form: ${ClassName}Entity;
  queryParams: Record<string, any>;
  rules: FormRules;
}

export const DEFAULT_FORM: ${ClassName}Entity = {
<#list fieldList as field>
<#if field.attrName == "id">
  id: -1,
<#elseif field.attrType == "String">
  ${field.attrName}: '',
<#elseif field.attrType == "Long" || field.attrType == "Integer" || field.attrType == "Double" || field.attrType == "Float">
  ${field.attrName}: 0,
<#elseif field.attrType == "Boolean">
  ${field.attrName}: false,
<#else>
  ${field.attrName}: null,
</#if>
</#list>
}

export const DATA: IData = {
  form: DEFAULT_FORM,
  queryParams: {
    pageNum: 1,
    limit: 10,
  },
  rules: {
<#list formList as field>
  	<#if field.formRequired>
    ${field.attrName}: [{ required: true, message: "${field.fieldName}不能为空", trigger: "blur" }],
    </#if>
</#list>
  }
}

export interface Props {
  title: string;
  formData: ${ClassName}Entity;
}
