package com.tungsten.fcl.ui.glass.page.launcher

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassCheckbox
import com.tungsten.fcl.ui.glass.component.GlassChip
import com.tungsten.fcl.ui.glass.component.GlassSectionTitle
import com.tungsten.fcl.ui.glass.component.GlassTextButton
import com.tungsten.fcl.ui.glass.component.GlassTextField
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fclcore.task.FetchTask
import com.tungsten.fcllibrary.component.theme.ThemeEngine

@Composable
fun LauncherSettingPage(
    backdrop: Backdrop,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state = remember { LauncherSettingStateHolder(context) }
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    val window = (context as? Activity)?.window

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(
            title = stringResource(R.string.settings_launcher),
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { GlassSectionTitle(text = stringResource(R.string.settings_launcher_language)) }
            item {
                GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(LANGUAGE_OPTIONS.size) { index ->
                            val (langIndex, resId) = LANGUAGE_OPTIONS[index]
                            GlassChip(
                                backdrop = backdrop,
                                text = stringResource(resId),
                                selected = state.language == langIndex,
                                onClick = { state.language = langIndex },
                                tint = tintColor
                            )
                        }
                    }
                }
            }

            item { GlassSectionTitle(text = stringResource(R.string.settings_launcher)) }
            item {
                GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        GlassCheckbox(
                            backdrop = backdrop,
                            checked = state.autoExitLauncher,
                            onCheckedChange = { state.autoExitLauncher = it },
                            label = stringResource(R.string.settings_launcher_exit_after_launching)
                        )
                        GlassCheckbox(
                            backdrop = backdrop,
                            checked = state.ignoreNotch,
                            onCheckedChange = {
                                state.ignoreNotch = it
                                window?.let { w ->
                                    ThemeEngine.getInstance().applyAndSave(context, w, it)
                                }
                            },
                            label = stringResource(R.string.settings_launcher_ignore_notch),
                            tint = tintColor
                        )
                        GlassCheckbox(
                            backdrop = backdrop,
                            checked = state.disableFullscreenInput,
                            onCheckedChange = { state.disableFullscreenInput = it },
                            label = stringResource(R.string.settings_disable_fullscreen_input)
                        )
                    }
                }
            }

            item { GlassSectionTitle(text = stringResource(R.string.settings_launcher_download)) }
            item {
                GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        GlassCheckbox(
                            backdrop = backdrop,
                            checked = state.autoChooseDownloadType,
                            onCheckedChange = { state.autoChooseDownloadType = it },
                            label = stringResource(R.string.settings_launcher_download_source_auto)
                        )

                        if (state.autoChooseDownloadType) {
                            Text(
                                text = stringResource(R.string.settings_launcher_download_source),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                VERSION_LIST_SOURCE_OPTIONS.forEach { (value, resId) ->
                                    GlassChip(
                                        backdrop = backdrop,
                                        text = stringResource(resId),
                                        selected = state.versionListSource == value,
                                        onClick = { state.versionListSource = value },
                                        tint = tintColor
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = stringResource(R.string.settings_launcher_download_source),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                DOWNLOAD_TYPE_OPTIONS.forEach { (value, resId) ->
                                    GlassChip(
                                        backdrop = backdrop,
                                        text = stringResource(resId),
                                        selected = state.downloadType == value,
                                        onClick = { state.downloadType = value },
                                        tint = tintColor
                                    )
                                }
                            }
                        }

                        GlassCheckbox(
                            backdrop = backdrop,
                            checked = state.autoDownloadThreads,
                            onCheckedChange = {
                                state.autoDownloadThreads = it
                                if (it) {
                                    state.downloadThreads = FetchTask.DEFAULT_CONCURRENCY
                                }
                            },
                            label = stringResource(R.string.settings_launcher_download_threads_auto)
                        )

                        if (!state.autoDownloadThreads) {
                            GlassTextField(
                                backdrop = backdrop,
                                value = state.downloadThreads.toString(),
                                onValueChange = { text ->
                                    text.toIntOrNull()?.let { state.downloadThreads = it }
                                },
                                hint = stringResource(R.string.settings_launcher_download_threads)
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                GlassTextButton(
                    backdrop = backdrop,
                    text = "Save",
                    onClick = {
                        state.save(context)
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    tint = tintColor
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

private val LANGUAGE_OPTIONS = listOf(
    0 to R.string.settings_launcher_language_system,
    1 to R.string.settings_launcher_language_english,
    2 to R.string.settings_launcher_language_simplified_chinese,
    3 to R.string.settings_launcher_language_russian,
    4 to R.string.settings_launcher_language_brazilian_portuguese,
    5 to R.string.settings_launcher_language_persian,
    6 to R.string.settings_launcher_language_ukrainian,
    7 to R.string.settings_launcher_language_german,
    8 to R.string.settings_launcher_language_traditional_chinese_hk
)

private val DOWNLOAD_TYPE_OPTIONS = listOf(
    "mojang" to R.string.download_provider_mojang,
    "bmclapi" to R.string.download_provider_bmclapi
)

private val VERSION_LIST_SOURCE_OPTIONS = listOf(
    "official" to R.string.download_provider_official,
    "balanced" to R.string.download_provider_balanced,
    "mirror" to R.string.download_provider_mirror
)
