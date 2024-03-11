package com.common.limiting.handler;

import com.common.limiting.abstraction.AbstractSingleRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 令牌桶算法
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-05 15:50
 * @Version: v1.0.0
 */
public class TokenBucketSingleRateLimiter extends AbstractSingleRateLimiter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenBucketSingleRateLimiter.class);

    /**
     * 令牌桶容量
     */
    private final Long capacity;
    /**
     * 令牌生成速率，单位：令牌/秒
     */
    private final Long rate;
    /**
     * 当前桶中令牌数量
     */
    private AtomicLong tokens;
    /**
     * 上次令牌生成时间戳
     */
    private Long lastRefillTimestamp;

    /**
     * 构造函数中传入令牌桶的容量和令牌生成速率。
     *
     * @param capacity 令牌桶容量
     * @param rate 令牌生成速率，单位：令牌/秒
     */
    public TokenBucketSingleRateLimiter(Long capacity, Long rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.tokens = new AtomicLong(capacity);
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    /**
     * 方法表示一个请求是否允许通过，该方法使用 synchronized 关键字进行同步，以保证线程安全。
     *
     * @return 桶中还有令牌返回true 否在返回false
     */
    @Override
    public synchronized boolean tryAcquire() {
        refill();
        if (tokens.get() > 0) {
            tokens.decrementAndGet();
            return true;
        }
        return false;
    }

    /**
     * 方法用于生成令牌，其中计算令牌数量的逻辑是按照令牌生成速率每秒钟生成一定数量的令牌，
     * tokens 变量表示当前令牌数量，
     * lastRefillTimestamp 变量表示上次令牌生成的时间戳。
     */
    private void refill() {
        Long now = System.currentTimeMillis();
        if (now > lastRefillTimestamp) {
            // 计算当前时间与上一次时间的时差（秒）
            long elapsedTime = (now - lastRefillTimestamp) / 1000;
            // 计算这段时间差所生成的Token数量
            long generatedTokens = elapsedTime * rate;
            tokens = new AtomicLong(Math.min(tokens.get() + generatedTokens, capacity));
            if (elapsedTime > 0) {
                lastRefillTimestamp = now;
                LOGGER.debug("生成：{}，目前桶中剩：{}", generatedTokens, tokens.get());
            }
        }
    }
}
