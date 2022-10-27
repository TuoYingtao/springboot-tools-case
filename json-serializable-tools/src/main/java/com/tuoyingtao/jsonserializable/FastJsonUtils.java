package com.tuoyingtao.jsonserializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * @author tuoyingtao
 * @create 2022-10-19 18:17
 */
public class FastJsonUtils {

    /**
     * JAVA 对象转换成 JSON数据
     * @param obj JAVA 对象
     * @return JSON数据
     */
    public static String getBeanToJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * JSON数据转换成 JAVA对象
     * @param jsonStr  JSON数据
     * @param objClass JAVA对象
     * @param <T> 需要转换的JAVA对象类型
     * @return JAVA对象
     */
    public static <T> T getJsonToBean(String jsonStr, Class<T> objClass) {
        return JSON.parseObject(jsonStr, objClass);
    }

    /**
     * JSON数据转换成集合
     * @param jsonStr  JSON数据
     * @param objClass JAVA对象
     * @param <T> 需要转换的JAVA对象类型
     * @return {@link List} 集合
     */
    public static <T> List<T> getJsonToList(String jsonStr, Class<T> objClass) {
        return JSON.parseArray(jsonStr, objClass);
    }

    /**
     * JSON数据转换成集合
     * @param jsonString JSON数据
     * @param objClass JAVA对象
     * @param <T> JAVA对象类型
     * @return {@link List} 集合
     */
    public static <T> List<T> getJsonToList2(String jsonString, Class<T> objClass) {
        @SuppressWarnings("unchecked")
        List<T> json = JSONArray.parseArray(jsonString, objClass);
        return json;
    }

    /**
     * JSON数据转换成 {@code List<Map<String, Object>>}
     * @param jsonStr JSON数据
     * @return {@code List<Map<String, Object>>}
     */
    public static List<Map<String, Object>> getJsonToListMap(String jsonStr) {
        return JSON.parseObject(jsonStr, new TypeReference<List<Map<String, Object>>>(){
        });
    }

    /**
     *  {@code List<T>} 转 json 保存到数据库
     * @param tList {@link List} 集合
     * @param <T> JAVA对象类型
     * @return JSON 数据
     */
    public static <T> String getListToJson(List<T> tList) {
        return JSON.toJSONString(tList);
    }

    /**
     * JAVA对象类型转换其它类型
     * @param obj JAVA对象
     * @param objClass 转换的目标类型
     * @param <T> JAVA对象类型
     * @return 新类型的JAVA对象
     */
    public static <T> T getConvertObject(Object obj, Class<T> objClass) {
        String beanToJson = getBeanToJson(obj);
        return getJsonToBean(beanToJson, objClass);
    }
}
