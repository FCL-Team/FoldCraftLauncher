package com.tungsten.fclauncher.plugins;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.mio.data.Renderer;
import com.tungsten.fclauncher.FCLConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

/** Final TOCTOU and path boundary immediately before plugin native directories are used. */
public final class PluginNativeLoadGuard {
    private static final Set<String> ALLOWED_ABIS = new HashSet<>(Arrays.asList(
            "arm64-v8a",
            "armeabi-v7a",
            "x86_64"
    ));

    private static final Set<String> NATIVE_PATH_ENVIRONMENT_VARIABLES = new HashSet<>(Arrays.asList(
            "DLOPEN",
            "DRIVER_PATH",
            "FFMPEG_PATH",
            "LIBGL_DRIVERS_PATH",
            "LIB_MESA_NAME",
            "MESA_LIBRARY",
            "POJAVEXEC_EGL",
            "VK_ADD_DRIVER_FILES",
            "VK_ADD_LAYER_PATH",
            "VK_DRIVER_FILES",
            "VK_ICD_FILENAMES",
            "VK_LAYER_PATH"
    ));
    private static final Set<String> LAUNCHER_OWNED_ENVIRONMENT_VARIABLES = new HashSet<>(Arrays.asList(
            "FCL_ENVIRON",
            "FCL_NATIVEDIR",
            "MOD_ANDROID_RUNTIME",
            "POJAV_ENVIRON",
            "POJAV_NATIVEDIR",
            "RENDERER_HANDLE",
            "TMPDIR",
            "VULKAN_PTR"
    ));
    private static final Set<String> DRIVER_NAME_ENVIRONMENT_VARIABLES = new HashSet<>(Arrays.asList(
            "GALLIUM_DRIVER",
            "MESA_LOADER_DRIVER_OVERRIDE"
    ));
    private static final Pattern DRIVER_NAME = Pattern.compile("[A-Za-z0-9_+-]{1,64}");
    private static final Set<String> READ_ONLY_SYSTEM_LIBRARY_ROOTS = new HashSet<>(Arrays.asList(
            "/apex",
            "/odm",
            "/system",
            "/system_ext",
            "/vendor"
    ));
    private PluginNativeLoadGuard() {
    }

    /**
     * Native loading configuration belongs to the verified launch plan, not the global custom
     * environment setting.  This is public so FCLauncher can apply the same policy at the last
     * point custom environment variables are merged.
     */
    public static boolean isProtectedNativeEnvironmentVariable(String key) {
        return NativePluginEnvironment.isProtectedNativeEnvironmentVariable(key);
    }

    /**
     * The one parse of a plugin-declared environment entry.  Every consumer must split identically:
     * a guard that authorizes "a.so=b" while the loader dlopens "a" is not a guard at all.  Returns
     * null for an entry no consumer may act on.
     */
    public static String[] parsePluginEnvironmentEntry(String entry) {
        return NativePluginEnvironment.parsePluginEnvironmentEntry(entry);
    }

    public static void verifyNativePluginLoads(FCLConfig config, List<NativePluginAuthorization> authorizations) throws IOException {
        Context context = config.getContext();
        Renderer renderer = config.getRenderer();
        String rendererPackage = RendererPlugin.getPluginPackageName(renderer);
        if (rendererPackage != null) {
            if (!config.isUseExternalNativePlugins()) {
                throw new IOException("External renderer plugin loading is disabled for this launch mode");
            }
            requireAuthorization(authorizations, "Renderer", rendererPackage, renderer.getPath());
            verifyRendererLibraries(renderer);
        } else if (!renderer.getPath().isEmpty()) {
            throw new IOException("Renderer native path is not owned by an installed plugin APK");
        }

        if (!config.isUseExternalNativePlugins()) return;

        DriverPlugin.Driver driver = DriverPlugin.getSelected();
        if (driver.getPackageName() != null) {
            requireAuthorization(authorizations, "Vulkan driver", driver.getPackageName(), driver.getPath());
        } else if (!samePath(driver.getPath(), context.getApplicationInfo().nativeLibraryDir)) {
            throw new IOException("Vulkan driver path is not owned by the launcher or an installed plugin APK");
        }

        for (NativeLibPlugin.NativePlugin plugin : NativeLibPlugin.getPluginList()) {
            requireAuthorization(authorizations, "Native plugin", plugin.getPackageName(), plugin.getPath());
            verifyNativePluginEnvironment(plugin);
        }

        FFmpegPlugin.discover(context);
        if (FFmpegPlugin.isAvailable) {
            requireAuthorization(authorizations, "FFmpeg plugin", FFmpegPlugin.PACKAGE_NAME, FFmpegPlugin.libraryPath);
            requireLibraryInside(FFmpegPlugin.libraryPath, "libffmpeg.so", "FFmpeg library");
        }
    }

