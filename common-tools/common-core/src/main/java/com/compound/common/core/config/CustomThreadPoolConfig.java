package com.compound.common.core.config;

import com.compound.common.core.properties.ThreadPoolConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 14:41:26
 * @Version: v1.0.0
*/
@EnableConfigurationProperties({ThreadPoolConfigProperties.class})
@Configuration
public class CustomThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool) {
        /**
         * new ThreadPoolExecutor(
         *  核心线程数,
         *  最大线程数,
         *  存活时间,
         *  时间单位,
         *  执行任务时，等待区的存活队列,
         *  创建线程时使用的工厂,
         *  线程池策略)
         */
        return new ThreadPoolExecutor(
                pool.getCorePoolSize(),
                pool.getMaximumPoolSize(),
                pool.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
