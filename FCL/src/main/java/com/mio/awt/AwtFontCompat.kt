package com.mio.awt

import android.system.Os
import com.tungsten.fclcore.util.Logging
import java.io.File
import java.util.logging.Level

/**
 * 为 JRE 安装 AWT 字体回退：把系统 CJK 字体挂进 lib/fonts/fallback 目录，
 * OpenJDK 启动字体系统时会把该目录注册为复合字体的兜底，
 * 使 AWT/Swing 文本缺失字形（中文等）时能落到系统字体渲染。
 */
object AwtFontCompat {

    private const val FALLBACK_FONT_NAME = "NotoSansCJK-Regular.ttc"

    private val candidates = listOf(
        "/system/fonts/NotoSansCJK-Regular.ttc",
        "/system/fonts/NotoSerifCJK-Regular.ttc",
        "/system/fonts/NotoSansSC-Regular.otf",
        "/system/fonts/DroidSansFallback.ttf"
    )

    /**
     * 确保给定的 fallback 目录中存在系统字体，幂等；JRE 重装后需重调
     */
    @JvmStatic
    fun ensureFallbackFont(fallbackDirPath: String) {
        try {
            val fallbackDir = File(fallbackDirPath)
            if (File(fallbackDir, FALLBACK_FONT_NAME).isFile) {
                return
            }
            val source = candidates.firstOrNull { File(it).isFile }
            if (source == null) {
                Logging.LOG.warning("No system CJK font found for AWT fallback")
                return
            }
            fallbackDir.mkdirs()
            val target = File(fallbackDir, FALLBACK_FONT_NAME)
            try {
                Os.symlink(source, target.absolutePath)
            } catch (e: Exception) {
                // 符号链接创建失败时退回复制
                target.delete()
                File(source).copyTo(target, overwrite = true)
            }
        } catch (e: Throwable) {
            Logging.LOG.log(Level.WARNING, "Install AWT fallback font failed: $fallbackDirPath", e)
        }
    }
}
