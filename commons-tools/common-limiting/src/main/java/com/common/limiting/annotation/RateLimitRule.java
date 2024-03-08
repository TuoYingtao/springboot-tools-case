package com.common.limiting.annotation;

import com.common.limiting.enums.LimitTacticsType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流注解规则
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-08 9:39
 * @Version: v1.0.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimitRule {

    /**
     * 标识限流方式
     * @return
     */
    LimitTacticsType value();

    /**
     * 阀值
     * @return
     */
    int threshold() default 20;

    /**
     * 时间单位（s）
     * @return
     */
    long time() default 10L;
}
