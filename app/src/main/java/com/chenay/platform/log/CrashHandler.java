package com.chenay.platform.log;

import android.util.Log;

import com.chenay.platform.log.impl.CrashFileLogImpl;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 为抓捕异常处理
 *
 * @author Y.Chen5
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";


    /**
     *获取单例内部类句柄
     * @return
     *
     * Created by Chenay
     */
    private static class CrashHandlerHolder {
            private static final CrashHandler SINGLETON = new CrashHandler();
        }
    /**
     *获取单例
     * @return
     *
     * Created by Chenay
     */
    public static CrashHandler newInstance() {
        return CrashHandlerHolder.SINGLETON;
    }
    /**
     * 防止反序列化操作破坏单例模式 (条件) implements Serializable
     *@return
     * Created by Chenay
     */
    private Object readResolve() throws ObjectStreamException {
        return CrashHandlerHolder.SINGLETON;
    }

    /**
     * 应用默认的处理器
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;


    private CrashFileLogImpl crashFileLog;

    public CrashFileLogImpl getCrashFileLog() {
        return crashFileLog;
    }

    public void setCrashFileLog(CrashFileLogImpl crashFileLog) {
        this.crashFileLog = crashFileLog;
    }

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                CrashHandler.this.handleException(t, e);
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        mDefaultHandler.uncaughtException(t, e);
    }

    /**
     * 初始化
     *
     */
    public void start() {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置当前CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        // 日志存放路径


    }


    private void dumpExceptionToSDCard(Throwable e) {
        /* 获得错误信息字符串 */
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String result = sw.toString();
        try {
            sw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        pw.close();
        writtenToSDCard(result);
    }


    /**
     * 写入SD卡中
     *
     * @param result
     * @return 写入成功，返回文件名
     */

    private String writtenToSDCard(String result) {

            crashFileLog.saveLog(result);

        return null;
    }

    /**
     * 处理异常
     *
     * @param t
     * @param e
     * @return false 未处理 true 已经处理
     */
    private void handleException(Thread t, Throwable e) {
        Log.e(TAG, "handleException: 保存一次到日志文件");
        dumpExceptionToSDCard(e);
    }


}
