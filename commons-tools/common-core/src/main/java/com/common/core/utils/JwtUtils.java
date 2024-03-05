package com.common.core.utils;

import com.common.core.constant.SecurityConstants;
import com.common.core.constant.TokenConstants;
import com.common.core.text.Convert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT 工具类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-01 16:07:23
 * @Version: v1.0.0
*/
public class JwtUtils {

    /** JWT加解密使用的密钥 */
    public static String secret = TokenConstants.SECRET;

    /** JWT的超期限时间 */
    public static Long expiration = TokenConstants.EXPIRATION;

    /**
     * 从数据声明生成令牌
     * @param claims 身份信息
     * @return Token 令牌
     */
    public static String generateToken(Claims claims) {
        JwtBuilder jwtBuilder = getJwtBuilder(null).setClaims(claims);
        return generateToken(jwtBuilder);
    }

    /**
     * 从数据声明生成令牌
     * @param claims 身份信息
     * @return Token 令牌
     */
    public static String generateToken(Map<String, Object> claims) {
        JwtBuilder jwtBuilder = getJwtBuilder(null).setClaims(claims);
        return generateToken(jwtBuilder);
    }

    /**
     * 从数据声明生成令牌
     * @param subject 主题信息
     * @param claims 身份信息
     * @return Token 令牌
     */
    public static String generateToken(String subject, Claims claims) {
        JwtBuilder jwtBuilder = getJwtBuilder(subject).setClaims(claims);
        return generateToken(jwtBuilder);
    }

    /**
     * 从数据声明生成令牌
     * @param subject 主题信息
     * @param claims 身份信息
     * @return Token 令牌
     */
    public static String generateToken(String subject, Map<String, Object> claims) {
        JwtBuilder jwtBuilder = getJwtBuilder(subject).setClaims(claims);
        return generateToken(jwtBuilder);
    }

    private static JwtBuilder getJwtBuilder(String subject) {
        return Jwts.builder().setSubject(subject);
    }

    private static String generateToken(JwtBuilder jwtBuilder) {
        return jwtBuilder.setHeaderParam("typ", "JWT")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /** 从令牌中获取数据声明 */
    public static Claims parserToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /** 从令牌中获取主题信息 */
    public static String getSubject(String token) {
        return parserToken(token).getSubject();
    }

    /** 根据身份信息获取键值 */
    public static Object getBody(String key, String token) {
        Claims claims = parserToken(token);
        return claims.get(key);
    }

    /**
     * 根据令牌获取用户标识
     * @param token token 令牌
     * @return 用户标识
     */
    public static String getUserKey(String token) {
        Claims claims = parserToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户标识
     * @param claims 身份信息
     * @return 用户标识
     */
    public static String getUserKey(Claims claims) {
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户ID
     * @param token token 令牌
     * @return 用户ID
     */
    public static String getUserId(String token) {
        Claims claims = parserToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据令牌获取用户ID
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserId(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据令牌获取用户名
     * @param token token 令牌
     * @return 用户名
     */
    public static String getUserName(String token) {
        Claims claims = parserToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据令牌获取用户名
     * @param claims 身份信息
     * @return 用户名
     */
    public static String getUserName(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取键值
     * @param claims 身份信息
     * @param key 键
     * @return 值
     */
    public static String getValue(Claims claims, String key) {
        return Convert.toStr(claims.get(key), "");
    }

    /**
     * 验证令牌的有效性
     * @param token token 令牌
     * @return true：有效令牌  false：无效令牌
     */
    public static boolean isVerify(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            return false;
        }
    }

    /**
     * 验证令牌是否过期
     * @param token token 令牌
     * @return true：过期了 false：没有过期
     */
    public static boolean isExpiration(String token) {
        return parserToken(token).getExpiration().before(new Date());
    }

    /**
     * 验证令牌是否失效
     * @param currentToken 用户请求中的令牌
     * @param cacheToken 缓存中存贮的令牌
     * @return 验证令牌唯一ID（jit）true：不是同一个凭证 false：同一个凭证
     */
    public static boolean isExpiration(String currentToken, String cacheToken) {
        Claims currentClaims = parserToken(currentToken);
        Claims cacheClaims = parserToken(cacheToken);
        String currentTokenId = Convert.toStr(currentClaims.get(Claims.ID), "");
        String cacheTokenId = Convert.toStr(cacheClaims.get(Claims.ID), "");
        if (!currentTokenId.equals(cacheTokenId)) {
            return true;
        }
        return false;
    }
}
