package com.tungsten.fcl.ui.glass.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.tungsten.fcl.ui.glass.page.QuickInputPage
import com.tungsten.fcl.ui.glass.page.SettingsPage
import com.tungsten.fcl.ui.glass.page.VersionsPage
import com.tungsten.fcl.ui.glass.page.download.VersionInstallPage
import com.tungsten.fcl.ui.glass.page.java.JavaRuntimePage
import com.tungsten.fcl.ui.glass.page.java.JvmArgsPage
import com.tungsten.fcl.ui.glass.page.launcher.LauncherSettingPage
import com.tungsten.fcl.ui.glass.page.version.ModInfoPage
import com.tungsten.fcl.ui.glass.page.version.VersionModListPage
import com.tungsten.fcl.ui.glass.page.version.VersionPackListPage
import com.tungsten.fcl.ui.glass.page.version.VersionSettingsPage
import com.tungsten.fcl.ui.glass.page.version.VersionWorldListPage
import com.tungsten.fcllibrary.component.theme.ThemePreset
import com.tungsten.fcl.R

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
                navController = navController,
                currentPreset = currentPreset,
                onPresetChange = onPresetChange
            )
        }
        composable<FCLGlassRoute.LauncherSettings> {
            LauncherSettingPage(
                backdrop = backdrop,
                onBack = { navController.popBackStack() }
            )
        }
        composable<FCLGlassRoute.JavaRuntime> {
            JavaRuntimePage(
                backdrop = backdrop,
                onBack = { navController.popBackStack() }
            )
        }
        composable<FCLGlassRoute.JvmArgs> {
            JvmArgsPage(
                backdrop = backdrop,
                onBack = { navController.popBackStack() }
            )
        }
        composable<FCLGlassRoute.QuickInput> { backStackEntry ->
            val route = backStackEntry.toRoute<FCLGlassRoute.QuickInput>()
            QuickInputPage(
                backdrop = backdrop,
                title = route.title,
                initialValue = route.initialValue,
                hint = route.hint,
                onConfirm = { /* consumers should navigate with result or use dialog */ },
                onBack = { navController.popBackStack() }
            )
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
        composable<FCLGlassRoute.VersionMods> { backStackEntry ->
            val route = backStackEntry.toRoute<FCLGlassRoute.VersionMods>()
            val profile = Profiles.profiles.find { it.name == route.profileName } ?: return@composable
            VersionModListPage(
                backdrop = backdrop,
                profile = profile,
                version = route.version,
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }
        composable<FCLGlassRoute.VersionWorlds> { backStackEntry ->
            val route = backStackEntry.toRoute<FCLGlassRoute.VersionWorlds>()
            val profile = Profiles.profiles.find { it.name == route.profileName } ?: return@composable
            VersionWorldListPage(
                backdrop = backdrop,
                profile = profile,
                version = route.version,
                onBack = { navController.popBackStack() }
            )
        }
        composable<FCLGlassRoute.VersionResourcePacks> { backStackEntry ->
            val route = backStackEntry.toRoute<FCLGlassRoute.VersionResourcePacks>()
            val profile = Profiles.profiles.find { it.name == route.profileName } ?: return@composable
            VersionPackListPage(
                backdrop = backdrop,
                profile = profile,
                version = route.version,
                folderName = "resourcepacks",
                title = stringResource(R.string.resourcepack),
                onBack = { navController.popBackStack() }
            )
        }
        composable<FCLGlassRoute.VersionShaderPacks> { backStackEntry ->
            val route = backStackEntry.toRoute<FCLGlassRoute.VersionShaderPacks>()
            val profile = Profiles.profiles.find { it.name == route.profileName } ?: return@composable
            VersionPackListPage(
                backdrop = backdrop,
                profile = profile,
                version = route.version,
                folderName = "shaderpacks",
                title = stringResource(R.string.shaderpack),
                onBack = { navController.popBackStack() }
            )
        }
        composable<FCLGlassRoute.ModInfo> { backStackEntry ->
            val route = backStackEntry.toRoute<FCLGlassRoute.ModInfo>()
            val profile = Profiles.profiles.find { it.name == route.profileName } ?: return@composable
            ModInfoPage(
                backdrop = backdrop,
                profile = profile,
                version = route.version,
                modFileName = route.modFileName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
