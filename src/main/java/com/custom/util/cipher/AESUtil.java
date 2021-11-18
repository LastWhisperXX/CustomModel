package com.custom.util.cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
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
     * 加密形式
     */
    private static final String KEY_AES = "AES";

    /**
     * 加密形式
     */
    private static final String KEY_AES_CBC = "AES/CBC/PKCS5Padding";

    /**
     * 加密编码
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key 加密密码
     * @return 密文
     */
    public static String encrypt(String data, String key) {
        return doAES(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key 解密密钥
     * @return 明文
     */
    public static String decrypt(String data, String key) {
        return doAES(data, key, Cipher.DECRYPT_MODE);
    }

    /**
     * 加解密
     *
     * @param data 待处理数据
     * @param key  密钥
     * @param mode 加解密mode
     * @return 已处理数据
     */
    private static String doAES(String data, String key, int mode) {
        try {
            if ("".equals(data) || "".equals(key) || data == null || key == null) {
                return null;
            }
            //判断是加密还是解密
            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            //true 加密内容 false 解密内容
            if (encrypt) {
                content = data.getBytes(DEFAULT_CHARSET);
            } else {
                content = parseHexStr2Byte(data);
            }
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_AES);
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            kgen.init(128, new SecureRandom(key.getBytes()));
            //3.产生原始对称密钥
            SecretKey secretKey = kgen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] enCodeFormat = secretKey.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(KEY_AES);
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(mode, keySpec);
            assert content != null;
            byte[] result = cipher.doFinal(content);
            if (encrypt) {
                //将二进制转换成16进制
                return parseByte2HexStr(result);
            } else {
                return new String(result, DEFAULT_CHARSET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf 二进制字符串
     * @return 16进制字符串
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr 16进制字符串
     * @return 二进制字符串
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 运行测试
     */
    public static void runTest() {
        String content = "{'repairPhone':'18547854787','customPhone':'12365478965','captchav':'58m7'}";
        String KEY = "123456";
        System.out.println("加密前：" + content);
        System.out.println("加密密钥和解密密钥：" + KEY);
        String encrypt = encrypt(content, KEY);
        System.out.println("加密后：" + encrypt);
        String decrypt = decrypt(encrypt, KEY);
        System.out.println("解密后：" + decrypt);
    }


    /**
     * 加密
     * @param cipherText 明文
     * @param secretKey 密钥
     * @return 密文
     * @throws Exception 异常
     */
    public static String encryptTypeCBC(String cipherText, String secretKey) throws Exception {
        if (secretKey == null) {
            System.out.print("Key为空null");
            return null;
        } else if (secretKey.length() < 16) {
            System.out.print("Key长度小于16位");
            return null;
        } else {
            Cipher cipher = custom(secretKey,1);
            byte[] encrypted = cipher.doFinal(cipherText.getBytes());
            return (new BASE64Encoder()).encode(encrypted);
        }
    }

    /**
     * 解密
     * @param cipherText 密文
     * @param secretKey 密钥
     * @return 明文
     * @throws Exception 异常
     */
    public static String decryptTypeCBC(String cipherText, String secretKey) throws Exception {
        if (secretKey == null) {
            return null;
        } else if (secretKey.length() < 16) {
            System.out.print("Key长度小于16位");
            return null;
        } else {
            Cipher cipher = custom(secretKey,2);
            byte[] encrypted = (new BASE64Decoder()).decodeBuffer(cipherText);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original);
        }
    }

    /**
     * 提取共用量
     * @param secretKey 密钥
     * @param cipherType 密文操作
     * @return 密文对象
     * @throws Exception 异常
     */
    private static Cipher custom( String secretKey , int cipherType)throws Exception {
        secretKey = secretKey.length() % 16 == 0 ? secretKey : secretKey.substring(0, 16);
        byte[] raw = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec sks = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(secretKey.substring(0, 16).getBytes());
        cipher.init(cipherType, sks, iv);
        return cipher;
    }

}