package com.common.core.asserts.impl;

import com.common.core.asserts.BusinessExceptionAssert;
import com.common.core.constant.HttpStatus;

/**
 * 断言-业务异常处理实现
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 15:45:50
 * @Version: v1.0.0
*/
public enum BusinessResponseEnum implements BusinessExceptionAssert {
    // 远程调用服务断言
    FEIGN_RESPONSE_CODE(HttpStatus.ERROR, "远程调用服务异常：{0}"),
    // 分页断言
    PAGE(HttpStatus.BAD_REQUEST, "分页参数【pageNum】不能为空：'{'pageNum:{0}'}'"),

    ;

    private Integer code;

    private String message;

    BusinessResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
