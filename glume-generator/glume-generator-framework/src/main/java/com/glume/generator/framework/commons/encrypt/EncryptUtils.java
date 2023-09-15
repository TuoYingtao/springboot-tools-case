package com.glume.generator.framework.commons.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 加解密工具类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 14:07:11
 * @Version: v1.0.0
*/
public class EncryptUtils {

    //加密用的Key 可以用字母和数字组成 此处使用AES-128-ECB加密模式，key需要为16位。
    private static final String ENCRYPT_KEY = "makulowcodeyyds1";

    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     * @param content 加密的明文
     * @return 加密后的密文
     */
    public static String encrypt(String content) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(ENCRYPT_KEY.getBytes(), "AES"));
        byte[] b = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        // 采用base64算法进行转码,避免出现中文乱码
        return Base64.encodeBase64String(b);
    }

    /**
     * 解密
     * @param encryptStr 解密的密文
     * @return 解密后的明文
     */
    public static String decrypt(String encryptStr) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(ENCRYPT_KEY.getBytes(), "AES"));
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

}
