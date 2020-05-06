package com.chenay.platform.device;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.io.ObjectStreamException;
import java.util.Locale;

/**
 * @author Y.Chen5
 */
public class LocaleHelper {


    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private static final String SELECTED_COUNTRY = "Locale.Helper.Selected.Country";

    public Context onAttach(Context context) {
        Locale locale = load(context);
        return setLocale(context, locale);
    }

    public Locale getLocale(Context context) {
        return load(context);
    }

    public Context setLocale(Context context, Locale locale) {
        persist(context, locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, locale);
        } else {
            return updateResourcesLegacy(context, locale);
        }

    }

    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(LocaleHelper.class.getName(), Context.MODE_PRIVATE);
    }

    private void persist(Context context, Locale locale) {
        if (locale == null) {
            return;
        }
        getPreferences(context)
                .edit()
                .putString(SELECTED_LANGUAGE, locale.getLanguage())
                .putString(SELECTED_COUNTRY, locale.getCountry())
                .apply();
    }

    private Locale load(Context context) {
        SharedPreferences preferences = getPreferences(context);
        String language = preferences.getString(SELECTED_LANGUAGE, Locale.getDefault().getLanguage());
        String country = preferences.getString(SELECTED_COUNTRY, Locale.getDefault().getCountry());
        return new Locale(language, country);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResources(Context context, Locale locale) {
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLegacy(Context context, Locale locale) {
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }


    /**
     *获取单例内部类句柄
     * @return
     *
     * Created by Chenay
     */
    private static class LocaleHelperHolder {
            private static final LocaleHelper SINGLETON = new LocaleHelper();
        }
    /**
     *获取单例
     * @return
     *
     * Created by Chenay
     */
    public static LocaleHelper newInstance() {
        return LocaleHelperHolder.SINGLETON;
    }
    /**
     * 防止反序列化操作破坏单例模式 (条件) implements Serializable
     *@return
     * Created by Chenay
     */
    private Object readResolve() throws ObjectStreamException {
        return LocaleHelperHolder.SINGLETON;
    }
}
