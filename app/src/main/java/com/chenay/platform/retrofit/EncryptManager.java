package com.chenay.platform.retrofit;

import android.text.TextUtils;
import android.util.Base64;

import com.chenay.platform.utils.EncryptUtil;

import java.io.ObjectStreamException;
import java.io.UnsupportedEncodingException;

import static com.chenay.platform.ChyApplication.logger;

/**
 * @author Y.Chen5
 */
 public class EncryptManager {
    private static final String TAG = "EncryptManager";
    private static boolean debug = false;
    /**
     * 是否加密
     */
    private static boolean isEncrypt = true;
    private static final String CHARSET_NAME = "utf-8";
    /* 加密使用的 key */
    private static String AES_KEY = "";
    /* 加密使用的 IV */
    private static String AES_IV = "";

    /**
     * 获取单例内部类句柄
     *
     * @return Created by Chenay
     */
    private static class EncryptManagerHolder {
        private static final EncryptManager SINGLETON = new EncryptManager();
    }


    /**
     * 获取单例
     *
     * @return Created by Chenay
     */
    public static EncryptManager newInstance() {
        return EncryptManagerHolder.SINGLETON;
    }

    /**
     * 防止反序列化操作破坏单例模式 (条件) implements Serializable
     *
     * @return Created by Chenay
     */
    private Object readResolve() throws ObjectStreamException {
        return EncryptManagerHolder.SINGLETON;
    }

    public EncryptManager() {

    }

    public void init(String key, String iv) {
        AES_KEY = key;
        AES_IV = iv;
    }

    public static synchronized void openDebug() {
        debug = true;
    }

    public static synchronized void openLog() {
//        logger.showLog(true);
        logger.info(TAG, "打开日志!");
    }

    public static synchronized void openEncryp() {
        isEncrypt = true;
    }

    /**
     * 加密
     *
     * @param msg
     * @return
     */
    public String encrypt(String msg) {
        if (!isEncrypt) {
            return msg;
        }
        if (isDebug()) {
            logger.debug(TAG, "convert: 加密前:" + msg);
        }
        // byte[] temp1 = CodeUtils.xorEncode(str.getBytes(Charset.defaultCharset()));
        // msg = new String(temp1);

        // temp1 = CodeUtils.xorDecode(str.getBytes(Charset.defaultCharset()));
        // msg = new String(temp1);

        // final RequestBody requestBody = RequestBody.create(MEDIA_TYPE, content);

        // byte[] temp1 = EncryptUtil.encryptDES(str.getBytes(Charset.defaultCharset()),
        // EncryptUtil.KEY_BYTES);
        try {
            byte[] temp1;
            temp1 = encrypt(msg.getBytes(CHARSET_NAME));
            msg = Base64.encodeToString(temp1, Base64.DEFAULT);
            if (isDebug()) {
                logger.debug(TAG, "convert: 加密后:" + msg);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return msg;
    }

    private byte[] encrypt(byte[] bytes) throws UnsupportedEncodingException {
        if (TextUtils.isEmpty(AES_KEY) || TextUtils.isEmpty(AES_IV)) {
            throw new NumberFormatException("请在application中调用EncryptManager.newInstance().init()");
        }
        byte[] temp1;
        temp1 = EncryptUtil.encryptAES(bytes,
                AES_KEY.getBytes(CHARSET_NAME), AES_IV.getBytes());
        return temp1;
    }

    /**
     * 解密
     *
     * @param msg
     * @return
     */
    public String decrypt(String msg) {
        if (!isEncrypt) {
            return msg;
        }
        try {
            if (isDebug()) {
                logger.debug(TAG, "convert: 解密前:" + msg);
            }
            final byte[] bytes = Base64.decode(msg, Base64.DEFAULT);
            byte[] temp1 = decryptByte(bytes);
            msg = new String(temp1, CHARSET_NAME);
            if (isDebug()) {
                logger.debug(TAG, "convert: 解密后:" + msg);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 解密
     *
     * @param bytes
     * @return
     */
    public byte[] decryptByte(byte[] bytes) throws UnsupportedEncodingException {
        if (TextUtils.isEmpty(AES_KEY) || TextUtils.isEmpty(AES_IV)) {
            throw new NumberFormatException("请在application中调用EncryptManager.newInstance().init()");
        }
        // byte[] temp1 = EncryptUtil.decryptDES(bytes,EncryptUtil.KEY_BYTES);
        byte[] temp1 = EncryptUtil.decryptAES(bytes,
                AES_KEY.getBytes(CHARSET_NAME), AES_IV.getBytes());
        return temp1;

    }

    public boolean isDebug() {
        return debug;
    }


    public boolean isEncrypt() {
        return isEncrypt;
    }
}
