package com.chenay.platform.http;

import androidx.annotation.NonNull;

import com.chenay.platform.ChyApplication;
import com.chenay.platform.apk.DefaultTimeConfig;
import com.chenay.platform.device.DevicesMessage;
import com.chenay.platform.device.LanguageHelper;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * HTTP请求的一些头信息
 * @author Y.Chen5
 */
public class HttpHeadMsgInterceptor implements Interceptor {



    private long preTime = 0;

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final long timeMillis = System.currentTimeMillis();
        String device;
        final long l = timeMillis - preTime;
        if (l > DefaultTimeConfig.networkDelayTime) {
            preTime = timeMillis;
            DevicesMessage.update(ChyApplication.context);
        }
        final String languageTag = LanguageHelper.getLanguageTag(ChyApplication.context);
        device = new Gson().toJson(DevicesMessage.DEVICES_INFOS);
        device = URLEncoder.encode(device, "UTF-8");
        Request request = chain.request();
        Request.Builder builder = request.newBuilder()
                .header("languageTag", languageTag)
                .header("Accept-Language",languageTag)
                .header("device", device);

        return chain.proceed(builder.build());
    }
}
