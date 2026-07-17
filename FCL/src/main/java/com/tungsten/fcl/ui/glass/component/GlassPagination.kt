package com.tungsten.fcl.ui.glass.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R

@Composable
fun GlassPagination(
    backdrop: Backdrop,
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    tint: Color? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        GlassTextButton(
            backdrop = backdrop,
            text = stringResource(R.string.search_first_page),
            onClick = { onPageChange(0) },
            tint = tint
        )
        GlassTextButton(
            backdrop = backdrop,
            text = stringResource(R.string.button_prev),
            onClick = { onPageChange((currentPage - 1).coerceAtLeast(0)) },
            tint = tint
        )
        Text(
            text = "${currentPage + 1} / ${if (totalPages > 0) totalPages else 1}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        GlassTextButton(
            backdrop = backdrop,
            text = stringResource(R.string.button_next),
            onClick = { onPageChange((currentPage + 1).coerceAtMost(totalPages - 1)) },
            tint = tint
        )
        GlassTextButton(
            backdrop = backdrop,
            text = stringResource(R.string.search_last_page),
            onClick = { onPageChange(totalPages - 1) },
            tint = tint
        )
    }
}
