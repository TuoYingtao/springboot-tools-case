package com.glume.generator.admin;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Glume Generator 启动器
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 9:52
 * @Version: v1.0.0
 */
@SpringBootApplication
public class GlumeGeneratorApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication();
        application.setBannerMode(Banner.Mode.LOG);
        application.run(GlumeGeneratorApplication.class, args);
    }
}
