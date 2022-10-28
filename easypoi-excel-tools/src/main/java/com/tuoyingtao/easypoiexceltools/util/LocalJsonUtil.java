package com.tuoyingtao.easypoiexceltools.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 从本地获取JSON数据的工具类
 * @author tuoyingtao
 * @create 2022-10-28 11:50
 */
public class LocalJsonUtil {

    /**
     * 从指定路径获取JSON并转换为List
     * @param path JSON资源路径
     * @param tClass List 元素类型
     */
    public static <T> List<T> getListFromJson(String path, Class<T> tClass) {
        ClassPathResource resource = new ClassPathResource(path);
        String jsonStr = IoUtil.read(resource.getStream(), StandardCharsets.UTF_8);
        JSONArray jsonArray = new JSONArray(jsonStr);
        return JSONUtil.toList(jsonArray, tClass);
    }
}
