package com.tuoyingtao.easypoiexceltools.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.tuoyingtao.easypoiexceltools.entity.MemberEntity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 从本地获取JSON数据的工具类
 * @author tuoyingtao
 * @create 2022-10-28 11:50
 */
public class LocalJsonUtil {

    private static Integer currentObjNum = 0;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static InputStream inputStream;

    private static JsonToken  currentJson;

    private static JsonParser parser;



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

    public static List<MemberEntity> readLargerJsonData() {
        List<MemberEntity> memberEntities = null;
        try {
            if (parser == null) {
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/members2.json");
                JsonFactory jsonFactory = new MappingJsonFactory();
                parser = jsonFactory.createParser(inputStream);

                currentJson = parser.nextToken();
                if (currentJson != JsonToken.START_ARRAY) {
                    System.out.println("Error: 当前不是一个JSON数组");
                    return null;
                } else {
                    currentJson = parser.nextToken();
                }
            }

            memberEntities = new ArrayList<>();
            while (currentJson != JsonToken.END_ARRAY) {
                if (currentJson == JsonToken.START_OBJECT) {
                    currentJson = parser.nextToken();
                    while (currentJson != JsonToken.END_OBJECT && currentJson != JsonToken.END_ARRAY) {
                        if (currentObjNum > 1000) {
                            return memberEntities;
                        } else {
                            // 将记录读入树模型，
                            // 这会将解析位置移动到它的末尾
                            JsonNode node = parser.readValueAsTree();
                            // 现在我们可以随机访问对象中的所有内容
                            Long id = Long.parseLong(node.get("id").asText());
                            String username = node.get("username").asText();
                            String nickname = node.get("nickname").asText();
                            Date birthday = simpleDateFormat.parse(node.get("birthday").asText());
                            String phone = node.get("phone").asText();
                            Integer gender = Integer.parseInt(node.get("gender").asText());
                            MemberEntity memberEntity = new MemberEntity(id, username, null, nickname, birthday, phone, null, gender);
                            memberEntities.add(memberEntity);
                            currentJson = parser.nextToken();
                            currentObjNum++;
                        }
                    }
                } else {
                    System.out.println("Error: 记录应该是一个对象: skipping.");
                    parser.skipChildren();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            currentObjNum = 0;
        }
        if (memberEntities != null && memberEntities.size() > 0) {
            return memberEntities;
        }
        return null;
    }

    public static void clearLargerJsonData() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (parser != null) {
                parser.close();
            }
            if (currentJson != null) {
                currentJson = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
