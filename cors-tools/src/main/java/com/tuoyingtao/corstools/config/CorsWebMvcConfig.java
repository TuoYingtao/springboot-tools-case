package com.tuoyingtao.corstools.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域
 * 方法二：重写 WebMvcConfigurer 配置的 addCorsMappings 方法
 * @author tuoyingtao
 * @create 2022-10-13 18:11
 */
//@Configuration
public class CorsWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 添加映射路径
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 放行哪些原始请求头部信息
                .allowedHeaders("*")
                // 设置允许的方法
                .allowedMethods(new String[]{"GET","POST","PUT","DELETE"})
                // 暴露哪些头部信息
                .exposedHeaders("*")
                // 是否允许证书（cookies）
                .allowCredentials(true)
                // 跨域允许时间
                .maxAge(3600);
    }
}
