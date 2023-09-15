package com.glume.generator.framework.domain.db;


import com.glume.generator.framework.commons.text.Convert;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.enums.DBSourceType;
import com.glume.generator.framework.abstracts.AbstractDBQuery;

/**
 * Oracle DB查询
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 14:51:09
 * @Version: v1.0.0
*/
public class OracleDBQuery implements AbstractDBQuery {

    @Override
    public DBSourceType dbType() {
        return DBSourceType.ORACLE;
    }

    @Override
    public String tableSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select dt.table_name, dtc.comments from user_tables dt,user_tab_comments dtc ");
        sql.append("where dt.table_name = dtc.table_name ");
        // 表名查询
        if (StringUtils.isNotBlank(tableName)) {
            sql.append("and dt.table_name = '").append(tableName).append("' ");
        }
        sql.append("order by dt.table_name asc");

        return Convert.toStr(sql);
    }

    @Override
    public String tableFieldsSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.COLUMN_NAME, A.DATA_TYPE, B.COMMENTS,DECODE(C.POSITION, '1', 'PRI') KEY FROM ALL_TAB_COLUMNS A ")
                .append(" INNER JOIN ALL_COL_COMMENTS B ON A.TABLE_NAME = B.TABLE_NAME AND A.COLUMN_NAME = B.COLUMN_NAME AND B.OWNER = '#schema'")
                .append(" LEFT JOIN ALL_CONSTRAINTS D ON D.TABLE_NAME = A.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P' AND D.OWNER = '#schema'")
                .append(" LEFT JOIN ALL_CONS_COLUMNS C ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME AND C.COLUMN_NAME=A.COLUMN_NAME AND C.OWNER = '#schema'")
                .append("WHERE A.OWNER = '#schema' AND A.TABLE_NAME = '%s' ORDER BY A.COLUMN_ID ");
        return Convert.toStr(sql);
    }

    @Override
    public String tableName() {
        return "table_name";
    }

    @Override
    public String tableComment() {
        return "comments";
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
