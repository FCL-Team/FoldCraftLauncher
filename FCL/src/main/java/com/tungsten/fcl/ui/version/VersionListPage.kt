package com.tungsten.fcl.ui.version

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.PageVersionListBinding
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.setting.Profiles.profiles
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.ui.FCLPage
import com.tungsten.fcllibrary.component.ui.observeWhileAttached
import java.util.Locale

/**
 * 版本列表页：只渲染 [VersionListViewModel] 的状态（数据加载/失效/去重全部在 VM），
 * 并 observe 选中版本流更新高亮。搜索文本与分类 tab 是页面自有筛选状态，
 * 数据刷新不再触碰它们（装完新版本回到列表，筛选保持）。
 */
class VersionListPage(context: Context?, id: Int) :
    FCLPage(context, id, R.layout.page_version_list),
    View.OnClickListener {
    private lateinit var binding: PageVersionListBinding
    private var adapter: VersionListAdapter? = null
    private lateinit var children: List<VersionListItem>
    private var textWatcher: TextWatcher? = null
    private var searchWatcherAttached = false
    private var searchText = ""
    private var currentTab = 0

    /** 上次渲染的版本 id 集合：仅数据集变化（首次加载/安装/删除版本/切 Profile）时才滚动
     *  定位选中项，收藏重排不滚动，避免连续收藏时视口被反复拉走 */
    private var lastEntryIds: Set<String>? = null
    private lateinit var viewModel: VersionListViewModel

    override fun onCreate() {
        super.onCreate()
        binding = PageVersionListBinding.bind(contentView)
        binding.refresh.setOnClickListener(this)
        binding.newProfile.setOnClickListener(this)

        viewModel = ViewModelProvider(getActivity()).get(VersionListViewModel::class.java)
        observeWhileAttached(viewModel.state) { render(it) }
        // 高亮跟随选中版本（StateFlow 重放当前值，页面重建自动校准）
        observeWhileAttached(Profiles.selectedVersion) { selected ->
            if (::children.isInitialized) {
                children.forEach { it.selectedProperty().set(it.version == selected) }
            }
            // adapter 内条目在 diff 后可能仍是未重绑的旧对象，勾选态须单独刷新
            adapter?.updateSelection(selected)
        }

        refreshProfile()
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                searchText = s.toString()
                applyFilter()
            }
        }
        binding.category.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                currentTab = tab.position
                applyFilter()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
        // TabLayout 无 XML 默认选中，初始化选中「全部」分类
        binding.category.selectTab(binding.category.getTabAt(0))
    }

    /**
     * 应用当前搜索文本与分类 tab 过滤（0 全部，1 Fabric，2 Forge，3 NeoForge，4 其他）
     */
    private fun applyFilter() {
        if (!::children.isInitialized) return
        var list = children
        if (searchText.isNotEmpty()) {
            val keyword = searchText.lowercase(Locale.getDefault())
            list = list.filter { it.version.lowercase(Locale.getDefault()).contains(keyword) }
        }
        list = when (currentTab) {
            1 -> list.filter { it.libraries.hasLoaderTag("Fabric") }
            2 -> list.filter { it.libraries.hasLoaderTag("Forge", excludeName = "NeoForge") }
            3 -> list.filter { it.libraries.hasLoaderTag("NeoForge") }
            4 -> list.filter {
                it.libraries.split(",").none { lib ->
                    lib.contains("Fabric") || lib.contains("Forge") || lib.contains("NeoForge")
                }
            }

            else -> list
        }
        adapter?.updateVersionList(list)
    }

    /**
     * 组件摘要片段（如 "Fabric: 0.16.0"）是否含某加载器；
     * excludeName 在片段级别排除另一加载器（NeoForge 含 "Forge" 字样，两者须按段区分）
     */
    private fun String.hasLoaderTag(name: String, excludeName: String? = null): Boolean =
        split(",").any { it.contains(":") && it.contains(name) && (excludeName == null || !it.contains(excludeName)) }

    /**
     * 渲染 VM 状态：条目变化时重建 item 列表并重新应用筛选；冷加载显示进度条。
     */
    private fun render(state: VersionListViewModel.UiState) {
        val profile = state.profile ?: return
        if (state.loading && state.entries.isEmpty()) {
            binding.layout.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
            binding.refresh.isEnabled = false
            return
        }
        children = state.entries.map {
            VersionListItem(profile, it.id, it.libraries, it.tag, it.newIcon(), it.modCount, it.id in state.pinned)
        }
        if (adapter == null) {
            adapter = VersionListAdapter(context, children)
            binding.versionList.adapter = adapter
            binding.versionList.layoutManager = LinearLayoutManager(context)
            adapter?.itemTouchHelper?.attachToRecyclerView(binding.versionList)
        }
        binding.progress.visibility = View.GONE
        binding.refresh.isEnabled = true
        if (children.isNotEmpty()) {
            binding.layout.visibility = View.VISIBLE
        }
        if (!searchWatcherAttached) {
            binding.search.addTextChangedListener(textWatcher)
            searchWatcherAttached = true
        }
        applyFilter()
        val selected = children.find { it.selectedProperty().get() }
        if (selected != null) {
            val entryIds = state.entries.map { it.id }.toSet()
            val entriesChanged = entryIds != lastEntryIds
            lastEntryIds = entryIds
            if (entriesChanged) {
                binding.versionList.scrollToPosition(children.indexOf(selected))
            }
        }
    }


    fun refreshProfile() {
        val adapter = ProfileListAdapter(context, profiles)
        binding.profileList.adapter = adapter
    }

    override fun onClick(view: View?) {
        if (view === binding.refresh) {
            // 强制刷新：VM 失效快照并触发仓库重扫，完成事件经 tick 走冷加载（进度条 + 全量重绘）
            viewModel.forceRefresh()
        }
        if (view === binding.newProfile) {
            val dialog = AddProfileDialog(context)
            dialog.show()
        }
    }
}
