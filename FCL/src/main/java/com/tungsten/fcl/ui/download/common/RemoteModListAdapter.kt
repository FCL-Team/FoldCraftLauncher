package com.tungsten.fcl.ui.download.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mio.data.FavoriteManager
import com.mio.ui.adapter.ViewHolder
import com.mio.ui.widget.SwipeMenuLayout
import com.mio.util.AnimUtil.Companion.playTranslationX
import com.mio.util.format
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.ItemRemoteModBinding
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.download.DownloadUI
import com.tungsten.fcl.util.ModTranslations
import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.mod.curse.CurseAddon
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.logging.Level
import java.util.stream.Collectors

class RemoteModListAdapter(
    private val context: Context,
    private val downloadPage: DownloadPage,
    private val list: ArrayList<RemoteMod>,
    private val callback: Callback
) : RecyclerView.Adapter<ViewHolder>() {
    private val modIdList: MutableList<String?> = ArrayList()
    private val scanMutex = Mutex()

    /** 当前已左滑打开菜单的 item（互斥：打开新的前先关旧的） */
    private var openMenuLayout: SwipeMenuLayout? = null

    /** 收藏状态收集任务（adapter 挂到 RecyclerView 时启动，分离时取消） */
    private var favoriteJob: Job? = null

    /** 上次收集到的收藏 id 集合，用于差量计算受影响的条目位置 */
    private var boundFavoriteIds: Set<String> = emptySet()

    init {
        MainActivity.getInstance().lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                // 后台预热 Mod 翻译数据，避免首次 bind 时在主线程解析大文件造成卡顿
                ModTranslations.getTranslationsByRepositoryType(downloadPage.repository.getType())
                    .preload()
            }
            refreshInstalledState()
        }
    }

    interface Callback {
        fun onItemSelect(mod: RemoteMod?)
    }

    /**
     * 后台扫描本地已安装模组并反查远程 modid，结果变化时刷新列表的"已安装"标记。
     * 并发触发时按顺序串行扫描，避免旧的扫描结果覆盖新的。
     */
    fun refreshInstalledState() {
        if (downloadPage.pageId != DownloadUI.PAGE_ID_DOWNLOAD_MOD) return
        MainActivity.getInstance().lifecycleScope.launch {
            scanMutex.withLock {
                val installedIds = withContext(Dispatchers.Default) { loadInstalledModIds() }
                if (installedIds != modIdList) {
                    modIdList.clear()
                    modIdList.addAll(installedIds)
                    notifyItemRangeChanged(0, itemCount, PAYLOAD_INSTALLED)
                }
            }
        }
    }

    private fun loadInstalledModIds(): List<String?> {
        // 动态取当前选中的目录/版本（页面存活期间可能被切换）
        val modManager = Profiles.getSelectedProfile().repository
            .getModManager(Profiles.getSelectedVersion())
        val modFiles = runCatching {
            modManager.getMods().parallelStream().collect(Collectors.toList())
        }.getOrNull() ?: emptyList<LocalModFile>()
        val ids = mutableListOf<String?>()
        for (localModFile in modFiles) {
            try {
                val remoteVersionOptional = downloadPage
                    .getRemoteVersionByLocalFile(localModFile, localModFile.file)
                remoteVersionOptional.ifPresent {
                    localModFile.remoteVersion = it
                }
                localModFile.remoteVersion?.let {
                    ids.add(it.modid())
                }
            } catch (e: Throwable) {
                Logging.LOG.log(Level.SEVERE, e.toString())
            }
        }
        return ids
    }

    companion object {
        /** payload：仅刷新"已安装"标记，重绑时跳过图片加载与入场动画 */
        const val PAYLOAD_INSTALLED = 1

        /** payload：仅刷新收藏星形图标，重绑时跳过图片加载与入场动画 */
        const val PAYLOAD_FAVORITE = 2

        /** 缓存占位位图（内容只读，多视图共享安全），避免每次 bind 重新分配与绘制 */
        private var placeholderBitmap: Bitmap? = null
    }

    /** 固定 90×90 内在尺寸的占位图（与 override 后图片尺寸一致，避免加载完成时
     *  drawable 内在尺寸变化触发 requestLayout 导致列表重排） */
    private fun fixedIconPlaceholder(): Drawable {
        var bitmap = placeholderBitmap
        if (bitmap == null) {
            bitmap = createBitmap(90, 90)
            val base = ContextCompat.getDrawable(context, R.drawable.ic_cube)!!.mutate()
            base.setBounds(0, 0, 90, 90)
            base.draw(Canvas(bitmap))
            placeholderBitmap = bitmap
        }
        return bitmap.toDrawable(context.resources)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemRemoteModBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            ).root
        )
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        // 收藏状态变化时仅局部刷新受影响条目（notifyDataSetChanged 会整表重绑，
        // 重播全部条目的入场动画并重载图片，点击收藏时列表明显闪烁）
        favoriteJob = MainActivity.getInstance().lifecycleScope.launch {
            FavoriteManager.favorites.collect { favorites ->
                val newIds = favorites.map { it.id }.toSet()
                val oldIds = boundFavoriteIds
                boundFavoriteIds = newIds
                val toggled = (newIds - oldIds) + (oldIds - newIds)
                if (toggled.isEmpty()) return@collect
                for (position in list.indices) {
                    val mod = list[position]
                    val id = FavoriteManager.idOf(FavoriteManager.sourceOf(mod), mod.modID)
                    if (id in toggled) {
                        notifyItemChanged(position, PAYLOAD_FAVORITE)
                    }
                }
            }
        }
        // 列表滚动时收起已打开的左滑菜单
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    openMenuLayout?.closeMenu()
                }
            }
        })
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        favoriteJob?.cancel()
        favoriteJob = null
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val binding = ItemRemoteModBinding.bind(holder.itemView)
        val remoteMod = list[position]
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
            callback.onItemSelect(
                remoteMod
            )
        }
        // 左滑菜单收藏按钮：按当前收藏状态显示实心/描边星形
        val favoriteEntity = FavoriteManager.fromRemoteMod(remoteMod, favoriteType())
        binding.btnFavorite.setImageResource(
            if (FavoriteManager.isFavorited(favoriteEntity.id)) R.drawable.ic_star_filled else R.drawable.ic_star_outline
        )
        binding.btnFavorite.setOnClickListener {
            MainActivity.getInstance().lifecycleScope.launch {
                val added = FavoriteManager.toggle(favoriteEntity)
                Toast.makeText(
                    context,
                    context.getString(if (added) R.string.favorite_added else R.string.favorite_removed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // 固定 90×90 占位（与 override 后图片内在尺寸一致）：图片加载完成替换时
        // drawable 内在尺寸不变，不触发 requestLayout，避免列表全局重排导致
        // 其他 item 的 marquee 文本被重置
        binding.icon.setImageDrawable(fixedIconPlaceholder())
        Glide.with(binding.icon)
            .load(remoteMod.iconUrl)
            .placeholder(fixedIconPlaceholder())
            .override(90, 90)
            .error(fixedIconPlaceholder())
            .into(binding.icon)
        binding.title.text = buildTitle(remoteMod)
        val categories = remoteMod.categories.stream()
            .map { downloadPage.getLocalizedCategory(remoteMod, it) }
            .collect(
                Collectors.toList()
            ).joinToString("   ")
        binding.tag.text = StringUtils.removeSuffix(categories, "   ")
        // 聚合模式在标题行末尾显示来源徽标（平台 LOGO + 平台名的主题次色胶囊），单源模式隐藏
        val sourceLabel = downloadPage.getSourceLabel(remoteMod)
        binding.sourceBadge.visibility = if (sourceLabel.isEmpty()) View.GONE else View.VISIBLE
        if (sourceLabel.isNotEmpty()) {
            binding.sourceBadge.text = sourceLabel
            // 注册换肤回调：主题（含次要色）修改后已加载的条目同步变色；
            // 同一 view 重复注册会覆盖旧回调，复用绑定不同条目时以最后一次为准
            ThemeEngine.getInstance()
                .registerEvent(binding.sourceBadge) { applySourceBadgeStyle(binding.sourceBadge, remoteMod) }
        }
        binding.description.text = remoteMod.description
        binding.downloadCount.text = remoteMod.downloadCount.format(context)
        playTranslationX(
            binding.root,
            ThemeEngine.getInstance().getTheme().animationSpeed * 30L,
            -100f,
            0f
        ).start()
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }
        // 局部刷新：已安装标记只更新标题，收藏状态只更新星形图标，
        // 均不重播入场动画、不重载图片
        val binding = ItemRemoteModBinding.bind(holder.itemView)
        val remoteMod = list.getOrNull(position) ?: return
        if (payloads.contains(PAYLOAD_INSTALLED)) {
            binding.title.text = buildTitle(remoteMod)
        }
        if (payloads.contains(PAYLOAD_FAVORITE)) {
            val id = FavoriteManager.idOf(FavoriteManager.sourceOf(remoteMod), remoteMod.modID)
            binding.btnFavorite.setImageResource(
                if (FavoriteManager.isFavorited(id)) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            )
        }
    }

    private fun buildTitle(remoteMod: RemoteMod): String {
        val mod =
            ModTranslations.getTranslationsByRepositoryType(downloadPage.repository.getType())
                .getModByCurseForgeId(remoteMod.slug)
        val title =
            if (mod != null && LocaleUtils.isChinese(context)) mod.getDisplayName() else remoteMod.title
        return if (downloadPage.pageId == DownloadUI.PAGE_ID_DOWNLOAD_MOD && modIdList.contains(remoteMod.modID)) {
            "[${context.getString(R.string.installed)}] $title"
        } else {
            title
        }
    }

    /** 当前页模式对应的资源类别（收藏实体用；注意 RESOURCE_PACK/SHADER_PACK 不能用 repository.getType()，其被固定为 MOD） */
    private fun favoriteType(): RemoteModRepository.Type = when (downloadPage.pageId) {
        DownloadUI.PAGE_ID_DOWNLOAD_MODPACK -> RemoteModRepository.Type.MODPACK
        DownloadUI.PAGE_ID_DOWNLOAD_RESOURCE_PACK -> RemoteModRepository.Type.RESOURCE_PACK
        DownloadUI.PAGE_ID_DOWNLOAD_SHADER_PACK -> RemoteModRepository.Type.SHADER_PACK
        DownloadUI.PAGE_ID_DOWNLOAD_WORLD -> RemoteModRepository.Type.WORLD
        else -> RemoteModRepository.Type.MOD
    }

    /** 来源徽标配色：主题次色实底 + 亮度对比色文字与平台 LOGO（换肤回调与首次 bind 共用） */
    private fun applySourceBadgeStyle(badge: FCLTextView, remoteMod: RemoteMod) {
        com.mio.util.applySourceBadgeStyle(badge, remoteMod.data is CurseAddon)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
