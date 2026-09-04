package com.mio.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.system.Os;


public class FFmpegPlugin {
    public static boolean isAvailable = false;
    public static String libraryPath;

    public static void discover(Context context) {
        if (!PluginManager.isEnabled(context, PluginManager.FFMPEG_PACKAGE)) {
            isAvailable = false;
            libraryPath = null;
            return;
        }
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo ffmpegPluginInfo = manager.getPackageInfo("net.kdt.pojavlaunch.ffmpeg", PackageManager.GET_SHARED_LIBRARY_FILES);
            libraryPath = ffmpegPluginInfo.applicationInfo.nativeLibraryDir;
            isAvailable = true;
            Os.setenv("FFMPEG_PATH", libraryPath + "/libffmpeg.so", true);
        } catch (Throwable ignore) {
        }
    }
}
