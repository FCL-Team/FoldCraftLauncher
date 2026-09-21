package com.mio.ui.popup

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.cache.VersionCache
import com.mio.ui.adapter.ViewHolder
import com.mio.ui.applySelectableItemStyle
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemVersionSwitchBinding
import com.tungsten.fcl.databinding.PopupVersionSwitchBinding
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.util.ConvertUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds

/**
 * 版本快速切换弹窗：长按主界面版本卡片弹出，就地列出版本（按真实游戏版本降序），
 * 点击条目切换选中版本，点击条目右侧启动按钮切换并启动。
 *
 * 弹窗与右菜单对齐：宽度同菜单宽度，顶到屏幕顶端、底悬于版本卡片上方，背景半透明。
 * 条目直接读取与版本列表页共享的 [VersionCache] 快照（[preload] 预热），
 * 打开后后台重刷快照并按最新数据刷新条目。
 */
class VersionSwitchPopup(private val activity: FCLActivity) {

    private lateinit var popup: PopupWindow
    private lateinit var binding: PopupVersionSwitchBinding
    private var adapter: Adapter? = null

    /** 展示弹窗并异步填充条目；[menu] 传右菜单视图，弹窗与其对齐 */
    fun show(anchor: View, menu: View, onLaunch: (String) -> Unit) {
        val profile = Profiles.getSelectedProfile()
        activity.lifecycleScope.launch {
            showPopup(anchor, menu, profile, onLaunch)
            // 快照为空（尚未预热）时显示圆形进度，等待与版本列表页共享的快照就绪
            if (VersionCache.get(profile).isEmpty()) {
                binding.progress.visibility = View.VISIBLE
                binding.list.visibility = View.GONE
                withTimeoutOrNull(60_000.milliseconds) { runCatching { VersionCache.refresh(profile) } }
            }
            val items = items(profile)
            if (items.isEmpty()) {
                if (popup.isShowing && !activity.isDestroyed && !activity.isFinishing) {
                    popup.dismiss()
                    Toast.makeText(activity, R.string.version_no_version, Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            binding.progress.visibility = View.GONE
            binding.list.visibility = View.VISIBLE
            applyItems(items)
            // 后台重刷共享快照保证数据新鲜，完成后按最新数据刷新条目
            withTimeoutOrNull(60_000.milliseconds) { runCatching { VersionCache.refresh(profile) } }
            applyItems(items(profile))
        }
    }

    private fun applyItems(items: List<Item>) {
        if (!popup.isShowing) return
        adapter?.submit(items)
        val selectedIndex = items.indexOfFirst { it.selected }
        if (selectedIndex > 0) binding.list.scrollToPosition(selectedIndex)
    }

    /** 从共享快照（VersionCache）派生条目：按真实游戏版本降序 */
    private fun items(profile: Profile): List<Item> {
        val selected = profile.selectedVersion
        return VersionCache.get(profile).values
            .sortedWith(compareByDescending<VersionCache.Entry> { it.gameVersion }.thenByDescending { it.id })
            .map { Item(it.id, it.newIcon(), it.gameVersion, it.id == selected) }
    }

    private fun showPopup(anchor: View, menu: View, profile: Profile, onLaunch: (String) -> Unit) {
        val metrics = activity.resources.displayMetrics
        binding = PopupVersionSwitchBinding.inflate(activity.layoutInflater)
        adapter = Adapter { item, launch ->
            popup.dismiss()
            profile.selectedVersion = item.id
            if (launch) onLaunch(item.id)
        }.also {
            binding.list.adapter = it
            binding.list.layoutManager = LinearLayoutManager(activity)
        }

        // 外层 padding 只留左侧与底部给卡片阴影，顶部与右侧贴边；
        // 窗口宽 = 右菜单宽 + 左侧留白，x 以右菜单左缘为基准
        // （菜单贴屏幕物理右缘，用其坐标避免 widthPixels 与物理屏的差异）
        val pad = ConvertUtils.dip2px(activity, 12f)
        val width = if (menu.width > 0) menu.width + pad
        else minOf(
            ConvertUtils.dip2px(activity, 280f),
            metrics.widthPixels - ConvertUtils.dip2px(activity, 24f)
        )
        val menuLocation = IntArray(2)
        menu.getLocationOnScreen(menuLocation)
        val x = if (menu.width > 0) menuLocation[0] - pad
        else (metrics.widthPixels - width).coerceAtLeast(ConvertUtils.dip2px(activity, 8f))
        val anchorLocation = IntArray(2)
        anchor.getLocationOnScreen(anchorLocation)
        // 高度顶到屏幕顶端，底部卡片悬在版本卡片上方 4dp（窗口尺寸固定，数据填充无需重排）
        val y = 0
        val height = anchorLocation[1] - ConvertUtils.dip2px(activity, 4f) + pad

        popup = PopupWindow(binding.root, width, height, true).apply {
            isOutsideTouchable = true
            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        }
        popup.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y)
    }

    /** 条目数据（不含组件摘要，保持弹窗轻量） */
    private data class Item(
        val id: String,
        val icon: Drawable,
        val gameVersion: GameVersionNumber,
        val selected: Boolean,
    )

    private class Adapter(
        private val onClick: (Item, Boolean) -> Unit
    ) : RecyclerView.Adapter<ViewHolder>() {

        private val items = mutableListOf<Item>()

        /** 全量替换条目并刷新（快照填充/后台重刷完成时调用） */
        @SuppressLint("NotifyDataSetChanged")
        fun submit(items: List<Item>) {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ItemVersionSwitchBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ).root
            )
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val binding = ItemVersionSwitchBinding.bind(holder.itemView)
            val item = items[position]
            val context = holder.itemView.context
            binding.title.text = item.id
            binding.icon.setBackgroundDrawable(item.icon)
            // 选中版本主题色高亮，其余普通卡片（与选择类对话框条目一致）
            applySelectableItemStyle(
                context,
                holder.itemView,
                null,
                item.selected,
                context.resources.displayMetrics.density
            )
            holder.itemView.setOnClickListener { onClick(item, false) }
            binding.launch.setOnClickListener { onClick(item, true) }
        }
    }

    companion object {

        private var preloading = false

        /**
         * 后台预热版本快照（等待版本仓库加载完成，上限 30s），
         * 与版本列表页共享同一份 VersionCache，长按弹出时即可完整显示
         */
        fun preload(activity: FCLActivity) {
            if (preloading) return
            preloading = true
            activity.lifecycleScope.launch {
                try {
                    val repository = Profiles.getSelectedProfile().repository
                    var waited = 0
                    while (!repository.isLoaded && waited < 30_000) {
                        delay(500.milliseconds)
                        waited += 500
                    }
                    withTimeoutOrNull(60_000.milliseconds) { runCatching { VersionCache.refresh(Profiles.getSelectedProfile()) } }
                } finally {
                    preloading = false
                }
            }
        }
    }
}
