package com.tuoyingtao.testools.utils;

import com.compound.common.core.constant.SecurityConstants;
import com.compound.common.core.text.StrFormatter;
import com.compound.common.core.utils.DateUtils;
import com.compound.common.core.utils.ExceptionUtil;
import com.compound.common.core.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: TuoYingtao
 * @Date: 2023-09-01 11:01
 * @Version: v1.0.0
 * @Description: TODO
 */
@SpringBootTest
public class JwtUtilsTest {

    @Test
    public void getToken() {
        Map<String, Object> map = new HashMap<>();
        map.put(SecurityConstants.DETAILS_USER_ID, 1001);
        map.put(SecurityConstants.DETAILS_USERNAME, "张三");
        map.put(SecurityConstants.USER_KEY, "WeChat");
        map.put("mobile", "15207447965");
        String mapToken = JwtUtils.generateToken(map);
        System.out.println(StrFormatter.format("mapToken：{}", mapToken));
        System.out.println(StrFormatter.format("isExpiration：{}", JwtUtils.isExpiration(mapToken)));
        System.out.println(StrFormatter.format("getUserId：{}", JwtUtils.getUserId(mapToken)));
        System.out.println(StrFormatter.format("getUserKey：{}", JwtUtils.getUserKey(mapToken)));
        System.out.println(StrFormatter.format("getUserName：{}", JwtUtils.getUserName(mapToken)));

        String token = JwtUtils.generateToken(map);
        System.out.println(StrFormatter.format("isExpiration：{}", JwtUtils.isExpiration(mapToken, token)));
    }

    @Test
    public void getDateUtilsTest() {
        try {
            Date nowDate = DateUtils.getNowDate();
            System.out.println(StrFormatter.format("nowDate：{}", nowDate));
            Integer i = 1 / 0;
        } catch (Exception e) {
            System.out.println(StrFormatter.format("getExceptionMessage：{}", ExceptionUtil.getExceptionMessage(e)));
            System.out.println(StrFormatter.format("getRootErrorMessage：{}", ExceptionUtil.getRootErrorMessage(e)));
        }
    }
}
