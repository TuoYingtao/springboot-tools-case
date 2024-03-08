package com.common.limiting.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多种限流组合注解
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-08 9:34
 * @Version: v1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiters {

    RateLimiter[] value();
}
