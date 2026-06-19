package com.tungsten.fcl.ui.glass.page.download

import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.FCLGlassDownloadRoute
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassSearchBar
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.versioning.VersionNumber
import java.util.Collections
import java.util.stream.Collectors

@Composable
fun RemoteModInfoPage(
    backdrop: Backdrop,
    type: RemoteContentType,
    mod: RemoteMod,
    gameVersion: String,
    onNavigate: (FCLGlassDownloadRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var versions by remember { mutableStateOf<List<RemoteMod.Version>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var versionFilter by remember { mutableStateOf("") }

    LaunchedEffect(mod, type) {
        isLoading = true
        error = null
        val repository = type.getRepository(type.defaultSource(context), context)
        Task.supplyAsync {
            mod.getData().loadVersions(repository).collect(Collectors.toList())
        }.whenComplete(Schedulers.androidUIThread()) { list, exception ->
            isLoading = false
            if (exception == null && list != null) {
                versions = list
            } else {
                error = exception?.message ?: context.getString(R.string.download_failed_empty)
            }
        }.start()
    }

    val gameVersions = remember(versions, versionFilter) {
        versions
            .flatMap { it.getGameVersions() }
            .distinct()
            .filter { it.contains(versionFilter, ignoreCase = true) }
            .sortedWith(Collections.reverseOrder { a, b ->
                try {
                    VersionNumber.asVersion(a).compareTo(VersionNumber.asVersion(b))
                } catch (e: Exception) {
                    a.compareTo(b)
                }
            })
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(title = mod.getTitle())

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AndroidView(
                    factory = { ctx ->
                        ImageView(ctx).apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                    },
                    update = { imageView ->
                        Glide.with(imageView.context)
                            .load(mod.getIconUrl())
                            .placeholder(R.drawable.ic_outline_extension_24)
                            .into(imageView)
                    },
                    modifier = Modifier.size(72.dp)
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = mod.getTitle(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = mod.getDescription(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    val categories = mod.getCategories()
                    if (categories.isNotEmpty()) {
                        Text(
                            text = categories.joinToString(", "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        GlassSearchBar(
            backdrop = backdrop,
            query = versionFilter,
            onQueryChange = { versionFilter = it },
            hint = stringResource(R.string.archive_game_version),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        if (error != null) {
            GlassEmptyState(text = error ?: stringResource(R.string.download_failed_empty), modifier = Modifier.weight(1f))
        } else if (gameVersions.isEmpty() && !isLoading) {
            GlassEmptyState(text = stringResource(R.string.download_failed_empty), modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                items(gameVersions) { version ->
                    GlassCard(
                        backdrop = backdrop,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .clickable {
                                    onNavigate(
                                        FCLGlassDownloadRoute.RemoteModVersions(
                                            typeName = type.name,
                                            modSlug = mod.getSlug(),
                                            targetGameVersion = version,
                                            cacheKey = "${type.name}_${mod.getSlug()}"
                                        )
                                    )
                                }
                                .padding(16.dp)
                        ) {
                            Text(
                                text = version,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
