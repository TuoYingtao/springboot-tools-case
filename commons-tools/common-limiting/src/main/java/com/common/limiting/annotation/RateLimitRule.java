package com.common.limiting.annotation;

import com.common.limiting.enums.LimitTacticsType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

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

    // /**
    //  * 标识限流方式
    //  * @return
    //  */
    // LimitTacticsType value();

    /**
     * 阀值
     * @LimitTacticsType.FIXED_WINDOWS：窗口阀值
     * @LimitTacticsType.SLIDING_WINDOWS：每分钟限流请求数
     * @LimitTacticsType.LEAKY_BUCKET：桶的容量
     * @LimitTacticsType.TOKEN_BUCKET：令牌桶容量
     * @return
     */
    int threshold() default 20;

    /**
     * 时限
     * @LimitTacticsType.FIXED_WINDOWS：固定时间窗口（单位：ms）
     * @LimitTacticsType.SLIDING_WINDOWS：单位时间划分的小周期（单位：s）
     * @LimitTacticsType.LEAKY_BUCKET：漏桶出水速率（单位：个/s）
     * @LimitTacticsType.TOKEN_BUCKET：令牌生成速率（单位：令牌/s）
     * @return
     */
    long time() default 10L;

    /**
     * 限流时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
