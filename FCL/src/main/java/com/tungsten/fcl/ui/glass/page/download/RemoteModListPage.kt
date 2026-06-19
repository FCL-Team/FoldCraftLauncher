package com.tungsten.fcl.ui.glass.page.download

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcl.ui.glass.FCLGlassDownloadRoute
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassGameVersionFilter
import com.tungsten.fcl.ui.glass.component.GlassModListItem
import com.tungsten.fcl.ui.glass.component.GlassPagination
import com.tungsten.fcl.ui.glass.component.GlassSearchBar
import com.tungsten.fcl.ui.glass.component.GlassSourceSelector
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import java.util.stream.Collectors

@Composable
fun RemoteModListPage(
    backdrop: Backdrop,
    type: RemoteContentType,
    initialGameVersion: String,
    onNavigate: (FCLGlassDownloadRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    var query by remember { mutableStateOf("") }
    var gameVersion by remember { mutableStateOf(initialGameVersion) }
    var source by remember { mutableStateOf(type.defaultSource(context)) }
    var sort by remember { mutableStateOf(type.sortType(source, context)) }
    var currentPage by remember { mutableIntStateOf(0) }
    var totalPages by remember { mutableIntStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var results by remember { mutableStateOf<List<RemoteMod>>(emptyList()) }

    fun performSearch() {
        isLoading = true
        error = null
        val repository = type.getRepository(source, context)
        val downloadProvider = DownloadProviders.getDownloadProvider()
        Task.supplyAsync {
            val searchResult = repository.search(
                downloadProvider,
                gameVersion,
                null,
                currentPage,
                50,
                query,
                sort,
                RemoteModRepository.SortOrder.DESC
            )
            val list = searchResult.results.collect(Collectors.toList())
            searchResult.totalPages to list
        }.whenComplete(Schedulers.androidUIThread()) { pair, exception ->
            isLoading = false
            if (exception == null && pair != null) {
                totalPages = pair.first.coerceAtLeast(1)
                results = pair.second
            } else {
                error = exception?.message ?: context.getString(R.string.download_failed_empty)
            }
        }.start()
    }

    LaunchedEffect(Unit) {
        performSearch()
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(title = stringResource(type.titleRes()))

        GlassSourceSelector(
            backdrop = backdrop,
            sources = type.supportedSources(context),
            selected = source,
            onSelect = {
                source = it
                sort = type.sortType(it, context)
                currentPage = 0
                performSearch()
            },
            tint = tintColor,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            GlassGameVersionFilter(
                backdrop = backdrop,
                gameVersion = gameVersion,
                onGameVersionChange = { gameVersion = it },
                modifier = Modifier.weight(1f)
            )
            GlassButton(
                backdrop = backdrop,
                onClick = { performSearch() },
                tint = tintColor
            ) {
                Text(text = stringResource(R.string.search), color = Color.White)
            }
        }

        GlassSearchBar(
            backdrop = backdrop,
            query = query,
            onQueryChange = { query = it },
            hint = stringResource(R.string.search),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        if (isLoading) {
            GlassEmptyState(
                text = "Loading...",
                modifier = Modifier.weight(1f)
            )
        } else if (error != null) {
            GlassEmptyState(
                text = error ?: context.getString(R.string.download_failed_empty),
                modifier = Modifier.weight(1f)
            )
        } else if (results.isEmpty()) {
            GlassEmptyState(
                text = stringResource(R.string.download_failed_empty),
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                items(results, key = { it.slug }) { mod ->
                    GlassModListItem(
                        backdrop = backdrop,
                        mod = mod,
                        onClick = {
                            val cacheKey = "${type.name}_${mod.slug}"
                            RemoteModCache.put(cacheKey, mod)
                            onNavigate(
                                FCLGlassDownloadRoute.RemoteModInfo(
                                    typeName = type.name,
                                    modSlug = mod.slug,
                                    modTitle = mod.title,
                                    gameVersion = gameVersion,
                                    cacheKey = cacheKey
                                )
                            )
                        }
                    )
                }
            }
        }

        GlassPagination(
            backdrop = backdrop,
            currentPage = currentPage,
            totalPages = totalPages,
            onPageChange = { page ->
                currentPage = page
                performSearch()
            },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            tint = tintColor
        )
    }
}
