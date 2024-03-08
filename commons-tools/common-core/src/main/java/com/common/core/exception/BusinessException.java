package com.common.core.exception;

/**
 * 业务异常
 *
 * @Author: TuoYingtao
 * @Date: 2023-08-31 16:28:43
 * @Version: v1.0.0
*/
public class BusinessException extends BaseException {
    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }
}
