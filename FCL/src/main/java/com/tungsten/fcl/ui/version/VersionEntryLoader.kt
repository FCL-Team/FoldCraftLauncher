package com.tungsten.fcl.ui.version

import android.content.Context
import com.google.gson.JsonParseException
import com.mio.cache.VersionCache
import com.mio.util.getLocalizedText
import com.mio.util.hasStringId
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.game.Version
import com.tungsten.fclcore.mod.ModpackConfiguration
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.file.Files
import java.util.logging.Level
import java.util.stream.Collectors
import kotlin.io.path.isRegularFile

/**
 * 在 IO 线程并行计算各版本的派生数据（组件摘要、整合包标签、图标、Mod 数、真实游戏版本）。
 * 供版本列表页与快速切换弹窗复用。
 */
suspend fun computeVersionEntries(context: Context, profile: Profile): List<VersionCache.Entry> =
    withContext(Dispatchers.IO) {
        val repository = profile.repository
        repository.displayVersions
            .parallel()
            .map { version: Version ->
                ensureActive()
                val game = profile.repository.getGameVersion(version.id)
                // 一次解析，analyzer 与图标判断复用（getVersionIconImage 不再重复 resolve）
                val resolved =
                    profile.repository.getResolvedPreservingPatchesVersion(version.id)
                val libraries =
                    StringBuilder(game.orElse(context.getString(R.string.message_unknown)))
                val analyzer = LibraryAnalyzer.analyze(resolved, game.orElse(null))
                for (mark in analyzer) {
                    ensureActive()
                    val libraryId = mark.libraryId
                    val libraryVersion = mark.libraryVersion
                    if (libraryId == LibraryAnalyzer.LibraryType.MINECRAFT.patchId) continue
                    if (hasStringId(
                            context,
                            "install_installer_" + libraryId.replace("-", "_")
                        )
                    ) {
                        libraries.append(", ").append(
                            getLocalizedText(
                                context,
                                "install_installer_" + libraryId.replace("-", "_")
                            )
                        )
                        if (libraryVersion != null) libraries.append(": ").append(
                            libraryVersion.replace(
                                ("(?i)$libraryId").toRegex(),
                                ""
                            )
                        )
                    }
                }
                var tag: String? = null
                try {
                    val config: ModpackConfiguration<*>? =
                        profile.repository.readModpackConfiguration<Any?>(
                            version.id
                        )
                    if (config != null) tag = config.version
                } catch (e: IOException) {
                    Logging.LOG.log(
                        Level.WARNING,
                        "Failed to read modpack configuration from $version",
                        e
                    )
                } catch (e: JsonParseException) {
                    Logging.LOG.log(
                        Level.WARNING,
                        "Failed to read modpack configuration from $version",
                        e
                    )
                }
                val icon = repository.getVersionIconImage(analyzer, version.id)
                val iconKey = repository.getVersionIconFile(version.id).lastModified()
                // Mod 数统计在 IO 线程并行流里完成（避免滑动时主线程目录 IO）；
                // use 关闭 DirectoryStream，否则文件描述符泄漏（CloseGuard 报资源未关闭）
                val modCount = runCatching {
                    Files.list(repository.getModsDirectory(version.id)).use { stream ->
                        stream.filter { it.isRegularFile() }.count().toInt()
                    }
                }.getOrNull() ?: 0
                VersionCache.Entry(
                    version.id,
                    GameVersionNumber.asGameVersion(game),
                    libraries.toString(),
                    tag,
                    icon.constantState!!,
                    modCount,
                    iconKey
                )
            }
            .collect(Collectors.toList())
    }

/**
 * 按真实游戏版本从大到小排序（GameVersionNumber 比较，无法识别的版本排在最后），
 * 同版本时按 id 倒序保持稳定。
 */
fun sortVersionEntries(entries: List<VersionCache.Entry>): List<VersionCache.Entry> {
    return entries.sortedWith(
        compareByDescending<VersionCache.Entry> { it.gameVersion }
            .thenByDescending { it.id }
    )
}
