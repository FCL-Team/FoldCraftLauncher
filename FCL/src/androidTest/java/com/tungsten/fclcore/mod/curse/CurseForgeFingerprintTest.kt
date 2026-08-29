package com.tungsten.fclcore.mod.curse

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tungsten.fclcore.util.hash32
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Random

/**
 * calculateFingerprint（流式两遍扫描）与「手动剔除空白符 + 数组版 hash32」的一致性验证。
 */
@RunWith(AndroidJUnit4::class)
class CurseForgeFingerprintTest {

    /**
     * 参考实现：读入整个文件，剔除空白符后用数组版 hash32 计算。
     */
    private fun expectedFingerprint(file: File): Long {
        val filtered = file.inputStream().use { input ->
            val baos = ByteArrayOutputStream()
            val buf = ByteArray(4096)
            var len = input.read(buf)
            while (len != -1) {
                for (i in 0 until len) {
                    val b = buf[i]
                    if (b != 0x09.toByte() && b != 0x0a.toByte() && b != 0x0d.toByte() && b != 0x20.toByte()) {
                        baos.write(b.toInt())
                    }
                }
                len = input.read(buf)
            }
            baos.toByteArray()
        }
        return hash32(filtered, filtered.size, 1).toLong() and 0xffffffffL
    }

    @Test
    fun fingerprintMatchesFilteredHash() {
        val dir = InstrumentationRegistry.getInstrumentation().targetContext.cacheDir
        val random = Random(1234)

        // 覆盖空文件、尾部余数、1MB 缓冲边界（跨缓冲的空白符压缩路径）
        val sizes = listOf(0, 1, 3, 4, 5, 63, 64, 65, 1023, 1024, 1025, 4096, 1024 * 1024 + 3)
        for (size in sizes) {
            val data = ByteArray(size).also { random.nextBytes(it) }
            // 每隔 3 字节插入一个随机空白符
            for (i in data.indices step 3) {
                data[i] = WHITESPACE[random.nextInt(WHITESPACE.size)]
            }

            val file = File.createTempFile("fingerprint", ".tmp", dir)
            file.writeBytes(data)
            try {
                assertEquals("size=$size", expectedFingerprint(file),
                        CurseForgeRemoteModRepository.calculateFingerprint(file.toPath()))
            } finally {
                file.delete()
            }
        }
    }

    @Test
    fun whitespaceOnlyFile() {
        val dir = InstrumentationRegistry.getInstrumentation().targetContext.cacheDir
        val file = File.createTempFile("fingerprint", ".tmp", dir)
        file.writeBytes(ByteArray(100) { 0x20 })
        try {
            val expected = hash32(ByteArray(0), 0, 1).toLong() and 0xffffffffL
            assertEquals(expected, CurseForgeRemoteModRepository.calculateFingerprint(file.toPath()))
        } finally {
            file.delete()
        }
    }

    companion object {
        private val WHITESPACE = byteArrayOf(0x09, 0x0a, 0x0d, 0x20)
    }
}
