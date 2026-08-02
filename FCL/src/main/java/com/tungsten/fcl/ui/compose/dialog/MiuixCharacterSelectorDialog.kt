package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclcore.auth.CharacterSelector
import com.tungsten.fclcore.auth.NoSelectedCharacterException
import com.tungsten.fclcore.auth.yggdrasil.GameProfile
import com.tungsten.fclcore.auth.yggdrasil.YggdrasilService
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fcllibrary.util.ConvertUtils
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.concurrent.CountDownLatch

/**
 * Miuix 版离线/外置登录角色选择弹窗（3.2 批 3，对应
 * CreateAccountDialog.DialogCharacterSelector + dialog_character_selector + item_character）。
 *
 * **阻塞契约保持不变**：[select] 由 AccountFactory.create 在后台线程调用，
 * 本类仍在 UI 线程 show 弹窗、后台线程 latch.await() 阻塞等待：
 * - 点选角色 → selectedProfile 赋值 + latch.countDown()；
 * - 取消按钮 → 仅 latch.countDown()（selectedProfile 保持 null → NoSelectedCharacterException）；
 * - await 中断 → NoSelectedCharacterException；
 * - finally dismiss()。
 * 与遗留实现逐行等价，调用方（AccountListItem/factory.create 链路的 CountDownLatch 语义）不受影响。
 *
 * setCancelable(false) 一致。头像改用 TexturesLoader.avatarFlow（StateFlow）订阅进 Compose 状态
 * （Flow 实例由本类强引用，防内部弱引用订阅被 GC），初始值即 uuid 默认皮肤，等价遗留异步加载。
 */
class MiuixCharacterSelectorDialog(
    context: Context,
) : FCLComposeDialog(context, cancelable = false), CharacterSelector {

    private val handler = Handler(Looper.getMainLooper())
    private val latch = CountDownLatch(1)
    private var selectedProfile: GameProfile? = null

    private val profilesState = mutableStateOf<List<GameProfile>>(emptyList())
    private var service: YggdrasilService? = null

    /** 强引用头像 StateFlow（内部订阅弱引用目标，无强引用会被 GC 导致头像停更）。 */
    private val avatarFlows = ArrayList<Any>()

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.account_select_character),
                scrollable = false,
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = {
                            latch.countDown()
                            dismiss()
                        },
                    ),
                ),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp),
                ) {
                    items(profilesState.value) { profile ->
                        service?.let { CharacterRow(it, profile) }
                    }
                }
            }
        }
    }

    @Composable
    private fun CharacterRow(service: YggdrasilService, profile: GameProfile) {
        val avatarState = remember { mutableStateOf<Bitmap?>(null) }
        DisposableEffect(profile.id) {
            val avatarFlow = TexturesLoader.avatarFlow(
                service,
                profile.id,
                ConvertUtils.dip2px(context, 30f),
            )
            avatarFlows.add(avatarFlow)
            // 对齐 FXUtils.onChangeAndOperate：先同步当前值再跟随后续变化
            val subscription = FlowSubscriptions.subscribeWithCurrent(avatarFlow) { drawable ->
                avatarState.value = drawable?.bitmap
            }
            onDispose { subscription.cancel() }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    selectedProfile = profile
                    latch.countDown()
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            avatarState.value?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    filterQuality = FilterQuality.None,
                )
            } ?: Spacer(Modifier.size(30.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = profile.name,
                style = MiuixTheme.textStyles.body2,
            )
        }
    }

    private fun refresh(service: YggdrasilService, profiles: List<GameProfile>) {
        this.service = service
        profilesState.value = profiles
    }

    override fun select(service: YggdrasilService, profiles: List<GameProfile>): GameProfile {
        handler.post {
            refresh(service, profiles)
            show()
        }

        try {
            latch.await()

            if (selectedProfile == null)
                throw NoSelectedCharacterException()

            return selectedProfile!!
        } catch (ignored: InterruptedException) {
            throw NoSelectedCharacterException()
        } finally {
            dismiss()
        }
    }
}
