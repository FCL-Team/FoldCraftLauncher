package com.mio.util

import net.fornwall.jelf.ElfFile
import java.io.DataInputStream
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.ZipFile

class AndroidUtil {
    companion object {
        fun getElfArchFromSo(filePath: String): String {
            RandomAccessFile(filePath, "r").use { file ->
                val magic = ByteArray(4)
                file.readFully(magic)
                if (magic[0] != 0x7F.toByte() || magic[1] != 'E'.code.toByte()
                    || magic[2] != 'L'.code.toByte() || magic[3] != 'F'.code.toByte()
                ) {
                    return ""
                }
                file.seek(0x05)
                val eiData = file.readByte().toInt() and 0xFF
                if (eiData !in listOf(1, 2)) {
                    return ""
                }
                file.seek(0x12)
                val eMachineBytes = ByteArray(2)
                file.readFully(eMachineBytes)
                val buffer = ByteBuffer.wrap(eMachineBytes).apply {
                    order(if (eiData == 1) ByteOrder.LITTLE_ENDIAN else ByteOrder.BIG_ENDIAN)
                }
                val eMachine = buffer.short.toInt() and 0xFFFF
                return when (eMachine) {
                    0x03 -> "x86"
                    0x3E -> "x86_64"
                    0x28 -> "ARM"
                    0xB7 -> "AArch64"
                    0x08 -> "MIPS"
                    0xF3 -> "RISC-V"
                    0x2A -> "SuperH"
                    0x32 -> "IA-64"
                    else -> ""
                }
            }
        }

        fun checkElfIsAndroid(file: File): Boolean {
            val elfFile = ElfFile.from(file)
            var isAndroid = true
            elfFile.dynamicSection.neededLibraries.forEach {
                if (Regex("lib[^.]+\\.so\\.\\d+").matches(it)) {
                    isAndroid = false
                }
            }
            return isAndroid
        }

        fun getElfArchFromZip(zipFile: File, elfEntryPath: String): String {
            var arch = ""
            try {
                ZipFile(zipFile).let { zip ->
                    val entry = zip.entries().toList().find { it.name == elfEntryPath }
                    if (entry == null || entry.isDirectory) {
                        return@let
                    }
                    zip.getInputStream(entry).let { stream ->
                        DataInputStream(stream).let { dataStream ->
                            val magic = ByteArray(4)
                            dataStream.readFully(magic)
                            if (!(magic[0] == 0x7F.toByte() && magic[1] == 'E'.code.toByte() &&
                                        magic[2] == 'L'.code.toByte() && magic[3] == 'F'.code.toByte())
                            ) {
                                return@let
                            }
                            val eIdentRest = ByteArray(12)
                            dataStream.readFully(eIdentRest)
                            val eiData = eIdentRest[1].toInt()
                            dataStream.skipBytes(2)
                            val machineBytes = ByteArray(2)
                            dataStream.readFully(machineBytes)
                            val byteOrder = when (eiData) {
                                1 -> ByteOrder.LITTLE_ENDIAN
                                2 -> ByteOrder.BIG_ENDIAN
                                else -> ByteOrder.LITTLE_ENDIAN
                            }
                            val machineType = ByteBuffer.wrap(machineBytes)
                                .order(byteOrder)
                                .short.toInt() and 0xFFFF
                            arch = when (machineType) {
                                0x03 -> "x86"
                                0x3E -> "x86_64"
                                0x28 -> "ARM"
                                0xB7 -> "AArch64"
                                0x08 -> "MIPS"
                                0xF3 -> "RISC-V"
                                0x2A -> "SPARC"
                                0x18 -> "ARM64"
                                else -> ""
                            }
                        }
                    }
                }
            } catch (_: Exception) {
            }
            return arch
        }
    }
}