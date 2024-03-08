package com.common.limiting.enums;

/**
 * 限流策略类型
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-08 11:56
 * @Version: v1.0.0
 */
public enum LimitTacticsType {

    /**
     * 固定窗口限流策略
     */
    FIXED_WINDOWS,
    /**
     * 滑动窗口限流策略
     */
    SLIDING_WINDOWS,
    /**
     * 漏桶限流策略
     */
    LEAKY_BUCKET,
    /**
     * 令牌桶限流策略
     */
    TOKEN_BUCKET,
    /**
     * 滑动日志限流策略
     */
    SLIDING_LOG
}
