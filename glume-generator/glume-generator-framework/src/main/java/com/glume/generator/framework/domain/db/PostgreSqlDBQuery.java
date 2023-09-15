package com.glume.generator.framework.domain.db;

import com.glume.generator.framework.commons.text.Convert;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.enums.DBSourceType;
import com.glume.generator.framework.abstracts.AbstractDBQuery;

/**
 * PostgreSql DB查询
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 14:52:04
 * @Version: v1.0.0
 */
public class PostgreSqlDBQuery implements AbstractDBQuery {

    @Override
    public DBSourceType dbType() {
        return DBSourceType.POSTGRE_SQL;
    }

    @Override
    public String tableSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t1.tablename, obj_description(relfilenode, 'pg_class') as comments from pg_tables t1, pg_class t2 ");
        sql.append("where t1.tablename not like 'pg%' and t1.tablename not like 'sql_%' and t1.tablename = t2.relname ");
        // 表名查询
        if (StringUtils.isNotBlank(tableName)) {
            sql.append("and t1.tablename = '").append(tableName).append("' ");
        }

        return Convert.toStr(sql);
    }

    @Override
    public String tableFieldsSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select t2.attname as columnName, pg_type.typname as dataType, col_description(t2.attrelid,t2.attnum) as columnComment,")
                .append("(CASE t3.contype WHEN 'p' THEN 'PRI' ELSE '' END) as columnKey ")
                .append("from pg_class as t1, pg_attribute as t2 inner join pg_type on pg_type.oid = t2.atttypid ")
                .append("left join pg_constraint t3 on t2.attnum = t3.conkey[1] and t2.attrelid = t3.conrelid ")
                .append("where t1.relname = '%s' and t2.attrelid = t1.oid and t2.attnum>0");
        return Convert.toStr(sql);
    }


    @Override
    public String tableName() {
        return "tablename";
    }

    @Override
    public String tableComment() {
        return "comments";
    }

    @Override
    public String fieldName() {
        return "columnName";
    }

    @Override
    public String fieldType() {
        return "dataType";
    }

    @Override
    public String fieldComment() {
        return "columnComment";
    }

    @Override
    public String fieldKey() {
        return "columnKey";
    }
}
