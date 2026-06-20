package com.tungsten.fcl.ui.glass.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.tungsten.fcl.ui.glass.FCLGlassRoute
import com.tungsten.fcl.ui.glass.page.HomePage
import com.tungsten.fcl.ui.glass.page.AccountPage
import com.tungsten.fcl.ui.glass.page.SettingsPage
import com.tungsten.fcl.ui.glass.page.VersionsPage
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
            VersionsPage(backdrop = backdrop)
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
    }
}
