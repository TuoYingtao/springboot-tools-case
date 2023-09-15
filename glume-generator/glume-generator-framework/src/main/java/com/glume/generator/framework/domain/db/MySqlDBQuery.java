package com.glume.generator.framework.domain.db;

import com.glume.generator.framework.commons.text.Convert;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.enums.DBSourceType;
import com.glume.generator.framework.abstracts.AbstractDBQuery;

/**
 * MySQL DB查询
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 14:50:37
 * @Version: v1.0.0
*/
public class MySqlDBQuery implements AbstractDBQuery {

    @Override
    public DBSourceType dbType() {
        return DBSourceType.MY_SQL;
    }

    @Override
    public String tableSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select table_name, table_comment from information_schema.tables ");
        sql.append("where table_schema = (select database()) ");
        // 表名查询
        if (StringUtils.isNotBlank(tableName)) {
            sql.append("and table_name = '").append(tableName).append("' ");
        }
        sql.append("order by table_name asc");

        return Convert.toStr(sql);
    }

    @Override
    public String tableFieldsSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select column_name, data_type, column_comment, column_key from information_schema.columns ")
                .append("where table_name = '%s' and table_schema = (select database()) order by ordinal_position");
        return Convert.toStr(sql);
    }

    @Override
    public String tableName() {
        return "table_name";
    }

    @Override
    public String tableComment() {
        return "table_comment";
    }

    @Override
    public String fieldName() {
        return "column_name";
    }

    @Override
    public String fieldType() {
        return "data_type";
    }

    @Override
    public String fieldComment() {
        return "column_comment";
    }

    @Override
    public String fieldKey() {
        return "column_key";
    }
}
