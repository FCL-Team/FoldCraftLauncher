package com.tungsten.fcl.ui

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mio.util.disableMouseWheelScroll
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.account.AccountUI
import com.tungsten.fcl.ui.controller.ControllerUI
import com.tungsten.fcl.ui.download.DownloadUI
import com.tungsten.fcl.ui.main.MainUI
import com.tungsten.fcl.ui.manage.ManageUI
import com.tungsten.fcl.ui.multiplayer.MultiplayerUI
import com.tungsten.fcl.ui.setting.SettingUI
import com.tungsten.fcl.ui.version.VersionUI
import com.tungsten.fcllibrary.component.ui.FCLBaseUI
import com.tungsten.fcllibrary.component.ui.FCLCommonUI

/**
 * 主界面 UI 管理器：用 ViewPager2 承载 8 个主 UI 页面。
 *
 * UI 实例随 ViewPager 页面生命周期创建/销毁，不保留状态：
 * 页面被 ViewPager 回收（超出 offscreenPageLimit）时销毁对应 UI 实例，
 * 下次进入时全新创建。
 */
class UIManager(val context: Context, val pager: ViewPager2) {
    companion object {
        @JvmStatic
        lateinit var instance: UIManager
    }

    /** 页面位置 → UI 实例注册表，页面被回收时销毁并清空对应位 */
    private val uiRegistry = arrayOfNulls<FCLCommonUI>(8)

    /** 页面位置 → UI 工厂 */
    private val factories: List<() -> FCLCommonUI> = listOf(
        { MainUI(context, R.layout.ui_main) },
        { ManageUI(context, R.layout.ui_manage) },
        { DownloadUI(context, R.layout.ui_download) },
        { ControllerUI(context, R.layout.ui_controller) },
        { MultiplayerUI(context, R.layout.ui_multiplayer) },
        { SettingUI(context, R.layout.ui_setting) },
        { AccountUI(context, R.layout.ui_account) },
        { VersionUI(context, R.layout.ui_version) }
    )

    var currentUI: FCLBaseUI? = null

    /** 页面切换回调，MainActivity 用于同步菜单高亮与标题 */
    var pageSelectedListener: ((Int) -> Unit)? = null

    /** 上次 onPageSelected 的页面位置，用于过滤 ViewPager2 重复 dispatch 当前页（如软键盘弹出等布局变化） */
    private var lastSelectedPosition = -1

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            currentUI = getUI(position)
            pageSelectedListener?.invoke(position)
            // 统一过渡动画：每次跨页切换都对目标页做淡入 + 上滑进入。
            // 同步执行（不 post）：onPageSelected 时页面已挂载但尚未绘制，此时置透明
            // 不会出现先显示后消失的闪烁。
            // 仅在页面位置真正变化时播放：ViewPager2 在布局变化（如软键盘弹出、页面
            // 内容刷新）后会重新 dispatch 当前页，此时不播放动画避免页面闪烁
            if (position != lastSelectedPosition) {
                currentUI?.contentView?.apply {
                    animate().cancel()
                    alpha = 0f
                    translationY = resources.displayMetrics.density * 30f
                    animate().alpha(1f).translationY(0f).setDuration(250).start()
                }
            }
            lastSelectedPosition = position
        }
    }

    val mainUI: MainUI get() = getUI(0) as MainUI
    val manageUI: ManageUI get() = getUI(1) as ManageUI
    val downloadUI: DownloadUI get() = getUI(2) as DownloadUI
    val controllerUI: ControllerUI get() = getUI(3) as ControllerUI
    val multiplayerUI: MultiplayerUI get() = getUI(4) as MultiplayerUI
    val settingUI: SettingUI get() = getUI(5) as SettingUI
    val accountUI: AccountUI get() = getUI(6) as AccountUI
    val versionUI: VersionUI get() = getUI(7) as VersionUI

    fun init() {
        instance = this
        pager.adapter = UIAdapter()
        // 禁止鼠标滚轮翻页（触摸滑动已由 isUserInputEnabled 禁用，但滚轮走另一条事件通道）
        pager.disableMouseWheelScroll()
        // 不预加载相邻页面：进入某页时只创建当前页，避免相邻页提前创建带来的
        // inflate/初始化开销（页面在切换时才创建）
        pager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        // 主界面切换动画为上下过渡（垂直方向）
        pager.orientation = ViewPager2.ORIENTATION_VERTICAL
        // 禁用滑动手势：页面内垂直滚动内容与滑动切换冲突，仅通过菜单切换
        pager.isUserInputEnabled = false
        // 不保留页面位置状态：Activity 重建后始终从主页开始，
        // 避免 ViewPager2 恢复上次位置时途经未初始化的页面（如未 setVersion 的 ManageUI）
        pager.isSaveEnabled = false
        pager.registerOnPageChangeCallback(pageChangeCallback)
    }

    fun switchUI(ui: FCLCommonUI) {
        val position = uiRegistry.indexOf(ui)
        if (position < 0) return
        if (ui === currentUI) return
        if (pager.currentItem == position) {
            // 与当前页位置相同（如启动时的初始页）：仅更新当前 UI
            currentUI = ui
        } else {
            // 跨页切换统一瞬时跳转，过渡动画统一由 onPageSelected 的淡入上滑处理。
            // 不用平滑滑动：远距跳转时平滑滚动会途经中间页导致重 UI 被逐个创建/回收
            pager.setCurrentItem(position, false)
        }
    }

    /** 获取指定位置的 UI，不存在则创建并执行 onCreate */
    fun getUI(position: Int): FCLCommonUI {
        return uiRegistry[position] ?: factories[position]().also {
            uiRegistry[position] = it
            it.onCreate()
        }
    }

    /** 页面被 ViewPager 回收时清出注册表（不保留状态），UI 资源随视图树释放 */
    fun destroyUI(position: Int) {
        uiRegistry[position] = null
    }

    fun registerDefaultBackEvent(runnable: Runnable?) {
        FCLBaseUI.setDefaultBackEvent(runnable)
    }

    fun onBackPressed() {
        currentUI?.onBackPressed()
    }

    fun onPause() {
        for (baseUI in uiRegistry) {
            baseUI?.onPause()
        }
    }

    fun onResume() {
        for (baseUI in uiRegistry) {
            baseUI?.onResume()
        }
    }

    inner class UIAdapter : RecyclerView.Adapter<UIAdapter.Holder>() {

        inner class Holder(val container: FrameLayout) : RecyclerView.ViewHolder(container) {
            var boundPosition: Int = 0
        }

        override fun getItemCount(): Int = 8

        override fun getItemViewType(position: Int): Int = position

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            // ViewPager2 要求页面直接子 View 必须 MATCH_PARENT
            val container = FrameLayout(parent.context)
            container.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            return Holder(container)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.boundPosition = position
            holder.container.removeAllViews()
            val contentView = getUI(position).contentView
            // 防御：GapWorker 预取可能将同一 UI 视图挂到其他容器（预取 bind 与正式 bind 竞争），
            // 先解除旧 parent，避免 addView 抛 "child already has a parent"
            (contentView.parent as? ViewGroup)?.removeView(contentView)
            holder.container.addView(
                contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        override fun onViewRecycled(holder: Holder) {
            destroyUI(holder.boundPosition)
        }
    }
}
