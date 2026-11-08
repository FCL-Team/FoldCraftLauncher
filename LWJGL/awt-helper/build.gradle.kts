plugins {
    java
}

// 游戏 JVM 内 AWT 辅助类，bootclasspath 注入，产物供 jre8/jre17+ 两个 cacio 运行时共用
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    // Java 8 字节码 + API 检查，兼容游戏 jre8 运行时（等效 javac --release 8）
    options.release.set(8)
}

tasks.jar {
    archiveFileName.set("AwtHelper.jar")
    destinationDirectory.set(file("$rootDir/FCL/src/main/assets/game"))
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
