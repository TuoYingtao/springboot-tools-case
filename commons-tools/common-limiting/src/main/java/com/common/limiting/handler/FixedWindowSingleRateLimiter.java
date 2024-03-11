package com.common.limiting.handler;

import com.common.limiting.abstraction.AbstractSingleRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口限流算法
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-05 14:33
 * @Version: v1.0.0
 */
public class FixedWindowSingleRateLimiter extends AbstractSingleRateLimiter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixedWindowSingleRateLimiter.class);

    /**
     * 窗口阀值是10
     */
    public Integer threshold = 10;
    /**
     * 固定时间窗口是1000ms
     */
    public Long windowUnit = 1000L;
    /**
     * 上一次获取时间
     */
    public Long lastAcquireTime = 0L;
    /**
     * 统计请求数
     */
    public final AtomicInteger atomicCount = new AtomicInteger(0);


    /**
     * @param threshold       窗口阀值是10
     * @param windowUnit      固定时间窗口是1000ms
     */
    public FixedWindowSingleRateLimiter(Integer threshold, Long windowUnit) {
        this.threshold = threshold;
        this.windowUnit = windowUnit;
    }

    /**
     * 固定窗口限流算法
     *
     * @return true: 未达到阈值不限流，false：以达到阈值范围准备限流
     */
    @Override
    public synchronized boolean tryAcquire() {
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 检查是否在时间窗口内
        if (now - lastAcquireTime > windowUnit) {
            atomicCount.set(0);
            lastAcquireTime = now;
        }
        // 小于阀值
        if (atomicCount.get() < threshold) {
            // 计数统计器加1
            atomicCount.incrementAndGet();
            return true;
        }
        LOGGER.debug("当前时间窗：{} 时间窗总阈值：{} 当前时间窗阈值：{}", now, threshold, atomicCount.get());
        return false;
    }

}
