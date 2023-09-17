package com.glume.generator.framework.handler;

import com.glume.generator.framework.domain.bo.GenDataSourceBO;
import com.glume.generator.framework.enums.DBSourceType;
import oracle.jdbc.OracleConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB工具类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 15:20
 * @Version: v1.0.0
 */
public class DBUtils {

    private static final int CONNECTION_TIMEOUTS_SECONDS = 6;

    /**
     * 获得数据库连接
     */
    public static Connection getConnection(GenDataSourceBO dataSource) throws ClassNotFoundException, SQLException {
        DriverManager.setLoginTimeout(CONNECTION_TIMEOUTS_SECONDS);
        Class.forName(dataSource.getDbSourceType().getDriverClass());
        Connection connection = DriverManager.getConnection(dataSource.getConnUrl(), dataSource.getUsername(), dataSource.getPassword());
        if (dataSource.getDbSourceType() == DBSourceType.ORACLE) {
            ((OracleConnection) connection).setRemarksReporting(true);
        }
        return connection;
    }
}
