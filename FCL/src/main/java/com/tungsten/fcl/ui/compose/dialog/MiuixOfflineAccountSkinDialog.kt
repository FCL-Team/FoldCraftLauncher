package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.ui.account.AccountListItem
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.auth.offline.OfflineAccount
import com.tungsten.fclcore.auth.offline.Skin
import com.tungsten.fclcore.auth.yggdrasil.TextureModel
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.skin.SkinRenderer
import com.tungsten.fcllibrary.skin.SkinViewer
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLCornerRadius
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import com.tungsten.fcl.ui.compose.FCLTextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.logging.Level

/**
 * Miuix 版离线账户皮肤设置弹窗（3.2 批 3，对应 ui/account/OfflineAccountSkinDialog
 * + dialog_offline_account_skin）。
 *
 * **GL 预览处置**：内嵌 3D 皮肤预览为 FCLLibrary 红线组件 SkinViewer（GLSurfaceView
 * + SkinRenderer，手写单指旋转/双指缩放手势），用 AndroidView 原样包装保留原生渲染，
 * 其余 UI（5 种皮肤类型单选、皮肤/披风路径、CSL 地址、按钮区）Miuix 化。
 * 生命周期对齐遗留：show() 后 skinView.onResume() + refreshSkin()，
 * dismiss() 前 skinView.onPause()（AndroidView onRelease 兜底 onPause）。
 *
 * 行为对齐：
 * - 初始单选/路径/CSL 地址取自 account.skin（isFirst 去抖：初始化不触发 refreshSkin，
 *   首次 refreshSkin 由 show() 显式触发，与遗留一致）；
 * - 之后任一输入变更（单选切换、路径选择、CSL 逐键输入）立即 refreshSkin 实时预览；
 * - 皮肤/披风路径按钮拉起 .png 单选；确定 → account.setSkin + refreshSkinBinding + dismiss；
 * - 窗体尺寸对齐遗留：宽 = 屏宽 2/3，高 = 横屏 MATCH_PARENT / 竖屏 屏高 2/3；
 * - setCancelable(false) 一致。
 */
