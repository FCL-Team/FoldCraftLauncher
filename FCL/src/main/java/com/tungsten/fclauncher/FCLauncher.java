package com.tungsten.fclauncher;

import static android.content.Context.MODE_PRIVATE;
import static com.tungsten.fclauncher.utils.Architecture.ARCH_X86;
import static com.tungsten.fclauncher.utils.Architecture.is64BitsDevice;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.system.ErrnoException;
import android.system.Os;
import android.util.ArrayMap;

import com.mio.data.Renderer;
import com.mio.device.VulkanCheckManager;
import com.mio.plugin.DriverPlugin;
import com.mio.plugin.FFmpegPlugin;
import com.mio.plugin.NativeLibPlugin;
import com.mio.plugin.RendererPlugin;
import com.oracle.dalvik.VMLauncher;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.utils.Architecture;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.Logging;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import org.json.JSONException;
import org.json.JSONObject;

public class FCLauncher {

    private static int FCL_VERSION_CODE = -1;

    private static void log(FCLBridge bridge, String log) {
        bridge.getCallback().onLog(log + "\n");
    }

    private static void printTaskTitle(FCLBridge bridge, String task) {
        log(bridge, "==================== " + task + " ====================");
    }

    private static void logStartInfo(FCLConfig config, FCLBridge bridge, String task) {
        printTaskTitle(bridge, "Start " + task);
        log(bridge, "Renderer: " + bridge.getRenderer());
        log(bridge, "Device: " + getDeviceName());
        log(bridge, "Architecture: " + Architecture.archAsString(Architecture.getDeviceArchitecture()));
        log(bridge, "CPU: " + getSocName());
        log(bridge, "Android SDK: " + Build.VERSION.SDK_INT);
        log(bridge, "Language: " + Locale.getDefault());

        PackageManager pm = config.getContext().getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(config.getContext().getPackageName(), 0);
            FCL_VERSION_CODE = packageInfo.versionCode;
            log(bridge, "FCL Version Code: " + FCL_VERSION_CODE);
        } catch (PackageManager.NameNotFoundException e) {
            log(bridge, "FCL Version Code: Can't get current version code, exception = " + e.getMessage());
        }
    }

    private static void logModList(FCLBridge bridge) {
        printTaskTitle(bridge, "Mods");
        log(bridge, bridge.getModSummary());
        bridge.setModSummary(null);
    }

    private static Map<String, String> readJREReleaseProperties(String javaPath) throws IOException {
        Map<String, String> jreReleaseMap = new ArrayMap<>();
        BufferedReader jreReleaseReader = new BufferedReader(new FileReader(javaPath + "/release"));
        String currLine;
        while ((currLine = jreReleaseReader.readLine()) != null) {
            if (currLine.contains("=")) {
                String[] keyValue = currLine.split("=");
                jreReleaseMap.put(keyValue[0], keyValue[1].replace("\"", ""));
            }
        }
        jreReleaseReader.close();
        return jreReleaseMap;
    }

    public static String getJavaLibDir(String javaPath) throws IOException {
        String architecture = readJREReleaseProperties(javaPath).get("OS_ARCH");
        if (Architecture.archAsInt(architecture) == ARCH_X86) {
            architecture = "i386/i486/i586";
        }
        String libDir = "/lib";
        if (architecture == null) {
            throw new IOException("Unsupported architecture!");
        }
        for (String arch : architecture.split("/")) {
            File file = new File(javaPath, "lib/" + arch);
            if (file.exists() && file.isDirectory()) {
                libDir = "/lib/" + arch;
            }
        }
        return libDir;
    }

    private static String getJvmLibDir(String javaPath) throws IOException {
        String jvmLibDir;
        String path = (isJDK8(javaPath) ? "/jre" : "") + getJavaLibDir(javaPath);
        File jvmFile = new File(javaPath + path + "/server/libjvm.so");
        jvmLibDir = jvmFile.exists() ? "/server" : "/client";
        return jvmLibDir;
    }

    private static String getLibraryPath(Context context, String javaPath, String pluginLibPath, String lwjglVersion) throws IOException {
        StringBuilder sb = new StringBuilder();
        String split = ":";

        if (javaPath != null) {
            String javaLibDir = getJavaLibDir(javaPath);
            String jvmLibDir = getJvmLibDir(javaPath);

            sb.append(javaPath).append(javaLibDir).append(split);
            sb.append(javaPath).append(javaLibDir).append("/jli").append(split);

            String jreLibDir;
            if (isJDK8(javaPath)) {
                jreLibDir = javaPath + "/jre" + javaLibDir + jvmLibDir;
                sb.append(jreLibDir).append(split);
                sb.append(javaPath).append("/jre").append(javaLibDir).append(split);
            } else {
                jreLibDir = javaPath + javaLibDir + jvmLibDir;
                sb.append(jreLibDir).append(split);
            }
        }

        return appendCommonPaths(sb, context, pluginLibPath, lwjglVersion);
    }

    private static String getLibraryPath(Context context, String pluginLibPath, String lwjglVersion) {
        return appendCommonPaths(new StringBuilder(), context, pluginLibPath, lwjglVersion);
    }

    private static String appendCommonPaths(StringBuilder sb, Context context, String pluginLibPath, String lwjglVersion) {
        String nativeDir = context.getApplicationInfo().nativeLibraryDir;
        String libDirName = is64BitsDevice() ? "lib64" : "lib";
        String split = ":";

        sb.append(context.getDir("runtime", 0).getAbsolutePath()).append("/jna").append(split);

        if (isValidPathString(pluginLibPath)) {
            sb.append(pluginLibPath).append(split);
        }

        String nativeLibPaths = NativeLibPlugin.getPaths(split);
        if (isValidPathString(nativeLibPaths)) {
            sb.append(nativeLibPaths).append(split);
        }

        sb.append(FCLPath.MOD_RUNTIME_DIR).append(split);
        sb.append("/system/").append(libDirName).append(split);
        sb.append("/vendor/").append(libDirName).append(split);
        sb.append("/vendor/").append(libDirName).append("/hw").append(split);
        sb.append("/system_ext/").append(libDirName).append(split);
        if (isValidPathString(lwjglVersion)) {
            String arch = Architecture.archAsStringAndroid(Architecture.getDeviceArchitecture());
            sb.append(FCLPath.LWJGL_DIR).append("/").append(lwjglVersion).append("/natives/").append(arch).append(split);
        }
        sb.append(nativeDir);

        return sb.toString();
    }

    private static boolean isValidPathString(String path) {
        return path != null && !path.isEmpty() && !path.equals("null");
    }

    private static String[] rebaseArgs(FCLConfig config, String libraryPath) {
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(config.getArgs()));
        argList.add(0, config.getJavaPath() + "/bin/java");
        String[] args = new String[argList.size()];
        for (int i = 0; i < argList.size(); i++) {
            String a = argList.get(i);
            if (argList.get(i).contains("-Djava.library.path")) {
                a = "-Djava.library.path=${natives_directory}";
            }
            a = a.replace("${natives_directory}", libraryPath);
            args[i] = a.replace("${gl_lib_name}", config.getRenderer().getGLPath())
                    .replace("${egl_lib_name}", config.getRenderer().getEGLPath());
        }
        return args;
    }

    /**
     * 实例是否运行 lwjgl3ify：以 config/lwjgl3ify.cfg 存在为准
     */
    private static boolean hasLwjgl3ify(File gameDir) {
        return new File(gameDir, "config/lwjgl3ify.cfg").isFile();
    }

    private static void addCommonEnv(FCLConfig config, HashMap<String, String> envMap) {
        if (FCL_VERSION_CODE != -1) {
            envMap.put("FCL_VERSION_CODE", FCL_VERSION_CODE + "");
        }
        envMap.put("HOME", config.getLogDir());
        envMap.put("JAVA_HOME", config.getJavaPath());
        envMap.put("FCL_NATIVEDIR", config.getContext().getApplicationInfo().nativeLibraryDir);
        envMap.put("POJAV_NATIVEDIR", config.getContext().getApplicationInfo().nativeLibraryDir);
        envMap.put("DRIVER_PATH", DriverPlugin.getSelected().getPath());
        // 自定义驱动加载参数：linkerhook 按 DRIVER_LIB 加载驱动库（Turnip/panvk 等），
        // GPU 门控按 DRIVER_GPU 判定家族（adreno/mali/any）
        envMap.put("DRIVER_LIB", DriverPlugin.getSelected().getLib());
        envMap.put("DRIVER_GPU", DriverPlugin.getSelected().getGpu());
        envMap.put("TMPDIR", config.getContext().getCacheDir().getAbsolutePath());
        envMap.put("PATH", config.getJavaPath() + "/bin:" + Os.getenv("PATH"));
        envMap.put("LD_LIBRARY_PATH", getLibraryPath(config.getContext(), config.getRenderer().getPath(), config.getLwjglVersion()));
        envMap.put("FORCE_VSYNC", "false");
        envMap.put("ALSOFT_DRIVERS", "opensl");

        // Native mod env var
        envMap.put("MOD_ANDROID_RUNTIME", FCLPath.MOD_RUNTIME_DIR == null ? "" : FCLPath.MOD_RUNTIME_DIR);

        // XDG 数据目录兜底（仅 lwjgl3ify 实例注入；workingDir 即 ${game_directory} 指向的实例目录）
        File gameDir = new File(config.getWorkingDir());
        if (hasLwjgl3ify(gameDir)) {
            File xdgDataHome = new File(gameDir, ".local/share");
            if (!xdgDataHome.isDirectory()) {
                //noinspection ResultOfMethodCallIgnored
                xdgDataHome.mkdirs();
            }
            envMap.put("XDG_DATA_HOME", xdgDataHome.getAbsolutePath());
        }

        // Dalvik(ART) 侧 JavaVM 与 Application 全局引用，供游戏 JVM 侧原生代码
        // （如 libflite 桥接安卓 TTS）attach 回安卓运行时调用系统 API
        envMap.put("DALVIK_JAVAVM", String.valueOf(FCLBridge.getJavaVMPointer()));
        envMap.put("DALVIK_APPLICATION", FCLBridge.jObjectToString(config.getContext().getApplicationContext()));

        FFmpegPlugin.discover(config.getContext());
        if (FFmpegPlugin.isAvailable) {
            envMap.put("PATH", FFmpegPlugin.libraryPath + ":" + envMap.get("PATH"));
            envMap.put("LD_LIBRARY_PATH", FFmpegPlugin.libraryPath + ":" + envMap.get("LD_LIBRARY_PATH"));
        }
        if (config.getUseVKDriverSystem()) {
            envMap.put("VULKAN_DRIVER_SYSTEM", "1");
        }
        // 检测判定设备存在可由 vkshim 补齐的驱动缺口（divisor 扩展合成 /
        // fillModeNonSolid 降级模拟）时，经 vkshim 包装系统 Vulkan 加载器
        if (VulkanCheckManager.INSTANCE.getNeedsVulkanShim()) {
            envMap.put("VKSHIM_ENABLE", "1");
        }
    }

    private static void addModLoaderEnv(FCLConfig config, HashMap<String, String> envMap) {
        if (config.getInstalledModLoaders() == null)
            return;

        if (config.getInstalledModLoaders().getInstallForge()) {
            envMap.put("INST_FORGE", "1");
        }
        if (config.getInstalledModLoaders().getInstallCleanroom()) {
            envMap.put("INST_CLEANROOM", "1");
        }
        if (config.getInstalledModLoaders().getInstallNeoForge()) {
            envMap.put("INST_NEOFORGE", "1");
        }
        if (config.getInstalledModLoaders().getInstallLiteLoader()) {
            envMap.put("INST_LITELOADER", "1");
        }
        if (config.getInstalledModLoaders().getInstallFabric()) {
            envMap.put("INST_FABRIC", "1");
        }
        if (config.getInstalledModLoaders().getInstallOptiFine()) {
            envMap.put("INST_OPTIFINE", "1");
        }
        if (config.getInstalledModLoaders().getInstallQuilt()) {
            envMap.put("INST_QUILT", "1");
        }
    }

    private static void addRendererEnv(FCLConfig config, HashMap<String, String> envMap) {
        addRendererEnvInner(config, envMap);
        // SDL 需要独立的 GL/EGL 库绝对路径（GL 用渲染器 GL 库、EGL 用渲染器 EGL 库）。
        // 必须对所有渲染器路径生效（插件渲染器在 inner 中途 return）
        Renderer renderer = config.getRenderer();
        String egl = envMap.get("POJAVEXEC_EGL");
        if (egl != null && !egl.startsWith("/")) {
            File candidate = new File(rendererLibPath(renderer), egl);
            // 仅当库真实打包在渲染器目录内才给 SDL 绝对路径；
            // 不在目录内（如系统 EGL libEGL.so）交给 SDL 按自身默认解析
            if (candidate.isFile()) {
                egl = candidate.getAbsolutePath();
            } else {
                egl = null;
            }
        }
        if (egl != null) {
            envMap.put("SDL_EGL_LIBRARY", egl);
        }
        String gl = renderer.getGlName();
        if (!gl.isEmpty()) {
            // v1 插件约定：glName 以 / 开头表示相对插件 nativeLibraryDir（与 eglName 一致），须拼上插件目录
            if (gl.startsWith("/")) {
                if (!renderer.getPath().isEmpty()) {
                    gl = renderer.getPath() + gl;
                }
            } else {
                gl = rendererLibPath(renderer) + "/" + gl;
            }
            // 与 SDL_EGL_LIBRARY 同策略：路径不存在时不设置，交给 SDL 按自身默认解析
            if (new File(gl).isFile()) {
                envMap.put("SDL_OPENGL_LIBRARY", gl);
            }
        }
    }

    /**
     * 渲染器库所在目录：插件渲染器用其自身 lib 目录（主 APK 目录下没有该库），内置渲染器用主 APK native 目录
     */
    private static String rendererLibPath(Renderer renderer) {
        String pluginPath = renderer.getPath();
        return pluginPath.isEmpty() ? FCLPath.NATIVE_LIB_DIR : pluginPath;
    }

    private static void addRendererEnvInner(FCLConfig config, HashMap<String, String> envMap) {
        Renderer renderer = config.getRenderer();
        if (!renderer.getPath().isEmpty()) {
            if (!renderer.getPojavRendererId().isEmpty()) {
                envMap.put("POJAV_RENDERER", renderer.getPojavRendererId());
            }
            String eglName = renderer.getEglName();
            if (eglName.startsWith("/")) {
                eglName = renderer.getPath() + eglName;
            }
            List<String> envList;
            envMap.put("POJAVEXEC_EGL", eglName);
            envList = renderer.getPojavEnv();
            if (envList != null) {
                envList.forEach(env -> {
                    String[] split = env.split("=");
                    if (split[0].equals("DLOPEN") || split.length < 2) {
                        return;
                    }
                    if (split[0].equals("LIB_MESA_NAME")) {
                        envMap.put(split[0], renderer.getPath() + "/" + split[1]);
                    } else if (split[0].equals("MESA_LIBRARY")) {
                        envMap.put(split[0], renderer.getPath() + "/" + split[1]);
                    } else {
                        envMap.put(split[0], split[1]);
                    }
                });
            }
            // 运行时才能确定的路径类 env 补全：只补缺，不覆盖插件显式设置的值
            if (!envMap.containsKey("MESA_GLSL_CACHE_DIR")) {
                envMap.put("MESA_GLSL_CACHE_DIR", config.getContext().getCacheDir().getAbsolutePath());
            }
            if ("gallium_virgl".equals(envMap.get("POJAV_RENDERER")) && !envMap.containsKey("VTEST_SOCKET_NAME")) {
                envMap.put("VTEST_SOCKET_NAME", new File(config.getContext().getCacheDir(), ".virgl_test").getAbsolutePath());
            }
            // 插件目录内带 virgl test server 时交给原生侧从该绝对路径加载（原生默认只在主 APK 目录找）
            File vts = new File(rendererLibPath(renderer), "libvirgl_test_server.so");
            if (vts.isFile()) {
                envMap.put("VIRGL_TEST_SERVER", vts.getAbsolutePath());
            }
            return;
        }
        if (renderer.isEqual(Renderer.ID_GL4ES) || renderer.isEqual(Renderer.ID_VGPU)) {
            envMap.put("LIBGL_ES", "2");
            envMap.put("LIBGL_MIPMAP", "3");
            envMap.put("LIBGL_NORMALIZE", "1");
            envMap.put("LIBGL_NOINTOVLHACK", "1");
            envMap.put("LIBGL_NOERROR", "1");
            if (renderer.getId().equals(Renderer.ID_GL4ES)) {
                envMap.put("POJAV_RENDERER", "opengles2");
            } else {
                envMap.put("POJAV_RENDERER", "opengles2_vgpu");
            }
        } else if (renderer.isEqual(Renderer.ID_NGGL4ES)) {
            envMap.put("LIBGL_USE_MC_COLOR", "1");
            envMap.put("DLOPEN", "libspirv-cross-c-shared.so");
            envMap.put("LIBGL_GL", "31");
            envMap.put("LIBGL_ES", "3");
            envMap.put("LIBGL_NORMALIZE", "1");
            envMap.put("LIBGL_NOINTOVLHACK", "1");
            envMap.put("LIBGL_NOERROR", "1");
            // TODO: set NGG_DIR_PATH to custom path
            envMap.put("POJAV_RENDERER", "opengles3");
            envMap.put("POJAVEXEC_EGL", "libEGL.so");
        } else {
            // VirGL/Freedreno 已剥离为插件（走上方提前返回）；此分支处理 ZINK 与 MobileGlues/SFPEW
            if (renderer.isEqual(Renderer.ID_ZINK)) {
                envMap.put("MESA_GLSL_CACHE_DIR", config.getContext().getCacheDir().getAbsolutePath());
                envMap.put("MESA_GL_VERSION_OVERRIDE", "4.6");
                envMap.put("MESA_GLSL_VERSION_OVERRIDE", "460");
                envMap.put("force_glsl_extensions_warn", "true");
                envMap.put("allow_higher_compat_version", "true");
                envMap.put("allow_glsl_extension_directive_midshader", "true");
                envMap.put("POJAV_RENDERER", "opengles3_desktopgl_zink_kopper");
                envMap.put("LIBGL_ES", "3");
                envMap.put("POJAVEXEC_EGL", renderer.getEglName());
            } else if (renderer.isEqual(Renderer.ID_MOBILEGLUES)) {
                envMap.put("POJAV_RENDERER", "opengles3_mobileglues");
                envMap.put("LIBGL_ES", "3");
                envMap.put("LIBGL_EGL", "libmobileglues.so");
                envMap.put("POJAVEXEC_EGL", "libmobileglues.so");
                // 内置版使用独立配置：不共用 /sdcard/MG，配置写在应用私有存储并经 MG_DIR_PATH 指向
                Map<String, String> mgPrefs = RendererPlugin.builtinEnvPrefs(Renderer.ID_MOBILEGLUES);
                File mgDir = new File(config.getContext().getFilesDir(), "mobileglues");
                writeMobileGluesConfig(mgDir, mgPrefs);
                envMap.put("MG_DIR_PATH", mgDir.getAbsolutePath());
                if (RendererPlugin.toggleEnabled(mgPrefs, "MG_COUNT_LAUNCH", true)) {
                    envMap.put("MG_COUNT_LAUNCH", "1");
                }
            } else if (renderer.isEqual(Renderer.ID_SFPEW)) {
                envMap.put("POJAV_RENDERER", "opengles3_sfpew");
                envMap.put("LIBGL_ES", "3");
                envMap.put("POJAVEXEC_EGL", "libEGL.so");
                // SFPEW 后端为系统 libEGL；单线程单上下文优化默认开启
                Map<String, String> sfpewPrefs = RendererPlugin.builtinEnvPrefs(Renderer.ID_SFPEW);
                if (RendererPlugin.toggleEnabled(sfpewPrefs, "SFPEW_RELAXED_CONTEXT", true)) {
                    envMap.put("SFPEW_RELAXED_CONTEXT", "1");
                }
            }
        }
    }

    private static void setEnv(FCLConfig config, FCLBridge bridge, boolean render) {
        HashMap<String, String> envMap = new HashMap<>();
        addCommonEnv(config, envMap);
        addModLoaderEnv(config, envMap);
        if (render) {
            addRendererEnv(config, envMap);
        }
        addCustomEnv(config, envMap);
        printTaskTitle(bridge, "Env Map");
        for (String key : envMap.keySet()) {
            log(bridge, "Env: " + key + "=" + envMap.get(key));
            bridge.setenv(key, envMap.get(key));
        }
    }

    private static void addCustomEnv(FCLConfig config, HashMap<String, String> envMap) {
        SharedPreferences preferences = config.getContext().getSharedPreferences("launcher", MODE_PRIVATE);
        String[] env = preferences.getString("env", "").split("\n");
        for (String e : env) {
            try {
                String[] split = e.split("=", 2);
                if (split.length == 2) {
                    envMap.put(split[0], split[1]);
                }
            } catch (Throwable ignore) {
            }
        }

    }

    /**
     * 把内置 MobileGlues 的用户配置合并进 MG/config.json（键名与默认值对齐官方 MGConfig）。
     * 只覆盖启动器里显式配置过的键；未配置的已知键与原生侧写入的未知键（如 hideMGEnvLevel）
     * 保留原值交给 MG 自管（MG 上游调整默认值时不受启动器钉死）。内容无变化时不重写。
     */
    private static void writeMobileGluesConfig(File mgDir, Map<String, String> prefs) {
        if (prefs.isEmpty()) return;
        File cfgFile = new File(mgDir, "config.json");
        JSONObject json = new JSONObject();
        String oldCfg = null;
        if (cfgFile.isFile()) {
            try {
                oldCfg = new String(readFileBytes(cfgFile), StandardCharsets.UTF_8);
                json = new JSONObject(oldCfg);
            } catch (Throwable ignored) {
                json = new JSONObject();
            }
        }
        try {
            mergeMobileGluesConfig(json, prefs);
        } catch (JSONException ignored) {
            return;
        }
        String newCfg = json.toString();
        if (newCfg.equals(oldCfg)) return;
        try {
            mgDir.mkdirs();
            try (FileOutputStream out = new FileOutputStream(cfgFile)) {
                out.write(newCfg.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {
        }
    }

    /** 仅合并用户显式配置过的键（开关型只写开关键，无值键）；默认值须与 RendererPlugin.builtinEnvSpecs 一致 */
    private static void mergeMobileGluesConfig(JSONObject json, Map<String, String> prefs) throws JSONException {
        if (RendererPlugin.isConfigured(prefs, "enableANGLE")) {
            json.put("enableANGLE", mgPrefInt(prefs, "enableANGLE", 1));
        }
        if (RendererPlugin.isConfigured(prefs, "enableNoError")) {
            json.put("enableNoError", mgPrefInt(prefs, "enableNoError", 0));
        }
        if (RendererPlugin.isConfigured(prefs, "angleDepthClearFixMode")) {
            json.put("angleDepthClearFixMode", RendererPlugin.toggleEnabled(prefs, "angleDepthClearFixMode", false) ? 1 : 0);
        }
        if (RendererPlugin.isConfigured(prefs, "customGLVersion")) {
            json.put("customGLVersion", mgPrefInt(prefs, "customGLVersion", 0));
        }
        if (RendererPlugin.isConfigured(prefs, "maxGlslCacheSize")) {
            json.put("maxGlslCacheSize", mgPrefInt(prefs, "maxGlslCacheSize", 32));
        }
        if (RendererPlugin.isConfigured(prefs, "enableExtTimerQuery")) {
            json.put("enableExtTimerQuery", RendererPlugin.toggleEnabled(prefs, "enableExtTimerQuery", true) ? 1 : 0);
        }
        if (RendererPlugin.isConfigured(prefs, "enableExtComputeShader")) {
            json.put("enableExtComputeShader", RendererPlugin.toggleEnabled(prefs, "enableExtComputeShader", false) ? 1 : 0);
        }
        if (RendererPlugin.isConfigured(prefs, "enableExtDirectStateAccess")) {
            json.put("enableExtDirectStateAccess", RendererPlugin.toggleEnabled(prefs, "enableExtDirectStateAccess", false) ? 1 : 0);
        }
        if (RendererPlugin.isConfigured(prefs, "fsr1Setting")) {
            json.put("fsr1Setting", mgPrefInt(prefs, "fsr1Setting", 0));
        }
    }

    /** 读取用户配置的整数值；未设置回落默认，显式清空视为 -1（MG 语义：≤0 关闭），非法值回落默认 */
    private static int mgPrefInt(Map<String, String> prefs, String key, int def) {
        String v = prefs.get(key);
        if (v == null) return def;
        v = v.trim();
        if (v.isEmpty()) return -1;
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static byte[] readFileBytes(File file) throws IOException {
        byte[] data = new byte[(int) file.length()];
        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            in.readFully(data);
        }
        return data;
    }

    private static void setUpJavaRuntime(FCLConfig config, FCLBridge bridge) throws IOException {
        printTaskTitle(bridge, "DLOPEN");
        String javaLibDir = config.getJavaPath() + getJavaLibDir(config.getJavaPath());
        String jliLibDir = new File(javaLibDir + "/jli/libjli.so").exists() ? javaLibDir + "/jli" : javaLibDir;
        if (isJDK8(config.getJavaPath()))
            javaLibDir = config.getJavaPath() + "/jre" + getJavaLibDir(config.getJavaPath());
        String jvmLibDir = javaLibDir + getJvmLibDir(config.getJavaPath());
        // dlopen jre
        bridge.dlopen(jliLibDir + "/libjli.so");
        bridge.dlopen(jvmLibDir + "/libjvm.so");
        bridge.dlopen(javaLibDir + "/libfreetype.so");
        bridge.dlopen(javaLibDir + "/libverify.so");
        bridge.dlopen(javaLibDir + "/libjava.so");
        bridge.dlopen(javaLibDir + "/libnet.so");
        bridge.dlopen(javaLibDir + "/libnio.so");
        bridge.dlopen(javaLibDir + "/libawt.so");
        bridge.dlopen(javaLibDir + "/libawt_headless.so");
        bridge.dlopen(javaLibDir + "/libfontmanager.so");
        for (File file : locateLibs(new File(config.getJavaPath()))) {
            bridge.dlopen(file.getAbsolutePath());
        }
    }

    public static ArrayList<File> locateLibs(File path) {
        ArrayList<File> returnValue = new ArrayList<>();
        File[] list = path.listFiles();
        if (list != null) {
            for (File f : list) {
                if (f.isFile() && f.getName().endsWith(".so")) {
                    returnValue.add(f);
                } else if (f.isDirectory()) {
                    returnValue.addAll(locateLibs(f));
                }
            }
        }
        return returnValue;
    }

    private static void setupGraphicAndSoundEngine(FCLConfig config, FCLBridge bridge) {
        String nativeDir = config.getContext().getApplicationInfo().nativeLibraryDir;

        bridge.dlopen(nativeDir + "/libopenal.so");
        if (!config.getRenderer().getPath().isEmpty()) {
            List<String> envList = config.getRenderer().getPojavEnv();
            if (envList != null) {
                envList.forEach(env -> {
                    String[] split = env.split("=");
                    if (split[0].equals("DLOPEN")) {
                        String[] libs = split[1].split(",");
                        for (String lib : libs) {
                            bridge.dlopen(config.getRenderer().getPath() + "/" + lib);
                        }
                    }
                });
            }
            long handle = bridge.dlopen(config.getRenderer().getGLPath());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                try {
                    Os.setenv("RENDERER_HANDLE", handle + "", true);
                } catch (ErrnoException ignore) {
                }
            }
//            bridge.dlopen(RendererPlugin.getSelected().getPath() + "/" + RendererPlugin.getSelected().getEglName());
        } else {
            bridge.dlopen(nativeDir + "/" + config.getRenderer().getGlName());
        }
    }

    private static void launch(FCLConfig config, FCLBridge bridge, String task) throws IOException {
        printTaskTitle(bridge, task + " Arguments");
        String libraryPath = getLibraryPath(config.getContext(), config.getJavaPath(), config.getRenderer().getPath(), config.getLwjglVersion());
        String[] args = rebaseArgs(config, libraryPath);
        boolean javaArgs = true;
        int mainClass = 0;
        boolean isToken = false;
        for (String arg : args) {
            if (javaArgs)
                javaArgs = !arg.equals("mio.Wrapper");
            String title = task.equals("Minecraft") ? javaArgs ? "Java" : task : task;
            String prefix = title + " argument: ";
            if (task.equals("Minecraft") && !javaArgs && mainClass < 2) {
                mainClass++;
                prefix = "MainClass: ";
            }
            if (isToken) {
                isToken = false;
                log(bridge, prefix + "***");
                continue;
            }
            if (arg.equals("--accessToken"))
                isToken = true;
            log(bridge, prefix + arg);
        }
        bridge.setLdLibraryPath(libraryPath);
        bridge.setupExitTrap(bridge);
        FCLBridge.initializeHooks();
        log(bridge, "Hook success");
        int exitCode = VMLauncher.launchJVM(args);
        bridge.onExit(exitCode);
    }

    public static FCLBridge launchMinecraft(FCLConfig config) {
        FCLBridge bridge = launchProcess(config, FCLPath.LATEST_GAME_LOG, "Minecraft", true, true, true);
        // 标志先落在 bridge 实例上：launch 线程无 Looper，不能在此触碰 CallbackBridge（类初始化含 Choreographer）
        bridge.setCleanroomActive(config.getInstalledModLoaders() != null
                && config.getInstalledModLoaders().getInstallCleanroom());
        return bridge;
    }

    public static FCLBridge launchJarExecutor(FCLConfig config) {
        return launchProcess(config, "latest_jar_executor.log", "Jar Executor", true, false, false);
    }

    public static FCLBridge launchAPIInstaller(FCLConfig config) {
        return launchProcess(config, "latest_api_installer.log", "API Installer", false, false, false);
    }

    private static FCLBridge launchProcess(FCLConfig config, String logName, String task, boolean render, boolean logModList, boolean highPriority) {

        // initialize FCLBridge
        FCLBridge bridge = new FCLBridge();
        bridge.setLogPath(config.getLogDir() + "/" + logName);
        Thread thread = new Thread(() -> {
            try {
                logStartInfo(config, bridge, task);
                if (logModList) {
                    logModList(bridge);
                }

                // env
                setEnv(config, bridge, render);

                // setup java runtime
                setUpJavaRuntime(config, bridge);

                if (render) {
                    // setup graphic and sound engine
                    setupGraphicAndSoundEngine(config, bridge);
                }

                // set working directory
                log(bridge, "Working directory: " + config.getWorkingDir());
                bridge.chdir(config.getWorkingDir());

                // launch
                launch(config, bridge, task);
            } catch (IOException e) {
                Logging.LOG.log(Level.SEVERE, e.toString());
            }
        });

        if (highPriority) {
            thread.setPriority(Thread.MAX_PRIORITY);
        }
        bridge.setThread(thread);

        return bridge;
    }

    public static String getSocName() {
        String name = null;
        try {
            Process process = Runtime.getRuntime().exec("getprop ro.soc.model");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            name = reader.readLine();
            reader.close();
        } catch (Exception ignore) {
        }
        return (name == null || name.trim().isEmpty()) ? Build.HARDWARE : name;
    }

    public static String getDeviceName() {
        String modelName = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        String product = Build.PRODUCT;
        if (!manufacturer.isEmpty()) {
            manufacturer = Character.toUpperCase(manufacturer.charAt(0))
                    + manufacturer.substring(1).toLowerCase();
        }
        return String.format("%s %s %s", manufacturer, product, modelName);
    }

    public static boolean isJDK8(String javaPath) {
        return new File(javaPath, "jre").exists() && new File(javaPath, "bin/javac").exists();
    }

}
