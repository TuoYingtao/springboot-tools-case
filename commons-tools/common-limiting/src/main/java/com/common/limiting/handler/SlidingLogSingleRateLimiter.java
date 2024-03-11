package com.common.limiting.handler;

import com.common.limiting.abstraction.AbstractSingleRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.TreeMap;

/**
 * 滑动日志限流算法
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-06 17:03
 * @Version: v1.0.0
 */
public class SlidingLogSingleRateLimiter extends AbstractSingleRateLimiter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlidingLogSingleRateLimiter.class);

    /**
     * 每分钟限制请求数
     */
    private static final Long PERMITS_PER_MINUTE = 60L;
    /**
     * 请求日志计数器, k-为请求的时间（秒），value当前时间的请求数量
     */
    private final TreeMap<Long, Integer> requestLogCountMap = new TreeMap<>();

    /**
     * @return true: 未达到阈值不限流，false：以达到阈值范围准备限流
     */
    @Override
    public synchronized boolean tryAcquire() {
        // 最小时间粒度为s
        long currentTimestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        // 获取当前窗口的请求总数
        Integer currentWindowCount = getCurrentWindowCount(currentTimestamp);
        if (currentWindowCount >= PERMITS_PER_MINUTE) {
            return false;
        }
        // 请求成功，将当前请求日志加入到日志中
        requestLogCountMap.merge(currentTimestamp, 1, Integer::sum);
        return true;
    }

    /**
     * 统计当前时间窗口内的请求数
     *
     * @param currentTime 当前时间
     * @return - 当前窗口中的计数
     */
    private Integer getCurrentWindowCount(long currentTime) {
        // 计算出窗口的开始位置时间
        long startTime = currentTime - 59;
        // 遍历当前存储的计数器，删除无效的子窗口计数器，并累加当前窗口中的所有计数器之和
        Integer sum = requestLogCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() >= startTime)
                .mapToInt(Map.Entry::getValue).sum();
        LOGGER.debug("当前时间窗口：{} 当前时间窗口位置：{} 当前窗口的请求总数：{}", currentTime, startTime, sum);
        return sum;
    }
}
