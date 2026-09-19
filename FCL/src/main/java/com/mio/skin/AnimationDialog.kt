package com.mio.skin

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.LinearLayoutCompat
import com.mio.ui.applySelectableItemStyle
import com.mio.ui.dialogCardBackground
import com.mio.util.getScreenWidth
import com.tungsten.fcl.databinding.DialogAnimationSwitchBinding
import com.tungsten.fcl.databinding.ItemAnimationBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog

/** 动画选中回调（SAM 接口，便于 Java 侧 lambda 调用），参数为烘焙 clip 名 */
fun interface OnAnimationSelectedListener {
    fun onSelected(clipId: String)
}

/** 3D 皮肤层开关回调（SAM 接口，便于 Java 侧 lambda 调用），参数为新的开关状态 */
fun interface OnSolidLayerToggledListener {
    fun onToggled(enabled: Boolean)
}

/** 身体与腿部分离开关回调（SAM 接口，便于 Java 侧 lambda 调用），参数为新的开关状态 */
fun interface OnBodySeparationToggledListener {
    fun onToggled(separated: Boolean)
}

/**
 * 皮肤模型设置弹窗：顶部 3D 皮肤层与身体腿部分离两个开关，下方列出全部支持的动画并标记当前项；
 * 点动画条目即切换并关闭，开关切换即时生效且保持弹窗打开。
 */
class AnimationDialog(
    context: Context,
    private val currentId: String?,
    private val solidLayerEnabled: Boolean,
    private val upperBodySeparated: Boolean,
    private val onSelected: OnAnimationSelectedListener,
    private val onSolidLayerToggled: OnSolidLayerToggledListener,
    private val onBodySeparationToggled: OnBodySeparationToggledListener
) : FCLDialog(context) {

    private val binding = DialogAnimationSwitchBinding.inflate(layoutInflater)
    private val density = context.resources.displayMetrics.density

    init {
        setContentView(binding.root)
        setCancelable(true)

        binding.solidLayerSwitch.isChecked = solidLayerEnabled
        binding.solidLayerRow.background = dialogCardBackground(context, density)
        binding.solidLayerRow.setOnClickListener { binding.solidLayerSwitch.toggle() }
        binding.solidLayerSwitch.setOnCheckedChangeListener { _, checked ->
            onSolidLayerToggled.onToggled(checked)
        }

        binding.bodySeparationSwitch.isChecked = upperBodySeparated
        binding.bodySeparationRow.background = dialogCardBackground(context, density)
        binding.bodySeparationRow.setOnClickListener { binding.bodySeparationSwitch.toggle() }
        binding.bodySeparationSwitch.setOnCheckedChangeListener { _, checked ->
            onBodySeparationToggled.onToggled(checked)
        }

        SkinAnimations.entries.forEach { entry ->
            val selected = entry.id == currentId
            binding.container.addView(createRow(entry.nameRes, entry.id, selected), rowParams())
        }
    }

    private fun createRow(nameRes: Int, clipId: String, selected: Boolean): View {
        val row = ItemAnimationBinding.inflate(layoutInflater)
        row.text.setText(nameRes)
        row.root.setOnClickListener {
            onSelected.onSelected(clipId)
            dismiss()
        }
        // 选中态统一样式：当前动画主题色底 + 勾选，其余普通卡片
        applySelectableItemStyle(context, row.root, row.check, selected, density)
        return row.root
    }

    private fun rowParams(): LinearLayoutCompat.LayoutParams {
        val params = LinearLayoutCompat.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.topMargin = (6 * density).toInt()
        params.marginStart = (3 * density).toInt()
        params.marginEnd = (3 * density).toInt()
        return params
    }

    override fun show() {
        window?.setLayout(getScreenWidth() / 2, WindowManager.LayoutParams.WRAP_CONTENT)
        super.show()
    }
}
