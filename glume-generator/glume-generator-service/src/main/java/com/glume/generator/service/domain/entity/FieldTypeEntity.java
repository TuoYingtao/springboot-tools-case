package com.glume.generator.service.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.glume.generator.service.base.entity.BaseEntity;
import lombok.Data;

/**
 * 字段类型管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 17:09
 * @Version: v1.0.0
 */
@Data
@TableName("gen_field_type")
public class FieldTypeEntity extends BaseEntity {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 字段类型
     */
    private String columnType;
    /**
     * 属性类型
     */
    private String attrType;
    /**
     * 属性包名
     */
    private String packageName;
}
