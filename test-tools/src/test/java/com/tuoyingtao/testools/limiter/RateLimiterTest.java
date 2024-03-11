package com.tuoyingtao.testools.limiter;

import com.common.limiting.handler.SlidingWindowSingleRateLimiter;
import com.common.limiting.handler.TokenBucketSingleRateLimiter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-05 16:51
 * @Version: v1.0.0
 */
@SpringBootTest
public class RateLimiterTest {

    ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 50, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100));

    @Test
    void dateTime() {
        System.out.println(System.currentTimeMillis() % 1000 / (1000 / 10));
        System.out.println(LocalDateTime.now());
        System.out.println(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        System.out.println(10 * (60L / 10 - 1));
        System.out.println(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - 100 * (60L / 100 - 1));
    }

    @Test
    void fixedWindowsRateLimiter() throws InterruptedException {
        int permitsPerMinute = 3;
        // FixedWindowsRateLimiter rateLimiter = new FixedWindowsRateLimiter(10, 1000L);
        SlidingWindowSingleRateLimiter rateLimiter = new SlidingWindowSingleRateLimiter(permitsPerMinute, 10L);
        // LeakyBucketRateLimiter rateLimiter = new LeakyBucketRateLimiter(15L, 2L);
        // TokenBucketSingleRateLimiter rateLimiter = new TokenBucketSingleRateLimiter(15L, 2L);

        for (int i = 0; i < 20; i++) {
            CountDownLatch countDownLatch = new CountDownLatch(permitsPerMinute);
            for (int j = 0; j < permitsPerMinute; j++) {
                executor.execute(() -> {
                    String name = Thread.currentThread().getName();
                    if (rateLimiter.tryAcquire()) {
                        long lastCount = countDownLatch.getCount();
                        countDownLatch.countDown();
                        System.out.println(LocalDateTime.now() + " " + name + "--正常放行: " + lastCount + "---剩余：" + countDownLatch.getCount());
                    } else {
                        long lastCount = countDownLatch.getCount();
                        countDownLatch.countDown();
                        System.out.println(LocalDateTime.now() + " " + name + "--限流中：" + lastCount + "---剩余：" + countDownLatch.getCount());
                    }
                });
            }
            // 等待所有线程结束
            countDownLatch.await();
            // 休眠1min
            TimeUnit.SECONDS.sleep(1);
            System.out.println("开始新的" + (i + 1) + "轮：--------------------------------------");
        }
    }
}
