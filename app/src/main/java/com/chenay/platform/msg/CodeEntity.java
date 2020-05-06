package com.chenay.platform.msg;

import androidx.annotation.NonNull;

public enum  CodeEntity {


    NULL(0,"返回Code是空值"),

    SUCCESS(1, "请求成功"),
    WARN(-1, "网络异常，请稍后重试"),
    ERROR(-1,"请求失败!"),
    NO_DATA(-2, "没有数据!"),

    DATA_NULL(1, "没有数据"),

    ALERT(2000, "提示!");

    private int code;
    private String msg;
    private String key;

    CodeEntity(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    CodeEntity(int code, String msg, String key) {
        this(code, msg);
        this.key = key;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

    public String getKey() {
        return key;
    }

    @NonNull
    public String getCodeString() {
        return code + "";
    }
}
