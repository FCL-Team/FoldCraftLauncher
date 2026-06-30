package com.tungsten.fcl.ui.glass.component

import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.tungsten.fcl.R
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.glass.theme.GlassTheme
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount
import com.tungsten.fclcore.auth.offline.OfflineAccount
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener

@Composable
fun GlassAccountItem(
    backdrop: Backdrop,
    account: Account,
    selected: Boolean,
    onSelect: () -> Unit,
    onRefresh: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(GlassTheme.glassCornerRadius)
    var avatar by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    DisposableEffect(account) {
        val binding = TexturesLoader.avatarBinding(account, 64)
        val listener = ChangeListener<android.graphics.drawable.Drawable> { _, _, new ->
            avatar = (new as? BitmapDrawable)?.bitmap
        }
        binding.addListener(listener)
        binding.value?.let { listener.changed(binding, null, it) }

        onDispose {
            binding.removeListener(listener)
        }
    }

    val title = remember(account) {
        val character = account.character
        if (account is OfflineAccount || account.username.isEmpty()) {
            character
        } else {
            "${account.username} - $character"
        }
    }

    val subtitle = remember(account) {
        val loginTypeName = Accounts.getLocalizedLoginTypeName(context, Accounts.getAccountFactory(account))
        if (account is AuthlibInjectorAccount) {
            val server = account.server
            "$loginTypeName, ${context.getString(R.string.account_injector_server)}: ${server.name}"
        } else {
            loginTypeName
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = {
                    vibrancy()
                    blur(8f.dp.toPx())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lens(12f.dp.toPx(), 24f.dp.toPx())
                    }
                },
                onDrawSurface = { drawRect(GlassTheme.surfaceColor()) }
            )
            .padding(16.dp)
    ) {
        RadioButton(selected = selected, onClick = onSelect)

        avatar?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        GlassIconButton(
            backdrop = backdrop,
            iconRes = R.drawable.ic_baseline_refresh_24,
            contentDescription = stringResource(R.string.action_refresh),
            onClick = onRefresh,
            modifier = Modifier.size(40.dp)
        )

        GlassIconButton(
            backdrop = backdrop,
            iconRes = R.drawable.ic_baseline_delete_24,
            contentDescription = stringResource(R.string.button_remove),
            onClick = onDelete,
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.error
        )
    }
}
