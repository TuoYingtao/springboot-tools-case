package com.common.limiting.handler;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口限流算法
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-05 14:33
 * @Version: v1.0.0
 */
public class FixedWindowsRateLimiter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixedWindowsRateLimiter.class);

    /**
     * 上一次获取时间
     */
    public Long lastAcquireTime = 0L;
    /**
     * 固定时间窗口是1000ms
     */
    public Long windowUnit = 1000L;
    /**
     * 窗口阀值是10
     */
    public Integer threshold = 10;
    /**
     * 统计请求数
     */
    public AtomicInteger atomicCount = new AtomicInteger(0);


    /**
     * @param lastAcquireTime 上一次获取时间
     * @param windowUnit      固定时间窗口是1000ms
     * @param threshold       窗口阀值是10
     */
    public FixedWindowsRateLimiter(Long lastAcquireTime, Long windowUnit, Integer threshold) {
        this.lastAcquireTime = lastAcquireTime;
        this.windowUnit = windowUnit;
        this.threshold = threshold;
        this.atomicCount = new AtomicInteger(0);
    }

    /**
     * 固定窗口限流算法
     *
     * @return true: 未达到阈值不限流，false：以达到阈值范围准备限流
     */
    public synchronized boolean fixedWindowsTryAcquire() {
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
        return false;
    }

}
