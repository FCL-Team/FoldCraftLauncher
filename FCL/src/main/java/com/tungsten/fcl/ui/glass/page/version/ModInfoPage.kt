package com.tungsten.fcl.ui.glass.page.version

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcl.util.AndroidUtils

@Composable
fun ModInfoPage(
    backdrop: Backdrop,
    profile: Profile,
    version: String,
    modFileName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state = remember(profile, version, modFileName) {
        ModInfoStateHolder(profile, version, modFileName)
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(
            title = "Mod Info",
            onBack = onBack
        )

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                }
            }
            state.mod == null -> {
                GlassEmptyState(
                    text = state.errorMessage ?: stringResource(R.string.message_unknown),
                    modifier = Modifier.weight(1f)
                )
            }
            else -> {
                val mod = state.mod!!
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        GlassCard(
                            backdrop = backdrop,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (state.icon != null) {
                                    Image(
                                        bitmap = state.icon!!.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier.size(80.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        modifier = Modifier.size(80.dp),
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = mod.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (mod.version.isNotBlank()) {
                                    Text(
                                        text = mod.version,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                                val loader = state.loaderDisplayName()
                                if (loader.isNotBlank()) {
                                    Text(
                                        text = loader,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    item {
                        GlassCard(
                            backdrop = backdrop,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                InfoRow(label = stringResource(R.string.mods_name), value = mod.name)
                                if (!mod.version.isNullOrBlank()) {
                                    InfoRow(label = stringResource(R.string.version), value = mod.version)
                                }
                                InfoRow(label = stringResource(R.string.archive_game_version), value = mod.gameVersion ?: "")
                                InfoRow(label = stringResource(R.string.archive_author), value = mod.authors ?: "")
                                InfoRow(label = "File", value = mod.fileName)
                            }
                        }
                    }

                    item {
                        GlassCard(
                            backdrop = backdrop,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = mod.description.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    val url = mod.url
                    if (!url.isNullOrBlank()) {
                        item {
                            GlassButton(
                                backdrop = backdrop,
                                onClick = { AndroidUtils.openLink(context, url) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.mods_url),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    if (value.isBlank()) return
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}