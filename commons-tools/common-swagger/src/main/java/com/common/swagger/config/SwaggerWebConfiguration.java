package com.common.swagger.config;

import cn.hutool.core.comparator.VersionComparator;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Swagger 资源映射路径
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-11 16:35
 * @Version: v1.0.0
 */
@EnableConfigurationProperties({WebMvcProperties.class})
public class SwaggerWebConfiguration implements WebMvcConfigurer {

    private final String[] skipAuthPaths = new String[]{
            "/swagger-ui/**",
            "/**/swagger-ui.html",
            "/**/v2/api-docs",
            "/**/swagger-resources/**"
    };

    public SwaggerWebConfiguration(WebMvcProperties webMvcProperties) {
        if (VersionComparator.INSTANCE.compare(SpringBootVersion.getVersion(), "2.6") != -1) {
            // 解决Swagger路径匹配策略报错： SpringBoot 2.6 将 SpringMVC 默认路径匹配策略改成了 PathPatternParser；我们将他改回 AntPathMatcher
            webMvcProperties.getPathmatch().setMatchingStrategy(WebMvcProperties.MatchingStrategy.ANT_PATH_MATCHER);
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** swagger-ui 地址 */
        registry.addResourceHandler(skipAuthPaths)
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }
}
