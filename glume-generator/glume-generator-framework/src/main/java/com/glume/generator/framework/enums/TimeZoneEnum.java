package com.glume.generator.framework.enums;

/**
 * 时区 枚举
 *
 * @Author: TuoYingtao
 * @Date: 2023-11-15 11:14
 * @Version: v1.0.0
 */
public enum TimeZoneEnum {

    /** 中国时间 = 格林尼治时间 + 8 */
    GMT_8("GMT+8"),
    /** 格林尼治时间 */
    GMT("GMT"),
    /** 世界标准时间 */
    UTC("UTC");

    private final String value;

    TimeZoneEnum(String name) {
        this.value = name;
    }

    public String getValue() {
        return value;
    }
}
