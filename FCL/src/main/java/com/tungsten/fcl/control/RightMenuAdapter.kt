package com.tungsten.fcl.control

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.mio.ui.selectedCardBackground
import com.mio.util.getScreenHeight
import com.mio.util.getScreenWidth
import com.tungsten.fcl.R
import com.tungsten.fcl.control.data.ControlViewGroup
import com.tungsten.fcl.databinding.ItemMenuButtonBinding
import com.tungsten.fcl.databinding.ItemMenuControlGroupBinding
import com.tungsten.fcl.databinding.ItemMenuSeekbarBinding
import com.tungsten.fcl.databinding.ItemMenuSpinnerBinding
import com.tungsten.fcl.databinding.ItemMenuSwitchBinding
import com.tungsten.fcl.game.sdl.SdlSettings
import com.tungsten.fcl.setting.MenuSetting
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLSpinner
import com.tungsten.fcllibrary.component.view.FCLTextView

/** 右菜单分类，与右菜单顶部标签栏一一对应 */
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
    DISABLE_GESTURE, GESTURE_MODE, DISABLE_LEFT_TOUCH, SLIDE_ACCELERATION, DISTANCE_ACCELERATION,
    SIMULTANEOUS_VIEW_CONTROL,

    // 鼠标
    MOUSE_MODE, MOUSE_SENSITIVITY, MOUSE_CURSOR_SENSITIVITY, MOUSE_SIZE,
    MOUSE_OFFSET_X, MOUSE_OFFSET_Y, PHYSICAL_MOUSE,
    CAPTURE_POINTER_KEY, CAPTURE_POINTER_MODIFIER, IME_TOGGLE_KEY, IME_TOGGLE_MODIFIER,

    // 手柄
    GAMEPAD_CONTROL, GAMEPAD_RESET_MAPPER, GAMEPAD_BUTTON_BINDING, GAMEPAD_DEADZONE,
    GAMEPAD_INPUT_MODE,

    // 陀螺仪
    GYRO, GYRO_INVERT_X, GYRO_INVERT_Y, GYRO_SENSITIVITY_X, GYRO_SENSITIVITY_Y,

    // 调试
    SHOW_MEMORY, PERFORMANCE_MODE, REQUEST_MAX_REFRESH_RATE, FORCE_RESOLUTION, FORCE_RESOLUTION_SIZE,
    SHOW_LOG, AUTO_SHOW_LOG, FORCE_EXIT,

    // SDL
    SDL_AUTO_SHOW_IME,

    // 编辑模式控件组面板
    EDIT_GROUP, REMOVE_GROUP
}

/**
 * 右菜单 RecyclerView 适配器。
 * 分类由菜单顶部标签栏切换，适配器只负责渲染当前分类的功能项列表；
 * 编辑模式下整体替换为控件组管理面板，回调分发与设置页适配器一致。
 */
