package com.tungsten.fcl.ui.glass.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.tungsten.fcl.ui.glass.FCLGlassRoute
import com.tungsten.fcl.ui.glass.page.DownloadPage
import com.tungsten.fcl.ui.glass.page.HomePage
import com.tungsten.fcl.ui.glass.page.ManagePage
import com.tungsten.fcl.ui.glass.page.SettingsPage
import com.tungsten.fcl.ui.glass.page.VersionsPage

/**
 * 玻璃导航容器，管理一级页面路由。
 * 整个导航 Host 被 layerBackdrop 捕获，供底部玻璃导航栏折射背景。
 */
@Composable
fun GlassNavHost(
    navController: NavHostController,
    backdrop: Backdrop,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FCLGlassRoute.Home,
        modifier = modifier
            .layerBackdrop(backdrop)
    ) {
        composable<FCLGlassRoute.Home> {
            HomePage()
        }
        composable<FCLGlassRoute.Versions> {
            VersionsPage()
        }
        composable<FCLGlassRoute.Download> {
            DownloadPage()
        }
        composable<FCLGlassRoute.Manage> {
            ManagePage()
        }
        composable<FCLGlassRoute.Settings> {
            SettingsPage()
        }
    }
}
