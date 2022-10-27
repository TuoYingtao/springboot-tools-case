package com.tuoyingtao.jsonserializable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author tuoyingtao
 * @create 2022-10-19 18:18
 */
public class JsonLibUtils {

    /**
     * Bean 转 JSON数组
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String beanToJsonArr(T obj) {
        if (obj == null) {
            return null;
        }
        return obj instanceof String ? (String) obj : JSONArray.fromObject(obj).toString();
    }

    /**
     * Bean 转 JSON
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String beanToJsonObj(T obj) {
        if (obj == null) {
            return null;
        }
        return obj instanceof String ? (String) obj : JSONObject.fromObject(obj).toString();
    }

    /**
     * JSON 转 Bean
     * @param jsonStr
     * @param tClass
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(String jsonStr, Class<T> tClass) {
        if (jsonStr == null || tClass == null) {
            return null;
        }
        return (T) (tClass.getClass().equals(String.class) ? jsonStr : JSONObject.toBean(JSONObject.fromObject(jsonStr), tClass));
    }

    /**
     * JSON 转 {@code List<Bean>}
     * @param jsonStr
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToListBean(String jsonStr, Class<T> tClass) {
        if (jsonStr == null || tClass == null) {
            return null;
        }
        List<T> ts = new ArrayList<>();
        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        Iterator iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            ts.add((T) JSONObject.toBean(JSONObject.fromObject(next), tClass));
        }
        return ts;
    }

    /**
     * JSON 转 {@code Map<Bean>}
     * <pre>
     *  String json = {"data":[{"name":"Luce"},{"name":"Luce"}]}
     *  Map<String, Class> hashMap = new HashMap();
     *  hashMap.put("data", MyBean2.class);
     *  MyBean myBean = JsonLibUtils.jsonToMapBean(myBeanStr, MyBean.class, hashMap);
     * </pre>
     * @param <T>
     * @param jsonStr
     * @param tClass
     * @param map
     * @return
     */
    public static <T> T jsonToMapBean(String jsonStr, Class<T> tClass, Map map) {
        if (jsonStr == null || tClass == null || map == null) {
            return null;
        }
        return (T) JSONObject.toBean(JSONObject.fromObject(jsonStr), tClass, map);
    }

}
