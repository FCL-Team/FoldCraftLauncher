package com.oracle.dalvik;

public final class VMLauncher {
    private VMLauncher() {
    }
    /**
     * 直启 JVM：jvmLibPath 为目标 HotSpot 的 libjvm.so 绝对路径（显式 dlopen，
     * 避免 RTLD_DEFAULT 命中 ART 的同名导出——app 进程 ART VM 已存在，调用返回 JNI_EEXIST）；
     * vmArgs 为 JVM 选项（classpath 以 -Djava.class.path 形式给出），主类与应用参数分离传递。
     * 返回负值=JVM 未创建（dlopen/创建失败，可重试创建）；0=main 正常返回；
     * 1=main 抛异常或创建后装配失败（两种情况下 JVM 均已常驻可复用）。
     * main 返回后 JVM 保留为常驻实例，供 {@link #runMainInExistingVM} 复用
     */
    public static native int launchJVM(String jvmLibPath, String[] vmArgs, String mainClass, String[] appArgs);

    /**
     * 常驻 JVM 多阶段复用：classpath 按 ':' 分隔逐项建 URL，为该阶段构造独立
     * URLClassLoader（无父加载器，纯 bootstrap 委派，避免常驻 JVM 首阶段 classpath
     * 遮蔽本阶段同名类）后 invoke main。常驻 JVM 创建后无法再接受 VM 选项，
     * 本阶段的 vmArgs（-Xmx/-D 等）不生效。返回 0=正常返回，1=main 抛异常，-1=启动失败
     */
    public static native int runMainInExistingVM(String classpath, String mainClass, String[] appArgs);
}
