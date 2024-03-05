package com.common.core.enums;

/**
 * 用户状态枚举
 *
 * @Author: TuoYingtao
 * @Date: 2023-08-31 15:56
 * @Version: v1.0.0
 */
public enum UserStatus {
    /** 状态值, 描述 */
    OK("0", "正常"),
    DISABLE("1", "停用"),
    DELETED("2", "删除");

    private final String code;
    private final String info;

    UserStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
