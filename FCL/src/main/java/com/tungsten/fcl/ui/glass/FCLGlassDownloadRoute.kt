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

    @Serializable
    data class ModDownload(val gameVersion: String = "") : FCLGlassDownloadRoute()

    @Serializable
    data class ModpackDownload(val gameVersion: String = "") : FCLGlassDownloadRoute()

    @Serializable
    data class ResourcePackDownload(val gameVersion: String = "") : FCLGlassDownloadRoute()

    @Serializable
    data class ShaderPackDownload(val gameVersion: String = "") : FCLGlassDownloadRoute()

    @Serializable
    data class RemoteModInfo(
        val typeName: String,
        val modSlug: String,
        val modTitle: String,
        val gameVersion: String,
        val cacheKey: String
    ) : FCLGlassDownloadRoute()

    @Serializable
    data class RemoteModVersions(
        val typeName: String,
        val modSlug: String,
        val targetGameVersion: String,
        val cacheKey: String
    ) : FCLGlassDownloadRoute()
}
