package com.example.farmedu_ermin.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleHelper {

    public static void setLocale(Context context, String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        context.getResources().updateConfiguration(
                config,
                context.getResources().getDisplayMetrics()
        );

        saveLanguage(context, lang);
    }

    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return prefs.getString("lang", "bs");
    }

    private static void saveLanguage(Context context, String lang) {
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        prefs.edit().putString("lang", lang).apply();
    }
}