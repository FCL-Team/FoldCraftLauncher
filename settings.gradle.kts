pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
rootProject.name = "Fold Craft Launcher"
include(":FCL")
include(":FCLCore")
include(":FCLauncher")
include(":Terracotta")
include(":ZipFileSystem")
include(":LWJGL")
// 子项目名以数字开头会导致 IntelliJ/Android Studio 无法正确解析模块依赖，
// 因此使用合法名称并重定向 projectDir（目录本身保持版本号命名）
include(":LWJGL:lwjgl-3.3.3")
project(":LWJGL:lwjgl-3.3.3").projectDir = file("LWJGL/3.3.3")
include(":LWJGL:lwjgl-3.4.1")
project(":LWJGL:lwjgl-3.4.1").projectDir = file("LWJGL/3.4.1")
