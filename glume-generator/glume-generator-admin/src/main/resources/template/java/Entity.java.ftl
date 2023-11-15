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
<#else>
import java.io.Serializable;
</#if>

/**
 * ${tableComment}
 *
 * @Author: ${author}
 * @Date: ${datetime}
 * @Version: v${version}
 */
<#if baseClass??>@EqualsAndHashCode(callSuper=false)</#if>
@Data
@TableName("${tableName}")
public class ${ClassName}Entity<#if baseClass??> extends ${baseClass.code}<#else> implements Serializable</#if> {
    private static final long serialVersionUID = 1L;

<#list fieldList as field>
<#if !field.baseField>
    <#if field.fieldComment!?length gt 0>
    /**
     * ${field.fieldComment}
     */
    </#if>
    <#if (field.dateFill?? && field.dateFill == "JSON_FORMAT")
        || (field.dateFill?? && field.dateFill == "JSON_DATE_FORMAT")>
    @JsonFormat(pattern = "${field.dateFormat}", timezone = "${field.timeZone}")
    </#if>
    <#if (field.dateFill?? && field.dateFill == "DATE_FORMAT")
        || (field.dateFill?? && field.dateFill == "JSON_DATE_FORMAT")>
    @DateTimeFormat(pattern = "${field.dateFormat}")
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
