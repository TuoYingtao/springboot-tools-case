package com.tuoyingtao.testools.limiter;

import com.common.limiting.handler.FixedWindowsRateLimiter;
import com.common.limiting.handler.SlidingWindowsRateLimiter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
public class RetaLimiterTest {

    ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 50, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100));

    @Test
    void fixedWindowsRateLimiter() throws InterruptedException {
        int maxThreadCount = 10;
        // FixedWindowsRateLimiter rateLimiter = new FixedWindowsRateLimiter(0L, 1000L, 10);
        SlidingWindowsRateLimiter rateLimiter = new SlidingWindowsRateLimiter(10, maxThreadCount);

        for (int i = 0; i < 5; i++) {
            CountDownLatch countDownLatch = new CountDownLatch(maxThreadCount);
            for (int j = 0; j < 30; j++) {
                executor.execute(() -> {
                    String name = Thread.currentThread().getName();
                    if (rateLimiter.slidingWindowsTryAcquire()) {
                        long lastCount = countDownLatch.getCount();
                        countDownLatch.countDown();
                        System.out.println(name + "--正常放行: " + lastCount + "---剩余：" + countDownLatch.getCount());
                    } else {
                        long lastCount = countDownLatch.getCount();
                        countDownLatch.countDown();
                        System.out.println(name + "--限流中：" + lastCount + "---剩余：" + countDownLatch.getCount());
                    }
                });
            }
            // 等待所有线程结束
            countDownLatch.await();
            // 休眠1min
            TimeUnit.SECONDS.sleep(1);
            System.out.println("开始新的一轮：--------------------------------------");
        }
    }
}
