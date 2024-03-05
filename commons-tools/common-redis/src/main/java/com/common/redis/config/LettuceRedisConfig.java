package com.common.redis.config;

import com.common.redis.properties.RedisClientProperties;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

/**
 * Lettuce 连接池配置
 * 根据自己的需要可设置连接的 Redis DB
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-07 14:47:23
 * @Version: v1.0.0
*/
@Configuration
// @ConditionalOnBean(name = {"io.lettuce.core.RedisClient"})
// @ConditionalOnProperty(name = "spring.redis.config.client-type", havingValue = "lettuce", matchIfMissing = true)
@EnableConfigurationProperties(RedisClientProperties.class)
public class LettuceRedisConfig extends RedisClientConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisRedisConfig.class);

    public LettuceRedisConfig(RedisClientProperties redisClientProperties) {
        super(redisClientProperties);
    }

    @Override
    public RedisConnectionFactory getRedisConnectionFactory(int database) {
        LettuceConnectionFactory lettuceConnectionFactory =
                new LettuceConnectionFactory(getRedisStandaloneConfig(database), getLettuceClientConfig());
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    private LettuceClientConfiguration getLettuceClientConfig() {
        LettucePoolingClientConfiguration build = LettucePoolingClientConfiguration.builder()
                .poolConfig(getGenericObjectPoolConfig())
                .build();
        return build;
    }

    private GenericObjectPoolConfig<?> getGenericObjectPoolConfig() {
        RedisClientProperties.Pool pool = getProperties().getLettuce().getPool();
        GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(pool.getMaxActive());
        config.setMaxIdle(pool.getMaxIdle());
        config.setMinIdle(pool.getMinIdle());
        config.setMaxWait(pool.getMaxWait());
        config.setTestOnBorrow(pool.getTestOnBorrow());
        config.setTestOnReturn(pool.getTestOnReturn());
        config.setTimeBetweenEvictionRuns(pool.getMaxWait());
        return config;
    }
}
