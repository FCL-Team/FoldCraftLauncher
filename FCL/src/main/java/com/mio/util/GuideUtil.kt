package com.mio.util

import android.app.Activity
import android.view.View
import androidx.datastore.core.DataStore
import com.mio.datastore.GuidePreference
import com.mio.datastore.guideDataStore
import com.mio.ui.view.GuideOverlayView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 一步功能引导：tag 用于防重复弹出，target 为高亮目标控件，text 为气泡描述文案
 */
data class GuideStep(val tag: String, val target: View, val text: String)

/**
 * 功能引导入口：按传入顺序逐步展示全屏引导遮罩，已展示过的 tag 自动过滤
 * （DataStore 持久化）；目标不可见的步骤静默跳过且不记录，下次仍会展示；
 * 跳过操作会记录剩余全部步骤，避免再次打扰。
 */
object GuideUtil {
    const val TAG_GUIDE_ACCOUNT = "account"
    const val TAG_GUIDE_VERSION_CARD = "version card"
    const val TAG_GUIDE_START = "start"
    const val TAG_GUIDE_MANAGE = "manage"
    const val TAG_GUIDE_DOWNLOAD = "download"
    const val TAG_GUIDE_CONTROLLER = "controller"
    const val TAG_GUIDE_MULTIPLAYER = "multiplayer"
    const val TAG_GUIDE_THEME_2 = "theme2"
    const val TAG_GUIDE_SHARE_LOG = "share log"

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun show(activity: Activity, vararg steps: GuideStep) {
        val dataStore = activity.applicationContext.guideDataStore
        scope.launch {
            val shown = dataStore.data.first().shownTags.toSet()
            val pending = steps.filter { it.tag !in shown }
            if (pending.isEmpty()) return@launch
            withContext(Dispatchers.Main) {
                GuideOverlayView.show(
                    activity,
                    pending,
                    onStepShown = { markShown(dataStore, listOf(it.tag)) },
                    onSkipped = { remaining -> markShown(dataStore, remaining.map { it.tag }) },
                )
            }
        }
    }

    private fun markShown(dataStore: DataStore<GuidePreference>, tags: List<String>) {
        scope.launch {
            dataStore.updateData { it.copy(shownTags = (it.shownTags + tags).distinct()) }
        }
    }
}
