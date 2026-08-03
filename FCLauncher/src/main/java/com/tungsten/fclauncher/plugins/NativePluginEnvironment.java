package com.tungsten.fclauncher.plugins;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Native environment classification shared by launcher startup and plugin verification. */
public final class NativePluginEnvironment {
    private static final Set<String> PROTECTED_NATIVE_ENVIRONMENT_VARIABLES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    "DLOPEN",
                    "DRIVER_PATH",
                    "FCL_ENVIRON",
                    "FFMPEG_PATH",
                    "FCL_NATIVEDIR",
                    "GALLIUM_DRIVER",
                    "LIBGL_DRIVERS_PATH",
                    "LIB_MESA_NAME",
                    "MESA_LIBRARY",
                    "MESA_LOADER_DRIVER_OVERRIDE",
                    "MOD_ANDROID_RUNTIME",
                    "POJAVEXEC_EGL",
                    "POJAV_ENVIRON",
                    "POJAV_NATIVEDIR",
                    "RENDERER_HANDLE",
                    "TMPDIR",
                    "VK_ADD_DRIVER_FILES",
                    "VK_ADD_LAYER_PATH",
                    "VK_DRIVER_FILES",
                    "VK_ICD_FILENAMES",
                    "VK_LAYER_PATH",
                    "VULKAN_PTR"
            ))
    );

    private NativePluginEnvironment() {
    }

    public static boolean isProtectedNativeEnvironmentVariable(String key) {
        return key != null && (key.startsWith("LD_") || PROTECTED_NATIVE_ENVIRONMENT_VARIABLES.contains(key));
    }

    public static Set<String> protectedNativeEnvironmentVariables() {
        return PROTECTED_NATIVE_ENVIRONMENT_VARIABLES;
    }

    public static String[] parsePluginEnvironmentEntry(String entry) {
        if (entry == null) return null;
        String[] split = entry.split("=", 2);
        if (split.length != 2 || split[0].isEmpty() || split[1].isEmpty()) return null;
        return split;
    }
}
