package com.common.core.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author tuoyingtao
 * @create 2022-10-19 18:17
 */
public class JacksonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String TIME_FORMAT = "HH:mm:ss";

    static {
        // JSON 忽略未知字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 反序列化是否会适应DeserializationContext#getTimeZone()提供的时区 （此特性仅对java8的时间/日期有效）
        objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, true);

        // 空对象不要抛出异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);

        // Date、Calendar等序列化为时间格式的字符串(如果不执行以下设置，就会序列化成时间戳格式)
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        // PrettyPrinter 格式化输出
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        // NULL不参与序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 指定时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        // 设置默认日期格式
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_TIME_FORMAT));
    }

    /**
     * 获取 ObjectMapper 实例
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * 对象转Json
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String beanToJson(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Serialization Bean to String error: {}", e);
            return null;
        }
    }

    /**
     * 对象转Json (格式化)
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String beanToJsonPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Serialization Bean to format string error: {}", e);
            return null;
        }
    }

    /**
     * json 转 对象
     * @param jsonStr
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T jsonToBean(String jsonStr, Class<T> tClass) {
        if (jsonStr == null || jsonStr.length() == 0 || tClass == null) {
            return null;
        }
        try {
            return tClass.equals(String.class) ? (T) jsonStr : objectMapper.readValue(jsonStr, tClass);
        } catch (JsonProcessingException e) {
            LOGGER.warn("DeSerialization String to Bean error: {}", e);
            return null;
        }
    }

    /**
     * json 转集合对象
     * @param jsonStr
     * @param tTypeReference
     * @param <T>
     * @return
     */
    public static <T> T jsonToList(String jsonStr, TypeReference<T> tTypeReference) {
        if (jsonStr == null || jsonStr.length() == 0 || tTypeReference == null) {
            return null;
        }
        try {
            return (T) (tTypeReference.getType().equals(String.class) ? jsonStr : objectMapper.readValue(jsonStr, tTypeReference));
        } catch (JsonProcessingException e) {
            LOGGER.warn("DeSerialization String to collection error: {}", e);
            return null;
        }
    }

    /**
     * 在字符串与集合对象转换时使用
     * <pre>
     * JavaType inner = TypeFactory.constructParametricType(Set.class, Integer.class);
     * return TypeFactory.constructParametricType(List.class, inner);
     * </pre>
     * @param jsonStr
     * @param collectionClazz
     * @param elementClazzs
     * @param <T>
     * @return
     */
    public static <T> T jsonToList(String jsonStr, Class<?> collectionClazz, Class<?>... elementClazzs) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClazz, elementClazzs);
        try {
            return objectMapper.readValue(jsonStr, javaType);
        } catch (JsonProcessingException e) {
            LOGGER.warn("DeSerialization String to nested collection error: {}", e);
            return null;
        }
    }
}
