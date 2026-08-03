package com.tungsten.fcl.ui.main.compose

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tungsten.fcl.R
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.compose.FCLDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.main.Announcement
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.HttpRequest
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.skin.SkinRenderer
import com.tungsten.fcllibrary.skin.SkinViewer
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.logging.Level

/** 公告地址（对齐已删除旧 MainUI 的同名常量，6.1 批 3 收编）。 */
private const val ANNOUNCEMENT_URL = "https://raw.githubusercontent.com/FCL-Team/FCL-Repo/refs/heads/main/res/announcement_v2.txt"
private const val ANNOUNCEMENT_URL_CN = "https://gitee.com/fcl-team/FCL-Repo/raw/main/res/announcement_v2.txt"

/**
 * 主页 Compose 界面（小步骤 3.6）：ui_main.xml 的 Miuix 重构。
 *
 * 布局对齐遗留：根 10dp padding；皮肤 3D 预览居中（50% 宽 × 80% 高）；
 * 公告栏靠左（40% 宽 × 全高，主题色着色容器 + 标题/内容/日期 + 隐藏按钮）。
 * 根布局保持透明（露出用户壁纸），与遗留 ui_main.xml 一致。
 *
 * 行为承接：
 * - 公告拉取/展示/隐藏逻辑逐行对齐 MainUI.checkAnnouncement/hideAnnouncement
 *   （含 shouldDisplay 过滤与 ignore_announcement 持久化）；重要公告隐藏前弹确认
 *   （对齐 MainUI.onClick 的 FCLAlertDialog ALERT 形态）；
 * - 公告正文链接：对齐 autoLink="web" + textColorLink #77FF00，Compose 侧用
 *   LinkAnnotation.Url 实现（foundation 1.7+，BasicText 自带点击打开浏览器）；
 * - 皮肤预览：FCLLibrary 红线组件 SkinViewer（GLSurfaceView + SkinRenderer）用
 *   AndroidView 原样包装（同 3.5 AccountScreen 的 GL 处置），纹理经
 *   TexturesLoader.textureBinding 跟随选中账户，无账户时显示默认 alex 皮肤；
 * - close_skin_model 开关（Theme.java:212，"theme" prefs）控制皮肤显隐，
 *   GL 生命周期由 [ComposeMainUI] 转发。
 */
@Composable
fun MainScreen() {
    val context = LocalContext.current

    // ---------- 公告状态（对齐 MainUI.checkAnnouncement :122-139） ----------
    var announcement by remember { mutableStateOf<Announcement?>(null) }
    var announcementVisible by remember { mutableStateOf(false) }
    var showSignificantDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val result = withContext(Dispatchers.IO) {
            runCatching {
                val url =
                    if (LocaleUtils.isChinese(context)) ANNOUNCEMENT_URL_CN else ANNOUNCEMENT_URL
                HttpRequest.HttpGetRequest.GET(url).getJson(Announcement::class.java)
            }.onFailure {
                Logging.LOG.log(Level.WARNING, "Failed to get announcement!", it)
            }.getOrNull()
        }
        if (result != null && result.shouldDisplay(context)) {
            announcement = result
            announcementVisible = true
        }
    }

    // ---------- 皮肤模型开关（closeSkinModelFlow 单一数据源，Theme.java:146） ----------
    // 开关路径：LauncherSettingViewModel.setCloseSkinModel → Theme.setiIgnoreSkinContainer
    // 写 flow 并 saveTheme 落盘，flow 与 prefs 同步更新，直接 collect flow 等价于
    // 原来的 "ThemeEngine 初值 + prefs 监听" 双轨读。
    val skinModelClosed by ThemeEngine.getInstance().theme.closeSkinModelFlow().collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        // 皮肤 3D 预览：居中 50% 宽 × 80% 高（对齐 ui_main.xml :87-95）
        if (!skinModelClosed) {
            SkinModelPreview(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.8f),
            )
        }

        // 公告栏：靠左 40% 宽 × 全高（对齐 ui_main.xml :8-85）
        val current = announcement
        if (announcementVisible && current != null) {
            AnnouncementPanel(
                announcement = current,
                onHide = {
                    if (current.isSignificant) {
                        showSignificantDialog = true
                    } else {
                        announcementVisible = false
                        current.hide(context)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(0.4f)
                    .fillMaxHeight(),
            )
        }
    }

    // 重要公告隐藏确认（对齐 MainUI.onClick :175-187 的 FCLAlertDialog ALERT，不可取消）
    if (showSignificantDialog) {
        FCLDialog(
            show = true,
            onDismissRequest = null,
            summary = stringResource(R.string.announcement_significant),
            buttons = listOf(
                FCLDialogButton(
                    stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                    onClick = {
                        showSignificantDialog = false
                        announcementVisible = false
                        announcement?.hide(context)
                    },
                ),
                FCLDialogButton(
                    stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                    onClick = { showSignificantDialog = false },
                ),
            ),
        )
    }
}

