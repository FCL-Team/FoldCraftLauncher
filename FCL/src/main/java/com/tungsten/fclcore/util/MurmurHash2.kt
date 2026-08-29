/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
@file:JvmName("MurmurHash2")

package com.tungsten.fclcore.util

import java.nio.charset.StandardCharsets
import java.util.zip.Checksum

// 32 位变体常量
private const val M32 = 0x5bd1e995
private const val R32 = 24

// 64 位变体常量（0xc6a4a7935bd1e995 超出 Long 正数范围，Kotlin 需经 ULong 按位转 Long）
private val M64 = 0xc6a4a7935bd1e995UL.toLong()
private const val R64 = 47

/**
 * MurmurHash2 32 位与 64 位哈希实现。
 *
 * <p>MurmurHash 是一种适用于一般哈希查找的非加密哈希函数，名字来自内层循环的
 * 两个基本操作：乘（MU）与循环移位（R）。它并非为抵抗逆向而设计，不适合加密用途。</p>
 *
 * <p>本文件是对 Austin Appleby 在 SMHasher 中的原始 C++ 代码
 * （32 位 {@code MurmurHash2} 与 64 位 {@code MurmurHash64A}）的移植，
 * 原始代码为公有领域。</p>
 *
 * @see <a href="https://en.wikipedia.org/wiki/MurmurHash">MurmurHash</a>
 * @see <a href="https://github.com/aappleby/smhasher/blob/master/src/MurmurHash2.cpp">
 *   MurmurHash2 原始 C++ 代码</a>
 */

/**
 * 用给定长度和种子，从字节数组生成 32 位哈希。
 */
fun hash32(data: ByteArray, length: Int, seed: Int): Int {
    // 用随机值初始化哈希
    var h = seed xor length

    // 每次混合 4 个字节
    val nblocks = length shr 2

    // 主体
    for (i in 0 until nblocks) {
        val index = i shl 2
        var k = getLittleEndianInt(data, index)
        k *= M32
        k = k xor (k ushr R32)
        k *= M32
        h *= M32
        h = h xor k
    }

    // 处理输入末尾不足 4 字节的部分
    val index = nblocks shl 2
    val remaining = length - index
    if (remaining >= 3) h = h xor ((data[index + 2].toInt() and 0xff) shl 16)
    if (remaining >= 2) h = h xor ((data[index + 1].toInt() and 0xff) shl 8)
    if (remaining >= 1) {
        h = h xor (data[index].toInt() and 0xff)
        h *= M32
    }

    // 末尾再混合几次，确保最后几个字节充分参与
    h = h xor (h ushr 13)
    h *= M32
    h = h xor (h ushr 15)

    return h
}

/**
 * 用默认种子（0x9747b28c）从字节数组生成 32 位哈希，
 * 等价于 `MurmurHash2.hash32(data, length, 0x9747b28c)`。
 */
// 0x9747b28c 超出 Int 正数范围，Kotlin 会推断为 Long，需显式转 Int
fun hash32(data: ByteArray, length: Int): Int = hash32(data, length, 0x9747b28c.toInt())

/**
 * 用默认种子从字符串生成 32 位哈希，字符串按 UTF-8 编码转字节。
 */
fun hash32(text: String): Int {
    val bytes = text.toByteArray(StandardCharsets.UTF_8)
    return hash32(bytes, bytes.size)
}

/**
 * 用默认种子从子字符串生成 32 位哈希。
 */
fun hash32(text: String, from: Int, length: Int): Int = hash32(text.substring(from, from + length))

/**
 * 创建一个针对精确字节数 length 的流式 32 位 MurmurHash2 校验和。
 * 通过 [Checksum.update] 持续喂数据，累计输入恰好等于 length 后调用
 * [Checksum.getValue] 取值；长度不符会抛出 [IllegalStateException]。
 *
 * @throws IllegalArgumentException length 为负时抛出
 */
fun hash32(length: Long, seed: Int): Checksum {
    require(length >= 0) { "Length cannot be negative" }
    return Hash32Checksum(length, seed)
}

/**
 * 用给定长度和种子，从字节数组生成 64 位哈希。
 */
fun hash64(data: ByteArray, length: Int, seed: Int): Long {
    var h = (seed.toLong() and 0xffffffffL) xor (length * M64)

    val nblocks = length shr 3

    // 主体
    for (i in 0 until nblocks) {
        val index = i shl 3
        var k = getLittleEndianLong(data, index)

        k *= M64
        k = k xor (k ushr R64)
        k *= M64

        h = h xor k
        h *= M64
    }

    // 处理输入末尾不足 8 字节的部分
    val index = nblocks shl 3
    val remaining = length - index
    if (remaining >= 7) h = h xor ((data[index + 6].toLong() and 0xffL) shl 48)
    if (remaining >= 6) h = h xor ((data[index + 5].toLong() and 0xffL) shl 40)
    if (remaining >= 5) h = h xor ((data[index + 4].toLong() and 0xffL) shl 32)
    if (remaining >= 4) h = h xor ((data[index + 3].toLong() and 0xffL) shl 24)
    if (remaining >= 3) h = h xor ((data[index + 2].toLong() and 0xffL) shl 16)
    if (remaining >= 2) h = h xor ((data[index + 1].toLong() and 0xffL) shl 8)
    if (remaining >= 1) {
        h = h xor (data[index].toLong() and 0xffL)
        h *= M64
    }

    h = h xor (h ushr R64)
    h *= M64
    h = h xor (h ushr R64)

    return h
}

