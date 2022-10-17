package com.tuoyingtao.corstools.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局跨域
 * 方法一：返回新的 CorsFilter
 * @author tuoyingtao
 * @create 2022-10-13 17:05
 */
@Configuration
public class CorsFilterConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加 CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 设置允许跨域请求的域名
        config.addAllowedOriginPattern("*");
        // 设置允许的方法
        config.addAllowedMethod("*");
        // 放行哪些原始请求头部信息
        config.addAllowedHeader("*");
        // 暴露哪些头部信息
        config.addExposedHeader("*");
        // 是否允许证书（cookies）
        config.setAllowCredentials(true);
        // 跨域允许时间
        config.setMaxAge(3600L);
        // 2. 添加映射路径
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        // 3. 返回新的CorsFilter
        return new CorsFilter(corsConfigurationSource);
    }

}
