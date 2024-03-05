package com.common.limiting.handler;

/**
 * 漏桶算法
 * 包含了桶的容量和漏桶出水速率等参数，
 * 以及当前桶中的水量和上次漏水时间戳等状态。
 * @Author: TuoYingtao
 * @Date: 2024-03-05 15:13
 * @Version: v1.0.0
 */
public class LeakyBucketRateLimiter {

    /**
     * 桶的容量
     */
    private final long capacity;
    /**
     * 漏桶出水速率
     */
    private final long rate;
    /**
     * 当前桶中的水量
     */
    private long water;
    /**
     * 上次漏水时间戳
     */
    private long lastLeakTimestamp;

    public LeakyBucketRateLimiter(long capacity, long rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.water = 0;
        this.lastLeakTimestamp = System.currentTimeMillis();
    }

    /**
     * 方法用于尝试向桶中放入一定量的水。
     * @param waterRequested 待进水量
     * @return 如果桶中还有足够的空间返回 false，否则返回 true。
     */
    public synchronized boolean tryConsume(Long waterRequested) {
        leak();
        // 计算桶中水量+待进水量是否超出桶总容量
        if (water + waterRequested <= capacity) {
            // 没有超出
            water += waterRequested;
            return false;
        }
        // 超出了
        return true;
    }

    /**
     * 方法用于漏水，根据当前时间和上次漏水时间戳计算出应该漏出的水量，然后更新桶中的水量和漏水时间戳等状态。
     */
    private void leak() {
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 计算当前时间与上一次时间的时差
        long elapsedTime = now - lastLeakTimestamp;
        // 露出的水量
        long leakedWater = elapsedTime * rate / 1000;
        if (leakedWater > 0) {
            water = Math.max(0, water - leakedWater);
            lastLeakTimestamp = now;
        }
    }
}
