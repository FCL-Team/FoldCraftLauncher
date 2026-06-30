package com.tungsten.fcl.ui.glass.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.FCLGlassDownloadRoute
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassSectionTitle
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcllibrary.component.theme.ThemeEngine

@Composable
fun DownloadPage(
    backdrop: Backdrop,
    onNavigate: (FCLGlassDownloadRoute) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    val categories = listOf(
        DownloadCategory(
            title = stringResource(R.string.install_installer_game),
            iconRes = R.drawable.ic_cube,
            route = FCLGlassDownloadRoute.VersionInstallList
        ),
        DownloadCategory(
            title = stringResource(R.string.mods),
            iconRes = R.drawable.ic_outline_extension_24,
            route = FCLGlassDownloadRoute.ModDownload()
        ),
        DownloadCategory(
            title = stringResource(R.string.modpack),
            iconRes = R.drawable.ic_baseline_application_24,
            route = FCLGlassDownloadRoute.ModpackDownload()
        ),
        DownloadCategory(
            title = stringResource(R.string.resourcepack),
            iconRes = R.drawable.ic_baseline_texture_24,
            route = FCLGlassDownloadRoute.ResourcePackDownload()
        ),
        DownloadCategory(
            title = stringResource(R.string.shaderpack),
            iconRes = R.drawable.ic_baseline_tune_24,
            route = FCLGlassDownloadRoute.ShaderPackDownload()
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GlassTopBar(title = stringResource(R.string.download))

        GlassSectionTitle(text = "Categories")

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(360.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
                    backdrop = backdrop,
                    category = category,
                    tint = tintColor,
                    onClick = { onNavigate(category.route) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private data class DownloadCategory(
    val title: String,
    val iconRes: Int,
    val route: FCLGlassDownloadRoute
)

@Composable
private fun CategoryCard(
    backdrop: Backdrop,
    category: DownloadCategory,
    tint: Color,
    onClick: () -> Unit
) {
    GlassCard(
        backdrop = backdrop,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable(onClick = onClick),
        tint = tint
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = category.iconRes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = category.title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}
