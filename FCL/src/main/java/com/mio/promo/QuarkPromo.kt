package com.mio.promo

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.mio.datastore.launchCountDataStore
import com.mio.util.openLink
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.random.Random
import androidx.core.content.edit

object QuarkPromo {

    private const val PREF_KEY_NETDISK_URL = "quark_netdisk_url"

    /** UpdateChecker 拉取远程 version_map.json 成功后调用，缓存最新网盘链接 */
    @JvmStatic
    fun updateNetdiskUrl(context: Context, url: String?) {
        if (url.isNullOrEmpty()) return
        val prefs = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
        if (prefs.getString(PREF_KEY_NETDISK_URL, null) == url) return
        prefs.edit { putString(PREF_KEY_NETDISK_URL, url) }
    }

    /**
     * 启动游戏前的推广拦截：仅中文大陆用户参与计数与掷骰子，其余直接放行。
     * 中签但缓存链接为空时保留触发（pendingPopup），下次启动链接可用则直接弹出；
     * 正常中签弹窗，点「打开夸克网盘」跳浏览器并取消本次启动，点「开始游戏」继续启动。
     */
    fun interceptLaunch(activity: FragmentActivity, onProceed: () -> Unit) {
        if (!LocaleUtils.isChinese(activity) || !LocaleUtils.IS_CHINA_MAINLAND) {
            onProceed()
            return
        }
        val netdiskUrl = activity.getSharedPreferences("launcher", Context.MODE_PRIVATE)
            .getString(PREF_KEY_NETDISK_URL, null)
        activity.lifecycleScope.launch {
            val current = activity.launchCountDataStore.data.first()
            val totalCount = current.totalLaunchCount + 1
            // 兑现之前保留的触发：有链接直接弹出，不再掷骰子
            if (current.pendingPopup && netdiskUrl != null) {
                activity.launchCountDataStore.updateData {
                    it.copy(totalLaunchCount = totalCount, pityCounter = 0, pendingPopup = false)
                }
                showDialog(activity, totalCount, netdiskUrl, onProceed)
                return@launch
            }
            val pity = current.pityCounter + 1
            val shown = Random.nextDouble() < popupProbability(pity)
            activity.launchCountDataStore.updateData {
                it.copy(
                    totalLaunchCount = totalCount,
                    pityCounter = if (shown) 0 else pity,
                    pendingPopup = current.pendingPopup || (shown && netdiskUrl == null)
                )
            }
            if (shown && netdiskUrl != null) {
                showDialog(activity, totalCount, netdiskUrl, onProceed)
            } else {
                onProceed()
            }
        }
    }

    // 保底概率：基础 0.6%，第 80 次起每次 +10%（封顶 90%），第 90 次必弹
    fun popupProbability(count: Int): Double = when {
        count >= 90 -> 1.0
        count >= 80 -> min(0.9, 0.1 * (count - 79))
        else -> 0.006
    }

    private fun showDialog(activity: FragmentActivity, totalCount: Int, netdiskUrl: String, onProceed: () -> Unit) {
        FCLAlertDialog.Builder(activity)
            .setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
            .setCancelable(false)
            .setTitle(activity.getString(R.string.quark_promo_title))
            .setMessage(activity.getString(R.string.quark_promo_message, totalCount))
            .setPositiveButton(activity.getString(R.string.quark_promo_open)) {
                openLink(activity, netdiskUrl)
            }
            .setNegativeButton(activity.getString(R.string.quark_promo_start)) {
                onProceed()
            }
            .create()
            .show()
    }
}