    private static void requireAuthorization(
            List<NativePluginAuthorization> authorizations,
            String type,
            String packageName,
            String expectedNativeDirectory
    ) throws IOException {
        NativePluginAuthorization authorization = authorizations.stream()
                .filter(candidate -> candidate.getPackageName().equals(packageName))
                .findFirst()
                .orElseThrow(() -> new IOException(type + " " + packageName + " has no matching pre-launch verification authorization"));
        if (!samePath(authorization.getNativeLibraryDirectory(), expectedNativeDirectory)) {
            throw new IOException(type + " native library directory no longer matches its APK package");
        }
        verifySupportedAbi(authorization.getNativeLibraryDirectory());
    }

    private static void verifyRendererLibraries(Renderer renderer) throws IOException {
        requireLibraryInside(renderer.getPath(), renderer.getGlName(), "Renderer OpenGL library");
        String eglName = renderer.getEglName();
        if (eglName != null && !eglName.isEmpty()) {
            requireLibraryInside(renderer.getPath(), stripLeadingSlash(eglName), "Renderer EGL library");
        }
        List<String> environment = renderer.getPojavEnv();
        if (environment == null) return;
        for (String entry : environment) {
            String[] split = parsePluginEnvironmentEntry(entry);
            if (split == null) continue;
            if ("DLOPEN".equals(split[0])) {
                for (String library : split[1].split(",")) {
                    requireLibraryInside(renderer.getPath(), library, "Renderer DLOPEN library");
                }
            } else if ("LIB_MESA_NAME".equals(split[0]) || "MESA_LIBRARY".equals(split[0])) {
                // FCLauncher resolves these two against the plugin directory before exporting them.
                requireLibraryInside(renderer.getPath(), split[1], "Renderer Mesa library");
            } else {
                verifyPluginDeclaredEnvironment("Renderer", renderer.getPath(), split[0], split[1]);
            }
        }
    }

    private static void verifyNativePluginEnvironment(NativeLibPlugin.NativePlugin plugin) throws IOException {
        for (Map.Entry<String, String> entry : plugin.getEnvMap().entrySet()) {
            verifyPluginDeclaredEnvironment("Native plugin", plugin.getPath(), entry.getKey(), entry.getValue());
        }
    }

    /**
     * A verified plugin still only speaks for its own library directory.  Declaring a protected path
     * outside it, or replacing a variable the launcher owns, would turn one-time plugin trust into a
     * loading path that any other process able to write that location controls.
     */
    static void verifyPluginDeclaredEnvironment(
            String label,
            String nativeDirectory,
            String key,
            String value
    ) throws IOException {
        switch (pluginEnvironmentPolicy(key)) {
            case NATIVE_PATH:
                if (value == null || value.isBlank()) {
                    throw new IOException(label + " declares an empty native path for " + key);
                }
                for (String entry : value.split(":")) {
                    if (entry.isBlank() || !(pathInside(nativeDirectory, entry) || isReadOnlySystemPath(entry))) {
                        throw new IOException(label + " environment points outside its installed library directory: " + key);
                    }
                }
                return;
            case LAUNCHER_OWNED:
                throw new IOException(label + " may not replace the launcher-controlled environment variable " + key);
            case DRIVER_NAME_ONLY:
                if (value == null || !DRIVER_NAME.matcher(value).matches()) {
                    throw new IOException(label + " declares " + key + " as something other than a plain driver name");
                }
                return;
            case UNPROTECTED:
                return;
            default:
                // A protected variable reaching here was added without deciding how a plugin may set
                // it. Refuse rather than pass it through to native code.
                throw new IOException(label + " declares the unclassified protected environment variable " + key);
        }
    }