/**
 * 公告栏（对齐 ui_main.xml 的 announcement_container：主题色着色容器 +
 * 标题/分隔线/可滚动正文/分隔线/日期 + 隐藏按钮）。
 * 遗留用 ThemeEngine registerEvent 给 bg_container_white 染主题色、文字 auto_text_tint，
 * Compose 侧直接取 MiuixTheme.colorScheme.primary / onPrimary（同一 token 来源）。
 */
@Composable
private fun AnnouncementPanel(
    announcement: Announcement,
    onHide: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MiuixTheme.colorScheme.primary, RoundedCornerShape(5.dp)),
        ) {
            Text(
                text = stringResource(
                    R.string.announcement,
                    announcement.getDisplayTitle(context),
                ),
                color = MiuixTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            )
            HorizontalDivider(thickness = 1.dp, color = FCLThemeTokens.StrokeGray)
            BasicText(
                text = linkifyAnnouncement(announcement.getDisplayContent(context)),
                style = MiuixTheme.textStyles.body1.copy(
                    color = MiuixTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp),
            )
            HorizontalDivider(thickness = 1.dp, color = FCLThemeTokens.StrokeGray)
            Text(
                text = stringResource(R.string.update_date, announcement.date),
                color = MiuixTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            )
        }
        Spacer(Modifier.height(10.dp))
        Button(
            onClick = onHide,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColorsPrimary(),
        ) {
            Text(text = stringResource(R.string.button_hide))
        }
    }
}

private val announcementUrlRegex = Regex("""https?://\S+""")

/** 对齐遗留 autoLink="web" + textColorLink #77FF00：URL 片段渲染为可点击链接。 */
private fun linkifyAnnouncement(text: String) = buildAnnotatedString {
    var lastEnd = 0
    for (match in announcementUrlRegex.findAll(text)) {
        append(text.substring(lastEnd, match.range.first))
        withLink(LinkAnnotation.Url(match.value)) {
            withStyle(
                SpanStyle(
                    color = FCLThemeTokens.AnnouncementLink,
                    textDecoration = TextDecoration.Underline,
                ),
            ) {
                append(match.value)
            }
        }
        lastEnd = match.range.last + 1
    }
    append(text.substring(lastEnd))
}

/**
 * 账户皮肤 3D 预览（GL 渲染，AndroidView 原样包装 SkinViewer）。
 * 纹理经 TexturesLoader.textureBinding 跟随选中账户（refreshSkin 节拍推进后重建绑定），
 * 无账户时显示默认 alex 皮肤（对齐 MainUI.setupSkinDisplay :147-162）。
 */
@Composable
private fun SkinModelPreview(modifier: Modifier = Modifier) {
    val account by Accounts.selectedAccountFlow().collectAsState()
    val refreshTick by ComposeMainUI.skinRefreshTick.collectAsState()
    // 默认 alex 皮肤解码属 IO，移出组合期（produceState + Dispatchers.IO）
    val defaultSkin by produceState<Bitmap?>(initialValue = null) {
        value = withContext(Dispatchers.IO) {
            BitmapFactory.decodeStream(ComposeMainUI::class.java.getResourceAsStream("/assets/img/alex.png"))
        }
    }
    val textures by produceState<Array<Bitmap?>?>(initialValue = null, account, refreshTick) {
        val current = account
        if (current == null) {
            value = arrayOf(defaultSkin, null)
        } else {
            val flow = TexturesLoader.textureFlow(current)
            value = flow.value?.let { arrayOf(it.getOrNull(0), it.getOrNull(1)) }
            flow.collect { newValue ->
                value = arrayOf(newValue?.getOrNull(0), newValue?.getOrNull(1))
            }
        }
    }
    val rendererHolder = remember { arrayOfNulls<SkinRenderer>(1) }

    // onStart 时重传纹理（对齐 MainUI.onStart 的 renderer.updateTexture 调用）
    DisposableEffect(textures) {
        ComposeMainUI.textureRefreshHook = {
            val t = textures
            if (t != null) rendererHolder[0]?.updateTexture(t[0], t.getOrNull(1))
        }
        onDispose { ComposeMainUI.textureRefreshHook = null }
    }

    AndroidView(
        factory = { ctx ->
            SkinViewer(ctx).also { viewer ->
                val renderer = SkinRenderer(ctx)
                viewer.setRenderer(renderer, 5f)
                rendererHolder[0] = renderer
                ComposeMainUI.activeSkinViewer = viewer
            }
        },
        update = {
            val t = textures
            if (t != null) rendererHolder[0]?.updateTexture(t[0], t.getOrNull(1))
        },
        onRelease = { viewer ->
            viewer.onPause()
            if (ComposeMainUI.activeSkinViewer === viewer) {
                ComposeMainUI.activeSkinViewer = null
            }
        },
        modifier = modifier,
    )
}
