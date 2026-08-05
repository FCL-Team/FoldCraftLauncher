package com.tungsten.fcl.ui.glass.page.launcher

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.LocaleUtils

class LauncherSettingStateHolder(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
    private val config = ConfigHolder.config()

    var language by mutableIntStateOf(LocaleUtils.getLanguage(context))
    var autoExitLauncher by mutableStateOf(sharedPreferences.getBoolean("autoExitLauncher", false))
    var ignoreNotch by mutableStateOf(ThemeEngine.getInstance().getTheme().isFullscreen())
    var disableFullscreenInput by mutableStateOf(sharedPreferences.getBoolean("disableFullscreenInput", true))
    var autoChooseDownloadType by mutableStateOf(config.isAutoChooseDownloadType())
    var downloadType by mutableStateOf(config.getDownloadType())
    var versionListSource by mutableStateOf(config.getVersionListSource())
    var autoDownloadThreads by mutableStateOf(config.getAutoDownloadThreads())
    var downloadThreads by mutableIntStateOf(config.getDownloadThreads())

    fun save(context: Context) {
        LocaleUtils.changeLanguage(context, language)

        sharedPreferences.edit()
            .putBoolean("autoExitLauncher", autoExitLauncher)
            .putBoolean("disableFullscreenInput", disableFullscreenInput)
            .apply()

        config.setAutoChooseDownloadType(autoChooseDownloadType)
        config.setDownloadType(downloadType)
        config.setVersionListSource(versionListSource)
        config.setAutoDownloadThreads(autoDownloadThreads)
        config.setDownloadThreads(downloadThreads)

        ThemeEngine.getInstance().applyAndSave(context, null, ignoreNotch)
    }
}
