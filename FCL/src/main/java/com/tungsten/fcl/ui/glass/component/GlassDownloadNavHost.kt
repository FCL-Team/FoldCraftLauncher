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
    }
}
