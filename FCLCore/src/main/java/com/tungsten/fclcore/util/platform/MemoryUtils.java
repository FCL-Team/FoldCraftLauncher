package com.tungsten.fclcore.util.platform;

import android.app.ActivityManager;
import android.content.Context;

import com.tungsten.fclauncher.utils.Architecture;

public class MemoryUtils {

    public static int getTotalDeviceMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memInfo);
        return (int) (memInfo.totalMem / 1048576L);
    }

    public static int getUsedDeviceMemory(Context context) {
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        return (int) ((memInfo.totalMem - memInfo.availMem) / 1048576L);
    }

    public static int getFreeDeviceMemory(Context context) {
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        return (int) (memInfo.availMem / 1048576L);
    }

    public static int findBestRAMAllocation(Context context) {
        int totalDeviceMemory = getTotalDeviceMemory(context);
        if (totalDeviceMemory <= 1024) {
            return 512;
        } else if (totalDeviceMemory <= 4096) {
            return Architecture.is32BitsDevice() ? 768 : 1024;
        } else if (totalDeviceMemory <= 6144) {
            // 4-6GB phones: 1GB heap is too small for modded (Forge/NeoForge) 1.20+, which causes
            // near-constant GC and extremely slow mod loading / world gen. 1.5GB leaves plenty of
            // headroom for the renderer's native (Mesa/Zink) allocations. Users can still tune this.
            return Architecture.is32BitsDevice() ? 768 : 1536;
        } else if (totalDeviceMemory <= 12288) {
            return Architecture.is32BitsDevice() ? 768 : 2048;
        } else {
            return Architecture.is32BitsDevice() ? 768 : 4096;
        }
    }

}