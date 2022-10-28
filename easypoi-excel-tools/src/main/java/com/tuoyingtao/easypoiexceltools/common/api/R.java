package com.tuoyingtao.easypoiexceltools.common.api;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tuoyingtao
 * @create 2022-10-28 11:27
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    /** 状态码 */
    public static final String CODE_TAG = "code";
    /** 返回内容 */
    public static final String MSG_TAG = "msg";
    /** 数据对象 */
    public static final String DATA_TAG = "data";


    public R() {
        put(CODE_TAG, ResultCode.SUCCESS.getCode());
        put(MSG_TAG, ResultCode.SUCCESS.getMessage());
    }

    public static R error() {
        return error(ResultCode.FAILED.getCode(), "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(ResultCode.FAILED.getCode(), msg);
    }

    public static R error(long code, String msg) {
        R r = new R();
        r.put(CODE_TAG, code);
        r.put(MSG_TAG, msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put(MSG_TAG, msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Integer getCode() {
        return Integer.parseInt(this.get(CODE_TAG).toString());
    }

    // 利用HuTool的JSON工具包进行反序列化
    public <T> T getData(TypeReference<T> typeReference) {
        Object data = get(DATA_TAG);	//默认是map
        String jsonString = JSONUtil.toJsonStr(data);
        T t = JSONUtil.toBean(jsonString, typeReference, false);
        return t;
    }

    // 利用HuTool的JSON进行反序列化
    public <T> T getData(String key,TypeReference<T> typeReference) {
        Object data = get(key);	//默认是map
        String jsonString = JSONUtil.toJsonStr(data);
        T t = JSONUtil.toBean(jsonString, typeReference, false);
        return t;
    }
}
