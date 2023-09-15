package com.glume.generator.framework.domain.db;

import com.glume.generator.framework.commons.text.Convert;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.enums.DBSourceType;
import com.glume.generator.framework.abstracts.AbstractDBQuery;

/**
 * 达梦8 DB查询
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 14:49:24
 * @Version: v1.0.0
*/
public class DmDBQuery implements AbstractDBQuery {

    @Override
    public DBSourceType dbType() {
        return DBSourceType.MY_SQL;
    }

    @Override
    public String tableSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T.* FROM (SELECT DISTINCT T1.TABLE_NAME AS TABLE_NAME,T2.COMMENTS AS TABLE_COMMENT FROM USER_TAB_COLUMNS T1 ");
        sql.append("INNER JOIN USER_TAB_COMMENTS T2 ON T1.TABLE_NAME = T2.TABLE_NAME) T WHERE 1=1 ");
        // 表名查询
        if (StringUtils.isNotBlank(tableName)) {
            sql.append("and T.TABLE_NAME = '").append(tableName).append("' ");
        }
        sql.append("order by T.TABLE_NAME asc");

        return Convert.toStr(sql);
    }

    @Override
    public String tableFieldsSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T2.COLUMN_NAME,T1.COMMENTS,")
                .append("CASE WHEN T2.DATA_TYPE='NUMBER' THEN (CASE WHEN T2.DATA_PRECISION IS NULL THEN T2.DATA_TYPE WHEN NVL(T2.DATA_SCALE, 0) > 0 THEN T2.DATA_TYPE||'('||T2.DATA_PRECISION||','||T2.DATA_SCALE||')' ELSE T2.DATA_TYPE||'('||T2.DATA_PRECISION||')' END) ELSE T2.DATA_TYPE END DATA_TYPE ,")
                .append("CASE WHEN CONSTRAINT_TYPE='P' THEN 'PRI' END AS KEY ")
                .append("FROM USER_COL_COMMENTS T1, USER_TAB_COLUMNS T2, ")
                .append("(SELECT T4.TABLE_NAME, T4.COLUMN_NAME ,T5.CONSTRAINT_TYPE ")
                .append("FROM USER_CONS_COLUMNS T4, USER_CONSTRAINTS T5 ")
                .append("WHERE T4.CONSTRAINT_NAME = T5.CONSTRAINT_NAME ")
                .append("AND T5.CONSTRAINT_TYPE = 'P')T3 ")
                .append("WHERE T1.TABLE_NAME = T2.TABLE_NAME AND ")
                .append("T1.COLUMN_NAME=T2.COLUMN_NAME AND ")
                .append("T1.TABLE_NAME = T3.TABLE_NAME(+) AND ")
                .append("T1.COLUMN_NAME=T3.COLUMN_NAME(+)   AND ")
                .append("T1.TABLE_NAME = '%s' ")
                .append("ORDER BY T2.TABLE_NAME,T2.COLUMN_ID");
        return Convert.toStr(sql);
    }

    @Override
    public String tableName() {
        return "TABLE_NAME";
    }

    @Override
    public String tableComment() {
        return "TABLE_COMMENT";
    }

    @Override
    public String fieldName() {
        return "COLUMN_NAME";
    }

    @Override
    public String fieldType() {
        return "DATA_TYPE";
    }

    @Override
    public String fieldComment() {
        return "COMMENTS";
    }

    @Override
    public String fieldKey() {
        return "KEY";
    }
}
