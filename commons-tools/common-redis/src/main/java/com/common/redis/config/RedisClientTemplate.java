package com.common.redis.config;

import com.common.redis.properties.RedisClientProperties;
import com.common.redis.utils.FastJson2JsonRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Redis 默认配置
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-07 14:02
 * @Version: v1.0.0
 */
@Configuration
@AutoConfigureBefore({ RedisAutoConfiguration.class })
@Import({ JedisRedisConfig.class, LettuceRedisConfig.class })
public class RedisClientTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClientTemplate.class);

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Resource
    private JedisRedisConfig jedisRedisConfig;

    @Resource
    private LettuceRedisConfig lettuceRedisConfig;

    @Resource
    private RedisClientProperties properties;

    @Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<Object, Object> redisTemplate() {
        if (StringUtils.hasText(properties.getPassword())) {
            String clientType = "";
            if (StringUtils.hasText(properties.getClientType())) {
                clientType = properties.getClientType();
            } else {
                if (redisConnectionFactory instanceof JedisConnectionFactory) {
                    clientType = RedisClientProperties.JEDIS_CLIENT_NAME;
                }
                if (redisConnectionFactory instanceof LettuceConnectionFactory) {
                    clientType = RedisClientProperties.LETTUCE_CLIENT_NAME;
                }
            }
            if (clientType.equals(RedisClientProperties.JEDIS_CLIENT_NAME)) {
                LOGGER.info("ClientType：{} 准备采用JedisClient的RedisTemplate", clientType);
                return jedisRedisConfig.getRedisTemplate();
            }
            if (clientType.equals(RedisClientProperties.LETTUCE_CLIENT_NAME)) {
                LOGGER.info("ClientType：{} 准备采用LettuceClient的RedisTemplate", clientType);
                return lettuceRedisConfig.getRedisTemplate();
            }
        }
        LOGGER.info("采用默认的RedisTemplate");
        return defaultTemplate();
    }

    private RedisTemplate<Object, Object> defaultTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        // 使用FastJson2JsonRedisSerializer来序列化和反序列化redis的value值的序列化方式
        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
