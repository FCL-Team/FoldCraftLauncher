package com.tungsten.fcl.ui.glass

import kotlinx.serialization.Serializable

sealed class FCLGlassDownloadRoute {
    @Serializable
    data object Home : FCLGlassDownloadRoute()

    @Serializable
    data object VersionInstallList : FCLGlassDownloadRoute()

    @Serializable
    data class VersionInstallInfo(val gameVersion: String) : FCLGlassDownloadRoute()

    @Serializable
    data class InstallerVersionSelect(
        val gameVersion: String,
        val libraryId: String
    ) : FCLGlassDownloadRoute()
}
