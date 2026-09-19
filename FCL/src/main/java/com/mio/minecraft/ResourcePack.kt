package com.mio.minecraft

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tungsten.fcl.setting.GameOption
import com.tungsten.fclcore.mod.modinfo.PackMcMeta
import com.tungsten.fclcore.util.gson.JsonUtils
import java.io.File
import java.util.zip.ZipFile

/**
 * 资源包信息（zip 文件或文件夹形式）
 */
data class ResourcePack(
    val file: File,
    val isDirectory: Boolean,
    /** 显示名称，zip 形式去掉扩展名 */
    val name: String,
    /** pack.mcmeta 的描述文本 */
    val description: String?,
    /** 格式版本范围描述，如 "34" 或 "15~60" */
    val packFormat: String?,
    /** pack.png 图标 */
    val icon: Bitmap?,
    /** pack.mcmeta 是否解析成功 */
    val isValid: Boolean,
    /** 文件大小，文件夹形式不计算 */
    val fileSize: Long?,
    /** 是否已写入 options.txt 的 resourcePacks 列表 */
    val enabled: Boolean,
)

/**
 * 解析目录下全部资源包（zip 与文件夹），按名称排序
 */
fun listResourcePacks(dir: File, enabledEntries: Set<String> = emptySet()): List<ResourcePack> {
    val entries = dir.listFiles() ?: return emptyList()
    return entries.mapNotNull { parseResourcePack(it, enabledEntries) }
        .sortedBy { it.name.lowercase() }
}

/** 资源包在 options.txt 中的条目名，1.7.2 引入资源包系统起即为该格式 */
private fun resourcePackEntry(fileName: String) = "file/$fileName"

private fun isPackEnabled(fileName: String, entries: Set<String>): Boolean =
    resourcePackEntry(fileName) in entries

/**
 * 解析单个资源包，仅接受文件夹与 zip 文件，有效性由 pack.mcmeta 是否解析成功决定
 */
fun parseResourcePack(file: File, enabledEntries: Set<String> = emptySet()): ResourcePack? {
    val isDirectory = file.isDirectory
    if (!isDirectory && file.extension.lowercase() != "zip") return null

    var metaContent: String? = null
    var iconBytes: ByteArray? = null
    try {
        if (isDirectory) {
            File(file, "pack.mcmeta").takeIf { it.isFile }?.let { metaContent = it.readText() }
            File(file, "pack.png").takeIf { it.isFile }?.let { iconBytes = it.readBytes() }
        } else {
            ZipFile(file).use { zip ->
                zip.getEntry("pack.mcmeta")?.let { metaContent = zip.getInputStream(it).readBytes().decodeToString() }
                zip.getEntry("pack.png")?.let { iconBytes = zip.getInputStream(it).readBytes() }
            }
        }
    } catch (_: Exception) {
        // 损坏的 zip 或不可读的文件，按无效资源包处理
    }

    val meta = metaContent?.let { content ->
        runCatching {
            content.byteInputStream().use { JsonUtils.fromNonNullJsonFully(it, PackMcMeta::class.java) }
        }.getOrNull()
    }
    val format = meta?.pack?.let { info ->
        val min = info.getEffectiveMinVersion()
        val max = info.getEffectiveMaxVersion()
        if (min == max) min.toString() else "${min}~$max"
    }

    return ResourcePack(
        file = file,
        isDirectory = isDirectory,
        name = if (isDirectory) file.name else file.nameWithoutExtension,
        description = meta?.pack?.description?.toString(),
        packFormat = format,
        icon = iconBytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) },
        isValid = meta != null,
        fileSize = if (isDirectory) null else file.length(),
        enabled = isPackEnabled(file.name, enabledEntries),
    )
}

/** 从 GameOption 读取字符串数组键，缺失或格式非法时返回空列表 */
private fun readStringList(option: GameOption, key: String): MutableList<String> {
    return option.get(key)?.let {
        runCatching { JsonUtils.UGLY_GSON.fromJson(it, Array<String>::class.java).toMutableList() }.getOrNull()
    } ?: mutableListOf()
}

/**
 * 读取 options.txt 的 resourcePacks 列表（游戏内已启用的资源包条目），文件缺失或格式非法时返回空集
 */
fun readEnabledResourcePacks(gameDir: File): Set<String> {
    val raw = GameOption(gameDir.path).get("resourcePacks") ?: return emptySet()
    return runCatching {
        JsonUtils.UGLY_GSON.fromJson(raw, Array<String>::class.java).toSet()
    }.getOrDefault(emptySet())
}

/**
 * 更新单个资源包在 options.txt 中的启用状态。
 * 除 resourcePacks 外还须同步维护 incompatibleResourcePacks（"用户已确认不兼容"名单）：
 * 格式版本不符的资源包若不在该名单，游戏启动时会被移出 resourcePacks 而不生效；
 * 等同于游戏内选择不兼容资源包时点"仍然使用"。
 */
fun setResourcePackEnabled(gameDir: File, fileName: String, enabled: Boolean): Boolean {
    return try {
        val option = GameOption(gameDir.path)
        val entry = resourcePackEntry(fileName)
        val packs = readStringList(option, "resourcePacks")
        val incompatible = readStringList(option, "incompatibleResourcePacks")

        if (enabled) {
            if (entry in packs && entry in incompatible) return true
            if (entry !in packs) packs.add(entry)
            if (entry !in incompatible) incompatible.add(entry)
        } else {
            if (entry !in packs && entry !in incompatible) return true
            packs.remove(entry)
            incompatible.remove(entry)
        }
        // GameOption.set(key, List) 走 List.toString() 不带引号，且 options.txt 逐行解析，
        // 必须写单行 JSON 数组（如 ["file/a.zip","vanilla"]）
        option.set("resourcePacks", JsonUtils.UGLY_GSON.toJson(packs))
        option.set("incompatibleResourcePacks", JsonUtils.UGLY_GSON.toJson(incompatible))
        option.save()
        true
    } catch (_: Exception) {
        false
    }
}
