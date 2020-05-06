package com.chenay.platform.utils;

import android.content.res.Resources;

import androidx.annotation.StringRes;

import com.chenay.platform.ChyApplication;

/**
 * arrays.xml
 * 静态数据映射工具
 * @author Y.Chen5
 */
public class ResourcesUtils {

    /**
     * 源文件获取字符
     * @param id
     * @return
     */
    public static String getString(@StringRes int id) {
        String str = null;
        try {
            str = ChyApplication.context.getResources().getString(id);
        } catch (Resources.NotFoundException e) {
            str = "";
        }
        return str;
    }

}
