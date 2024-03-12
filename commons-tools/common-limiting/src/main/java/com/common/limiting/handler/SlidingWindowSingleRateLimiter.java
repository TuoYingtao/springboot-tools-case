package com.common.limiting.handler;

import com.common.limiting.abstraction.AbstractSingleRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 滑动窗口限流算法
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-05 14:53
 * @Version: v1.0.0
 */
public class SlidingWindowSingleRateLimiter extends AbstractSingleRateLimiter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlidingWindowSingleRateLimiter.class);

    /**
     * 每分钟限流请求数
     */
    private final Integer permitsPerMinute;
    /**
     * 单位时间划分的小周期（单位：S）
     */
    private final Long subCycle;
    /**
     * 计数器, k-为当前窗口的开始时间值秒，value为当前窗口的计数
     */
    private final TreeMap<Long, AtomicInteger> counters = new TreeMap<>();

    /**
     * @param permitsPerMinute 每分钟限流请求数
     * @param subCycle      单位时间划分的小周期（单位时间是1分钟，10s一个小格子窗口，一共6个格子）
     */
    public SlidingWindowSingleRateLimiter(Integer permitsPerMinute, Long subCycle) {
        this.permitsPerMinute = permitsPerMinute;
        this.subCycle = subCycle;
        long windowCount = 60L / this.subCycle;
        Long timeKey = System.currentTimeMillis() / 1000;
        for (long i = 0L; i < windowCount; i++) {
            counters.put(timeKey, new AtomicInteger(0));
            windowCount += windowCount;
        }
    }

    /**
     * 滑动窗口时间算法实现
     *
     * @return true: 未达到阈值不限流，false：以达到阈值范围准备限流
     */
    @Override
    public synchronized boolean tryAcquire() {
        // 获取当前时间在哪个小周期窗口
        Long currentWindowTime = System.currentTimeMillis() / 1000;
        // 当前窗口总请求数
        Integer currentWindowNum = countCurrentWindow(currentWindowTime);
        // 超过阀值限流
        if (currentWindowNum >= permitsPerMinute) {
            return false;
        }
        // 计数器+1
        counters.merge(currentWindowTime, new AtomicInteger(1), (a, b) -> new AtomicInteger(a.addAndGet(b.get())));
        return true;
    }

    /**
     * 获取当前窗口中的所有请求数（并删除所有无效的子窗口计数器）
     *
     * @param currentWindowTime 当前子窗口时间
     * @return 当前窗口中的计数
     */
    private Integer countCurrentWindow(Long currentWindowTime) {
        Integer count = 0;
        // 遍历当前存储的计数器，删除无效的子窗口计数器，并累加当前窗口中的所有计数器之和
        Iterator<Map.Entry<Long, AtomicInteger>> iterator = counters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, AtomicInteger> entry = iterator.next();
            // 删除无效过期的子窗口计数器
            if ((currentWindowTime - entry.getKey()) > subCycle) {
                iterator.remove();
            } else {
                // 累加当前窗口的所有计数器之和
                count += entry.getValue().get();
            }
        }
        LOGGER.debug("当前时间窗口：{} 当前窗口的请求总数：{}", currentWindowTime, count);
        return count;
    }
}
