package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclauncher.plugins.DriverPlugin
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.function.Consumer

/**
 * Miuix 版驱动选择弹窗（5.1 回归遗留 L1，对应 com/mio/ui/dialog/DriverSelectDialog
 * + item_renderer，与 dialog_select_renderer 共用列表项样式）。
 *
 * 行为对齐遗留 DriverSelectDialog：列表项文案 = DriverPlugin.driverList 各项的
 * driver 名；点击某项把 driver 写入当前 Profile 的 global/versionSetting、更新
 * DriverPlugin.selected，dismiss 后以该项名称回调 callback；取消 dismiss
 * （遗留未显式 setCancelable，默认 true 一致）。
 */
class MiuixDriverSelectDialog(
    context: Context,
    private val isGlobal: Boolean,
    private val callback: Consumer<String>,
) : FCLComposeDialog(context) {

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.settings_fcl_driver),
                scrollable = false,
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(R.string.button_cancel),
                        onClick = { dismiss() },
                    ),
                ),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp),
                ) {
                    itemsIndexed(DriverPlugin.driverList) { index, driver ->
                        Text(
                            text = driver.driver,
                            style = MiuixTheme.textStyles.body2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(index) }
                                .padding(10.dp),
                        )
                    }
                }
            }
        }
    }

    private fun onSelect(index: Int) {
        val driver = DriverPlugin.driverList[index]
        val versionSetting =
            if (isGlobal) Profiles.getSelectedProfile().global else Profiles.getSelectedProfile().versionSetting
        versionSetting.driver = driver.driver
        DriverPlugin.selected = driver
        dismiss()
        callback.accept(driver.driver)
    }
}