class RightMenuAdapter(
    private val context: Context,
    private val gameMenu: GameMenu,
    private val listener: Listener
) : RecyclerView.Adapter<RightMenuAdapter.Holder>() {

    /** 交互回调，由 GameMenu 实现并分派到原菜单逻辑 */
    interface Listener {
        fun onButtonClick(tag: RightMenuTag)
        fun onSwitchToggle(tag: RightMenuTag, checked: Boolean)
        fun onSwitchLongClick(tag: RightMenuTag)
        fun onSpinnerSelect(tag: RightMenuTag, position: Int)
        fun onSeekBarChange(tag: RightMenuTag, progress: Int)

        /** 快捷键设置行：点击后进入按键监听，下一个按下的物理键即被绑定为该快捷键 */
        fun onKeyBindClick(tag: RightMenuTag)

        /** 编辑模式控件组面板：点击组名切换当前编辑组 */
        fun onEditGroupSelect(group: ControlViewGroup)

        /** 编辑模式控件组面板：切换组在编辑画布的显示/隐藏（可多组同时显示） */
        fun onEditGroupToggle(group: ControlViewGroup, visible: Boolean)

        /** 编辑模式控件组面板：复制控件组（含全部控件，控件 id 重新生成） */
        fun onEditGroupCopy(group: ControlViewGroup)

        /** 编辑模式控件组面板：新建控件组 */
        fun onEditGroupAdd()

        /** 编辑模式控件组面板：编辑组属性（名称/初始可见性） */
        fun onEditGroupEdit(group: ControlViewGroup)

        /** 编辑模式控件组面板：删除组（含确认） */
        fun onEditGroupRemove(group: ControlViewGroup)
    }

    private val menuSetting: MenuSetting get() = gameMenu.menuSetting
    private val screenWidth = getScreenWidth()
    private val screenHeight = getScreenHeight()
    private val density = context.resources.displayMetrics.density
    private val multiplayerEnabled =
        context.getSharedPreferences("third_party", Context.MODE_PRIVATE)
            .getBoolean("terracotta", false)

    private val typeSwitch = 1
    private val typeButton = 2
    private val typeSpinner = 3
    private val typeSeekBar = 4
    private val typeControlGroup = 5
    private val typeKeyBind = 6

    /** 快捷键修饰键选项，下标与 MenuSetting 的 HOTKEY_MOD_* 一致 */
    private val modifierOptions = listOf(
        context.getString(R.string.key_modifier_none),
        context.getString(R.string.key_modifier_shift),
        context.getString(R.string.key_modifier_ctrl),
        context.getString(R.string.key_modifier_alt)
    )

    /** 当前显示的分类，由顶部标签栏切换 */
    private var currentCategory: RightMenuCategory = RightMenuCategory.FUNCTION
    private var rows: List<Row> = emptyList()

    fun showCategory(category: RightMenuCategory) {
        currentCategory = category
        rebuild()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun rebuild() {
        rows = buildRows()
        notifyDataSetChanged()
    }

    private fun buildRows(): List<Row> {
        // 编辑模式：右菜单整体替换为控件组管理面板（显示/隐藏、层级排序、切换编辑组）
        if (gameMenu.isEditMode) {
            return buildEditRows()
        }
        return when (currentCategory) {
        RightMenuCategory.FUNCTION -> listOfNotNull(
            Row.ButtonRow(
                R.string.menu_settings_force_exit,
                listOf(R.string.menu_settings_force_exit_button to RightMenuTag.FORCE_EXIT)
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
            Row.SwitchRow(
                R.string.menu_settings_sdl_auto_show_ime,
                { SdlSettings.sdlAutoShowIme.value },
                RightMenuTag.SDL_AUTO_SHOW_IME
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
            ),
            Row.SwitchRow(
                R.string.menu_settings_slide_acceleration,
                { menuSetting.isSlideAcceleration },
                RightMenuTag.SLIDE_ACCELERATION
            ),
            Row.SwitchRow(
                R.string.menu_settings_distance_acceleration,
                { menuSetting.isDistanceAcceleration },
                RightMenuTag.DISTANCE_ACCELERATION
            ),
            Row.SwitchRow(
                R.string.menu_settings_simultaneous_view_control,
                { menuSetting.isSimultaneousViewControl },
                RightMenuTag.SIMULTANEOUS_VIEW_CONTROL
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
            ),
            Row.KeyBindRow(
                R.string.menu_settings_capture_pointer_key,
                { menuSetting.capturePointerKey },
                RightMenuTag.CAPTURE_POINTER_KEY
            ),
            Row.SpinnerRow(
                R.string.menu_settings_key_modifier,
                modifierOptions,
                menuSetting.capturePointerModifier,
                RightMenuTag.CAPTURE_POINTER_MODIFIER
            ),
            Row.KeyBindRow(
                R.string.menu_settings_ime_toggle_key,
                { menuSetting.imeToggleKey },
                RightMenuTag.IME_TOGGLE_KEY
            ),
            Row.SpinnerRow(
                R.string.menu_settings_key_modifier,
                modifierOptions,
                menuSetting.imeToggleModifier,
                RightMenuTag.IME_TOGGLE_MODIFIER
            )
        )

        RightMenuCategory.GAMEPAD -> listOf(
            Row.SwitchRow(
                R.string.menu_settings_gamepad_control,
                { gameMenu.isGamepadControl },
                RightMenuTag.GAMEPAD_CONTROL
            ),
            Row.SpinnerRow(
                R.string.menu_settings_gamepad_input_mode,
                listOf(
                    context.getString(R.string.menu_settings_gamepad_input_mode_mapped),
                    context.getString(R.string.menu_settings_gamepad_input_mode_sdl_direct)
                ),
                SdlSettings.gamepadInputMode.value.ordinal,
                RightMenuTag.GAMEPAD_INPUT_MODE,
                enabled = gameMenu.isGamepadControl
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
                R.string.menu_settings_gyro_invert_x,
                { menuSetting.isInvertGyroscopeX },
                RightMenuTag.GYRO_INVERT_X
            ),
            Row.SwitchRow(
                R.string.menu_settings_gyro_invert_y,
                { menuSetting.isInvertGyroscopeY },
                RightMenuTag.GYRO_INVERT_Y
            ),
            Row.SeekBarRow(
                R.string.menu_settings_gyro_sensitivity_x, 1000, 0,
                { menuSetting.gyroscopeSensitivityX * 10 }, RightMenuTag.GYRO_SENSITIVITY_X, "%"
            ),
            Row.SeekBarRow(
                R.string.menu_settings_gyro_sensitivity_y, 1000, 0,
                { menuSetting.gyroscopeSensitivityY * 10 }, RightMenuTag.GYRO_SENSITIVITY_Y, "%"
            )
        )

        RightMenuCategory.DEBUG -> listOf(
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
                R.string.menu_settings_max_refresh_rate,
                { menuSetting.isRequestMaxRefreshRate },
                RightMenuTag.REQUEST_MAX_REFRESH_RATE
            ),
            Row.SwitchRow(
                R.string.settings_advanced_force_resolution,
                { menuSetting.isForceResolution },
                RightMenuTag.FORCE_RESOLUTION
            ),
            Row.ButtonRow(
                R.string.menu_settings_force_resolution_size,
                listOf(R.string.menu_settings_force_resolution_edit to RightMenuTag.FORCE_RESOLUTION_SIZE)
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
    }

    /** 编辑模式控件组面板：各组行（点击切组、开关显隐、编辑属性、删除），组行长按拖动排序 */
    private fun buildEditRows(): List<Row> {
        val groups = gameMenu.controller?.viewGroups() ?: emptyList()
        return groups.map { group ->
            Row.ControlGroupRow(group)
        }
    }

    private sealed class Row {
        /** 控件组行：组名（点击切换编辑组）+ 属性编辑 + 删除 + 显示开关；长按拖动调整渲染层级 */
        data class ControlGroupRow(
            val group: ControlViewGroup
        ) : Row()

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
            val tag: RightMenuTag,
            val enabled: Boolean = true
        ) : Row()

        data class SeekBarRow(
            val labelRes: Int,
            val max: Int,
            val min: Int,
            val value: () -> Int,
            val tag: RightMenuTag,
            val suffix: String? = null
        ) : Row()

        /** 快捷键设置行：label + 当前键名按钮，点击进入按键监听（下一个物理键绑定） */
        data class KeyBindRow(
            val labelRes: Int,
            val keycode: () -> Int,
            val tag: RightMenuTag
        ) : Row()
    }

    companion object {
        /** FCLKeycodes → 显示名，用于快捷键设置行；未收录的键显示编号 */
        @JvmStatic
        fun keycodeName(code: Int): String {
            return when (code) {
                0 -> "None"
                1 -> "Esc"
                in 2..11 -> (code - 1).toString()
                12 -> "Minus"
                13 -> "Equal"
                14 -> "Backspace"
                15 -> "Tab"
                in 16..25 -> "${'Q' + (code - 16)}"
                26 -> "LeftBrace"
                27 -> "RightBrace"
                28 -> "Enter"
                29 -> "LCtrl"
                in 30..38 -> "${'A' + (code - 30)}"
                39 -> "Semicolon"
                40 -> "Apostrophe"
                41 -> "Grave"
                42 -> "LShift"
                43 -> "Backslash"
                in 44..50 -> "${'Z' + (code - 44)}"
                51 -> "Comma"
                52 -> "Dot"
                53 -> "Slash"
                54 -> "RShift"
                55 -> "KPAsterisk"
                56 -> "LAlt"
                57 -> "Space"
                58 -> "CapsLock"
                in 59..68 -> "F${code - 58}"
                69 -> "NumLock"
                70 -> "ScrollLock"
                87 -> "F11"
                88 -> "F12"
                96 -> "KPEnter"
                97 -> "RCtrl"
                98 -> "KPSlash"
                99 -> "SysRq"
                100 -> "RAlt"
                102 -> "Home"
                103 -> "Up"
                104 -> "PageUp"
                105 -> "Left"
                106 -> "Right"
                107 -> "End"
                108 -> "Down"
                109 -> "PageDown"
                110 -> "Insert"
                111 -> "Delete"
                else -> "Key#$code"
            }
        }
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
        is Row.ControlGroupRow -> typeControlGroup
        is Row.KeyBindRow -> typeKeyBind
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            typeSwitch -> ItemMenuSwitchBinding.inflate(inflater, parent, false).root
            typeButton -> ItemMenuButtonBinding.inflate(inflater, parent, false).root
            typeSpinner -> ItemMenuSpinnerBinding.inflate(inflater, parent, false).root
            typeControlGroup -> ItemMenuControlGroupBinding.inflate(inflater, parent, false).root
            typeKeyBind -> ItemMenuButtonBinding.inflate(inflater, parent, false).root
            else -> ItemMenuSeekbarBinding.inflate(inflater, parent, false).root
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val row = rows[position]
        // 条目卡片背景：以对话框背景为基准微亮，暗色下仅微亮避免刺眼（与 AnimationDialog 行样式一致）
        holder.itemView.background = menuCardBackground(density)
        holder.itemView.findViewById<FCLTextView>(R.id.description)?.visibility = View.GONE
        when (row) {
            is Row.SwitchRow -> bindSwitch(holder, row)
            is Row.ButtonRow -> bindButton(holder, row)
            is Row.SpinnerRow -> bindSpinner(holder, row)
            is Row.SeekBarRow -> bindSeekBar(holder, row)
            is Row.ControlGroupRow -> bindControlGroup(holder, row)
            is Row.KeyBindRow -> bindKeyBind(holder, row)
        }
    }

    /** 快捷键设置行：按钮显示当前键名，点击进入按键监听 */
    private fun bindKeyBind(holder: Holder, row: Row.KeyBindRow) {
        val binding = ItemMenuButtonBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.labelRes)
        binding.button1.text = keycodeName(row.keycode())
        binding.button1.visibility = View.VISIBLE
        binding.button1.setOnClickListener { listener.onKeyBindClick(row.tag) }
        binding.button2.visibility = View.GONE
        binding.button2.setOnClickListener(null)
        binding.button3.visibility = View.GONE
        binding.button3.setOnClickListener(null)
    }

    /** 控件组行：当前编辑组主题色高亮，组名点击切换，开关控制编辑画布显隐，按钮编辑/删除属性 */
    private fun bindControlGroup(holder: Holder, row: Row.ControlGroupRow) {
        val binding = ItemMenuControlGroupBinding.bind(holder.itemView)
        val isCurrent = row.group == gameMenu.viewGroup
        binding.label.text = row.group.name
        if (isCurrent) {
            holder.itemView.background = selectedCardBackground(ThemeEngine.getTheme().getColor(), density)
        }
        binding.root.setOnClickListener { listener.onEditGroupSelect(row.group) }
        binding.copy.setOnClickListener { listener.onEditGroupCopy(row.group) }
        binding.edit.setOnClickListener { listener.onEditGroupEdit(row.group) }
        binding.delete.setOnClickListener { listener.onEditGroupRemove(row.group) }
        binding.switchView.setOnCheckedChangeListener(null)
        binding.switchView.isChecked = !gameMenu.isEditorGroupHidden(row.group)
        // 当前编辑组始终显示，不允许隐藏
        binding.switchView.isEnabled = !isCurrent
        binding.switchView.setOnCheckedChangeListener { _, checked ->
            listener.onEditGroupToggle(row.group, checked)
        }
    }

    /** 该位置是否为控件组行（编辑模式列表全部为组行） */
    fun isControlGroupPosition(position: Int): Boolean =
        gameMenu.isEditMode && position in rows.indices

    /** 控件组行位置转组列表索引 */
    fun groupIndexOf(position: Int): Int = position

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
        // ViewBinding 对布局中的泛型控件生成 raw 类型，条目实际为 String
        @Suppress("UNCHECKED_CAST")
        val spinner = binding.spinner as FCLSpinner<String>
        spinner.setItems(row.data)
        spinner.setSelection(row.selection)
        spinner.isEnabled = row.enabled
        spinner.setOnItemSelectedListener { position, _ ->
            listener.onSpinnerSelect(row.tag, position)
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