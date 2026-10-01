package com.mio.ui.dialog

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import com.mio.device.VulkanCapabilities
import com.mio.device.VulkanDependency
import com.mio.device.VulkanRequirements
import com.mio.device.profileSupport
import com.mio.device.supports
import com.mio.util.getScreenHeight
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogVulkanCheckBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * Vulkan 检测结果对话框：总体结论、各版本区间支持情况与逐项扩展/特性依赖标注
 */
class VulkanCheckDialog(
    context: Context,
    private val capabilities: VulkanCapabilities?,
    private val useTurnip: Boolean,
    private val driverName: String?,
    private val onConfirm: (() -> Unit)? = null,
) : FCLDialog(context) {

    /** 缺失条目的警示红 */
    private val errorColor = "#E53935".toColorInt()

    init {
        val params = window?.attributes
        params?.width = ConvertUtils.dip2px(context, 420f)
        params?.height = (getScreenHeight() * 3 / 4).coerceAtLeast(ConvertUtils.dip2px(context, 360f))
        window?.attributes = params
        val binding = DialogVulkanCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.confirm.setOnClickListener {
            dismiss()
            onConfirm?.invoke()
        }
        fillContent(binding)
    }

    private fun fillContent(binding: DialogVulkanCheckBinding) {
        val content = binding.content
        if (capabilities == null) {
            content.addView(textView(context.getString(R.string.vulkan_check_failed), errorColor))
            return
        }
        val profiles = capabilities.profileSupport()
        val themeColor = ThemeEngine.getInstance().getTheme().getColor2()

        //总体结论
        val summary = when {
            profiles.all { it.supported } ->
                context.getString(R.string.vulkan_check_support_all, profiles.first().since)
            profiles.none { it.supported } ->
                context.getString(R.string.vulkan_check_support_none)
            else ->
                context.getString(R.string.vulkan_check_support_partial)
        }
        content.addView(textView(summary))

        //仅在部分版本区间受支持时，展示各版本区间的支持情况
        if (profiles.distinctBy { it.supported }.size > 1) {
            content.addView(sectionTitle(context.getString(R.string.vulkan_check_versions), themeColor))
            profiles.forEach { profile ->
                content.addView(
                    textView(
                        context.getString(R.string.vulkan_check_version_range, profile.versionRangeText) +
                                " " + context.getString(
                            if (profile.supported) R.string.vulkan_check_profile_support
                            else R.string.vulkan_check_profile_unsupport
                        ),
                        color = if (profile.supported) null else errorColor
                    )
                )
            }
        }

        //版本号与驱动
        content.addView(textView(context.getString(R.string.vulkan_check_version, capabilities.versionString)))
        val driverText = if (useTurnip) driverName ?: "Turnip"
        else context.getString(R.string.vulkan_check_driver_system)
        content.addView(textView(context.getString(R.string.vulkan_check_turnip, driverText)))

        //各功能/扩展的支持情况与版本依赖标注
        content.addView(sectionTitle(context.getString(R.string.vulkan_check_extensions), themeColor))
        VulkanRequirements.EXTENSIONS.forEach {
            content.addView(dependencyView(it, capabilities.supports(it)))
        }
        content.addView(sectionTitle(context.getString(R.string.vulkan_check_features), themeColor))
        VulkanRequirements.FEATURES.forEach {
            content.addView(dependencyView(it, capabilities.supports(it)))
        }
    }

    /** 依赖条目：名称行（缺失标红）+ 必需/可选版本区间行 */
    private fun dependencyView(dependency: VulkanDependency, supported: Boolean): LinearLayout {
        val group = LinearLayout(context)
        group.orientation = LinearLayout.VERTICAL
        val nameText = if (supported) dependency.name
        else context.getString(R.string.vulkan_check_item_name_missing, dependency.name)
        group.addView(textView(nameText, if (supported) null else errorColor))
        if (dependency.requiredIn.isNotEmpty()) {
            group.addView(
                indentView(
                    context.getString(
                        R.string.vulkan_check_dep_required,
                        dependency.requiredIn.joinToString { it.displayText })
                )
            )
        }
        if (dependency.optionalIn.isNotEmpty()) {
            group.addView(
                indentView(
                    context.getString(
                        R.string.vulkan_check_dep_optional,
                        dependency.optionalIn.joinToString { it.displayText })
                )
            )
        }
        return group
    }

    private fun sectionTitle(text: String, color: Int): FCLTextView {
        val view = FCLTextView(context)
        view.text = text
        view.setTextColor(color)
        view.setPadding(0, ConvertUtils.dip2px(context, 8f), 0, ConvertUtils.dip2px(context, 2f))
        return view
    }

    private fun indentView(text: String): TextView {
        val view = textView(text)
        view.setPadding(ConvertUtils.dip2px(context, 12f), 0, 0, 0)
        return view
    }

    private fun textView(text: String, color: Int? = null): TextView {
        val view = TextView(context)
        view.text = text
        view.textSize = 14f
        //默认 textColorPrimary 跟随亮暗模式，与对话框背景一致；仅缺失/警示条目显式指定红色
        color?.let { view.setTextColor(it) }
        view.setPadding(0, ConvertUtils.dip2px(context, 2f), 0, ConvertUtils.dip2px(context, 2f))
        return view
    }
}
