package com.tungsten.fcl.ui.version.compose

import android.app.Application
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
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
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
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.bridge.collectAsState
import com.tungsten.fcl.ui.compose.rememberShakeState
import com.tungsten.fcl.ui.compose.shake
import com.tungsten.fcl.ui.compose.dialog.ComposeDialogs
import com.tungsten.fcl.ui.compose.dialog.MiuixAddProfileDialog
import com.tungsten.fcl.ui.version.AddProfileDialog
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fclcore.fakefx.collections.ListChangeListener
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 版本列表页 Compose 界面（小步骤 3.3）：page_version_list.xml + VersionListPage.kt +
 * VersionListAdapter.kt + ProfileListAdapter.java 的 Miuix 重构。
 *
 * 布局对齐遗留：左侧 30% 栏（搜索框 + 游戏目录列表 + 刷新/新建目录按钮），
 * 右侧 70% 栏（分类过滤条 + 版本卡片列表）；加载中显示进度圈、空列表隐藏右侧面板。
 * 根布局保持透明（露出用户壁纸），与遗留 page_version_list.xml 透明根一致。
 *
 * 行为承接：Composable 只读 uiState、只调 ViewModel 语义化方法；
 * 弹窗/跨 UI 跳转等一次性副作用经 onEvent 转 [VersionListScreenHost]。
 */
@Composable
fun VersionListScreen(
    onEvent: (VersionListEvent) -> Unit = {},
) {
    val context = LocalContext.current
    val viewModel: VersionListViewModel = viewModel(initializer = {
        VersionListViewModel(context.applicationContext as Application)
    })
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { onEvent(it) }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        // ---------- 左栏：搜索 + 游戏目录 + 刷新/新建（对齐布局 :12-58，30% 宽） ----------
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f),
        ) {
            TextField(
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
            Button(
                onClick = viewModel::onRefresh,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.loading,
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.action_refresh))
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = viewModel::onNewProfile,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.version_new_profile))
            }
        }

        Spacer(Modifier.width(10.dp))

        // ---------- 右栏：分类条 + 版本列表（对齐 :60-146；加载中进度圈、空列表隐藏） ----------
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f),
        ) {
            when {
                state.loading -> {
                    InfiniteProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MiuixTheme.colorScheme.primary,
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

/** 游戏目录列表（对齐 ProfileListAdapter：点击切换 / 删除 / 选中高亮 / 错误抖动）。 */
@Composable
private fun ProfileListColumn(modifier: Modifier = Modifier) {
    val profiles by rememberProfiles()
    val selectedProfile by Profiles.selectedProfileProperty().collectAsState()
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

/** 观察 fakefx ObservableList<Profile>（对齐 FCLAdapter 的列表监听自动刷新）。 */
@Composable
private fun rememberProfiles(): State<List<Profile>> {
    val state = remember { mutableStateOf(Profiles.profiles.toList()) }
    DisposableEffect(Unit) {
        val listener = ListChangeListener<Profile> { state.value = Profiles.profiles.toList() }
        Profiles.profiles.addListener(listener)
        onDispose { Profiles.profiles.removeListener(listener) }
    }
    return state
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
            .background(
                if (selected) MiuixTheme.colorScheme.primaryContainer else Color.Transparent,
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
            )
            BasicText(
                text = profile.gameDir.absolutePath,
                style = MiuixTheme.textStyles.body2.copy(
                    fontSize = 11.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
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
                    Profiles.profiles.remove(profile)
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
        Card(modifier = Modifier.fillMaxWidth()) {
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
                    modifier = Modifier.animateItem(
                        // 对齐 Adapter :94-99 入场动画（时长 = animationSpeed × 30ms）
                        fadeInSpec = tween(
                            ThemeEngine.getInstance().theme?.animationSpeed?.times(30) ?: 150,
                        ),
                        placementSpec = null,
                        fadeOutSpec = null,
                    ),
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
        RadioButton(selected = selected, onClick = onClick)
        Spacer(Modifier.width(4.dp))
        Text(
            text = label,
            style = MiuixTheme.textStyles.body2,
            color = if (selected) MiuixTheme.colorScheme.primary
            else MiuixTheme.colorScheme.onSurface,
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSelect)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(selected = selected, onClick = onSelect)
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
                    )
                    if (item.tag != null) {
                        Text(
                            text = item.tag,
                            fontSize = 11.sp,
                            maxLines = 1,
                            color = MiuixTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                    }
                }
                Text(
                    // 对齐 Adapter :89-93 的副标题拼接（"%s  Mods:%d"）
                    text = "%s  Mods:%d".format(item.libraries, item.modCount),
                    fontSize = 11.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
            }
            // 设置按钮仅版本独立设置可见（对齐 Adapter :67-80）
            if (item.showSetting) {
                IconButton(onClick = onSettings) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_settings_24),
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onSurface,
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_delete_24),
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.onSurface,
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
                // 对齐 VersionListPage :240-248（含 3.2 弹窗开关）
                if (ComposeDialogs.USE_COMPOSE_ADD_PROFILE) {
                    MiuixAddProfileDialog(context).show()
                } else {
                    AddProfileDialog(context).show()
                }
            }

            is VersionListEvent.DeleteVersion -> {
                // 业务零重写：确认弹窗 + 删除任务留在遗留 Versions.deleteVersion
                Versions.deleteVersion(context, event.profile, event.version)
            }

            VersionListEvent.OpenVersionSettings -> {
                // 对齐 VersionListAdapter :69-77：跳 Manage UI 并选中第一个 Tab
                val uiManager = MainActivity.getInstance().uiManager
                MainActivity.getInstance().binding.manage.isSelected = true
                uiManager.manageUI.runAfterInit {
                    val tab = uiManager.manageUI.tabLayout.getTabAt(0)
                    uiManager.manageUI.tabLayout.selectTab(tab)
                }
            }
        }
    }
}
