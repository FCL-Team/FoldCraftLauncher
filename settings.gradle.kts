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
include(":FCLLibrary")
include(":Terracotta")
include(":ZipFileSystem")
include(":LWJGL")
include(":LWJGL:3.3.3")
include(":LWJGL:3.4.1")
//include(":NG-GL4ES")