package com.tungsten.fcl.ui.account.compose

import android.app.Application
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mio.util.copyToClipBoard
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.account.AccountListItem
import com.tungsten.fcl.ui.account.AddAuthlibInjectorServerDialog
import com.tungsten.fcl.ui.account.CreateAccountDialog
import com.tungsten.fcl.ui.account.OfflineAccountSkinDialog
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.bridge.collectAsState
import com.tungsten.fcl.ui.compose.FCLDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.dialog.ComposeDialogs
import com.tungsten.fcl.ui.compose.dialog.MiuixAddAuthlibInjectorServerDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixCreateAccountDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixOfflineAccountSkinDialog
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer
import com.tungsten.fclcore.auth.offline.OfflineAccount
import com.tungsten.fclcore.auth.offline.Skin
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.skin.SkinRenderer
import com.tungsten.fcllibrary.skin.SkinViewer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 账户页 Compose 界面（小步骤 3.5）：ui_account.xml + item_account.xml +
 * item_authlib_injector_server.xml 的 Miuix 重构。
 *
 * 布局对齐遗留：左侧 30% 栏（"创建账户"分组头 + 离线/微软入口 + 外置登录服务器列表 +
 * 添加服务器入口），右侧 70% 栏（当前账户 3D 皮肤预览 + 账户卡片列表）。
 * 根布局保持透明（露出用户壁纸），与遗留 ui_account.xml 透明根一致。
 *
 * 行为承接：Composable 只读 uiState、只调 ViewModel 语义化方法；
 * 弹窗/文件选择等一次性副作用经 onEvent 转 [AccountScreenHost]；
 * 登录/刷新/换肤业务链路全部留在遗留 AccountListItem（零重写）。
 *
 * 皮肤预览：FCLLibrary 红线组件 SkinViewer（GLSurfaceView + SkinRenderer）用 AndroidView
 * 原样包装（同 3.2 MiuixOfflineAccountSkinDialog 的 GL 处置），纹理跟随选中账户的
 * AccountListItem.textureProperty（refreshSkinBinding 重绑后此处自动更新）；
 * GL 生命周期由 [ComposeAccountUI] 的 onStart/onStop/onPause/onResume 转发。
 */
@Composable
fun AccountScreen(
    onEvent: (AccountEvent) -> Unit = {},
) {
    val context = LocalContext.current
    val viewModel: AccountViewModel = viewModel(initializer = {
        AccountViewModel(context.applicationContext as Application)
    })
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { onEvent(it) }
    }

    // 承接遗留 refresh() 契约（UIManager.accountUI.refresh() 反向调用点）
    DisposableEffect(viewModel) {
        ComposeAccountUI.refreshHook = { viewModel.reloadAccounts() }
        onDispose { ComposeAccountUI.refreshHook = null }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        // ---------- 左栏：创建账户入口 + 外置服务器列表（对齐 ui_account.xml :8-131，30% 宽） ----------
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f),
        ) {
            Text(
                text = stringResource(R.string.account_create),
                fontSize = 11.sp,
                color = MiuixTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 10.dp),
            )
            Spacer(
                Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(1.dp),
            )
            AddAccountEntry(
                icon = R.drawable.ic_baseline_person_add_24,
                label = stringResource(R.string.account_methods_offline),
                onClick = { viewModel.onAddAccount(Accounts.FACTORY_OFFLINE) },
            )
            AddAccountEntry(
                icon = R.drawable.ic_baseline_microsoft_24,
                label = stringResource(R.string.account_methods_microsoft),
                onClick = { viewModel.onAddAccount(Accounts.FACTORY_MICROSOFT) },
            )
            ServerListColumn(
                servers = state.servers,
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
            AddAccountEntry(
                icon = com.tungsten.fcllibrary.R.drawable.ic_baseline_add_24,
                label = stringResource(R.string.account_create_server),
                onClick = viewModel::onAddAuthlibServer,
            )
        }

        Spacer(Modifier.width(10.dp))

        // ---------- 右栏：选中账户皮肤预览 + 账户列表（70% 宽） ----------
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f),
        ) {
            SelectedAccountSkinPreview(state = state)
            Spacer(Modifier.height(3.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.accounts, key = { it.account.uuid.toString() }) { item ->
                    AccountRow(
                        item = item,
                        selected = item.account == state.selectedAccount,
                        viewModel = viewModel,
                    )
                }
            }
        }
    }

    AccountDialogs(state = state, viewModel = viewModel)
}

