package com.tungsten.fcl.ui.manage

import android.content.Context
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.UIListener
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
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
    }

    var profile: Profile? = null
    var version: String? = null

    lateinit var managePage: ManagePage
    val versionSettingPage: VersionSettingPage by lazy {
        VersionSettingPage(
            context,
            PAGE_ID_MANAGE_SETTING,
            parent,
            R.layout.page_version_setting,
            false
        )
    }
    val installerListPage: InstallerListPage by lazy {
        InstallerListPage(
            context,
            PAGE_ID_MANAGE_INSTALL,
            parent,
            R.layout.page_installer_list
        )
    }
    val modListPage: ModListPage by lazy {
        ModListPage(
            context,
            PAGE_ID_MANAGE_MOD,
            parent,
            R.layout.page_mod_list
        )
    }
    val worldListPage: WorldListPage by lazy {
        WorldListPage(
            context,
            PAGE_ID_MANAGE_WORLD,
            parent,
            R.layout.page_world_list
        )
    }

    init {
        instance = this
    }

    override fun init(listener: UIListener?) {
        managePage =
            ManagePage(context, PAGE_ID_MANAGE_MANAGE, parent, R.layout.page_manage)
        listener?.onLoad()
    }

    override fun getAllPages(): ArrayList<FCLCommonPage> {
        return ArrayList<FCLCommonPage>().apply {
            add(managePage)
        }
    }

    override fun createPageById(id: Int): FCLCommonPage? {
        val page: FCLCommonPage? = when (id) {
            PAGE_ID_MANAGE_SETTING -> versionSettingPage
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
    }

    fun onRunDirectoryChange(profile: Profile?, version: String?) {
        modListPage.loadVersion(profile, version)
        worldListPage.loadVersion(profile, version)
    }
}