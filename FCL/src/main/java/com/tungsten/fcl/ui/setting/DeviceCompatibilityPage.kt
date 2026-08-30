package com.tungsten.fcl.ui.setting

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.data.Renderer
import com.mio.manager.RendererManager
import com.mio.ui.adapter.SpacingItemDecoration
import com.mio.util.DeviceCapability
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemAboutDescBinding
import com.tungsten.fcl.databinding.PageSettingDeviceBinding
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.ui.FCLPage

/**
 * 兼容性页：只读展示本机探测到的硬件信息，以及各内置渲染器已知支持的 Minecraft 版本范围。
 * 数据来源于真实探测值（架构/内存/GPU/GLES/Vulkan）与 RendererManager 中已有的渲染器元数据，
 * 不是一个经过验证的硬件兼容性数据库，仅作最佳估计参考（见 device_compatibility_disclaimer）。
 */
class DeviceCompatibilityPage(context: Context?, id: Int) :
    FCLPage(context, id, R.layout.page_setting_device) {

    private lateinit var binding: PageSettingDeviceBinding

    init {
        create()
    }

    private fun create() {
        binding = PageSettingDeviceBinding.bind(contentView)
        binding.deviceList.layoutManager = LinearLayoutManager(context)
        val rowSpacing = (8 * context.resources.displayMetrics.density).toInt()
        binding.deviceList.addItemDecoration(
            SpacingItemDecoration(
                rowSpacing,
                { _, _ -> rowSpacing },
                { ThemeEngine.getInstance().getTheme().getColor() }
            )
        )
        binding.deviceList.adapter = DeviceInfoAdapter(buildLines())
    }

    override fun refresh(vararg param: Any?): Task<*>? {
        return null
    }

    private fun buildLines(): List<String> {
        val lines = mutableListOf<String>()

        lines += context.getString(R.string.device_info_header)
        lines += context.getString(R.string.device_info_android_version, DeviceCapability.getAndroidVersionString())
        lines += context.getString(R.string.device_info_arch, DeviceCapability.getArchString())
        lines += context.getString(
            R.string.device_info_ram,
            DeviceCapability.getTotalRamMb(context),
            DeviceCapability.getFreeRamMb(context)
        )
        lines += context.getString(
            R.string.device_info_ram_recommendation,
            DeviceCapability.getRecommendedRamMb(context)
        )

        val gpuInfo = DeviceCapability.getGpuInfo()
        lines += if (gpuInfo != null) {
            context.getString(R.string.device_info_gpu, gpuInfo.vendor, gpuInfo.renderer)
        } else {
            context.getString(R.string.device_info_gpu_unknown)
        }
        lines += context.getString(R.string.device_info_gles, DeviceCapability.getDeclaredGlEsVersion(context))

        val vulkanVersion = DeviceCapability.getVulkanVersion(context)
        lines += if (DeviceCapability.hasVulkanSupport(context)) {
            context.getString(R.string.device_info_vulkan_yes, vulkanVersion ?: "?")
        } else {
            context.getString(R.string.device_info_vulkan_no)
        }

        lines += context.getString(R.string.device_renderer_header)
        RendererManager.rendererList.forEach { renderer ->
            lines += rendererCompatibilityLine(renderer)
        }

        lines += context.getString(R.string.device_compatibility_disclaimer)

        return lines
    }

    private fun rendererCompatibilityLine(renderer: Renderer): String {
        val hasMin = renderer.minMCver.isNotEmpty()
        val hasMax = renderer.maxMCver.isNotEmpty()
        return when {
            hasMin && hasMax -> context.getString(
                R.string.device_renderer_mc_range_both, renderer.name, renderer.minMCver, renderer.maxMCver
            )
            hasMin -> context.getString(
                R.string.device_renderer_mc_range_min_only, renderer.name, renderer.minMCver
            )
            hasMax -> context.getString(
                R.string.device_renderer_mc_range_max_only, renderer.name, renderer.maxMCver
            )
            else -> context.getString(R.string.device_renderer_mc_range_none, renderer.name)
        }
    }

    private class DeviceInfoAdapter(
        private val lines: List<String>
    ) : RecyclerView.Adapter<DeviceInfoAdapter.Holder>() {

        override fun getItemCount(): Int = lines.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val inflater = LayoutInflater.from(parent.context)
            return Holder(ItemAboutDescBinding.inflate(inflater, parent, false).root)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.itemView.setBackgroundResource(
                when {
                    itemCount == 1 -> R.drawable.bg_item_rounded
                    position == 0 -> R.drawable.bg_item_rounded_top
                    position == itemCount - 1 -> R.drawable.bg_item_rounded_bottom
                    else -> R.drawable.bg_item_rounded_middle
                }
            )
            ThemeEngine.getInstance().unregisterEvent(holder.itemView)
            ThemeEngine.getInstance().registerEvent(holder.itemView) {
                holder.itemView.backgroundTintList =
                    ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)
            }
            ItemAboutDescBinding.bind(holder.itemView).title.text = lines[position]
        }

        class Holder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView)
    }
}
