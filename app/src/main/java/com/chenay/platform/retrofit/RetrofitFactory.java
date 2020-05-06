package com.chenay.platform.retrofit;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Y.Chen5
 */
public class RetrofitFactory {
    private OkHttpClient.Builder clientBuilder;
    private Retrofit.Builder builder;

    public void setClientBuilder(OkHttpClient.Builder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public OkHttpClient.Builder getClientBuilder() {
        return clientBuilder;
    }

    @NonNull
    public Retrofit.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Retrofit.Builder builder) {
        this.builder = builder;
    }


    public RetrofitFactory() {
        initClient();
    }

    private void initClient() {
        clientBuilder = new OkHttpClient.Builder().
                connectTimeout(60, TimeUnit.SECONDS).
                readTimeout(30, TimeUnit.SECONDS).
                writeTimeout(30, TimeUnit.SECONDS)
                //忽略证书
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier());
    }

    @NonNull
    private Retrofit.Builder getRetrofitBulie(@NonNull URL url) {
        final Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url);
        this.builder = builder;
        return builder;

    }


    public Retrofit getRetrofit(@NonNull String serverIp, @NonNull String port, InputStream... inputStreams) throws MalformedURLException {
        if (inputStreams != null && inputStreams.length > 0) {
            setCertificates(clientBuilder, inputStreams);
        }

        String uri = serverIp + ":" + port;
        final URL url = new URL(uri);
        return getRetrofitBulie(url)
                .client(clientBuilder.build())
                //数据加密
                .addConverterFactory(IGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @NonNull
    public Retrofit getRetrofitNoEncrypt(@NonNull String serverIp, @NonNull String port, InputStream... inputStreams) throws MalformedURLException {
        if (inputStreams != null && inputStreams.length > 0) {
            setCertificates(clientBuilder, inputStreams);
        }

        String uri = serverIp + ":" + port;
        final URL url = new URL(uri);
        return getRetrofitBulie(url)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 通过okhttpClient来设置证书
     *
     * @param clientBuilder OKhttpClient.builder
     * @param certificates  读取证书的InputStream
     */
    public void setCertificates(OkHttpClient.Builder clientBuilder, InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory
                        .generateCertificate(certificate));
                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            clientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单例内部类句柄
     *
     * @return Created by Chenay
     */
    private static class RetrofitManagerHolder {
        private static final RetrofitFactory SINGLETON = new RetrofitFactory();
    }

    /**
     * 获取单例
     *
     * @return Created by Chenay
     */
    public static RetrofitFactory newInstance() {
        return RetrofitManagerHolder.SINGLETON;
    }

    /**
     * 防止反序列化操作破坏单例模式 (条件) implements Serializable
     *
     * @return Created by Chenay
     */
    private Object readResolve() throws ObjectStreamException {
        return RetrofitManagerHolder.SINGLETON;
    }

}
