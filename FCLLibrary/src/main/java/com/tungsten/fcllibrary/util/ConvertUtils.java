package com.tungsten.fcllibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;

public class ConvertUtils {

    public static int dip2px(Context context, float dpValue) {
        float scare = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scare + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float scare = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scare + 0.5f);
    }

    public static Bitmap stringToBitmap(String string) {
        if (string == null)
            return null;
        byte[] bitmapArray;
        bitmapArray = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    public static String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    public static Bitmap getBitmapFromRes(Context context, int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        TypedValue value = new TypedValue();
        options.inTargetDensity = value.density;
        options.inScaled = false;
        return BitmapFactory.decodeResource(context.getResources(), id, options);
    }
}