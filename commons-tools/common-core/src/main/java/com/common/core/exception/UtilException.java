package com.common.core.exception;

/**
 * 工具类异常
 *
 * @Author: TuoYingtao
 * @Date: 2023-08-31 16:18
 * @Version: v1.0.0
 */
public class UtilException extends BaseException {
    private static final long serialVersionUID = 8247685219171014183L;

    public UtilException(String defaultMessage) {
        super(defaultMessage);
    }

    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
