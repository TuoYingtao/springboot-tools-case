package com.common.limiting.handler;

import com.common.limiting.abstraction.AbstractSingleRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 漏桶算法
 * 包含了桶的容量和漏桶出水速率等参数，
 * 以及当前桶中的水量和上次漏水时间戳等状态。
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-05 15:13
 * @Version: v1.0.0
 */
public class LeakyBucketSingleRateLimiter extends AbstractSingleRateLimiter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeakyBucketSingleRateLimiter.class);

    /**
     * 桶的容量
     */
    private final Long capacity;
    /**
     * 漏桶出水速率（单位：个/s）
     */
    private final Long rate;
    /**
     * 当前桶中的水量
     */
    private AtomicLong water;
    /**
     * 上次漏水时间戳（单位：ms）
     */
    private Long lastLeakTimestamp;

    /**
     * @param capacity 桶的容量
     * @param rate 漏桶出水速率（单位：个/s）
     */
    public LeakyBucketSingleRateLimiter(Long capacity, Long rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.water = new AtomicLong(0L);
        this.lastLeakTimestamp = System.currentTimeMillis();
    }

    @Override
    public boolean tryAcquire() {
        return tryConsume(1L);
    }

    /**
     * 方法用于尝试向桶中放入一定量的水。
     *
     * @param waterRequested 待进水量
     * @return 如果桶中还有足够的空间返回 true，否则返回 false。
     */
    public synchronized boolean tryConsume(Long waterRequested) {
        leak();
        // 计算桶中水量+待进水量是否超出桶总容量
        if (water.get() + waterRequested <= capacity) {
            // 没有超出
            water.addAndGet(waterRequested);
            return true;
        }
        // 超出了
        return false;
    }

    /**
     * 方法用于漏水，根据当前时间和上次漏水时间戳计算出应该漏出的水量，然后更新桶中的水量和漏水时间戳等状态。
     */
    private void leak() {
        // 获取当前时间
        Long now = System.currentTimeMillis();
        // 计算当前时间与上一次时间的时差（秒）
        Long elapsedTime = (now - lastLeakTimestamp) / 1000;
        // 露出的水量
        Long leakedWater = elapsedTime * rate;
        if (leakedWater > 0) {
            water = new AtomicLong(Math.max(0, water.get() - leakedWater));
            lastLeakTimestamp = now;
            LOGGER.debug("漏水：{}，目前桶中剩：{}", leakedWater, water.get());
        }
    }
}
