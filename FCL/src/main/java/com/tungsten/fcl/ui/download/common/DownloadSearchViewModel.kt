package com.tungsten.fcl.ui.download.common

import androidx.lifecycle.ViewModel
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository

/**
 * 下载页搜索状态：挂 Activity 的 ViewModelStore，与 Activity 同生命周期。
 *
 * 共享下载页在 5 个模式（Mod/整合包/资源包/世界/光影）间切换时，
 * 各模式的搜索条件与结果按页面 id 分别保存，切回时直接恢复，
 * 不重新搜索、不重新加载图片。
 */
class DownloadSearchViewModel : ViewModel() {

    /** 单个下载模式（按页面 id 区分）的搜索状态 */
    class State {
        @JvmField
        var searchFilter: String = ""
        @JvmField
        var userGameVersion: String? = null
        @JvmField
        var category: RemoteModRepository.Category? = null
        @JvmField
        var sortType: RemoteModRepository.SortType = RemoteModRepository.SortType.POPULARITY
        @JvmField
        var pageOffset: Int = 0
        /** 下载源名称（Modrinth/CurseForge，仅 Localized 模式） */
        @JvmField
        var source: String? = null
        @JvmField
        var result: ArrayList<RemoteMod>? = null
        @JvmField
        var pageCount: Int = -1
        /** Mod 模式的加载器筛选位置 */
        @JvmField
        var modLoaderPosition: Int = 0
        /** 该模式的列表 adapter（切换模式时复用，避免重建列表重播 item 动画） */
        @JvmField
        var adapter: RemoteModListAdapter? = null
    }

    private val states = HashMap<Int, State>()

    fun getState(pageId: Int): State = states.getOrPut(pageId) { State() }
}
