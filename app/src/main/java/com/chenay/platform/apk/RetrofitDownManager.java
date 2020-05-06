package com.chenay.platform.apk;


import android.util.Log;

import androidx.annotation.NonNull;

import com.chenay.platform.thread.ThreadPoolUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author Y.Chen5
 */
public class RetrofitDownManager implements Callback<ResponseBody> {
    private static final String TAG = "RetrofitDownManager";
    private String name;
    private String path;
    private DownloadListener listener;
    private boolean stop;
    private FileDownloadRun runnable;

    public RetrofitDownManager(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public RetrofitDownManager setListener(DownloadListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        runnable = new FileDownloadRun(response, listener);
        ThreadPoolUtil.execute_(runnable);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }

    public void stopDownload() {
        this.stop = true;
        if (runnable != null) {
            ThreadPoolUtil.cancle(runnable);
        }
    }


    public interface DownloadListener {
        void onStart();

        void onProgress(int p);

        void onFinish(String path);

        void onError(Throwable e);
    }

    public class FileDownloadRun implements Runnable {
        Response<ResponseBody> mResponseBody;
        DownloadListener mDownloadApkListener;

        public FileDownloadRun(Response<ResponseBody> responseBody,
                               final DownloadListener downloadApkListener) {
            mResponseBody = responseBody;
            mDownloadApkListener = downloadApkListener;
        }

        @Override
        public void run() {
            writeResponseBodyToDisk(mResponseBody.body(), mDownloadApkListener);
        }
    }


    /**
     * @param body
     * @param downloadListener
     * @return
     */
    private boolean writeResponseBodyToDisk(ResponseBody body, final DownloadListener downloadListener) {
        if (body==null) {
            return false;
        }
        stop = false;
        if (downloadListener != null) {
            downloadListener.onStart();
        }
        try {
            // 改成自己需要的存储位置
            File file = new File(path, name);
            Log.e(TAG, "writeResponseBodyToDisk() file=" + file.getPath());
            if (file.exists()) {
                file.delete();
            }
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                int preP = 0;
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (!stop) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    //计算当前下载百分比，并经由回调传出
                    if (downloadListener != null) {
                        final int p = (int) (100 * fileSizeDownloaded / fileSize);
                        if (p % 1 == 0) {
                            if (p == preP) {
                            } else {
                                downloadListener.onProgress(p);
                                preP = p;
                            }
                        }
                    }
                }

                if (downloadListener != null ) {
                    if (preP == 100) {
                        downloadListener.onFinish(file.getPath());

                    } else {
                        downloadListener.onError(new Throwable("下载失败!"));
                    }
                }
                outputStream.flush();

                return true;
            } catch (IOException e) {
                if (downloadListener != null) {
                    downloadListener.onError(e);
                }
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
