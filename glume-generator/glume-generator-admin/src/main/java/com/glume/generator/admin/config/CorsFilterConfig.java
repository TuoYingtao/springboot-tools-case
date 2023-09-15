package com.glume.generator.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局跨域
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 14:53:53
 * @Version: v1.0.0
*/
@Configuration
public class CorsFilterConfig {

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 是否允许证书（cookies）
        corsConfiguration.setAllowCredentials(true);
        // 放行哪些原始请求头部信息
        corsConfiguration.addAllowedHeader("*");
        // 设置允许跨域请求的域名
        corsConfiguration.addAllowedOriginPattern("*");
        // 设置允许的方法
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

}
