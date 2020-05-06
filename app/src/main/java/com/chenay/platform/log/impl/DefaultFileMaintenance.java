package com.chenay.platform.log.impl;

import com.chenay.platform.log.FileMaintenance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 默认的日志文件处理
 *
 * @author Y.Chen5
 */
public class DefaultFileMaintenance implements FileMaintenance {


    private File file;

    private String encoding;




    public DefaultFileMaintenance(File file) {
        this.file = file;
    }

    @Override
    public void append(CharSequence c) throws IOException {
        create();
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.append(String.valueOf(c)).append("\r\n");
        fileWriter.close();
    }

    @Override
    public void delete() {

    }

    @Override
    public File create() throws IOException {
        if (!file.exists()) {
            final File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    throw new IOException(parentFile.getAbsolutePath() + "mkdirs is filed");
                }
            }

            if (!file.createNewFile()) {
                throw new IOException(file.getAbsolutePath() + "create is filed");
            }
        }

        return file;
    }


    public void scanFile() {
    }
}
