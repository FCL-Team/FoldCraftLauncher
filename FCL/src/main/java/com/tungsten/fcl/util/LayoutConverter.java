package com.tungsten.fcl.util;

import android.content.Context;
import android.os.Build;

import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.Logging;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * 调用 control-converter 项目编译的 Android aarch64 原生二进制 {@code cc}
 * 实现 FCL 控制布局与 ZalithLauncher2 (ZL2) 布局之间的转换。
 *
 * 二进制来源：d:\project\control-converter\dist\cc (Nuitka 编译, 单文件免依赖)
 * 打包位置：jniLibs/arm64-v8a/libcc.so
 * （Android 10+ 的 W^X 策略禁止执行 filesDir 下的文件，
 *   必须通过 jniLibs 打包，系统会释放到 nativeLibraryDir 并赋予执行权限）
 */
public final class LayoutConverter {

    private static final long PROCESS_TIMEOUT_SECONDS = 30;

    private LayoutConverter() {
    }

    /** 当前设备是否支持运行转换二进制（仅 arm64-v8a）。 */
    public static boolean isSupported() {
        for (String abi : Build.SUPPORTED_ABIS) {
            if ("arm64-v8a".equals(abi)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换二进制在设备上的路径：nativeLibraryDir/libcc.so。
     * 系统从 APK 的 jniLibs 中自动释放并赋予可执行权限。
     */
    public static File getBinaryPath() {
        Context context = FCLPath.CONTEXT;
        String nativeLibraryDir = context.getApplicationInfo().nativeLibraryDir;
        return new File(nativeLibraryDir, "libcc.so");
    }

    /**
     * 将 FCL 控制布局 JSON 转换为 ZL2 格式。
     * <p>同步阻塞，调用方需在后台线程执行。
     *
     * @param input  FCL 控制布局 JSON 文件
     * @param output 转换后的 ZL2 JSON 输出文件
     * @return 转换成功返回 null；失败返回错误信息（含进程 stderr）
     */
    public static String convertFclToZl2(File input, File output) throws java.io.IOException, InterruptedException {
        File binary = getBinaryPath();
        ProcessBuilder pb = new ProcessBuilder(
                binary.getAbsolutePath(),
                "fcl2zl",
                input.getAbsolutePath(),
                output.getAbsolutePath(),
                "--lossless"
        );
        pb.redirectErrorStream(true);
        pb.directory(new File(FCLPath.CACHE_DIR));
        Process process = pb.start();
        String outputLog = com.tungsten.fclcore.util.io.IOUtils.readFullyAsString(process.getInputStream());
        boolean finished = process.waitFor(PROCESS_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            return "Conversion timed out after " + PROCESS_TIMEOUT_SECONDS + "s";
        }
        int exitCode = process.exitValue();
        if (exitCode != 0) {
            Logging.LOG.log(Level.WARNING, "cc fcl2zl failed (exit " + exitCode + "): " + outputLog);
            return "cc exit code " + exitCode + "\n" + outputLog;
        }
        if (!output.exists() || output.length() == 0) {
            return "cc produced no output file\n" + outputLog;
        }
        return null;
    }
}
