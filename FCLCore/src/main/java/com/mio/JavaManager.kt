package com.mio

import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.game.JavaVersion
import com.tungsten.fclcore.game.Version
import com.tungsten.fclcore.util.io.FileUtils
import java.io.File

object JavaManager {
    private var isInit = false;

    @JvmStatic
    val javaList: MutableList<JavaVersion> = mutableListOf()
        get() {
            if (!isInit) {
                init()
            }
            return field
        }

    @JvmStatic
    fun init() {
        isInit = true
        javaList.add(JavaVersion(true, "1.8", "Auto"))
        File(FCLPath.JAVA_PATH).listFiles()?.forEach {
            addToJavaVersion(it)
        }
    }

    @JvmStatic
    fun remove(name: String) {
        FileUtils.deleteDirectory(File(FCLPath.JAVA_PATH, name))
        javaList.removeIf { it.name == name }
    }

    fun addToJavaVersion(javaDir: File) {
        if (javaDir.isDirectory && javaDir.resolve("release").exists()) {
            val version =
                Regex("JAVA_VERSION=\"([^\"]+)\"").find(javaDir.resolve("release").readText())
                    ?.let { match ->
                        match.groupValues[1]
                    } ?: return
            javaList.add(JavaVersion(false, version, javaDir.name))
        }
    }

    @JvmStatic
    fun getJavaFromVersionName(name: String): JavaVersion {
        return javaList.find { it.name == name } ?: javaList[0]
    }

    @JvmStatic
    fun getSuitableJavaVersion(version: Version?): JavaVersion {
        if (version == null) {
            return getJavaFromVersionName("jre8")
        }
        return findExactOrNextGreater(version.javaVersion.majorVersion);
    }

    private fun findExactOrNextGreater(version: Int): JavaVersion {
        var exact: JavaVersion? = null
        var closestGreater: JavaVersion? = null

        for (java in javaList.filter { it.name != "Auto" }) {
            when {
                java.getVersion() == version -> {
                    exact = java
                    break
                }

                java.getVersion() > version -> {
                    closestGreater = when {
                        closestGreater == null -> java
                        java.getVersion() < closestGreater.getVersion() -> java
                        else -> closestGreater
                    }
                }
            }
        }
        return exact ?: closestGreater!!
    }
}