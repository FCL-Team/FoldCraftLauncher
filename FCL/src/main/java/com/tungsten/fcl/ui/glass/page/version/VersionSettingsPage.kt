package com.tungsten.fcl.ui.glass.page.version

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kyant.backdrop.Backdrop
import com.mio.JavaManager
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.glass.FCLGlassRoute
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassCheckbox
import com.tungsten.fcl.ui.glass.component.GlassSectionTitle
import com.tungsten.fcl.ui.glass.component.GlassSourceSelector
import com.tungsten.fcl.ui.glass.component.GlassTextButton
import com.tungsten.fcl.ui.glass.component.GlassTextField
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcllibrary.component.theme.ThemeEngine

@Composable
fun VersionSettingsPage(
    backdrop: Backdrop,
    profile: Profile,
    version: String,
    navController: NavController,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state = remember(profile, version) { VersionSettingsStateHolder(profile, version) }
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(
            title = version,
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { GlassSectionTitle(text = stringResource(R.string.settings_memory)) }
            item {
                GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        GlassCheckbox(
                            backdrop = backdrop,
                            checked = state.autoMemory,
                            onCheckedChange = { state.autoMemory = it },
                            label = stringResource(R.string.settings_memory_auto_allocate)
                        )
                        GlassTextField(
                            backdrop = backdrop,
                            value = state.maxMemory,
                            onValueChange = { state.maxMemory = it },
                            hint = "Max Memory (MB)"
                        )
                        GlassTextField(
                            backdrop = backdrop,
                            value = state.minMemory,
                            onValueChange = { state.minMemory = it },
                            hint = "Min Memory (MB)"
                        )
                    }
                }
            }

            item { GlassSectionTitle(text = stringResource(R.string.settings_game_java_version)) }
            item {
                GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
                    val javaVersions = remember { JavaManager.javaList.map { it.name } }
                    GlassSourceSelector(
                        backdrop = backdrop,
                        sources = javaVersions,
                        selected = state.java,
                        onSelect = { state.java = it },
                        tint = tintColor
                    )
                }
            }

            item { GlassSectionTitle(text = "JVM Arguments") }
            item {
                GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        GlassTextField(
                            backdrop = backdrop,
                            value = state.javaArgs,
                            onValueChange = { state.javaArgs = it },
                            hint = "JVM Arguments",
                            singleLine = false
                        )
                        GlassTextField(
                            backdrop = backdrop,
                            value = state.minecraftArgs,
                            onValueChange = { state.minecraftArgs = it },
                            hint = "Minecraft Arguments",
                            singleLine = false
                        )
                    }
                }
            }

            item { GlassSectionTitle(text = "Display") }
            item {
                GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        GlassCheckbox(
                            backdrop = backdrop,
                            checked = state.fullscreen,
                            onCheckedChange = { state.fullscreen = it },
                            label = "Force Resolution",
                            tint = tintColor
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            GlassTextField(
                                backdrop = backdrop,
                                value = state.windowWidth,
                                onValueChange = { state.windowWidth = it },
                                hint = "Width",
                                modifier = Modifier.weight(1f)
                            )
                            GlassTextField(
                                backdrop = backdrop,
                                value = state.windowHeight,
                                onValueChange = { state.windowHeight = it },
                                hint = "Height",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            item { GlassSectionTitle(text = "Server") }
            item {
                GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
                    GlassTextField(
                        backdrop = backdrop,
                        value = state.serverIp,
                        onValueChange = { state.serverIp = it },
                        hint = "Server Address"
                    )
                }
            }

            item { GlassSectionTitle(text = "Other") }
            item {
                GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        GlassCheckbox(
                            backdrop = backdrop,
                            checked = state.isolateGameDir,
                            onCheckedChange = { state.isolateGameDir = it },
                            label = stringResource(R.string.settings_game_working_directory)
                        )
                        GlassCheckbox(
                            backdrop = backdrop,
                            checked = state.notCheckGame,
                            onCheckedChange = { state.notCheckGame = it },
                            label = "Skip game completeness check"
                        )
                        GlassCheckbox(
                            backdrop = backdrop,
                            checked = state.notCheckJVM,
                            onCheckedChange = { state.notCheckJVM = it },
                            label = "Skip JVM check"
                        )
                    }
                }
            }

            item { GlassSectionTitle(text = "Manage") }
            item {
                GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        GlassTextButton(
                            backdrop = backdrop,
                            text = "Manage Mods",
                            onClick = { navController.navigate(FCLGlassRoute.VersionMods(profile.name, version)) },
                            modifier = Modifier.fillMaxWidth(),
                            tint = tintColor
                        )
                        GlassTextButton(
                            backdrop = backdrop,
                            text = "Manage Worlds",
                            onClick = { navController.navigate(FCLGlassRoute.VersionWorlds(profile.name, version)) },
                            modifier = Modifier.fillMaxWidth(),
                            tint = tintColor
                        )
                        GlassTextButton(
                            backdrop = backdrop,
                            text = "Manage Resource Packs",
                            onClick = { navController.navigate(FCLGlassRoute.VersionResourcePacks(profile.name, version)) },
                            modifier = Modifier.fillMaxWidth(),
                            tint = tintColor
                        )
                        GlassTextButton(
                            backdrop = backdrop,
                            text = "Manage Shader Packs",
                            onClick = { navController.navigate(FCLGlassRoute.VersionShaderPacks(profile.name, version)) },
                            modifier = Modifier.fillMaxWidth(),
                            tint = tintColor
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                GlassTextButton(
                    backdrop = backdrop,
                    text = "Save",
                    onClick = {
                        state.save()
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
