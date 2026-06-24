package com.tungsten.fcl.util;

import android.os.Build;

/**
 * 通过 JNI 调用 control-converter 项目编译的原生库 {@code libcc.so}
 * 实现 FCL 控制布局与 ZalithLauncher2 (ZL2) 布局之间的转换。
 *
 * 原生库来源：d:\project\control-converter\go (Go 实现, c-shared 编译)
 * 打包位置：jniLibs/arm64-v8a/libcc.so
 * 通过 JNI 调用，避免命令执行方式在 Android 上的兼容性问题。
 */
public final class LayoutConverter {

    static {
        System.loadLibrary("cc");
    }

    private LayoutConverter() {
    }

    /** 当前设备是否支持运行转换（仅 arm64-v8a）。 */
    public static boolean isSupported() {
        for (String abi : Build.SUPPORTED_ABIS) {
            if ("arm64-v8a".equals(abi)) {
                return true;
            }
        }
        return false;
    }

    /**
     * JNI 原生方法：将 FCL 控制布局 JSON 转换为 ZL2 格式。
     *
     * @param inputPath  FCL 控制布局 JSON 文件路径
     * @param outputPath 转换后的 ZL2 JSON 输出文件路径
     * @return 转换成功返回 null；失败返回错误信息
     */
    private static native String convertFclToZl2Native(String inputPath, String outputPath);

    /**
     * 将 FCL 控制布局 JSON 转换为 ZL2 格式。
     * <p>同步阻塞，调用方需在后台线程执行。
     *
     * @param input  FCL 控制布局 JSON 文件
     * @param output 转换后的 ZL2 JSON 输出文件
     * @return 转换成功返回 null；失败返回错误信息
     */
    public static String convertFclToZl2(java.io.File input, java.io.File output) {
        try {
            return convertFclToZl2Native(input.getAbsolutePath(), output.getAbsolutePath());
        } catch (Throwable t) {
            return t.getClass().getSimpleName() + ": " + t.getMessage();
        }
    }
}
