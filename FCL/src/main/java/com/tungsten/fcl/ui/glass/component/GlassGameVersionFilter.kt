package com.tungsten.fcl.ui.glass.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R

@Composable
fun GlassGameVersionFilter(
    backdrop: Backdrop,
    gameVersion: String,
    onGameVersionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassTextField(
        backdrop = backdrop,
        value = gameVersion,
        onValueChange = onGameVersionChange,
        hint = stringResource(R.string.archive_game_version),
        modifier = modifier
    )
}
