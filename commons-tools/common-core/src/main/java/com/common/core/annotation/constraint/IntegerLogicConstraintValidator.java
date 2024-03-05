package com.common.core.annotation.constraint;

import com.common.core.annotation.Logic;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * Integer 型 Logic验证
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 15:51:12
 * @Version: v1.0.0
*/
public class IntegerLogicConstraintValidator implements ConstraintValidator<Logic,Integer> {

    private Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(Logic constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for (Integer val : vals) {
            set.add(val);
        }
    }

    /**
     * 判断是否校验成功
     * @param integer 需要校验的值
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
