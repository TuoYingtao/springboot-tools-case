package com.glume.generator.framework.enums;

/**
 * 日期格式
 *
 * @Author: TuoYingtao
 * @Date: 2023-11-15 11:05
 * @Version: v1.0.0
 */
public enum DateFormatEnum {
    YYYY_MM_DD_HH_MM_SS_1("yyyy-MM-dd HH:mm:ss"),
    YYYY_MM_DD_HH_MM_SS_2("yyyy/MM/dd HH:mm:ss"),
    YYYY_MM_DD_HH_MM_SS_3("yyyy.MM.dd HH:mm:ss"),
    YYYY_MM_DD_HH_MM_1("yyyy-MM-dd HH:mm"),
    YYYY_MM_DD_HH_MM_2("yyyy/MM/dd HH:mm"),
    YYYY_MM_DD_HH_MM_3("yyyy.MM.dd HH:mm"),
    YYYY_MM_DD_1("yyyy-MM-dd"),
    YYYY_MM_DD_2("yyyy/MM/dd"),
    YYYY_MM_DD_3("yyyy.MM.dd"),
    YYYY_MM_1("yyyy-MM"),
    YYYY_MM_2("yyyy/MM"),
    YYYY_MM_3("yyyy.MM");

    private final String value;

    DateFormatEnum(String name) {
        this.value = name;
    }

    public String getValue() {
        return value;
    }
}
