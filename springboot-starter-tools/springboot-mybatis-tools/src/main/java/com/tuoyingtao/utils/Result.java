package com.tuoyingtao.utils;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求响应实体类
 * @author tuoyingtao
 * @create 2023-04-19 11:34
 */
public class Result extends HashMap<String, Object> implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 状态码 */
	public static final String CODE_TAG = "code";

	/** 返回内容 */
	public static final String MSG_TAG = "msg";

	/** 数据对象 */
	public static final String DATA_TAG = "data";

	public Result() {
		put(CODE_TAG, HttpStatus.OK.value());
		put(MSG_TAG, "success");
		put(DATA_TAG, "");
	}

	/**
	 * 错误响应状态 默认信息体为：未知异常，请联系管理员
	 * @return 返回当前 {@link Result} 对象
	 */
	public static Result error() {
		return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");
	}

	/**
	 * 错误响应状态
	 * @param msg 错误信息体
	 * @return 返回当前 {@link Result} 对象
	 */
	public static Result error(String msg) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
	}

	/**
	 * 错误响应状态
	 * @param code 错误响应状态码
	 * @param msg 错误信息体
	 * @return 返回当前 {@link Result} 对象
	 */
	public static Result error(int code, String msg) {
		Result result = ok();
		result.put(CODE_TAG, code);
		result.put(MSG_TAG, msg);
		return result;
	}

	/**
	 * 成功响应状态
	 * @param msg 成功信息体
	 * @return 返回当前 {@link Result} 对象
	 */
	public static Result ok(String msg) {
		Result result = ok();
		result.put(MSG_TAG, msg);
		return result;
	}

	/**
	 * 成功响应状态
	 * @param map 需要加入的{@link Map}元素集合
	 * @return 返回当前 {@link Result} 对象
	 */
	public static Result ok(Map<String, Object> map) {
		Result result = ok();
		result.putAll(map);
		return result;
	}

	/**
	 * 成功响应状态
	 * @return 获取新的{@link R}实例对象
	 */
	public static Result ok() {
		return new Result();
	}

	/**
	 * 此方法接收一个{@link Object}对象，默认是put到{@value DATA_TAG}
	 * @param value {@link Object}对象
	 * @return 返回当前 {@link Result} 对象
	 */
	public Result put(Object value) {
		this.put(DATA_TAG, value);
		return this;
	}

	/**
	 * 此方法接收一个{@link String}类型的值作为 key 与{@link Object}对象；
	 * 将得到的值put到当前实例中
	 * @param key {@link String}类型的值
	 * @param value {@link Object}对象
	 * @return 返回当前 {@link Result} 对象
	 */
	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	/**
	 * @return 获取当前 {@link Result} 对象的状态码
	 */
	public Integer getCode() {
		return Integer.parseInt(this.get(CODE_TAG).toString());
	}

}