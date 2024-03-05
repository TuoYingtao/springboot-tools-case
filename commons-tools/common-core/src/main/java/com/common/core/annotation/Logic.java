package com.common.core.annotation;

import com.common.core.annotation.constraint.CharacterLogicConstraintValidator;
import com.common.core.annotation.constraint.IntegerLogicConstraintValidator;
import com.common.core.annotation.constraint.LongLogicConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定数字校验
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 15:50:24
 * @Version: v1.0.0
*/
@Documented
@Constraint(validatedBy = {IntegerLogicConstraintValidator.class,
        CharacterLogicConstraintValidator.class,
        LongLogicConstraintValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logic {

    String message() default "{com.common.core.annotation.Logic.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] vals() default {};
}
