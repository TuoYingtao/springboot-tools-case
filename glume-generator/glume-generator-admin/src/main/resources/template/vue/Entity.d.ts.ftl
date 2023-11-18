export type ${ClassName}EntityList<T extends typeof BaseEntity = ${ClassName}Entity> = BaseEntityList<T>;

export interface ${ClassName}Entity extends BaseEntity {
<#list fieldList as field>

  <#if field.attrName == "id">
  id?: number;
  <#elseif field.attrType == "String">
  ${field.attrName}: string,
  <#elseif field.attrType == "Long" || field.attrType == "Integer" || field.attrType == "Double" || field.attrType == "Float">
  ${field.attrName}: number,
  <#elseif field.attrType == "Boolean">
  ${field.attrName}: boolean,
  <#else>
  ${field.attrName}?: unknown,
  </#if>
</#list>
}
