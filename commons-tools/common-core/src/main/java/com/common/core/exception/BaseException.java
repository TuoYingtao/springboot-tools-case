package com.common.core.exception;

import java.io.Serializable;

/**
 * 异常基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-08-31 16:15:58
 * @Version: v1.0.0
*/
public class BaseException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 所属模块 */
    private String module;

    /** 错误码 */
    private Integer code;

    /** 错误提示 */
    private String message;

    /** 错误明细，内部调试错误消息 */
    private String defaultMessage;

    /** 错误码对应的参数 */
    private Object[] args;

    /** 堆栈跟踪数据 */
    private Throwable cause;

    /** 空构造方法，避免反序列化问题 */
    public BaseException() {}

    public BaseException(String message) {
        this(null, null, message, null, null, null);
    }

    public BaseException(Integer code, String message) {
        this(null, code, message, null, null, null);
    }

    public BaseException(Object[] args, String message) {
        this(null, null, message, null, args, null);
    }

    public BaseException(Object[] args, String message, Throwable cause) {
        this(null, null, message, null, args, cause);
    }

    public BaseException(String defaultMessage, Throwable cause) {
        this(null, null, null, defaultMessage, null, cause);
    }

    public BaseException(Integer code, Object[] args) {
        this(null, code, null, null, args, null);
    }

    public BaseException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage, null, null);
    }

    public BaseException(String module, Integer code, Object[] args) {
        this(module, code, null, null, args, null);
    }

    public BaseException(String module, Integer code, String message, String defaultMessage, Object[] args, Throwable cause) {
        this.module = module;
        this.code = code;
        this.message = message;
        this.defaultMessage = defaultMessage;
        this.args = args;
        this.cause = cause;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
