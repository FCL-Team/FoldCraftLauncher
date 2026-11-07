package com.mio.ui.dialog

import android.content.Context
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.data.Renderer
import com.mio.manager.RendererManager
import com.mio.plugin.RendererPlugin
import com.mio.ui.adapter.RendererSelectItemAdapter
import com.mio.ui.adapter.SpacingItemDecoration
import com.mio.util.getScreenHeight
import com.mio.util.getScreenWidth
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogRendererSelectBinding
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.setting.VersionSetting
import com.tungsten.fcl.vr.QuestVrRuntime
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import java.util.function.Consumer

/**
 * 渲染器选择对话框：列表按当前实例是否为 VR 模式过滤。
 * explicitVersionSetting 指定目标设置；缺省时取所选 Profile 的全局或当前版本设置。
 */
class RendererSelectDialog(
    context: Context,
    val isGlobal: Boolean,
    private val explicitVersionSetting: VersionSetting? = null,
    val callback: Consumer<String>
) : FCLDialog(context) {

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
        val binding = DialogRendererSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.title.text = context.getString(R.string.settings_fcl_renderer)
        val versionSetting = explicitVersionSetting
            ?: if (isGlobal) Profiles.getSelectedProfile().globalVersionSetting
            else Profiles.getSelectedProfile().versionSetting
        val vrEnabled = !isGlobal && versionSetting.isVrEnabled && QuestVrRuntime.isSupported(context)
        val currentId = RendererManager.getRendererForMode(versionSetting.renderer, vrEnabled).id
        val availableRenderers = RendererManager.getRenderersForMode(vrEnabled).toMutableList()
        // 齿轮图标：内置渲染器的可配置环境变量，复用插件页同款 RendererEnvDialog
        val configCallback: (Renderer) -> Unit = { renderer ->
            val specs = RendererPlugin.builtinEnvSpecs(context, renderer.id)
            if (specs.isNotEmpty()) {
                RendererEnvDialog(context, renderer.name, specs) { values ->
                    RendererPlugin.updateBuiltinEnvConfigs(context, renderer.id, values)
                }.show()
            }
        }
        val adapter = RendererSelectItemAdapter(
            context,
            availableRenderers,
            currentId,
            configCallback
        ) { renderer ->
            versionSetting.renderer = renderer.id
            dismiss()
            callback.accept(renderer.des)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(
            SpacingItemDecoration(ConvertUtils.dip2px(context, 10f))
        )
        binding.recyclerView.adapter = adapter
        // 打开时定位到当前选中的渲染器（首次布局前调用，LayoutManager 会从该位置开始布局）
        val selectedIndex = availableRenderers.indexOfFirst { it.id == currentId }
        if (selectedIndex > 0) {
            binding.recyclerView.scrollToPosition(selectedIndex)
        }
        binding.refresh.setOnClickListener {
            RendererManager.refresh(context)
            availableRenderers.clear()
            availableRenderers.addAll(RendererManager.getRenderersForMode(vrEnabled))
            adapter.notifyDataSetChanged()
        }
        binding.advanced.setOnClickListener {
            RendererAdvancedDialog(context).show()
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }
}
