package com.chenay.platform.log;

import java.io.File;
import java.io.IOException;

/**
 * 文件相关操作
 *
 * @author Y.Chen5
 */
public interface FileMaintenance {


    /**
     * 追加
     */
    void append(CharSequence c) throws IOException;

    /**
     * 删除
     */
    void delete();

    File create() throws IOException;
}
