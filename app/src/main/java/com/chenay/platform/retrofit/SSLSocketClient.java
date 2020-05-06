package com.chenay.platform.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Y.Chen5 on 6/25/2018.
 */

public class SSLSocketClient {
    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSLSocketFactory()
    {
        try
        {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    //获取TrustManager
    private static TrustManager[] getTrustManager()
    {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()
        {


            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[]{};
            }
        }};
        return trustAllCerts;
    }

    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier()
    {
        HostnameVerifier hostnameVerifier = new HostnameVerifier()
        {
            @Override
            public boolean verify(String s, SSLSession sslSession)
            {
                return true;
            }
        };
        return hostnameVerifier;
    }

    public static SSLSocketFactory getSSLSocketFactory(Context context, int... certificates) {

        if (context == null) {
            return null;
        }

        //CertificateFactory用来证书生成
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            //Create a KeyStore containing our trusted CAs
            final String defaultType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(defaultType);
            keyStore.load(null);
            for (int i = 0; i < certificates.length; i++) {
                //读取本地证书

                InputStream is = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i) + "tti", certificateFactory.generateCertificate(is));
                is.close();

                readX509CerFile(context, certificates[i]);
            }
            //Create a TrustManager that trusts the CAs in our keyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //Create an SSLContext that uses our TrustManager
//            SSLContext sslContext = SSLContext.getInstance("TLS");
            SSLContext sslContext = SSLContext.getInstance("TLSv1", "AndroidOpenSSL");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static SSLSocketFactory getSocketFactoryBks(Context context, int... certificates) {


        try {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            for (int i = 0; i < certificates.length; i++) {
                InputStream is = context.getResources().openRawResource(certificates[i]);
                keyStore.load(is, "chy1224".toCharArray());
            }
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "chy1224".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }


    /***
     * 读取*.cer公钥证书文件， 获取公钥证书信息
     * @author xgh
     */
    public static void readX509CerFile(Context context, int certificates) throws Exception {


        try {
            // 读取证书文件

            InputStream inStream = context.getResources().openRawResource(certificates);

            // 创建X509工厂类
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //CertificateFactory cf = CertificateFactory.getInstance("X509");
            // 创建证书对象
            X509Certificate oCert = (X509Certificate) cf
                    .generateCertificate(inStream);
            inStream.close();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
            String info = null;
            // 获得证书版本
            info = String.valueOf(oCert.getVersion());
            System.out.println("证书版本:" + info);
            // 获得证书序列号
            info = oCert.getSerialNumber().toString(16);
            System.out.println("证书序列号:" + info);
            // 获得证书有效期
            Date beforedate = oCert.getNotBefore();
            info = dateformat.format(beforedate);
            System.out.println("证书生效日期:" + info);
            Date afterdate = oCert.getNotAfter();
            info = dateformat.format(afterdate);
            System.out.println("证书失效日期:" + info);
            // 获得证书主体信息
            info = oCert.getSubjectDN().getName();
            System.out.println("证书拥有者:" + info);
            // 获得证书颁发者信息
            info = oCert.getIssuerDN().getName();
            System.out.println("证书颁发者:" + info);
            // 获得证书签名算法名称
            info = oCert.getSigAlgName();
            System.out.println("证书签名算法:" + info);

        } catch (Exception e) {
            System.out.println("解析证书出错！");
            e.printStackTrace();
        }
    }

    public static SSLSocketFactory getSSLSocketFactoryAll() {

        try {

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                    for (X509Certificate cert : chain) {
//                        cert.checkValidity();
//                        try {
//                            cert.verify(((X509Certificate) ca).getPublicKey());
//                        } catch (NoSuchAlgorithmException e) {
//                            e.printStackTrace();
//                        } catch (InvalidKeyException e) {
//                            e.printStackTrace();
//                        } catch (NoSuchProviderException e) {
//                            e.printStackTrace();
//                        } catch (SignatureException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};

            //Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            return sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
