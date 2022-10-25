package com.tungsten.fcllibrary.util;

import android.content.Context;

public class ConvertUtils {

    public static int dip2px(Context context, float dpValue) {
        float scare = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scare + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float scare = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scare + 0.5f);
    }
}