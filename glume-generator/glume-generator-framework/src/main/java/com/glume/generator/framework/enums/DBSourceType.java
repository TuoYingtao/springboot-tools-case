package com.glume.generator.framework.enums;

import com.glume.generator.framework.abstracts.DBFactory;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.factory.ClickHouseDBFactory;
import com.glume.generator.framework.factory.DmDBFactory;
import com.glume.generator.framework.factory.MySqlDBFactory;
import com.glume.generator.framework.factory.OracleDBFactory;
import com.glume.generator.framework.factory.PostgreSqlDBFactory;
import com.glume.generator.framework.factory.SQLServerDBFactory;

/**
 * 数据库类型 枚举
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 14:17
 * @Version: v1.0.0
 */
public enum DBSourceType {

    MY_SQL("com.mysql.cj.jdbc.Driver", new MySqlDBFactory()),
    ORACLE("oracle.jdbc.driver.OracleDriver", new OracleDBFactory()),
    POSTGRE_SQL("org.postgresql.Driver", new PostgreSqlDBFactory()),
    SQL_SERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver", new SQLServerDBFactory()),
    DM("dm.jdbc.driver.DmDriver", new DmDBFactory()),
    CLICKHOUSE("com.clickhouse.jdbc.ClickHouseDriver", new ClickHouseDBFactory());

    private final String driverClass;

    private final DBFactory dbQuery;

    DBSourceType(String driverClass, DBFactory dbQuery) {
        this.driverClass = driverClass;
        this.dbQuery = dbQuery;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public DBFactory getDbQuery() {
        return dbQuery;
    }

    public static DBSourceType getValue(String dbType) {
        if (StringUtils.inStringIgnoreCase(dbType, "MySQL")) {
            return MY_SQL;
        }

        if (StringUtils.inStringIgnoreCase(dbType, "Oracle")) {
            return ORACLE;
        }

        if (StringUtils.inStringIgnoreCase(dbType, "PostgreSQL")) {
            return POSTGRE_SQL;
        }

        if (StringUtils.inStringIgnoreCase(dbType, "SQLServer", "Microsoft SQL Server")) {
            return SQL_SERVER;
        }

        if (StringUtils.inStringIgnoreCase(dbType, "DM", "DM DBMS")) {
            return DM;
        }

        if (StringUtils.inStringIgnoreCase(dbType, "Clickhouse")) {
            return CLICKHOUSE;
        }
        return null;
    }
}
