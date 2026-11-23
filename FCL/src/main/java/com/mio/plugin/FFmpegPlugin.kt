package com.mio.plugin

import android.content.Context
import android.content.pm.PackageManager
import android.system.Os
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Logging
import java.io.File
import java.util.logging.Level

/**
 * FFmpeg 插件探测：把插件提供的可执行文件以标准命令名暴露到 [FCLPath.FFMPEG_BIN_DIR]，
 * 使游戏进程能通过 PATH 找到 ffmpeg / ffprobe。
 */
object FFmpegPlugin {
    /** 主命令，插件没有该命令时整个目录不会进入 PATH */
    private const val PRIMARY_COMMAND = "ffmpeg"

    /** 链接名 -> 插件 native 目录内的实际文件名 */
    private val exposedExecutables = mapOf(
        PRIMARY_COMMAND to "libffmpeg.so",
        "ffprobe" to "libffprobe.so"
    )

    /** 插件的 native 库目录，插件不可用时为 null */
    @JvmStatic
    var libraryPath: String? = null
        private set

    /** 暴露进游戏 PATH 的命令目录，插件不可用时为 null */
    @JvmStatic
    var binPath: String? = null
        private set

    /** 插件是否可用 */
    @JvmStatic
    var isAvailable: Boolean = false
        private set

    /**
     * 探测并加载 FFmpeg 插件；插件启用状态或安装状态变化后重复调用会刷新状态
     */
    @JvmStatic
    fun discover(context: Context) {
        reset()
        if (!PluginManager.isEnabled(context, PluginManager.FFMPEG_PACKAGE)) {
            clearLinks()
            return
        }
        val info = try {
            context.packageManager.getPackageInfo(
                PluginManager.FFMPEG_PACKAGE,
                PackageManager.GET_SHARED_LIBRARY_FILES
            )
        } catch (_: PackageManager.NameNotFoundException) {
            // 未安装
            clearLinks()
            return
        }
        runCatching {
            val appInfo = info.applicationInfo ?: return
            val libDir = appInfo.nativeLibraryDir ?: return
            val executable = File(libDir, "libffmpeg.so")
            if (!executable.exists()) return

            libraryPath = libDir
            isAvailable = true
            binPath = createLinks(libDir)
            // 游戏 JVM 的 exec hook 通过该变量定位插件可执行文件
            Os.setenv("FFMPEG_PATH", executable.absolutePath, true)
        }.onFailure { e ->
            Logging.LOG.log(Level.WARNING, "加载 FFmpeg 插件失败", e)
        }
    }

    /**
     * 清理插件状态与命令链接
     */
    private fun reset() {
        isAvailable = false
        libraryPath = null
        binPath = null
        runCatching { Os.unsetenv("FFMPEG_PATH") }
    }

    /**
     * 删除命令目录内的全部符号链接
     */
    private fun clearLinks() {
        val binDir = FCLPath.FFMPEG_BIN_DIR?.let(::File) ?: return
        exposedExecutables.keys.forEach { File(binDir, it).delete() }
    }

    /**
     * 为插件可执行文件建立标准命令名的符号链接，返回命令目录；主命令链接创建失败时返回 null。
     * 单个链接失败只跳过该项，不影响其他命令。
     */
    private fun createLinks(libDir: String): String? {
        val binDir = FCLPath.FFMPEG_BIN_DIR?.let(::File) ?: return null
        if (!binDir.isDirectory && !binDir.mkdirs()) {
            Logging.LOG.warning("无法创建 FFmpeg 命令目录: ${binDir.absolutePath}")
            return null
        }
        exposedExecutables.forEach { (name, fileName) ->
            val link = File(binDir, name)
            val target = File(libDir, fileName)
            runCatching {
                when {
                    !target.isFile -> link.delete()
                    // 链接缺失、悬空或仍指向旧插件目录时重建
                    runCatching { Os.readlink(link.path) }.getOrNull() != target.path -> {
                        link.delete()
                        Os.symlink(target.path, link.path)
                    }
                }
            }.onFailure { e ->
                Logging.LOG.log(Level.WARNING, "创建 FFmpeg 命令链接失败: ${link.absolutePath}", e)
            }
        }
        return binDir.absolutePath.takeIf { File(it, PRIMARY_COMMAND).exists() }
    }
}
