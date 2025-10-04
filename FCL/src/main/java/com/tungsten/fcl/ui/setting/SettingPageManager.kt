package com.tungsten.fcl.ui.setting

import android.content.Context
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.UIListener
import com.tungsten.fcl.ui.manage.VersionSettingPage
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout

class SettingPageManager(
    context: Context,
    parent: FCLUILayout,
    defaultPageId: Int,
    val listener: UIListener?
) : PageManager(context, parent, defaultPageId, listener) {
    companion object {
        @JvmStatic
        var instance: SettingPageManager? = null
        const val PAGE_ID_SETTING_GAME: Int = 15030
        const val PAGE_ID_SETTING_LAUNCHER: Int = 15031
        const val PAGE_ID_SETTING_HELP: Int = 15032
        const val PAGE_ID_SETTING_ABOUT: Int = 15034
    }

    init {
        instance = this
    }

    private lateinit var versionSettingPage: VersionSettingPage
    private val launcherSettingPage: LauncherSettingPage by lazy {
        LauncherSettingPage(
            context,
            PAGE_ID_SETTING_LAUNCHER,
            parent,
            R.layout.page_setting_launcher
        )
    }
    private val helpPage: HelpPage by lazy {
        HelpPage(context, PAGE_ID_SETTING_HELP, parent, R.layout.page_setting_help)
    }
    private val aboutPage: AboutPage by lazy {
        AboutPage(context, PAGE_ID_SETTING_ABOUT, parent, R.layout.page_setting_about)
    }


    override fun init(listener: UIListener?) {
        versionSettingPage = VersionSettingPage(
            context,
            PAGE_ID_SETTING_GAME,
            parent,
            R.layout.page_version_setting,
            true
        )
        versionSettingPage.loadVersion(Profiles.getSelectedProfile(), null)
        listener?.onLoad()
    }

    override fun getAllPages(): ArrayList<FCLCommonPage> {
        return ArrayList<FCLCommonPage>().apply {
            add(versionSettingPage)
        }
    }

    override fun createPageById(id: Int): FCLCommonPage? {
        val page: FCLCommonPage? = when (id) {
            PAGE_ID_SETTING_LAUNCHER -> launcherSettingPage
            PAGE_ID_SETTING_HELP -> helpPage
            PAGE_ID_SETTING_ABOUT -> aboutPage
            else -> null
        }
        return page
    }
}