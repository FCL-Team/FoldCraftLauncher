package com.tungsten.fcl.ui.manage

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.ItemLocalModBinding
import com.tungsten.fcl.ui.manage.ModListPage.ModInfoObject
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.Observable
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.ListProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty
import com.tungsten.fclcore.fakefx.collections.FXCollections
import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fcllibrary.component.FCLAdapter
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLCheckBox
import com.tungsten.fcllibrary.component.view.FCLImageButton
import com.tungsten.fcllibrary.component.view.FCLImageView
import com.tungsten.fcllibrary.component.view.FCLLinearLayout
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Optional

class LocalModListAdapter(context: Context?, private val modListPage: ModListPage) :
    FCLAdapter(context) {

    val listProperty: ListProperty<ModInfoObject> = SimpleListProperty<ModInfoObject>(
        FXCollections.observableArrayList<ModInfoObject?>()
    )
    val selectedItemsProperty: ListProperty<ModInfoObject?> =
        SimpleListProperty<ModInfoObject?>(
            FXCollections.observableArrayList<ModInfoObject?>()
        )

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

    private var fromSelf = false

    init {
        this.listProperty.addListener(InvalidationListener { observable: Observable? ->
            fromSelf = true
            selectedItemsProperty.clear()
            fromSelf = false
            notifyDataSetChanged()
        })
        selectedItemsProperty.addListener(InvalidationListener { observable: Observable? ->
            if (!fromSelf) {
                notifyDataSetChanged()
            }
        })
    }

    private class ViewHolder {
        lateinit var parent: FCLLinearLayout
        lateinit var checkBox: FCLCheckBox
        lateinit var icon: FCLImageView
        lateinit var name: FCLTextView
        lateinit var tag: FCLTextView
        lateinit var description: FCLTextView
        lateinit var restore: FCLImageButton
        lateinit var info: FCLImageButton
        var booleanProperty: BooleanProperty? = null
    }

    override fun getCount(): Int {
        return listProperty.size
    }

    override fun getItem(i: Int): Any? {
        return listProperty[i]
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        var view = view
        val viewHolder: ViewHolder
        if (view == null) {
            viewHolder = ViewHolder()
            val binding = ItemLocalModBinding.inflate(LayoutInflater.from(context))
            view = binding.root
            viewHolder.parent = binding.parent
            viewHolder.checkBox = binding.check
            viewHolder.icon = binding.icon
            viewHolder.name = binding.name
            viewHolder.tag = binding.tag
            viewHolder.description = binding.description
            viewHolder.restore = binding.restore
            viewHolder.info = binding.info
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        val modInfoObject = listProperty[i]
        viewHolder.parent.setBackgroundTintList(
            ColorStateList(
                arrayOf<IntArray?>(intArrayOf()),
                intArrayOf(
                    if (selectedItemsProperty.contains(modInfoObject)) ThemeEngine.getInstance()
                        .getTheme().color else ThemeEngine.getInstance().getTheme()
                        .ltColor
                )
            )
        )
        ThemeEngine.getInstance().registerEvent(viewHolder.parent, Runnable {
            viewHolder.parent.setBackgroundTintList(
                ColorStateList(
                    arrayOf<IntArray?>(intArrayOf()),
                    intArrayOf(
                        if (selectedItemsProperty.contains(modInfoObject)) ThemeEngine.getInstance()
                            .getTheme().color else ThemeEngine.getInstance().getTheme()
                            .ltColor
                    )
                )
            )
        })
        viewHolder.parent.setOnClickListener(View.OnClickListener { v: View? ->
            if (selectedItemsProperty.contains(modInfoObject)) {
                fromSelf = true
                selectedItemsProperty.remove(modInfoObject)
                fromSelf = false
                viewHolder.parent.setBackgroundTintList(
                    ColorStateList(
                        arrayOf<IntArray?>(
                            intArrayOf()
                        ), intArrayOf(ThemeEngine.getInstance().getTheme().ltColor)
                    )
                )
            } else {
                fromSelf = true
                selectedItemsProperty.add(modInfoObject)
                fromSelf = false
                viewHolder.parent.setBackgroundTintList(
                    ColorStateList(
                        arrayOf<IntArray?>(
                            intArrayOf()
                        ), intArrayOf(ThemeEngine.getInstance().getTheme().color)
                    )
                )
            }
        })
        viewHolder.checkBox.addCheckedChangeListener()
        viewHolder.booleanProperty?.let {
            viewHolder.checkBox.checkProperty().unbindBidirectional(it)
        }
        viewHolder.checkBox.checkProperty()
            .bindBidirectional(modInfoObject.active.also { viewHolder.booleanProperty = it })
        viewHolder.icon.tag = i
        viewHolder.icon.setImageBitmap(null)
        viewHolder.name.text = modInfoObject.title
        val tag = getTag(modInfoObject)
        viewHolder.tag.text = tag
        viewHolder.tag.visibility = if (tag == "") View.GONE else View.VISIBLE
        viewHolder.description.text = modInfoObject.subtitle
        viewHolder.restore.setVisibility(
            if (modInfoObject.modInfo.mod.oldFiles
                    .isEmpty()
            ) View.GONE else View.VISIBLE
        )
        viewHolder.restore.setOnClickListener(View.OnClickListener { v: View? ->
            val dialog = ModRollbackDialog(
                context,
                ArrayList<LocalModFile?>(modInfoObject.modInfo.mod.oldFiles),
                ModRollbackDialog.Callback { localModFile: LocalModFile? ->
                    modListPage.rollback(modInfoObject.modInfo, localModFile)
                    notifyDataSetChanged()
                })
            dialog.show()
        })
        viewHolder.info.setOnClickListener(View.OnClickListener { v: View? ->
            val dialog = ModInfoDialog(context, modInfoObject)
            dialog.show()
        })
        val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_cube)
        if (drawable != null) {
            drawable.setTint(ThemeEngine.getInstance().getTheme().color)
            viewHolder.icon.setImageDrawable(drawable)
        }
        MainActivity.getInstance().lifecycleScope.launch {
            val mod = withContext(Dispatchers.IO) {
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
                    } catch (_: Throwable) {
                    }
                }
                null
            }
            mod?.let {
                if (viewHolder.icon.tag as Int == i) {
                    viewHolder.icon.setVisibility(View.VISIBLE)
                    Glide.with(viewHolder.icon).load(mod.iconUrl)
                        .into(viewHolder.icon)
                    viewHolder.name.text = mod.title
                }
            }
        }
        return view
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
}
