package com.tungsten.fcl.ui.download.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mio.data.FavoriteManager
import com.mio.data.favorite.DownloadFavoriteEntity
import com.mio.ui.adapter.ViewHolder
import com.mio.ui.widget.SwipeMenuLayout
import com.mio.ui.widget.closeSwipeMenuOnOutsideTouch
import com.mio.util.applySourceBadgeStyle
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.ItemFavoriteBinding
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/** 收藏列表适配器：左滑修改分组/取消收藏，点击条目进入对应资源详情页 */
class FavoriteAdapter(
    private val context: Context,
    private val page: FavoritePage,
) : RecyclerView.Adapter<ViewHolder>() {

    private val list = ArrayList<DownloadFavoriteEntity>()

    /** 当前已左滑打开菜单的 item（互斥：打开新的前先关旧的） */
    private var openMenuLayout: SwipeMenuLayout? = null

    /** 分组定义收集任务（挂到 RecyclerView 时启动，分离时取消） */
    private var groupJob: Job? = null

    /** 多选模式：长按条目进入，点击条目切换选中；由收藏页操作栏执行批量操作 */
    private val selectedIds = mutableSetOf<String>()
    var isMultiSelectMode = false
        private set

    /** 多选状态回调：(是否处于多选模式, 已选数量)，页面据此显隐操作栏与更新计数 */
    var onMultiSelectStateChanged: ((Boolean, Int) -> Unit)? = null

    /** 当前选中的收藏条目 id */
    fun getSelectedIds(): Set<String> = selectedIds.toSet()

    /** 进入多选模式并选中首个条目 */
    fun enterMultiSelect(firstId: String) {
        if (isMultiSelectMode) return
        isMultiSelectMode = true
        openMenuLayout?.closeMenu()
        openMenuLayout = null
        selectedIds.add(firstId)
        notifyDataSetChanged()
        notifyState()
    }

    /** 退出多选模式并清空选中 */
    fun exitMultiSelect() {
        if (!isMultiSelectMode) return
        isMultiSelectMode = false
        selectedIds.clear()
        notifyDataSetChanged()
        notifyState()
    }

    /** 全选当前列表条目 */
    fun selectAll() {
        selectedIds.clear()
        selectedIds.addAll(list.map { it.id })
        notifyDataSetChanged()
        notifyState()
    }

    /** 反选：已选的取消、未选的选中 */
    fun invertSelect() {
        for (item in list) {
            if (item.id in selectedIds) selectedIds.remove(item.id) else selectedIds.add(item.id)
        }
        notifyDataSetChanged()
        notifyState()
    }

    private fun toggleSelect(id: String) {
        if (id in selectedIds) selectedIds.remove(id) else selectedIds.add(id)
        notifyDataSetChanged()
        notifyState()
    }

    private fun notifyState() {
        onMultiSelectStateChanged?.invoke(isMultiSelectMode, selectedIds.size)
    }

    /** 数据更新：DiffUtil 差量刷新，增删带动画，未变化条目不重绑（避免图标重载闪烁） */
    fun submit(items: List<DownloadFavoriteEntity>) {
        // 筛选切换可能移除"菜单打开中"的条目，其 ViewHolder 会经 Recycler 缓存原样复用
        // （复用不重绑），把打开状态带回列表；数据刷新时统一收起
        openMenuLayout?.closeMenu()
        openMenuLayout = null
        // 数据刷新后清理已不在列表中的选中项（多选模式保持）
        selectedIds.retainAll(items.map { it.id }.toSet())
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = list.size
            override fun getNewListSize(): Int = items.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                list[oldItemPosition].id == items[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                list[oldItemPosition] == items[newItemPosition]
        })
        list.clear()
        list.addAll(items)
        result.dispatchUpdatesTo(this)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        // 列表滚动时收起已打开的左滑菜单；点击菜单外区域同样收起
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    openMenuLayout?.closeMenu()
                }
            }
        })
        recyclerView.closeSwipeMenuOnOutsideTouch { openMenuLayout }
        // 分组定义变化（重命名等）时条目上的分组名需要刷新：低频操作，直接全量重绑
        groupJob = MainActivity.getInstance().lifecycleScope.launch {
            FavoriteManager.groups.collect { notifyDataSetChanged() }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        groupJob?.cancel()
        groupJob = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFavoriteBinding.inflate(LayoutInflater.from(context), parent, false).root
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = ItemFavoriteBinding.bind(holder.itemView)
        val favorite = list[position]
        binding.root.onMenuStateChangeListener = object : SwipeMenuLayout.OnMenuStateChangeListener {
            override fun onMenuOpened() {
                openMenuLayout?.let { if (it !== binding.root) it.closeMenu() }
                openMenuLayout = binding.root
            }

            override fun onMenuClosed() {
                if (openMenuLayout === binding.root) {
                    openMenuLayout = null
                }
            }
        }
        if (isMultiSelectMode) {
            // 多选模式：勾选框展示选中态，点击/长按切换选中，左滑菜单与详情跳转停用
            binding.selectCheck.visibility = View.VISIBLE
            binding.selectCheck.isChecked = favorite.id in selectedIds
            binding.parent.setOnClickListener { toggleSelect(favorite.id) }
            binding.parent.setOnLongClickListener { toggleSelect(favorite.id); true }
            binding.btnRemoveFavorite.setOnClickListener(null)
            binding.btnEditGroup.setOnClickListener(null)
        } else {
            binding.selectCheck.visibility = View.GONE
            binding.parent.setOnClickListener {
                page.openFavorite(favorite)
            }
            // 长按进入多选模式并选中该条目
            binding.parent.setOnLongClickListener { enterMultiSelect(favorite.id); true }
            binding.btnRemoveFavorite.setOnClickListener {
                // 触发动作后立即收起菜单
                binding.root.closeMenu()
                MainActivity.getInstance().lifecycleScope.launch {
                    FavoriteManager.toggle(favorite)
                    Toast.makeText(context, context.getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show()
                }
            }
            // 修改所属分组：弹出多选对话框（预勾选当前分组），确认后写库并经 Flow 回推差量刷新
            binding.btnEditGroup.setOnClickListener {
                binding.root.closeMenu()
                GroupSelectionDialog(
                    context,
                    context.getString(R.string.favorite_group_edit),
                    favorite.groups.toSet()
                ) { groupIds ->
                    MainActivity.getInstance().lifecycleScope.launch {
                        FavoriteManager.setGroups(favorite.id, groupIds)
                    }
                }.show()
            }
        }
        Glide.with(binding.icon)
            .load(favorite.iconUrl)
            .placeholder(R.drawable.ic_cube)
            .into(binding.icon)
        binding.title.text = favorite.title
        binding.typeTag.text = typeLabel(favorite.type)
        // 类别为次级信息，用提示色弱化显示（随主题联动）
        binding.typeTag.setTextColor(ThemeEngine.getInstance().getTheme().autoHintTint)
        ThemeEngine.getInstance().registerEvent(binding.typeTag) {
            binding.typeTag.setTextColor(ThemeEngine.getInstance().getTheme().autoHintTint)
        }
        // 来源徽标（平台 LOGO + 平台名的主题次色胶囊）
        val curseforge = favorite.source == FavoriteManager.SOURCE_CURSEFORGE
        binding.sourceBadge.text = context.getString(if (curseforge) R.string.mods_curseforge else R.string.mods_modrinth)
        ThemeEngine.getInstance().registerEvent(binding.sourceBadge) { applySourceBadgeStyle(binding.sourceBadge, curseforge) }
        binding.description.text = favorite.description
        binding.downloadCount.text = favorite.downloadCount.toString()
        // 所属分组标签：与类别一致用提示色弱化显示（随主题联动）
        val groupNames = favorite.groups.mapNotNull { id ->
            FavoriteManager.groups.value.firstOrNull { it.groupId == id }?.name
        }
        binding.groupTag.visibility = if (groupNames.isEmpty()) View.GONE else View.VISIBLE
        binding.groupTag.text = groupNames.joinToString(" · ")
        binding.groupTag.setTextColor(ThemeEngine.getInstance().getTheme().autoHintTint)
        ThemeEngine.getInstance().registerEvent(binding.groupTag) {
            binding.groupTag.setTextColor(ThemeEngine.getInstance().getTheme().autoHintTint)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun typeLabel(type: String): String = try {
        when (RemoteModRepository.Type.valueOf(type)) {
            RemoteModRepository.Type.MODPACK -> context.getString(R.string.modpack)
            RemoteModRepository.Type.RESOURCE_PACK -> context.getString(R.string.resourcepack)
            RemoteModRepository.Type.SHADER_PACK -> context.getString(R.string.shaderpack)
            RemoteModRepository.Type.WORLD -> context.getString(R.string.world)
            else -> context.getString(R.string.mods)
        }
    } catch (e: IllegalArgumentException) {
        context.getString(R.string.mods)
    }
}
