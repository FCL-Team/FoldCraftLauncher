package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mio.datastore.GameItemBarSetting
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.ui.compose.fclSwitchColors
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版游戏物品栏设置弹窗（3.2 批 2，对应 control/GameItemBarSettingDialog + dialog_itembar_setting）。
 *
 * 行为对齐：两个开关初始值取自 setting，每次切换立即 callback；关闭按钮 dismiss；
 * 遗留未显式 setCancelable，默认 true 一致。
 * （遗留窗体背景为游戏菜单半透明 bg_game_menu，Miuix 卡片样式取代之。）
 *
 * 有意偏差：遗留每次回调都是基于构造时 setting 的单字段 copy，同一次会话先动
 * 开关 A 再动开关 B 会丢失 A 的改动（lost-update 缺陷）；本实现以当前两个开关的
 * 实时状态构造回调值，两处改动均保留。附录 D 登记。
 *
 * 运行于游戏内（GameItemBar → ControllerActivity，AppCompatActivity），
 * AppCompatDialog + ComposeView 可用。
 */
class MiuixGameItemBarSettingDialog(
    context: Context,
    setting: GameItemBarSetting,
    private val callback: (GameItemBarSetting) -> Unit,
) : FCLComposeDialog(context) {

    private val slideSelectionState = mutableStateOf(setting.slideSelection)
    private val swapHandsState = mutableStateOf(setting.doubleTapSwapHands)

    init {
        setDialogContent {
            FCLDialogCard(
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.close),
                        onClick = { dismiss() },
                    ),
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.slide_selection),
                        style = MiuixTheme.textStyles.body2,
                        modifier = Modifier.weight(1f),
                    )
                    Switch(
                        checked = slideSelectionState.value,
                        onCheckedChange = {
                            slideSelectionState.value = it
                            callback(GameItemBarSetting(it, swapHandsState.value))
                        },
                        colors = fclSwitchColors(),
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.swap_hands),
                        style = MiuixTheme.textStyles.body2,
                        modifier = Modifier.weight(1f),
                    )
                    Switch(
                        checked = swapHandsState.value,
                        onCheckedChange = {
                            swapHandsState.value = it
                            callback(GameItemBarSetting(slideSelectionState.value, it))
                        },
                        colors = fclSwitchColors(),
                    )
                }
            }
        }
    }
}
