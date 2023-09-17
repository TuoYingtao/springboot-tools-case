package com.glume.generator.service.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.glume.generator.framework.commons.utils.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Pojo 基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 18:14
 * @Version: v1.0.0
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 6384215274179815390L;

    /**
     * id
     */
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
