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
import com.tungsten.fcl.ui.download.DownloadUI
import com.tungsten.fcl.ui.manage.ModListPage.ModInfoObject
import com.tungsten.fclcore.fakefx.beans.Observable
import com.tungsten.fclcore.fakefx.beans.property.ListProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty
import com.tungsten.fclcore.fakefx.collections.FXCollections
import com.tungsten.fclcore.fakefx.collections.ListChangeListener
import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Optional
import java.util.logging.Level
import kotlin.time.Duration.Companion.milliseconds

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
    private val jobs = HashMap<String, Job>()

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
        this.listProperty.addListener(ListChangeListener { c -> // 增量插入只通知新条目（notifyItemRangeInserted），已显示条目不会重绘，
            // 避免其在每次增量刷新时被重绘（图标重新加载、状态闪烁）
            var replaced = false
            while (c.next()) {
                if (c.wasReplaced()) {
                    // 全量替换（搜索 / 勾选筛选）：整体刷新，远程查询与选中状态全部重置
                    replaced = true
                } else if (c.wasRemoved()) {
                    c.removed.forEach {
                        jobs.remove(it.modInfo.fileName)?.cancel()
                        fromSelf = true
                        selectedItemsProperty.remove(it)
                        fromSelf = false
                    }
                    notifyItemRangeRemoved(c.from, c.removedSize)
                } else if (c.wasAdded()) {
                    notifyItemRangeInserted(c.from, c.addedSize)
                }
            }
            if (replaced) {
                jobs.values.forEach { it.cancel() }
                jobs.clear()
                fromSelf = true
                selectedItemsProperty.clear()
                fromSelf = false
                notifyDataSetChanged()
            }
        })
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

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val binding = ItemLocalModBinding.bind(holder.itemView)
        val modInfoObject = listProperty[position]
        val key = modInfoObject.modInfo.fileName
        jobs[key]?.cancel()
        jobs.remove(key)
        binding.parent.backgroundTintList = ColorStateList(
            arrayOf<IntArray?>(intArrayOf()),
            intArrayOf(
                if (selectedItemsProperty.contains(modInfoObject)) ThemeEngine.getInstance()
                    .getTheme().getColor() else ThemeEngine.getInstance().getTheme()
                    .ltColor
            )
        )
        ThemeEngine.getInstance().unregisterEvent(binding.root)
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
        binding.parent.setOnClickListener { _: View? ->
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
                    ), intArrayOf(ThemeEngine.getInstance().getTheme().getColor())
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
        binding.name.text = modInfoObject.title
        val tag = getTag(modInfoObject)
        binding.tag.text = tag
        binding.tag.visibility = if (tag == "") View.GONE else View.VISIBLE
        binding.description.text = modInfoObject.subtitle
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
            uiManager.downloadUI.showDownloadPage(DownloadUI.PAGE_ID_DOWNLOAD_MOD)
            uiManager.downloadUI.downloadPage.jumpToModPage(modInfoObject.remoteMod)
        }

        drawable.setTint(ThemeEngine.getInstance().getTheme().getColor())
        binding.icon.setImageDrawable(drawable)
        val cachedRemoteMod = modInfoObject.remoteMod
        if (cachedRemoteMod != null) {
            applyRemoteMod(binding, cachedRemoteMod, modInfoObject)
        }
    }

    /**
     * 远程信息查询挂在 attach 生命周期上而不是 onBindViewHolder：RecyclerView 的 view cache
     * （视口外侧各约 2 个条目）复用缓存 view 重新显示时不会重新绑定，挂在绑定上会导致
     * 查询被防抖跳过后（如快速滑动）永久失去重试机会；attach 则每次重新显示都会触发。
     * detach 时取消在途查询，delay(200ms) 后仍存活即说明条目停在了屏幕上。
     */
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.bindingAdapterPosition
        if (position == RecyclerView.NO_POSITION || position >= listProperty.size) return
        val modInfoObject = listProperty[position]
        if (modInfoObject.remoteMod != null) return
        val key = modInfoObject.modInfo.fileName
        val existing = jobs[key]
        if (existing != null && existing.isActive) return
        val binding = ItemLocalModBinding.bind(holder.itemView)
        holder.itemView.tag = key
        val job = MainActivity.getInstance().lifecycleScope.launch {
            delay(200L.milliseconds)
            val mod = withContext(Dispatchers.IO) {
                for (type in RemoteMod.Type.entries.toTypedArray()) {
                    ensureActive()
                    try {
                        if (modInfoObject.remoteMod == null) {
                            val remoteVersion: Optional<RemoteMod.Version?> =
                                type.remoteModRepository.getRemoteVersionByLocalFile(
                                    modInfoObject.modInfo,
                                    modInfoObject.modInfo.file
                                )
                            if (remoteVersion.isPresent) {
                                val remoteMod: RemoteMod? = type.remoteModRepository
                                    .getModById(remoteVersion.get().modid())
                                modInfoObject.modInfo.remoteVersion =
                                    remoteVersion.get()
                                modInfoObject.remoteMod = remoteMod
                            } else {
                                continue
                            }
                        }
                        return@withContext modInfoObject.remoteMod
                    } catch (e: Throwable) {
                        Logging.LOG.log(
                            Level.SEVERE,
                            "getRemoteVersionByLocalFile error: ${modInfoObject.modInfo.file.fileName}\n${e.toString()}"
                        )
                    }
                }
                null
            }
            if (isActive) {
                mod?.let {
                    applyRemoteMod(binding, it, modInfoObject)
                }
            }
        }
        jobs[key] = job
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val key = holder.itemView.tag as? String ?: return
        holder.itemView.tag = null
        jobs[key]?.cancel()
        jobs.remove(key)
    }

    @SuppressLint("SetTextI18n")
    private fun applyRemoteMod(
        binding: ItemLocalModBinding,
        mod: RemoteMod,
        modInfoObject: ModInfoObject
    ) {
        binding.icon.visibility = View.VISIBLE
        Glide.with(binding.icon).load(mod.iconUrl).error(drawable)
            .into(binding.icon)
        binding.name.text = mod.title
        binding.jump.visibility = View.VISIBLE
        if (modInfoObject.mod != null && LocaleUtils.isChinese(context)) {
            val name = modInfoObject.mod.name()
            if (name.isNotEmpty() && StringUtils.containsChinese(name)) {
                binding.name.text = "[${name}]${mod.title}"
            }
        }
    }

    override fun getItemCount(): Int {
        return listProperty.size
    }
}
