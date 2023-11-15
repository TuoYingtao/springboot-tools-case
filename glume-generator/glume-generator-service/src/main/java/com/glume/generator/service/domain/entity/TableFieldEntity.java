package com.glume.generator.service.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.glume.generator.service.base.domain.entity.BaseEntity;
import lombok.Data;

/**
 * 表字段
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 12:02
 * @Version: v1.0.0
 */
@Data
@TableName("gen_table_field")
public class TableFieldEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    /**
     * 表ID
     */
    private Long tableId;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 字段说明
     */
    private String fieldComment;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 属性类型
     */
    private String attrType;
    /**
     * 属性包名
     */
    private String packageName;
    /**
     * 自动填充
     */
    private String autoFill;
    /**
     * 日期填充
     */
    private String dateFill;
    /**
     * 日期格式
     */
    private String dateFormat;
    /**
     * 时区
     */
    private String timeZone;
    /**
     * 主键 0：否  1：是
     */
    private boolean primaryPk;
    /**
     * 基类字段 0：否  1：是
     */
    private boolean baseField;
    /**
     * 表单项 0：否  1：是
     */
    private boolean formItem;
    /**
     * 表单必填 0：否  1：是
     */
    private boolean formRequired;
    /**
     * 表单类型
     */
    private String formType;
    /**
     * 表单字典类型
     */
    private String formDict;
    /**
     * 表单效验
     */
    private String formValidator;
    /**
     * 列表项 0：否  1：是
     */
    private boolean gridItem;
    /**
     * 列表排序 0：否  1：是
     */
    private boolean gridSort;
    /**
     * 查询项 0：否  1：是
     */
    private boolean queryItem;
    /**
     * 查询方式
     */
    private String queryType;
    /**
     * 查询表单类型
     */
    private String queryFormType;

}
