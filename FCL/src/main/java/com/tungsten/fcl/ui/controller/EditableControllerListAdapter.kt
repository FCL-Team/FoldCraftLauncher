package com.tungsten.fcl.ui.controller

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Controller
import com.tungsten.fcllibrary.component.FCLAdapter
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.view.FCLImageButton
import com.tungsten.fcllibrary.component.view.FCLTextView

/**
 * 控制布局列表适配器：列表项选择/删除通过回调交给页面处理，
 * 选中高亮由 [getSelected] 提供，不使用 fakefx property。
 */
class EditableControllerListAdapter(
    context: Context,
    private val list: List<Controller>,
    private val getSelected: () -> Controller?,
    private val onSelect: (Controller) -> Unit,
    private val onDelete: (Controller) -> Unit
) : FCLAdapter(context) {

    override fun getCount(): Int = list.size

    override fun getItem(i: Int): Any = list[i]

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun getView(i: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            view = LayoutInflater.from(context)
                .inflate(R.layout.item_controller_editable, parent, false)
            holder.parent = view.findViewById(R.id.parent)
            holder.name = view.findViewById(R.id.name)
            holder.version = view.findViewById(R.id.version)
            holder.delete = view.findViewById(R.id.delete)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        val controller = list[i]
        holder.parent.background = if (controller == getSelected()) {
            context.getDrawable(R.drawable.bg_container_transparent_selected)
        } else {
            context.getDrawable(R.drawable.bg_container_transparent_clickable)
        }
        holder.name.text = controller.name
        holder.version.text = controller.version
        holder.parent.setOnClickListener { onSelect(controller) }
        holder.delete.setOnClickListener {
            FCLAlertDialog.Builder(context)
                .setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
                .setCancelable(false)
                .setMessage(context.getString(R.string.control_delete))
                .setPositiveButton { onDelete(controller) }
                .setNegativeButton(null)
                .create()
                .show()
        }
        return view
    }

    private class ViewHolder {
        lateinit var parent: ConstraintLayout
        lateinit var name: FCLTextView
        lateinit var version: FCLTextView
        lateinit var delete: FCLImageButton
    }
}
