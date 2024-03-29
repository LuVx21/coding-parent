package org.luvx.coding.common;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES CBC PKCS5 模式加密解密
 */
public class AesUtil {
    private static final String charset = "UTF-8";

    /**
     * 加密
     */
    public static String encrypt(String content, String key, String iv) throws Exception {
        // 明文
        byte[] contentBytes = content.getBytes(charset);

        // AES KEY
        byte[] keyBytes = key.getBytes(charset);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // AES IV
        byte[] initParam = iv.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        byte[] byEnd = cipher.doFinal(contentBytes);

        return new String(byEnd);
    }

    /**
     * 解密
     */
    public static String decrypt(String content, String key, String iv)
            throws Exception {
        // 反向解析BASE64字符串为byte数组
        byte[] encryptedBytes = content.getBytes();

        // AES KEY
        byte[] keyBytes = key.getBytes(charset);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // AES IV
        byte[] initParam = iv.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        byte[] byEnd = cipher.doFinal(encryptedBytes);

        // 加密后的byte数组直接转字符串
        return new String(byEnd, charset);
    }

}