package com.tuoyingtao.mybatisplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MyBatisPlus 启动入口
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-12 15:30:49
 * @Version: v1.0.0
*/
@SpringBootApplication
public class SpringbootMybatisPlusToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisPlusToolsApplication.class, args);
        System.out.println("SpringBoot 版本" + SpringBootVersion.getVersion());
    }

}
