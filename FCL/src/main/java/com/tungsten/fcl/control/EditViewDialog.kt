package com.tungsten.fcl.control

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.tungsten.fcl.R
import com.tungsten.fcl.control.data.ControlButtonData
import com.tungsten.fcl.control.data.ControlDirectionData
import com.tungsten.fcl.control.data.CustomControl
import com.tungsten.fcl.databinding.DialogEditViewBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils

/** 游戏内控件编辑对话框：info / event 两页切换，确认 / 复制 / 删除通过回调通知调用方 */
class EditViewDialog(
    context: Context,
    private val cloneView: CustomControl,
    menu: GameMenu?,
    private val callback: Callback,
    cloneable: Boolean,
) : FCLDialog(context) {

    interface Callback {
        fun onPositive(view: CustomControl)

        fun onClone(view: CustomControl)

        fun onDelete() {}
    }

    /** 控件编辑页：info / event 两块布局，提交时给出数据快照 */
    interface Details {
        val infoLayout: View

        val eventLayout: View

        fun getView(): CustomControl
    }

    private val details: Details = if (cloneView.type == CustomControl.ViewType.CONTROL_BUTTON) {
        EditButtonDetails(context, menu, cloneView as ControlButtonData)
    } else {
        EditDirectionDetails(context, menu, cloneView as ControlDirectionData)
    }

    private var binding: DialogEditViewBinding

    init {
        setCancelable(false)
        // 游戏内悬浮面板：与游戏菜单一致的半透明背景
        window?.setBackgroundDrawableResource(R.drawable.bg_game_menu)
        window?.setLayout(ConvertUtils.dip2px(context, 500f), ViewGroup.LayoutParams.MATCH_PARENT)
        binding = DialogEditViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.setText(
            if (cloneView.type == CustomControl.ViewType.CONTROL_BUTTON) R.string.edit_button_title
            else R.string.edit_direction_title
        )

        binding.info.setOnClickListener { showLayout(0) }
        binding.event.setOnClickListener { showLayout(1) }

        binding.clone.visibility = if (cloneable) View.VISIBLE else View.GONE
        binding.clone.setOnClickListener {
            callback.onClone(cloneView.cloneView())
            dismiss()
        }
        binding.delete.setOnClickListener {
            callback.onDelete()
            dismiss()
        }
        binding.positive.setOnClickListener {
            callback.onPositive(details.getView())
            dismiss()
        }
        binding.negative.setOnClickListener { dismiss() }

        showLayout(0)
    }

    private fun showLayout(position: Int) {
        binding.container.removeAllViews()
        binding.container.addView(
            if (position == 0) details.infoLayout else details.eventLayout,
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
