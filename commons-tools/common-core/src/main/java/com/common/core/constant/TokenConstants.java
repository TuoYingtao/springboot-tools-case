package com.common.core.constant;

/**
 * Token的Key常量
 *
 * @Author: TuoYingtao
 * @Date: 2023-08-31 15:14
 * @Version: v1.0.0
 */
public class TokenConstants {

    /**
     * 令牌的超期限时间(60*60*24*1) 1天
     */
    public final static Long EXPIRATION = 86400L;

    /**
     * 令牌自定义标识
     */
    public static final String AUTHENTICATION = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String PREFIX = "Bearer ";

    /**
     * 令牌秘钥
     */
    public final static String SECRET = "abcdefghijklmnopqrstuvwxyz";
}
