package com.mio.ui.dialog

import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.mio.plugin.RendererPlugin.EnvSpec
import com.mio.plugin.RendererPlugin.EnvType
import com.mio.plugin.RendererPlugin.EnvValue
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogRendererEnvBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLEditText
import com.tungsten.fcllibrary.component.view.FCLSpinner
import com.tungsten.fcllibrary.component.view.FCLSwitch
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * v2 渲染器插件的可配置环境变量编辑对话框：
 * selectable → 选项 Spinner（check 非 null 时附带启用开关），
 * customizable → 输入框（留空不启用），toggleable → 开关。
 * 确定时全量回传所有可配置项的当前值。
 */
class RendererEnvDialog(
    context: Context,
    title: String,
    private val specs: List<EnvSpec>,
    private val onConfirm: (Map<String, EnvValue>) -> Unit,
) : FCLDialog(context) {

    private val binding = DialogRendererEnvBinding.inflate(layoutInflater)

    private val switches = mutableMapOf<String, FCLSwitch>()
    private val spinners = mutableMapOf<String, FCLSpinner<String>>()
    private val inputs = mutableMapOf<String, FCLEditText>()

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

        setContentView(binding.root)
        binding.title.text = title
        specs.forEach { binding.envContainer.addView(buildRow(it)) }
        binding.ok.setOnClickListener { submit() }
        binding.cancel.setOnClickListener { dismiss() }
    }

    private fun buildRow(spec: EnvSpec): LinearLayout {
        val density = context.resources.displayMetrics.density
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, (8 * density).toInt(), 0, 0)
        }
        val header = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val label = FCLTextView(context).apply {
            text = spec.title
            textSize = 15f
            // 对话框是普通白/浅底，用主题默认文字色（autoTint 是与主色对比的黑/白，主色偏深时为白色，会看不清）
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        }
        header.addView(label)

        when (spec.type) {
            EnvType.SELECTABLE -> {
                if (spec.checkable) {
                    header.addView(FCLSwitch(context).apply {
                        isChecked = spec.enabled
                        switches[spec.key] = this
                    })
                }
                val spinner = FCLSpinner<String>(context).apply {
                    // 对话框恒浅底（dialog_background），关闭 autoTint（深色模式下其残留
                    // 白色对比色会与浅底同色）并显式使用固定深色文字
                    setAutoTextTint(false)
                    setTextColor(ContextCompat.getColor(context, R.color.primary_text))
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply { topMargin = (4 * density).toInt() }
                    setItems(spec.options)
                    setSelection(spec.options.indexOf(spec.value).coerceAtLeast(0))
                    spinners[spec.key] = this
                }
                row.addView(header)
                row.addView(spinner)
            }

            EnvType.CUSTOMIZABLE -> {
                val input = FCLEditText(context).apply {
                    hint = spec.defaultValue
                    setText(spec.value)
                    maxLines = 1
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply { topMargin = (4 * density).toInt() }
                    inputs[spec.key] = this
                }
                row.addView(header)
                row.addView(input)
            }

            EnvType.TOGGLEABLE -> {
                header.addView(FCLSwitch(context).apply {
                    isChecked = spec.enabled
                    switches[spec.key] = this
                })
                row.addView(header)
            }
        }
        return row
    }

    private fun submit() {
        val result = mutableMapOf<String, EnvValue>()
        specs.forEach { spec ->
            when (spec.type) {
                EnvType.SELECTABLE -> result[spec.key] = EnvValue(
                    enabled = switches[spec.key]?.isChecked,
                    value = spinners[spec.key]?.getSelectedItem()?.toString()
                )

                EnvType.CUSTOMIZABLE -> result[spec.key] = EnvValue(
                    value = inputs[spec.key]?.text?.toString().orEmpty()
                )

                EnvType.TOGGLEABLE -> result[spec.key] = EnvValue(
                    enabled = switches[spec.key]?.isChecked
                )
            }
        }
        onConfirm(result)
        dismiss()
    }
}
