package com.tungsten.fclcore.game

import com.mio.JavaManager
import com.tungsten.fclauncher.utils.FCLPath

class JavaVersion(val isAuto: Boolean, val version: Int, val versionName: String) {

    fun getJavaPath(version: Version): String {
        val javaVersion = if (isAuto) getSuitableJavaVersion(version) else this
        return "${FCLPath.JAVA_PATH}/${javaVersion.versionName}"
    }

    companion object {
        const val JAVA_VERSION_8: Int = 8
        const val JAVA_VERSION_11: Int = 11
        const val JAVA_VERSION_17: Int = 17
        const val JAVA_VERSION_21: Int = 21

        @JvmField
        val JAVA_AUTO: JavaVersion = JavaVersion(true, JAVA_VERSION_8, "Auto")

        @JvmStatic
        fun getSuitableJavaVersion(version: Version): JavaVersion {
            return JavaManager.getSuitableJavaVersion(version)
        }
    }
}
