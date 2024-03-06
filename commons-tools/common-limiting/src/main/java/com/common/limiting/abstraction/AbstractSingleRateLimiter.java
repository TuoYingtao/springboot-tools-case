package com.common.limiting.abstraction;

/**
 * 单机应用程序限流算法抽象类
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-06 17:23
 * @Version: v1.0.0
 */
public abstract class AbstractSingleRateLimiter {

    /**
     * @return true: 未达到阈值不限流，false：以达到阈值范围准备限流
     */
    public abstract boolean tryAcquire();

}
