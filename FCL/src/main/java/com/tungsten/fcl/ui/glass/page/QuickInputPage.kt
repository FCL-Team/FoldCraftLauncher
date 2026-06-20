package com.tungsten.fcl.ui.glass.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassTextField
import com.tungsten.fcl.ui.glass.component.GlassTopBar

@Composable
fun QuickInputPage(
    backdrop: Backdrop,
    title: String,
    initialValue: String,
    hint: String,
    onConfirm: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var value by rememberSaveable { mutableStateOf(initialValue) }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(
            title = title,
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            GlassTextField(
                backdrop = backdrop,
                value = value,
                onValueChange = { value = it },
                hint = hint,
                singleLine = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

        GlassButton(
            backdrop = backdrop,
            onClick = {
                onConfirm(value)
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(text = "OK", color = Color.White)
        }
    }
}
