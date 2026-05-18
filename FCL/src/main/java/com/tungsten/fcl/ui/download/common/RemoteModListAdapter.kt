package com.tungsten.fcl.ui.download.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mio.util.AnimUtil.Companion.playTranslationX
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.ItemRemoteVersionBinding
import com.tungsten.fcl.ui.download.ModDownloadPage
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
) : RecyclerView.Adapter<RemoteModListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val modIdList: MutableList<String?> = ArrayList()

    init {
        if (downloadPage is ModDownloadPage) {
            MainActivity.getInstance().lifecycleScope.launch(Dispatchers.Default) {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemRemoteVersionBinding.inflate(
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
        val binding = ItemRemoteVersionBinding.bind(holder.itemView)
        val remoteMod = list[position]
        binding.parent.setOnClickListener {
            callback.onItemSelect(
                remoteMod
            )
        }
        binding.icon.setImageDrawable(null)
        binding.icon.tag = position
        Glide.with(context).load(remoteMod.iconUrl).into(binding.icon)
        val mod =
            ModTranslations.getTranslationsByRepositoryType(downloadPage.repository.getType())
                .getModByCurseForgeId(remoteMod.slug)
        binding.version.text =
            if (mod != null && LocaleUtils.isChinese(context)) mod.getDisplayName() else remoteMod.title
        val categories = remoteMod.categories.stream()
            .map<String> { downloadPage.getLocalizedCategory(it) }
            .collect(
                Collectors.toList()
            ).joinToString("   ")
        val tag = StringUtils.removeSuffix(categories, "   ")
        binding.tag.text = tag
        binding.date.text = remoteMod.description
        binding.tag.setSelected(true)
        binding.date.setSelected(true)
        playTranslationX(
            binding.root,
            ThemeEngine.getInstance().getTheme().animationSpeed * 30L,
            -100f,
            0f
        ).start()
        if (downloadPage is ModDownloadPage) {
            if (modIdList.isNotEmpty() && modIdList.contains(remoteMod.modID)) {
                val text = binding.version.getText().toString()
                if (!text.startsWith(context.getString(R.string.installed))) {
                    binding.version.text = String.format(
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
