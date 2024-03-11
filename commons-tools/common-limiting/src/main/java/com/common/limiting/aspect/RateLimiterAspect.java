package com.common.limiting.aspect;

import com.common.core.exception.BusinessException;
import com.common.core.exception.ResponseException;
import com.common.core.text.Convert;
import com.common.core.text.StrFormatter;
import com.common.core.utils.StringUtils;
import com.common.core.utils.ip.IpUtils;
import com.common.core.utils.uuid.IdUtils;
import com.common.limiting.abstraction.AbstractSingleRateLimiter;
import com.common.limiting.annotation.RateLimitRule;
import com.common.limiting.annotation.RateLimiter;
import com.common.limiting.annotation.RateLimiters;
import com.common.limiting.component.GlobalMapCache;
import com.common.limiting.enums.LimitTacticsType;
import com.common.limiting.enums.LimitTargetType;
import com.common.limiting.handler.FixedWindowSingleRateLimiter;
import com.common.limiting.handler.LeakyBucketSingleRateLimiter;
import com.common.limiting.handler.SlidingLogSingleRateLimiter;
import com.common.limiting.handler.SlidingWindowSingleRateLimiter;
import com.common.limiting.handler.TokenBucketSingleRateLimiter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RateLimiterSingle 切面类
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-08 9:44
 * @Version: v1.0.0
 */
@Aspect
@Order(0)
@Component
public class RateLimiterAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiterAspect.class);

    @Autowired
    public GlobalMapCache globalMapCache;

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.common.limiting.annotation.RateLimiter)")
    public void rateLimiter() {
    }

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.common.limiting.annotation.RateLimiters)")
    public void rateLimiters() {
    }

    /**
     * 定义切点之前的操作
     * @param joinPoint 链接点
     */
    @Before("rateLimiter() || rateLimiters()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            // 从切点获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取方法
            Method method = signature.getMethod();
            // 获取方法全类名
            String className = StrFormatter.format("{}.{}", joinPoint.getTarget().getClass().getName(), signature.getName());
            RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
            RateLimiters rateLimiters = method.getAnnotation(RateLimiters.class);

            List<RateLimiter> limiterSingles = new ArrayList<>();
            if (StringUtils.isNotNull(rateLimiter)) {
                limiterSingles.add(rateLimiter);
            }
            if (StringUtils.isNotNull(rateLimiters) && StringUtils.isNotEmpty(Arrays.asList(rateLimiters.value()))) {
                limiterSingles.addAll(Arrays.asList(rateLimiters.value()));
            }

            if (!allowRequest(limiterSingles, className)) {
                throw new BusinessException(302, "访问过于频繁，请稍后再试！");
                // throw new ResponseException("访问过于频繁，请稍后再试！");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (ResponseException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("服务限流异常，请稍后再试！");
        }
    }

    /**
     * 是否允许请求
     * @param limiterSingles 限流注解集合
     * @param className 全类名
     * @return true：允许请求 false：拒绝请求
     */
    private boolean allowRequest(List<RateLimiter> limiterSingles, String className) {
        List<String> keys = getKeys(limiterSingles, className);
        return keys.stream().allMatch((key) -> {
            boolean acquire = globalMapCache.getFromCache(key).tryAcquire();
            if (!acquire) {
                LOGGER.debug("限流了：{}", key);
            } else {
                LOGGER.debug("正常执行：{}", key);
            }
            return acquire;
        });
    }

    /**
     * 获取 Key 值
     * @param limiterSingles 限流注解集合
     * @param className 全类名
     * @return 限流 Key 集合
     */
    private List<String> getKeys(List<RateLimiter> limiterSingles, String className) {
        List<String> keys = new ArrayList<>();
        for (RateLimiter rateLimiter : limiterSingles) {
            String key = rateLimiter.key();
            RateLimitRule[] rateLimitRules = rateLimiter.rules();
            LimitTargetType limitTargetType = rateLimiter.targetType();
            LimitTacticsType limitTacticsType = rateLimiter.tacticsType();

            StringBuilder sb = new StringBuilder();
            sb.append(key).append(className)
                    .append("_").append(limitTacticsType);

            if (LimitTargetType.IP.equals(limitTargetType)) {
                String ipAddr = IpUtils.getIpAddr();
                sb.append("_").append(ipAddr);
            }

            for (RateLimitRule rateLimitRule : rateLimitRules) {
                Long time = limitTacticsType.equals(LimitTacticsType.FIXED_WINDOWS)
                        ? rateLimitRule.time() * 1000
                        : rateLimitRule.time();
                Integer threshold = rateLimitRule.threshold();
                StringBuilder stringBuilder = new StringBuilder(sb);
                String keyStr = stringBuilder
                        .append("_").append(time)
                        .append("_").append(threshold)
                        .toString();
                keys.add(keyStr);
                if (!globalMapCache.containsKey(keyStr)) {
                    AbstractSingleRateLimiter rateLimiterInstance = createRateLimiter(rateLimitRule, limitTacticsType);
                    globalMapCache.putIntoCache(keyStr, rateLimiterInstance);
                }
            }
        }
        return keys;
    }

    /**
     * 创建限流器
     * @param rateLimitRule 限流规则
     * @param limitTacticsType 限流器类型
     * @return 限流器
     */
    private AbstractSingleRateLimiter createRateLimiter(RateLimitRule rateLimitRule, LimitTacticsType limitTacticsType) {
        Integer threshold = rateLimitRule.threshold();
        Long time = rateLimitRule.time();
        if (limitTacticsType.equals(LimitTacticsType.FIXED_WINDOWS)) {
            return new FixedWindowSingleRateLimiter(threshold, time * 1000);
        } else if (limitTacticsType.equals(LimitTacticsType.SLIDING_WINDOWS)) {
            return new SlidingWindowSingleRateLimiter(threshold, time);
        } else if (limitTacticsType.equals(LimitTacticsType.SLIDING_LOG)) {
            return new SlidingLogSingleRateLimiter();
        } else if (limitTacticsType.equals(LimitTacticsType.LEAKY_BUCKET)) {
            return new LeakyBucketSingleRateLimiter(Convert.toLong(threshold), time);
        } else if (limitTacticsType.equals(LimitTacticsType.TOKEN_BUCKET)) {
            return new TokenBucketSingleRateLimiter(Convert.toLong(threshold), time);
        } else {
            return new TokenBucketSingleRateLimiter(Convert.toLong(threshold), time);
        }
    }

    /**
     * 获取参数
     * @param limiterSingles 限流注解集合
     * @return Object[] 参数数组
     */
    private Object[] getArgs(List<RateLimiter> limiterSingles) {
        List<Object> args = new ArrayList<>();
        args.add(System.currentTimeMillis());
        for (RateLimiter limiterSingle : limiterSingles) {
            RateLimitRule[] rateLimitRules = limiterSingle.rules();
            for (RateLimitRule ruleSingle : rateLimitRules) {
                Long time = ruleSingle.time();
                Integer threshold = ruleSingle.threshold();
                args.add(time);
                args.add(threshold);
                args.add(IdUtils.fastSimpleUUID());
            }
        }
        return args.toArray();
    }
}
