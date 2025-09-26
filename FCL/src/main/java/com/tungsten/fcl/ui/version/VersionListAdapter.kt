package com.tungsten.fcl.ui.version

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import com.mio.util.AnimUtil.Companion.playTranslationX
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.ItemVersionBinding
import com.tungsten.fcl.ui.manage.ManagePageManager
import com.tungsten.fcllibrary.component.FCLAdapter
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLImageButton
import com.tungsten.fcllibrary.component.view.FCLRadioButton
import com.tungsten.fcllibrary.component.view.FCLTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VersionListAdapter(context: Context?, private val list: ArrayList<VersionListItem>) :
    FCLAdapter(context) {
    internal class ViewHolder {
        lateinit var radioButton: FCLRadioButton
        lateinit var icon: AppCompatImageView
        lateinit var title: FCLTextView
        lateinit var tag: FCLTextView
        lateinit var subtitle: FCLTextView
        lateinit var setting: FCLImageButton
        lateinit var delete: FCLImageButton
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(i: Int): Any? {
        return list[i]
    }

    @SuppressLint("DefaultLocale")
    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        var view = view
        val viewHolder: ViewHolder
        if (view == null) {
            viewHolder = ViewHolder()
            val binding = ItemVersionBinding.inflate(LayoutInflater.from(context))
            view = binding.root
            viewHolder.radioButton = binding.radio
            viewHolder.icon = binding.icon
            viewHolder.title = binding.title
            viewHolder.tag = binding.tag
            viewHolder.subtitle = binding.subtitle
            viewHolder.setting = binding.setting
            viewHolder.delete = binding.delete
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        val versionListItem = list[i]
        viewHolder.radioButton.checkProperty().unbind()
        viewHolder.radioButton.checkProperty().bind(versionListItem.selectedProperty())
        viewHolder.icon.background = versionListItem.drawable
        viewHolder.title.text = versionListItem.version
        viewHolder.tag.visibility = if (versionListItem.tag == null) View.GONE else View.VISIBLE
        if (versionListItem.tag != null) {
            viewHolder.tag.text = versionListItem.tag
        }
        viewHolder.subtitle.text = versionListItem.libraries
        viewHolder.radioButton.setOnClickListener { v: View? ->
            versionListItem.profile.selectedVersion = versionListItem.version
        }
        viewHolder.delete.setOnClickListener { view1: View? ->
            Versions.deleteVersion(
                context,
                versionListItem.profile,
                versionListItem.version
            )
        }
        viewHolder.subtitle.tag = i
        view.setOnClickListener { v: View? ->
            versionListItem.profile.selectedVersion = versionListItem.version
        }
        if (!versionListItem.profile.getVersionSetting(versionListItem.version).isGlobal) {
            viewHolder.setting.visibility = View.VISIBLE
            viewHolder.setting.setOnClickListener {
                versionListItem.profile.selectedVersion = versionListItem.version
                val uiManager = MainActivity.getInstance().uiManager
                MainActivity.getInstance().binding.manage.isSelected = true
                uiManager.manageUI.checkPageManager {
                    val tab = uiManager.manageUI.tabLayout.getTabAt(0)
                    uiManager.manageUI.tabLayout.selectTab(tab)
                }
            }
        } else {
            viewHolder.setting.visibility = View.GONE
        }
        MainActivity.getInstance().lifecycleScope.launch {
            var modCount = 0
            runCatching {
                modCount = withContext(Dispatchers.IO) {
                    versionListItem.profile.repository.getModManager(versionListItem.version)
                        .getMods().size
                }
            }
            if ((viewHolder.subtitle.tag as Int) == i) {
                viewHolder.subtitle.text = String.format(
                    "%s  Mods:%d",
                    viewHolder.subtitle.getText(),
                    modCount
                )
            }
        }
        playTranslationX(
            view,
            ThemeEngine.getInstance().getTheme().animationSpeed * 30L,
            -100f,
            0f
        ).start()
        return view
    }
}
