package com.tungsten.fcl.ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
 * - 弹层 = Miuix [WindowListPopup] + [ListPopupColumn] 容器，选项行自绘还原
 *   item_spinner_dropdown 旧样式：白底、8dp padding、14sp、singleLine、marquee，
 *   选中项 ltColor（primaryContainer）底 + dkColor（primaryVariant）文字，未选中黑字；
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
                // 旧版 AppCompatSpinner 默认样式：文本末端自绘小实心倒三角
                //（Material spinner triangle，纯色填充，颜色跟随行文字色），
                // 替换 Miuix DropdownArrowEndAction 的 MIUI 箭头形态
                SpinnerTriangle(color = textColor)
            }
            WindowListPopup(
                show = expanded,
                onDismissRequest = { expanded = false },
            ) {
                ListPopupColumn {
                    // 自绘 item_spinner_dropdown 旧样式（替代 MIUI 圆角高亮的 DropdownImpl）：
                    // 白底 + 黑字，选中项 ltColor 底 + dkColor 文字
                    val checkedBackground = MiuixTheme.colorScheme.primaryContainer
                    val checkedText = MiuixTheme.colorScheme.primaryVariant
                    items.forEachIndexed { index, text ->
                        val checked = index == selectedIndex
                        Text(
                            text = text,
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = if (checked) checkedText else Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (checked) checkedBackground else Color.White)
                                .clickable {
                                    onSelectedIndexChange(index)
                                    expanded = false
                                }
                                .padding(8.dp)
                                .basicMarquee(),
                        )
                    }
                }
            }
        }
    }
}

/**
 * 旧版 Material spinner 末端的小实心倒三角（宽 10dp、高 6dp，纯色填充）。
 */
@Composable
private fun SpinnerTriangle(color: Color) {
    Canvas(
        modifier = Modifier
            .padding(start = 8.dp)
            .size(width = 10.dp, height = 6.dp),
    ) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width / 2f, size.height)
            close()
        }
        drawPath(path, color = color)
    }
}
