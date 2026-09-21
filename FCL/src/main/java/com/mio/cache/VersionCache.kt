package com.mio.cache

import android.graphics.drawable.Drawable
import com.google.gson.JsonParseException
import com.mio.util.getLocalizedText
import com.mio.util.hasStringId
import com.tungsten.fcl.FCLApp
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.mod.ModpackConfiguration
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.file.Files
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level
import java.util.stream.Collectors
import kotlin.io.path.isRegularFile

/**
 * 版本列表的会话级快照缓存：按 Profile 实例缓存各版本的派生数据
 * （组件摘要、整合包标签、图标、Mod 数、真实游戏版本），
 * 供版本列表页与主界面快速切换弹窗共享，加载完成后即时渲染。
 */
object VersionCache {

    /**
     * 单个版本的派生数据快照
     */
    class Entry(
        val id: String,
        val gameVersion: GameVersionNumber,
        val libraries: String,
        val tag: String?,
        private val iconState: Drawable.ConstantState,
        val modCount: Int,
        /** 自定义图标文件的 lastModified（0 表示无自定义图标），用于识别图标文件变化 */
        val iconKey: Long,
    ) {
        /**
         * 每次派生新的 Drawable 实例，避免共享实例的 bounds 被列表 item 修改后互相污染
         */
        fun newIcon(): Drawable = iconState.newDrawable()
    }

    private val snapshots = ConcurrentHashMap<Profile, Map<String, Entry>>()

    fun get(profile: Profile): Map<String, Entry> {
        return snapshots[profile] ?: emptyMap()
    }

    fun put(profile: Profile, entries: List<Entry>) {
        snapshots[profile] = entries.associateBy { it.id }
    }

    /** 失效指定 Profile 的快照，下次加载走冷加载 */
    fun invalidate(profile: Profile) {
        snapshots.remove(profile)
    }

    /**
     * 计算全部版本的派生数据并写入快照，返回计算结果。
     * 版本列表页刷新与快速切换弹窗预热共用此入口，共享同一份快照。
     */
    suspend fun refresh(profile: Profile): List<Entry> = withContext(Dispatchers.IO) {
        val context = FCLApp.getAppContext()
        val repository = profile.repository
        val entries = repository.displayVersions
            .parallel()
            .map { version ->
                ensureActive()
                val game = repository.getGameVersion(version.id)
                // 一次解析，analyzer 与图标判断复用（getVersionIconImage 不再重复 resolve）
                val resolved = repository.getResolvedPreservingPatchesVersion(version.id)
                val libraries =
                    StringBuilder(game.orElse(context.getString(R.string.message_unknown)))
                val analyzer = LibraryAnalyzer.analyze(resolved, game.orElse(null))
                for (mark in analyzer) {
                    ensureActive()
                    val libraryId = mark.libraryId
                    val libraryVersion = mark.libraryVersion
                    if (libraryId == LibraryAnalyzer.LibraryType.MINECRAFT.patchId) continue
                    if (hasStringId(context, "install_installer_" + libraryId.replace("-", "_"))) {
                        libraries.append(", ").append(
                            getLocalizedText(context, "install_installer_" + libraryId.replace("-", "_"))
                        )
                        if (libraryVersion != null) libraries.append(": ").append(
                            libraryVersion.replace(("(?i)$libraryId").toRegex(), "")
                        )
                    }
                }
                var tag: String? = null
                try {
                    val config: ModpackConfiguration<*>? =
                        repository.readModpackConfiguration<Any?>(version.id)
                    if (config != null) tag = config.version
                } catch (e: IOException) {
                    Logging.LOG.log(Level.WARNING, "Failed to read modpack configuration from $version", e)
                } catch (e: JsonParseException) {
                    Logging.LOG.log(Level.WARNING, "Failed to read modpack configuration from $version", e)
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
                Entry(
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
        put(profile, entries)
        entries
    }
}