/**
 * 用默认种子（0xe17a1465）从字节数组生成 64 位哈希，
 * 等价于 `MurmurHash2.hash64(data, length, 0xe17a1465)`。
 */
fun hash64(data: ByteArray, length: Int): Long = hash64(data, length, 0xe17a1465.toInt())

/**
 * 用默认种子从字符串生成 64 位哈希，字符串按 UTF-8 编码转字节。
 */
fun hash64(text: String): Long {
    val bytes = text.toByteArray(StandardCharsets.UTF_8)
    return hash64(bytes, bytes.size)
}

/**
 * 用默认种子从子字符串生成 64 位哈希。
 */
fun hash64(text: String, from: Int, length: Int): Long = hash64(text.substring(from, from + length))

/**
 * 从 data[index] 起读取 4 字节的小端 int。
 */
private fun getLittleEndianInt(data: ByteArray, index: Int): Int =
    (data[index].toInt() and 0xff) or
            ((data[index + 1].toInt() and 0xff) shl 8) or
            ((data[index + 2].toInt() and 0xff) shl 16) or
            ((data[index + 3].toInt() and 0xff) shl 24)

/**
 * 从 data[index] 起读取 8 字节的小端 long。
 */
private fun getLittleEndianLong(data: ByteArray, index: Int): Long =
    (data[index].toLong() and 0xffL) or
            ((data[index + 1].toLong() and 0xffL) shl 8) or
            ((data[index + 2].toLong() and 0xffL) shl 16) or
            ((data[index + 3].toLong() and 0xffL) shl 24) or
            ((data[index + 4].toLong() and 0xffL) shl 32) or
            ((data[index + 5].toLong() and 0xffL) shl 40) or
            ((data[index + 6].toLong() and 0xffL) shl 48) or
            ((data[index + 7].toLong() and 0xffL) shl 56)

/**
 * 针对精确字节长度的流式 32 位 MurmurHash2 校验和。
 * 可变且非线程安全。
 */
private class Hash32Checksum(
    private val expectedLength: Long,
    seed: Int,
) : Checksum {
    // 初始哈希 = seed ^ 预期输入长度（与数组版 h = seed ^ length 一致）
    private val initialHash = seed xor expectedLength.toInt()
    private var hash = initialHash
    private var inputLength = 0L
    private var inputLengthExceeded = false

    // 尾部小端缓存（4 字节，凑满一块即混合，常态持有 0~3 字节）
    private val tail = ByteArray(Int.SIZE_BYTES)
    private var tailLength = 0

    override fun update(b: Int) {
        appendByte(b)
        addInputLength(1)
    }

    override fun update(data: ByteArray, offset: Int, length: Int) {
        addInputLength(length)

        var nblocks = (tailLength + length) / Int.SIZE_BYTES

        var offset2 = offset
        if (tailLength > 0) {
            // 先用新数据补满尾部缓存
            while (tailLength < Int.SIZE_BYTES) {
                if (offset2 >= offset + length) break
                tail[tailLength++] = data[offset2++]
            }

            if (tailLength < Int.SIZE_BYTES) {
                return
            }

            hash = mixBlock(hash, tail, 0)
            nblocks--
        }

        for (block in 0 until nblocks) {
            hash = mixBlock(hash, data, offset2 + block * Int.SIZE_BYTES)
        }

        // 剩余不足 4 字节的部分留作新尾部
        tailLength = offset + length - (offset2 + nblocks * Int.SIZE_BYTES)
        System.arraycopy(data, offset2 + nblocks * Int.SIZE_BYTES, tail, 0, tailLength)
    }

    override fun getValue(): Long {
        check(inputLength == expectedLength && !inputLengthExceeded) {
            "Input length does not match the expected length"
        }

        var h = hash
        if (tailLength >= 3) h = h xor ((tail[2].toInt() and 0xff) shl 16)
        if (tailLength >= 2) h = h xor ((tail[1].toInt() and 0xff) shl 8)
        if (tailLength >= 1) {
            h = h xor (tail[0].toInt() and 0xff)
            h *= M32
        }

        h = h xor (h ushr 13)
        h *= M32
        h = h xor (h ushr 15)
        return h.toLong() and 0xffffffffL
    }

    override fun reset() {
        hash = initialHash
        inputLength = 0
        inputLengthExceeded = false
        tailLength = 0
    }

    private fun addInputLength(length: Int) {
        val newLength = inputLength + length
        if (newLength > expectedLength) {
            inputLengthExceeded = true
        }
        inputLength = newLength
    }

    private fun appendByte(b: Int) {
        tail[tailLength++] = b.toByte()
        if (tailLength == Int.SIZE_BYTES) {
            hash = mixBlock(hash, tail, 0)
            tailLength = 0
        }
    }

    private fun mixBlock(h: Int, data: ByteArray, index: Int): Int {
        var k = getLittleEndianInt(data, index)
        k *= M32
        k = k xor (k ushr R32)
        k *= M32
        val result = h * M32
        return result xor k
    }
}
