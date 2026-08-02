package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.control.FCLInput
import com.tungsten.fcl.control.gamepad.GamepadEmulatedButton
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclcore.fakefx.collections.FXCollections
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版手柄按键映射弹窗（3.2 批 2，对应 com/mio/ui/dialog/GamepadMapDialog + dialog_gamepad_map）。
 *
 * 行为对齐：16 行映射项，图标与顺序同遗留 GamepadMapItemAdapter；点每行设置钮打开
 * MiuixSelectKeycodeDialog 编辑该键 keycodes；
 * 确定 → gamepad.saveMapper() + dismiss；取消直接 dismiss；
 * 遗留未显式 setCancelable，默认 true 一致。
 *
 * 运行于游戏内（GameMenu → ControllerActivity，AppCompatActivity），
 * AppCompatDialog + ComposeView 可用。
 */
class MiuixGamepadMapDialog(
    context: Context,
    private val fclInput: FCLInput,
) : FCLComposeDialog(context) {

    private class Item(
        val icon: Int,
        val button: GamepadEmulatedButton,
    )

    private val items: List<Item>

    init {
        val map = fclInput.gamepad.currentMap
        items = listOf(
            Item(fr.spse.gamepad_remapper.R.drawable.button_a, map.BUTTON_A),
            Item(fr.spse.gamepad_remapper.R.drawable.button_b, map.BUTTON_B),
            Item(fr.spse.gamepad_remapper.R.drawable.button_x, map.BUTTON_X),
            Item(fr.spse.gamepad_remapper.R.drawable.button_y, map.BUTTON_Y),
            Item(fr.spse.gamepad_remapper.R.drawable.button_start, map.BUTTON_START),
            Item(fr.spse.gamepad_remapper.R.drawable.button_select, map.BUTTON_SELECT),
            Item(fr.spse.gamepad_remapper.R.drawable.shoulder_left, map.SHOULDER_LEFT),
            Item(fr.spse.gamepad_remapper.R.drawable.shoulder_right, map.SHOULDER_RIGHT),
            Item(fr.spse.gamepad_remapper.R.drawable.trigger_left, map.TRIGGER_LEFT),
            Item(fr.spse.gamepad_remapper.R.drawable.trigger_right, map.TRIGGER_RIGHT),
            Item(fr.spse.gamepad_remapper.R.drawable.stick_left_click, map.THUMBSTICK_LEFT),
            Item(fr.spse.gamepad_remapper.R.drawable.stick_right_click, map.THUMBSTICK_RIGHT),
            Item(fr.spse.gamepad_remapper.R.drawable.dpad_up, map.DPAD_UP),
            Item(fr.spse.gamepad_remapper.R.drawable.dpad_down, map.DPAD_DOWN),
            Item(fr.spse.gamepad_remapper.R.drawable.dpad_left, map.DPAD_LEFT),
            Item(fr.spse.gamepad_remapper.R.drawable.dpad_right, map.DPAD_RIGHT),
        )

        setDialogContent {
            FCLDialogCard(
                scrollable = false,
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = {
                            fclInput.gamepad.saveMapper()
                            dismiss()
                        },
                    ),
                    FCLDialogButton(
                        text = stringResource(R.string.button_cancel),
                        onClick = { dismiss() },
                    ),
                ),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                ) {
                    items(items) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(item.icon),
                                contentDescription = null,
                                modifier = Modifier.size(50.dp),
                            )
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = {
                                val list = FXCollections.observableList(item.button.keycodes)
                                // 4.1 接入点：Miuix 键码选择弹窗
                                MiuixSelectKeycodeDialog(context, list, false, true).show()
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_baseline_settings_24),
                                    contentDescription = null,
                                    tint = MiuixTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
