package com.glume.generator.service.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.glume.generator.service.base.domain.entity.BaseEntity;
import lombok.Data;

/**
 * 基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 9:34
 * @Version: v1.0.0
 */
@Data
@TableName("gen_base_class")
public class BaseClassEntity extends BaseEntity {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 基类包名
     */
    private String packageName;
    /**
     * 基类编码
     */
    private String code;
    /**
     * 公共字段，多个用英文逗号分隔
     */
    private String fields;
    /**
     * 备注
     */
    private String remark;

}
