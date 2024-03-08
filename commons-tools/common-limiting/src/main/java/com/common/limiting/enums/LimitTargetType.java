package com.common.limiting.enums;

/**
 * 限流类型
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-08 9:42
 * @Version: v1.0.0
 */
public enum LimitTargetType {

    /**
     * 默认策略全局限流, 根据接口限流
     */
    DEFAULT,
    /**
     * 根据请求者IP进行限流
     */
    IP,
    /**
     * 根据用户限流
     */
    USER,
}
