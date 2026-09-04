package com.mio.ui.dialog

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.manager.RendererManager
import com.mio.ui.adapter.RendererSelectItemAdapter
import com.mio.ui.adapter.SpacingItemDecoration
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogRendererSelectBinding
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import java.util.function.Consumer

class RendererSelectDialog(
    context: Context,
    val isGlobal: Boolean,
    val callback: Consumer<String>
) : FCLDialog(context) {

    init {
        val point = Point()
        window?.windowManager?.defaultDisplay?.getSize(point)
        val params = window?.attributes
        params?.width = ConvertUtils.dip2px(context, 500f)
        val ratio = point.x.toFloat() / point.y.toFloat()
        if (ratio >= 1.5f) {
            params?.height = WindowManager.LayoutParams.MATCH_PARENT
        } else {
            params?.height = point.y * 1 / 2
        }
        window?.attributes = params
        val binding = DialogRendererSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.title.text = context.getString(R.string.settings_fcl_renderer)
        val adapter = RendererSelectItemAdapter(
            context,
            RendererManager.rendererList,
            currentRendererId()
        ) { renderer ->
            val versionSetting =
                if (isGlobal) Profiles.getSelectedProfile().globalVersionSetting else Profiles.getSelectedProfile().versionSetting
            versionSetting.renderer = renderer.id
            dismiss()
            callback.accept(renderer.des)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(
            SpacingItemDecoration(ConvertUtils.dip2px(context, 10f))
        )
        binding.recyclerView.adapter = adapter
        binding.refresh.setOnClickListener {
            RendererManager.refresh(context)
            // rendererList 是同一实例被 clear 后重填，直接全量刷新
            adapter.notifyDataSetChanged()
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }

    private fun currentRendererId(): String {
        return if (isGlobal) Profiles.getSelectedProfile().globalVersionSetting.renderer
        else Profiles.getSelectedProfile().versionSetting.renderer
    }
}
