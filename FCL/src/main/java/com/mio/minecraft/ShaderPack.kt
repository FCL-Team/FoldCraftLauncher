package com.mio.minecraft

import java.io.File
import java.util.zip.ZipFile

/**
 * 光影包信息（zip 文件或文件夹形式）
 */
data class ShaderPack(
    val file: File,
    val isDirectory: Boolean,
    /** 显示名称，zip 形式去掉扩展名 */
    val name: String,
    /** zip 或文件夹内是否含 shaders/ 目录 */
    val isValid: Boolean,
    /** 文件大小，文件夹形式不计算 */
    val fileSize: Long?,
)

/**
 * 解析目录下全部光影包（zip 与文件夹），按名称排序
 */
fun listShaderPacks(dir: File): List<ShaderPack> {
    val entries = dir.listFiles() ?: return emptyList()
    return entries.mapNotNull(::parseShaderPack)
        .sortedBy { it.name.lowercase() }
}

/**
 * 解析单个光影包，仅接受文件夹与 zip 文件，有效性由 shaders/ 目录是否存在决定
 */
fun parseShaderPack(file: File): ShaderPack? {
    val isDirectory = file.isDirectory
    if (!isDirectory && file.extension.lowercase() != "zip") return null

    val valid = try {
        if (isDirectory) {
            File(file, "shaders").isDirectory
        } else {
            ZipFile(file).use { zip ->
                zip.entries().asSequence().any { it.name.startsWith("shaders/") }
            }
        }
    } catch (_: Exception) {
        // 损坏的 zip 或不可读的文件，按无效光影包处理
        false
    }

    return ShaderPack(
        file = file,
        isDirectory = isDirectory,
        name = if (isDirectory) file.name else file.nameWithoutExtension,
        isValid = valid,
        fileSize = if (isDirectory) null else file.length(),
    )
}
