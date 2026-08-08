package com.tungsten.fcl.ui.setting

import android.content.Context
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.UIListener
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fcl.ui.manage.compose.ComposeVersionSettingPage
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
    }

    init {
        instance = this
    }

    private lateinit var versionSettingPage: FCLCommonPage
    // 批 2：Compose 开关已固化，旧 View 页面（LauncherSettingPage/HelpPage/AboutPage）已删除。
    private val launcherSettingPage: FCLCommonPage by lazy {
        ComposeSettingPage(context, PAGE_ID_SETTING_LAUNCHER, parent, ComposeSettingPage.ScreenType.LAUNCHER)
    }
    private val helpPage: FCLCommonPage by lazy {
        ComposeSettingPage(context, PAGE_ID_SETTING_HELP, parent, ComposeSettingPage.ScreenType.HELP)
    }
    private val aboutPage: FCLCommonPage by lazy {
        ComposeSettingPage(context, PAGE_ID_SETTING_ABOUT, parent, ComposeSettingPage.ScreenType.ABOUT)
    }


    override fun init(listener: UIListener?) {
        // 批 2：Compose 开关已固化，旧 View 页面（VersionSettingPage + page_version_setting.xml）已删除。
        versionSettingPage = ComposeVersionSettingPage(context, PAGE_ID_SETTING_GAME, parent, true)
        (versionSettingPage as VersionLoadable).loadVersion(Profiles.getSelectedProfile(), null)
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
