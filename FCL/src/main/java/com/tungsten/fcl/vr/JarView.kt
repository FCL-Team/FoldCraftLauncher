package com.tungsten.fcl.vr

import com.tungsten.fclcore.util.io.CompressingUtils
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

/**
 * 只读 jar 视图：基于 ZipFileSystem 的条目访问（统一经 CompressingUtils，兼容混合编码 zip）。
 * 条目读取带字节限额，防止恶意超大描述符。
 */
internal class JarView private constructor(private val fs: FileSystem) : Closeable {
    private val root: Path = fs.rootDirectories.first()

    /** 定位条目；不存在时返回 null */
    fun entry(name: String): Path? = fs.getPath(name).takeIf { Files.exists(it) }

    /**
     * 读取条目文本；条目超过 maxBytes 时返回 null（读前按真实大小与流式累计双重限制）
     */
    @Throws(IOException::class)
    fun readText(entry: Path, maxBytes: Int): String? {
        if (Files.size(entry) > maxBytes) return null
        val output = ByteArrayOutputStream()
        Files.newInputStream(entry).use { input ->
            val buffer = ByteArray(8192)
            while (true) {
                val count = input.read(buffer)
                if (count == -1) break
                if (output.size() + count > maxBytes) return null
                output.write(buffer, 0, count)
            }
        }
        return String(output.toByteArray(), StandardCharsets.UTF_8)
    }

    /** 遍历全部条目名（相对路径，不带前导 /） */
    @Throws(IOException::class)
    inline fun forEachEntry(action: (String) -> Unit) {
        Files.walk(root).use { entries ->
            for (entry in entries) {
                action(entry.toString().removePrefix("/"))
            }
        }
    }

    override fun close() {
        fs.close()
    }

    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun open(file: Path): JarView = JarView(CompressingUtils.createReadOnlyZipFileSystem(file))
    }
}
