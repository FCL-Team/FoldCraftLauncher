package com.tungsten.fcl.ui.main.compose

import android.graphics.drawable.Drawable
import android.view.animation.BounceInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.basicMarquee
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.tungsten.fcl.R
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.compose.ShakeState
import com.tungsten.fcl.ui.compose.rememberShakeState
import com.tungsten.fcl.ui.compose.shake
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount
import com.tungsten.fclcore.auth.yggdrasil.TextureModel
import com.tungsten.fclcore.observable.value.ChangeListener
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 主界面右侧栏 Compose 状态桥（小步骤 3.6）。
 *
 * 右侧栏（账户头像区/启动区/版本卡片/JAR 按钮）在 activity_main.xml 的 right_menu 内、
 * 跨全部一级界面常驻，业务逻辑（版本加载、头像重绑、控制器未就绪抖动）仍由 MainActivity
 * 持有；本对象只做 MainActivity → Compose 的单向状态投递，不改任何业务链路。
 */
object MainRightMenuBridge {

    /** 版本卡片展示态（MainActivity.loadVersion 写入，Compose 侧订阅渲染）。 */
    data class VersionDisplay(
        val loading: Boolean = true,
        val name: String = "",
        val icon: Drawable? = null,
    )

    val versionDisplay = MutableStateFlow(VersionDisplay())

    /** 头像重绑节拍（MainActivity.refreshAvatar 反向调用 → +1 → Compose 侧重建 avatarBinding）。 */
    val avatarRefreshTick = MutableStateFlow(0)

    /** 启动按钮抖动节拍（控制器未加载完成时的错误反馈，对齐 AnimUtil.playTranslationX 抖动）。 */
    val startShakeTick = MutableStateFlow(0)
}

/**
 * 主界面右侧栏 Compose 界面（小步骤 3.6）：activity_main.xml right_menu 内
 * account/start/version/jar 四个 View 的 Miuix 重构（对齐 :103-278）。
 *
 * 信息层级对齐遗留：账户区（52dp 头像 + 16sp 粗体名 + 12sp 类型/服务器名）居中占满剩余空间；
 * 启动区（"启动"粗体 + 30dp 版本图标 + 版本名跑马灯 + 设置入口 + 加载进度）；
 * 底部版本管理/JAR 执行双按钮（10% 高、1dp color2 描边透明底）。
 * 全部文字/描边色取 MiuixTheme.colorScheme.onSurface（= ThemeEngine color2 token，
 * FCLTheme 已实时对接取色器），面板底色仍由 activity_main.xml 的 bg_right_menu 承载。
 *
 * 交互全部经回调转交 MainActivity（切 UI/启动游戏/渲染器选择/JAR 执行，逻辑零改动）；
 * 入场动画对齐 MainActivity.playAnim（自上方落入 + Bounce 插值 + (index+1)×100ms 错峰）。
 */
@Composable
fun MainRightMenu(
    onAccountClick: () -> Unit,
    onVersionClick: () -> Unit,
    onStartClick: () -> Unit,
    onStartLongClick: () -> Unit,
    onJarClick: () -> Unit,
    onJarLongClick: () -> Unit,
    onGoSettingClick: () -> Unit,
) {
    val animSpeed = remember { ThemeEngine.getInstance().theme.animationSpeed }
    val shakeState = rememberShakeState()
    val shakeTick by MainRightMenuBridge.startShakeTick.collectAsStateWithLifecycle()
    LaunchedEffect(shakeTick) {
        if (shakeTick > 0) shakeState.shake()
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val actionHeight = maxHeight * 0.1f
        Column(Modifier.fillMaxSize()) {
            // 账户区：占满剩余空间并居中（对齐 activity_main.xml :114-167）
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                AccountBlock(onClick = onAccountClick)
            }
            // 启动区（对齐 activity_main.xml :169-241）
            StartBlock(
                onClick = onStartClick,
                onLongClick = onStartLongClick,
                onGoSettingClick = onGoSettingClick,
                shakeState = shakeState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .entrance(index = 0, animSpeed = animSpeed),
            )
            Spacer(Modifier.height(10.dp))
            // 版本管理 / JAR 执行双按钮（对齐 activity_main.xml :243-277，10% 高）
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(actionHeight)
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            ) {
                OutlineActionButton(
                    text = stringResource(R.string.manage_version),
                    onClick = onVersionClick,
                    onLongClick = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 4.dp)
                        .entrance(index = 1, animSpeed = animSpeed),
                )
                OutlineActionButton(
                    text = stringResource(R.string.jar_execute),
                    onClick = onJarClick,
                    onLongClick = onJarLongClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 4.dp)
                        .entrance(index = 2, animSpeed = animSpeed),
                )
            }
        }
    }
}

