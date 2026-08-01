package com.tungsten.fcl.ui.setting

import android.content.Context
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.UIListener
import com.tungsten.fcl.ui.manage.VersionSettingPage
import com.tungsten.fcl.ui.setting.compose.ComposeSettingPage
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

        /**
         * 阶段三 3.1 设置页 Miuix 迁移开关：true 挂载 Compose 页面（ComposeSettingPage），
         * false 回滚旧 View 页面（LauncherSettingPage/HelpPage/AboutPage，文件保留未删）。
         * 游戏设置页（VersionSettingPage）不在本步骤迁移范围，始终走旧 View 页面。
         */
        private const val USE_COMPOSE_SETTING_PAGES = true
    }

    init {
        instance = this
    }

    private lateinit var versionSettingPage: VersionSettingPage
    private val launcherSettingPage: FCLCommonPage by lazy {
        if (USE_COMPOSE_SETTING_PAGES) {
            ComposeSettingPage(context, PAGE_ID_SETTING_LAUNCHER, parent, ComposeSettingPage.ScreenType.LAUNCHER)
        } else {
            LauncherSettingPage(
                context,
                PAGE_ID_SETTING_LAUNCHER,
                parent,
                R.layout.page_setting_launcher
            )
        }
    }
    private val helpPage: FCLCommonPage by lazy {
        if (USE_COMPOSE_SETTING_PAGES) {
            ComposeSettingPage(context, PAGE_ID_SETTING_HELP, parent, ComposeSettingPage.ScreenType.HELP)
        } else {
            HelpPage(context, PAGE_ID_SETTING_HELP, parent, R.layout.page_setting_help)
        }
    }
    private val aboutPage: FCLCommonPage by lazy {
        if (USE_COMPOSE_SETTING_PAGES) {
            ComposeSettingPage(context, PAGE_ID_SETTING_ABOUT, parent, ComposeSettingPage.ScreenType.ABOUT)
        } else {
            AboutPage(context, PAGE_ID_SETTING_ABOUT, parent, R.layout.page_setting_about)
        }
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
