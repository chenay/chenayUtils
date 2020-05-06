package com.chenay.platform.log.impl;

import androidx.annotation.NonNull;

import com.chenay.platform.retrofit.SSLSocketClient;

import java.io.File;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class DefaultLogModelImpl {


    /**
     * 删除日志文件
     */
    public void deleteLogFile() {

    }



    /**
     * 根据url，发送异步Post请求
     * @param url 提交到服务器的地址
     * @param fileNames 完整的上传的文件的路径名
     * @param callback OkHttp的回调接口
     */
    public  void httpUploadLogFile(String url, List<String> fileNames, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //忽略证书
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        Call call = okHttpClient.newCall(getRequest(url, fileNames));
        call.enqueue(callback);
    }
    /**
     * 获得Request实例
     * @param url
     * @param fileNames 完整的文件路径
     * @return
     */
    private  Request getRequest(String url, List<String> fileNames) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(getRequestBody(fileNames));
        return builder.build();
    }

    /**
     * 通过上传的文件的完整路径生成RequestBody
     * @param fileNames 完整的文件路径
     * @return
     */
    private  RequestBody getRequestBody(List<String> fileNames) {
        //创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
//        builder.setType(MultipartBody.FORM);
        for (int i = 0; i < fileNames.size(); i++) {
            //对文件进行遍历
            File file = new File(fileNames.get(i));
            //生成文件
            //根据文件的后缀名，获得文件类型
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart(
                    "file",
                    file.getName(),
                RequestBody.create(MediaType.parse(fileType), file));
        }
        builder.addFormDataPart("destination", "logs/attendance/");
        return builder.build();
    }

    @NonNull
    private String getMimeType(String name) {
//        return "*/*";
        return "application/octet-stream";
    }


}
