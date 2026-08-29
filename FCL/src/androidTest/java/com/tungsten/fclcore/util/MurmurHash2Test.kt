package com.tungsten.fclcore.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Random

/**
 * MurmurHash2 流式校验和 hash32(length, seed) 与数组版实现的等价性验证，
 * 以及精确长度校验和 reset 行为。
 */
@RunWith(AndroidJUnit4::class)
class MurmurHash2Test {

    private fun randomBytes(random: Random, size: Int): ByteArray =
        ByteArray(size).also { random.nextBytes(it) }

    /**
     * 按给定切块大小依次 update，剩余部分用单字节 update 喂完。
     */
    private fun streamingHash(data: ByteArray, seed: Int, chunkSizes: List<Int>): Long {
        val checksum = hash32(data.size.toLong(), seed)
        var offset = 0
        for (chunk in chunkSizes) {
            val len = minOf(chunk, data.size - offset)
            if (len <= 0) break
            checksum.update(data, offset, len)
            offset += len
        }
        for (i in offset until data.size) {
            checksum.update(data[i].toInt())
        }
        return checksum.getValue()
    }

    @Test
    fun streamingMatchesArrayImplementation() {
        val random = Random(0x9747b28c)
        // 覆盖 0 与全部尾部余数（1~3）、块边界（1023/1024/1025）、1MB 缓冲边界
        val sizes = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 15, 16, 17, 63, 64, 65, 255,
                1023, 1024, 1025, 4096, (1 shl 20) + 3)
        for (size in sizes) {
            val data = randomBytes(random, size)
            for (seed in listOf(1, 42, 0x9747b28c.toInt())) {
                val expected = hash32(data, size, seed).toLong() and 0xffffffffL
                assertEquals("size=$size seed=$seed single", expected,
                        streamingHash(data, seed, listOf(Int.MAX_VALUE)))
                assertEquals("size=$size seed=$seed bytewise", expected,
                        streamingHash(data, seed, listOf(1)))
                assertEquals("size=$size seed=$seed chunks", expected,
                        streamingHash(data, seed, List(8) { 1 + random.nextInt(300) }))
            }
        }
    }

    @Test
    fun lengthMismatchThrows() {
        val data = ByteArray(10)

        // 少喂 1 字节
        val underfed = hash32(10L, 1)
        underfed.update(data, 0, 9)
        assertThrows(IllegalStateException::class.java) { underfed.getValue() }

        // 超长输入
        val overfed = hash32(10L, 1)
        overfed.update(data, 0, 10)
        overfed.update(data, 0, 10)
        assertThrows(IllegalStateException::class.java) { overfed.getValue() }

        // 负长度
        assertThrows(IllegalArgumentException::class.java) { hash32(-1L, 1) }
    }

    @Test
    fun resetRestoresState() {
        val random = Random(42)
        val data = randomBytes(random, 37)
        val expected = hash32(data, data.size, 7).toLong() and 0xffffffffL

        val checksum = hash32(data.size.toLong(), 7)
        checksum.update(randomBytes(random, data.size), 0, data.size)
        checksum.reset()
        checksum.update(data, 0, data.size)
        assertEquals(expected, checksum.getValue())
    }

    @Test
    fun knownVectorsFromOriginalImplementation() {
        // 基准向量由重构前的原始 Java 实现生成，并经独立 Python 实现交叉确认
        assertEquals(0x7f1ddbbdL, hash32("hello").toLong() and 0xffffffffL)
        assertEquals(0xa631918eL, hash32("hello".toByteArray(Charsets.UTF_8), 5, 1).toLong() and 0xffffffffL)
        assertEquals(0x5bd15e36L, hash32(ByteArray(0), 0, 1).toLong() and 0xffffffffL)

        val seq = ByteArray(64) { it.toByte() }
        assertEquals(0x5e0d1e9bL, hash32(seq, 64, 1).toLong() and 0xffffffffL)
        assertEquals(0x95b4befcL, hash32(seq + ByteArray(1), 65, 1).toLong() and 0xffffffffL)

        assertEquals(0xc656272f1aa32ab5UL.toLong(), hash64("hello"))

        // 333 字节已知随机数据，流式版结果须一致
        val rand = hexToBytes(RAND333_HEX)
        val checksum = hash32(rand.size.toLong(), 1)
        checksum.update(rand, 0, rand.size)
        assertEquals(0x7bc8f64fL, checksum.getValue())
    }

    private fun hexToBytes(hex: String): ByteArray =
        ByteArray(hex.length / 2) { i ->
            ((Character.digit(hex[i * 2], 16) shl 4) + Character.digit(hex[i * 2 + 1], 16)).toByte()
        }

    private companion object {
        // Random(42).nextBytes(333) 的十六进制
        private val RAND333_HEX = buildString {
            val bytes = ByteArray(333)
            Random(42).nextBytes(bytes)
            for (b in bytes) append(String.format("%02x", b))
        }
    }
}
