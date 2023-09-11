package com.tuoyingtao;

import com.commons.compound.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author TuoYingtao
 * @create 2023-08-11 15:14
 */
@EnableCustomSwagger2
@MapperScan(value = { "com.tuoyingtao.mapper" })
@SpringBootApplication
public class SpringBootToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootToolsApplication.class, args);
    }

}
