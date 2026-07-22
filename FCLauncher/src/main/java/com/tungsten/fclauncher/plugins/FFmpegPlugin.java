package com.tungsten.fclauncher.plugins;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.system.Os;

public class FFmpegPlugin {
    public static final String PACKAGE_NAME = "net.kdt.pojavlaunch.ffmpeg";
    public static boolean isAvailable = false;
    public static String libraryPath;

    public static void discover(Context context) {
        isAvailable = false;
        libraryPath = null;
        try {
            Os.unsetenv("FFMPEG_PATH");
        } catch (Throwable ignore) {
        }
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo ffmpegPluginInfo = manager.getPackageInfo(PACKAGE_NAME, PackageManager.GET_SHARED_LIBRARY_FILES);
            libraryPath = ffmpegPluginInfo.applicationInfo.nativeLibraryDir;
            isAvailable = true;
        } catch (Throwable ignore) {
        }
    }

    /** Call only after PluginNativeLoadGuard has accepted this installed plugin. */
    public static void activate() {
        if (!isAvailable || libraryPath == null) return;
        try {
            Os.setenv("FFMPEG_PATH", libraryPath + "/libffmpeg.so", true);
        } catch (Throwable ignore) {
        }
    }
}
