package com.tungsten.fcl.ui.download.compose

import android.content.Context
import com.tungsten.fcl.R
import com.tungsten.fcl.game.LocalizedRemoteModRepository
import com.tungsten.fcl.ui.download.DownloadPageManager
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.mod.curse.CurseAddon
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository
import com.tungsten.fclcore.mod.modrinth.ModrinthRemoteModRepository

/**
 * 下载域 5 个远程资源 Tab 的仓库配置（迁移自 DownloadPage 各子类的构造差异，旧 View 页面已删除）：
 * - [MODPACK] ↔ ModpackDownloadPage（支持中文翻译搜索 + 安装本地整合包入口）；
 * - [MOD] ↔ ModDownloadPage（中文翻译 + ModLoader 二次过滤 + 已安装检测）；
 * - [RESOURCE_PACK] / [SHADER_PACK] ↔ 同名子类（无翻译按钮）；
 * - [WORLD] ↔ 直接使用 CurseForgeRemoteModRepository.WORLDS 的基类实例
 *   （无下载源切换、callback 为 null → 走"另存为"）。
 */
enum class DownloadTab(
    val pageId: Int,
    val supportChinese: Boolean,
    val hasDownloadSource: Boolean,
    val hasModLoaderFilter: Boolean,
    val hasInstallLocalModpack: Boolean,
    val repositoryType: RemoteModRepository.Type,
) {
    MODPACK(
        DownloadPageManager.PAGE_ID_DOWNLOAD_MODPACK,
        supportChinese = true,
        hasDownloadSource = true,
        hasModLoaderFilter = false,
        hasInstallLocalModpack = true,
        repositoryType = RemoteModRepository.Type.MODPACK,
    ),
    MOD(
        DownloadPageManager.PAGE_ID_DOWNLOAD_MOD,
        supportChinese = true,
        hasDownloadSource = true,
        hasModLoaderFilter = true,
        hasInstallLocalModpack = false,
        repositoryType = RemoteModRepository.Type.MOD,
    ),
    RESOURCE_PACK(
        DownloadPageManager.PAGE_ID_DOWNLOAD_RESOURCE_PACK,
        supportChinese = false,
        hasDownloadSource = true,
        hasModLoaderFilter = false,
        hasInstallLocalModpack = false,
        repositoryType = RemoteModRepository.Type.MOD,
    ),
    WORLD(
        DownloadPageManager.PAGE_ID_DOWNLOAD_WORLD,
        supportChinese = false,
        hasDownloadSource = false,
        hasModLoaderFilter = false,
        hasInstallLocalModpack = false,
        repositoryType = RemoteModRepository.Type.MOD,
    ),
    SHADER_PACK(
        DownloadPageManager.PAGE_ID_DOWNLOAD_SHADER_PACK,
        supportChinese = false,
        hasDownloadSource = true,
        hasModLoaderFilter = false,
        hasInstallLocalModpack = false,
        repositoryType = RemoteModRepository.Type.MOD,
    );

    /** 是否默认选中 Modrinth 源（对齐各子类 downloadSource.set(modrinth)）。 */
    val defaultModrinth: Boolean get() = hasDownloadSource

    /** 创建该 Tab 的仓库（对齐子类匿名 Repository：按当前源动态选择后端）。 */
    fun createRepository(isModrinth: () -> Boolean): RemoteModRepository =
        if (this == WORLD) {
            CurseForgeRemoteModRepository.WORLDS
        } else {
            TabBackedRepository(this, isModrinth)
        }

    /** 分类文案本地化（对齐各子类 getLocalizedCategory 的重写差异）。 */
    fun localizedCategory(context: Context, isModrinth: Boolean, category: String): String =
        if (hasDownloadSource && isModrinth) {
            val suffix = if (this == RESOURCE_PACK) {
                category.replace("-", "_").replace("+", "")
            } else {
                category.replace("-", "_")
            }
            AndroidUtils.getLocalizedText(context, "modrinth_category_$suffix")
        } else {
            AndroidUtils.getLocalizedText(context, "curse_category_$category")
        }

    /** 分类缩进项展示文案（对齐 DownloadPage.getLocalizedCategoryIndent）。 */
    fun localizedCategoryIndent(
        context: Context,
        isModrinth: Boolean,
        entry: CategoryEntry,
    ): String {
        val category = entry.category
            ?: return context.getString(R.string.curse_category_0)
        val prefix = " ".repeat(entry.indent * 4)
        val localized = localizedCategory(context, isModrinth, category.id)
        if (!localized.startsWith("curse_category_")) {
            return prefix + localized
        }
        return when (val self = category.self) {
            is CurseAddon.Category -> prefix + self.name
            is ModrinthRemoteModRepository.Category -> prefix + self.name()
            else -> prefix + localized
        }
    }
}

/** 分类树缩进展开条目（对齐 DownloadPage.CategoryIndented，indent=0 且 category=null 为"全部"）。 */
data class CategoryEntry(val indent: Int, val category: RemoteModRepository.Category?)

/** 递归展开分类树（对齐 DownloadPage.resolveCategory）。 */
fun resolveCategoryEntries(
    category: RemoteModRepository.Category,
    indent: Int,
    result: MutableList<CategoryEntry>,
) {
    result.add(CategoryEntry(indent, category))
    for (sub in category.subcategories()) {
        resolveCategoryEntries(sub, indent + 1, result)
    }
}

/** 对齐各 DownloadPage 子类的匿名 Repository：按"当前下载源"动态委托后端仓库。 */
private class TabBackedRepository(
    private val tab: DownloadTab,
    private val isModrinth: () -> Boolean,
) : LocalizedRemoteModRepository() {

    override fun getBackedRemoteModRepository(): RemoteModRepository {
        val modrinth = isModrinth()
        return when (tab) {
            DownloadTab.MODPACK ->
                if (modrinth) ModrinthRemoteModRepository.MODPACKS else CurseForgeRemoteModRepository.MODPACKS

            DownloadTab.MOD ->
                if (modrinth) ModrinthRemoteModRepository.MODS else CurseForgeRemoteModRepository.MODS

            DownloadTab.RESOURCE_PACK ->
                if (modrinth) ModrinthRemoteModRepository.RESOURCE_PACKS else CurseForgeRemoteModRepository.RESOURCE_PACKS

            DownloadTab.SHADER_PACK ->
                if (modrinth) ModrinthRemoteModRepository.SHADER_PACKS else CurseForgeRemoteModRepository.SHADER_PACKS

            DownloadTab.WORLD -> CurseForgeRemoteModRepository.WORLDS // 不可达（WORLD 不走本类）
        }
    }

    override fun getBackedRemoteModRepositorySortOrder(): RemoteModRepository.SortType =
        if (isModrinth()) RemoteModRepository.SortType.NAME else RemoteModRepository.SortType.POPULARITY

    override fun getType(): RemoteModRepository.Type = tab.repositoryType
}