    /** How a plugin is allowed to declare a given variable. The single source of truth for the policy. */
    enum PluginEnvironmentPolicy { NATIVE_PATH, LAUNCHER_OWNED, DRIVER_NAME_ONLY, UNPROTECTED, UNCLASSIFIED }

    static PluginEnvironmentPolicy pluginEnvironmentPolicy(String key) {
        if (isNativePathEnvironmentKey(key)) return PluginEnvironmentPolicy.NATIVE_PATH;
        if (LAUNCHER_OWNED_ENVIRONMENT_VARIABLES.contains(key)) return PluginEnvironmentPolicy.LAUNCHER_OWNED;
        if (DRIVER_NAME_ENVIRONMENT_VARIABLES.contains(key)) return PluginEnvironmentPolicy.DRIVER_NAME_ONLY;
        if (isProtectedNativeEnvironmentVariable(key)) return PluginEnvironmentPolicy.UNCLASSIFIED;
        return PluginEnvironmentPolicy.UNPROTECTED;
    }

    static Set<String> protectedNativeEnvironmentVariablesForTest() {
        return NativePluginEnvironment.protectedNativeEnvironmentVariables();
    }

    private static boolean isReadOnlySystemPath(String path) throws IOException {
        for (String root : READ_ONLY_SYSTEM_LIBRARY_ROOTS) {
            if (pathInside(root, path)) return true;
        }
        return false;
    }

    private static boolean isNativePathEnvironmentKey(String key) {
        return key != null && (key.startsWith("LD_")
                || NATIVE_PATH_ENVIRONMENT_VARIABLES.contains(key)
                || "PATH".equals(key));
    }

    private static void requireLibraryInside(String nativeDirectory, String relativeLibrary, String label) throws IOException {
        if (relativeLibrary == null || relativeLibrary.isBlank() || !relativeLibrary.endsWith(".so")) {
            throw new IOException(label + " is not a shared-library file");
        }
        File target = new File(nativeDirectory, stripLeadingSlash(relativeLibrary)).getCanonicalFile();
        if (!pathInside(nativeDirectory, target.getPath())) {
            throw new IOException(label + " escapes the installed native library directory");
        }
        if (!target.isFile()) {
            throw new IOException(label + " is missing from the installed native library directory");
        }
    }

    private static boolean pathInside(String base, String path) throws IOException {
        if (base == null || path == null) return false;
        File baseFile = new File(base).getCanonicalFile();
        File targetFile = new File(path).getCanonicalFile();
        return targetFile.toPath().startsWith(baseFile.toPath());
    }

    private static void verifySupportedAbi(String nativeDirectory) throws IOException {
        boolean deviceSupportsAllowedAbi = Arrays.stream(Build.SUPPORTED_ABIS).anyMatch(ALLOWED_ABIS::contains);
        if (!deviceSupportsAllowedAbi) {
            throw new IOException("External native plugins are not supported on this device ABI");
        }
        String normalized = nativeDirectory.replace('\\', '/');
        if (normalized.endsWith("/x86") || normalized.contains("/x86/")) {
            throw new IOException("x86 external native plugins are not permitted");
        }
    }

    private static boolean samePath(String first, String second) {
        if (first == null || second == null) return false;
        try {
            return new File(first).getCanonicalFile().equals(new File(second).getCanonicalFile());
        } catch (IOException e) {
            return false;
        }
    }

    private static String stripLeadingSlash(String value) {
        int index = 0;
        while (index < value.length() && value.charAt(index) == '/') index++;
        return value.substring(index);
    }
}
