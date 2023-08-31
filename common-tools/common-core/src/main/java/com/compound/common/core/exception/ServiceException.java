package com.compound.common.core.exception;

/**
 * @Author: TuoYingtao
 * @Date: 2023-08-31 16:28:43
 * @Version: v1.0.0
 * @Description: 业务异常
*/
public class ServiceException extends BaseException {
    private static final long serialVersionUID = 1L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Integer code, String message) {
        super(code, message);
    }
}
