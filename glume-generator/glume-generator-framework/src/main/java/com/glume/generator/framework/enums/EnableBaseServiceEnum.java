package com.glume.generator.framework.enums;

/**
 * 业务三层架构基类启用 枚举类
 *
 * @Author: TuoYingtao
 * @Date: 2023-11-16 14:34
 * @Version: v1.0.0
 */
public enum EnableBaseServiceEnum {

    ENABLE(0),
    CLOSE(1);
    private final Integer value;

    EnableBaseServiceEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
