package ${package}.${moduleName}.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
<#if baseClass??>
import lombok.EqualsAndHashCode;
</#if>
<#list importList as i>
import ${i!};
</#list>
<#if baseClass??>
import ${baseClass.packageName}.${baseClass.code};
</#if>

/**
 * ${tableComment}
 *
 * @Author: ${author}
 * @Date: ${datetime}
 * @Version: v${version}
 */
@Data
<#if baseClass??>@EqualsAndHashCode(callSuper=false)</#if>
@TableName("${tableName}")
public class ${ClassName}Entity<#if baseClass??> extends ${baseClass.code}</#if> {
<#if baseClass??>
    private static final long serialVersionUID = 1L;

</#if>
<#list fieldList as field>
<#if !field.baseField>
    <#if field.fieldComment!?length gt 0>
    /**
     * ${field.fieldComment}
     */
    </#if>
    <#if (field.dateFormat?? && field.dateFormat == "JSON_FORMAT")
        || (field.dateFormat?? && field.dateFormat == "JSON_DATE_FORMAT")>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    </#if>
    <#if (field.dateFormat?? && field.dateFormat == "DATE_FORMAT")
        || (field.dateFormat?? && field.dateFormat == "JSON_DATE_FORMAT")>
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    <#if field.autoFill == "INSERT">
    @TableField(fill = FieldFill.INSERT)
    </#if>
    <#if field.autoFill == "UPDATE">
    @TableField(fill = FieldFill.UPDATE)
    </#if>
    <#if field.autoFill == "INSERT_UPDATE">
    @TableField(fill = FieldFill.INSERT_UPDATE)
    </#if>
    <#if field.primaryPk>
    @TableId
    </#if>
    private ${field.attrType} ${field.attrName};
</#if>
</#list>

}
