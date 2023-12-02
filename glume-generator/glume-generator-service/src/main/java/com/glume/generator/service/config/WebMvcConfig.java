package com.glume.generator.service.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc 配置静态访问路径
 *
 * @Author: TuoYingtao
 * @Date: 2023-12-01 18:00
 * @Version: v1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private String STATIC_HOME_INDEX = "/ui/index.html";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/ui/**")
                .addResourceLocations("classpath:/static/ui/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/ui/index")
                .setViewName(STATIC_HOME_INDEX);
        registry.addRedirectViewController("/", "/ui/index");
        registry.addRedirectViewController("/ui", "/ui/index");
        WebMvcConfigurer.super.addViewControllers(registry);
    }

}
