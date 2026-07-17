package com.tungsten.fcl.ui.glass

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.hasRoute
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.tungsten.fcl.activity.GlassMainActivity
import com.tungsten.fcl.ui.glass.component.GlassBottomBar
import com.tungsten.fcl.ui.glass.component.GlassNavHost
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogHost
import com.tungsten.fcl.ui.glass.theme.GlassTheme
import com.tungsten.fcllibrary.component.theme.ThemePreset

@Composable
fun FCLGlassApp(
    initialPreset: ThemePreset = ThemePreset.DEFAULT
) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("launcher", Context.MODE_PRIVATE) }
    val presetOrdinal = remember { sharedPreferences.getInt("themePreset", initialPreset.ordinal) }
    var currentPreset by remember { mutableStateOf(ThemePreset.values()[presetOrdinal]) }

    val isDark = isSystemInDarkTheme()
    val backgroundColor = GlassTheme.backgroundColor()

    val onPresetChange: (ThemePreset) -> Unit = { preset ->
        currentPreset = preset
        sharedPreferences.edit().putInt("themePreset", preset.ordinal).apply()
    }

    val layout = remember(context) {
        (context as? GlassMainActivity)?.layout
    }

    CompositionLocalProvider(LocalFCLUILayout provides layout) {
        MaterialTheme(
            colorScheme = if (isDark) darkColorScheme() else lightColorScheme()
        ) {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val currentRoute = when {
                currentDestination?.hasRoute<FCLGlassRoute.Home>() == true -> FCLGlassRoute.Home
                currentDestination?.hasRoute<FCLGlassRoute.Versions>() == true -> FCLGlassRoute.Versions
                currentDestination?.hasRoute<FCLGlassRoute.Download>() == true -> FCLGlassRoute.Download
                currentDestination?.hasRoute<FCLGlassRoute.Account>() == true -> FCLGlassRoute.Account
                currentDestination?.hasRoute<FCLGlassRoute.Settings>() == true -> FCLGlassRoute.Settings
                else -> FCLGlassRoute.Home
            }

            val backdrop = rememberLayerBackdrop {
                drawRect(backgroundColor)
                drawContent()
            }

            Box(modifier = Modifier.fillMaxSize()) {
                GlassNavHost(
                    navController = navController,
                    backdrop = backdrop,
                    currentPreset = currentPreset,
                    onPresetChange = onPresetChange,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                )

                GlassBottomBar(
                    backdrop = backdrop,
                    preset = currentPreset,
                    currentRoute = currentRoute,
                    onRouteSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )

                GlassDialogHost(backdrop = backdrop)
            }
        }
    }
}
