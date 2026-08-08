package com.tungsten.fcl.ui.version.compose

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.util.NavigationBus
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.compose.rememberShakeState
import com.tungsten.fcl.ui.compose.shake
import com.tungsten.fcl.ui.compose.dialog.MiuixAddProfileDialog
import com.tungsten.fcl.ui.compose.fclItemEntryModifier
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcl.ui.compose.FCLButton
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import com.tungsten.fcl.ui.compose.FCLCard
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.RadioButtonDefaults
import top.yukonga.miuix.kmp.basic.Text
import com.tungsten.fcl.ui.compose.FCLTextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 版本列表页 Compose 界面（小步骤 3.3）：page_version_list.xml + VersionListPage.kt +
 * VersionListAdapter.kt + ProfileListAdapter.java 的 Miuix 重构。
 *
 * 布局对齐遗留：左侧 30% 栏（顶部搜索框 + 游戏目录列表 + 刷新/新建目录按钮），
 * 右侧 70% 栏（分类过滤条 + 版本卡片列表）；加载中显示进度圈、空列表隐藏列表区。
 * 根布局保持透明（露出用户壁纸），与遗留 page_version_list.xml 透明根一致。
 *
 * 行为承接：Composable 只读 uiState、只调 ViewModel 语义化方法；
 * 弹窗/跨 UI 跳转等一次性副作用经 onEvent 转 [VersionListScreenHost]。
 */
@Composable
fun VersionListScreen(
    onEvent: (VersionListEvent) -> Unit = {},
) {
    // Application 由默认 Factory 经 CreationExtras 注入（FCLViewModel 已改 AndroidViewModel）
    val viewModel: VersionListViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { onEvent(it) }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 10.dp, end = 10.dp),
    ) {
        // ---------- 左栏：搜索 + 游戏目录 + 刷新/新建（对齐 page_version_list.xml :15-58，30% 宽） ----------
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f),
        ) {
            // 搜索框在左栏顶部（对齐旧版 :15-26，宽 30%、居中、hint=search）
            FCLTextField(
                value = state.searchText,
                onValueChange = viewModel::onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.search),
                useLabelAsPlaceholder = true,
                singleLine = true,
            )
            ProfileListColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 8.dp),
            )
            FCLButton(
                onClick = viewModel::onRefresh,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.loading,
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.action_refresh))
            }
            // 旧版间距：refresh marginBottom=8 + new_profile marginTop=10 = 18dp
            Spacer(Modifier.height(18.dp))
            FCLButton(
                onClick = viewModel::onNewProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.version_new_profile))
            }
        }

        Spacer(Modifier.width(10.dp))

        // ---------- 右栏：分类条 + 版本列表（对齐 :60-146）；加载/空列表时保持分类条可见 ----------
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                when {
                    state.loading -> {
                        // 对齐 FCLProgressBar 的 dkColor 着色（Compose 侧 = primaryVariant，
                        // 与 main 域 MainRightMenu.kt:368 同一约定）
                        InfiniteProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MiuixTheme.colorScheme.primaryVariant,
                        )
                    }

                    state.hasVersions -> {
                        VersionListArea(state = state, viewModel = viewModel)
                    }
                    // 空列表：旧代码隐藏整个右侧面板（binding.layout GONE），Compose 渲染空白
                }
            }
        }
    }
}

/** 游戏目录列表（对齐 ProfileListAdapter：点击切换 / 删除 / 选中高亮 / 错误抖动）。 */
@Composable
private fun ProfileListColumn(modifier: Modifier = Modifier) {
    val profiles by rememberProfiles()
    val selectedProfile by Profiles.selectedProfileFlow().collectAsState()
    LazyColumn(modifier = modifier) {
        items(profiles, key = { it.name }) { profile ->
            ProfileRow(
                profile = profile,
                selected = profile == selectedProfile,
                modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
            )
        }
    }
}

/** 观察 Profiles 信号流（阶段 4a：成员增删与目录内部变更都会递增，对齐原 extractor 列表监听）。 */
@Composable
private fun rememberProfiles(): State<List<Profile>> {
    val signal by Profiles.profilesSignalFlow().collectAsState()
    return remember(signal) { mutableStateOf(Profiles.profiles) }
}

