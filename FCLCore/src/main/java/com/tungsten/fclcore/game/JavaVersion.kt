package com.tungsten.fclcore.game

import com.mio.JavaManager
import com.tungsten.fclauncher.utils.FCLPath

class JavaVersion(val isAuto: Boolean, val versionName: String, val name: String) {

    fun getJavaPath(version: Version): String {
        val javaVersion = if (isAuto) getSuitableJavaVersion(version) else this
        return "${FCLPath.JAVA_PATH}/${javaVersion.name}"
    }

    fun getVersion(): Int {
        val split = versionName.split(".")
        return if (split[0] == "1") {
            split[1].toInt()
        } else {
            split[0].toInt()
        }
    }

    companion object {
        const val JAVA_VERSION_8: Int = 8
        const val JAVA_VERSION_11: Int = 11
        const val JAVA_VERSION_17: Int = 17
        const val JAVA_VERSION_21: Int = 21

        @JvmField
        val JAVA_AUTO: JavaVersion = JavaVersion(true, "1.8", "Auto")

        @JvmStatic
        fun getSuitableJavaVersion(version: Version): JavaVersion {
            return JavaManager.getSuitableJavaVersion(version)
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is JavaVersion) false
        else name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
