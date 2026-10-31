package com.oracle.dalvik;

public final class VMLauncher {
    private VMLauncher() {
    }
    /** 直启 JVM：vmArgs 为 JVM 选项（classpath 以 -Djava.class.path 形式给出），主类与应用参数分离传递 */
    public static native int launchJVM(String[] vmArgs, String mainClass, String[] appArgs);
}
