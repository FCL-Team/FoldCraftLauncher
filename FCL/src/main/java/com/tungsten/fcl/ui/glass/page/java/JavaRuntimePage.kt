package com.tungsten.fcl.ui.glass.page.java

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassSectionTitle
import com.tungsten.fcl.ui.glass.component.GlassTextButton
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager

@Composable
fun JavaRuntimePage(
    backdrop: Backdrop,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state = remember { JavaRuntimeStateHolder() }

    DisposableEffect(Unit) {
        state.load()
        onDispose { }
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(
            title = "Java Runtime",
            onBack = onBack
        )

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { GlassSectionTitle(text = "Installed") }

                items(
                    items = state.javaList,
                    key = { it.name }
                ) { item ->
                    JavaRuntimeCard(
                        backdrop = backdrop,
                        item = item,
                        onDelete = {
                            if (!item.isAuto) {
                                GlassDialogManager.showAlert(
                                    title = "Remove Java Runtime",
                                    message = "Are you sure you want to remove ${item.name}?",
                                    confirmText = "Remove",
                                    dismissText = "Cancel",
                                    onConfirm = { state.remove(item) }
                                ) {}
                            }
                        }
                    )
                }

                item { GlassSectionTitle(text = "Install Java") }
                item {
                    GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("jre8", "jre17", "jre21", "jre25").forEach { version ->
                                GlassTextButton(
                                    backdrop = backdrop,
                                    text = version.uppercase(),
                                    onClick = { state.install(context, version) },
                                    modifier = Modifier.weight(1f),
                                    enabled = !state.isLoading
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (!state.isLoading && state.javaList.isEmpty()) {
                GlassEmptyState(
                    text = "No Java runtimes found",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun JavaRuntimeCard(
    backdrop: Backdrop,
    item: JavaRuntimeItem,
    onDelete: () -> Unit
) {
    GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.version,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                if (item.path.isNotEmpty()) {
                    Text(
                        text = item.path,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            if (!item.isAuto) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
