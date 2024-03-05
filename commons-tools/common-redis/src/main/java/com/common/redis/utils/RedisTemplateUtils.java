package com.common.redis.utils;

import com.common.redis.config.JedisRedisConfig;
import com.common.redis.config.LettuceRedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis 模板工具
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-07 14:54:15
 * @Version: v1.0.0
*/
@Component
public class RedisTemplateUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTemplateUtils.class);

    @Value("${redis.database.user-db:${spring.redis.database}}")
    private int databaseUser;


    public static final String REDIS_TEMPLATE_USER = "userRedisTemplate";

    public static final String REDIS_TEMPLATE_JEDIS = "jedisRedisTemplate";

    public static final String REDIS_TEMPLATE_LETTUCE = "lettuceRedisTemplate";

    @Bean(REDIS_TEMPLATE_JEDIS)
    @ConditionalOnBean(value = { JedisRedisConfig.class })
    public RedisTemplate<Object, Object> jedisRedisTemplate(JedisRedisConfig jedisRedisConfig) {
        LOGGER.info("准备JedisRedisTemplate Bean....");
        return jedisRedisConfig.getRedisTemplate();
    }

    @Bean(REDIS_TEMPLATE_LETTUCE)
    @ConditionalOnBean(value = { LettuceRedisConfig.class })
    public RedisTemplate<Object, Object> lettuceRedisTemplate(LettuceRedisConfig lettuceRedisConfig) {
        LOGGER.info("准备LettuceRedisTemplate Bean....");
        return lettuceRedisConfig.getRedisTemplate();
    }

    @Bean(REDIS_TEMPLATE_USER)
    @ConditionalOnBean(value = { LettuceRedisConfig.class })
    public RedisTemplate<Object, Object> userRedisTemplate(LettuceRedisConfig lettuceRedisConfig) {
        LOGGER.info("准备UserRedisTemplate Bean 并指定索引库：{}.... 连接池采用Lettuce", this.databaseUser);
        return lettuceRedisConfig.getRedisTemplate(this.databaseUser);
    }

}
