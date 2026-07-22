package com.tungsten.fcl.ui.manage

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mio.ui.adapter.ViewHolder
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.ItemLocalModBinding
import com.tungsten.fcl.ui.download.DownloadPageManager
import com.tungsten.fcl.ui.download.ModDownloadPage
import com.tungsten.fcl.ui.manage.ModListPage.ModInfoObject
import com.tungsten.fclcore.fakefx.beans.Observable
import com.tungsten.fclcore.fakefx.beans.property.ListProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty
import com.tungsten.fclcore.fakefx.collections.FXCollections
import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Optional
import java.util.logging.Level

class LocalModListAdapter(
    private val context: Context,
    private val modListPage: ModListPage,
    val onChecked: () -> Unit
) :
    RecyclerView.Adapter<ViewHolder>() {
    val listProperty: ListProperty<ModInfoObject> = SimpleListProperty(
        FXCollections.observableArrayList()
    )
    val selectedItemsProperty: ListProperty<ModInfoObject?> =
        SimpleListProperty(
            FXCollections.observableArrayList<ModInfoObject?>()
        )

    val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_cube)!!
    private val jobs = HashMap<Int, Job>()

    fun listProperty(): ListProperty<ModInfoObject> {
        return listProperty
    }

    fun selectedItemsProperty(): ListProperty<ModInfoObject?> {
        return selectedItemsProperty
    }

    fun selectAll() {
        selectedItemsProperty.clear()
        selectedItemsProperty.addAll(listProperty)
    }

    fun selectInvert() {
        val list = ArrayList<ModInfoObject>()
        listProperty.forEach {
            if (!selectedItemsProperty.get().contains(it)) {
                list.add(it)
            }
        }
        selectedItemsProperty.clear()
        selectedItemsProperty.addAll(list)
    }

    private var fromSelf = false

    init {
        this.listProperty.addListener { _: Observable? ->
            fromSelf = true
            selectedItemsProperty.clear()
            fromSelf = false
            notifyDataSetChanged()
        }
        selectedItemsProperty.addListener { _: Observable? ->
            if (!fromSelf) {
                notifyDataSetChanged()
            }
        }
    }

    private fun getTag(modInfoObject: ModInfoObject): String {
        val stringBuilder = StringBuilder()
        val modLoaderType = getModLoader(modInfoObject.modInfo.modLoaderType)
        stringBuilder.append(modLoaderType)
        if (modInfoObject.mod != null && LocaleUtils.isChinese(context)) {
            val pre = if (modLoaderType == "") "" else "   "
            stringBuilder.append(pre).append(modInfoObject.mod.getDisplayName())
        }
        return stringBuilder.toString()
    }

    private fun getModLoader(modLoaderType: ModLoaderType): String {
        return when (modLoaderType) {
            ModLoaderType.FORGE -> context.getString(R.string.install_installer_forge)
            ModLoaderType.NEO_FORGED -> context.getString(R.string.install_installer_neoforge)
            ModLoaderType.FABRIC -> context.getString(R.string.install_installer_fabric)
            ModLoaderType.LITE_LOADER -> context.getString(R.string.install_installer_liteloader)
            ModLoaderType.QUILT -> context.getString(R.string.install_installer_quilt)
            else -> ""
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemLocalModBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val binding = ItemLocalModBinding.bind(holder.itemView)
        jobs[position]?.cancel()
        jobs.remove(position)
        val modInfoObject = listProperty[position]
        binding.parent.backgroundTintList = ColorStateList(
            arrayOf<IntArray?>(intArrayOf()),
            intArrayOf(
                if (selectedItemsProperty.contains(modInfoObject)) ThemeEngine.getInstance()
                    .getTheme().color else ThemeEngine.getInstance().getTheme()
                    .ltColor
            )
        )
        ThemeEngine.getInstance().registerEvent(binding.root) {
            binding.parent.backgroundTintList = ColorStateList(
                arrayOf<IntArray?>(intArrayOf()),
                intArrayOf(
                    if (selectedItemsProperty.contains(modInfoObject)) ThemeEngine.getInstance()
                        .getTheme().color else ThemeEngine.getInstance().getTheme()
                        .ltColor
                )
            )
        }
        binding.parent.setOnClickListener { v: View? ->
            if (selectedItemsProperty.contains(modInfoObject)) {
                fromSelf = true
                selectedItemsProperty.remove(modInfoObject)
                fromSelf = false
                binding.parent.backgroundTintList = ColorStateList(
                    arrayOf<IntArray?>(
                        intArrayOf()
                    ), intArrayOf(ThemeEngine.getInstance().getTheme().ltColor)
                )
            } else {
                fromSelf = true
                selectedItemsProperty.add(modInfoObject)
                fromSelf = false
                binding.parent.backgroundTintList = ColorStateList(
                    arrayOf<IntArray?>(
                        intArrayOf()
                    ), intArrayOf(ThemeEngine.getInstance().getTheme().color)
                )
            }
        }
        //必须先清除Listener
        binding.check.setOnCheckedChangeListener(null)
        binding.check.isChecked = modInfoObject.active.get()
        binding.check.setOnCheckedChangeListener { _, checked ->
            modInfoObject.active.set(checked)
            onChecked.invoke()
        }
        binding.icon.tag = position
        binding.name.text = modInfoObject.title
        binding.name.isSelected = true
        val tag = getTag(modInfoObject)
        binding.tag.text = tag
        binding.tag.isSelected = true
        binding.tag.visibility = if (tag == "") View.GONE else View.VISIBLE
        binding.description.text = modInfoObject.subtitle
        binding.description.isSelected = true
        binding.restore.visibility = if (modInfoObject.modInfo.mod.oldFiles
                .isEmpty()
        ) View.GONE else View.VISIBLE
        binding.restore.setOnClickListener {
            val dialog = ModRollbackDialog(
                context,
                ArrayList<LocalModFile?>(modInfoObject.modInfo.mod.oldFiles)
            ) { localModFile: LocalModFile? ->
                modListPage.rollback(modInfoObject.modInfo, localModFile)
                notifyDataSetChanged()
            }
            dialog.show()
        }
        binding.info.setOnClickListener {
            val dialog = ModInfoDialog(context, modInfoObject)
            dialog.show()
        }
        binding.jump.visibility = View.GONE
        binding.jump.setOnClickListener {
            val uiManager = MainActivity.getInstance().uiManager
            MainActivity.getInstance().binding.download.isSelected = true
            MainActivity.getInstance().uiManager.downloadUI.runAfterInit {
                uiManager.downloadUI.tabLayout.selectTab(uiManager.downloadUI.tabLayout.getTabAt(2))
                uiManager.downloadUI.pageManager
                    .switchPage(DownloadPageManager.PAGE_ID_DOWNLOAD_MOD)
                val downloadPage =
                    uiManager.downloadUI.pageManager.getPageById(DownloadPageManager.PAGE_ID_DOWNLOAD_MOD) as ModDownloadPage
                downloadPage.jumpToModPage(modInfoObject.remoteMod)
            }
        }

        drawable.setTint(ThemeEngine.getInstance().getTheme().color)
        binding.icon.setImageDrawable(drawable)
        val job = MainActivity.getInstance().lifecycleScope.launch {
            val mod = withContext(Dispatchers.IO) {
                if (modInfoObject.modInfo.file.toFile()
                        .length() > 104857600
                ) return@withContext null
                for (type in RemoteMod.Type.entries.toTypedArray()) {
                    try {
                        if (modInfoObject.remoteMod == null) {
                            val remoteVersion: Optional<RemoteMod.Version?> =
                                type.remoteModRepository.getRemoteVersionByLocalFile(
                                    modInfoObject.modInfo,
                                    modInfoObject.modInfo.file
                                )
                            if (remoteVersion.isPresent) {
                                val remoteMod: RemoteMod? = type.remoteModRepository
                                    .getModById(remoteVersion.get().modid)
                                modInfoObject.modInfo.remoteVersion = remoteVersion.get()
                                modInfoObject.remoteMod = remoteMod
                            } else {
                                continue
                            }
                        }
                        return@withContext modInfoObject.remoteMod
                    } catch (e: Throwable) {
                        System.gc()
                        Logging.LOG.log(Level.SEVERE, e.toString())
                    }
                }
                null
            }
            mod?.let {
                if (isActive && binding.icon.tag as Int == position) {
                    binding.icon.visibility = View.VISIBLE
                    Glide.with(binding.icon).load(mod.iconUrl).error(drawable)
                        .into(binding.icon)
                    binding.name.text = mod.title
                    binding.jump.visibility = View.VISIBLE
                    if (modInfoObject.mod != null && LocaleUtils.isChinese(context)) {
                        val name = modInfoObject.mod.name
                        if (name.isNotEmpty() && StringUtils.containsChinese(name)) {
                            binding.name.text = "[${name}]${mod.title}"
                        }
                    }

                }
            }
        }
        jobs[position] = job
    }

    override fun getItemCount(): Int {
        return listProperty.size
    }
}
