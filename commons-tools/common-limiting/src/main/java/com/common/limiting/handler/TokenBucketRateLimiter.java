package com.common.limiting.handler;

/**
 * 令牌桶算法
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-05 15:50
 * @Version: v1.0.0
 */
public class TokenBucketRateLimiter {

    /**
     * 令牌桶容量
     */
    private final int capacity;
    /**
     * 令牌生成速率，单位：令牌/秒
     */
    private final int rate;
    /**
     * 当前令牌数量
     */
    private int tokens;
    /**
     * 上次令牌生成时间戳
     */
    private long lastRefillTimestamp;

    /**
     * 构造函数中传入令牌桶的容量和令牌生成速率。
     * @param capacity
     * @param rate
     */
    public TokenBucketRateLimiter(int capacity, int rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    /**
     * 方法表示一个请求是否允许通过，该方法使用 synchronized 关键字进行同步，以保证线程安全。
     * @return 桶中还有令牌返回false 否在返回true
     */
    public synchronized boolean allowRequest() {
        refill();
        if (tokens > 0) {
            tokens--;
            return false;
        }
        return true;
    }

    /**
     * 方法用于生成令牌，其中计算令牌数量的逻辑是按照令牌生成速率每秒钟生成一定数量的令牌，
     * tokens 变量表示当前令牌数量，
     * lastRefillTimestamp 变量表示上次令牌生成的时间戳。
     */
    private void refill() {
        long now = System.currentTimeMillis();
        if (now > lastRefillTimestamp) {
            int generatedTokens = (int) ((now - lastRefillTimestamp) / 1000 * rate);
            tokens = Math.min(tokens + generatedTokens, capacity);
            lastRefillTimestamp = now;
        }
    }
}
