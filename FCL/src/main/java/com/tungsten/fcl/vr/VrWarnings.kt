package com.tungsten.fcl.vr

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.mio.util.openLink
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.download.QuestVivecraftDownloadPage
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.view.FCLButton
import com.tungsten.fcllibrary.util.ConvertUtils

/** VR 前置条件不满足（缺少/不兼容/冲突的 Vivecraft）时的提示对话框 */
object VrWarnings {
    const val VIVECRAFT_DOWNLOAD_URL = "https://github.com/QuestCraftPlusPlus/VivecraftMod/releases"

    @JvmStatic
    fun messageFor(status: VivecraftInspector.Status): Int = when (status) {
        VivecraftInspector.Status.INCOMPATIBLE -> R.string.settings_vr_vivecraft_incompatible
        VivecraftInspector.Status.CONFLICT -> R.string.settings_vr_vivecraft_conflict
        VivecraftInspector.Status.FAILED -> R.string.settings_vr_vivecraft_scan_failed
        else -> R.string.settings_vr_vivecraft_missing
    }

    /** 提示框三个出口：确认 / 浏览并安装（下载临时页）/ 打开官方发布页；关闭后回调 onDismiss */
    @JvmStatic
    fun show(context: Context, status: VivecraftInspector.Status,
             target: QuestVivecraftTarget, onDismiss: Runnable?) {
        var browseRequested = false
        val dialog = FCLAlertDialog.Builder(context)
            .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
            .setTitle(context.getString(R.string.settings_vr_requirements_title))
            .setMessage(context.getString(messageFor(status)))
            .setPositiveButton(context.getString(R.string.dialog_positive), null)
            .setNeutralButton(context.getString(R.string.qv_download_browse)) { browseRequested = true }
            .setNegativeButton(context.getString(R.string.settings_vr_vivecraft_download)) {
                openLink(context, VIVECRAFT_DOWNLOAD_URL)
            }
            .create()
        stackWarningButtons(dialog)
        dialog.setOnDismissListener {
            onDismiss?.run()
            if (browseRequested) {
                Handler(Looper.getMainLooper()).post { QuestVivecraftDownloadPage.open(target) }
            }
        }
        dialog.show()
    }

    /** 按钮文案较长，改为纵向堆叠并允许换行 */
    private fun stackWarningButtons(dialog: FCLAlertDialog) {
        val actions = dialog.findViewById<FCLButton>(R.id.positive)?.parent as? LinearLayoutCompat ?: return
        actions.orientation = LinearLayoutCompat.VERTICAL
        var hasVisibleButton = false
        for (id in intArrayOf(R.id.positive, R.id.neutral, R.id.extra, R.id.negative)) {
            val button = dialog.findViewById<FCLButton>(id) ?: continue
            val params = LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            if (hasVisibleButton && button.visibility != View.GONE) {
                params.topMargin = ConvertUtils.dip2px(dialog.context, 4f)
            }
            button.layoutParams = params
            button.isSingleLine = false
            button.maxLines = Int.MAX_VALUE
            button.setHorizontallyScrolling(false)
            button.ellipsize = null
            button.minHeight = ConvertUtils.dip2px(dialog.context, 48f)
            hasVisibleButton = hasVisibleButton || button.visibility != View.GONE
        }
    }
}
