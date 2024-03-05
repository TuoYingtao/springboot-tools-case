package com.common.core.annotation.constraint;

import com.common.core.annotation.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号校验注解实现
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 15:51:30
 * @Version: v1.0.0
*/
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneField == null) {
            return true;
        }
        //  大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^[1]((3[0-9])|(4[5-9])|(5[0-3,5-9])|([6][5,6])|(7[0-9])|(8[0-9])|(9[1,8,9]))\\d{8}$";
        return phoneField.matches(regExp);
    }
}
