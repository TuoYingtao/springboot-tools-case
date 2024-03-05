package com.common.core.annotation;

import com.common.core.annotation.constraint.CharacterLogicStrConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定字母校验
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 15:50:35
 * @Version: v1.0.0
*/
@Documented
@Constraint(validatedBy = {CharacterLogicStrConstraintValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStr {
    String message() default "{com.common.core.annotation.Logic.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] vals() default {};
}
