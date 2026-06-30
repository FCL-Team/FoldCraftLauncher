package com.tungsten.fcl.ui.glass.page.download

import androidx.compose.runtime.mutableStateMapOf
import com.tungsten.fclcore.download.RemoteVersion

object VersionInstallState {
    val selectedVersions = mutableStateMapOf<String, RemoteVersion>()

    fun clear() {
        selectedVersions.clear()
    }
}
