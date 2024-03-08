package com.common.limiting.component;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局缓存
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-08 16:39
 * @Version: v1.0.0
 */
@Component
public class GlobalMapCache {

    /**
     * 使用ConcurrentHashMap作为全局的缓存存储
     */
    private final Map<String, Object> globalCache = new ConcurrentHashMap<>();

    /**
     * 使用@Cacheable注解来定义缓存行为
     * @param key
     * @return 值
     */
    @Cacheable(value = "globalMapCache", key = "#key")
    public Object getFromCache(String key) {
        // 从全局缓存中获取数据
        return globalCache.get(key);
    }

    /**
     * 将数据存入全局缓存
     * @param key 键
     * @param value 值
     */
    public void putIntoCache(String key, Object value) {
        globalCache.put(key, value);
    }

    /**
     * 清除缓存中的指定数据
     * @param key 键
     */
    public void removeFromCache(String key) {
        globalCache.remove(key);
    }

}
