package com.custom.util.cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AESUtil
 *
 * AES加密解密工具类
 * @Author Sunset
 * @Version 1.8
 */
public class AESUtil {

    /**
     * 加密字符串
     * @param plainText 明文
     * @param customKey 自定义密钥
     * @return 密文
     */
    public static String encodeCipher(String plainText, String customKey) {
        return doCipher(plainText, customKey, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密字符串
     * @param cipherText 密文
     * @param customKey 自定义密钥
     * @return 明文
     */
    public static String decodeCipher(String cipherText, String customKey) {
        return doCipher(cipherText, customKey, Cipher.DECRYPT_MODE);
    }

    /**
     * 加解密函数(默认)
     *
     * @param contentText   待操作字符串
     * @param customKey     自定义密钥
     * @param operationMode 操作模式
     * @return 处理后字符串
     */
    private static String doCipher(String contentText, String customKey , int operationMode){
        //向量内容 加密操作与解密操作需要使用相同的向量
        final String ivText = "?S0PTMtn&*O6iX+o";
        return doCipher(contentText , customKey , ivText , operationMode);
    }
    /**
     * 加解密函数(自定义向量)
     *
     * @param contentText   待操作字符串
     * @param customKey     自定义密钥
     * @param ivText        向量
     * @param operationMode 操作模式
     * @return 处理后字符串
     */
    private static String doCipher(String contentText, String customKey , String ivText, int operationMode) {
        //加密算法
        final String algorithm = "AES";
        //转换形式声明
        // 格式 “算法/模式/填充 ”
        // 算法 AES / DES / DESede / RSA
        // 模式 ECB（默认）电码本模式 / CBC 密码分组链接模式 / CFB模式 密码反馈模式 / OFB模式 输出反馈模式 / CTR模式 计算器模式
        //   常用
        //     ECB Y 不需要IV 简单、速度快、可并行 N 明文相同时 密文也相同 安全性相对较低
        //     CBC Y 每次加密密钥不同,安全性较高 N 加密无法并行,相对影响运行速度
        //     CTR Y 加解密并行 运行速度快 N 对IV要求严格,需要瞬时IV
        // 填充 NoPadding(不填充) / PKCS5Padding (填充5) / PKCS7Padding (填充7 此填充无法直接使用，需要对JDK进行调整)
        final String statement = "AES/CTR/PKCS5Padding";
        //随机源长度
        final int cipherLength = 256;
        try {
            //密钥生成器 String 算法声明
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            //生成指定长度随机源 int 密钥长度 , SecureRandom 随机源
            keyGenerator.init(cipherLength, new SecureRandom(customKey.getBytes(StandardCharsets.UTF_8)));
            //生成对称密钥
            SecretKey secretKey = keyGenerator.generateKey();
            //生成AES密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), algorithm);
            //生成向量 byte 长度为16的字符串
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivText.getBytes(StandardCharsets.UTF_8));
            //根据转换形式声明生成密码对象
            Cipher cipher = Cipher.getInstance(statement);
            //初始化密码对象确定操作模式 int 操作模式1加密 2解密
            cipher.init(operationMode, secretKeySpec, ivParameterSpec);
            //生成操作字节 加密将明文转为字节 解密将密文base64解密后转为字节
            byte[] contentBytes = operationMode == 1 ? contentText.getBytes(StandardCharsets.UTF_8) : new BASE64Decoder().decodeBuffer(contentText);
            //进行最终字节操作
            byte[] cipherBytes = cipher.doFinal(contentBytes);
            //返回字符串 加密将字节base64编码后返回 解密直接生成String对象返回
            return operationMode == 1 ? new BASE64Encoder().encode(cipherBytes) : new String(cipherBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IOException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 工具demo
     */
    public void aesDemo() {
        String plainText = "一段用于测试的字符串_";
        String customKey = "WIbY!k-r";
        String cipherText = encodeCipher(plainText, customKey);
        String decode = decodeCipher(cipherText, customKey);
        System.out.println("原始字符串："+plainText);
        System.out.println("自定义密钥："+customKey);
        System.out.println("密文："+cipherText);
        System.out.println("明文："+decode);
    }

}
