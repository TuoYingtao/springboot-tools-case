package com.common.redis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Redis Client 连接池配置
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-07 13:59:22
 * @Version: v1.0.0
*/
@ConfigurationProperties(prefix = "spring.redis.config")
public class RedisClientProperties {

    public static final String JEDIS_CLIENT_NAME = "jedis";

    public static final String LETTUCE_CLIENT_NAME = "lettuce";

    private String host = "localhost";

    private int port = 6379;

    private String password;

    private int database = 0;

    private String clientType;

    private final Jedis jedis = new Jedis();

    private final Lettuce lettuce = new Lettuce();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public Lettuce getLettuce() {
        return lettuce;
    }

    public static class Pool {

        //连接池中最大连接数
        private int maxActive = 8;

        //最大空闲连接数
        private int maxIdle = 8;

        //最小空闲连接数
        private int minIdle = 0;

        //当池内没有可用的连接时，最大等待时间
        private Duration maxWait = Duration.ofMillis(-1);

        //在获取连接时是否检查有效性
        private Boolean testOnBorrow = false;

        //在归还连接时是否检查有效性
        private Boolean testOnReturn = false;

        public int getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public int getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public Duration getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(Duration maxWait) {
            this.maxWait = maxWait;
        }

        public Boolean getTestOnBorrow() {
            return testOnBorrow;
        }

        public void setTestOnBorrow(Boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
        }

        public Boolean getTestOnReturn() {
            return testOnReturn;
        }

        public void setTestOnReturn(Boolean testOnReturn) {
            this.testOnReturn = testOnReturn;
        }
    }

    public static class Jedis {
        private final Pool pool = new Pool();

        public Pool getPool() {
            return pool;
        }
    }

    public static class Lettuce {
        private final Pool pool = new Pool();

        public Pool getPool() {
            return pool;
        }
    }
}
