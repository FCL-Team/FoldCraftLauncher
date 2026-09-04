package com.mio.ui.dialog

import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogMiolibpatcherBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLSwitch
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * MioLibPatcher 功能开关对话框（与 v2 渲染器环境变量对话框同款交互）：
 * ALC10 / Sable Rapier / ASM 后门 三个开关，标题下带说明，确定时全量回传。
 */
class MioLibPatcherDialog(
    context: Context,
    alc10: Boolean,
    sablerapier: Boolean,
    asmBackport: Boolean,
    private val onConfirm: (alc10: Boolean, sablerapier: Boolean, asmBackport: Boolean) -> Unit,
) : FCLDialog(context) {

    private val binding = DialogMiolibpatcherBinding.inflate(layoutInflater)

    private lateinit var alc10Switch: FCLSwitch
    private lateinit var sableSwitch: FCLSwitch
    private lateinit var asmSwitch: FCLSwitch

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
        binding.title.setText(R.string.plugin_miolibpatcher_name)
        alc10Switch = addFeatureRow(
            R.string.plugin_miolibpatcher_alc10,
            R.string.plugin_miolibpatcher_alc10_desc,
            alc10
        )
        sableSwitch = addFeatureRow(
            R.string.plugin_miolibpatcher_sable,
            R.string.plugin_miolibpatcher_sable_desc,
            sablerapier
        )
        asmSwitch = addFeatureRow(
            R.string.plugin_miolibpatcher_asm,
            R.string.plugin_miolibpatcher_asm_desc,
            asmBackport
        )
        binding.ok.setOnClickListener { submit() }
        binding.cancel.setOnClickListener { dismiss() }
    }

    /** 开关行：标题 + 开关（右侧），下方跟说明小字，返回开关供提交时取值 */
    private fun addFeatureRow(titleRes: Int, descRes: Int, checked: Boolean): FCLSwitch {
        val density = context.resources.displayMetrics.density
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, (8 * density).toInt(), 0, 0)
        }
        val label = FCLTextView(context).apply {
            text = context.getString(titleRes)
            textSize = 15f
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        }
        row.addView(label)
        val switch = FCLSwitch(context).apply {
            isChecked = checked
        }
        row.addView(switch)

        val wrapper = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, (8 * density).toInt(), 0, 0)
        }
        wrapper.addView(row)
        wrapper.addView(FCLTextView(context).apply {
            text = context.getString(descRes)
            textSize = 12f
            setPadding(0, (2 * density).toInt(), 0, 0)
        })
        binding.featureContainer.addView(wrapper)
        return switch
    }

    private fun submit() {
        onConfirm(alc10Switch.isChecked, sableSwitch.isChecked, asmSwitch.isChecked)
        dismiss()
    }
}