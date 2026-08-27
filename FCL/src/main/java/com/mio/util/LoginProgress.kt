package com.mio.util

import android.content.Context
import android.widget.TextView
import com.tungsten.fcl.R
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fclcore.auth.microsoft.MicrosoftService
import com.tungsten.fclcore.task.Schedulers

/**
 * 微软登录进度辅助：把 [MicrosoftService] 上报的登录阶段映射为本地化文案，
 * 并提供后台线程安全刷新进度文字的回调实现。
 */

/** 登录阶段标识 → 文案资源 id；未知阶段退回通用的「正在登录」 */
fun loginStageText(context: Context, stage: String?): String = when (stage) {
    MicrosoftService.STAGE_XBOX -> context.getString(R.string.login_state_microsoft_xbox)
    MicrosoftService.STAGE_XSTS -> context.getString(R.string.login_state_microsoft_xsts)
    MicrosoftService.STAGE_MINECRAFT -> context.getString(R.string.login_state_microsoft_minecraft)
    MicrosoftService.STAGE_OWNERSHIP -> context.getString(R.string.login_state_microsoft_ownership)
    MicrosoftService.STAGE_PROFILE -> context.getString(R.string.login_state_microsoft_profile)
    else -> context.getString(R.string.launch_state_logging_in)
}

/**
 * 登录进度回调实现：后台线程的阶段通知经主线程调度后刷新进度文字，
 * 避免在回调线程直接触碰 View。
 */
class LoginStageTextBinder(
    private val context: Context,
    private val textView: TextView
) : AccountFactory.ProgressCallback {

    override fun onProgressChanged(stageName: String) {
        Schedulers.androidUIThread().execute {
            textView.text = loginStageText(context, stageName)
        }
    }
}
