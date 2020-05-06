package com.chenay.platform.apk;

import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chenay.platform.ChyApplication;
import com.chenay.platform.msg.ResponseEntity;
import com.chenay.platform.retrofit.EncryptManager;
import com.chenay.platform.retrofit.RetrofitHelper;
import com.chenay.platform.utils.R;
import com.chenay.platform.utils.ResourcesUtils;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

import static com.chenay.platform.ChyApplication.logger;

/**
 * @author Y.Chen5
 */
public class ApkModelImpl extends ViewModel implements ApkModel  {
    private AlertDialog dialog;

    private ApkModelListener listener;

    private static final String TAG = ApkModelImpl.class.getName();

    public interface BaseApi {
        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @POST
        Observable<ResponseEntity<ApkConfigBean>> checkApkConfig(@Url String url, @Body ApkConfigBean configBean);

        @Streaming
        @GET()
        Call<ResponseBody> downloadFileUrl(@Url String fileUrl);
    }

    public interface ApkModelListener {

        void doToastMsg(String stringBuffer);

        void doUploadApk(String nowVersionName, ApkConfigBean apkConfigBean);
    }

    public void setListener(@NonNull ApkModelListener listener) {
        this.listener = listener;
    }



    /**
     * apk下载器
     */
    private RetrofitDownManager apkDownManager;
    private RetrofitDownManager.DownloadListener downloadListener = new RetrofitDownManager.DownloadListener() {


        @Override
        public void onStart() {
            try {
                Log.d(TAG, "onStart: ");
                DownloadInfoEntity downloadInfoEntity = new DownloadInfoEntity();
                downloadInfoEntity.setDloadStart(true).setMsg("开始下载").setProgress(0);
                getDownLiveData().postValue(downloadInfoEntity);
            } catch (Exception e) {
                onError(e);
            }
        }

        @Override
        public void onProgress(int p) {
            try {
                Log.d(TAG, "onProgress:" + p);
                DownloadInfoEntity downloadInfoEntity = new DownloadInfoEntity();
                downloadInfoEntity.setDloading(true).setMsg("正在下载").setProgress(p);
                getDownLiveData().postValue(downloadInfoEntity);
            } catch (Exception e) {
                onError(e);
            }
        }

        @Override
        public void onFinish(String path) {
            try {
                Log.d(TAG, "onFinish: " + path);
                apkDownManager = null;
                DownloadInfoEntity downloadInfoEntity = new DownloadInfoEntity();
                downloadInfoEntity.setDloadEnd(true).setSuccess(true).setPath(path).setMsg("下载完成").setProgress(100);
                getDownLiveData().postValue(downloadInfoEntity);
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
                DownloadInfoEntity downloadInfoEntity = new DownloadInfoEntity();
                downloadInfoEntity.setDloadEnd(true).setSuccess(false).setMsg("下载失败");
                getDownLiveData().postValue(downloadInfoEntity);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private MutableLiveData<DownloadInfoEntity> downloadLiveData;
    @NonNull
    public MutableLiveData<DownloadInfoEntity> getDownLiveData() {
        if (downloadLiveData==null) {
            downloadLiveData = new MutableLiveData<>();
        }
        return downloadLiveData;
    }

    /**
     * Role:取得程序的当前版本<BR>
     */
    @Override
    public ApkConfigBean getApkConfig() {
        int verCode = 0;
        String verName = null;
        ApkConfigBean configBean = null;
        try {
            configBean = new ApkConfigBean();
            String packageName = ChyApplication.context.getPackageName();
            verCode = ChyApplication.context.getPackageManager()
                    .getPackageInfo(packageName, 0).versionCode;
            verName = ChyApplication.context.getPackageManager()
                    .getPackageInfo(packageName, 0).versionName;
            final ApkConfigBean.ApkInfoBean apkInfo = new ApkConfigBean.ApkInfoBean();
            apkInfo.setVersionCode(verCode);
            apkInfo.setVersionName(verName);
            final ApkConfigBean.PropertiesBean properties = new ApkConfigBean.PropertiesBean();
            properties.setPackageId(packageName);
            configBean.setProperties(properties);
            configBean.setApkInfo(apkInfo);
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println("no");
        }
        logger.debug(TAG, "verCode" + verCode + "===" + "verName" + verName);
        return configBean;
    }

    @Override
    public void httpRequestApkConfig(String url, final ApkConfigBean nowConfig, boolean isEncrypt) {
        try {
            Retrofit retrofit = RetrofitHelper.getRetrofit(isEncrypt);
            BaseApi serverApi = retrofit.create(BaseApi.class);
            Observable<ResponseEntity<ApkConfigBean>> observable = serverApi.checkApkConfig(url, nowConfig);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<ResponseEntity<ApkConfigBean>>() {
                        @Override
                        public void onNext(ResponseEntity<ApkConfigBean> messageEntity) {
                            if ("1".equals(messageEntity.getRtnCode())) {
                                List<ApkConfigBean> beanList = messageEntity.getBeanList();
                                httpResponseCheckApk(nowConfig, beanList, true);
                            } else {
                                logger.debug(TAG, "没有找到更新的配置文件");
                                if (listener != null) {
                                    listener.doToastMsg(ResourcesUtils.getString(R.string.str_update_apk_msg2));
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public RetrofitDownManager httpRequestDownApk(String url, String downPath, ApkConfigBean apkConfigBean) {
        try {
            File file = new File(downPath);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    logger.error(TAG, "文件夹创建失败!");
                    return null;
                }
            }
            String apkName = apkConfigBean.getPath();
            String pkgName = apkConfigBean.getProperties().getPackageId();

            String tempAppUrl = url;

            if (true) {
                //从wms服务器获取对应更新的apk
                final String key = "http://10.64.20.204:8080/fmms/api";
                if (tempAppUrl.contains(key)) {
                    tempAppUrl = tempAppUrl.replace(key, "https://10.34.0.157:8443/bootwms/");
                } else {
                    final String prodKey = "https://factorygomobile.ttigroup.com:443/fmms/api";
                    if (tempAppUrl.contains(prodKey)) {
                        tempAppUrl = tempAppUrl.replace(prodKey, "https://10.34.0.87:2996/bootwms/");
                    }
                }
                String pkg = EncryptManager.newInstance().encrypt(pkgName);
                String apkNa = EncryptManager.newInstance().encrypt(apkName);
                pkg = URLEncoder.encode(pkg, "utf-8");
                apkNa = URLEncoder.encode(apkNa, "utf-8");
                url = tempAppUrl + "?url=" + pkg + "&name=" + apkNa;
                if (TextUtils.isEmpty(url)) {
                    return null;
                }

            } else {
                String path = "apks" + File.separator + pkgName + File.separator + apkName;
                path = URLEncoder.encode(EncryptManager.newInstance().encrypt(path), "utf-8");
                url = tempAppUrl + "?path=" + path;
            }


            Retrofit retrofit = RetrofitHelper.getRetrofit(false);
            BaseApi appServerApi = retrofit.create(BaseApi.class);
            apkDownManager = new RetrofitDownManager(apkName, downPath);
            apkDownManager.setListener(downloadListener);
            appServerApi.downloadFileUrl(url)
                    .enqueue(apkDownManager);
        } catch (Exception e) {
            e.printStackTrace();
            apkDownManager = null;
            logger.error(TAG, "下载失败!");
        }

        return apkDownManager;
    }


    private void httpResponseCheckApk(ApkConfigBean nowConfig, List<ApkConfigBean> beanList, boolean b) {

        final String nowVersionName = nowConfig.getApkInfo().getVersionName();
        final int nowVersionCode = nowConfig.getApkInfo().getVersionCode();


        if (beanList == null || (beanList.size() == 0)) {
            String stringBuffer = String.format(ResourcesUtils.getString(R.string.str_update_apk_msg1), nowVersionName);
            if (listener != null) {
                listener.doToastMsg(stringBuffer);
            }
            return;
        }
        for (int i = 0; i < beanList.size(); i++) {
            ApkConfigBean ApkConfigBean = beanList.get(i);
            ApkConfigBean.ApkInfoBean apkInfo = ApkConfigBean.getApkInfo();
            String apkName = ApkConfigBean.getPath();

            if (apkInfo != null && apkName != null) {
                long newVersionCode = apkInfo.getVersionCode();
                if (newVersionCode > nowVersionCode) {
                    if (ApkConfigBean.getProperties().getPackageId() == null) {
                        ApkConfigBean.getProperties().setPackageId(nowConfig.getProperties().getPackageId());
                    }
                    if (listener != null) {
                        listener.doUploadApk(nowVersionName, ApkConfigBean);
                    }
                    break;
                } else {
                    String stringBuffer = String.format(ResourcesUtils.getString(R.string.str_update_apk_msg1), nowVersionName);
                    if (listener != null) {
                        listener.doToastMsg(stringBuffer);
                    }
                }
            }
            break;
        }

    }
}
