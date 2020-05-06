package com.chenay.platform.device;

import android.content.Context;

import com.chenay.platform.utils.R;

import java.util.Locale;

/**
 * @author Y.Chen5
 */
public class LanguageHelper {
    private static final Locale VIETNAMESE = new Locale("vi", "VN");

    private static final Locale SYSTEM = new Locale("auto", "system");

    public static String getLanguageTag(Context context) {
        return LocaleHelper.newInstance().getLocale(context).toLanguageTag();
    }


    /**
     * 加载当前APP 语言
     *
     * @param newBase
     * @return
     */
    public static Context loadContext(Context newBase) {
        if (getLanguageTag(newBase).equals(Lang.SYSTEM_.getLocal().toLanguageTag())) {
            LocaleHelper.newInstance().setLocale(newBase, Locale.getDefault());
        }
        return LocaleHelper.newInstance().onAttach(newBase);
    }

    public static void updateLanguageConfig(Context context, Lang choose) {
        LocaleHelper.newInstance().setLocale(context, choose.local);
    }
    public enum Lang {
        /**
         *
         */
        SYSTEM_(0, SYSTEM, R.string.str_lang_sys, false),
        /**
         *
         */
        SIMPLIFIED_CHINESE_(1, Locale.SIMPLIFIED_CHINESE, R.string.str_lang_simp_chinese, false),
        /**
         *
         */
        VIETNAMESE_(2, VIETNAMESE, R.string.str_lang_vietnamese, false);


        private final int ids;
        private final Locale local;
        private int code;
        private boolean check;

        Lang(int i, Locale s, int id, boolean check) {
            this.code = i;
            this.local = s;
            this.ids = id;
            this.check = check;
        }

        public int getIds() {
            return ids;
        }

        public int getCode() {
            return code;
        }

        public Locale getLocal() {
            return local;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
    }
}