package com.tungsten.fcl.ui.glass.page.java

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassTextField
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task

@Composable
fun JvmArgsPage(
    backdrop: Backdrop,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val global = remember { Profiles.getSelectedProfile().getGlobal() }
    var args by remember { mutableStateOf(global.javaArgs) }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(
            title = stringResource(R.string.settings_advanced_jvm_args),
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_advanced_jvm_args),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            GlassTextField(
                backdrop = backdrop,
                value = args,
                onValueChange = { args = it },
                hint = "JVM Arguments",
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        GlassButton(
            backdrop = backdrop,
            onClick = {
                Task.runAsync {
                    global.javaArgs = args
                    Schedulers.androidUIThread().execute {
                        Toast.makeText(
                            context,
                            R.string.message_success,
                            Toast.LENGTH_SHORT
                        ).show()
                        onBack()
                    }
                }.start()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.button_save), color = Color.White)
        }
    }
}
