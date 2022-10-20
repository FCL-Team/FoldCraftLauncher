package com.tungsten.fclcore.constant;

import android.content.Context;

public class FCLPath {

    public static String NATIVE_LIB_DIR;

    public static void loadPaths(Context context) {
        NATIVE_LIB_DIR = context.getApplicationInfo().nativeLibraryDir;
    }

}
