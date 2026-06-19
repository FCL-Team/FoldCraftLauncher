package com.tungsten.fcl.ui.glass.page

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassChip
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassSearchBar
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcl.ui.glass.page.versions.VersionCategory
import com.tungsten.fcl.ui.glass.page.versions.VersionListStateHolder
import com.tungsten.fcl.ui.version.VersionListItem
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener

@Composable
fun VersionsPage(
    backdrop: Backdrop,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state = remember { VersionListStateHolder() }
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())

    LaunchedEffect(state.selectedProfile) {
        state.loadVersions(context, state.selectedProfile)
    }

    LaunchedEffect(Unit) {
        Profiles.registerVersionsListener { profile ->
            if (profile == state.selectedProfile) {
                launch {
                    state.loadVersions(context, profile)
                }
            }
        }
    }

    val profiles = remember { Profiles.profiles }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(title = stringResource(R.string.version))

        ProfileSelector(
            backdrop = backdrop,
            profiles = profiles,
            selected = state.selectedProfile,
            onSelect = {
                Profiles.setSelectedProfile(it)
                state.selectedProfile = it
                state.refresh()
            },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        GlassSearchBar(
            backdrop = backdrop,
            query = state.query,
            onQueryChange = { state.query = it },
            hint = stringResource(R.string.search),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        FilterChips(
            backdrop = backdrop,
            selected = state.selectedCategory,
            onSelect = { state.selectedCategory = it },
            tint = tintColor,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        val filtered = state.filteredVersions()
        if (filtered.isEmpty() && !state.isLoading) {
            GlassEmptyState(
                text = stringResource(R.string.download_failed_empty),
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                items(filtered, key = { it.version }) { item ->
                    VersionItemCard(
                        backdrop = backdrop,
                        item = item,
                        tint = tintColor
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileSelector(
    backdrop: Backdrop,
    profiles: List<Profile>,
    selected: Profile,
    onSelect: (Profile) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(backdrop = backdrop, modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            profiles.forEach { profile ->
                GlassChip(
                    backdrop = backdrop,
                    text = profile.name,
                    selected = profile == selected,
                    onClick = { onSelect(profile) }
                )
            }
        }
    }
}

@Composable
private fun FilterChips(
    backdrop: Backdrop,
    selected: VersionCategory,
    onSelect: (VersionCategory) -> Unit,
    tint: Color,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        VersionCategory.ALL to R.string.curse_category_0,
        VersionCategory.FABRIC to R.string.install_installer_fabric,
        VersionCategory.FORGE to R.string.install_installer_forge,
        VersionCategory.NEOFORGE to R.string.install_installer_neoforge,
        VersionCategory.OTHER to R.string.control_download_device_other
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        categories.forEach { (cat, res) ->
            GlassChip(
                backdrop = backdrop,
                text = stringResource(res),
                selected = selected == cat,
                onClick = { onSelect(cat) },
                tint = tint
            )
        }
    }
}

@Composable
private fun VersionItemCard(
    backdrop: Backdrop,
    item: VersionListItem,
    tint: Color
) {
    val context = LocalContext.current
    val selected by observeBooleanProperty(item.selectedProperty())
    GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val bitmap = item.drawable?.toBitmap()
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = item.version,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.libraries,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                if (item.tag != null) {
                    Text(
                        text = item.tag,
                        style = MaterialTheme.typography.labelSmall,
                        color = tint.copy(alpha = 0.9f)
                    )
                }
            }
            RadioButton(
                selected = selected,
                onClick = { item.profile.selectedVersion = item.version }
            )
            GlassButton(
                backdrop = backdrop,
                onClick = {
                    Versions.launch(context, item.profile, item.version)
                },
                tint = tint
            ) {
                Text(
                    text = stringResource(R.string.launch),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun observeBooleanProperty(property: BooleanProperty): Boolean {
    var value by remember(property) { mutableStateOf(property.get()) }
    DisposableEffect(property) {
        val listener = ChangeListener<Boolean> { _, _, newValue -> value = newValue }
        property.addListener(listener)
        onDispose { property.removeListener(listener) }
    }
    return value
}
