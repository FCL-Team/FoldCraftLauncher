package com.tungsten.fcl.ui.download.version

import androidx.lifecycle.ViewModel
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fclcore.download.ComponentRemoteVersion
import com.tungsten.fclcore.download.ComponentVersionList
import com.tungsten.fclcore.game.GameComponentType
import com.tungsten.fclcore.task.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 游戏版本/安装组件的远端清单加载状态机：挂 Activity 的 ViewModelStore（按 key 区分组件），
 * 页面（随 ViewPager 回收/临时页弹栈销毁）重建时直接恢复现有状态，不重新发起网络请求。
 *
 * 清单数据本身由 DownloadProviders 返回的 [ComponentVersionList] 单例持有，
 * isLoaded 命中时 load 直接回填不发网络；Task 框架无 in-flight 合并，Loading 期间的重入在此挡下。
 */
class DownloadListViewModel : ViewModel() {

    sealed interface State {
        data object Idle : State
        data object Loading : State

        data class Loaded(val versions: List<ComponentRemoteVersion>) : State

        /** 加载失败：lastList 为失败前已加载的数据，页面据此保留旧列表、仅显示重试入口 */
        data class Failed(val error: Throwable, val lastList: List<ComponentRemoteVersion>) : State
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    private var lastLoaded: List<ComponentRemoteVersion> = emptyList()

    /** 在途/最近一次清单请求对应的游戏版本：回调落地时据此丢弃过期结果 */
    private var requestedGameVersion: String? = null

    fun load(gameVersion: String, componentType: GameComponentType, force: Boolean) {
        val versionList: ComponentVersionList<*> =
            DownloadProviders.getDownloadProvider().getVersionList(componentType)
        // 游戏清单是全量列表（getVersionsImpl 忽略 gameVersion 参数），isLoaded 必须用无参形式
        val loaded =
            if (componentType == GameComponentType.GAME) versionList.isLoaded() else versionList.isLoaded(gameVersion)
        if (!force && loaded) {
            requestedGameVersion = gameVersion
            updateLoaded(items(versionList, gameVersion))
            return
        }
        // 同一版本的清单拉取已在途：等回调落地，不重入。
        // 不同版本的请求不能复用在途结果（VM 按组件 key 跨页面共享，gameVersion 随页面变化）
        if (_state.value is State.Loading && requestedGameVersion == gameVersion) return
        requestedGameVersion = gameVersion
        _state.value = State.Loading
        versionList.refreshAsync(gameVersion)
            .whenComplete(Schedulers.androidUIThread()) { _, error ->
                // 请求已被更新 gameVersion 的加载取代：结果过期，丢弃
                // （否则旧版本的清单会作为 Loaded 写进状态，新页面渲染出错误版本的组件列表）
                if (gameVersion != requestedGameVersion) return@whenComplete
                if (error == null) {
                    updateLoaded(items(versionList, gameVersion))
                } else {
                    _state.value = State.Failed(error, lastLoaded)
                }
            }.start()
    }

    private fun updateLoaded(versions: List<ComponentRemoteVersion>) {
        lastLoaded = versions
        _state.value = State.Loaded(versions)
    }

    private fun items(
        versionList: ComponentVersionList<*>,
        gameVersion: String
    ): List<ComponentRemoteVersion> =
        versionList.getVersions(gameVersion).filterIsInstance<ComponentRemoteVersion>()
}
