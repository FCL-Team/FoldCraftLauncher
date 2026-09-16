package com.mio.cache

import android.graphics.drawable.Drawable
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import java.util.concurrent.ConcurrentHashMap

/**
 * 版本列表的会话级快照缓存：按 Profile 实例缓存各版本的派生数据
 * （组件摘要、整合包标签、图标、Mod 数、真实游戏版本），
 * 供版本列表页打开时即时渲染，后台刷新完成后覆盖更新。
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
}
