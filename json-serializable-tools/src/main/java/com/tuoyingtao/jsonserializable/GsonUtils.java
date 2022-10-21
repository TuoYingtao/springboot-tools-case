package com.tuoyingtao.jsonserializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author tuoyingtao
 * @create 2022-10-19 18:17
 */
public class GsonUtils {

    private static final Gson GSON;

    private static final JsonParser JSON_PARSER;

    // 不过滤空值
    private static final Gson GSON_NULL;

    static {
        GSON = new GsonBuilder()
                .enableComplexMapKeySerialization() // 当Map的key为复杂对象时,需要开启该方法
//                .serializeNulls() // 当字段值为空或null时，依然对该字段进行序列化
//                .excludeFieldsWithoutExposeAnnotation() // 打开Export注解，但打开了这个注解,副作用，要转换和不转换都要加注解
//                .setPrettyPrinting() // 自动格式化换行
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .disableHtmlEscaping() //防止特殊字符出现乱码
                .create();
        GSON_NULL = new GsonBuilder().enableComplexMapKeySerialization() //当Map的key为复杂对象时,需要开启该方法
                .serializeNulls() //当字段值为空或null时，依然对该字段进行序列化
//                .excludeFieldsWithoutExposeAnnotation()//打开Export注解，但打开了这个注解,副作用，要转换和不转换都要加注解
                .setDateFormat("yyyy-MM-dd HH:mm:ss")//序列化日期格式  "yyyy-MM-dd"
//                .setPrettyPrinting() //自动格式化换行
                .disableHtmlEscaping() //防止特殊字符出现乱码
                .create();
        JSON_PARSER = new JsonParser();
    }

    /**
     * 获取gson解析器
     * @return Gson
     */
    public static Gson getGson() {
        return GSON;
    }

    /**
     * 获取gson解析器 有空值 解析
     * @return Gson
     */
    public static Gson getWriteNullGson() {
        return GSON_NULL;
    }


    /**
     * 根据对象返回json  过滤空值字段
     * @return String
     */
    public static String toJsonStringIgnoreNull(Object object) {
        return GSON.toJson(object);
    }

    /**
     * 根据对象返回json  不过滤空值字段
     * @return String
     */
    public static String toJsonString(Object object) {
        return GSON_NULL.toJson(object);
    }


    /**
     * 将字符串转化对象
     * @param json     源字符串
     * @param classOfT 目标对象类型
     * @param <T> Java 对象类型
     * @return Java 对象
     */
    public static <T> T strToJavaBean(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    /**
     * 将json转化为对应的实体对象
     * @param typeOfT 目标对象类型 new TypeToken<List<T>>() {}.getType()
     *                new TypeToken<List<T>>() {}.getType()
     *                new TypeToken<Map<String, T>>() {}.getType()
     *                new TypeToken<List<Map<String, T>>>() {}.getType()
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    /**
     * 转成list
     * @param gsonString Json 数据
     * @param aClass 目标对象类型
     * @return List 集合
     */
    public static <T> List<T> strToList(String gsonString, Class<T> aClass) {
        return GSON.fromJson(gsonString, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * 转成list中有map的
     * @param gsonString Json 数据
     * @return List<Map<String, T>> 集合
     */
    public static <T> List<Map<String, T>> strToListMaps(String gsonString) {
        return GSON.fromJson(gsonString, new TypeToken<List<Map<String, String>>>() {
        }.getType());
    }

    /**
     * 转成map
     * @param gsonString Json 数据
     * @return Map<String, T> 集合
     */
    public static <T> Map<String, T> strToMaps(String gsonString) {
        return GSON.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
    }
}
