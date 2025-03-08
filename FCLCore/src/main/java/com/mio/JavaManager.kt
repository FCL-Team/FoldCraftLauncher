package com.mio

import android.util.Log
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.game.JavaVersion
import com.tungsten.fclcore.game.Version
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
        javaList.add(JavaVersion(true, 8, "Auto"))
        File(FCLPath.JAVA_PATH).listFiles()?.forEach {
            addToJavaVersion(it)
        }
        javaList.forEach {
            Log.e("测试", "${it.version} ${it.versionName}")
        }
    }

    fun addToJavaVersion(javaDir: File) {
        if (javaDir.isDirectory && javaDir.resolve("release").exists()) {
            val version =
                Regex("JAVA_VERSION=\"([^\"]+)\"").find(javaDir.resolve("release").readText())
                    ?.let { match ->
                        match.groupValues[1]
                    } ?: return
            val split = version.split(".")
            var versionInt = if (split[0] == "1") {
                split[1].toInt()
            } else {
                split[0].toInt()
            }
            javaList.add(JavaVersion(false, versionInt, javaDir.name))
        }
    }

    @JvmStatic
    fun getJavaFromVersionName(versionName: String): JavaVersion {
        return javaList.find { it.versionName == versionName } ?: javaList[0]
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

        for (java in javaList) {
            when {
                java.version == version -> {
                    exact = java
                    break
                }

                java.version > version -> {
                    closestGreater = when {
                        closestGreater == null -> java
                        java.version < closestGreater.version -> java
                        else -> closestGreater
                    }
                }
            }
        }
        return exact ?: closestGreater!!
    }
}