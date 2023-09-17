package com.glume.generator.starter.config;

import com.glume.generator.framework.handler.GenConfigUtils;
import com.glume.generator.starter.properties.GlumeGeneratorProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Glume Generator 自动配置类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 11:12
 * @Version: v1.0.0
 */
@Configuration
@EnableConfigurationProperties({GlumeGeneratorProperties.class})
@MapperScan(basePackages = {"com.glume.generator.service.mapper"})
@ComponentScan(basePackages = {"com.glume.generator.service", "com.glume.generator.framework"})
public class GlumeGeneratorAutoConfiguration {

    private final GlumeGeneratorProperties generatorProperties;

    public GlumeGeneratorAutoConfiguration(GlumeGeneratorProperties generatorProperties) {
        this.generatorProperties = generatorProperties;
    }

    @Bean
    public GenConfigUtils generatorConfigUtils() {
        return new GenConfigUtils(generatorProperties.getTemplate());
    }
}
