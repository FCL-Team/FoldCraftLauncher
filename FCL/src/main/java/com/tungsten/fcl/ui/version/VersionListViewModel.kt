package com.tungsten.fcl.ui.version

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mio.cache.VersionCache
import com.mio.datastore.VersionPinManager
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.stream.Collectors

/**
 * 已安装版本列表的共享状态（挂 Activity 的 ViewModelStore，单例 key）：
 * 版本列表页与主界面快速切换弹窗 collect 同一份状态。
 *
 * selectedProfile 或版本刷新 tick（[Profiles.versionsRefreshed]）变化时自动重载——
 * 先发缓存快照即时渲染，随后后台全量重算；条目未变化时不再向页面发射，
 * 避免无意义重绘。旧实现的 loadJob 取消/过期守卫/快照比对均由结构化并发承担。
 */
class VersionListViewModel : ViewModel() {

    data class UiState(
        val profile: Profile? = null,
        val entries: List<VersionCache.Entry> = emptyList(),
        /** 无快照可用、正在冷加载（页面据此显示进度条） */
        val loading: Boolean = false,
        /** 当前 Profile 的置顶 id 集合：收藏不改变顺序时也要驱动 item 重绘星标 */
        val pinned: Set<String> = emptySet(),
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        viewModelScope.launch {
            combine(Profiles.selectedProfile, Profiles.versionsRefreshed) { profile, _ -> profile }
                .collect { profile -> if (profile != null) load(profile) }
        }
        // 置顶变化只对当前条目轻量重排，不触发版本仓库重扫
        viewModelScope.launch {
            VersionPinManager.pinned.collect { reapplyPin() }
        }
    }

    /** 刷新按钮通道：失效快照后触发版本仓库重扫，完成事件经 tick 回到 load 走冷加载 */
    fun forceRefresh() {
        val profile = _state.value.profile ?: Profiles.selectedProfile.value ?: return
        VersionCache.invalidate(profile)
        profile.repository.refreshVersionsAsync().start()
    }

    private fun load(profile: Profile) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val repository = profile.repository
            // 冷启动时版本仓库尚未加载（versions map 为 null，读 displayVersions 会 NPE 崩进程）：
            // 先呈现缓存快照/loading 态，仓库自载完成的 RefreshedVersionsEvent 经 versionsRefreshed
            // tick 重入本方法完成真实加载
            if (!repository.isLoaded) {
                val snapshot = VersionCache.get(profile).values.toList()
                if (snapshot.isEmpty()) update(profile, emptyList(), loading = true)
                else update(profile, applyPin(profile, snapshot), loading = false)
                return@launch
            }
            val ids = withContext(Dispatchers.IO) {
                repository.displayVersions.map { it.id }.collect(Collectors.toList())
            }
            // 会话级快照命中即时渲染，最新数据随后台重算覆盖（VersionCache 共享给弹窗）
            val snapshot = applyPin(profile, ids.mapNotNull { VersionCache.get(profile)[it] })
            if (snapshot.isNotEmpty()) {
                update(profile, snapshot, loading = false)
            } else {
                update(profile, emptyList(), loading = true)
            }
            val sorted = applyPin(profile, VersionCache.refresh(profile))
            update(profile, sorted, loading = false)
        }
    }

    /** 置顶表变化时对当前已加载条目重排后重新发射（无数据重载） */
    private fun reapplyPin() {
        val current = _state.value
        val profile = current.profile ?: return
        update(profile, applyPin(profile, current.entries), current.loading)
    }

    /**
     * 置顶版本按置顶顺序提前（已在列表中消失的置顶 id 自动忽略），
     * 其余条目重新按真实版本排序——取消收藏的条目从置顶区回落时不能沿用置顶区位置
     */
    private fun applyPin(profile: Profile, entries: List<VersionCache.Entry>): List<VersionCache.Entry> {
        val pinnedIds = VersionPinManager.getPinned(profile.name)
        if (pinnedIds.isEmpty()) return sortEntries(entries)
        val pinnedSet = pinnedIds.toHashSet()
        val pinned = pinnedIds.mapNotNull { id -> entries.firstOrNull { it.id == id } }
        return pinned + sortEntries(entries.filter { it.id !in pinnedSet })
    }

    private fun update(profile: Profile, entries: List<VersionCache.Entry>, loading: Boolean) {
        val current = _state.value
        val pinned = VersionPinManager.getPinned(profile.name).toSet()
        if (current.profile === profile && current.loading == loading
            && current.pinned == pinned && sameEntries(current.entries, entries)
        ) return
        _state.value = UiState(profile, entries, loading, pinned)
    }

    /** 按真实游戏版本从大到小排序（无法识别的版本排最后），同版本时按 id 倒序保持稳定 */
    private fun sortEntries(entries: List<VersionCache.Entry>): List<VersionCache.Entry> {
        return entries.sortedWith(
            compareByDescending<VersionCache.Entry> { it.gameVersion }
                .thenByDescending { it.id }
        )
    }

    /** 条目级比对：id/libraries/tag/modCount/iconKey 全部一致视为未变化 */
    private fun sameEntries(a: List<VersionCache.Entry>, b: List<VersionCache.Entry>): Boolean {
        if (a.size != b.size) return false
        return a.zip(b).all { (x, y) ->
            x.id == y.id && x.libraries == y.libraries && x.tag == y.tag
                    && x.modCount == y.modCount && x.iconKey == y.iconKey
        }
    }
}
