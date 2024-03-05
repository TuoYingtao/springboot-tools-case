package com.common.core.domain;

import com.common.core.constant.Constants;
import com.common.core.utils.json.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @Author: TuoYingtao
 * @Date: 2023-08-31 15:36
 * @Version: v1.0.0
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 成功 */
    public static final int SUCCESS = Constants.SUCCESS;

    /** 失败 */
    public static final int FAIL = Constants.FAIL;

    /** 状态码 */
    private int code;

    /** 数据对象 */
    private String msg;

    /** 返回内容 */
    private T data;

    public static <T> Result<T> ok() {
        return restResult(null, SUCCESS, null);
    }

    public static <T> Result<T> ok(T data) {
        return restResult(data, SUCCESS, null);
    }

    public static <T> Result<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> Result<T> fail() {
        return restResult(null, FAIL, null);
    }

    public static <T> Result<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> Result<T> fail(T data) {
        return restResult(data, FAIL, null);
    }

    public static <T> Result<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> Result<T> restResult(T data, int code, String msg) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public static <T> Boolean isError(Result<T> ret) {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(Result<T> ret) {
        return Result.SUCCESS == ret.getCode();
    }

    /**
     * 对obj进行反序列化为目标类型
     */
    public static  <T> T getConvertDate(Object obj, TypeReference<T> typeReference) {
        String jsonStr = JacksonUtils.beanToJson(obj);
        return JacksonUtils.jsonToList(jsonStr, typeReference);
    }

    public int getCode() {
        return code;
    }

    public Result<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
