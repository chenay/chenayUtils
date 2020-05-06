package com.chenay.platform.utils;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Y.Chen5
 */
public class EncryptUtil {
    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    // 加密的密钥，构造一定长度的字节数组
    private final static byte[] KEY_BYTES1 = "Vp6flFpGW86g7hi6MhD3Zl2eThJTjPnIjXE4".getBytes();
    private final static int KEY_LENGTH = KEY_BYTES1.length;

    /**
     * 异或运算加密
     *
     * @param input 要加密的内容
     * @return 加密后的数据
     */
    public static byte[] xorEncode(byte[] input) {
        int keyIndex = 0;
        int length = input.length;
        for (int i = 0; i < length; i++) {
            input[i] = (byte) (input[i] ^ KEY_BYTES1[(keyIndex++ % KEY_LENGTH)]);
        }
        return input;
    }

    /**
     * 异或运算解密
     *
     * @param input 要解密的内容
     * @return 解密后的数据
     */
    public static byte[] xorDecode(byte[] input) {
        int keyIndex = 0;
        int length = input.length;
        for (int i = 0; i < length; i++) {
            input[i] = (byte) (input[i] ^ KEY_BYTES1[(keyIndex++ % KEY_LENGTH)]);
        }
        return input;
    }


    ///////////////////////////////////////////////////////////////////////////
    // des
    ///////////////////////////////////////////////////////////////////////////
    /* 加密使用的 key */
    public final static byte[] KEY_BYTES = "Vp6fhlFXKpGW8k6QPRg7Q6Jb7HyAhRi6MIhJ2YtGD3Zl26eTthJTj5PnIjXH5EI4".getBytes();

    /**
     * DES 加密
     *
     * @param content 待加密内容
     * @param key     加密的密钥
     * @return 加密后的字节数组
     */
    public static byte[] encryptDES(byte[] content, byte[] key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            // DES 是加密方式, EBC 是工作模式, PKCS5Padding 是填充模式
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES 解密
     *
     * @param content 待解密内容
     * @param key     解密的密钥
     * @return 解密的数据
     */
    public static byte[] decryptDES(byte[] content, byte[] key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    ///////////////////////////////////////////////////////////////////////////
    //Base64
    ///////////////////////////////////////////////////////////////////////////

    public static void en() {
        // 编码
        String encode = Base64.encodeToString("123456abcdef".getBytes(), Base64.DEFAULT);

        // 解码
        byte[] decodeByte = Base64.decode(encode.getBytes(), Base64.DEFAULT);
        String decode = new String(decodeByte);
    }


    ///////////////////////////////////////////////////////////////////////////
    // MD5
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 计算字符串的 MD5
     *
     * @param text 原文
     * @return 密文
     */
    public static String md5Encode(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & 0xFF);
                if (hexString.length() == 1) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // AES 加密
    ///////////////////////////////////////////////////////////////////////////

    /**
     * AES 加密
     *
     * @param content 待解密内容
     * @param key     密钥
     * @param iv      偏移量
     * @return 解密的数据
     */
    public static byte[] encryptAES(byte[] content, byte[] key, byte[] iv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            // AES 是加密方式, CBC 是工作模式, PKCS5Padding 是填充模式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // IV 是初始向量，可以增强密码的强度
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES 解密
     *
     * @param content 待解密内容
     * @param key     密钥
     * @return 解密的数据
     */
    public static byte[] decryptAES(byte[] content, byte[] key, byte[] iv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    //RSA 加密
    ///////////////////////////////////////////////////////////////////////////

    public static final String RSA = "RSA";
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";

    /**
     * 随机生成 RSA 密钥对
     *
     * @param keyLength 密钥长度, 范围: 512～2048, 一般 1024
     * @return 密钥对
     */
    public static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param data      原文
     * @param publicKey 公钥
     * @return 加密后的数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        try {
            KeyFactory kf = KeyFactory.getInstance(RSA);
            PublicKey keyPublic = kf.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, keyPublic);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 密钥
     * @return 加密后的数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        try {
            KeyFactory kf = KeyFactory.getInstance(RSA);
            PrivateKey keyPrivate = kf.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥解密
     *
     * @param data      待解密数据
     * @param publicKey 密钥
     * @return 解密后的数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        try {
            KeyFactory kf = KeyFactory.getInstance(RSA);
            PublicKey keyPublic = kf.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keyPublic);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param data       待解密的数据
     * @param privateKey 私钥
     * @return 解密后的数据
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] privateKey) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        try {
            KeyFactory kf = KeyFactory.getInstance(RSA);
            PrivateKey keyPrivate = kf.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keyPrivate);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
