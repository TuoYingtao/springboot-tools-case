package com.common.core.exception;

/**
 * 请求响应异常
 *
 * @Author: TuoYingtao
 * @Date: 2024-03-09 14:48
 * @Version: v1.0.0
 */
public class ResponseException extends BaseException {
    private static final long serialVersionUID = 1L;

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(Integer code, String message) {
        super(code, message);
    }
}
