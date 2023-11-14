package com.glume.generator.service.base.domain.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.glume.generator.framework.commons.utils.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询 基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-11-14 11:31
 * @Version: v1.0.0
 */
@Data
public class BaseParamQuery implements Serializable {
    private static final long serialVersionUID = 6384218674179815390L;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD, timezone = DateUtils.TIMEZONE_GMT8)
    Date beginTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = DateUtils.YYYY_MM_DD, timezone = DateUtils.TIMEZONE_GMT8)
    Date endTime;
    /**
     * 页码
     */
    Integer pageNum;
    /**
     * 每页条数
     */
    Integer limit;
}
