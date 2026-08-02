package com.tungsten.fcl.ui.version.compose

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.bridge.FCLViewModel
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.mod.ModpackConfiguration
import com.tungsten.fclcore.util.Logging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.file.Files
import java.util.Locale
import java.util.logging.Level
import java.util.stream.Collectors
import kotlin.io.path.isRegularFile

/**
 * 版本列表页 ViewModel（小步骤 3.3）：VersionListPage.kt + VersionListAdapter.kt 的 Compose 化承接。
 *
 * 行为对齐（interaction-map §3.2/§3.3 逐条）：
 * - 版本仓库变更监听自动重载：`Profiles.registerVersionsListener`（:47）；
 * - 加载开始：清空搜索、分类强制回到"全部"、禁用刷新、显示进度（:126-133）；
 * - 列表数据构建：游戏版本 + 安装器标记 + 整合包 tag + 图标（:138-202），
 *   Mod 数量统计从 Adapter onBind 的同步 IO 前移到加载期（行为输出一致）；
 * - 搜索过滤：大小写不敏感 contains，空文本显示全部（:58-65）；
 * - 分类过滤：全部/Fabric/Forge/NeoForge/其他（:67-113）；
 *   搜索与分类**不叠加**，以最后操作为准（旧代码两个监听各自全量替换列表）；
 * - 选中态：`profile.selectedVersionProperty` 经 fakefx → Flow 单向承接（:223-229），
 *   点击整行/单选按钮 = 写 `profile.selectedVersion`（:54,64-66）；
 * - 加载完成后滚动到选中版本（:218-221），经 [VersionListUiState.loadTick] 触发。
 */
