package com.commons.compound.swagger.config;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.core.model.MarkdownProperty;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;

import java.util.ArrayList;

/**
 * TODO
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-12 11:20
 * @Version: v1.0.0
 */
// @Configuration
public class SwaggerKnife4jConfiguration {

    // @Bean
    public OpenApiExtensionResolver openApiExtensionResolver() {
        OpenApiExtendSetting openApiExtendSetting = new OpenApiExtendSetting();
        openApiExtendSetting.setEnableDynamicParameter(true);
        ArrayList<MarkdownProperty> markdownProperties = new ArrayList<>();
        return new OpenApiExtensionResolver(openApiExtendSetting, markdownProperties);
    }
}
