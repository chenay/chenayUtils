package com.chenay.platform.thread;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * @author Y.Chen5
 */
public class ThreadPoolUtil {

    private static final String TAG = "ThreadPoolUtil";

    private static final int CUP_COUNT = Runtime.getRuntime().availableProcessors();
    public static final int KEEP_ALIVE_TIME = 1;
    private static final int CORE_POOL_SIZE = CUP_COUNT + 1;
    public static final int MAXIMUM_POOL_SIZE = CUP_COUNT * 2 + 1;
    private static ExecutorService threadPool;


    private static void initThreadPool(int max) { // 可以在Application中进行配置
        try {
            if (max > 0) {
                max = max < 3 ? 3 : max;
                threadPool = Executors.newFixedThreadPool(max);
            } else {
                threadPool = Executors.newCachedThreadPool();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Logger.d(TAG, "[ThreadPool]ThreadPool init success...max thread: " + max);

    }

    public static void initThreadPool() { // 可以在Application中进行配置
        initThreadPool(CORE_POOL_SIZE);
    }

    public static ExecutorService getInstances() {
        return threadPool;
    }

    public synchronized static <U, R> void execute_(Runtask<U, R> runtask) {
        if (null == threadPool) {
            Logger.getLogger(TAG, "ThreadPool没有被初始化，请在Application中进行初始化操作...");
            return;
        }
//        runtaskWeakReference.onBefore();
        threadPool.execute(runtask);
    }

    public synchronized static void execute_(Runnable runnable) {
        if (null == threadPool) {
            Logger.getLogger(TAG, "ThreadPool没有被初始化，请在Application中进行初始化操作...");
            return;
        }
        threadPool.execute(runnable);
    }

    public static void cancle(Runnable runnable) {
        Future<?> future = threadPool.submit(runnable);
        future.cancel(true);
    }

    public static void schedule(){
        if (null == threadPool) {
            Logger.getLogger(TAG, "ThreadPool没有被初始化，请在Application中进行初始化操作...");
            return;
        }
    }
}
