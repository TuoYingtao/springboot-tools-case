package com.compound.commons.core.utils.html;

import com.compound.commons.core.exception.ServiceException;
import com.compound.commons.core.utils.StringUtils;

/**
 * SQL过滤
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 14:15
 * @Version: v1.0.0
 */
public class SQLFilter {
    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     */
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        //去掉'|"|;|\字符
        str = org.springframework.util.StringUtils.replace(str, "'", "");
        str = org.springframework.util.StringUtils.replace(str, "\"", "");
        str = org.springframework.util.StringUtils.replace(str, ";", "");
        str = org.springframework.util.StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

        //判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.indexOf(keyword) != -1) {
                throw new ServiceException("包含非法字符");
            }
        }

        return str;
    }
}
