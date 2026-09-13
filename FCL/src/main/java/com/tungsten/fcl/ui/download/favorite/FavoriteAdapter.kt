package com.tungsten.fcl.ui.download.favorite

import android.content.Context
import android.view.LayoutInflater
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
import com.mio.util.applySourceBadgeStyle
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.ItemFavoriteBinding
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import kotlinx.coroutines.launch

/** 收藏列表适配器：左滑取消收藏，点击条目进入对应资源详情页 */
class FavoriteAdapter(
    private val context: Context,
    private val page: FavoritePage,
) : RecyclerView.Adapter<ViewHolder>() {

    private val list = ArrayList<DownloadFavoriteEntity>()

    /** 当前已左滑打开菜单的 item（互斥：打开新的前先关旧的） */
    private var openMenuLayout: SwipeMenuLayout? = null

    /** 数据更新：DiffUtil 差量刷新，增删带动画，未变化条目不重绑（避免图标重载闪烁） */
    fun submit(items: List<DownloadFavoriteEntity>) {
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
        // 列表滚动时收起已打开的左滑菜单
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    openMenuLayout?.closeMenu()
                }
            }
        })
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
        binding.parent.setOnClickListener {
            page.openFavorite(favorite)
        }
        binding.btnRemoveFavorite.setOnClickListener {
            MainActivity.getInstance().lifecycleScope.launch {
                FavoriteManager.toggle(favorite)
                Toast.makeText(context, context.getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show()
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
