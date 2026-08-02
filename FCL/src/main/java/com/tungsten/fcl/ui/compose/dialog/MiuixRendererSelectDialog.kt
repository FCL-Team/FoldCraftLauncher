package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mio.manager.RendererManager
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.function.Consumer

/**
 * Miuix 版渲染器选择弹窗（3.2 批 2，对应 com/mio/ui/dialog/RendererSelectDialog + dialog_select_renderer）。
 *
 * 行为对齐：列表项文案 = 渲染器描述（含支持 MC 版本区间时追加括号说明，同遗留 getAdapter）；
 * 点击某项把 rendererList[position].id 写入当前 Profile 的 global/versionSetting，
 * dismiss 后以该项显示文案回调 callback；刷新按钮 RendererManager.refresh 后重建列表；
 * 取消 dismiss（遗留未显式 setCancelable，默认 true 一致）。
 */
class MiuixRendererSelectDialog(
    context: Context,
    private val isGlobal: Boolean,
    private val callback: Consumer<String>,
) : FCLComposeDialog(context) {

    private val itemsState = mutableStateOf(buildItems())

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.settings_fcl_renderer),
                scrollable = false,
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(R.string.button_cancel),
                        onClick = { dismiss() },
                    ),
                    FCLDialogButton(
                        text = stringResource(R.string.action_refresh),
                        onClick = {
                            RendererManager.refresh(context)
                            itemsState.value = buildItems()
                        },
                    ),
                ),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp),
                ) {
                    itemsIndexed(itemsState.value) { index, label ->
                        Text(
                            text = label,
                            style = MiuixTheme.textStyles.body2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(index, label) }
                                .padding(10.dp),
                        )
                    }
                }
            }
        }
    }

    private fun onSelect(index: Int, label: String) {
        val versionSetting =
            if (isGlobal) Profiles.getSelectedProfile().global else Profiles.getSelectedProfile().versionSetting
        versionSetting.renderer = RendererManager.rendererList[index].id
        dismiss()
        callback.accept(label)
    }

    private fun buildItems(): List<String> = RendererManager.rendererList.map {
        val ver = when {
            it.minMCver.isNotEmpty() && it.maxMCver.isNotEmpty() -> "${it.minMCver}~${it.maxMCver}"
            it.minMCver.isNotEmpty() -> ">=${it.minMCver}"
            it.maxMCver.isNotEmpty() -> "<=${it.maxMCver}"
            else -> ""
        }
        if (ver.isNotEmpty()) "${it.des} (${context.getString(R.string.supported_mc_version)} $ver)" else it.des
    }
}
