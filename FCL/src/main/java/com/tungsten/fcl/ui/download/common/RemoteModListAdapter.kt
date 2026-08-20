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
import java.util.logging.Level
import java.util.stream.Collectors

class RemoteModListAdapter(
    private val context: Context,
    private val downloadPage: DownloadPage,
    private val list: ArrayList<RemoteMod>,
    private val callback: Callback
) : RecyclerView.Adapter<ViewHolder>() {
    private val modIdList: MutableList<String?> = ArrayList()

    init {
        MainActivity.getInstance().lifecycleScope.launch(Dispatchers.Default) {
            // 后台预热 Mod 翻译数据，避免首次 bind 时在主线程解析大文件造成卡顿
            ModTranslations.getTranslationsByRepositoryType(downloadPage.repository.getType())
                .preload()
            if (downloadPage.pageId == DownloadUI.PAGE_ID_DOWNLOAD_MOD) {
                val modManager = downloadPage.modManager
                val modFiles = runCatching {
                    modManager.getMods().parallelStream().collect(Collectors.toList())
                }.getOrNull() ?: emptyList<LocalModFile>()
                for (localModFile in modFiles) {
                    try {
                        val size = localModFile.file.toFile().length()
                        if (size > 104857600) continue
                        val remoteVersionOptional = downloadPage.getRepository()
                            .getRemoteVersionByLocalFile(localModFile, localModFile.file)
                        remoteVersionOptional.ifPresent {
                            localModFile.remoteVersion = it
                        }
                        localModFile.remoteVersion?.let {
                            modIdList.add(it.modid)
                        }
                    } catch (e: Throwable) {
                        System.gc()
                        Logging.LOG.log(Level.SEVERE, e.toString())
                    }
                }
            }
        }
    }

    interface Callback {
        fun onItemSelect(mod: RemoteMod?)
    }

    companion object {
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
        val mod =
            ModTranslations.getTranslationsByRepositoryType(downloadPage.repository.getType())
                .getModByCurseForgeId(remoteMod.slug)
        binding.title.text =
            if (mod != null && LocaleUtils.isChinese(context)) mod.getDisplayName() else remoteMod.title
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
        if (downloadPage.pageId == DownloadUI.PAGE_ID_DOWNLOAD_MOD) {
            if (modIdList.isNotEmpty() && modIdList.contains(remoteMod.modID)) {
                val text = binding.title.getText().toString()
                if (!text.startsWith(context.getString(R.string.installed))) {
                    binding.title.text = String.format(
                        "[%s] %s",
                        context.getString(R.string.installed),
                        text
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
