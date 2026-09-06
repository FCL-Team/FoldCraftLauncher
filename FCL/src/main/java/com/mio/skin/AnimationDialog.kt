package com.mio.skin

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.LinearLayoutCompat
import com.mio.ui.applySelectableItemStyle
import com.mio.util.getScreenWidth
import com.tungsten.fcl.databinding.DialogAnimationSwitchBinding
import com.tungsten.fcl.databinding.ItemAnimationBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog

/** 动画选中回调（SAM 接口，便于 Java 侧 lambda 调用），参数为烘焙 clip 名 */
fun interface OnAnimationSelectedListener {
    fun onSelected(clipId: String)
}

/**
 * 动画切换弹窗：列出全部支持的动画并标记当前项，点击即切换并关闭。
 */
class AnimationDialog(
    context: Context,
    private val currentId: String?,
    private val onSelected: OnAnimationSelectedListener
) : FCLDialog(context) {

    private val binding = DialogAnimationSwitchBinding.inflate(layoutInflater)
    private val density = context.resources.displayMetrics.density

    init {
        setContentView(binding.root)
        setCancelable(true)

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
        return params
    }

    override fun show() {
        window?.setLayout(getScreenWidth() / 2, WindowManager.LayoutParams.WRAP_CONTENT)
        super.show()
    }
}
