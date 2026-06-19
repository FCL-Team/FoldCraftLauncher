package com.tungsten.fcl.ui.glass.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.FCLGlassRoute
import com.tungsten.fcllibrary.component.theme.ThemePreset

/**
 * 底部玻璃导航栏，使用 Backdrop 实现官方教程中的液态玻璃效果。
 *
 * @param backdrop 由 GlassNavHost 通过 layerBackdrop 捕获的背景 backdrop
 */
@Composable
fun GlassBottomBar(
    backdrop: Backdrop,
    preset: ThemePreset,
    currentRoute: FCLGlassRoute,
    onRouteSelected: (FCLGlassRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val tintColor = Color(preset.getColor())

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .safeContentPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Versions,
            NavigationItem.Download,
            NavigationItem.Manage,
            NavigationItem.Settings
        )

        items.forEach { item ->
            val selected = currentRoute == item.route
            GlassNavItem(
                backdrop = backdrop,
                item = item,
                selected = selected,
                tintColor = tintColor,
                onClick = { onRouteSelected(item.route) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun GlassNavItem(
    backdrop: Backdrop,
    item: NavigationItem,
    selected: Boolean,
    tintColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .clickable(onClick = onClick)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedCornerShape(50) },
                effects = {
                    vibrancy()
                    blur(4f.dp.toPx())
                    lens(16f.dp.toPx(), 32f.dp.toPx())
                },
                onDrawSurface = {
                    if (selected) {
                        drawRect(tintColor, blendMode = androidx.compose.ui.graphics.BlendMode.Hue)
                        drawRect(tintColor.copy(alpha = 0.75f))
                    } else {
                        drawRect(Color.White.copy(alpha = 0.5f))
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = item.iconRes),
            contentDescription = item.contentDescription,
            tint = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
    }
}

private sealed class NavigationItem(
    val route: FCLGlassRoute,
    val iconRes: Int,
    val contentDescription: String
) {
    data object Home : NavigationItem(FCLGlassRoute.Home, R.drawable.ic_baseline_home_24, "Home")
    data object Versions : NavigationItem(FCLGlassRoute.Versions, R.drawable.ic_baseline_videogame_asset_24, "Versions")
    data object Download : NavigationItem(FCLGlassRoute.Download, R.drawable.ic_baseline_cloud_download_24, "Download")
    data object Manage : NavigationItem(FCLGlassRoute.Manage, R.drawable.ic_baseline_tune_24, "Manage")
    data object Settings : NavigationItem(FCLGlassRoute.Settings, R.drawable.ic_baseline_settings_24, "Settings")
}
