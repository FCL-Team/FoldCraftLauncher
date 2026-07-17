pluginManagement {
    repositories {
        // 国内镜像优先，加速插件下载
        maven("https://maven.aliyun.com/repository/gradle-plugin")
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
        // 国内镜像优先
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/public")
        // 官方仓库兜底
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
rootProject.name = "Fold Craft Launcher"
include(":FCL")
include(":FCLCore")
include(":FCLauncher")
include(":FCLLibrary")
include(":LWJGL-Pojav")
include(":Terracotta")
include(":ZipFileSystem")
//include(":NG-GL4ES")
