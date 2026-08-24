package com.tungsten.fcl.control

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.mio.util.getScreenHeight
import com.mio.util.getScreenWidth
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemMenuButtonBinding
import com.tungsten.fcl.databinding.ItemMenuCategoryBinding
import com.tungsten.fcl.databinding.ItemMenuSeekbarBinding
import com.tungsten.fcl.databinding.ItemMenuSpinnerBinding
import com.tungsten.fcl.databinding.ItemMenuSwitchBinding
import com.tungsten.fcl.setting.MenuSetting
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fcllibrary.component.view.FCLTextView

/** 右菜单一级分类 */
enum class RightMenuCategory(@param:StringRes val titleRes: Int) {
    FUNCTION(R.string.menu_settings_function),
    GESTURE(R.string.menu_settings_gesture),
    MOUSE(R.string.menu_settings_mouse),
    GAMEPAD(R.string.menu_settings_gamepad),
    GYRO(R.string.menu_settings_gyro),
    DEBUG(R.string.menu_settings_debug)
}

/** 右菜单功能项标签，交互回调按此分发 */
enum class RightMenuTag {
    // 功能
    LOCK_VIEW, HIDE_VIEW, SHOW_FPS, OPEN_MULTIPLAYER, OPEN_QUICK_INPUT, OPEN_SEND_KEY,
    SOFT_KEYBOARD_ADJUST, ITEM_BAR_WIDTH, ITEM_BAR_HEIGHT, WINDOW_SCALE, CURSOR_OFFSET,

    // 手势
    DISABLE_GESTURE, GESTURE_MODE, DISABLE_LEFT_TOUCH,

    // 鼠标
    MOUSE_MODE, MOUSE_SENSITIVITY, MOUSE_CURSOR_SENSITIVITY, MOUSE_SIZE,
    MOUSE_OFFSET_X, MOUSE_OFFSET_Y, PHYSICAL_MOUSE,

    // 手柄
    DISABLE_GAMEPAD_MAPPING, GAMEPAD_RESET_MAPPER, GAMEPAD_BUTTON_BINDING, GAMEPAD_DEADZONE,

    // 陀螺仪
    GYRO, GYRO_INVERT, GYRO_SENSITIVITY,

    // 调试
    SHOW_MEMORY, PERFORMANCE_MODE, SHOW_LOG, AUTO_SHOW_LOG, FORCE_EXIT
}

/**
 * 右菜单两级 RecyclerView 适配器。
 * 一级为分类列表，点击分类后切换到该分类下的功能项列表（二级），回调分发与设置页适配器一致。
 */
