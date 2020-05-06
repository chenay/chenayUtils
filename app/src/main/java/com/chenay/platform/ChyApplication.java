package com.chenay.platform;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chenay.platform.http.HttpHeadMsgInterceptor;
import com.chenay.platform.http.HttpLoggingInterceptor;
import com.chenay.platform.log.CrashHandler;
import com.chenay.platform.log.impl.CrashFileLogImpl;
import com.chenay.platform.log.impl.DefaultLogger;
import com.chenay.platform.retrofit.EncryptManager;
import com.chenay.platform.retrofit.RetrofitFactory;
import com.chenay.platform.thread.ThreadPoolUtil;
import com.chenay.platform.utils.AndroidUtil;
import com.chenay.platform.utils.FileUtils;
import com.chenay.platform.utils.PreferenceUtil;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.chenay.platform.utils.AndroidUtil.isAppDebug;

/**
 * @author Y.Chen5
 */
public abstract class ChyApplication extends Application {
    private static final String TAG = ChyApplication.class.getName();
    public static DefaultLogger logger;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private boolean isOpenHttpLog = false;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initLog(null);
        PreferenceUtil.install(this);
        ThreadPoolUtil.initThreadPool();
        initConfig();
    }

    protected void initLog(Map<String, String> map) {
        copyProperties("properties/log.properties");
        logger = new DefaultLogger(LoggerFactory.getLogger(this.getClass()));
        if (isAppDebug()) {
            logger.setLevel(DefaultLogger.DEBUG);
        } else {
            logger.setLevel(DefaultLogger.INFO);
        }
        if (map != null) {
            updateLogProperties(map);
        }
        initCrashLog();
    }

    /**
     * 默认配置
     * 初始错误日志记录
     */
    private void initCrashLog() {
        CrashFileLogImpl crashFileLog = new CrashFileLogImpl("crash_" + this.getPackageName());
        CrashHandler crashHandler = CrashHandler.newInstance();
        crashHandler.setCrashFileLog(crashFileLog);
        crashHandler.start();
    }

    private void updateCrashLog(String crashLogPath) {
        CrashFileLogImpl crashFileLog = new CrashFileLogImpl(crashLogPath + "_" + this.getPackageName());
        CrashHandler crashHandler = CrashHandler.newInstance();
        crashHandler.setCrashFileLog(crashFileLog);
    }

    /**
     * 默认的日志配置
     * 将日志文件配置文件拷贝到对应的目录
     *
     * @param fileName
     */
    private void copyProperties(String fileName) {
        try {
            final File file = new File("/storage/emulated/0/chenay/log.properties");
            if (file.exists()) {
                return;
            }
            if (FileUtils.creatFile(file)) {
                InputStream in = getResources().getAssets().open(fileName);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = in.read(buffer)) != -1) {
                    // buffer字节
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();// 刷新缓冲区
                in.close();
                fos.close();
            } else {
                Log.d(TAG, "copyProperties:" + file.getAbsolutePath() + "创建失败!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新日志文件配置的一些信息
     *
     * @param map
     */
    public void updateLogProperties(@NonNull Map<String, String> map) {
        final File file = new File("/storage/emulated/0/chenay/log.properties");
        FileUtils.setPropertiesValue(file, map);
        final String crashFileName = map.get("crashFileName");
        if (TextUtils.isEmpty(crashFileName)) {
            updateCrashLog(crashFileName);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        context = base;
        super.attachBaseContext(base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void initConfig() {
        configHttpServer();
        configEncrypt();
    }

    /**
     * Retrofit 配置服务器地址
     * ServerPreference.newInstance().setServerIp("http://192.168.164.196");
     * ServerPreference.newInstance().setServerPort("8686");
     */
    protected abstract void configHttpServer();

    /**
     * 加密使用的 key
     * String aesKey = "KUbHwTqBy6TBQ2gM";
     * 加密使用的 IV
     * String aesIv = "pIbF6GR3XEN1PG06";
     * EncryptManager.newInstance().init(aesKey, aesIv);
     */
    protected abstract void configEncrypt();


    protected void openHttpLog() {
        if (!isOpenHttpLog) {
            /* debug时完整输出 http*/
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            RetrofitFactory.newInstance().getClientBuilder().addInterceptor(loggingInterceptor);
            isOpenHttpLog = true;
        }
    }

    protected void addHttpHeadMsg() {
        RetrofitFactory.newInstance().getClientBuilder().addInterceptor(new HttpHeadMsgInterceptor());
    }

    protected void openAppLog() {
        if (AndroidUtil.isAppDebug()) {
            logger.setLevel(DefaultLogger.DEBUG);
            EncryptManager.openDebug();
            EncryptManager.openLog();
            openHttpLog();
        } else {
            logger.setLevel(DefaultLogger.INFO);
        }
    }

}
