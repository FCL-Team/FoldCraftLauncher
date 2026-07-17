package com.tungsten.fcl.ui.glass.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.glass.FCLGlassDownloadRoute
import com.tungsten.fcl.ui.glass.page.DownloadPage
import com.tungsten.fcl.ui.glass.page.download.InstallerVersionSelectPage
import com.tungsten.fcl.ui.glass.page.download.RemoteContentType
import com.tungsten.fcl.ui.glass.page.download.RemoteModCache
import com.tungsten.fcl.ui.glass.page.download.RemoteModInfoPage
import com.tungsten.fcl.ui.glass.page.download.RemoteModListPage
import com.tungsten.fcl.ui.glass.page.download.RemoteModVersionPage
import com.tungsten.fcl.ui.glass.page.download.VersionInstallInfoPage
import com.tungsten.fcl.ui.glass.page.download.VersionInstallListPage
import com.tungsten.fcl.ui.glass.page.download.VersionInstallState

@Composable
fun GlassDownloadNavHost(
    backdrop: Backdrop,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FCLGlassDownloadRoute.Home,
        modifier = modifier
    ) {
        composable<FCLGlassDownloadRoute.Home> {
            DownloadPage(
                backdrop = backdrop,
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable<FCLGlassDownloadRoute.VersionInstallList> {
            LaunchedEffect(Unit) {
                VersionInstallState.clear()
            }
            VersionInstallListPage(
                backdrop = backdrop,
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable<FCLGlassDownloadRoute.VersionInstallInfo> { backStackEntry ->
            val info = backStackEntry.toRoute<FCLGlassDownloadRoute.VersionInstallInfo>()
            VersionInstallInfoPage(
                backdrop = backdrop,
                gameVersion = info.gameVersion,
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable<FCLGlassDownloadRoute.InstallerVersionSelect> { backStackEntry ->
            val select = backStackEntry.toRoute<FCLGlassDownloadRoute.InstallerVersionSelect>()
            InstallerVersionSelectPage(
                backdrop = backdrop,
                gameVersion = select.gameVersion,
                libraryId = select.libraryId,
                onVersionSelected = { remoteVersion ->
                    VersionInstallState.selectedVersions[select.libraryId] = remoteVersion
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable<FCLGlassDownloadRoute.ModDownload> { entry ->
            val route = entry.toRoute<FCLGlassDownloadRoute.ModDownload>()
            RemoteModListPage(
                backdrop = backdrop,
                type = RemoteContentType.MOD,
                initialGameVersion = route.gameVersion,
                onNavigate = { navController.navigate(it) }
            )
        }
        composable<FCLGlassDownloadRoute.ModpackDownload> { entry ->
            val route = entry.toRoute<FCLGlassDownloadRoute.ModpackDownload>()
            RemoteModListPage(
                backdrop = backdrop,
                type = RemoteContentType.MODPACK,
                initialGameVersion = route.gameVersion,
                onNavigate = { navController.navigate(it) }
            )
        }
        composable<FCLGlassDownloadRoute.ResourcePackDownload> { entry ->
            val route = entry.toRoute<FCLGlassDownloadRoute.ResourcePackDownload>()
            RemoteModListPage(
                backdrop = backdrop,
                type = RemoteContentType.RESOURCE_PACK,
                initialGameVersion = route.gameVersion,
                onNavigate = { navController.navigate(it) }
            )
        }
        composable<FCLGlassDownloadRoute.ShaderPackDownload> { entry ->
            val route = entry.toRoute<FCLGlassDownloadRoute.ShaderPackDownload>()
            RemoteModListPage(
                backdrop = backdrop,
                type = RemoteContentType.SHADER_PACK,
                initialGameVersion = route.gameVersion,
                onNavigate = { navController.navigate(it) }
            )
        }
        composable<FCLGlassDownloadRoute.RemoteModInfo> { entry ->
            val info = entry.toRoute<FCLGlassDownloadRoute.RemoteModInfo>()
            val mod = RemoteModCache.get(info.cacheKey)
            if (mod != null) {
                RemoteModInfoPage(
                    backdrop = backdrop,
                    type = RemoteContentType.valueOf(info.typeName),
                    mod = mod,
                    gameVersion = info.gameVersion,
                    onNavigate = { navController.navigate(it) }
                )
            } else {
                // Fallback: show empty state with back button
                GlassEmptyState(
                    text = "Mod info unavailable",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        composable<FCLGlassDownloadRoute.RemoteModVersions> { entry ->
            val versions = entry.toRoute<FCLGlassDownloadRoute.RemoteModVersions>()
            val mod = RemoteModCache.get(versions.cacheKey)
            if (mod != null) {
                RemoteModVersionPage(
                    backdrop = backdrop,
                    type = RemoteContentType.valueOf(versions.typeName),
                    mod = mod,
                    targetGameVersion = versions.targetGameVersion,
                    onBack = { navController.popBackStack() }
                )
            } else {
                GlassEmptyState(
                    text = "Mod info unavailable",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
