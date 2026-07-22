package com.tungsten.fclauncher.plugins;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.mio.data.Renderer;
import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.verifiedpluginload.api.VerifiedPluginLoad;
import com.tungsten.verifiedpluginload.api.VerifiedPluginLoadRegistry;
import com.tungsten.verifiedpluginload.model.PluginLoadAuthorization;
import com.tungsten.verifiedpluginload.model.PluginTrustStatus;
import com.tungsten.verifiedpluginload.model.PluginVerificationResult;
import com.tungsten.verifiedpluginload.model.TrustSource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Objects;

/** Final TOCTOU and path boundary immediately before plugin native directories are used. */
public final class PluginNativeLoadGuard {
    private static final String TAG = "VerifiedPluginLoad";
    private static final Set<String> ALLOWED_ABIS = new HashSet<>(Arrays.asList(
            "arm64-v8a",
            "armeabi-v7a",
            "x86_64"
    ));
    /*
     * These values are consumed by native code as library paths, loader configuration, or
     * already-loaded handles.  Letting the user-controlled environment editor replace one
     * would bypass the APK/source-dir authorization immediately above the actual dlopen.
     */
    private static final Set<String> PROTECTED_NATIVE_ENVIRONMENT_VARIABLES = new HashSet<>(Arrays.asList(
            "DLOPEN",
            "DRIVER_PATH",
            "FFMPEG_PATH",
            "FCL_NATIVEDIR",
            "LIBGL_DRIVERS_PATH",
            "LIB_MESA_NAME",
            "MESA_LIBRARY",
            "MESA_LOADER_DRIVER_OVERRIDE",
            "MOD_ANDROID_RUNTIME",
            "POJAVEXEC_EGL",
            "POJAV_NATIVEDIR",
            "RENDERER_HANDLE",
            "TMPDIR",
            "VK_ADD_DRIVER_FILES",
            "VK_ADD_LAYER_PATH",
            "VK_DRIVER_FILES",
            "VK_ICD_FILENAMES",
            "VK_LAYER_PATH",
            "VULKAN_PTR"
    ));
    private static final Set<String> NATIVE_PATH_ENVIRONMENT_VARIABLES = new HashSet<>(Arrays.asList(
            "DLOPEN",
            "DRIVER_PATH",
            "FFMPEG_PATH",
            "LIBGL_DRIVERS_PATH",
            "LIB_MESA_NAME",
            "MESA_LIBRARY",
            "POJAVEXEC_EGL"
    ));

    private PluginNativeLoadGuard() {
    }

    /**
     * Native loading configuration belongs to the verified launch plan, not the global custom
     * environment setting.  This is public so FCLauncher can apply the same policy at the last
     * point custom environment variables are merged.
     */
    public static boolean isProtectedNativeEnvironmentVariable(String key) {
        return key != null && (key.startsWith("LD_") || PROTECTED_NATIVE_ENVIRONMENT_VARIABLES.contains(key));
    }

    /** A stored per-certificate trust is only active while the launcher setting permits it. */
    public static boolean isExplicitKeyTrustAllowed(TrustSource trustSource, boolean allowUntrustedPlugins) {
        return trustSource != TrustSource.KEY || allowUntrustedPlugins;
    }

    public static void verify(FCLConfig config) throws IOException {
        Context context = config.getContext();
        VerifiedPluginLoad vpl = VerifiedPluginLoadRegistry.get(context);
        List<PluginLoadAuthorization> authorizations = config.getPluginLoadAuthorizations();
        boolean allowUntrustedPlugins = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
                .getBoolean("allow_untrusted_plugins", false);

        Renderer renderer = config.getRenderer();
        String rendererPackage = RendererPlugin.getPluginPackageName(renderer);
        if (rendererPackage != null) {
            if (!config.isUseExternalNativePlugins()) {
                throw new IOException("External renderer plugin loading is disabled for this launch mode");
            }
            verifyPlugin(vpl, authorizations, "Renderer", rendererPackage, renderer.getPath(), allowUntrustedPlugins);
            verifyRendererLibraries(renderer);
        } else if (!renderer.getPath().isEmpty()) {
            throw new IOException("Renderer native path is not owned by an installed plugin APK");
        }

        if (!config.isUseExternalNativePlugins()) return;

        DriverPlugin.Driver driver = DriverPlugin.getSelected();
        if (driver.getPackageName() != null) {
            verifyPlugin(vpl, authorizations, "Vulkan driver", driver.getPackageName(), driver.getPath(), allowUntrustedPlugins);
        } else if (!samePath(driver.getPath(), context.getApplicationInfo().nativeLibraryDir)) {
            throw new IOException("Vulkan driver path is not owned by the launcher or an installed plugin APK");
        }

        for (NativeLibPlugin.NativePlugin plugin : NativeLibPlugin.getPluginList()) {
            verifyPlugin(vpl, authorizations, "Native plugin", plugin.getPackageName(), plugin.getPath(), allowUntrustedPlugins);
            verifyNativePluginEnvironment(plugin);
        }

        FFmpegPlugin.discover(context);
        if (FFmpegPlugin.isAvailable) {
            verifyPlugin(vpl, authorizations, "FFmpeg plugin", FFmpegPlugin.PACKAGE_NAME, FFmpegPlugin.libraryPath, allowUntrustedPlugins);
            requireLibraryInside(FFmpegPlugin.libraryPath, "libffmpeg.so", "FFmpeg library");
        }
    }

