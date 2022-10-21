package com.tungsten.fclauncher;

import static com.tungsten.fclauncher.utils.Architecture.ARCH_X86;
import static com.tungsten.fclauncher.utils.Architecture.is64BitsDevice;

import android.content.Context;
import android.util.ArrayMap;

import com.jaredrummler.android.device.DeviceName;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.utils.Architecture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FCLauncher {

    // Todo : don't crash when launch 1.17+ with OpenGL 2.1
    // Todo : mouse scroll event
    // Todo : custom logger
    // Todo : mesa

    private static void printTaskTitle(String task) {
        System.out.println("==================== " + task + " ====================");
    }

    private static void logStartInfo(String task) {
        printTaskTitle("Start " + task);
        System.out.println("Device: " + DeviceName.getDeviceName());
        System.out.println("Architecture: " + Architecture.archAsString(Architecture.getDeviceArchitecture()));
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

    private static String getJreLibDir(String javaPath) throws IOException {
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

    private static String getLibraryPath(Context context, String javaPath) throws IOException {
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

                nativeDir;
    }

    private static String[] rebaseArgs(FCLConfig config) throws IOException {
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(config.getArgs()));
        argList.add(0, config.getJavaPath() + "/bin/java");
        String[] args = new String[argList.size()];
        for (int i = 0; i < argList.size(); i++) {
            args[i] = argList.get(i).replace("${natives_directory}", getLibraryPath(config.getContext(), config.getJavaPath()));
            args[i] = argList.get(i).replace("${gl_lib_name}", config.getRenderer().getGlLibName());
        }
        return args;
    }

    private static void addCommonEnv(FCLConfig config, HashMap<String, String> envMap) {
        envMap.put("HOME", config.getLogDir());
        envMap.put("JAVA_HOME", config.getJavaPath());
    }

    private static void addRendererEnv(FCLConfig config, HashMap<String, String> envMap) {
        // Todo : mesa env
        String nativeDir = config.getContext().getApplicationInfo().nativeLibraryDir;
        envMap.put("LIBGL_NAME", config.getRenderer().getGlLibName());
        envMap.put("LIBEGL_NAME", config.getRenderer().getEglLibName());
        if (config.getRenderer() == FCLConfig.Renderer.RENDERER_GL4ES) {
            envMap.put("LIBGL_MIPMAP", "3");
            envMap.put("LIBGL_NORMALIZE", "1");
            envMap.put("LIBGL_VSYNC", "1");
            envMap.put("LIBGL_NOINTOVLHACK", "1");
        }
        else {
            envMap.put("LIBGL_DRIVERS_PATH", nativeDir);
            envMap.put("MESA_GL_VERSION_OVERRIDE", "4.6");
            envMap.put("MESA_GLSL_VERSION_OVERRIDE", "460");
            envMap.put("GALLIUM_DRIVER", "zink");
            envMap.put("MESA_GLSL_CACHE_DIR", config.getContext().getCacheDir().getAbsolutePath());
        }
    }

    private static void setEnv(FCLConfig config, FCLBridge bridge, boolean render) {
        HashMap<String, String> envMap = new HashMap<>();
        addCommonEnv(config, envMap);
        if (render) {
            addRendererEnv(config, envMap);
        }
        printTaskTitle("Env Map");
        for (String key : envMap.keySet()) {
            System.out.println("Env: " + key + "=" + envMap.get(key));
            bridge.setenv(key, envMap.get(key));
        }
        printTaskTitle("Env Map");
    }

    private static void setUpJavaRuntime(FCLConfig config, FCLBridge bridge) throws IOException {
        String jreLibDir = config.getJavaPath() + getJreLibDir(config.getJavaPath());
        String jliLibDir = jreLibDir + "/jli";
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
    }

    private static void setupGraphicAndSoundEngine(FCLConfig config, FCLBridge bridge) {
        String nativeDir = config.getContext().getApplicationInfo().nativeLibraryDir;

        bridge.dlopen(nativeDir + "/libopenal.so");

        // Todo : mesa
        bridge.dlopen(nativeDir + "/" + config.getRenderer().getGlLibName());
        bridge.dlopen(nativeDir + "/" + config.getRenderer().getEglLibName());
        if (config.getRenderer() == FCLConfig.Renderer.RENDERER_ZINK) {
            bridge.dlopen(nativeDir + "/libglapi.so");
            bridge.dlopen(nativeDir + "/libexpat.so");
            bridge.dlopen(nativeDir + "/zink_dri.so");
        }
    }

    private static void launch(FCLConfig config, FCLBridge bridge, String task) throws IOException {
        printTaskTitle(task + " Arguments");
        String[] args = rebaseArgs(config);
        for (String arg : args) {
            System.out.println(task + " argument: " + arg);
        }
        bridge.setupJLI();
        System.out.println("OpenJDK exited with code : " + bridge.jliLaunch(args));
    }

    public static FCLBridge launchMinecraft(FCLConfig config) {

        // initialize FCLBridge
        FCLBridge bridge = new FCLBridge(config.getContext(), config.getCallback());

        Thread gameThread = new Thread(() -> {
            try {
                // redirect log path
                bridge.redirectStdio(config.getLogDir() + "/latest_game.log");

                logStartInfo("Minecraft");

                // set graphic output and event pipe
                bridge.setFCLNativeWindow(config.getSurface());
                bridge.setEventPipe();

                // env
                setEnv(config, bridge, true);

                // setup java runtime
                setUpJavaRuntime(config, bridge);

                // setup graphic and sound engine
                setupGraphicAndSoundEngine(config, bridge);

                // hook exit
                bridge.setupExitTrap(bridge);

                // set working directory
                System.out.println("Working directory: " + config.getWorkingDir());
                bridge.chdir(config.getWorkingDir());

                // launch game
                launch(config, bridge, "Minecraft");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gameThread.setPriority(Thread.MAX_PRIORITY);
        gameThread.start();

        return bridge;
    }

    public static FCLBridge launchJavaGUI(FCLConfig config) {

        // initialize FCLBridge
        FCLBridge bridge = new FCLBridge(config.getContext(), config.getCallback());

        Thread javaGUIThread = new Thread(() -> {
            try {
                // redirect log path
                bridge.redirectStdio(config.getLogDir() + "/latest_java_gui.log");

                logStartInfo("Java GUI");

                // set graphic output and event pipe
                bridge.setFCLNativeWindow(config.getSurface());
                bridge.setEventPipe();

                // env
                setEnv(config, bridge, true);

                // setup java runtime
                setUpJavaRuntime(config, bridge);

                // setup graphic and sound engine
                setupGraphicAndSoundEngine(config, bridge);

                // hook exit
                bridge.setupExitTrap(bridge);

                // set working directory
                System.out.println("Working directory: " + config.getWorkingDir());
                bridge.chdir(config.getWorkingDir());

                // launch java gui
                launch(config, bridge, "Java GUI");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        javaGUIThread.start();

        return bridge;
    }

    public static void launchAPIInstaller(FCLConfig config) {

        // initialize FCLBridge
        FCLBridge bridge = new FCLBridge(config.getContext(), config.getCallback());

        Thread apiInstallerThread = new Thread(() -> {
            try {
                // redirect log path
                bridge.redirectStdio(config.getLogDir() + "/latest_api_installer.log");

                logStartInfo("API Installer");

                // env
                setEnv(config, bridge, false);

                // setup java runtime
                setUpJavaRuntime(config, bridge);

                // hook exit
                bridge.setupExitTrap(bridge);

                // set working directory
                System.out.println("Working directory: " + config.getWorkingDir());
                bridge.chdir(config.getWorkingDir());

                // launch api installer
                launch(config, bridge, "API Installer");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        apiInstallerThread.start();
    }

}
