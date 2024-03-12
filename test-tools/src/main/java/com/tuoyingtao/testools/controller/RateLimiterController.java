package com.tuoyingtao.testools.controller;

import com.common.core.exception.BusinessException;
import com.common.limiting.annotation.RateLimitRule;
import com.common.limiting.annotation.RateLimiter;
import com.common.limiting.enums.LimitTacticsType;
import com.common.limiting.enums.LimitTargetType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * TODO
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-08 15:19
 * @Version: v1.0.0
 */
@RestController
@RequestMapping("rate_limiter")
public class RateLimiterController {

    @GetMapping("index")
    @RateLimiter(targetType = LimitTargetType.DEFAULT, tacticsType = LimitTacticsType.FIXED_WINDOWS, rules = {
            @RateLimitRule(threshold = 5, time = 500)
    })
    // @RateLimiter(targetType = LimitTargetType.DEFAULT, tacticsType = LimitTacticsType.SLIDING_WINDOWS, rules = {
    //         @RateLimitRule(threshold = 10, time = 2)
    // })
    // @RateLimiter(targetType = LimitTargetType.DEFAULT, tacticsType = LimitTacticsType.TOKEN_BUCKET, rules = {
    //         @RateLimitRule(threshold = 3, time = 3)
    // })
    // @RateLimiter(targetType = LimitTargetType.IP, tacticsType = LimitTacticsType.LEAKY_BUCKET, rules = {
    //         @RateLimitRule(threshold = 10, time = 3)
    // })
    // @RateLimiter(targetType = LimitTargetType.DEFAULT, tacticsType = LimitTacticsType.LEAKY_BUCKET, rules = {
    //         @RateLimitRule(threshold = 700, time = 300)
    // })
    public String annotationRateLimiter() {
        try {
            int min = 100;
            int max = 1000;
            int sleepTime = new Random().nextInt(Math.max(min, max) - Math.min(min, max)) + 5;
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping("index2")
    public String annotationRateLimiter2() {
        throw new BusinessException(500, "访问过于频繁，请稍后再试！");
    }
}
