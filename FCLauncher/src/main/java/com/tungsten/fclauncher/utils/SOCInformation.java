package com.tungsten.fclauncher.utils;

import android.os.Build;

import java.lang.reflect.Method;

public class SOCInformation {

    public static String getSocName() {
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method getMethod = systemPropertiesClass.getMethod("get", String.class, String.class);
            
            return (String) getMethod.invoke(null, "ro.soc.model", Build.HARDWARE);
        } catch (Exception e) {
            return Build.HARDWARE;
        }
    }
}
