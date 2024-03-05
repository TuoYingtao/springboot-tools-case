package com.common.core.config.validator;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Validation 配置类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 14:41:36
 * @Version: v1.0.0
*/
@Configuration
@ConfigurationProperties(prefix = "common.validator")
public class ValidatorConfig {

    /**
     * 快速失败模式
     */
    private Boolean failFast = true;

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(failFast)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

    public Boolean getFailFast() {
        return failFast;
    }

    public void setFailFast(Boolean failFast) {
        this.failFast = failFast;
    }
}