class RightMenuAdapter(
    private val context: Context,
    private val gameMenu: GameMenu,
    private val listener: Listener
) : RecyclerView.Adapter<RightMenuAdapter.Holder>() {

    /** 交互回调，由 GameMenu 实现并分派到原菜单逻辑 */
    interface Listener {
        fun onCategoryClick(category: RightMenuCategory)
        fun onButtonClick(tag: RightMenuTag)
        fun onSwitchToggle(tag: RightMenuTag, checked: Boolean)
        fun onSwitchLongClick(tag: RightMenuTag)
        fun onSpinnerSelect(tag: RightMenuTag, position: Int)
        fun onSeekBarChange(tag: RightMenuTag, progress: Int)
    }

    private val menuSetting: MenuSetting get() = gameMenu.menuSetting
    private val screenWidth = getScreenWidth()
    private val screenHeight = getScreenHeight()
    private val multiplayerEnabled =
        context.getSharedPreferences("third_party", Context.MODE_PRIVATE)
            .getBoolean("terracotta", false)

    private val typeCategory = 0
    private val typeSwitch = 1
    private val typeButton = 2
    private val typeSpinner = 3
    private val typeSeekBar = 4

    /** 当前所在二级分类，null 表示处于一级分类列表 */
    private var currentCategory: RightMenuCategory? = null
    private var rows: List<Row> = emptyList()

    fun showCategory(category: RightMenuCategory) {
        currentCategory = category
        rebuild()
    }

    fun showCategories() {
        currentCategory = null
        rebuild()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun rebuild() {
        rows = buildRows()
        notifyDataSetChanged()
    }

    private fun buildRows(): List<Row> = when (currentCategory) {
        null -> listOf(
            Row.ButtonRow(
                R.string.menu_settings_force_exit,
                listOf(R.string.menu_settings_force_exit_button to RightMenuTag.FORCE_EXIT)
            )
        ) + RightMenuCategory.entries.map { Row.CategoryRow(it) }

        RightMenuCategory.FUNCTION -> listOfNotNull(
            Row.SwitchRow(
                R.string.menu_settings_lock_view,
                { menuSetting.isLockMenuView },
                RightMenuTag.LOCK_VIEW
            ),
            Row.SwitchRow(
                R.string.menu_settings_hide_view,
                { menuSetting.isHideMenuView },
                RightMenuTag.HIDE_VIEW
            ),
            Row.SwitchRow(
                R.string.menu_settings_show_fps,
                { menuSetting.isShowFps },
                RightMenuTag.SHOW_FPS,
                longClick = true
            ),
            if (!gameMenu.isSimulated && multiplayerEnabled) {
                Row.ButtonRow(
                    R.string.terracotta_menu,
                    listOf(R.string.terracotta_menu_open to RightMenuTag.OPEN_MULTIPLAYER)
                )
            } else {
                null
            },
            Row.ButtonRow(
                R.string.menu_settings_quick_input,
                listOf(R.string.menu_settings_quick_input_button to RightMenuTag.OPEN_QUICK_INPUT)
            ),
            Row.ButtonRow(
                R.string.menu_settings_send_key,
                listOf(R.string.menu_settings_send_key_button to RightMenuTag.OPEN_SEND_KEY)
            ),
            Row.SwitchRow(
                R.string.menu_settings_soft_keyboard_adjust,
                { menuSetting.isDisableSoftKeyAdjust },
                RightMenuTag.SOFT_KEYBOARD_ADJUST
            ),
            Row.SeekBarRow(
                R.string.menu_settings_item_bar_scale_width, 100, 0,
                { menuSetting.itemBarWidth * 100 / screenWidth }, RightMenuTag.ITEM_BAR_WIDTH, "%"
            ),
            Row.SeekBarRow(
                R.string.menu_settings_item_bar_scale_height,
                100,
                0,
                { menuSetting.itemBarHeight * 100 / screenHeight },
                RightMenuTag.ITEM_BAR_HEIGHT,
                "%"
            ),
            Row.SeekBarRow(
                R.string.settings_game_dimension, 300, 1,
                { (menuSetting.windowScale * 100).toInt() }, RightMenuTag.WINDOW_SCALE, "%"
            )
        )

        RightMenuCategory.GESTURE -> listOf(
            Row.SwitchRow(
                R.string.menu_settings_disable_gesture,
                { menuSetting.isDisableGesture },
                RightMenuTag.DISABLE_GESTURE
            ),
            Row.SpinnerRow(
                R.string.menu_settings_gesture_mode,
                listOf(
                    context.getString(R.string.menu_settings_gesture_mode_build),
                    context.getString(R.string.menu_settings_gesture_mode_fight)
                ),
                menuSetting.gestureMode.id, RightMenuTag.GESTURE_MODE
            ),
            Row.SwitchRow(
                R.string.menu_settings_disable_left_touch,
                { menuSetting.isDisableLeftTouch },
                RightMenuTag.DISABLE_LEFT_TOUCH
            )
        )

        RightMenuCategory.MOUSE -> listOf(
            Row.SpinnerRow(
                R.string.menu_settings_mouse_mode,
                listOf(
                    context.getString(R.string.menu_settings_mouse_mode_click),
                    context.getString(R.string.menu_settings_mouse_mode_slide)
                ),
                menuSetting.mouseMoveMode.id, RightMenuTag.MOUSE_MODE
            ),
            Row.SeekBarRow(
                R.string.settings_game_cursor_offset, 150, -150,
                { menuSetting.cursorOffset.toInt() }, RightMenuTag.CURSOR_OFFSET
            ),
            Row.SeekBarRow(
                R.string.menu_settings_mouse_sensitivity,
                1000,
                1,
                { (menuSetting.mouseSensitivity * 100).toInt() },
                RightMenuTag.MOUSE_SENSITIVITY,
                "%"
            ),
            Row.SeekBarRow(
                R.string.menu_settings_mouse_cursor_sensitivity,
                1000,
                1,
                { (menuSetting.mouseSensitivityCursor * 100).toInt() },
                RightMenuTag.MOUSE_CURSOR_SENSITIVITY,
                "%"
            ),
            Row.SeekBarRow(
                R.string.menu_settings_mouse_size, 30, 0,
                { menuSetting.mouseSize }, RightMenuTag.MOUSE_SIZE, "dp"
            ),
            Row.SeekBarRow(
                R.string.menu_settings_mouse_offset_x, 30, -30,
                { menuSetting.mouseOffsetX }, RightMenuTag.MOUSE_OFFSET_X, "dp"
            ),
            Row.SeekBarRow(
                R.string.menu_settings_mouse_offset_y, 30, -30,
                { menuSetting.mouseOffsetY }, RightMenuTag.MOUSE_OFFSET_Y, "dp"
            ),
            Row.SwitchRow(
                R.string.menu_settings_physical_mouse_mode,
                { menuSetting.isPhysicalMouseMode },
                RightMenuTag.PHYSICAL_MOUSE
            )
        )

        RightMenuCategory.GAMEPAD -> listOf(
            Row.SwitchRow(
                R.string.menu_settings_gamepad_disable_mapping,
                { gameMenu.isGamepadDisabled },
                RightMenuTag.DISABLE_GAMEPAD_MAPPING
            ),
            Row.ButtonRow(
                R.string.menu_settings_gamepad_reset_mapper,
                listOf(R.string.menu_settings_gamepad_reset to RightMenuTag.GAMEPAD_RESET_MAPPER)
            ),
            Row.ButtonRow(
                R.string.menu_settings_gamepad_button_binding,
                listOf(R.string.menu_settings_gamepad_open_button to RightMenuTag.GAMEPAD_BUTTON_BINDING)
            ),
            Row.SeekBarRow(
                R.string.menu_settings_gamepad_deadzone, 100, 0,
                { (menuSetting.gamepadDeadzone * 100).toInt() }, RightMenuTag.GAMEPAD_DEADZONE, "%"
            )
        )

        RightMenuCategory.GYRO -> listOf(
            Row.SwitchRow(
                R.string.menu_settings_gyro,
                { menuSetting.isEnableGyroscope },
                RightMenuTag.GYRO
            ),
            Row.SwitchRow(
                R.string.menu_settings_gyro_invert,
                { menuSetting.isInvertGyroscope },
                RightMenuTag.GYRO_INVERT
            ),
            Row.SeekBarRow(
                R.string.menu_settings_gyro_sensitivity, 1000, 0,
                { menuSetting.gyroscopeSensitivity }, RightMenuTag.GYRO_SENSITIVITY
            )
        )

        RightMenuCategory.DEBUG -> listOf(
            Row.SwitchRow(
                R.string.menu_settings_show_memory,
                { menuSetting.isShowMemory },
                RightMenuTag.SHOW_MEMORY,
                longClick = true
            ),
            Row.SwitchRow(
                R.string.menu_settings_performance_mode,
                { menuSetting.isPerformanceMode },
                RightMenuTag.PERFORMANCE_MODE
            ),
            Row.SwitchRow(
                R.string.menu_settings_show_log,
                { menuSetting.isShowLog },
                RightMenuTag.SHOW_LOG
            ),
            Row.SwitchRow(
                R.string.menu_settings_show_log_auto,
                { menuSetting.isAutoShowLog },
                RightMenuTag.AUTO_SHOW_LOG
            )
        )
    }

    private sealed class Row {
        data class CategoryRow(val category: RightMenuCategory) : Row()

        data class SwitchRow(
            val labelRes: Int,
            val value: () -> Boolean,
            val tag: RightMenuTag,
            val longClick: Boolean = false
        ) : Row()

        data class ButtonRow(
            val labelRes: Int,
            val buttons: List<Pair<Int, RightMenuTag>>
        ) : Row()

        data class SpinnerRow(
            val labelRes: Int,
            val data: List<String>,
            val selection: Int,
            val tag: RightMenuTag
        ) : Row()

        data class SeekBarRow(
            val labelRes: Int,
            val max: Int,
            val min: Int,
            val value: () -> Int,
            val tag: RightMenuTag,
            val suffix: String? = null
        ) : Row()
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 滑块进度监听器，行复用时先移除旧监听避免累积 */
        var progressListener: InvalidationListener? = null
    }

    override fun getItemCount(): Int = rows.size

    override fun getItemViewType(position: Int): Int = when (rows[position]) {
        is Row.CategoryRow -> typeCategory
        is Row.SwitchRow -> typeSwitch
        is Row.ButtonRow -> typeButton
        is Row.SpinnerRow -> typeSpinner
        is Row.SeekBarRow -> typeSeekBar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            typeCategory -> ItemMenuCategoryBinding.inflate(inflater, parent, false).root
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
        holder.itemView.background = null
        holder.itemView.findViewById<FCLTextView>(R.id.description)?.visibility = View.GONE
        when (row) {
            is Row.CategoryRow -> bindCategory(holder, row)
            is Row.SwitchRow -> bindSwitch(holder, row)
            is Row.ButtonRow -> bindButton(holder, row)
            is Row.SpinnerRow -> bindSpinner(holder, row)
            is Row.SeekBarRow -> bindSeekBar(holder, row)
        }
    }

    private fun bindCategory(holder: Holder, row: Row.CategoryRow) {
        val binding = ItemMenuCategoryBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.category.titleRes)
        holder.itemView.setOnClickListener { listener.onCategoryClick(row.category) }
    }

    private fun bindSwitch(holder: Holder, row: Row.SwitchRow) {
        val binding = ItemMenuSwitchBinding.bind(holder.itemView)
        binding.switchView.text = context.getString(row.labelRes)
        binding.switchView.setOnCheckedChangeListener(null)
        binding.switchView.isChecked = row.value()
        binding.switchView.setOnCheckedChangeListener { _, checked ->
            listener.onSwitchToggle(row.tag, checked)
        }
        binding.switchView.setOnLongClickListener(
            if (row.longClick) {
                {
                    listener.onSwitchLongClick(row.tag)
                    true
                }
            } else {
                null
            }
        )
    }

    private fun bindButton(holder: Holder, row: Row.ButtonRow) {
        val binding = ItemMenuButtonBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.labelRes)
        listOf(
            Triple(binding.button1, 0, RightMenuTag.FORCE_EXIT),
            Triple(binding.button2, 1, RightMenuTag.FORCE_EXIT),
            Triple(binding.button3, 2, RightMenuTag.FORCE_EXIT)
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
        val adapter = ArrayAdapter(context, R.layout.item_spinner_small, row.data)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown_small)
        // 先清监听再设 adapter/selection，避免复用行时触发旧监听
        binding.spinner.onItemSelectedListener = null
        binding.spinner.adapter = adapter
        binding.spinner.setSelection(row.selection)
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                listener.onSpinnerSelect(row.tag, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun bindSeekBar(holder: Holder, row: Row.SeekBarRow) {
        val binding = ItemMenuSeekbarBinding.bind(holder.itemView)
        // 先移除旧监听，避免下方 setMax/setMin 等属性设置触发旧回调
        holder.progressListener?.let { binding.seekBar.progressProperty().removeListener(it) }
        holder.progressListener = null
        binding.label.text = context.getString(row.labelRes)
        binding.seekBar.max = row.max
        binding.seekBar.min = row.min
        row.suffix?.let { binding.seekBar.setSuffix(it) }
        binding.seekBar.addProgressListener()
        binding.seekBar.progressProperty().set(row.value())
        val progressListener = InvalidationListener {
            listener.onSeekBarChange(row.tag, binding.seekBar.progressProperty().get())
        }
        binding.seekBar.progressProperty().addListener(progressListener)
        holder.progressListener = progressListener
    }
}