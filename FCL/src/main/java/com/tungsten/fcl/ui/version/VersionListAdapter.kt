package com.tungsten.fcl.ui.version

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.datastore.VersionPinManager
import com.mio.ui.adapter.ViewHolder
import com.mio.util.AnimUtil.Companion.playTranslationX
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.ItemVersionBinding
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import java.util.Collections

class VersionListAdapter(val context: Context, initList: List<VersionListItem>) :
    RecyclerView.Adapter<ViewHolder>() {
    private val versionList = mutableListOf<VersionListItem>()

    /** 当前选中版本：updateSelection 以此定位新旧选中项做局部刷新 */
    private var lastSelected: String? = initList.firstOrNull { it.selectedProperty().get() }?.version

    /** 置顶项长按拖拽排序的触控器，页面创建列表后 attach 到 RecyclerView */
    val itemTouchHelper = ItemTouchHelper(PinDragCallback())

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        recyclerView = rv
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        recyclerView = null
    }

    init {
        versionList.addAll(initList)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemVersionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val versionListItem = versionList[position]
        val binding = ItemVersionBinding.bind(holder.itemView)
        // 勾选态不与条目对象做属性绑定（diff 局部刷新后未重绑的条目会持有旧对象，
        // 属性驱动收不到更新），由 updateSelection 以 payload 局部刷新
        binding.radioButton.setCheckValue(versionListItem.selectedProperty().get())
        binding.icon.background = versionListItem.drawable
        binding.title.text = versionListItem.version
        binding.tag.visibility = if (versionListItem.tag == null) View.GONE else View.VISIBLE
        if (versionListItem.tag != null) {
            binding.tag.text = versionListItem.tag
        }
        binding.subtitle.text = versionListItem.libraries
        binding.radioButton.setOnClickListener {
            versionListItem.profile.selectedVersion = versionListItem.version
        }
        binding.delete.setOnClickListener {
            Versions.deleteVersion(
                context,
                versionListItem.profile,
                versionListItem.version
            )
        }
        binding.root.setOnClickListener {
            versionListItem.profile.selectedVersion = versionListItem.version
        }
        bindPin(binding, versionListItem.isPinned)
        binding.pin.setOnClickListener { togglePin(versionListItem) }
        binding.root.setOnLongClickListener {
            if (versionListItem.isPinned) {
                itemTouchHelper.startDrag(holder)
                true
            } else {
                false
            }
        }
        if (!versionListItem.profile.getVersionSetting(versionListItem.version).isUsesGlobal) {
            binding.setting.visibility = View.VISIBLE
            binding.setting.setOnClickListener {
                versionListItem.profile.selectedVersion = versionListItem.version
                val uiManager = MainActivity.getInstance().uiManager
                MainActivity.getInstance().binding.manage.isSelected = true
                val tab = uiManager.manageUI.tabLayout.getTabAt(0)
                uiManager.manageUI.tabLayout.selectTab(tab)
            }
        } else {
            binding.setting.visibility = View.GONE
        }
        binding.subtitle.text = String.format(
            "%s  Mods:%d",
            versionListItem.libraries,
            versionListItem.modCount
        )
        playTranslationX(
            binding.root,
            ThemeEngine.getInstance().getTheme().animationSpeed * 30L,
            -100f,
            0f
        ).start()
    }

    /** 星标/勾选状态局部重绑：仅切换对应控件，不重建整行绑定也不播放入场动画 */
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        var partial = false
        val binding = ItemVersionBinding.bind(holder.itemView)
        val item = versionList[position]
        if (payloads.any { it === PAYLOAD_PIN }) {
            partial = true
            bindPin(binding, item.isPinned)
        }
        if (payloads.any { it === PAYLOAD_SELECTION }) {
            partial = true
            binding.radioButton.setCheckValue(item.version == lastSelected)
        }
        if (!partial) {
            onBindViewHolder(holder, position)
        }
    }

    private fun bindPin(binding: ItemVersionBinding, pinned: Boolean) {
        binding.pin.setImageResource(if (pinned) R.drawable.ic_star_filled else R.drawable.ic_star_outline)
        binding.pin.contentDescription = context.getString(
            if (pinned) R.string.version_unpin else R.string.version_pin
        )
    }

    override fun getItemCount(): Int {
        return versionList.size
    }

    /**
     * DiffUtil 局部刷新：只对新增/删除/移动/内容变化的条目分发 notify，
     * 收藏等星标变化以 payload 局部重绑（无动画）；其余条目不重绑、动画不重播。
     */
    fun updateVersionList(newList: List<VersionListItem>) {
        // 同序同内容直接跳过：保留条目对象身份，radioButton 的属性绑定不中断
        if (newList.size == versionList.size &&
            newList.indices.all { sameItem(versionList[it], newList[it]) }
        ) return
        val manager = recyclerView?.layoutManager as? LinearLayoutManager
        // diff 前记录第一可见项（p0）及其视口顶部偏移与后一条（p1）：
        // 首个可见项被 move/移除时 LayoutManager 的锚点会跟随它把视口拖走，
        // 分发后若 p0 未动则幂等恢复；若动了则钉住 p1 顶替 p0 的原视口位置，
        // 使收藏/取消收藏时视口纹丝不动
        var p0Index = -1
        var p0Top = 0
        var p0Id: String? = null
        var p1Id: String? = null
        if (manager != null && versionList.isNotEmpty()) {
            val first = manager.findFirstVisibleItemPosition()
            if (first in versionList.indices) {
                p0Index = first
                p0Id = versionList[first].version
                p0Top = manager.findViewByPosition(first)?.top ?: 0
                if (first + 1 < versionList.size) p1Id = versionList[first + 1].version
            }
        }
        // 传快照而非引用：getChangePayload 在 dispatchUpdatesTo 阶段才回调，
        // 届时 versionList 已被替换为新列表，共用引用会越界且比较错位
        val diff = DiffUtil.calculateDiff(VersionDiffCallback(versionList.toList(), newList), true)
        versionList.clear()
        versionList.addAll(newList)
        diff.dispatchUpdatesTo(this)
        if (manager == null || p0Id == null) return
        val newPosP0 = newList.indexOfFirst { it.version == p0Id }
        if (newPosP0 == p0Index) {
            manager.scrollToPositionWithOffset(newPosP0, p0Top)
            return
        }
        val p1 = p1Id ?: return
        val newPosP1 = newList.indexOfFirst { it.version == p1 }
        if (newPosP1 >= 0) {
            manager.scrollToPositionWithOffset(newPosP1, p0Top)
        }
    }

    /**
     * 选中版本变化时以 payload 局部刷新新旧选中项的勾选圈，
     * 并同步条目属性（供页面滚动定位查询）
     */
    fun updateSelection(selected: String?) {
        val old = lastSelected
        if (old == selected) return
        lastSelected = selected
        versionList.forEachIndexed { index, item ->
            if (item.version == old || item.version == selected) {
                item.selectedProperty().set(item.version == selected)
                notifyItemChanged(index, PAYLOAD_SELECTION)
            }
        }
    }

    private fun togglePin(item: VersionListItem) {
        val current = VersionPinManager.getPinned(item.profile.name)
        VersionPinManager.setPinned(
            item.profile.name,
            if (item.version in current) current - item.version else current + item.version
        )
    }

    /** 显示列表前缀中的置顶项数量（置顶项连续居前，前缀判定即区间长度） */
    private fun pinnedCount(): Int {
        val first = versionList.firstOrNull() ?: return 0
        val pinnedSet = VersionPinManager.getPinned(first.profile.name).toHashSet()
        return versionList.takeWhile { it.version in pinnedSet }.size
    }

    /**
     * 拖拽结束提交新置顶顺序：取显示列表前缀中的置顶项出现顺序，
     * 保留原有相对位置合并回完整置顶列表（搜索/分类过滤下不可见的置顶项不受影响）
     */
    private fun commitPinOrder() {
        val first = versionList.firstOrNull() ?: return
        val old = VersionPinManager.getPinned(first.profile.name)
        if (old.isEmpty()) return
        val oldSet = old.toHashSet()
        val queue = ArrayDeque(versionList.takeWhile { it.version in oldSet }.map { it.version })
        val newOrder = old.map { if (it in oldSet) queue.removeFirst() else it }
        if (newOrder != old) {
            VersionPinManager.setPinned(first.profile.name, newOrder)
        }
    }

    /** 置顶项长按拖拽排序：仅允许在列表前缀的置顶区间内移动，松手提交顺序 */
    private inner class PinDragCallback : ItemTouchHelper.Callback() {
        override fun isLongPressDragEnabled() = false

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val position = viewHolder.bindingAdapterPosition
            if (position < 0 || position >= pinnedCount()) return 0
            return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val from = viewHolder.bindingAdapterPosition
            val to = target.bindingAdapterPosition
            val count = pinnedCount()
            if (from < 0 || to < 0 || from >= count || to >= count) return false
            if (from < to) {
                for (i in from until to) Collections.swap(versionList, i, i + 1)
            } else {
                for (i in from downTo to + 1) Collections.swap(versionList, i, i - 1)
            }
            notifyItemMoved(from, to)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                viewHolder?.itemView?.alpha = 0.7f
            }
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            viewHolder.itemView.alpha = 1f
            commitPinOrder()
        }
    }

    class VersionDiffCallback(
        private val oldList: List<VersionListItem>,
        private val newList: List<VersionListItem>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
            oldList[oldPos].version == newList[newPos].version

        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
            sameItem(oldList[oldPos], newList[newPos])

        /** 仅星标状态变化时给出 payload 局部重绑；其余内容变化走整行重绑 */
        override fun getChangePayload(oldPos: Int, newPos: Int): Any? {
            val oldItem = oldList[oldPos]
            val newItem = newList[newPos]
            return if (oldItem.isPinned != newItem.isPinned
                && oldItem.libraries == newItem.libraries
                && oldItem.tag == newItem.tag
                && oldItem.modCount == newItem.modCount
            ) {
                PAYLOAD_PIN
            } else {
                null
            }
        }
    }

    companion object {
        private val PAYLOAD_PIN = Any()
        private val PAYLOAD_SELECTION = Any()

        private fun sameItem(a: VersionListItem, b: VersionListItem): Boolean =
            a.version == b.version && a.libraries == b.libraries && a.tag == b.tag
                    && a.modCount == b.modCount && a.isPinned == b.isPinned
    }
}
