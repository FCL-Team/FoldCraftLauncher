package com.tungsten.fcl.ui.version

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mio.cache.VersionCache
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
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        viewModelScope.launch {
            combine(Profiles.selectedProfile, Profiles.versionsRefreshed) { profile, _ -> profile }
                .collect { profile -> if (profile != null) load(profile) }
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
                else update(profile, sortEntries(snapshot), loading = false)
                return@launch
            }
            val ids = withContext(Dispatchers.IO) {
                repository.displayVersions.map { it.id }.collect(Collectors.toList())
            }
            // 会话级快照命中即时渲染，最新数据随后台重算覆盖（VersionCache 共享给弹窗）
            val snapshot = sortEntries(ids.mapNotNull { VersionCache.get(profile)[it] })
            if (snapshot.isNotEmpty()) {
                update(profile, snapshot, loading = false)
            } else {
                update(profile, emptyList(), loading = true)
            }
            val sorted = sortEntries(VersionCache.refresh(profile))
            update(profile, sorted, loading = false)
        }
    }

    private fun update(profile: Profile, entries: List<VersionCache.Entry>, loading: Boolean) {
        val current = _state.value
        if (current.profile === profile && current.loading == loading && sameEntries(current.entries, entries)) return
        _state.value = UiState(profile, entries, loading)
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
