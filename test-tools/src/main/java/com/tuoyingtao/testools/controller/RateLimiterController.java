package com.tuoyingtao.testools.controller;

import com.common.limiting.annotation.RateLimitRule;
import com.common.limiting.annotation.RateLimiter;
import com.common.limiting.enums.LimitTacticsType;
import com.common.limiting.enums.LimitTargetType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TODO
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-08 15:19
 * @Version: v1.0.0
 */
@Controller
@RequestMapping("rate_limiter")
public class RateLimiterController {

    @GetMapping("index")
    @RateLimiter(targetType = LimitTargetType.IP, rules = {
            @RateLimitRule(value = LimitTacticsType.LEAKY_BUCKET, threshold = 2, time = 5),
            @RateLimitRule(value = LimitTacticsType.TOKEN_BUCKET, threshold = 3, time = 5)
    })
    @RateLimiter(rules = {@RateLimitRule(value = LimitTacticsType.FIXED_WINDOWS, threshold = 10, time = 10),}, targetType = LimitTargetType.DEFAULT)
    public String annotationRateLimiter() {

        return "success";
    }
}
