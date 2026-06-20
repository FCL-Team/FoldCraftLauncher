package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassTextButton
import com.tungsten.fcl.ui.glass.component.GlassTextField
import com.tungsten.fcllibrary.component.theme.ThemeEngine

@Composable
fun GlassDialogHost(
    backdrop: Backdrop,
    modifier: Modifier = Modifier
) {
    val request = GlassDialogManager.current

    AnimatedVisibility(visible = request != null) {
        request?.let { currentRequest ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            when (currentRequest) {
                                is DialogRequest.Alert,
                                is DialogRequest.Input,
                                is DialogRequest.Selection<*> -> {
                                    currentRequest.onDismiss()
                                    GlassDialogManager.dismiss(currentRequest)
                                }
                                else -> { /* non-dismissible by background tap */ }
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { /* consume clicks so the scrim doesn't dismiss */ }
                        )
                ) {
                    when (currentRequest) {
                        is DialogRequest.Alert -> GlassAlertDialog(
                            backdrop = backdrop,
                            request = currentRequest
                        )
                        is DialogRequest.Input -> GlassInputDialog(
                            backdrop = backdrop,
                            request = currentRequest
                        )
                        is DialogRequest.Progress -> GlassProgressDialog(
                            backdrop = backdrop,
                            request = currentRequest
                        )
                        is DialogRequest.Selection<*> -> GlassSelectionDialog(
                            backdrop = backdrop,
                            request = currentRequest
                        )
                        else -> GlassNotImplementedDialog(
                            backdrop = backdrop,
                            request = currentRequest
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GlassAlertDialog(
    backdrop: Backdrop,
    request: DialogRequest.Alert
) {
    val tint = Color(ThemeEngine.getInstance().getTheme().getColor())
    GlassDialogSurface(backdrop = backdrop) {
        Text(
            text = request.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = request.message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            request.dismissText?.let {
                GlassTextButton(
                    backdrop = backdrop,
                    text = it,
                    onClick = {
                        request.onDismiss()
                        GlassDialogManager.dismiss(request)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            GlassButton(
                backdrop = backdrop,
                onClick = {
                    request.onConfirm()
                    GlassDialogManager.dismiss(request)
                },
                tint = tint,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = request.confirmText, color = Color.White)
            }
        }
    }
}

@Composable
private fun GlassInputDialog(
    backdrop: Backdrop,
    request: DialogRequest.Input
) {
    val tint = Color(ThemeEngine.getInstance().getTheme().getColor())
    var value by remember(request.id) { mutableStateOf(request.initialValue) }

    GlassDialogSurface(backdrop = backdrop) {
        Text(
            text = request.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        GlassTextField(
            backdrop = backdrop,
            value = value,
            onValueChange = { value = it },
            hint = request.hint,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (request.dismissText.isNotEmpty()) {
                GlassTextButton(
                    backdrop = backdrop,
                    text = request.dismissText,
                    onClick = {
                        request.onDismiss()
                        GlassDialogManager.dismiss(request)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            GlassButton(
                backdrop = backdrop,
                onClick = {
                    request.onConfirm(value)
                    GlassDialogManager.dismiss(request)
                },
                tint = tint,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = request.confirmText, color = Color.White)
            }
        }
    }
}

@Composable
private fun GlassProgressDialog(
    backdrop: Backdrop,
    request: DialogRequest.Progress
) {
    GlassDialogSurface(backdrop = backdrop) {
        Text(
            text = request.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (request.message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = request.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        val progressValue = request.progress
        if (progressValue != null) {
            LinearProgressIndicator(
                progress = { progressValue },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        } else {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        request.onCancel?.let { onCancel ->
            Spacer(modifier = Modifier.height(16.dp))
            GlassTextButton(
                backdrop = backdrop,
                text = "Cancel",
                onClick = {
                    onCancel()
                    GlassDialogManager.dismiss(request)
                },
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
private fun GlassSelectionDialog(
    backdrop: Backdrop,
    request: DialogRequest.Selection<*>
) {
    var selected by remember(request.id) { mutableStateOf<Any?>(request.selected) }

    GlassDialogSurface(backdrop = backdrop) {
        Text(
            text = request.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            request.items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selected = item }
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = selected == item,
                        onClick = { selected = item }
                    )
                    Text(
                        text = request.itemText(item),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (request.dismissText.isNotEmpty()) {
                GlassTextButton(
                    backdrop = backdrop,
                    text = request.dismissText,
                    onClick = {
                        request.onDismiss()
                        GlassDialogManager.dismiss(request)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            request.confirmText?.let { confirmText ->
                GlassTextButton(
                    backdrop = backdrop,
                    text = confirmText,
                    onClick = {
                        selected?.let {
                            @Suppress("UNCHECKED_CAST")
                            (request.onSelect as (Any?) -> Unit)(it)
                        }
                        GlassDialogManager.dismiss(request)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun GlassNotImplementedDialog(
    backdrop: Backdrop,
    request: DialogRequest
) {
    GlassDialogSurface(backdrop = backdrop) {
        Text(
            text = "Not implemented",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "This dialog type is not implemented yet.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(20.dp))
        GlassTextButton(
            backdrop = backdrop,
            text = "OK",
            onClick = { GlassDialogManager.dismiss(request) },
            modifier = Modifier.align(Alignment.End)
        )
    }
}
