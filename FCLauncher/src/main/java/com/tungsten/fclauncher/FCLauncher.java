package com.tungsten.fclauncher;

import static com.tungsten.fclauncher.utils.Architecture.ARCH_X86;
import static com.tungsten.fclauncher.utils.Architecture.is64BitsDevice;

import android.content.Context;
import android.os.Build;
import android.system.Os;
import android.util.ArrayMap;

import com.jaredrummler.android.device.DeviceName;
import com.oracle.dalvik.VMLauncher;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.plugins.FFmpegPlugin;
import com.tungsten.fclauncher.plugins.RendererPlugin;
import com.tungsten.fclauncher.utils.Architecture;
import com.tungsten.fclauncher.utils.FCLPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FCLauncher {

    private static void log(FCLBridge bridge, String log) {
        bridge.getCallback().onLog(log + "\n");
    }

    private static void printTaskTitle(FCLBridge bridge, String task) {
        log(bridge, "==================== " + task + " ====================");
    }

    private static void logStartInfo(FCLBridge bridge, String task) {
        printTaskTitle(bridge, "Start " + task);
        log(bridge, "Device: " + DeviceName.getDeviceName());
        log(bridge, "Architecture: " + Architecture.archAsString(Architecture.getDeviceArchitecture()));
        log(bridge, "CPU:" + Build.HARDWARE);
        log(bridge, "Android SDK: " + Build.VERSION.SDK_INT);
    }

    private static void logModList(FCLBridge bridge) {
        File modsDir = new File(bridge.getGameDir(), "mods");
        if (!modsDir.exists()) return;
        printTaskTitle(bridge, "Mods");
        try (Stream<Path> walk = Files.walk(modsDir.toPath(), Integer.MAX_VALUE)) {
            walk.forEach(path -> {
                File file = path.toFile();
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    log(bridge, "Mod File: " + file.getName());
                }
            });
        } catch (IOException ignore) {
        }
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

    public static String getJreLibDir(String javaPath) throws IOException {
        String jreArchitecture = readJREReleaseProperties(javaPath).get("OS_ARCH");
        if (Architecture.archAsInt(jreArchitecture) == ARCH_X86) {
            jreArchitecture = "i386/i486/i586";
        }
        String jreLibDir = "/lib";
        if (jreArchitecture == null) {
            throw new IOException("Unsupported architecture!");
        }
        for (String arch : jreArchitecture.split("/")) {
            File file = new File(javaPath, "lib/" + arch);
            if (file.exists() && file.isDirectory()) {
                jreLibDir = "/lib/" + arch;
            }
        }
        return jreLibDir;
    }

    private static String getJvmLibDir(String javaPath) throws IOException {
        String jvmLibDir;
        File jvmFile = new File(javaPath + getJreLibDir(javaPath) + "/server/libjvm.so");
        jvmLibDir = jvmFile.exists() ? "/server" : "/client";
        return jvmLibDir;
    }

    private static String getLibraryPath(Context context, String javaPath, String pluginLibPath) throws IOException {
        String nativeDir = context.getApplicationInfo().nativeLibraryDir;
        String libDirName = is64BitsDevice() ? "lib64" : "lib";
        String jreLibDir = getJreLibDir(javaPath);
        String jvmLibDir = getJvmLibDir(javaPath);
        String jliLibDir = "/jli";
        String split = ":";
        return javaPath +
                jreLibDir +
                split +

                javaPath +
                jreLibDir +
                jliLibDir +
                split +

                javaPath +
                jreLibDir +
                jvmLibDir +
                split +

                "/system/" +
                libDirName +
                split +

                "/vendor/" +
                libDirName +
                split +

                "/vendor/" +
                libDirName +
                "/hw" +
                split +

                FCLPath.RUNTIME_DIR + "/jna" +
                split +

                ((pluginLibPath != null) ? pluginLibPath + split : "") +

                nativeDir;
    }

    private static String getLibraryPath(Context context, String pluginLibPath) {
        String nativeDir = context.getApplicationInfo().nativeLibraryDir;
        String libDirName = is64BitsDevice() ? "lib64" : "lib";
        String split = ":";
        return "/system/" +
                libDirName +
                split +

                "/vendor/" +
                libDirName +
                split +

                "/vendor/" +
                libDirName +
                "/hw" +
                split +

                ((pluginLibPath != null) ? pluginLibPath + split : "") +

                nativeDir;
    }

    private static String[] rebaseArgs(FCLConfig config) throws IOException {
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(config.getArgs()));
        argList.add(0, config.getJavaPath() + "/bin/java");
        String[] args = new String[argList.size()];
        for (int i = 0; i < argList.size(); i++) {
            String a = argList.get(i).replace("${natives_directory}", getLibraryPath(config.getContext(), config.getJavaPath(), config.getRenderer() == FCLConfig.Renderer.RENDERER_CUSTOM ? RendererPlugin.getSelected().getPath() : null));
            args[i] = config.getRenderer() == null ? a : a.replace("${gl_lib_name}", config.getRenderer() == FCLConfig.Renderer.RENDERER_CUSTOM ? RendererPlugin.getSelected().getGlName() : config.getRenderer().getGlLibName());
        }
        return args;
    }

    private static void addCommonEnv(FCLConfig config, HashMap<String, String> envMap) {
        envMap.put("HOME", config.getLogDir());
        envMap.put("JAVA_HOME", config.getJavaPath());
        envMap.put("FCL_NATIVEDIR", config.getContext().getApplicationInfo().nativeLibraryDir);
        envMap.put("POJAV_NATIVEDIR", config.getContext().getApplicationInfo().nativeLibraryDir);
        envMap.put("TMPDIR", config.getContext().getCacheDir().getAbsolutePath());
        envMap.put("PATH", config.getJavaPath() + "/bin:" + Os.getenv("PATH"));
        envMap.put("LD_LIBRARY_PATH", getLibraryPath(config.getContext(), config.getRenderer() == FCLConfig.Renderer.RENDERER_CUSTOM ? RendererPlugin.getSelected().getPath() : null));
        envMap.put("FORCE_VSYNC", "false");
        if (!config.getJavaPath().contains("jre8")) {
            String libName = config.getJavaPath().contains("jre17") ? "/libjsph17.so" : "/libjsph21.so";
            envMap.put("JSP", config.getContext().getApplicationInfo().nativeLibraryDir + libName);
        }
        FFmpegPlugin.discover(config.getContext());
        if (FFmpegPlugin.isAvailable) {
            envMap.put("PATH", FFmpegPlugin.libraryPath + ":" + envMap.get("PATH"));
            envMap.put("LD_LIBRARY_PATH", FFmpegPlugin.libraryPath + ":" + envMap.get("LD_LIBRARY_PATH"));
        }
        if (config.isUseVKDriverSystem()) {
            envMap.put("VULKAN_DRIVER_SYSTEM", "1");
        }
        if (config.isPojavBigCore()) {
            envMap.put("POJAV_BIG_CORE_AFFINITY", "1");
        }
    }

    private static void addRendererEnv(FCLConfig config, HashMap<String, String> envMap) {
        FCLConfig.Renderer renderer = config.getRenderer() == null ? FCLConfig.Renderer.RENDERER_GL4ES : config.getRenderer();
        if (renderer == FCLConfig.Renderer.RENDERER_CUSTOM) {
            if (FCLBridge.BACKEND_IS_BOAT) {
                envMap.put("LIBGL_STRING", RendererPlugin.getSelected().getName());
                envMap.put("LIBGL_NAME", RendererPlugin.getSelected().getGlName());
                envMap.put("LIBEGL_NAME", RendererPlugin.getSelected().getEglName());
                RendererPlugin.getSelected().getBoatEnv().forEach(env -> {
                    String[] split = env.split("=");
                    if (split[0].equals("LIB_MESA_NAME")) {
                        envMap.put(split[0], RendererPlugin.getSelected().getPath() + "/" + split[1]);
                    } else {
                        envMap.put(split[0], split[1]);
                    }
                });
            } else {
                envMap.put("POJAVEXEC_EGL", RendererPlugin.getSelected().getEglName());
                RendererPlugin.getSelected().getPojavEnv().forEach(env -> {
                    String[] split = env.split("=");
                    if (split[0].equals("LIB_MESA_NAME")) {
                        envMap.put(split[0], RendererPlugin.getSelected().getPath() + "/" + split[1]);
                    } else {
                        envMap.put(split[0], split[1]);
                    }
                });
            }
            return;
        }
        if (FCLBridge.BACKEND_IS_BOAT) {
            envMap.put("LIBGL_STRING", renderer.toString());
            envMap.put("LIBGL_NAME", renderer.getGlLibName());
            envMap.put("LIBEGL_NAME", renderer.getEglLibName());
        }
        if (renderer == FCLConfig.Renderer.RENDERER_GL4ES || renderer == FCLConfig.Renderer.RENDERER_VGPU) {
            envMap.put("LIBGL_ES", "2");
            envMap.put("LIBGL_MIPMAP", "3");
            envMap.put("LIBGL_NORMALIZE", "1");
            envMap.put("LIBGL_NOINTOVLHACK", "1");
            envMap.put("LIBGL_NOERROR", "1");
            if (!FCLBridge.BACKEND_IS_BOAT) {
                if (renderer == FCLConfig.Renderer.RENDERER_GL4ES) {
                    envMap.put("POJAV_RENDERER", "opengles2");
                } else {
                    envMap.put("POJAV_RENDERER", "opengles2_vgpu");
                }
            }
        } else if (renderer == FCLConfig.Renderer.RENDERER_LTW) {
            envMap.put("LIBGL_ES", "3");
            if (!FCLBridge.BACKEND_IS_BOAT) {
                envMap.put("POJAV_RENDERER", "opengles3_ltw");
                envMap.put("POJAVEXEC_EGL", renderer.getEglLibName());
            }
        } else {
            envMap.put("MESA_GLSL_CACHE_DIR", config.getContext().getCacheDir().getAbsolutePath());
            envMap.put("MESA_GL_VERSION_OVERRIDE", renderer == FCLConfig.Renderer.RENDERER_VIRGL ? "4.3" : "4.6");
            envMap.put("MESA_GLSL_VERSION_OVERRIDE", renderer == FCLConfig.Renderer.RENDERER_VIRGL ? "430" : "460");
            envMap.put("force_glsl_extensions_warn", "true");
            envMap.put("allow_higher_compat_version", "true");
            envMap.put("allow_glsl_extension_directive_midshader", "true");
            envMap.put("MESA_LOADER_DRIVER_OVERRIDE", "zink");
            envMap.put("VTEST_SOCKET_NAME", new File(config.getContext().getCacheDir().getAbsolutePath(), ".virgl_test").getAbsolutePath());
            if (renderer == FCLConfig.Renderer.RENDERER_VIRGL) {
                if (FCLBridge.BACKEND_IS_BOAT) {
                    envMap.put("GALLIUM_DRIVER", "virpipe");
                } else {
                    envMap.put("POJAV_RENDERER", "gallium_virgl");
                }
                envMap.put("OSMESA_NO_FLUSH_FRONTBUFFER", "1");
            } else if (renderer == FCLConfig.Renderer.RENDERER_ZINK) {
                if (FCLBridge.BACKEND_IS_BOAT) {
                    envMap.put("GALLIUM_DRIVER", "zink");
                } else {
                    envMap.put("POJAV_RENDERER", "vulkan_zink");
                }
            } else if (renderer == FCLConfig.Renderer.RENDERER_FREEDRENO) {
                if (FCLBridge.BACKEND_IS_BOAT) {
                    envMap.put("GALLIUM_DRIVER", "freedreno");
                    envMap.put("MESA_LOADER_DRIVER_OVERRIDE", "kgsl");
                } else {
                    envMap.put("POJAV_RENDERER", "gallium_freedreno");
                }
            }
        }
    }

    private static void setEnv(FCLConfig config, FCLBridge bridge, boolean render) {
        HashMap<String, String> envMap = new HashMap<>();
        addCommonEnv(config, envMap);
        if (render) {
            addRendererEnv(config, envMap);
        }
        printTaskTitle(bridge, "Env Map");
        for (String key : envMap.keySet()) {
            log(bridge, "Env: " + key + "=" + envMap.get(key));
            bridge.setenv(key, envMap.get(key));
        }
        printTaskTitle(bridge, "Env Map");
    }

    private static void setUpJavaRuntime(FCLConfig config, FCLBridge bridge) throws IOException {
        String jreLibDir = config.getJavaPath() + getJreLibDir(config.getJavaPath());
        String jliLibDir = new File(jreLibDir + "/jli/libjli.so").exists() ? jreLibDir + "/jli" : jreLibDir;
        String jvmLibDir = jreLibDir + getJvmLibDir(config.getJavaPath());
        // dlopen jre
        bridge.dlopen(jliLibDir + "/libjli.so");
        bridge.dlopen(jvmLibDir + "/libjvm.so");
        bridge.dlopen(jreLibDir + "/libfreetype.so");
        bridge.dlopen(jreLibDir + "/libverify.so");
        bridge.dlopen(jreLibDir + "/libjava.so");
        bridge.dlopen(jreLibDir + "/libnet.so");
        bridge.dlopen(jreLibDir + "/libnio.so");
        bridge.dlopen(jreLibDir + "/libawt.so");
        bridge.dlopen(jreLibDir + "/libawt_headless.so");
        bridge.dlopen(jreLibDir + "/libfontmanager.so");
        bridge.dlopen(jreLibDir + "/libtinyiconv.so");
        bridge.dlopen(jreLibDir + "/libinstrument.so");
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
        if (config.getRenderer() == FCLConfig.Renderer.RENDERER_CUSTOM) {
            bridge.dlopen(RendererPlugin.getSelected().getPath() + "/" + RendererPlugin.getSelected().getGlName());
        } else {
            bridge.dlopen(nativeDir + "/" + config.getRenderer().getGlLibName());
        }
    }

    private static void launch(FCLConfig config, FCLBridge bridge, String task) throws IOException {
        printTaskTitle(bridge, task + " Arguments");
        String[] args = rebaseArgs(config);
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
        bridge.setLdLibraryPath(getLibraryPath(config.getContext(), config.getJavaPath(), config.getRenderer() == FCLConfig.Renderer.RENDERER_CUSTOM ? RendererPlugin.getSelected().getPath() : null));
        bridge.setupExitTrap(bridge);
        log(bridge, "Hook success");
        int exitCode = VMLauncher.launchJVM(args);
        bridge.onExit(exitCode);
    }

    public static FCLBridge launchMinecraft(FCLConfig config) {

        // initialize FCLBridge
        FCLBridge bridge = new FCLBridge();
        bridge.setLogPath(config.getLogDir() + "/latest_game.log");
        Thread gameThread = new Thread(() -> {
            try {
                logStartInfo(bridge, "Minecraft");
                logModList(bridge);

                // env
                setEnv(config, bridge, true);

                // setup java runtime
                setUpJavaRuntime(config, bridge);

                // setup graphic and sound engine
                setupGraphicAndSoundEngine(config, bridge);

                // set working directory
                log(bridge, "Working directory: " + config.getWorkingDir());
                bridge.chdir(config.getWorkingDir());

                // launch game
                launch(config, bridge, "Minecraft");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gameThread.setPriority(Thread.MAX_PRIORITY);
        bridge.setThread(gameThread);

        return bridge;
    }

    public static FCLBridge launchJarExecutor(FCLConfig config) {

        // initialize FCLBridge
        FCLBridge bridge = new FCLBridge();
        bridge.setLogPath(config.getLogDir() + "/latest_jar_executor.log");
        Thread javaGUIThread = new Thread(() -> {
            try {

                logStartInfo(bridge, "Jar Executor");

                // env
                setEnv(config, bridge, true);

                // setup java runtime
                setUpJavaRuntime(config, bridge);

                // setup graphic and sound engine
                setupGraphicAndSoundEngine(config, bridge);

                // set working directory
                log(bridge, "Working directory: " + config.getWorkingDir());
                bridge.chdir(config.getWorkingDir());

                // launch jar executor
                launch(config, bridge, "Jar Executor");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        bridge.setThread(javaGUIThread);

        return bridge;
    }

    public static FCLBridge launchAPIInstaller(FCLConfig config) {

        // initialize FCLBridge
        FCLBridge bridge = new FCLBridge();
        bridge.setLogPath(config.getLogDir() + "/latest_api_installer.log");
        Thread apiInstallerThread = new Thread(() -> {
            try {

                logStartInfo(bridge, "API Installer");

                // env
                setEnv(config, bridge, false);

                // setup java runtime
                setUpJavaRuntime(config, bridge);

                // set working directory
                log(bridge, "Working directory: " + config.getWorkingDir());
                bridge.chdir(config.getWorkingDir());

                // launch api installer
                launch(config, bridge, "API Installer");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        bridge.setThread(apiInstallerThread);

        return bridge;
    }

}