@Composable
private fun ProfileRow(
    profile: Profile,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val shakeState = rememberShakeState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shake(shakeState)
            // 对齐 bg_container_transparent_selected（ui_bg_color #40F4F4F4，无 night
            // 变体、昼夜同色；不是 ltColor 染色），与 setting 域 HelpScreen.kt:187 同一约定
            .background(
                if (selected) FCLThemeTokens.UiBackgroundLight else Color.Transparent,
            )
            .clickable {
                // 对齐 ProfileListAdapter :69-76：版本加载中禁止切换目录并播抖动
                if (MainActivity.getInstance().isVersionLoading) {
                    shakeState.shake()
                } else {
                    Profiles.setSelectedProfile(profile)
                }
            }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = profile.name,
                style = MiuixTheme.textStyles.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                // 对齐 item_profile.xml 的 use_theme_color（color2 = onSurface）
                color = MiuixTheme.colorScheme.onSurface,
            )
            BasicText(
                text = profile.gameDir.absolutePath,
                style = MiuixTheme.textStyles.body2.copy(
                    fontSize = 11.sp,
                    // 对齐 item_profile.xml：path 同为 use_theme_color（全量 color2），不是弱化变体
                    color = MiuixTheme.colorScheme.onSurface,
                ),
                maxLines = 1,
                // 对齐 item_profile.xml 的路径跑马灯（marquee 与 Ellipsis 冲突，省略号移除）
                modifier = Modifier.basicMarquee(),
            )
        }
        Spacer(Modifier.width(10.dp))
        IconButton(
            onClick = {
                // 对齐 ProfileListAdapter :77-84：仅剩 1 个目录时抖动拒绝，
                // 否则直接删除（遗留无确认弹窗）；Compose 侧经列表监听自动刷新
                if (Profiles.profiles.size == 1) {
                    shakeState.shake()
                } else {
                    Profiles.removeProfile(profile)
                }
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_close_24),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MiuixTheme.colorScheme.onSurface,
            )
        }
    }
}

/** 右侧区域：分类过滤条（对齐 RadioGroup :83-122）+ 版本卡片列表。 */
@Composable
private fun VersionListArea(
    state: VersionListUiState,
    viewModel: VersionListViewModel,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 对齐 FCLAppBarLayout 的 bg_container_white + auto_tint（ltColor 染色 = primaryContainer）
        FCLCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CategoryOption(
                    label = stringResource(R.string.curse_category_0),
                    selected = state.category == VersionCategory.ALL,
                    onClick = { viewModel.onCategoryChange(VersionCategory.ALL) },
                )
                CategoryOption(
                    label = "Fabric",
                    selected = state.category == VersionCategory.FABRIC,
                    onClick = { viewModel.onCategoryChange(VersionCategory.FABRIC) },
                )
                CategoryOption(
                    label = "Forge",
                    selected = state.category == VersionCategory.FORGE,
                    onClick = { viewModel.onCategoryChange(VersionCategory.FORGE) },
                )
                CategoryOption(
                    label = "NeoForge",
                    selected = state.category == VersionCategory.NEOFORGE,
                    onClick = { viewModel.onCategoryChange(VersionCategory.NEOFORGE) },
                )
                CategoryOption(
                    label = stringResource(R.string.control_download_device_other),
                    selected = state.category == VersionCategory.OTHER,
                    onClick = { viewModel.onCategoryChange(VersionCategory.OTHER) },
                )
            }
        }
        Spacer(Modifier.height(3.dp))

        val listState = rememberLazyListState()
        // 加载完成后滚动到选中版本（对齐 VersionListPage :218-221；
        // 加载时过滤条件已重置，展示列表即完整列表，下标语义一致）
        LaunchedEffect(state.loadTick) {
            if (state.loadTick > 0) {
                val index = state.items.indexOfFirst { it.version == state.selectedVersion }
                if (index >= 0) listState.scrollToItem(index)
            }
        }
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(state.items, key = { it.version }) { item ->
                VersionRow(
                    item = item,
                    selected = item.version == state.selectedVersion,
                    onSelect = { viewModel.onSelectVersion(item) },
                    onDelete = { viewModel.onDeleteVersion(item) },
                    onSettings = { viewModel.onOpenVersionSettings(item) },
                    modifier = fclItemEntryModifier(),
                )
            }
        }
    }
}

