package com.tungsten.fcl.control

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemMenuButtonBinding
import com.tungsten.fcl.databinding.ItemMenuSeekbarBinding
import com.tungsten.fcl.databinding.ItemMenuSpinnerBinding
import com.tungsten.fcl.databinding.ItemMenuSwitchBinding
import com.tungsten.fcl.setting.Controllers
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fcllibrary.component.view.FCLSpinner
import com.tungsten.fcllibrary.component.view.FCLTextView

/** 左菜单条目标签，交互回调按此分发 */
enum class LeftMenuTag {
    EDIT_MODE, SHOW_BOUNDARY, HIDE_ALL, AUTO_FIT, AUTO_FIT_DIST,
    CURRENT_CONTROLLER, CURRENT_VIEW_GROUP,
    MANAGE_VIEW_GROUPS, ADD_BUTTON, ADD_DIRECTION, MANAGE_BUTTON_STYLE, MANAGE_DIRECTION_STYLE
}

/**
 * 左菜单平铺 RecyclerView 适配器（暂不分类）。
 * 编辑布局相关条目仅在编辑模式开启时加入列表，等价于原 edit_layout 的可见性绑定。
 */
class LeftMenuAdapter(
    private val context: Context,
    private val gameMenu: GameMenu,
    private val listener: Listener
) : RecyclerView.Adapter<LeftMenuAdapter.Holder>() {

    /** 交互回调，由 GameMenu 实现并分派到原菜单逻辑 */
    interface Listener {
        fun onButtonClick(tag: LeftMenuTag)
        fun onSwitchToggle(tag: LeftMenuTag, checked: Boolean)
        fun onSpinnerSelect(tag: LeftMenuTag, position: Int)
        fun onSeekBarChange(tag: LeftMenuTag, progress: Int)
    }

    private val typeSwitch = 0
    private val typeButton = 1
    private val typeSpinner = 2
    private val typeSeekBar = 3

    private var rows: List<Row> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun rebuild() {
        rows = buildRows()
        notifyDataSetChanged()
    }

    private fun buildRows(): List<Row> {
        val controllers = Controllers.getControllers()
        val currentController = gameMenu.controller
        val currentViewGroup = gameMenu.viewGroup
        val viewGroups = currentController?.viewGroups() ?: emptyList()
        var rows = listOf(
            Row.SpinnerRow(
                R.string.menu_controls_current,
                controllers.map { it.name },
                currentController?.let { controllers.indexOf(it).coerceAtLeast(0) } ?: 0,
                LeftMenuTag.CURRENT_CONTROLLER
            ),
            Row.SwitchRow(R.string.menu_controls_edit_mode, { gameMenu.isEditMode }, LeftMenuTag.EDIT_MODE),
            Row.SwitchRow(R.string.menu_controls_show_boundary, { gameMenu.isShowViewBoundaries }, LeftMenuTag.SHOW_BOUNDARY),
            Row.SwitchRow(R.string.menu_controls_hide_all, { gameMenu.isHideAllViews }, LeftMenuTag.HIDE_ALL),
            Row.SwitchRow(R.string.menu_controls_auto_fit, { gameMenu.menuSetting.isAutoFit }, LeftMenuTag.AUTO_FIT),
            Row.SeekBarRow(R.string.menu_controls_auto_fit_dist, 10, 0,
                { gameMenu.menuSetting.autoFitDist }, LeftMenuTag.AUTO_FIT_DIST, "dp")
        )
        if (gameMenu.isEditMode) {
            rows = rows + listOf(
                Row.SpinnerRow(
                    R.string.menu_controls_current_view_group,
                    viewGroups.map { it.name },
                    currentViewGroup?.let { viewGroups.indexOf(it).coerceAtLeast(0) } ?: 0,
                    LeftMenuTag.CURRENT_VIEW_GROUP
                ),
                Row.ButtonRow(R.string.menu_controls_groups, listOf(R.string.menu_controls_manage to LeftMenuTag.MANAGE_VIEW_GROUPS)),
                Row.ButtonRow(R.string.menu_controls_add_button, listOf(R.string.menu_controls_add_view_button to LeftMenuTag.ADD_BUTTON)),
                Row.ButtonRow(R.string.menu_controls_add_direction, listOf(R.string.menu_controls_add_view_button to LeftMenuTag.ADD_DIRECTION)),
                Row.ButtonRow(R.string.menu_controls_button_style, listOf(R.string.menu_controls_manage to LeftMenuTag.MANAGE_BUTTON_STYLE)),
                Row.ButtonRow(R.string.menu_controls_direction_style, listOf(R.string.menu_controls_manage to LeftMenuTag.MANAGE_DIRECTION_STYLE))
            )
        }
        return rows
    }

    private sealed class Row {
        data class SwitchRow(
            val labelRes: Int,
            val value: () -> Boolean,
            val tag: LeftMenuTag
        ) : Row()

        data class ButtonRow(
            val labelRes: Int,
            val buttons: List<Pair<Int, LeftMenuTag>>
        ) : Row()

        data class SpinnerRow(
            val labelRes: Int,
            val data: List<String>,
            val selection: Int,
            val tag: LeftMenuTag
        ) : Row()

        data class SeekBarRow(
            val labelRes: Int,
            val max: Int,
            val min: Int,
            val value: () -> Int,
            val tag: LeftMenuTag,
            val suffix: String? = null
        ) : Row()
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 滑块进度监听器，行复用时先移除旧监听避免累积 */
        var progressListener: InvalidationListener? = null
    }

    override fun getItemCount(): Int = rows.size

    override fun getItemViewType(position: Int): Int = when (rows[position]) {
        is Row.SwitchRow -> typeSwitch
        is Row.ButtonRow -> typeButton
        is Row.SpinnerRow -> typeSpinner
        is Row.SeekBarRow -> typeSeekBar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            typeSwitch -> ItemMenuSwitchBinding.inflate(inflater, parent, false).root
            typeButton -> ItemMenuButtonBinding.inflate(inflater, parent, false).root
            typeSpinner -> ItemMenuSpinnerBinding.inflate(inflater, parent, false).root
            else -> ItemMenuSeekbarBinding.inflate(inflater, parent, false).root
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val row = rows[position]
        // 菜单条目背景透明，露出抽屉背景
        holder.itemView.setBackground(null)
        holder.itemView.findViewById<FCLTextView>(R.id.description)?.visibility = View.GONE
        when (row) {
            is Row.SwitchRow -> bindSwitch(holder, row)
            is Row.ButtonRow -> bindButton(holder, row)
            is Row.SpinnerRow -> bindSpinner(holder, row)
            is Row.SeekBarRow -> bindSeekBar(holder, row)
        }
    }

    private fun bindSwitch(holder: Holder, row: Row.SwitchRow) {
        val binding = ItemMenuSwitchBinding.bind(holder.itemView)
        binding.switchView.text = context.getString(row.labelRes)
        binding.switchView.setOnCheckedChangeListener(null)
        binding.switchView.isChecked = row.value()
        binding.switchView.setOnCheckedChangeListener { _, checked ->
            listener.onSwitchToggle(row.tag, checked)
        }
    }

    private fun bindButton(holder: Holder, row: Row.ButtonRow) {
        val binding = ItemMenuButtonBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.labelRes)
        listOf(
            Triple(binding.button1, 0, LeftMenuTag.MANAGE_VIEW_GROUPS),
            Triple(binding.button2, 1, LeftMenuTag.MANAGE_VIEW_GROUPS),
            Triple(binding.button3, 2, LeftMenuTag.MANAGE_VIEW_GROUPS)
        ).forEach { (button, index, _) ->
            if (index < row.buttons.size) {
                val (textRes, tag) = row.buttons[index]
                button.text = context.getString(textRes)
                button.visibility = View.VISIBLE
                button.setOnClickListener { listener.onButtonClick(tag) }
            } else {
                button.visibility = View.GONE
                button.setOnClickListener(null)
            }
        }
    }

    private fun bindSpinner(holder: Holder, row: Row.SpinnerRow) {
        val binding = ItemMenuSpinnerBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.labelRes)
        // ViewBinding 对布局中的泛型控件生成 raw 类型，条目实际为 String
        @Suppress("UNCHECKED_CAST")
        val spinner = binding.spinner as FCLSpinner<String>
        spinner.setItems(row.data)
        spinner.setSelection(row.selection)
        spinner.setOnItemSelectedListener { position, _ ->
            listener.onSpinnerSelect(row.tag, position)
        }
        // 视图组尚未选中时（如切换控制器后）主动选中当前项，与原 refreshViewGroupList 的默认选中行为一致
        if (row.tag == LeftMenuTag.CURRENT_VIEW_GROUP && gameMenu.viewGroup == null && row.data.isNotEmpty()) {
            listener.onSpinnerSelect(row.tag, spinner.getSelectedIndex())
        }
    }

    private fun bindSeekBar(holder: Holder, row: Row.SeekBarRow) {
        val binding = ItemMenuSeekbarBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.labelRes)
        binding.seekBar.max = row.max
        binding.seekBar.min = row.min
        row.suffix?.let { binding.seekBar.setSuffix(it) }
        binding.seekBar.addProgressListener()
        holder.progressListener?.let { binding.seekBar.progressProperty().removeListener(it) }
        binding.seekBar.progressProperty().set(row.value())
        val progressListener = InvalidationListener {
            listener.onSeekBarChange(row.tag, binding.seekBar.progressProperty().get())
        }
        binding.seekBar.progressProperty().addListener(progressListener)
        holder.progressListener = progressListener
    }
}