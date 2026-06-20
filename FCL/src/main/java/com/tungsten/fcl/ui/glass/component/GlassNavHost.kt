package com.tungsten.fcl.ui.glass.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.glass.FCLGlassRoute
import com.tungsten.fcl.ui.glass.page.HomePage
import com.tungsten.fcl.ui.glass.page.AccountPage
import com.tungsten.fcl.ui.glass.page.SettingsPage
import com.tungsten.fcl.ui.glass.page.VersionsPage
import com.tungsten.fcl.ui.glass.page.download.VersionInstallPage
import com.tungsten.fcl.ui.glass.page.version.VersionSettingsPage
import com.tungsten.fcllibrary.component.theme.ThemePreset

@Composable
fun GlassNavHost(
    navController: NavHostController,
    backdrop: Backdrop,
    currentPreset: ThemePreset,
    onPresetChange: (ThemePreset) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FCLGlassRoute.Home,
        modifier = modifier.layerBackdrop(backdrop)
    ) {
        composable<FCLGlassRoute.Home> {
            HomePage(backdrop = backdrop)
        }
        composable<FCLGlassRoute.Versions> {
            VersionsPage(
                backdrop = backdrop,
                navController = navController
            )
        }
        composable<FCLGlassRoute.Download> {
            GlassDownloadNavHost(backdrop = backdrop)
        }
        composable<FCLGlassRoute.Account> {
            AccountPage(backdrop = backdrop)
        }
        composable<FCLGlassRoute.Settings> {
            SettingsPage(
                backdrop = backdrop,
                currentPreset = currentPreset,
                onPresetChange = onPresetChange
            )
        }
        composable<FCLGlassRoute.LauncherSettings> {
            GlassPlaceholderPage(backdrop = backdrop, title = "LauncherSettings")
        }
        composable<FCLGlassRoute.JavaRuntime> {
            GlassPlaceholderPage(backdrop = backdrop, title = "JavaRuntime")
        }
        composable<FCLGlassRoute.JvmArgs> {
            GlassPlaceholderPage(backdrop = backdrop, title = "JvmArgs")
        }
        composable<FCLGlassRoute.QuickInput> {
            GlassPlaceholderPage(backdrop = backdrop, title = "QuickInput")
        }
        composable<FCLGlassRoute.VersionSettings> { backStackEntry ->
            val route = backStackEntry.toRoute<FCLGlassRoute.VersionSettings>()
            val profile = Profiles.profiles.find { it.name == route.profileName } ?: return@composable
            VersionSettingsPage(
                backdrop = backdrop,
                profile = profile,
                version = route.version,
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }
        composable<FCLGlassRoute.VersionInstall> { backStackEntry ->
            val route = backStackEntry.toRoute<FCLGlassRoute.VersionInstall>()
            val profile = Profiles.profiles.find { it.name == route.profileName } ?: return@composable
            VersionInstallPage(
                backdrop = backdrop,
                profile = profile,
                parentVersion = route.version,
                onBack = { navController.popBackStack() },
                onComplete = { navController.popBackStack() }
            )
        }
        composable<FCLGlassRoute.VersionMods> {
            GlassPlaceholderPage(backdrop = backdrop, title = "VersionMods")
        }
        composable<FCLGlassRoute.VersionWorlds> {
            GlassPlaceholderPage(backdrop = backdrop, title = "VersionWorlds")
        }
        composable<FCLGlassRoute.VersionResourcePacks> {
            GlassPlaceholderPage(backdrop = backdrop, title = "VersionResourcePacks")
        }
        composable<FCLGlassRoute.VersionShaderPacks> {
            GlassPlaceholderPage(backdrop = backdrop, title = "VersionShaderPacks")
        }
        composable<FCLGlassRoute.ModInfo> {
            GlassPlaceholderPage(backdrop = backdrop, title = "ModInfo")
        }
    }
}
