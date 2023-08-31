package com.compound.common.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * JWT 工具类
 * @author tuoyingtao
 * @create 2021-10-18 11:03
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {

    /** JWT加解密使用的密钥 */
    private String secret;

    /** JWT的超期限时间 */
    private Long expiration;

    /** JWT 客户端名称 */
    private String clientHeader;

    /** JWT 管理系统名称 */
    private String systemHeader;

    /**
     * 创建Token
     * @param username 主题信息 可以是用户名或是 JSON 对象
     * @return
     */
    public String generateToken(String username) {
        return generateToken(username,null);
    }

    public String generateToken(String username, Claims claims) {
        if (StringUtils.isEmpty(username)) throw new RuntimeException("主题名不能为空");
        if (StringUtils.isNull(expiration)) throw new RuntimeException("过期时间不能为空");
        if (StringUtils.isEmpty(secret)) throw new RuntimeException("密钥不能为空");
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret);
        return jwtBuilder.compact();

    }

    /** 解析 Token 获取信息体 */
    public Claims tokenParser(String token) {
        if (StringUtils.isEmpty(token)) throw new RuntimeException("token参数不能为空");
        if (StringUtils.isEmpty(secret)) throw new RuntimeException("密钥不能为空");
        Claims body = null;
        try {
            body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw e;
        }
        return body;
    }

    /** 从Token中获取用户名 */
    public String getUserNameFromToken(String token) {
        return tokenParser(token).getSubject();
    }

    /** 获取Token主题中的信息 */
    public Object getTokenBody(String key, String token) {
        return tokenParser(token).get(key);
    }

    /**
     * 验证Token是否过期
     * @param token 用户请求中的令牌
     * @return true：过期了 false：没有过期
     */
    public boolean isExpiration(String token) {
        return tokenParser(token).getExpiration().before(new Date());
    }

    /**
     * 验证 Token 是否失效
     * @param currentToken 用户请求中的令牌
     * @param cacheToken 缓存中存贮的令牌
     * @return 验证 Token 唯一ID（jit）false-同一个凭证 true-不是同一个凭证
     */
    public boolean isExpiration(String currentToken, String cacheToken) {
        Claims currentBody = tokenParser(currentToken);
        Claims cacheBody = tokenParser(cacheToken);
        String currentTokenID = currentBody.get(Claims.ID).toString();
        String cacheTokenID = cacheBody.get(Claims.ID).toString();
        if (!currentTokenID.equals(cacheTokenID)) {
            return true;
        }
        return false;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public String getClientHeader() {
        return clientHeader;
    }

    public void setClientHeader(String clientHeader) {
        this.clientHeader = clientHeader;
    }

    public String getSystemHeader() {
        return systemHeader;
    }

    public void setSystemHeader(String systemHeader) {
        this.systemHeader = systemHeader;
    }
}
