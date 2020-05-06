package com.chenay.platform.apk;


import com.chenay.platform.mvp.view.IMvpBaseView;

public interface ApkView extends IMvpBaseView {
    void httpResponseDownload(DownloadInfoEntity downloadInfoEntity);
}
