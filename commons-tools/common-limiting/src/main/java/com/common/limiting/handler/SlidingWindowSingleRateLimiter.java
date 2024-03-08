package com.common.limiting.handler;

import com.common.limiting.abstraction.AbstractSingleRateLimiter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 滑动窗口限流算法
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-05 14:53
 * @Version: v1.0.0
 */
public class SlidingWindowSingleRateLimiter extends AbstractSingleRateLimiter {

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
    private final TreeMap<Long, Integer> counters = new TreeMap<>();

    /**
     * @param permitsPerMinute 每分钟限流请求数
     * @param subCycle      单位时间划分的小周期（单位时间是1分钟，10s一个小格子窗口，一共6个格子）
     */
    public SlidingWindowSingleRateLimiter(Integer permitsPerMinute, Long subCycle) {
        this.permitsPerMinute = permitsPerMinute;
        this.subCycle = subCycle;
    }

    /**
     * 滑动窗口时间算法实现
     *
     * @return true: 未达到阈值不限流，false：以达到阈值范围准备限流
     */
    @Override
    public synchronized boolean tryAcquire() {
        // 获取当前时间在哪个小周期窗口
        long currentWindowTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) / subCycle * subCycle;
        // 当前窗口总请求数
        Integer currentWindowNum = countCurrentWindow(currentWindowTime);
        // 超过阀值限流
        if (currentWindowNum >= permitsPerMinute) {
            return false;
        }
        // 计数器+1
        counters.merge(currentWindowTime, 1, Integer::sum);
        return true;
    }

    /**
     * 获取当前窗口中的所有请求数（并删除所有无效的子窗口计数器）
     *
     * @param currentWindowTime 当前子窗口时间
     * @return 当前窗口中的计数
     */
    private Integer countCurrentWindow(Long currentWindowTime) {
        // 窗口总大小（单位：S）
        Long windowSize = 60L;
        // 计算窗口开始位置
        Long startTime = currentWindowTime - subCycle * (windowSize / subCycle - 1);
        int count = 0;
        // 遍历当前存储的计数器，删除无效的子窗口计数器，并累加当前窗口中的所有计数器之和
        Iterator<Map.Entry<Long, Integer>> iterator = counters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> entry = iterator.next();
            // 删除无效过期的子窗口计数器
            if (entry.getKey() < startTime) {
                iterator.remove();
            } else {
                // 累加当前窗口的所有计数器之和
                count += entry.getValue();
            }
        }
        return count;
    }
}
