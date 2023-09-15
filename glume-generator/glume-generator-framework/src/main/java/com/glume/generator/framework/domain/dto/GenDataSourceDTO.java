package com.glume.generator.framework.domain.dto;

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
public class GenDataSourceDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(GenDataSourceDTO.class);

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


    public GenDataSourceDTO(GEnDataSourceBuilder GEnDataSourceBuilder) {
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

    public GenDataSourceDTO(Connection connection) throws SQLException {
        this.id = 0L;
        String dbName = connection.getMetaData().getDatabaseProductName();
        this.dbSourceType = DBSourceType.getValue(dbName);
        this.dbQuery = DBSourceType.getValue(dbName).getDbQuery().create();
        this.connection = connection;
    }

    public static final class GEnDataSourceBuilder {
        private Long id;

        private String dbSourceType;

        private String connUrl;

        private String username;

        private String password;

        public GEnDataSourceBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public GEnDataSourceBuilder setDbSourceType(String dbSourceType) {
            this.dbSourceType = dbSourceType;
            return this;
        }

        public GEnDataSourceBuilder setConnUrl(String connUrl) {
            this.connUrl = connUrl;
            return this;
        }

        public GEnDataSourceBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public GEnDataSourceBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public GenDataSourceDTO builder() {
            return new GenDataSourceDTO(this);
        }
    }
}
