package com.common.redis.config;

import com.common.redis.properties.RedisClientProperties;
import com.common.redis.utils.FastJson2JsonRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 客户端接口
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-07 14:48:24
 * @Version: v1.0.0
*/
public abstract class RedisClientConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClientConfig.class);

    private final RedisClientProperties properties;

    public RedisClientConfig(RedisClientProperties redisClientProperties) {
        this.properties = redisClientProperties;
    }

    public RedisTemplate<Object, Object> getRedisTemplate() {
        return getRedisTemplate(this.properties.getDatabase());
    }

    public RedisTemplate<Object, Object> getRedisTemplate(int database) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate();

        // 使用FastJson2JsonRedisSerializer来序列化和反序列化redis的value值的序列化方式
        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setConnectionFactory(getRedisConnectionFactory(database));
        redisTemplate.afterPropertiesSet();
        LOGGER.info("完成RedisTemplate配置 索引库已选择：{}", database);
        return redisTemplate;
    }

    /**
     * 抽象 Redis 连接工厂
     * @return
     */
    public abstract RedisConnectionFactory getRedisConnectionFactory(int database);

    /**
     * Redis 单机配置
     */
    public RedisStandaloneConfiguration getRedisStandaloneConfig(int database) {
        RedisStandaloneConfiguration redisStandaloneConfig = new RedisStandaloneConfiguration();
        redisStandaloneConfig.setHostName(this.properties.getHost());
        redisStandaloneConfig.setPort(this.properties.getPort());
        redisStandaloneConfig.setPassword(RedisPassword.of(this.properties.getPassword()));
        redisStandaloneConfig.setDatabase(database);
        return redisStandaloneConfig;
    }

    public RedisClientProperties getProperties() {
        return properties;
    }
}
