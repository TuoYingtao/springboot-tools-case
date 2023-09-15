package com.glume.generator.service.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据表 试图层
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 16:56
 * @Version: v1.0.0
 */
@Data
public class TableVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 表名
     */
    private String tableName;
    /**
     * 功能名
     */
    private String tableComment;
    /**
     * 数据源ID
     */
    private Long datasourceId;
}
