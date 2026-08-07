package com.tungsten.fcl.ui.manage

import android.content.Context
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.UIListener
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fcl.ui.manage.compose.ComposeManageInstallerListPage
import com.tungsten.fcl.ui.manage.compose.ComposeManagePage
import com.tungsten.fcl.ui.manage.compose.ComposeModListPage
import com.tungsten.fcl.ui.manage.compose.ComposeVersionSettingPage
import com.tungsten.fcl.ui.manage.compose.ComposeWorldListPage
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout

class ManagePageManager(
    context: Context,
    parent: FCLUILayout,
    defaultPageId: Int,
    val listener: UIListener?
) : PageManager(context, parent, defaultPageId, listener) {
    companion object {
        @JvmStatic
        var instance: ManagePageManager? = null
        const val PAGE_ID_MANAGE_MANAGE: Int = 15000
        const val PAGE_ID_MANAGE_SETTING: Int = 15001
        const val PAGE_ID_MANAGE_INSTALL: Int = 15002
        const val PAGE_ID_MANAGE_MOD: Int = 15003
        const val PAGE_ID_MANAGE_WORLD: Int = 15004

        /** Mod 列表/更新页 Compose 开关：false 回滚旧 ModListPage/ModUpdatesPage。 */
        const val USE_COMPOSE_MOD_PAGES: Boolean = true
    }

    var profile: Profile? = null
    var version: String? = null

    private lateinit var versionSettingPage: FCLCommonPage
    private val managePage: FCLCommonPage by lazy {
        // 批 2：Compose 开关已固化，旧 View 页面（ManagePage + page_manage_version.xml）已删除。
        ComposeManagePage(context, PAGE_ID_MANAGE_MANAGE, parent)
    }
    private val installerListPage: FCLCommonPage = ComposeManageInstallerListPage(context, PAGE_ID_MANAGE_INSTALL, parent)
    private val modListPage: FCLCommonPage by lazy {
        if (USE_COMPOSE_MOD_PAGES) {
            ComposeModListPage(context, PAGE_ID_MANAGE_MOD, parent)
        } else {
            ModListPage(
                context,
                PAGE_ID_MANAGE_MOD,
                parent,
                R.layout.page_manage_mod
            )
        }
    }
    private val worldListPage: FCLCommonPage = ComposeWorldListPage(context, PAGE_ID_MANAGE_WORLD, parent)

    private var versionLoaded = false
    private lateinit var runnable: () -> Unit

    init {
        instance = this
    }

    override fun init(listener: UIListener?) {
        // 批 2：Compose 开关已固化，旧 View 页面（VersionSettingPage + page_version_setting.xml）已删除。
        versionSettingPage = ComposeVersionSettingPage(context, PAGE_ID_MANAGE_SETTING, parent, false)
        listener?.onLoad()
    }

    override fun getAllPages(): ArrayList<FCLCommonPage> {
        return ArrayList<FCLCommonPage>().apply {
            add(versionSettingPage)
        }
    }

    override fun switchPage(id: Int) {
        runnable = {
            super.switchPage(id)
        }
        if (versionLoaded)
            runnable()
    }

    override fun createPageById(id: Int): FCLCommonPage? {
        val page: FCLCommonPage? = when (id) {
            PAGE_ID_MANAGE_MANAGE -> managePage
            PAGE_ID_MANAGE_INSTALL -> installerListPage
            PAGE_ID_MANAGE_MOD -> modListPage
            PAGE_ID_MANAGE_WORLD -> worldListPage
            else -> null
        }
        if (page != null) {
            allPages.add(page)
            (page as VersionLoadable).loadVersion(profile, version)
        }
        return page
    }

    fun loadVersion(profile: Profile?, version: String?) {
        this.profile = profile
        this.version = version
        allPages.forEach {
            (it as VersionLoadable).loadVersion(profile, version)
        }
        if (!versionLoaded) {
            versionLoaded = true
            runnable()
        }
    }

    fun onRunDirectoryChange(profile: Profile, version: String?) {
        (modListPage as VersionLoadable).loadVersion(profile, version)
        (worldListPage as VersionLoadable).loadVersion(profile, version)
    }
}