package com.tungsten.fcl.ui.version

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.mio.cache.VersionCache
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.PageVersionListBinding
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.setting.Profiles.getSelectedProfile
import com.tungsten.fcl.setting.Profiles.profiles
import com.tungsten.fcl.setting.Profiles.registerVersionsListener
import com.tungsten.fcl.setting.Profiles.unregisterVersionsListener
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.ui.FCLPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.function.Consumer
import java.util.stream.Collectors

class VersionListPage(context: Context?, id: Int) :
    FCLPage(context, id, R.layout.page_version_list),
    View.OnClickListener {
    private lateinit var binding: PageVersionListBinding
    private var adapter: VersionListAdapter? = null
    private lateinit var children: List<VersionListItem>
    private var textWatcher: TextWatcher? = null
    private var searchWatcherAttached = false
    private var highlightedProfile: Profile? = null
    private var versionHighlightListener: Runnable? = null
    private var loadJob: Job? = null
    private var versionsListener: Consumer<Profile>? = null
    private var profileCollectJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        binding = PageVersionListBinding.bind(contentView)
        binding.refresh.setOnClickListener(this)
        binding.newProfile.setOnClickListener(this)
        // 版本刷新监听：attach 恢复、detach 注销，防止静态列表持有已销毁页面（与 DownloadUI 一致）
        val listener = Consumer<Profile> { loadVersions(it) }
        versionsListener = listener
        registerVersionsListener(listener)
        contentView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                versionsListener?.let {
                    unregisterVersionsListener(it)
                    registerVersionsListener(it)
                }
                // 版本刷新事件走监听器；首次 attach 与切换 Profile 由 collect 首值承担，避免双重加载
                profileCollectJob = activity.lifecycleScope.launch {
                    Profiles.selectedProfile.collect { profile ->
                        if (profile != null) loadVersions(profile)
                    }
                }
            }

            override fun onViewDetachedFromWindow(v: View) {
                versionsListener?.let { unregisterVersionsListener(it) }
                profileCollectJob?.cancel()
                // 移除挂到 profile 单例上的高亮监听，避免页面销毁后仍被回调
                // （attach 时 collect 立即发射当前值，会重新 loadVersions 注册）
                versionHighlightListener?.let { highlightedProfile?.removeSelectedVersionListener(it) }
                versionHighlightListener = null
                highlightedProfile = null
            }
        })
        refreshProfile()
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val text = s.toString()
                adapter?.updateVersionList(if (text.isEmpty()) children else children.filter {
                    it.version.lowercase(
                        Locale.getDefault()
                    ).contains(text.lowercase(Locale.getDefault()))
                })
            }
        }
        binding.category.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                filterByTab(tab.position)
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
     * 按分类 tab 过滤版本列表：0 全部，1 Fabric，2 Forge，3 NeoForge，4 其他
     */
    private fun filterByTab(position: Int) {
        when (position) {
            0 -> {
                adapter?.updateVersionList(children)
            }

            1 -> {
                adapter?.updateVersionList(
                    children.filter {
                        it.libraries.split(",").find { lib ->
                            lib.contains(":") && lib.contains("Fabric")
                        } != null
                    }
                )
            }

            2 -> {
                adapter?.updateVersionList(
                    children.filter {
                        it.libraries.split(",").find { lib ->
                            lib.contains(":") && lib.contains("Forge") && !lib.contains("NeoForge")
                        } != null
                    }
                )
            }

            3 -> {
                adapter?.updateVersionList(
                    children.filter {
                        it.libraries.split(",").find { lib ->
                            lib.contains(":") && lib.contains("NeoForge")
                        } != null
                    }
                )
            }

            else -> {
                adapter?.updateVersionList(
                    children.filter {
                        it.libraries.split(",").none { lib ->
                            lib.contains("Fabric") || lib.contains("Forge") || lib.contains("NeoForge")
                        }
                    }
                )
            }

        }
    }

    override fun refresh(vararg param: Any?): Task<*>? {
        return Task.runAsync {}
    }

    fun refreshProfile() {
        val adapter = ProfileListAdapter(context, profiles)
        binding.profileList.adapter = adapter
    }

    private fun loadVersions(profile: Profile) {
        // 终止上一个加载（切换 profile 时旧版本加载立即取消，避免过期结果覆盖）
        loadJob?.cancel()
        var job: Job? = null
        job = MainActivity.getInstance().lifecycleScope.launch {
            binding.category.selectTab(binding.category.getTabAt(0))
            binding.search.removeTextChangedListener(textWatcher)
            searchWatcherAttached = false
            binding.search.setText("")
            binding.refresh.isEnabled = false
            if (profile == getSelectedProfile()) {
                val repository = profile.repository
                val ids = withContext(Dispatchers.IO) {
                    repository.displayVersions.map { it.id }.collect(Collectors.toList())
                }
                // 命中会话级快照时跳过进度条即时渲染，最新数据随后台刷新覆盖
                // （快照在创建时排序，绘制与后续 sameEntries 比较共用同一份，避免顺序不一致误判为数据变化）
                val snapshotMap = VersionCache.get(profile)
                val snapshot = sortEntries(ids.mapNotNull { snapshotMap[it] })
                if (snapshot.isNotEmpty()) {
                    showVersions(profile, snapshot)
                } else {
                    binding.layout.visibility = View.GONE
                    binding.progress.visibility = View.VISIBLE
                }
                registerHighlightListener(profile)
                // 共享快照重算（写入 VersionCache，主界面快速切换弹窗同样读取）
                val entries = VersionCache.refresh(profile)
                // 加载期间可能已切换 profile 或重新加载，放弃过期结果
                if (loadJob !== job) return@launch
                val sorted = sortEntries(entries)
                // 与快照一致时跳过重绘，避免列表无意义地重放入场动画
                if (snapshot.isEmpty() || !sameEntries(snapshot, sorted)) {
                    showVersions(profile, sorted)
                }
            }
        }
        loadJob = job
    }

    /**
     * 在主线程应用一批版本条目：刷新适配器、恢复搜索框过滤、滚动到选中版本
     */
    private fun showVersions(profile: Profile, entries: List<VersionCache.Entry>) {
        children = entries.map {
            VersionListItem(profile, it.id, it.libraries, it.tag, it.newIcon(), it.modCount)
        }
        if (adapter == null) {
            adapter = VersionListAdapter(context, children)
            binding.versionList.adapter = adapter
            binding.versionList.layoutManager = LinearLayoutManager(context)
        } else {
            adapter!!.updateVersionList(children)
        }
        binding.refresh.isEnabled = true
        if (children.isNotEmpty()) {
            binding.layout.visibility = View.VISIBLE
        }
        binding.progress.visibility = View.GONE
        if (!searchWatcherAttached) {
            binding.search.addTextChangedListener(textWatcher)
            searchWatcherAttached = true
        }
        val selected = children.find { it.selectedProperty().get() }
        if (selected != null) {
            binding.versionList.scrollToPosition(children.indexOf(selected))
        }
    }

    /**
     * 按真实游戏版本从大到小排序（GameVersionNumber 比较，无法识别的版本排在最后），同版本时按 id 倒序保持稳定
     */
    private fun sortEntries(entries: List<VersionCache.Entry>): List<VersionCache.Entry> {
        return entries.sortedWith(
            compareByDescending<VersionCache.Entry> { it.gameVersion }
                .thenByDescending { it.id }
        )
    }

    private fun sameEntries(
        a: List<VersionCache.Entry>,
        b: List<VersionCache.Entry>
    ): Boolean {
        if (a.size != b.size) return false
        return a.zip(b).all { (x, y) ->
            x.id == y.id && x.libraries == y.libraries && x.tag == y.tag
                    && x.modCount == y.modCount && x.iconKey == y.iconKey
        }
    }

    /**
     * 版本选中高亮：监听 profile 版本变化时更新（替代 fakefx bind）
     */
    private fun registerHighlightListener(profile: Profile) {
        versionHighlightListener?.let { highlightedProfile?.removeSelectedVersionListener(it) }
        val highlightListener = Runnable {
            if (!::children.isInitialized) return@Runnable
            children.forEach { item ->
                item.selectedProperty().set(profile.selectedVersion == item.version)
            }
        }
        versionHighlightListener = highlightListener
        highlightedProfile = profile
        profile.addSelectedVersionListener(highlightListener)
    }

    override fun onClick(view: View?) {
        if (view === binding.refresh) {
            val profile = getSelectedProfile()
            // 强制刷新：失效快照缓存，刷新完成的事件回调走冷加载（显示进度条并全量重绘）
            VersionCache.invalidate(profile)
            profile.repository.refreshVersionsAsync().start()
        }
        if (view === binding.newProfile) {
            val dialog = AddProfileDialog(context)
            dialog.show()
        }
    }
}
