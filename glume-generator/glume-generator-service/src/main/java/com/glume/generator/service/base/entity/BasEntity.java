package com.glume.generator.service.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.glume.generator.framework.commons.utils.DateUtils;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 18:14
 * @Version: v1.0.0
 */
@Data
public class BasEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS, timezone = DateUtils.TIMEZONE_GMT8)
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS, timezone = DateUtils.TIMEZONE_GMT8)
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
