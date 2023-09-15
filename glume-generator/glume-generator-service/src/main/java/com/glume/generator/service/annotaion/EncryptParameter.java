package com.glume.generator.service.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数加解密注解
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 13:45
 * @Version: v1.0.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptParameter {
}
