package com.tungsten.fcl.ui.compose

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.DropdownArrowEndAction
import top.yukonga.miuix.kmp.basic.DropdownImpl
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.window.WindowListPopup

/**
 * FCL 旧版 FCLSpinner 形态在 Miuix 上的共享实现（对齐 page_download.xml 左栏
 * "FCLTextView 小字 label + 紧凑下拉行" 纵向堆叠）：
 * - label = FCLTextView（无 textSize → 平台默认 14sp，auto_text_tint = onPrimary）；
 * - label 与下拉行间距 = 10dp（对齐旧版 FCLSpinner layout_marginTop=10dp）；
 * - 选中值行 = item_spinner_auto_tint（14sp、marquee、padding 8dp、透明底）；
 * - 弹层 = Miuix [WindowListPopup] + [ListPopupColumn] + [DropdownImpl] 选项行，
 *   锚定到本控件（分类树缩进字符串直接作为文本传入即可，与旧版一致）；
 * - 文字色直读 colorScheme：onPrimary（置于 primaryContainer 容器内，对齐 auto_text_tint）；
 * - [enabled] = false 时同时禁用弹层触发（对齐 DownloadPage.setLoading 禁用 spinner 语义）。
 */
@Composable
fun FCLDropdownField(
    label: String,
    items: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }
    val textColor = MiuixTheme.colorScheme.onPrimary.copy(
        alpha = if (enabled) 1f else 0.38f,
    )
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = textColor,
        )
        // 对齐旧版 FCLSpinner layout_marginTop=10dp（label 与下拉行间距）
        Spacer(Modifier.height(10.dp))
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) { expanded = true }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 对齐 item_spinner_auto_tint：14sp、singleLine、marquee
                Text(
                    text = items.getOrElse(selectedIndex) { "" },
                    fontSize = 14.sp,
                    maxLines = 1,
                    color = textColor,
                    modifier = Modifier
                        .weight(1f)
                        .basicMarquee(),
                )
                DropdownArrowEndAction(actionColor = textColor)
            }
            WindowListPopup(
                show = expanded,
                onDismissRequest = { expanded = false },
            ) {
                ListPopupColumn {
                    items.forEachIndexed { index, text ->
                        DropdownImpl(
                            text = text,
                            optionSize = items.size,
                            isSelected = index == selectedIndex,
                            index = index,
                            onSelectedIndexChange = {
                                onSelectedIndexChange(it)
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}
