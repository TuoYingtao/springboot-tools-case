package com.common.limiting.handler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
public class SlidingWindowsRateLimiter {

    /**
     * 单位时间划分的小周期（单位时间是1分钟，10s一个小格子窗口，一共6个格子）
     */
    private Integer subCycle = 10;
    /**
     * 每分钟限流请求数
     */
    private Integer thresholdPerMin = 100;
    /**
     * 计数器, k-为当前窗口的开始时间值秒，value为当前窗口的计数
     */
    private TreeMap<Long, AtomicInteger> counters = new TreeMap<>();

    /**
     * @param subCycle 单位时间划分的小周期（单位时间是1分钟，10s一个小格子窗口，一共6个格子）
     * @param thresholdPerMin 每分钟限流请求数
     */
    public SlidingWindowsRateLimiter(Integer subCycle, Integer thresholdPerMin) {
        this.subCycle = subCycle;
        this.thresholdPerMin = thresholdPerMin;
        this.counters = new TreeMap<>();
        this.counters.put(0L, new AtomicInteger(0));
    }

    /**
     * 滑动窗口时间算法实现
     * @return true: 未达到阈值不限流，false：以达到阈值范围准备限流
     */
    public synchronized boolean slidingWindowsTryAcquire() {
        // 获取当前时间在哪个小周期窗口
        long currentWindowTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) / subCycle * subCycle;
        // 当前窗口总请求数
        int currentWindowNum = countCurrentWindow(currentWindowTime);
        // 超过阀值限流
        if (currentWindowNum >= thresholdPerMin) {
            counters.get(currentWindowTime).set(0);
            return false;
        }
        // 计数器+1
        AtomicInteger atomicCount = counters.get(currentWindowTime);
        if (atomicCount != null) {
            atomicCount.incrementAndGet();
        }
        return true;
    }

    /**
     * 统计当前窗口的请求数
     */
    private int countCurrentWindow(Long currentWindowTime) {
        // 计算窗口开始位置
        Long startTime = currentWindowTime - subCycle * (60 / subCycle - 1L);
        int count = 0;
        // 遍历存储的计数器
        Iterator<Map.Entry<Long, AtomicInteger>> iterator = counters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, AtomicInteger> entry = iterator.next();
            // 删除无效过期的子窗口计数器
            if (entry.getKey() < startTime) {
                iterator.remove();
            } else {
                // 累加当前窗口的所有计数器之和
                count = entry.getValue().addAndGet(count);
            }
        }
        return count;
    }
}
