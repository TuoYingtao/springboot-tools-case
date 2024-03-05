package com.common.redis.config;

import com.common.redis.properties.RedisClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis 连接池配置
 * 根据自己的需要可设置连接的 Redis DB
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-07 14:47:00
 * @Version: v1.0.0
*/
@Configuration
// @ConditionalOnBean(name = {"redis.clients.jedis.Jedis"})
// @ConditionalOnProperty(name = "spring.redis.config.client-type", havingValue = "jedis", matchIfMissing = true)
@EnableConfigurationProperties(RedisClientProperties.class)
public class JedisRedisConfig extends RedisClientConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisRedisConfig.class);

    public JedisRedisConfig(RedisClientProperties redisClientProperties) {
        super(redisClientProperties);
    }

    /**
     * Jedis 线程池配置
     */
    @Bean
    public JedisPool JedisPoolFactory() {
        JedisPool jedisPool = new JedisPool(
                jedisPoolConfig(),
                getProperties().getHost(),
                getProperties().getPort(),
                getProperties().getJedis().getPool().getMaxWait().getNano(),
                getProperties().getPassword());
        LOGGER.info("JedisPool 连接成功：{}:{}", getProperties().getHost(), getProperties().getPort());
        return jedisPool;
    }

    /**
     * Redis 连接工厂
     * 使用了单机配置 + Jedis 客户端 = Jedis 连接工厂
     */
    @Override
    public RedisConnectionFactory getRedisConnectionFactory(int database) {
        JedisConnectionFactory jedisConnectionFactory =
                new JedisConnectionFactory(getRedisStandaloneConfig(database), getJedisClientConfig());
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    /**
     * Jedis 客户端连接池配置
     */
    private JedisClientConfiguration getJedisClientConfig() {
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpccb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        jpccb.poolConfig(jedisPoolConfig());

        return jpccb.build();
    }

    /**
     * Jedis 线程池配置
     */
    public JedisPoolConfig jedisPoolConfig() {
        RedisClientProperties.Pool pool = getProperties().getJedis().getPool();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数
        jedisPoolConfig.setMaxIdle(pool.getMaxIdle());
        // 最小空闲连接数
        jedisPoolConfig.setMinIdle(pool.getMinIdle());
        // 连接池中最大连接数
        jedisPoolConfig.setMaxTotal(pool.getMaxActive());
        // 当池内没有可用的连接时，最大等待时间
        jedisPoolConfig.setMaxWait(pool.getMaxWait());
        // 在获取连接时是否检查有效性, 默认false
        jedisPoolConfig.setTestOnBorrow(pool.getTestOnBorrow());
        // 在归还连接时是否检查有效性, 默认false
        jedisPoolConfig.setTestOnReturn(pool.getTestOnReturn());
        jedisPoolConfig.setTimeBetweenEvictionRuns(pool.getMaxWait());
        return jedisPoolConfig;
    }
}