/** 分类过滤项（对齐 FCLRadioButton + text_use_theme_color）。 */
@Composable
private fun CategoryOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 对齐 FCLRadioButton：选中态圆点着色为 dkColor（Compose 侧 = primaryVariant）
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.radioButtonColors(
                selectedColor = MiuixTheme.colorScheme.primaryVariant,
            ),
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = label,
            style = MiuixTheme.textStyles.body2,
            // 对齐 FCLRadioButton text_use_theme_color：文字恒定染 color（primary），
            // 与选中态无关（FCLRadioButton.java:45-47 setTextColor(getColor())）
            color = MiuixTheme.colorScheme.primary,
        )
    }
}

/** 版本卡片（对齐 item_version.xml：单选 + 图标 + 标题/tag + 副标题 + 设置/删除按钮）。 */
@Composable
private fun VersionRow(
    item: VersionItemUi,
    selected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FCLCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        // 对齐 item_version.xml 的 bg_container_white + auto_tint（ltColor 染色 = primaryContainer）
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSelect)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 对齐 item_version.xml 的 FCLRadioButton：选中态圆点 dkColor（= primaryVariant）
            RadioButton(
                selected = selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.radioButtonColors(
                    selectedColor = MiuixTheme.colorScheme.primaryVariant,
                ),
            )
            val iconBitmap = remember(item.icon) { item.icon?.toBitmap()?.asImageBitmap() }
            if (iconBitmap != null) {
                Image(
                    bitmap = iconBitmap,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                )
            } else {
                Spacer(Modifier.size(30.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.version,
                        style = MiuixTheme.textStyles.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        // 对齐 item_version.xml 的 auto_text_tint（按主色亮度取黑/白 = onPrimary）
                        color = MiuixTheme.colorScheme.onPrimary,
                    )
                    if (item.tag != null) {
                        Text(
                            text = item.tag,
                            fontSize = 11.sp,
                            maxLines = 1,
                            // 同标题：auto_text_tint = onPrimary，不是主色本色
                            color = MiuixTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                    }
                }
                Text(
                    // 对齐 Adapter :89-93 的副标题拼接（"%s  Mods:%d"）
                    text = "%s  Mods:%d".format(item.libraries, item.modCount),
                    fontSize = 11.sp,
                    // 同标题：auto_text_tint = onPrimary
                    color = MiuixTheme.colorScheme.onPrimary,
                )
            }
            // 设置按钮仅版本独立设置可见（对齐 Adapter :67-80）
            if (item.showSetting) {
                IconButton(onClick = onSettings) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_settings_24),
                        contentDescription = null,
                        // 对齐 item_version.xml 的 FCLImageButton auto_tint（按主色亮度取黑/白 = onPrimary）
                        tint = MiuixTheme.colorScheme.onPrimary,
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_delete_24),
                    contentDescription = null,
                    // 同上：auto_tint = onPrimary
                    tint = MiuixTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

/** 版本列表页宿主事件处理：弹窗与跨 UI 跳转（对齐 VersionListPage.onClick / Adapter 设置按钮）。 */
object VersionListScreenHost {
    fun handle(context: Context, event: VersionListEvent) {
        when (event) {
            VersionListEvent.NewProfile -> {
                MiuixAddProfileDialog(context).show()
            }

            is VersionListEvent.DeleteVersion -> {
                // 业务零重写：确认弹窗 + 删除任务留在遗留 Versions.deleteVersion
                Versions.deleteVersion(context, event.profile, event.version)
            }

            VersionListEvent.OpenVersionSettings -> {
                // 对齐 VersionListAdapter :69-77：跳 Manage UI 并选中第一个 Tab
                val uiManager = MainActivity.getInstance().uiManager
                NavigationBus.select(NavigationBus.Menu.MANAGE)
                uiManager.manageUI.runAfterInit {
                    val tab = uiManager.manageUI.tabLayout.getTabAt(0)
                    uiManager.manageUI.tabLayout.selectTab(tab)
                }
            }
        }
    }
}
