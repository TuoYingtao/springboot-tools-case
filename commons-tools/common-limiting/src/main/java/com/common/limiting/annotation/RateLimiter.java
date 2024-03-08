package com.common.limiting.annotation;

import com.common.limiting.enums.LimitTacticsType;
import com.common.limiting.enums.LimitTargetType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流注解
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-08 9:34
 * @Version: v1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(RateLimiters.class) // 支持重复注解
public @interface RateLimiter {

    /**
     * 限流键前缀
     * @return
     */
    String key() default "rate_limit:";

    /**
     * 限流规则
     * @return
     */
    RateLimitRule[] rules() default {};

    /**
     * 限流策略
     * @return
     */
    LimitTacticsType tacticsType() default LimitTacticsType.FIXED_WINDOWS;

    /**
     * 限流目标类型
     * @return
     */
    LimitTargetType targetType() default LimitTargetType.DEFAULT;
}
