package com.tungsten.fcl.ui

import android.content.Context
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.account.AccountUI
import com.tungsten.fcl.ui.account.compose.ComposeAccountUI
import com.tungsten.fcl.ui.controller.ControllerUI
import com.tungsten.fcl.ui.download.DownloadUI
import com.tungsten.fcl.ui.main.MainUI
import com.tungsten.fcl.ui.main.compose.ComposeMainUI
import com.tungsten.fcl.ui.manage.ManageUI
import com.tungsten.fcl.ui.multiplayer.MultiplayerUI
import com.tungsten.fcl.ui.setting.SettingUI
import com.tungsten.fcl.ui.version.VersionUI
import com.tungsten.fclcore.util.Logging
import com.tungsten.fcllibrary.component.ui.FCLBaseUI
import com.tungsten.fcllibrary.component.ui.FCLCommonUI
import com.tungsten.fcllibrary.component.view.FCLUILayout
import java.util.logging.Level

class UIManager(val context: Context, val parent: FCLUILayout) {
    companion object {
        @JvmStatic
        lateinit var instance: UIManager
    }

    private var initialized = false
    // 阶段三 3.6：ComposeMainUI.USE_COMPOSE_MAIN_UI 为整体回滚开关（对齐 3.5 模式），
    // true = Compose 主页，false = 旧 MainUI（ui_main.xml）；类型放宽为 FCLCommonUI，
    // 既有反向调用点（switchUI / currentUI === 比较）签名不变；refreshSkin 契约由
    // AccountListItem 按实例类型分发（ComposeMainUI.refreshSkin / MainUI.refreshSkin）。
    val mainUI: FCLCommonUI by lazy {
        if (ComposeMainUI.USE_COMPOSE_MAIN_UI) {
            ComposeMainUI(context, parent)
        } else {
            MainUI(context, parent, R.layout.ui_main)
        }
    }
    // 阶段三 3.5：ComposeAccountUI.USE_COMPOSE_ACCOUNT_UI 为整体回滚开关（对齐 3.3/3.4 模式），
    // true = Compose 账户页，false = 旧 AccountUI（ui_account.xml）；类型放宽为 FCLCommonUI，
    // 既有反向调用点（refresh().start() / switchUI）签名不变。
    val accountUI: FCLCommonUI by lazy {
        if (ComposeAccountUI.USE_COMPOSE_ACCOUNT_UI) {
            ComposeAccountUI(context, parent)
        } else {
            AccountUI(context, parent, R.layout.ui_account)
        }
    }
    val versionUI: VersionUI by lazy { VersionUI(context, parent, R.layout.ui_version) }
    val manageUI: ManageUI by lazy { ManageUI(context, parent, R.layout.ui_manage) }
    val downloadUI: DownloadUI by lazy { DownloadUI(context, parent, R.layout.ui_download) }
    val controllerUI: ControllerUI by lazy { ControllerUI(context, parent, R.layout.ui_controller) }
    val multiplayerUI: MultiplayerUI by lazy { MultiplayerUI(context, parent, R.layout.ui_multiplayer) }
    val settingUI: SettingUI by lazy { SettingUI(context, parent, R.layout.ui_setting) }

    private val allUIList = mutableListOf<FCLBaseUI>()
    var currentUI: FCLBaseUI? = null

    fun init(listener: UIListener) {
        if (initialized) {
            Logging.LOG.log(Level.WARNING, "UIManager already initialized!")
            return
        }
        instance = this
        allUIList.add(mainUI)
        mainUI.addLoadingCallback {
            listener.onLoad()
        }
    }

    fun switchUI(ui: FCLCommonUI) {
        var isFirstAdd = false
        if (!allUIList.contains(ui)) {
            isFirstAdd = true
            allUIList.add(ui)
        }
        for (baseUI in allUIList) {
            if (ui === baseUI) {
                currentUI?.onStop()
                if (isFirstAdd) {
                    ui.addLoadingCallback {
                        ui.onStart()
                    }
                } else {
                    ui.onStart()
                }
                currentUI = ui
                break
            }
        }
    }

    fun registerDefaultBackEvent(runnable: Runnable?) {
        FCLBaseUI.setDefaultBackEvent(runnable)
    }

    fun onBackPressed() {
        currentUI?.onBackPressed()
    }

    fun onPause() {
        for (baseUI in allUIList) {
            baseUI.onPause()
        }
    }

    fun onResume() {
        for (baseUI in allUIList) {
            baseUI.onResume()
        }
    }
}