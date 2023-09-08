package com.tuoyingtao.testools.utils;

import com.alibaba.fastjson2.JSON;
import com.compound.common.redis.properties.RedisClientProperties;
import com.compound.common.redis.utils.RedisTemplateUtils;
import com.compound.common.redis.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-07 15:38
 * @Version: v1.0.0
 */
@SpringBootTest
public class RedisTemplateTest {

    @Resource(name = RedisTemplateUtils.REDIS_TEMPLATE_JEDIS)
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private RedisUtils redisUtils;

    @Test
    public void defaultRedisTemplateTest() {
        System.out.println(redisTemplate);
        RedisClientProperties redisClientProperties = new RedisClientProperties();
        redisTemplate.opsForValue().set(redisClientProperties, JSON.toJSONString(redisClientProperties));
        System.out.println(redisTemplate.opsForValue().get(redisClientProperties));
    }

    @Test
    public void redisUtilsTest() {
        System.out.println(redisUtils);
        redisUtils.set("redisUtils", new String[]{"测试1", "测试2"});
        System.out.println(redisUtils.get("redisUtils"));
    }
}
