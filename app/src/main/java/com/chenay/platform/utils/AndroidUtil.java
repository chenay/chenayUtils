package com.chenay.platform.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.chenay.platform.ChyApplication.context;

/**
 * @author Y.Chen5
 */
public class AndroidUtil {


    private static String AppRoot = null;

    public static void init(String appRoot) {
        AppRoot = appRoot;
    }

    /**
     * 获取APP根目录
     *
     * @return
     */
    private static String getAppDir() {
        if (TextUtils.isEmpty(AppRoot)) {
            throw new NullPointerException("请在Application类中调用 FileUtils.init(xxx)初始化!");
        }
        return Environment.getExternalStorageDirectory() + "/" + AppRoot + "/";
    }


    public static String getCacheDir() {
        String dir = getAppDir() + "cache/";
        return FileUtils.mkdirs(dir);
    }

    public static String getLogDir() {
        String dir = getAppDir() + "log/";
        return FileUtils.mkdirs(dir);
    }
    /**
     * 判断App是否是Debug版本
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppDebug() {
        if (StringUtil.isSpace(context.getPackageName())) {
            return false;
        }
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void setAndroidSystemTime(Context mContext){
        String time = "2019-01-17 17:30:54";
        SimpleDateFormat simpleDateFormat = null;
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            long when = calendar.getTimeInMillis();
            ((AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    /**
     * 屏幕唤醒
     * @param context
     */
    public static void wakeUpAndUnlock(Context context) {
        //屏锁管理器
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

    /**
     * 获取屏幕亮度默认亮度值
     * @param context
     * @return
     */
    public static float getScreenLight(Activity context) {
       return context.getWindow().getAttributes().screenBrightness;
    }

    /**
     * 设置亮度
     *
     * @param context
     * @param light
     */
    public static void setScreenLight(Activity context, int light) {
        WindowManager.LayoutParams localLayoutParams = context.getWindow().getAttributes();
        localLayoutParams.screenBrightness = (light / 255.0F);
        context.getWindow().setAttributes(localLayoutParams);
    }

}
