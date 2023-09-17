package com.glume.generator.framework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 表单布局 枚举
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 13:20:20
 * @Version: v1.0.0
*/
@Getter
@AllArgsConstructor
public enum FormLayoutEnum {
    ONE(1),

    TWO(2);

    private final Integer value;
}
