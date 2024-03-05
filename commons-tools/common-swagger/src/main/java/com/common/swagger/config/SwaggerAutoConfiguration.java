package com.common.swagger.config;


import com.common.swagger.domain.SwaggerProperties;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Swagger 配置类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-13 10:11:58
 * @Version: v1.0.0
*/
@Configuration
@EnableKnife4j
@EnableSwagger2
@EnableConfigurationProperties({SwaggerProperties.class})
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@Import({SwaggerBeanPostProcessor.class, SwaggerWebConfiguration.class})
public class SwaggerAutoConfiguration {

    /**
     * 默认的排除路径，排除Spring Boot默认的错误处理路径和端点
     */
    private static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");

    private static final String BASE_PATH = "/**";

    private final String pathMapping = "/";

    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public SwaggerAutoConfiguration(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    /**
     * Swagger2 整合 springfox-swagger-ui
     *  访问地址：http://localhost:端口/swagger-ui.html 或 http://localhost:端口/swagger-ui/index.html
     * Swagger2 整合 knife4j 后的访问地址：http://localhost:端口/doc.html
     */
    @Bean
    public Docket createRestApi(SwaggerProperties swaggerProperties) {
        if (swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add(BASE_PATH);
        }
        List<Predicate<String>> basePath = new ArrayList<>();
        swaggerProperties.getBasePath().forEach(path -> basePath.add(PathSelectors.ant(path)));

        if (swaggerProperties.getExcludePath().isEmpty()) {
            swaggerProperties.getExcludePath().addAll(DEFAULT_EXCLUDE_PATH);
        }
        ArrayList<Predicate<String>> excludePath = new ArrayList<>();
        swaggerProperties.getExcludePath().forEach(path -> excludePath.add(PathSelectors.ant(path)));

        ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(webApiInfo(swaggerProperties))
                .host(swaggerProperties.getHost())
                // 设置请求的统一前缀
                .pathMapping(pathMapping)
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描指定包中的swagger注解
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()));
                // 扫描所有有注解的Api 用这种方式更灵活
                // .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));
        swaggerProperties.getBasePath().forEach(path -> apiSelectorBuilder.paths(PathSelectors.ant(path)));
        swaggerProperties.getExcludePath().forEach(path -> apiSelectorBuilder.paths(PathSelectors.ant(path).negate()));
        String groupName = "1.X版本";
        return apiSelectorBuilder.build()
                /* 设置安全模式，swagger可以设置访问token */
                .securitySchemes(securitySchemes(swaggerProperties))
                .securityContexts(securityContexts(swaggerProperties))
                .extensions(openApiExtensionResolver.buildExtensions(groupName))
                .groupName(groupName);
    }

    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<SecurityScheme> securitySchemes(SwaggerProperties swaggerProperties) {
        List<SecurityScheme> apiKeyList = new ArrayList<>();
        for (String authField : swaggerProperties.getAuthField()) {
            apiKeyList.add(new ApiKey(authField, authField, In.HEADER.toValue()));
        }
        return apiKeyList;
    }

    /**
     * 安全上下文
     */
    private List<SecurityContext> securityContexts(SwaggerProperties swaggerProperties) {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth(swaggerProperties))
                        .operationSelector(operationContext ->
                                operationContext.requestMappingPattern().matches("/.*"))
                        .build());
        return securityContexts;
    }

    /**
     * 默认的安全上引用
     */
    private List<SecurityReference> defaultAuth(SwaggerProperties swaggerProperties) {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        for (String authField : swaggerProperties.getAuthField()) {
            securityReferences.add(new SecurityReference(authField, authorizationScopes));
        }
        return securityReferences;
    }

    /**
     * 添加摘要信息
     *
     * @return
     */
    public ApiInfo webApiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                // 设置标题
                .title(swaggerProperties.getTitle())
                // 描述
                .description(swaggerProperties.getDescription())
                // 联系人
                .contact(new Contact(swaggerProperties.getContact().getName(),
                        swaggerProperties.getContact().getUrl(),
                        swaggerProperties.getContact().getEmail()))
                // 服务器URL地址
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                // 版本
                .version(swaggerProperties.getVersion())
                // 许可证
                .license(swaggerProperties.getLicense())
                // 许可证地址
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .build();
    }

}
