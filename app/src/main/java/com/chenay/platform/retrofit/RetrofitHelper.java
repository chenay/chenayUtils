package com.chenay.platform.retrofit;


import androidx.annotation.NonNull;

import com.chenay.platform.ServerPreference;

import java.io.IOException;

import retrofit2.Retrofit;

/**
 * @author Y.Chen5
 */
public class RetrofitHelper {

    /**
     * @param isEncrypt true 加密 false 不加密方式
     * @return
     * @throws Exception
     */
    public static Retrofit getRetrofit(boolean isEncrypt) throws Exception {
        final String serverIp = ServerPreference.newInstance().getServerIp();
        final String serverPort = ServerPreference.newInstance().getServerPort();
        if (serverIp != null && serverPort != null) {
            return getRetrofit(serverIp, serverPort, isEncrypt);
        } else {
            throw new NullPointerException("服务器IP 或 端口号为空");
        }

    }

    /**
     * @param ip
     * @param port
     * @param isEncrypt true 加密 false 不加密方式
     * @return
     * @throws IOException
     */
    public static Retrofit getRetrofit(@NonNull String ip, @NonNull String port, boolean isEncrypt) throws IOException {

//        InputStream[] open1 = new InputStream[]{BaseApplication.context.getResources().getAssets().open("tti_efactory.cer")};
        if (isEncrypt) {
            return RetrofitFactory.newInstance().getRetrofit(ip, port, null);
        } else {
            return RetrofitFactory.newInstance().getRetrofitNoEncrypt(ip, port, null);
        }
    }

}
