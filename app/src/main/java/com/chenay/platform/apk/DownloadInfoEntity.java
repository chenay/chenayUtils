package com.chenay.platform.apk;

import java.io.Serializable;

/**
 * @author Y.Chen5
 */
public class DownloadInfoEntity implements Serializable {

    private boolean dloadStart;

    private boolean dloading;

    private boolean dloadEnd;

    private boolean success;

    private float progress;

    private String msg;

    private String path;

    public boolean isDloadStart() {
        return dloadStart;
    }

    public DownloadInfoEntity setDloadStart(boolean dloadStart) {
        this.dloadStart = dloadStart;
        this.dloadEnd = !dloadStart;
        return this;
    }

    public boolean isDloading() {
        return dloading;
    }

    public DownloadInfoEntity setDloading(boolean dloading) {
        this.dloading = dloading;
        return this;
    }

    public boolean isDloadEnd() {
        return dloadEnd;
    }

    public DownloadInfoEntity setDloadEnd(boolean dloadEnd) {
        this.dloadEnd = dloadEnd;
        this.dloadStart = !dloadEnd;
        return this;
    }

    public float getProgress() {
        return progress;
    }

    public DownloadInfoEntity setProgress(float progress) {
        this.progress = progress;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public DownloadInfoEntity setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public DownloadInfoEntity setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getPath() {
        return path;
    }

    public DownloadInfoEntity setPath(String path) {
        this.path = path;
        return this;
    }
}
