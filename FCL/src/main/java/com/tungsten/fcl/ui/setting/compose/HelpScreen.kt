package com.tungsten.fcl.ui.setting.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.setting.DocIndex
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import com.tungsten.fcl.util.AndroidUtils
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import com.tungsten.fcl.ui.compose.FCLCard
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 帮助页 Compose 界面（小步骤 3.1）：page_setting_help.xml + HelpPage.java 的 Miuix 重构。
 *
 * 布局对齐遗留：左侧 30% 分类栏（标题 + 分割线 + 分类列表 + 刷新/文档站按钮），
 * 右侧文章列表；加载中显示进度圈、失败显示重试（对齐 HelpPage.setLoading 三态）。
 */
@Composable
fun HelpScreen(
    onEvent: (HelpEvent) -> Unit = {},
) {
    // Application 由默认 Factory 经 CreationExtras 注入（FCLViewModel 已改 AndroidViewModel）
    val viewModel: HelpViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { onEvent(it) }
    }

    // 根布局保持透明（露出用户壁纸），对齐遗留 page_setting_help.xml 透明根。
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
    ) {
        // 左侧分类栏（对齐 @id/left，30% 宽）
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f),
        ) {
            Text(
                text = stringResource(R.string.help_category),
                fontSize = 11.sp,
                // 对齐遗留 use_theme_color="true"（color2 = onSurface）
                color = MiuixTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 10.dp),
            )
            Spacer(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MiuixTheme.colorScheme.outline),
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(state.categories, key = { it.category }) { category ->
                    CategoryRow(
                        category = category,
                        selected = category == state.selectedCategory,
                        onClick = { viewModel.selectCategory(category) },
                    )
                }
            }
            Button(
                onClick = viewModel::refresh,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                enabled = !state.loading,
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.action_refresh))
            }
            Button(
                onClick = viewModel::onWebsiteClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                enabled = !state.loading,
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.help_website))
            }
        }

        Spacer(Modifier.width(10.dp))

        // 右侧文章列表（对齐 @id/list + progress + retry 三态）
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f),
        ) {
            when {
                state.loading -> {
                    InfiniteProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        // 对齐遗留 FCLProgressBar 的 dkColor 染色（= primaryVariant）
                        color = MiuixTheme.colorScheme.primaryVariant,
                    )
                }

                !state.success -> {
                    // 遗留为重试图标（FCLImageButton，不染主色，仅 ripple ltColor），
                    // 故用默认中性配色的按钮而非主色实心按钮
                    Button(
                        onClick = viewModel::refresh,
                        modifier = Modifier.align(Alignment.Center),
                    ) {
                        Text(text = stringResource(R.string.action_refresh))
                    }
                }

                else -> {
                    // 对齐 item_article.xml 的 auto_linear_background_tint（ltColor = primaryContainer）
                    FCLCard(
                        modifier = Modifier.fillMaxSize(),
                        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.articles, key = { it.path }) { item ->
                                ArrowPreference(
                                    title = item.title,
                                    summary = item.subtitle,
                                    onClick = { viewModel.onArticleClick(item) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** 分类行：选中态高亮（对齐 DocCategoryAdapter 的 selected 背景切换）。 */
@Composable
private fun CategoryRow(
    category: DocIndex,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            // 对齐 bg_container_transparent_selected（ui_bg_color #40F4F4F4，无 night 变体、昼夜同色）
            .background(
                if (selected) FCLThemeTokens.UiBackgroundLight
                else Color.Transparent,
            )
            .clickable(onClick = onClick)
            .padding(10.dp),
    ) {
        Text(
            text = category.getDisplayName(LocalContext.current),
            style = MiuixTheme.textStyles.body2,
            maxLines = 1,
            // 对齐 DocCategoryAdapter 的 setUseThemeColor(true)（color2 = onSurface，选中不变色）
            color = MiuixTheme.colorScheme.onSurface,
        )
    }
}

/** 帮助页宿主事件处理：链接跳转（对齐 AndroidUtils.openLink 调用点）。 */
object HelpScreenHost {
    fun handle(context: Context, event: HelpEvent) {
        when (event) {
            is HelpEvent.OpenLink -> AndroidUtils.openLink(context, event.url)
        }
    }
}
