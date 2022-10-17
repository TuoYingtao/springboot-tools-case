package com.tuoyingtao.corstools.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局跨域
 * 方法三：自定义 Filter 实现跨域 （SSM 写法）
 * @author tuoyingtao
 * @create 2022-10-13 18:15
 */
//@WebFilter(filterName = "CorsFilter")
//@Configuration
public class SSMCorsFilterConfig implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 设置允许跨域请求的域名
        response.setHeader("Access-Control-Allow-Origin","http://192.168.50.117:8080");
        // 是否允许证书（cookies）
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 设置允许的方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
        // 跨域允许时间
        response.setHeader("Access-Control-Max-Age", "3600");
        // 放行哪些原始请求头部信息
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
