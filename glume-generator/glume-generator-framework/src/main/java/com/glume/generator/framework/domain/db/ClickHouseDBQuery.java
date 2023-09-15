package com.glume.generator.framework.domain.db;

import com.glume.generator.framework.commons.text.Convert;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.enums.DBSourceType;
import com.glume.generator.framework.abstracts.AbstractDBQuery;

/**
 * ClickHouse DB查询
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 14:49:16
 * @Version: v1.0.0
 */
public class ClickHouseDBQuery implements AbstractDBQuery {

    @Override
    public DBSourceType dbType() {
        return DBSourceType.CLICKHOUSE;
    }

    @Override
    public String tableFieldsSql() {
        return "select * from system.columns where table='%s'";
    }


    @Override
    public String tableSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM system.tables WHERE 1=1 ");
        // 表名查询
        if (StringUtils.isNotBlank(tableName)) {
            sql.append("and name = '").append(tableName).append("' ");
        }
        return Convert.toStr(sql);
    }

    @Override
    public String tableName() {
        return "name";
    }

    @Override
    public String tableComment() {
        return "comment";
    }

    @Override
    public String fieldName() {
        return "name";
    }

    @Override
    public String fieldType() {
        return "type";
    }

    @Override
    public String fieldComment() {
        return "comment";
    }

    @Override
    public String fieldKey() {
        return "is_in_primary_key";
    }

}
