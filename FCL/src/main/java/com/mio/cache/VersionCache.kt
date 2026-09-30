package com.mio.cache

import android.content.Context
import android.graphics.drawable.Drawable
import com.google.gson.JsonParseException
import com.mio.util.getLocalizedText
import com.mio.util.hasStringId
import com.tungsten.fcl.FCLApp
import com.tungsten.fcl.R
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.mod.ModpackConfiguration
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
 *
 * 并发防护：同一 Profile 的 refresh 经 Mutex 合并（列表页/弹窗/预热同时触发只算一次）；
 * 来源未变化的版本按指纹（json/图标/mods 目录 mtime）直接复用旧条目，
 * LibraryAnalyzer 全量解析只对来源变化的版本执行。
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
        /** 版本 json 与 modpack.json 的 lastModified 较大者，识别解析类派生数据来源变化 */
        val jsonKey: Long,
        /** mods 目录的 lastModified，识别 Mod 数变化 */
        val modsKey: Long,
    ) {
        /**
         * 每次派生新的 Drawable 实例，避免共享实例的 bounds 被列表 item 修改后互相污染
         */
        fun newIcon(): Drawable = iconState.newDrawable()
    }

    private val snapshots = ConcurrentHashMap<Profile, Map<String, Entry>>()
    private val locks = ConcurrentHashMap<Profile, Mutex>()

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
        val callScope = this
        locks.computeIfAbsent(profile) { Mutex() }.withLock {
            val context = FCLApp.getAppContext()
            val repository = profile.repository
            val ids = repository.displayVersions.map { it.id }.collect(Collectors.toList())
            val old = snapshots[profile].orEmpty()

            // 指纹扫描（纯 stat 调用，廉价）：来源未变的条目直接复用
            val fingerprints = ids.associateWith { id ->
                longArrayOf(
                    maxOf(
                        repository.getVersionJson(id).lastModified(),
                        repository.getModpackConfiguration(id).lastModified()
                    ),
                    repository.getVersionIconFile(id).lastModified(),
                    repository.getModsDirectory(id).toFile().lastModified()
                )
            }
            val reusable = ids.mapNotNull { id ->
                val entry = old[id] ?: return@mapNotNull null
                val key = fingerprints[id]!!
                if (entry.jsonKey == key[0] && entry.iconKey == key[1] && entry.modsKey == key[2]) entry else null
            }.associateBy { it.id }

            val computed = ids.filter { it !in reusable }
                .parallelStream()
                .map { id ->
                    val key = fingerprints[id]!!
                    computeEntry(callScope, repository, context, id, key[0], key[1], key[2])
                }
                .collect(Collectors.toMap({ it.id }, { it }))

            val entries = ids.map { reusable[it] ?: computed[it]!! }
            put(profile, entries)
            entries
        }
    }

    private fun computeEntry(
        scope: CoroutineScope,
        repository: FCLGameRepository,
        context: Context,
        id: String,
        jsonKey: Long,
        iconKey: Long,
        modsKey: Long,
    ): Entry {
        scope.ensureActive()
        val game = repository.getGameVersion(id)
        // 一次解析，analyzer 与图标判断复用（getVersionIconImage 不再重复 resolve）
        val resolved = repository.getResolvedPreservingPatchesVersion(id)
        val libraries =
            StringBuilder(game.orElse(context.getString(R.string.message_unknown)))
        val analyzer = LibraryAnalyzer.analyze(resolved, game.orElse(null))
        for (mark in analyzer) {
            scope.ensureActive()
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
                repository.readModpackConfiguration<Any?>(id)
            if (config != null) tag = config.version
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, "Failed to read modpack configuration from $id", e)
        } catch (e: JsonParseException) {
            Logging.LOG.log(Level.WARNING, "Failed to read modpack configuration from $id", e)
        }
        val icon = repository.getVersionIconImage(analyzer, id)
        // Mod 数统计在 IO 线程并行流里完成（避免滑动时主线程目录 IO）；
        // use 关闭 DirectoryStream，否则文件描述符泄漏（CloseGuard 报资源未关闭）
        val modCount = runCatching {
            Files.list(repository.getModsDirectory(id)).use { stream ->
                stream.filter { it.isRegularFile() }.count().toInt()
            }
        }.getOrNull() ?: 0
        scope.ensureActive()
        return Entry(
            id,
            GameVersionNumber.asGameVersion(game),
            libraries.toString(),
            tag,
            icon.constantState!!,
            modCount,
            iconKey,
            jsonKey,
            modsKey,
        )
    }
}
