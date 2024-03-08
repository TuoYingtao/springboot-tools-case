package com.common.core.exception;

import com.common.core.enums.IResponseEnum;

/**
 * 断言-业务异常
 *
 * @Author: TuoYingtao
 * @Date: 2023-08-31 16:29:18
 * @Version: v1.0.0
*/
public class BusinessAssertException extends BaseException {
    private static final long serialVersionUID = 1L;

    private IResponseEnum responseEnum;

    public BusinessAssertException(IResponseEnum responseEnum, Object[] args, String message) {
        super(args, message);
        this.responseEnum = responseEnum;
    }

    public BusinessAssertException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(args, message, cause);
        this.responseEnum = responseEnum;
    }

    public IResponseEnum getResponseEnum() {
        return responseEnum;
    }
}