private val BounceEasing = Easing { BounceInterpolator().getInterpolation(it) }

/**
 * 入场动画修饰符：对齐 MainActivity.playAnim 的 translationY -200→0 +
 * BounceInterpolator + (index+1)×100ms 错峰，时长 = animationSpeed×100。
 */
@Composable
private fun Modifier.entrance(index: Int, animSpeed: Int): Modifier {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay((index + 1) * 100L)
        progress.animateTo(1f, tween(durationMillis = animSpeed * 100, easing = BounceEasing))
    }
    return graphicsLayer { translationY = -200f * (1f - progress.value) }
}

/** 账户区（对齐 activity_main.xml :114-167 + MainActivity.setupAccountDisplay）。 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun AccountBlock(onClick: () -> Unit) {
    val context = LocalContext.current
    val account by Accounts.selectedAccountFlow().collectAsState()
    val avatarTick by MainRightMenuBridge.avatarRefreshTick.collectAsStateWithLifecycle()
    val avatarSize = with(LocalDensity.current) { 52.dp.roundToPx() }
    val defaultAvatar = remember {
        TexturesLoader.toAvatar(
            TexturesLoader.getDefaultSkin(TextureModel.ALEX).image,
            avatarSize,
        ).toDrawable(context.resources)
    }
    // 头像：TexturesLoader.avatarBinding（observable 绑定），refreshAvatar 节拍推进后重建
    val avatar by produceState<Drawable>(defaultAvatar, account, avatarTick) {
        val current = account
        if (current == null) {
            value = defaultAvatar
        } else {
            val binding = TexturesLoader.avatarBinding(current, avatarSize)
            binding.value?.let { value = it }
            val listener = ChangeListener<android.graphics.drawable.BitmapDrawable> { _, _, newValue ->
                newValue?.let { value = it }
            }
            binding.addListener(listener)
            awaitDispose { binding.removeListener(listener) }
        }
    }

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        GlideImage(
            model = avatar,
            contentDescription = null,
            modifier = Modifier.size(52.dp),
        )
        Text(
            text = account?.character ?: stringResource(R.string.account_state_no_account),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MiuixTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp),
        )
        AccountSubtitle(
            account = account,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}

/**
 * 账户副标题（对齐 MainActivity.accountSubtitle）：
 * AuthlibInjector 账户显示服务器名（跟随 revisionFlow 刷新），否则显示本地化登录类型名。
 */
@Composable
private fun AccountSubtitle(account: Account?, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val text = when (account) {
        null -> stringResource(R.string.account_state_add)
        is AuthlibInjectorAccount -> {
            val serverName by remember(account) {
                account.server.revisionFlow()
                    .map { account.server.name }
                    .stateIn(scope, SharingStarted.Eagerly, account.server.name)
            }.collectAsState()
            serverName
        }
        else -> remember(account) {
            Accounts.getLocalizedLoginTypeName(context, Accounts.getAccountFactory(account))
        }
    }
    Text(
        text = text,
        fontSize = 12.sp,
        color = MiuixTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

/**
 * 启动区（对齐 activity_main.xml :169-241）：
 * "启动"粗体 + 版本图标/版本名跑马灯/设置入口；加载中显示进度（对齐 version_progress）。
 * 点击 = 启动游戏，长按 = 选择渲染器后启动（回调转 MainActivity，逻辑零改动）。
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
private fun StartBlock(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onGoSettingClick: () -> Unit,
    shakeState: ShakeState,
    modifier: Modifier = Modifier,
) {
    val display by MainRightMenuBridge.versionDisplay.collectAsStateWithLifecycle()
    Box(
        modifier = modifier
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .shake(shakeState)
            .padding(5.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.launch),
                fontWeight = FontWeight.Bold,
                color = MiuixTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (display.icon != null) {
                    GlideImage(
                        model = display.icon,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.img_grass),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = androidx.compose.ui.graphics.Color.Unspecified,
                    )
                }
                Spacer(Modifier.width(4.dp))
                BasicText(
                    text = display.name,
                    style = MiuixTheme.textStyles.body2.copy(
                        color = MiuixTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    ),
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        // 对齐 version_name 的跑马灯
                        .basicMarquee(),
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_settings_24),
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onGoSettingClick),
                )
            }
        }
        if (display.loading) {
            InfiniteProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp),
                color = MiuixTheme.colorScheme.primary,
            )
        }
    }
}

/**
 * 版本管理 / JAR 执行按钮（对齐 activity_main.xml 的 MaterialButton：
 * 透明底 + 1dp color2 描边 + 8dp 圆角 + color2 文字，见 MainActivity.createBackground）。
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OutlineActionButton(
    text: String,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .border(1.dp, MiuixTheme.colorScheme.onSurface, RoundedCornerShape(8.dp))
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = MiuixTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
