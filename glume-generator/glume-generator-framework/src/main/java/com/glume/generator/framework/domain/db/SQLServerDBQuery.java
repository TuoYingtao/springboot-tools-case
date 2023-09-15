package com.glume.generator.framework.domain.db;

import com.glume.generator.framework.commons.text.Convert;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.enums.DBSourceType;
import com.glume.generator.framework.abstracts.AbstractDBQuery;

/**
 * SQLServer DB查询
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 14:52:14
 * @Version: v1.0.0
 */
public class SQLServerDBQuery implements AbstractDBQuery {

    @Override
    public DBSourceType dbType() {
        return DBSourceType.SQL_SERVER;
    }

    @Override
    public String tableSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select cast(so.name as varchar(500)) as TABLE_NAME, cast(sep.value as varchar(500)) as COMMENTS from sysobjects so ");
        sql.append("left JOIN sys.extended_properties sep on sep.major_id=so.id and sep.minor_id=0 where (xtype='U' or xtype='V') ");

        // 表名查询
        if (StringUtils.isNotBlank(tableName)) {
            sql.append("and cast(so.name as varchar(500)) = '").append(tableName).append("' ");
        }
        sql.append(" order by cast(so.name as varchar(500))");

        return Convert.toStr(sql);
    }

    @Override
    public String tableFieldsSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  cast(a.name AS VARCHAR(500)) AS TABLE_NAME,cast(b.name AS VARCHAR(500)) AS COLUMN_NAME, ")
                .append("cast(c.VALUE AS NVARCHAR(500)) AS COMMENTS,cast(sys.types.name AS VARCHAR (500)) AS DATA_TYPE,")
                .append("(SELECT CASE count(1) WHEN 1 then 'PRI' ELSE '' END")
                .append(" FROM syscolumns,sysobjects,sysindexes,sysindexkeys,systypes ")
                .append(" WHERE syscolumns.xusertype = systypes.xusertype AND syscolumns.id = object_id (a.name) AND sysobjects.xtype = 'PK'")
                .append(" AND sysobjects.parent_obj = syscolumns.id  AND sysindexes.id = syscolumns.id ")
                .append(" AND sysobjects.name = sysindexes.name AND sysindexkeys.id = syscolumns.id ")
                .append(" AND sysindexkeys.indid = sysindexes.indid ")
                .append(" AND syscolumns.colid = sysindexkeys.colid AND syscolumns.name = b.name) as 'KEY',")
                .append("  b.is_identity isIdentity ")
                .append(" FROM ( select name,object_id from sys.tables UNION all select name,object_id from sys.views ) a ")
                .append(" INNER JOIN sys.columns b ON b.object_id = a.object_id ")
                .append(" LEFT JOIN sys.types ON b.user_type_id = sys.types.user_type_id   ")
                .append(" LEFT JOIN sys.extended_properties c ON c.major_id = b.object_id AND c.minor_id = b.column_id ")
                .append(" WHERE a.name = '%s' and sys.types.name !='sysname' ");
        return Convert.toStr(sql);
    }

    @Override
    public String tableName() {
        return "TABLE_NAME";
    }

    @Override
    public String tableComment() {
        return "COMMENTS";
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
