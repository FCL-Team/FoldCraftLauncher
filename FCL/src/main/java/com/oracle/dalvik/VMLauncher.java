package com.oracle.dalvik;

public final class VMLauncher {
    private VMLauncher() {
    }
    /**
     * 直启 JVM：jvmLibPath 为目标 HotSpot 的 libjvm.so 绝对路径（显式 dlopen，
     * 避免 RTLD_DEFAULT 命中 ART 的同名导出——app 进程 ART VM 已存在，调用返回 JNI_EEXIST）；
     * vmArgs 为 JVM 选项（classpath 以 -Djava.class.path 形式给出），主类与应用参数分离传递
     */
    public static native int launchJVM(String jvmLibPath, String[] vmArgs, String mainClass, String[] appArgs);
}
