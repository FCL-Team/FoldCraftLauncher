package com.tungsten.fclauncher.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FCLPath {

    public static Context CONTEXT;

    public static String NATIVE_LIB_DIR;

    public static String LOG_DIR;
    public static String CACHE_DIR;

    public static String RUNTIME_DIR;
    public static String JAVA_8_PATH;
    public static String JAVA_11_PATH;
    public static String JAVA_17_PATH;
    public static String JAVA_21_PATH;
    public static String JNA_PATH;
    public static String LWJGL_DIR;
    public static String CACIOCAVALLO_8_DIR;
    public static String CACIOCAVALLO_11_DIR;
    public static String CACIOCAVALLO_17_DIR;

    public static String FILES_DIR;
    public static String PLUGIN_DIR;
    public static String BACKGROUND_DIR;
    public static String CONTROLLER_DIR;

    public static String PRIVATE_COMMON_DIR;
    public static String SHARED_COMMON_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FCL/.minecraft";

    public static String AUTHLIB_INJECTOR_PATH;
    public static String LIB_FIXER_PATH;
    public static String MIO_LAUNCH_WRAPPER;
    public static String LT_BACKGROUND_PATH;
    public static String DK_BACKGROUND_PATH;

    public static void loadPaths(Context context) {
        CONTEXT = context;

        NATIVE_LIB_DIR = context.getApplicationInfo().nativeLibraryDir;

        LOG_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FCL/log";
        CACHE_DIR = context.getCacheDir() + "/fclauncher";

        RUNTIME_DIR = context.getDir("runtime", 0).getAbsolutePath();
        JAVA_8_PATH = RUNTIME_DIR + "/java/jre8";
        JAVA_11_PATH = RUNTIME_DIR + "/java/jre11";
        JAVA_17_PATH = RUNTIME_DIR + "/java/jre17";
        JAVA_21_PATH = RUNTIME_DIR + "/java/jre21";
        JNA_PATH = RUNTIME_DIR + "/jna";
        LWJGL_DIR = RUNTIME_DIR + "/lwjgl";
        CACIOCAVALLO_8_DIR = RUNTIME_DIR + "/caciocavallo";
        CACIOCAVALLO_11_DIR = RUNTIME_DIR + "/caciocavallo11";
        CACIOCAVALLO_17_DIR = RUNTIME_DIR + "/caciocavallo17";

        FILES_DIR = context.getFilesDir().getAbsolutePath();
        PLUGIN_DIR = FILES_DIR + "/plugins";
        BACKGROUND_DIR = FILES_DIR + "/background";
        CONTROLLER_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FCL/control";

        PRIVATE_COMMON_DIR = context.getExternalFilesDir(".minecraft").getAbsolutePath();

        AUTHLIB_INJECTOR_PATH = PLUGIN_DIR + "/authlib-injector.jar";
        LIB_FIXER_PATH = PLUGIN_DIR + "/MioLibFixer.jar";
        MIO_LAUNCH_WRAPPER = PLUGIN_DIR + "/MioLaunchWrapper.jar";
        LT_BACKGROUND_PATH = BACKGROUND_DIR + "/lt.png";
        DK_BACKGROUND_PATH = BACKGROUND_DIR + "/dk.png";

        init(LOG_DIR);
        init(CACHE_DIR);
        init(RUNTIME_DIR);
        init(JAVA_8_PATH);
        init(JAVA_11_PATH);
        init(JAVA_17_PATH);
        init(JAVA_21_PATH);
        init(LWJGL_DIR);
        init(CACIOCAVALLO_8_DIR);
        init(CACIOCAVALLO_11_DIR);
        init(CACIOCAVALLO_17_DIR);
        init(FILES_DIR);
        init(PLUGIN_DIR);
        init(BACKGROUND_DIR);
        init(CONTROLLER_DIR);
        init(PRIVATE_COMMON_DIR);
        init(SHARED_COMMON_DIR);
    }

    private static boolean init(String path) {
        if (!new File(path).exists()) {
            return new File(path).mkdirs();
        }
        return true;
    }

}
