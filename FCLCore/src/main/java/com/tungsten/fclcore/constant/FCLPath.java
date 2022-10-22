package com.tungsten.fclcore.constant;

import android.content.Context;

public class FCLPath {

    public static Context CONTEXT;

    public static String NATIVE_LIB_DIR;

    public static String LOG_DIR;
    public static String CACHE_DIR;

    public static String RUNTIME_DIR;
    public static String JAVA_8_PATH;
    public static String JAVA_17_PATH;
    public static String LWJGL2_DIR;
    public static String LWJGL3_DIR;

    public static void loadPaths(Context context) {
        CONTEXT = context;

        NATIVE_LIB_DIR = context.getApplicationInfo().nativeLibraryDir;

        LOG_DIR = context.getExternalFilesDir("log").getAbsolutePath();
        CACHE_DIR = context.getCacheDir() + "/fclauncher";

        RUNTIME_DIR = context.getDir("runtime", 0).getAbsolutePath();
        JAVA_8_PATH = RUNTIME_DIR + "/java/jre8";
        JAVA_17_PATH = RUNTIME_DIR + "/java/jre17";
        LWJGL2_DIR = RUNTIME_DIR + "/lwjgl2";
        LWJGL3_DIR = RUNTIME_DIR + "/lwjgl3";
    }

}
