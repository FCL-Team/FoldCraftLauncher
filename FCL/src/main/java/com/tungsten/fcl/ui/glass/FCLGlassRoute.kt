package com.tungsten.fcl.ui.glass

import kotlinx.serialization.Serializable

sealed class FCLGlassRoute {
    @Serializable
    data object Home : FCLGlassRoute()

    @Serializable
    data object Versions : FCLGlassRoute()

    @Serializable
    data object Download : FCLGlassRoute()

    @Serializable
    data object Account : FCLGlassRoute()

    @Serializable
    data object Settings : FCLGlassRoute()

    @Serializable
    data object LauncherSettings : FCLGlassRoute()

    @Serializable
    data object JavaRuntime : FCLGlassRoute()

    @Serializable
    data object JvmArgs : FCLGlassRoute()

    @Serializable
    data class QuickInput(
        val title: String,
        val initialValue: String,
        val hint: String
    ) : FCLGlassRoute()

    @Serializable
    data class VersionSettings(
        val profileName: String,
        val version: String
    ) : FCLGlassRoute()

    @Serializable
    data class VersionInstall(
        val profileName: String,
        val version: String? = null
    ) : FCLGlassRoute()

    @Serializable
    data class VersionMods(
        val profileName: String,
        val version: String
    ) : FCLGlassRoute()

    @Serializable
    data class VersionWorlds(
        val profileName: String,
        val version: String
    ) : FCLGlassRoute()

    @Serializable
    data class VersionResourcePacks(
        val profileName: String,
        val version: String
    ) : FCLGlassRoute()

    @Serializable
    data class VersionShaderPacks(
        val profileName: String,
        val version: String
    ) : FCLGlassRoute()

    @Serializable
    data class ModInfo(
        val modFileName: String,
        val profileName: String,
        val version: String
    ) : FCLGlassRoute()
}
