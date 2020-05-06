package com.chenay.platform.apk;

/**
 * @author Y.Chen5
 */
public interface ApkModel {

    void httpRequestApkConfig(String url, ApkConfigBean nowConfig, boolean isEncrypt);

    RetrofitDownManager httpRequestDownApk(String url, String downPath, ApkConfigBean apkConfigBean);

     ApkConfigBean getApkConfig();
}
