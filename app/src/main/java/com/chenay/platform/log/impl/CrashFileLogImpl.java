package com.chenay.platform.log.impl;

import com.chenay.platform.log.FileLogModel;
import com.chenay.platform.log.FileMaintenance;
import com.chenay.platform.utils.AndroidUtil;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * 异常日志抓捕
 * @author Y.Chen5
 */
public class CrashFileLogImpl implements FileLogModel {

    private FileMaintenance fileMaintenance;

    public CrashFileLogImpl(String fileName) {
        String time = new java.text.SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US)
                .format(new java.util.Date());
        this.fileMaintenance = new DefaultFileMaintenance(new File(AndroidUtil.getLogDir()+"crash/", fileName + time + ".txt"));
    }

    @Override
    public void saveLog(CharSequence msg) {
        if (fileMaintenance != null) {
            try {
                fileMaintenance.append(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
