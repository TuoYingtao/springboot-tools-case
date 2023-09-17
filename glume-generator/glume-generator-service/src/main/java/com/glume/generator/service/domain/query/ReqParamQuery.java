package com.glume.generator.service.domain.query;

import lombok.Data;

/**
 * 公共查询参数
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 17:30
 * @Version: v1.0.0
 */
@Data
public class ReqParamQuery {

    String code;
    String tableName;
    String attrType;
    String columnType;
    String connName;
    String dbType;
    String projectName;
    /**
     * 页码
     */
    Integer pageNum;
    /**
     * 每页条数
     */
    Integer limit;
}