class VersionListViewModel(
    private val application: Application,
) : FCLViewModel<VersionListUiState, VersionListEvent>(VersionListUiState()) {

    /** 当前列表所属的游戏目录（加载时快照，对齐 VersionListItem.profile）。 */
    private var profile: Profile? = null

    /** 未过滤的完整列表（对齐旧页面的 children 字段）。 */
    private var children: List<VersionItemUi> = emptyList()

    /** 最后生效的过滤方式（搜索或分类，旧代码两个监听互相覆盖，不叠加）。 */
    private var filterMode: FilterMode = FilterMode.Search("")

    init {
        // 选中版本联动：Profiles.selectedVersion 已绑定到选中 Profile 的 selectedVersionProperty
        Profiles.selectedVersionProperty().asStateFlow()
            .observeIntoState { copy(selectedVersion = it) }
        Profiles.registerVersionsListener { loadVersions(it) }
    }

    /** 刷新按钮：重新扫描版本仓库（对齐 VersionListPage.onClick :237-239）。 */
    fun onRefresh() {
        Profiles.getSelectedProfile().repository.refreshVersionsAsync().start()
    }

    /** 新建游戏目录按钮：弹窗属一次性副作用，交宿主（对齐 :240-248）。 */
    fun onNewProfile() {
        sendEvent(VersionListEvent.NewProfile)
    }

    /** 搜索框实时过滤（对齐 textWatcher :58-65）。 */
    fun onSearchChange(text: String) {
        filterMode = FilterMode.Search(text)
        updateState { copy(searchText = text, items = applyFilter(children, filterMode)) }
    }

    /** 分类切换过滤（对齐 category.setOnCheckedChangeListener :67-113）。 */
    fun onCategoryChange(category: VersionCategory) {
        filterMode = FilterMode.Category(category)
        updateState { copy(category = category, items = applyFilter(children, filterMode)) }
    }

    /** 整行/单选按钮点击 = 设为选中版本（对齐 :54, :64-66）。 */
    fun onSelectVersion(item: VersionItemUi) {
        profile?.selectedVersion = item.version
    }

    /** 删除按钮：确认弹窗走遗留 Versions.deleteVersion（业务零重写，对齐 :57-63）。 */
    fun onDeleteVersion(item: VersionItemUi) {
        val p = profile ?: return
        sendEvent(VersionListEvent.DeleteVersion(p, item.version))
    }

    /** 设置按钮（仅版本独立设置可见）：选中该版本并跳管理 UI 第一个 Tab（对齐 :67-77）。 */
    fun onOpenVersionSettings(item: VersionItemUi) {
        profile?.selectedVersion = item.version
        sendEvent(VersionListEvent.OpenVersionSettings)
    }

    /** 版本仓库变更 → 重新加载列表（对齐 loadVersions :126-234）。 */
    private fun loadVersions(p: Profile) {
        profile = p
        filterMode = FilterMode.Search("")
        // 对齐 :128-133：重置搜索/分类、禁用刷新、隐藏列表、显示进度
        updateState {
            copy(
                loading = true,
                searchText = "",
                category = VersionCategory.ALL,
            )
        }
        val repository = p.repository
        viewModelScope.launch {
            if (p != Profiles.getSelectedProfile()) return@launch
            val loaded = withContext(Dispatchers.IO) {
                repository.displayVersions
                    .parallel()
                    .map { version -> buildItem(p, version.id) }
                    .collect(Collectors.toList())
            }
            if (p != Profiles.getSelectedProfile()) return@launch
            children = loaded
            updateState {
                copy(
                    loading = false,
                    hasVersions = loaded.isNotEmpty(),
                    items = loaded,
                    loadTick = loadTick + 1,
                )
            }
        }
    }

    /** 单个版本条目构建（对齐 :141-200 + Adapter :81-93 的 Mod 计数、:67 的独立设置判定）。 */
    private fun buildItem(
        profile: Profile,
        id: String,
    ): VersionItemUi {
        val repository = profile.repository
        val game = repository.getGameVersion(id)
        val libraries = StringBuilder(game.orElse(application.getString(R.string.message_unknown)))
        val analyzer = LibraryAnalyzer.analyze(
            repository.getResolvedPreservingPatchesVersion(id),
            game.orElse(null),
        )
        for (mark in analyzer) {
            val libraryId = mark.libraryId
            val libraryVersion = mark.libraryVersion
            if (libraryId == LibraryAnalyzer.LibraryType.MINECRAFT.patchId) continue
            if (AndroidUtils.hasStringId(
                    application,
                    "install_installer_" + libraryId.replace("-", "_"),
                )
            ) {
                libraries.append(", ").append(
                    AndroidUtils.getLocalizedText(
                        application,
                        "install_installer_" + libraryId.replace("-", "_"),
                    )
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
        val modCount = runCatching {
            Files.list(repository.getModsDirectory(id)).use { stream ->
                stream.filter { it.isRegularFile() }.count().toInt()
            }
        }.getOrDefault(0)
        return VersionItemUi(
            version = id,
            libraries = libraries.toString(),
            modCount = modCount,
            tag = tag,
            icon = repository.getVersionIconImage(id),
            showSetting = !profile.getVersionSetting(id).isGlobal,
        )
    }

    /** 过滤求值（语义对齐旧代码两个监听的各自分支）。 */
    private fun applyFilter(children: List<VersionItemUi>, mode: FilterMode): List<VersionItemUi> =
        when (mode) {
            is FilterMode.Search -> if (mode.text.isEmpty()) {
                children
            } else {
                children.filter {
                    it.version.lowercase(Locale.getDefault())
                        .contains(mode.text.lowercase(Locale.getDefault()))
                }
            }

            is FilterMode.Category -> when (mode.category) {
                VersionCategory.ALL -> children
                VersionCategory.FABRIC -> children.filter { item ->
                    item.libraries.split(",").any { lib ->
                        lib.contains(":") && lib.contains("Fabric")
                    }
                }

                VersionCategory.FORGE -> children.filter { item ->
                    item.libraries.split(",").any { lib ->
                        lib.contains(":") && lib.contains("Forge") && !lib.contains("NeoForge")
                    }
                }

                VersionCategory.NEOFORGE -> children.filter { item ->
                    item.libraries.split(",").any { lib ->
                        lib.contains(":") && lib.contains("NeoForge")
                    }
                }

                VersionCategory.OTHER -> children.filter { item ->
                    item.libraries.split(",").none { lib ->
                        lib.contains("Fabric") || lib.contains("Forge") || lib.contains("NeoForge")
                    }
                }
            }
        }

    private sealed interface FilterMode {
        data class Search(val text: String) : FilterMode
        data class Category(val category: VersionCategory) : FilterMode
    }
}

/** 版本分类（对齐 page_version_list.xml 的 RadioGroup 五项）。 */
enum class VersionCategory {
    ALL, FABRIC, FORGE, NEOFORGE, OTHER
}

/** 版本列表条目 UI 模型（对齐 VersionListItem + Adapter onBind 的派生数据）。 */
data class VersionItemUi(
    val version: String,
    /** 安装器标记原文（分类过滤的判定输入，不含 Mod 计数后缀）。 */
    val libraries: String,
    /** mods 目录文件数（对齐 Adapter :81-93，加载期一次算好）。 */
    val modCount: Int,
    val tag: String?,
    val icon: Drawable?,
    /** 是否显示"版本独立设置"按钮（!isGlobal，对齐 Adapter :67）。 */
    val showSetting: Boolean,
)

/** 版本列表页 UI 状态。 */
data class VersionListUiState(
    val loading: Boolean = true,
    /** 未过滤列表是否非空（旧代码空列表隐藏整个右侧面板，:215-216）。 */
    val hasVersions: Boolean = false,
    /** 当前展示的（已过滤）列表。 */
    val items: List<VersionItemUi> = emptyList(),
    val searchText: String = "",
    val category: VersionCategory = VersionCategory.ALL,
    val selectedVersion: String? = null,
    /** 每次加载完成自增，驱动"滚动到选中版本"（对齐 :218-221）。 */
    val loadTick: Int = 0,
)

/** 版本列表页一次性事件。 */
sealed interface VersionListEvent {
    data object NewProfile : VersionListEvent
    data class DeleteVersion(val profile: Profile, val version: String) : VersionListEvent
    data object OpenVersionSettings : VersionListEvent
}
