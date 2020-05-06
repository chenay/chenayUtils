package com.chenay.platform.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.io.ObjectStreamException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Y.Chen5
 */
public class PreferenceUtil {

    /**
     *获取单例内部类句柄
     * @return
     *
     * Created by Chenay
     */
    private static class PreferenceUtilsHolder {
        private static final PreferenceUtil SINGLETON = new PreferenceUtil();
    }
    /**
     *获取单例
     * @return
     *
     * Created by Chenay
     */
    public static PreferenceUtil newInstance() {
        return PreferenceUtilsHolder.SINGLETON;
    }
    /**
     * 防止反序列化操作破坏单例模式 (条件) implements Serializable
     *@return
     * Created by Chenay
     */
    private Object readResolve() throws ObjectStreamException {
        return PreferenceUtilsHolder.SINGLETON;
    }


    private static SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor = null;


    public static void install(Context mContext) {
        if (null == mSharedPreferences) {
            mSharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
        }
    }

    public static void unInstall(Context mContext) {
        if (mSharedPreferences != null) {
            mSharedPreferences = null;
        }
    }


    public void removeKey(String key) {
        mEditor = mSharedPreferences.edit();
        mEditor.remove(key);
        mEditor.apply();
    }

    public void removeAll() {
        mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
    }

    public boolean commitString(String key, String value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        return mEditor.commit();
    }


    public String getString(String key, String faillValue) {
        return mSharedPreferences.getString(key, faillValue);
    }


    public boolean commitStringSet(String key, @Nullable Set<String> values) {
        mEditor = mSharedPreferences.edit();
        mEditor.putStringSet(key, values);
        return mEditor.commit();
    }

    public Set<String> getStringSet(String key, HashSet<String> objects) {
        return mSharedPreferences.getStringSet(key, objects);
    }

    public boolean commitInt(String key, int value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putInt(key, value);
        return mEditor.commit();
    }

    public int getInt(String key, int failValue) {
        return mSharedPreferences.getInt(key, failValue);
    }

    public boolean commitLong(String key, long value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putLong(key, value);
        return mEditor.commit();
    }

    public long getLong(String key, long failValue) {
        return mSharedPreferences.getLong(key, failValue);
    }

    public boolean commitBoolean(String key, boolean value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key, value);
        return mEditor.commit();
    }

    public Boolean getBoolean(String key, boolean failValue) {
        return mSharedPreferences.getBoolean(key, failValue);
    }

}
