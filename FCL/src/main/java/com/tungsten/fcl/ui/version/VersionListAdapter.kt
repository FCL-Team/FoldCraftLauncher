package com.tungsten.fcl.ui.version

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mio.ui.adapter.ViewHolder
import com.mio.util.AnimUtil.Companion.playTranslationX
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.ItemVersionBinding
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import java.nio.file.Files
import kotlin.io.path.isRegularFile

class VersionListAdapter(val context: Context, initList: List<VersionListItem>) :
    RecyclerView.Adapter<ViewHolder>() {
    private val versionList = mutableListOf<VersionListItem>()

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
        binding.radioButton.checkProperty().unbind()
        binding.radioButton.checkProperty().bind(versionListItem.selectedProperty())
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
        if (!versionListItem.profile.getVersionSetting(versionListItem.version).isGlobal) {
            binding.setting.visibility = View.VISIBLE
            binding.setting.setOnClickListener {
                versionListItem.profile.selectedVersion = versionListItem.version
                val uiManager = MainActivity.getInstance().uiManager
                MainActivity.getInstance().binding.manage.isSelected = true
                uiManager.manageUI.runAfterInit {
                    val tab = uiManager.manageUI.tabLayout.getTabAt(0)
                    uiManager.manageUI.tabLayout.selectTab(tab)
                }
            }
        } else {
            binding.setting.visibility = View.GONE
        }
        val modCount =
            runCatching {
                val modDir =
                    versionListItem.profile.repository.getModsDirectory(versionListItem.version)
                Files.list(modDir).filter {
                    it.isRegularFile()
                }.count().toInt()
            }.getOrNull() ?: 0
        binding.subtitle.text = String.format(
            "%s  Mods:%d",
            binding.subtitle.getText(),
            modCount
        )
        playTranslationX(
            binding.root,
            ThemeEngine.getInstance().getTheme().animationSpeed * 30L,
            -100f,
            0f
        ).start()
    }

    override fun getItemCount(): Int {
        return versionList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateVersionList(versionList: List<VersionListItem>) {
        this.versionList.clear()
        this.versionList.addAll(versionList)
        notifyDataSetChanged()
    }
}
