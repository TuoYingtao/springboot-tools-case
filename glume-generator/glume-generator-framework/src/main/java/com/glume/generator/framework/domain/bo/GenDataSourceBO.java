package com.glume.generator.framework.domain.bo;

import com.glume.generator.framework.abstracts.AbstractDBQuery;
import com.glume.generator.framework.enums.DBSourceType;
import com.glume.generator.framework.handler.DBUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源实体类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 15:11
 * @Version: v1.0.0
 */
@Data
public class GenDataSourceBO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(GenDataSourceBO.class);

    /**
     * 数据源ID
     */
    private Long id;
    /**
     * 数据库类型
     */
    private DBSourceType dbSourceType;
    /**
     * 数据库URL
     */
    private String connUrl;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * DB 查询对象
     */
    private AbstractDBQuery dbQuery;
    /**
     *  DB 连接会话
     */
    private Connection connection;


    public GenDataSourceBO(GenDataSourceBuilder GEnDataSourceBuilder) {
        this.id = GEnDataSourceBuilder.id;
        this.dbSourceType = DBSourceType.getValue(GEnDataSourceBuilder.dbSourceType);
        this.connUrl = GEnDataSourceBuilder.connUrl;
        this.username = GEnDataSourceBuilder.username;
        this.password = GEnDataSourceBuilder.password;
        this.dbQuery = DBSourceType.getValue(GEnDataSourceBuilder.dbSourceType).getDbQuery().create();
        try {
            this.connection = DBUtils.getConnection(this);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public GenDataSourceBO(Connection connection) throws SQLException {
        this.id = 0L;
        String dbName = connection.getMetaData().getDatabaseProductName();
        this.dbSourceType = DBSourceType.getValue(dbName);
        this.dbQuery = DBSourceType.getValue(dbName).getDbQuery().create();
        this.connection = connection;
    }

    /**
     * 释放数据源
     */
    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static final class GenDataSourceBuilder {
        private Long id;

        private String dbSourceType;

        private String connUrl;

        private String username;

        private String password;

        public GenDataSourceBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public GenDataSourceBuilder setDbSourceType(String dbSourceType) {
            this.dbSourceType = dbSourceType;
            return this;
        }

        public GenDataSourceBuilder setConnUrl(String connUrl) {
            this.connUrl = connUrl;
            return this;
        }

        public GenDataSourceBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public GenDataSourceBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public GenDataSourceBO builder() {
            return new GenDataSourceBO(this);
        }
    }
}
