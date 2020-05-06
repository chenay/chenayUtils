package com.chenay.platform.apk;

import android.util.Log;

import com.chenay.platform.ChyApplication;
import com.chenay.platform.mvp.presenter.IMvpBasePresenterN;
import com.chenay.platform.retrofit.SSLSocketClient;

import java.io.File;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static com.chenay.platform.ChyApplication.logger;


/**
 * @author Y.Chen5
 */
public class ApkPresenter extends IMvpBasePresenterN<ApkView> {

    private static final String TAG = ApkPresenter.class.getName();
    private RetrofitDownManager apkDownManager;
    private RetrofitDownManager.DownloadListener downloadListener = new RetrofitDownManager.DownloadListener() {
        private DownloadInfoEntity downloadInfoEntity;

        @Override
        public void onStart() {
            try {
                Log.d(TAG, "onStart: ");
                downloadInfoEntity = new DownloadInfoEntity();
                getMvpView().httpResponseDownload(downloadInfoEntity.setDloadStart(true).setMsg("开始下载").setProgress(0));
            } catch (Exception e) {
                onError(e);
            }
        }

        @Override
        public void onProgress(int p) {
            try {
                Log.d(TAG, "onProgress:" + p);
                getMvpView().httpResponseDownload(downloadInfoEntity.setDloading(true).setMsg("正在下载").setProgress(p));
            } catch (Exception e) {
                onError(e);
            }
        }

        @Override
        public void onFinish(String path) {
            try {
                Log.d(TAG, "onFinish: " + path);
                apkDownManager = null;
                getMvpView().httpResponseDownload(downloadInfoEntity.setDloadEnd(true).setSuccess(true).setPath(path).setMsg("下载完成").setProgress(100));
            } catch (Exception e) {
                onError(e);
            }
        }

        @Override
        public void onError(Throwable msg) {
            try {
                Log.d(TAG, "onError: " + msg);
                apkDownManager.stopDownload();
                apkDownManager = null;
                if (getMvpView() != null) {
                    getMvpView().httpResponseDownload(downloadInfoEntity.setDloadEnd(true).setSuccess(false).setMsg("下载失败"));
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private OkHttpClient client;

    public void requestDownLoadApk(String url , ApkConfigBean updateConfigBean) {
        try {
            File file = new File(BaseApkActivity.DOWN_FILE_PATH);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    ChyApplication.logger.error(TAG, "下载文件夹创建失败");
                    return;
                }
            }
            String apkName = updateConfigBean.getPath();

            if (client == null) {
                client = new OkHttpClient.Builder()
                        //忽略证书
                        .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                        .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                        .build();
            }
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.baidu.com/").client(client).build();
            ApkModelImpl.BaseApi appServerApi = retrofit.create(ApkModelImpl.BaseApi.class);
            apkDownManager = new RetrofitDownManager(apkName, BaseApkActivity.DOWN_FILE_PATH);
            apkDownManager.setListener(downloadListener);
            appServerApi.downloadFileUrl(url)
                    .enqueue(apkDownManager);
        } catch (Exception e) {
            e.printStackTrace();
            apkDownManager = null;
            logger.error(TAG, "下载失败!");
        }
    }
}
