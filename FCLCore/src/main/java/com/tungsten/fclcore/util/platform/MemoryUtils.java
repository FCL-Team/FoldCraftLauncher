package com.tungsten.fclcore.util.platform;

import android.app.ActivityManager;
import android.content.Context;

public class MemoryUtils {

    public static int getTotalDeviceMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memInfo);
        return (int) (memInfo.totalMem / 1048576L);
    }

    public static int getFreeDeviceMemory(Context context) {
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        return (int) (memInfo.availMem / 1048576L);
    }

    public static int findBestRAMAllocation(Context context) {
        int totalDeviceMemory = getTotalDeviceMemory(context);
        if (totalDeviceMemory < 1024) {
            return 512;
        } else if (totalDeviceMemory < 2048) {
            return 1024;
        } else if (totalDeviceMemory < 4096) {
            return 2048;
        } else {
            return 4096;
        }
    }

}