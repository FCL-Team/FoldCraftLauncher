package com.tungsten.fcllibrary.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.LocaleList;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocaleUtils {

    /**
     * 0: System
     * 1: English
     * 2: Simplified Chinese
     * 3: Traditional Chinese
     * 4: Vietnamese
     */

    private static DateTimeFormatter dateTimeFormatter;

    public static boolean isChinese(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE);
        int lang = sharedPreferences.getInt("lang", 0);
        return lang == 2 || (lang == 0 && getSystemLocale().toString().equals(Locale.CHINA.toString()));
    }

    public static int getLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("lang", 0);
    }

    public static Context setLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE);
        return updateResources(context, sharedPreferences.getInt("lang", 0));
    }

    public static void changeLanguage(Context context, int lang) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lang", lang);
        editor.apply();
    }

    private static Context updateResources(Context context, int lang) {
        Locale locale = getLocale(lang);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    public static Locale getLocale(int lang) {
        switch (lang) {
            case 1:
                return Locale.ENGLISH;
            case 2:
                return Locale.CHINA;
            case 3:
                return Locale.TAIWAN;
            default:
                return getSystemLocale();
        }
    }

    public static Locale getSystemLocale() {
        return LocaleList.getDefault().get(0);
    }

    public static String formatDateTime(Context context, Instant instant) {
        return getDateTimeFormatter(context).format(instant);
    }

    public static DateTimeFormatter getDateTimeFormatter(Context context) {
        if (dateTimeFormatter == null) {
            @SuppressLint("DiscouragedApi") int resId = context.getResources().getIdentifier("world_time", "string", context.getPackageName());
            dateTimeFormatter = DateTimeFormatter.ofPattern(context.getString(resId)).withZone(ZoneId.systemDefault());
        }
        return dateTimeFormatter;
    }

}