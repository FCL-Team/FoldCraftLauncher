package com.mio.ui.dialog

import android.content.Context
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.ui.adapter.RendererOverrideItemAdapter
import com.mio.ui.adapter.SpacingItemDecoration
import com.mio.util.getScreenHeight
import com.mio.util.getScreenWidth
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogRendererOverrideBinding
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * 渲染器覆盖对话框：列出当前配置的全部版本，逐版本用 FCLSpinner 指定渲染器；
 * 默认项「跟随设置」表示不覆盖（取全局/特定版本设置中的渲染器）。
 */
class RendererOverrideDialog(context: Context) : FCLDialog(context) {

    init {
        val params = window?.attributes
        params?.width = ConvertUtils.dip2px(context, 500f)
        val ratio = getScreenWidth().toFloat() / getScreenHeight().toFloat()
        if (ratio >= 1.5f) {
            params?.height = WindowManager.LayoutParams.MATCH_PARENT
        } else {
            params?.height = getScreenHeight() * 1 / 2
        }
        window?.attributes = params
        val binding = DialogRendererOverrideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val profile = Profiles.getSelectedProfile()
        val versions = profile.repository.versions
            .map { it.getId() }
            .sorted()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(
            SpacingItemDecoration(ConvertUtils.dip2px(context, 4f))
        )
        binding.recyclerView.adapter = RendererOverrideItemAdapter(context, profile, versions)
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }
}