class MiuixOfflineAccountSkinDialog(
    context: Context,
    private val accountListItem: AccountListItem,
) : FCLComposeDialog(context, cancelable = false) {

    private val account: OfflineAccount = accountListItem.account as OfflineAccount
    private val renderer = SkinRenderer(context)
    private var skinView: SkinViewer? = null

    private val typeState = mutableStateOf(Skin.Type.DEFAULT)
    private val cslUrlState = mutableStateOf("")
    private val skinPathState = mutableStateOf<String?>(null)
    private val capePathState = mutableStateOf<String?>(null)

    init {
        account.skin?.let { skin ->
            typeState.value = when (skin.type) {
                Skin.Type.STEVE -> Skin.Type.STEVE
                Skin.Type.ALEX -> Skin.Type.ALEX
                Skin.Type.LOCAL_FILE -> Skin.Type.LOCAL_FILE
                Skin.Type.CUSTOM_SKIN_LOADER_API -> Skin.Type.CUSTOM_SKIN_LOADER_API
                else -> Skin.Type.DEFAULT
            }
            skinPathState.value = skin.localSkinPath
            capePathState.value = skin.localCapePath
            cslUrlState.value = skin.cslApi ?: ""
        }

        setDialogContent {
            DialogContent()
        }
    }

    override fun show() {
        super.show()
        // 对齐遗留：宽 = 屏宽 2/3；横屏（高*2<宽）高撑满，竖屏高 = 屏高 2/3
        val width = AndroidUtils.getScreenWidth()
        var height = AndroidUtils.getScreenHeight()
        height = if (height * 2 < width) {
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            height * 2 / 3
        }
        window?.setLayout(width * 2 / 3, height)
        skinView?.onResume()
        Logging.LOG.log(Level.INFO, "========== refreshSkin by show() ==========")
        refreshSkin()
    }

    override fun dismiss() {
        skinView?.onPause()
        super.dismiss()
    }

    private val skin: Skin
        get() = Skin(
            typeState.value,
            cslUrlState.value,
            null,
            if (StringUtils.isBlank(skinPathState.value)) null else skinPathState.value,
            if (StringUtils.isBlank(capePathState.value)) null else capePathState.value,
        )

    private fun refreshSkin() {
        this.skin.load(account.username)
            .whenComplete(Schedulers.androidUIThread()) { result: Skin.LoadedSkin?, exception: Exception? ->
                if (exception != null) {
                    Logging.LOG.log(Level.WARNING, "Failed to load skin", exception)
                    Toast.makeText(
                        context,
                        context.getString(R.string.message_failed),
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    if (result == null || result.skin == null && result.cape == null) {
                        renderer.setTexture(
                            TexturesLoader.getDefaultSkin(
                                TextureModel.detectUUID(account.uuid),
                            ).image, null,
                        )
                        return@whenComplete
                    }
                    renderer.setTexture(
                        if (result.skin != null) result.skin.image
                        else TexturesLoader.getDefaultSkin(
                            TextureModel.detectUUID(account.uuid),
                        ).image,
                        if (result.cape != null) result.cape.image else null,
                    )
                }
            }.start()
    }

    @Composable
    private fun DialogContent() {
        FCLCard(
            cornerRadius = FCLCornerRadius.Dialog,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            insideMargin = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Column(Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.account_skin),
                    modifier = Modifier.fillMaxWidth(),
                    style = MiuixTheme.textStyles.title4,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.weight(1f).fillMaxWidth()) {
                    AndroidView(
                        factory = { ctx ->
                            SkinViewer(ctx).also { viewer ->
                                viewer.setRenderer(renderer, 5f)
                                skinView = viewer
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onRelease = { it.onPause() },
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(start = 8.dp),
                    ) {
                        SkinTypeRadio(Skin.Type.DEFAULT, stringResource(R.string.account_skin_type_default))
                        SkinTypeRadio(Skin.Type.STEVE, stringResource(R.string.account_skin_type_steve))
                        SkinTypeRadio(Skin.Type.ALEX, stringResource(R.string.account_skin_type_alex))
                        SkinTypeRadio(Skin.Type.LOCAL_FILE, stringResource(R.string.account_skin_type_local_file))
                        SkinTypeRadio(Skin.Type.CUSTOM_SKIN_LOADER_API, stringResource(R.string.account_skin_type_csl_api))
                        if (typeState.value == Skin.Type.LOCAL_FILE) {
                            LocalFileSection()
                        }
                        if (typeState.value == Skin.Type.CUSTOM_SKIN_LOADER_API) {
                            CslSection()
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = {
                            account.skin = skin
                            accountListItem.refreshSkinBinding()
                            dismiss()
                        },
                    )
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = { dismiss() },
                    )
                }
            }
        }
    }

    @Composable
    private fun SkinTypeRadio(type: Skin.Type, label: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    typeState.value = type
                    refreshSkin()
                }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = typeState.value == type,
                onClick = {
                    typeState.value = type
                    refreshSkin()
                },
            )
            Spacer(Modifier.width(4.dp))
            // 对齐遗留 text_use_theme_color="true"：单选文字用主色（FCLRadioButton color|0xFF000000）
            Text(
                text = label,
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.primary,
            )
        }
    }

    @Composable
    private fun LocalFileSection() {
        PathRow(
            label = stringResource(R.string.account_skin),
            path = skinPathState.value,
        ) {
            MainActivity.getInstance().fileLauncher.launchSingleSelection(
                null,
                listOf(".png"),
            ) {
                skinPathState.value = it?.get(0) ?: return@launchSingleSelection
                refreshSkin()
            }
        }
        PathRow(
            label = stringResource(R.string.account_cape),
            path = capePathState.value,
        ) {
            MainActivity.getInstance().fileLauncher.launchSingleSelection(
                null,
                listOf(".png"),
            ) {
                capePathState.value = it?.get(0) ?: return@launchSingleSelection
                refreshSkin()
            }
        }
    }

    @Composable
    private fun PathRow(label: String, path: String?, onPick: () -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(20.dp))
            IconButton(onClick = onPick) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_edit_24),
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.onSurface,
                )
            }
        }
        Text(
            text = path ?: "",
            style = MiuixTheme.textStyles.footnote1,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        )
    }

    @Composable
    private fun CslSection() {
        // 对齐遗留 bg_container_white + auto_text_background_tint + auto_text_tint：
        // 5dp 圆角底染主色（color），文字按主色亮度取黑/白（autoTint）
        Text(
            text = stringResource(R.string.account_skin_type_csl_api_hint),
            style = MiuixTheme.textStyles.footnote1,
            color = MiuixTheme.colorScheme.onPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(MiuixTheme.colorScheme.primary, RoundedCornerShape(5.dp))
                .padding(10.dp),
        )
        FCLTextField(
            value = cslUrlState.value,
            onValueChange = {
                cslUrlState.value = it
                refreshSkin()
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = stringResource(R.string.account_skin_type_csl_api_location_hint),
        )
    }
}