    private static void verifyPlugin(
            VerifiedPluginLoad vpl,
            List<PluginLoadAuthorization> authorizations,
            String type,
            String packageName,
            String expectedNativeDirectory,
            boolean allowUntrustedPlugins
    ) throws IOException {
        PluginVerificationResult result = vpl.inspectInstalledPackage(packageName);
        if (result.getStatus() != PluginTrustStatus.TRUSTED) {
            throw new IOException(type + " " + packageName + " is not trusted: " + result.getStatus());
        }
        if (!isExplicitKeyTrustAllowed(result.getTrustSource(), allowUntrustedPlugins)) {
            throw new IOException(type + " " + packageName + " is trusted only by an individual signature hash while untrusted plugin loading is disabled");
        }
        String apkPath = result.getPackageInfo().getApkPath();
        String nativeDirectory = result.getPackageInfo().getNativeLibraryDirectory();
        if (!samePath(nativeDirectory, expectedNativeDirectory)) {
            throw new IOException(type + " native library directory no longer matches its APK package");
        }
        verifySupportedAbi(nativeDirectory);
        boolean authorized = authorizations.stream().anyMatch(authorization ->
                authorization.getPackageName().equals(packageName)
                        && samePath(authorization.getApkPath(), apkPath)
                        && Objects.equals(authorization.getVersionCode(), result.getPackageInfo().getVersionCode())
                        && authorization.getCurrentSignatures().equals(new java.util.LinkedHashSet<>(result.getCurrentSignatures()))
        );
        if (!authorized) {
            throw new IOException(type + " has no matching pre-launch verification authorization");
        }
        String hash = result.getMatchedSignature() == null ? "unknown" : result.getMatchedSignature().getSha256();
        Log.i(TAG, type + " trusted: package=" + packageName
                + ", version=" + result.getPackageInfo().getVersionName()
                + ", sha256=" + hash
                + ", trustListVersion=" + result.getTrustListVersion());
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
            String[] split = entry.split("=", 2);
            if (split.length != 2) continue;
            if ("DLOPEN".equals(split[0])) {
                for (String library : split[1].split(",")) {
                    requireLibraryInside(renderer.getPath(), library, "Renderer DLOPEN library");
                }
            } else if ("LIB_MESA_NAME".equals(split[0]) || "MESA_LIBRARY".equals(split[0])) {
                requireLibraryInside(renderer.getPath(), split[1], "Renderer Mesa library");
            }
        }
    }

    private static void verifyNativePluginEnvironment(NativeLibPlugin.NativePlugin plugin) throws IOException {
        for (Map.Entry<String, String> entry : plugin.getEnvMap().entrySet()) {
            String value = entry.getValue();
            if (isNativePathEnvironmentKey(entry.getKey()) && !controlledNativePath(plugin.getPath(), value)) {
                throw new IOException("Native plugin environment points outside its installed library directory: " + entry.getKey());
            }
        }
    }

    private static boolean isNativePathEnvironmentKey(String key) {
        return key != null && (key.startsWith("LD_")
                || NATIVE_PATH_ENVIRONMENT_VARIABLES.contains(key)
                || "PATH".equals(key));
    }

    private static boolean controlledNativePath(String base, String value) throws IOException {
        if (value == null || value.isBlank()) return false;
        for (String entry : value.split(":")) {
            if (entry.isBlank() || !pathInside(base, entry)) return false;
        }
        return true;
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
