package com.tungsten.fcl.ui.glass.page.java

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mio.JavaManager
import com.tungsten.fcl.util.RuntimeUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import java.io.File

data class JavaRuntimeItem(
    val name: String,
    val version: String,
    val path: String,
    val isAuto: Boolean
)

class JavaRuntimeStateHolder {

    var isLoading by mutableStateOf(false)
        private set
    val javaList = mutableStateListOf<JavaRuntimeItem>()

    fun load() {
        isLoading = true
        Task.runAsync {
            try {
                val list = JavaManager.javaList.map { java ->
                    JavaRuntimeItem(
                        name = java.name,
                        version = java.versionName,
                        path = if (java.isAuto) "" else "${FCLPath.JAVA_PATH}/${java.name}",
                        isAuto = java.isAuto
                    )
                }
                Schedulers.androidUIThread().execute {
                    javaList.clear()
                    javaList.addAll(list)
                    isLoading = false
                }
            } catch (e: Exception) {
                Schedulers.androidUIThread().execute {
                    isLoading = false
                }
            }
        }.start()
    }

    fun remove(item: JavaRuntimeItem) {
        if (item.isAuto) return
        isLoading = true
        Task.runAsync {
            try {
                JavaManager.remove(item.name)
                Schedulers.androidUIThread().execute { load() }
            } catch (e: Exception) {
                Schedulers.androidUIThread().execute {
                    isLoading = false
                }
            }
        }.start()
    }

    fun install(context: Context, versionName: String) {
        val (targetDir, srcDir) = when (versionName) {
            "jre8" -> FCLPath.JAVA_8_PATH to "app_runtime/java/jre8"
            "jre17" -> FCLPath.JAVA_17_PATH to "app_runtime/java/jre17"
            "jre21" -> FCLPath.JAVA_21_PATH to "app_runtime/java/jre21"
            "jre25" -> FCLPath.JAVA_25_PATH to "app_runtime/java/jre25"
            else -> {
                Toast.makeText(context, "Unsupported Java version: $versionName", Toast.LENGTH_SHORT).show()
                return
            }
        }
        isLoading = true
        Task.runAsync {
            try {
                RuntimeUtils.installJava(context, targetDir, srcDir)
                JavaManager.addToJavaVersion(File(FCLPath.JAVA_PATH, versionName))
                Schedulers.androidUIThread().execute {
                    Toast.makeText(context, "Installed $versionName", Toast.LENGTH_SHORT).show()
                    load()
                }
            } catch (e: Exception) {
                Schedulers.androidUIThread().execute {
                    isLoading = false
                    Toast.makeText(
                        context,
                        e.localizedMessage ?: "Failed to install $versionName",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.start()
    }
}
