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
}
