package com.tungsten.fcl.ui.download.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mio.ui.adapter.ViewHolder
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
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.Dispatchers
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
                val remoteVersionOptional = downloadPage.getRepository()
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

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val binding = ItemRemoteModBinding.bind(holder.itemView)
        val remoteMod = list[position]
        binding.parent.setOnClickListener {
            callback.onItemSelect(
                remoteMod
            )
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
            .map { downloadPage.getLocalizedCategory(it) }
            .collect(
                Collectors.toList()
            ).joinToString("   ")
        val tag = StringUtils.removeSuffix(categories, "   ")
        binding.tag.text = tag
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
        // 已安装标记刷新：只更新标题，不重播入场动画、不重载图片
        val binding = ItemRemoteModBinding.bind(holder.itemView)
        binding.title.text = buildTitle(list[position])
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

    override fun getItemCount(): Int {
        return list.size
    }
}