/** 左栏入口行（对齐 ui_account.xml 的 offline/microsoft/add_login_server 行）。 */
@Composable
private fun AddAccountEntry(
    icon: Int,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MiuixTheme.colorScheme.primary,
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = label,
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/** 外置登录服务器列表（对齐 ServerListAdapter：name/url 两行 + 删除按钮 + 点击建号）。 */
@Composable
private fun ServerListColumn(
    servers: List<AuthlibInjectorServer>,
    viewModel: AccountViewModel,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(servers, key = { it.url }) { server ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onServerClick(server) }
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = server.name,
                        style = MiuixTheme.textStyles.body2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    BasicText(
                        text = server.url,
                        style = MiuixTheme.textStyles.body2.copy(
                            fontSize = 11.sp,
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        ),
                        maxLines = 1,
                        // 对齐 item_authlib_injector_server.xml 的 URL 跑马灯
                        modifier = Modifier.basicMarquee(),
                    )
                }
                Spacer(Modifier.width(10.dp))
                IconButton(onClick = { viewModel.onDeleteServerClick(server) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_close_24),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MiuixTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

/**
 * 选中账户的 3D 皮肤预览（GL 渲染，AndroidView 原样包装 SkinViewer）。
 * 纹理取选中账户 AccountListItem 的 textureProperty（fakefx），换肤重绑后自动更新；
 * 无选中账户时显示默认 alex 皮肤（对齐 MainUI.setupSkinDisplay :154-156）。
 */
@Composable
private fun SelectedAccountSkinPreview(state: AccountUiState) {
    val defaultSkin = remember {
        BitmapFactory.decodeStream(
            AccountScreenHost::class.java.getResourceAsStream("/assets/img/alex.png")
        )
    }
    val selectedItem = state.accounts.firstOrNull { it.account == state.selectedAccount }
    val texture = selectedItem?.textureProperty()?.collectAsState()?.value
    val rendererHolder = remember { mutableStateOf<SkinRenderer?>(null) }

    Card(modifier = Modifier.fillMaxWidth().height(240.dp)) {
        AndroidView(
            factory = { ctx ->
                SkinViewer(ctx).also { viewer ->
                    val renderer = SkinRenderer(ctx)
                    viewer.setRenderer(renderer, 5f)
                    rendererHolder.value = renderer
                    ComposeAccountUI.activeSkinViewer = viewer
                }
            },
            update = {
                val bitmaps = texture
                if (bitmaps != null && bitmaps.isNotEmpty()) {
                    rendererHolder.value?.setTexture(bitmaps[0], bitmaps.getOrNull(1))
                } else {
                    rendererHolder.value?.setTexture(defaultSkin, null)
                }
            },
            onRelease = { viewer ->
                viewer.onPause()
                if (ComposeAccountUI.activeSkinViewer === viewer) {
                    ComposeAccountUI.activeSkinViewer = null
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

/** 账户卡片（对齐 item_account.xml：单选 + 头像 + 名称/类型 + 皮肤/刷新/复制UUID/编辑/删除）。 */
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
private fun AccountRow(
    item: AccountListItem,
    selected: Boolean,
    viewModel: AccountViewModel,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val title by item.titleProperty().collectAsState()
    val subtitle by item.subtitleProperty().collectAsState()
    val avatar by item.imageProperty().collectAsState()
    // canUploadSkin() 每次调用都新建绑定，remember 缓存避免重组时反复订阅
    val canUploadSkin by remember(item) { item.canUploadSkin() }.collectAsState()
    var refreshing by remember { mutableStateOf(false) }
    var skinBusy by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 单选：设为当前账户（对齐 AccountListAdapter.kt:63-66）
            RadioButton(
                selected = selected,
                onClick = { viewModel.onSelectAccount(item) },
            )
            // 头像：TexturesLoader fakefx 绑定产物（本地生成的 BitmapDrawable），
            // 图片加载按 bridge-api.md §4 决策走 glide-compose
            GlideImage(
                model = avatar,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                BasicText(
                    text = title,
                    style = MiuixTheme.textStyles.body1.copy(
                        color = MiuixTheme.colorScheme.onSurface,
                    ),
                    maxLines = 1,
                    // 对齐 item_account.xml 的名称跑马灯
                    modifier = Modifier.basicMarquee(),
                )
                BasicText(
                    text = subtitle,
                    style = MiuixTheme.textStyles.body2.copy(
                        fontSize = 11.sp,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    ),
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(),
                )
            }
            // 皮肤按钮：点击换肤（按账户类型分支），长按（仅离线）选本地 .png
            // （对齐 AccountListAdapter.kt:88-149 + :184-198）
            if (canUploadSkin) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .combinedClickable(
                            onClick = {
                                when (item.account) {
                                    // 离线账户：皮肤设置弹窗（3.2 弹窗开关双分支）
                                    is OfflineAccount -> viewModel.onOfflineSkinClick(item)
                                    // AuthlibInjector：旧代码在后台线程阻塞等待 FileBrowser 结果
                                    // （CountDownLatch），Compose 侧改为协程承接，契约不变
                                    is AuthlibInjectorAccount -> scope.launch {
                                        val task = withContext(Dispatchers.IO) {
                                            runCatching { item.uploadSkin()?.get() }
                                                .onFailure { it.printStackTrace() }
                                                .getOrNull()
                                        }
                                        if (task != null) {
                                            skinBusy = true
                                            task.whenComplete(Schedulers.androidUIThread()) {
                                                skinBusy = false
                                                item.refreshSkinBinding()
                                            }.start()
                                        }
                                    }
                                    // 其他（Microsoft 等）：uploadSkin 立即完成（打开换肤网页），
                                    // 对齐旧 Adapter 主线程直取
                                    else -> {
                                        val task = runCatching { item.uploadSkin()?.get() }
                                            .onFailure { it.printStackTrace() }
                                            .getOrNull()
                                        if (task != null) {
                                            skinBusy = true
                                            task.whenComplete(Schedulers.androidUIThread()) {
                                                skinBusy = false
                                                item.refreshSkinBinding()
                                            }.start()
                                        }
                                    }
                                }
                            },
                            onLongClick = { viewModel.onPickLocalSkin(item) },
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (skinBusy) {
                        InfiniteProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MiuixTheme.colorScheme.primary,
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_hanger_24),
                            contentDescription = null,
                            tint = MiuixTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
            // 刷新凭据（对齐 AccountListAdapter.kt:67-87：进度圈切换 + 失败弹窗 + 刷新皮肤绑定）
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable(enabled = !refreshing) {
                        refreshing = true
                        item.refreshAsync()
                            .whenComplete(Schedulers.androidUIThread()) { ex ->
                                refreshing = false
                                if (ex != null) {
                                    viewModel.onActionError(ex)
                                }
                                item.refreshSkinBinding()
                            }
                            .start()
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (refreshing) {
                    InfiniteProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MiuixTheme.colorScheme.primary,
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_refresh_24),
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onSurface,
                    )
                }
            }
            // 复制 UUID（对齐 AccountListAdapter.kt:143-146）
            IconButton(
                onClick = {
                    copyToClipBoard(context, item.account.uuid.toString())
                    Toast.makeText(context, R.string.message_copy, Toast.LENGTH_SHORT).show()
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_content_copy_24),
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.onSurface,
                )
            }
            // 编辑 UUID（仅离线账户，对齐 AccountListAdapter.kt:60-62）
            if (item.account is OfflineAccount) {
                IconButton(onClick = { viewModel.onEditUuidClick(item) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_edit_24),
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onSurface,
                    )
                }
            }
            // 删除（对齐 AccountListAdapter.kt:168-183）
            IconButton(onClick = { viewModel.onDeleteAccountClick(item) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_delete_24),
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

/** 页内确认弹窗（Miuix FCLDialog）：删除账户 / 删除服务器 / 离线账户改 UUID。 */
@Composable
private fun AccountDialogs(
    state: AccountUiState,
    viewModel: AccountViewModel,
) {
    val positive = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive)
    val negative = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative)
    when (val dialog = state.dialog) {
        is AccountDialogState.DeleteAccount -> FCLDialog(
            show = true,
            onDismissRequest = viewModel::onDialogDismiss,
            summary = String.format(
                stringResource(R.string.version_manage_remove_confirm),
                dialog.item.title,
            ),
            buttons = listOf(
                FCLDialogButton(positive, onClick = { viewModel.onConfirmDeleteAccount() }),
                FCLDialogButton(negative, onClick = { viewModel.onDialogDismiss() }),
            ),
        )

        is AccountDialogState.DeleteServer -> FCLDialog(
            show = true,
            onDismissRequest = viewModel::onDialogDismiss,
            summary = String.format(
                stringResource(R.string.version_manage_remove_confirm),
                dialog.server.name,
            ),
            buttons = listOf(
                FCLDialogButton(positive, onClick = { viewModel.onConfirmDeleteServer() }),
                FCLDialogButton(negative, onClick = { viewModel.onDialogDismiss() }),
            ),
        )

        is AccountDialogState.EditUuid -> FCLDialog(
            show = true,
            onDismissRequest = viewModel::onDialogDismiss,
            title = stringResource(R.string.settings_advanced_custom_uuid),
            buttons = listOf(
                FCLDialogButton(positive, onClick = { viewModel.onConfirmEditUuid() }),
                FCLDialogButton(negative, onClick = { viewModel.onDialogDismiss() }),
            ),
        ) {
            TextField(
                value = dialog.text,
                onValueChange = viewModel::onEditUuidTextChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
        }

        AccountDialogState.None -> Unit
    }
}

/** 账户页宿主事件处理：弹窗双分支（3.2 开关）与文件选择/错误提示。 */
object AccountScreenHost {
    fun handle(context: Context, event: AccountEvent) {
        when (event) {
            is AccountEvent.CreateAccount -> {
                // 对齐 AccountUI.onClick :77-90（含 3.2 弹窗开关）
                if (ComposeDialogs.USE_COMPOSE_CREATE_ACCOUNT) {
                    MiuixCreateAccountDialog(context, event.factory).show()
                } else {
                    CreateAccountDialog(context, event.factory).show()
                }
            }

            AccountEvent.AddAuthlibServer -> {
                // 对齐 AccountUI.onClick :91-99
                if (ComposeDialogs.USE_COMPOSE_ADD_AUTHLIB_INJECTOR_SERVER) {
                    MiuixAddAuthlibInjectorServerDialog(context).show()
                } else {
                    AddAuthlibInjectorServerDialog(context).show()
                }
            }

            is AccountEvent.CreateAccountForServer -> {
                // 对齐 ServerListAdapter :74-82
                if (ComposeDialogs.USE_COMPOSE_CREATE_ACCOUNT) {
                    MiuixCreateAccountDialog(context, event.server).show()
                } else {
                    CreateAccountDialog(context, event.server).show()
                }
            }

            is AccountEvent.OpenOfflineSkin -> {
                // 对齐 AccountListAdapter.kt:119-126
                if (ComposeDialogs.USE_COMPOSE_OFFLINE_ACCOUNT_SKIN) {
                    MiuixOfflineAccountSkinDialog(context, event.item).show()
                } else {
                    OfflineAccountSkinDialog(context, event.item).show()
                }
            }

            is AccountEvent.PickLocalSkin -> {
                // 对齐 AccountListAdapter.kt:184-198：离线账户长按选本地皮肤
                MainActivity.getInstance().fileLauncher.launchSingleSelection(
                    null,
                    listOf(".png"),
                ) { files ->
                    (event.item.account as OfflineAccount).skin =
                        Skin(Skin.Type.LOCAL_FILE, "", null, files[0], null)
                    event.item.refreshSkinBinding()
                }
            }

            is AccountEvent.ShowError -> {
                // Compose 前台优先走 LegacyDialogHost；槽位被占回退遗留 FCLAlertDialog
                // （对齐 AccountListAdapter.kt:76-84）
                val accepted = LegacyBridge.requestAlertDialog(
                    null,
                    event.message,
                    null,
                    context.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                    null,
                )
                if (!accepted) {
                    val builder = FCLAlertDialog.Builder(context)
                    builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                    builder.setMessage(event.message)
                    builder.setNegativeButton(
                        context.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                        null,
                    )
                    builder.create().show()
                }
            }

            is AccountEvent.ShowToast -> {
                Toast.makeText(context, event.resId, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
