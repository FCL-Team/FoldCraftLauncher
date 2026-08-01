package com.tungsten.fcl.ui.setting.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.ColorPicker
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.window.WindowDialog

/**
 * Miuix 取色器弹窗（小步骤 3.1）：替换 FCLColorPickerDialog。
 *
 * Miuix 0.9.3 自带 basic/ColorPicker（HSV 滑杆组 + 预览，源码核实自带外部颜色回环防护），
 * 弹窗外壳用 window/WindowDialog（无 Scaffold 依赖，与遗留 FCLDialog 独立 window 语义一致，
 * 见 bridge-api.md §2.3）。
 *
 * 回调语义与 FCLColorPickerDialog.Listener 一一对应：
 * - [onColorChanged]：拖动实时预览（对应 onColorChanged，调用方只 apply 不 save）；
 * - [onConfirm]：确定保存（对应 onPositive）；
 * - [onDismiss]：取消/点遮罩/返回键（对应 onNegative，调用方还原初始色）。
 */
@Composable
fun ColorPickerDialog(
    title: String,
    color: Color,
    onColorChanged: (Color) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    WindowDialog(
        show = true,
        title = title,
        onDismissRequest = onDismiss,
    ) {
        ColorPicker(
            color = color,
            onColorChanged = onColorChanged,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                onClick = onDismiss,
            )
            Spacer(Modifier.width(8.dp))
            TextButton(
                text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                onClick = onConfirm,
            )
        }
    }
}
