package com.tuoyingtao.easypoiexceltools.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.tuoyingtao.easypoiexceltools.entity.MemberEntity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 从本地获取JSON数据的工具类
 * @author tuoyingtao
 * @create 2022-10-28 11:50
 */
public class LocalJsonUtil {
    public static final ThreadLocal<Map<String, Integer>> threadLocal = new ThreadLocal<>();

    private static Integer currentRow = 0;

    private static Integer currentObjNum = 0;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
        try {
            JsonToken currentJson = null;
            JsonFactory jsonFactory = new MappingJsonFactory();
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/members2.json");
            JsonParser parser = jsonFactory.createParser(inputStream);

            Map<String, Integer> threadLocalMap = threadLocal.get();
            if (threadLocalMap == null) {
                threadLocalMap = new HashMap<>();
                threadLocalMap.put("targetRow", 1);
            }
            Integer targetRow = threadLocalMap.get("targetRow");
            if (targetRow != 1) {
                while (((UTF8StreamJsonParser) parser).getTokenLineNr() < targetRow) {
                    if (currentJson == JsonToken.START_OBJECT) {
                        parser.readValueAsTree();
                        currentJson = getNextToken(parser);
                    } else {
                        currentJson = getNextToken(parser);
                    }
                }
            } else {
                currentJson = getNextToken(parser);
                if (currentJson != JsonToken.START_ARRAY) {
                    System.out.println("Error: 当前不是一个JSON数组");
                    return null;
                } else {
                    currentJson = getNextToken(parser);
                }
            }

            List<MemberEntity> memberEntities = new ArrayList<>();
            while (currentJson != JsonToken.END_ARRAY) {
                if (currentJson == JsonToken.START_OBJECT) {
                    currentJson = getNextToken(parser);
                    while (currentJson != JsonToken.END_OBJECT && currentJson != JsonToken.END_ARRAY) {
                        if (currentObjNum > 500) {
                            threadLocalMap.put("targetRow", ((UTF8StreamJsonParser) parser).getTokenLineNr());
                            threadLocal.set(threadLocalMap);
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
                            currentJson = getNextToken(parser);
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
        return null;
    }

    private static JsonToken getNextToken(JsonParser parser) throws IOException {
        return parser.nextToken();
    }
}
