package com.glume.generator.service.domain.query;

import com.glume.generator.service.base.domain.query.BaseParamQuery;
import lombok.Data;

/**
 * 公共查询参数
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 17:30
 * @Version: v1.0.0
 */
@Data
public class ReqParamQuery extends BaseParamQuery {

    String code;
    String tableName;
    String attrType;
    String columnType;
    String connName;
    String dbType;
    String projectName;
}
