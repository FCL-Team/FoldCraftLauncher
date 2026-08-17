package com.tungsten.fcl.ui.setting

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemLauncherSettingButtonBinding
import com.tungsten.fcl.databinding.ItemLauncherSettingSeekbarBinding
import com.tungsten.fcl.databinding.ItemLauncherSettingSourceBinding
import com.tungsten.fcl.databinding.ItemLauncherSettingSpinnerBinding
import com.tungsten.fcl.databinding.ItemLauncherSettingThreadsBinding
import com.tungsten.fcl.databinding.ItemVersionSettingEditBinding
import com.tungsten.fcl.databinding.ItemVersionSettingSwitchBinding
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.util.LocaleUtils

/** 启动器设置行操作标签：按钮 / 开关 / Spinner / SeekBar / 勾选框按此分发 */
enum class LauncherSettingTag {
    // 按钮行
    CHECK_UPDATE,
    CLEAR_CACHE,
    EXPORT_LOG,
    REQUEST_AUDIO,
    THEME_COLOR_RESET,
    THEME_COLOR_FETCH,
    THEME_COLOR_SET,
    THEME_COLOR2_RESET,
    THEME_COLOR2_FETCH,
    THEME_COLOR2_SET,
    THEME_COLOR2_DARK_RESET,
    THEME_COLOR2_DARK_FETCH,
    THEME_COLOR2_DARK_SET,
    BACKGROUND_LT_RESET,
    BACKGROUND_LT_SET,
    BACKGROUND_DK_RESET,
    BACKGROUND_DK_SET,
    BACKGROUND_LIVE_RESET,
    BACKGROUND_LIVE_SET,
    CURSOR_RESET,
    CURSOR_SET,
    MENU_ICON_RESET,
    MENU_ICON_SET,

    // 开关行
    SWITCH_AUTO_EXIT,
    SWITCH_IGNORE_NOTCH,
    SWITCH_CLOSE_SKIN_MODEL,
    SWITCH_DISABLE_FULLSCREEN_INPUT,
    SWITCH_ALLOW_SCREENSHOTS,

    // Spinner 行
    SPINNER_LANGUAGE,
    SPINNER_THEME_MODE,
    SPINNER_SOURCE_AUTO,
    SPINNER_SOURCE,

    // SeekBar 行
    SEEKBAR_VIDEO_VOLUME,
    SEEKBAR_ANIMATION_SPEED,
    SEEKBAR_VIBRATION,
    SEEKBAR_THREADS,

    // 勾选行
    CHECK_AUTO_SOURCE,
    CHECK_AUTO_THREADS
}

/**
 * 启动器设置页 RecyclerView 适配器。
 * 行类型复用、扁平列表、数据读写手动回写，与版本设置页保持一致。
 */
class LauncherSettingAdapter(
    private val context: Context,
    private val listener: Listener
) : RecyclerView.Adapter<LauncherSettingAdapter.Holder>() {

    /** 页面回调：按钮 / 开关 / Spinner / SeekBar / 自动勾选 */
    interface Listener {
        fun onButtonClick(tag: LauncherSettingTag)
        fun onSwitchToggle(tag: LauncherSettingTag, checked: Boolean)
        fun onSpinnerSelect(tag: LauncherSettingTag, position: Int)
        fun onSeekBarChange(tag: LauncherSettingTag, progress: Int)
        fun onCheckToggle(tag: LauncherSettingTag, checked: Boolean)
    }

    private val TYPE_SWITCH = 0
    private val TYPE_BUTTON = 1
    private val TYPE_SPINNER = 2
    private val TYPE_SEEKBAR = 3
    private val TYPE_EDIT = 4
    private val TYPE_SOURCE = 5
    private val TYPE_THREADS = 6

    private val prefs = context.getSharedPreferences("launcher", MODE_PRIVATE)
    private var rows: List<Row> = emptyList()

    fun rebuild() {
        rows = buildRows()
        notifyDataSetChanged()
    }

    private fun buildRows(): List<Row> {
        val config = ConfigHolder.config()
        val languageList = listOf(
            context.getString(R.string.settings_launcher_language_system),
            context.getString(R.string.settings_launcher_language_english),
            context.getString(R.string.settings_launcher_language_simplified_chinese),
            context.getString(R.string.settings_launcher_language_russian),
            context.getString(R.string.settings_launcher_language_brazilian_portuguese),
            context.getString(R.string.settings_launcher_language_persian),
            context.getString(R.string.settings_launcher_language_ukrainian),
            context.getString(R.string.settings_launcher_language_german),
            context.getString(R.string.settings_launcher_language_traditional_chinese_hk)
        )
        val themeModeList = listOf(
            context.getString(R.string.settings_launcher_theme_mode_follow),
            context.getString(R.string.settings_launcher_theme_mode_light),
            context.getString(R.string.settings_launcher_theme_mode_dark)
        )
        return listOf(
            Row.SpinnerRow(
                R.string.settings_launcher_language, languageList,
                LocaleUtils.getLanguage(context), LauncherSettingTag.SPINNER_LANGUAGE,
                R.string.settings_launcher_language_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_upgrade,
                listOf(R.string.settings_launcher_upgrade_check to LauncherSettingTag.CHECK_UPDATE),
                R.string.settings_launcher_upgrade_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_cache,
                listOf(R.string.settings_launcher_clear_cache to LauncherSettingTag.CLEAR_CACHE),
                R.string.settings_launcher_cache_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_debug,
                listOf(R.string.settings_launcher_launcher_log_export to LauncherSettingTag.EXPORT_LOG),
                R.string.settings_launcher_debug_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_request_recording_permission,
                listOf(R.string.settings_launcher_request to LauncherSettingTag.REQUEST_AUDIO),
                R.string.settings_launcher_request_recording_permission_desc
            ),
            Row.SwitchRow(
                R.string.settings_launcher_exit_after_launching,
                { prefs.getBoolean("autoExitLauncher", false) },
                LauncherSettingTag.SWITCH_AUTO_EXIT,
                R.string.settings_launcher_exit_after_launching_desc
            ),
            Row.SpinnerRow(
                R.string.settings_launcher_theme_mode, themeModeList,
                prefs.getInt("themeMode", 0), LauncherSettingTag.SPINNER_THEME_MODE,
                R.string.settings_launcher_theme_mode_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_theme,
                listOf(
                    R.string.button_reset to LauncherSettingTag.THEME_COLOR_RESET,
                    R.string.settings_launcher_theme_fetch_background to LauncherSettingTag.THEME_COLOR_FETCH,
                    R.string.button_set to LauncherSettingTag.THEME_COLOR_SET
                ),
                R.string.settings_launcher_theme_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_theme2,
                listOf(
                    R.string.button_reset to LauncherSettingTag.THEME_COLOR2_RESET,
                    R.string.settings_launcher_theme_fetch_background to LauncherSettingTag.THEME_COLOR2_FETCH,
                    R.string.button_set to LauncherSettingTag.THEME_COLOR2_SET
                ),
                R.string.settings_launcher_theme2_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_theme2_dark,
                listOf(
                    R.string.button_reset to LauncherSettingTag.THEME_COLOR2_DARK_RESET,
                    R.string.settings_launcher_theme_fetch_background to LauncherSettingTag.THEME_COLOR2_DARK_FETCH,
                    R.string.button_set to LauncherSettingTag.THEME_COLOR2_DARK_SET
                ),
                R.string.settings_launcher_theme2_dark_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_background_lt,
                listOf(
                    R.string.button_reset to LauncherSettingTag.BACKGROUND_LT_RESET,
                    R.string.button_set to LauncherSettingTag.BACKGROUND_LT_SET
                ),
                R.string.settings_launcher_background_lt_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_background_dk,
                listOf(
                    R.string.button_reset to LauncherSettingTag.BACKGROUND_DK_RESET,
                    R.string.button_set to LauncherSettingTag.BACKGROUND_DK_SET
                ),
                R.string.settings_launcher_background_dk_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_background_video,
                listOf(
                    R.string.button_reset to LauncherSettingTag.BACKGROUND_LIVE_RESET,
                    R.string.button_set to LauncherSettingTag.BACKGROUND_LIVE_SET
                ),
                R.string.settings_launcher_background_video_desc
            ),
            Row.SeekBarRow(
                R.string.settings_launcher_background_video_volume, 100, 0,
                { prefs.getInt("videoBackgroundVolume", 100) },
                LauncherSettingTag.SEEKBAR_VIDEO_VOLUME, "%",
                R.string.settings_launcher_background_video_volume_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_cursor,
                listOf(
                    R.string.button_reset to LauncherSettingTag.CURSOR_RESET,
                    R.string.button_set to LauncherSettingTag.CURSOR_SET
                ),
                R.string.settings_launcher_cursor_desc
            ),
            Row.ButtonRow(
                R.string.settings_launcher_menu_icon,
                listOf(
                    R.string.button_reset to LauncherSettingTag.MENU_ICON_RESET,
                    R.string.button_set to LauncherSettingTag.MENU_ICON_SET
                ),
                R.string.settings_launcher_menu_icon_desc
            ),
            Row.SwitchRow(
                R.string.settings_launcher_ignore_notch,
                { ThemeEngine.getInstance().getTheme().fullscreen },
                LauncherSettingTag.SWITCH_IGNORE_NOTCH,
                R.string.settings_launcher_ignore_notch_desc
            ),
            Row.SwitchRow(
                R.string.settings_launcher_close_skin_view,
                { ThemeEngine.getInstance().getTheme().closeSkinModel },
                LauncherSettingTag.SWITCH_CLOSE_SKIN_MODEL,
                R.string.settings_launcher_close_skin_view_desc
            ),
            Row.SeekBarRow(
                R.string.settings_launcher_animation_speed, 20, 1,
                { ThemeEngine.getInstance().getTheme().animationSpeed },
                LauncherSettingTag.SEEKBAR_ANIMATION_SPEED, "00",
                R.string.settings_launcher_animation_speed_desc
            ),
            Row.SeekBarRow(
                R.string.settings_launcher_vibrate_duration, 500, 20,
                { prefs.getInt("vibrationDuration", 100) },
                LauncherSettingTag.SEEKBAR_VIBRATION, "MS",
                R.string.settings_launcher_vibrate_duration_desc
            ),
            Row.SwitchRow(
                R.string.settings_disable_fullscreen_input,
                { prefs.getBoolean("disableFullscreenInput", true) },
                LauncherSettingTag.SWITCH_DISABLE_FULLSCREEN_INPUT,
                R.string.settings_disable_fullscreen_input_desc
            ),
            Row.EditRow(
                R.string.settings_launcher_custom_launcher_name,
                {
                    prefs.getString("custom_launcher_name", context.getString(R.string.app_name))
                        ?: ""
                },
                { text -> prefs.edit { putString("custom_launcher_name", text) } },
                $$"Fold Craft Launcher/${launcher_version}",
                R.string.settings_launcher_custom_launcher_name_desc
            ),
            Row.SwitchRow(
                R.string.settings_launcher_allow_screenshot,
                { prefs.getBoolean("allowScreenshots", false) },
                LauncherSettingTag.SWITCH_ALLOW_SCREENSHOTS,
                R.string.settings_launcher_allow_screenshot_desc
            ),
            Row.SourceRow(
                { config.autoChooseDownloadTypeProperty().get() },
                ArrayList(DownloadProviders.providersById.keys),
                { getSourcePosition(config.versionListSourceProperty().get()) },
                ArrayList(DownloadProviders.rawProviders.keys),
                { getSourcePosition(config.downloadTypeProperty().get()) },
                R.string.settings_launcher_download_source_desc
            ),
            Row.ThreadsRow(
                { config.autoDownloadThreads },
                { config.downloadThreads },
                R.string.settings_launcher_download_threads_desc
            )
        )
    }

    private fun getSourcePosition(source: String): Int = when (source) {
        "official", "mojang" -> 0
        "mirror" -> 2
        else -> 1
    }

    private sealed class Row {
        open val rowTag: LauncherSettingTag? = null

        /** 行下方的作用描述文案资源，0 表示无描述 */
        open val descriptionRes: Int = 0

        data class SwitchRow(
            val labelRes: Int,
            val value: () -> Boolean,
            val tag: LauncherSettingTag,
            override val descriptionRes: Int = 0
        ) : Row() {
            override val rowTag: LauncherSettingTag get() = tag
        }

        data class ButtonRow(
            val labelRes: Int,
            val buttons: List<Pair<Int, LauncherSettingTag>>,
            override val descriptionRes: Int = 0
        ) : Row()

        data class SpinnerRow(
            val labelRes: Int,
            val data: List<String>,
            val selection: Int,
            val tag: LauncherSettingTag,
            override val descriptionRes: Int = 0
        ) : Row() {
            override val rowTag: LauncherSettingTag get() = tag
        }

        data class SeekBarRow(
            val labelRes: Int,
            val max: Int,
            val min: Int,
            val value: () -> Int,
            val tag: LauncherSettingTag,
            val suffix: String? = null,
            override val descriptionRes: Int = 0
        ) : Row() {
            override val rowTag: LauncherSettingTag get() = tag
        }

        data class EditRow(
            val labelRes: Int,
            val value: () -> String,
            val write: (String) -> Unit,
            val hint: String? = null,
            override val descriptionRes: Int = 0
        ) : Row()

        data class SourceRow(
            val autoChecked: () -> Boolean,
            val autoData: List<String>,
            val autoSelection: () -> Int,
            val manualData: List<String>,
            val manualSelection: () -> Int,
            override val descriptionRes: Int = 0
        ) : Row()

        data class ThreadsRow(
            val autoChecked: () -> Boolean,
            val threads: () -> Int,
            override val descriptionRes: Int = 0
        ) : Row()
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 编辑行复用时先移除旧 TextWatcher，避免监听累积
        var textWatcher: TextWatcher? = null
    }

    override fun getItemCount(): Int = rows.size

    override fun getItemViewType(position: Int): Int = when (rows[position]) {
        is Row.SwitchRow -> TYPE_SWITCH
        is Row.ButtonRow -> TYPE_BUTTON
        is Row.SpinnerRow -> TYPE_SPINNER
        is Row.SeekBarRow -> TYPE_SEEKBAR
        is Row.EditRow -> TYPE_EDIT
        is Row.SourceRow -> TYPE_SOURCE
        is Row.ThreadsRow -> TYPE_THREADS
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            TYPE_SWITCH -> ItemVersionSettingSwitchBinding.inflate(inflater, parent, false).root
            TYPE_BUTTON -> ItemLauncherSettingButtonBinding.inflate(inflater, parent, false).root
            TYPE_SPINNER -> ItemLauncherSettingSpinnerBinding.inflate(inflater, parent, false).root
            TYPE_SEEKBAR -> ItemLauncherSettingSeekbarBinding.inflate(inflater, parent, false).root
            TYPE_EDIT -> ItemVersionSettingEditBinding.inflate(inflater, parent, false).root
            TYPE_SOURCE -> ItemLauncherSettingSourceBinding.inflate(inflater, parent, false).root
            else -> ItemLauncherSettingThreadsBinding.inflate(inflater, parent, false).root
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 行背景为圆角形状（item 布局中定义），颜色 tint 取主题浅色；间隔空隙透出页面背景
        ThemeEngine.getInstance().unregisterEvent(holder.itemView)
        ThemeEngine.getInstance().registerEvent(holder.itemView) {
            holder.itemView.backgroundTintList =
                ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)
        }
        val row = rows[position]
        // 行下方的作用描述
        holder.itemView.findViewById<FCLTextView>(R.id.description)?.let { description ->
            if (row.descriptionRes != 0) {
                description.text = context.getString(row.descriptionRes)
                description.visibility = View.VISIBLE
            } else {
                description.visibility = View.GONE
            }
        }
        when (row) {
            is Row.SwitchRow -> bindSwitch(holder, row)
            is Row.ButtonRow -> bindButton(holder, row)
            is Row.SpinnerRow -> bindSpinner(holder, row)
            is Row.SeekBarRow -> bindSeekBar(holder, row)
            is Row.EditRow -> bindEdit(holder, row)
            is Row.SourceRow -> bindSource(holder, row)
            is Row.ThreadsRow -> bindThreads(holder, row)
        }
    }

    private fun bindSwitch(holder: Holder, row: Row.SwitchRow) {
        val binding = ItemVersionSettingSwitchBinding.bind(holder.itemView)
        binding.switchView.text = context.getString(row.labelRes)
        binding.switchView.setOnCheckedChangeListener(null)
        binding.switchView.isChecked = row.value()
        binding.switchView.setOnCheckedChangeListener { _, checked ->
            listener.onSwitchToggle(
                row.tag,
                checked
            )
        }
    }

    private fun bindButton(holder: Holder, row: Row.ButtonRow) {
        val binding = ItemLauncherSettingButtonBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.labelRes)
        val buttons = listOf(
            Triple(binding.button1, 0, LauncherSettingTag.CHECK_UPDATE),
            Triple(binding.button2, 1, LauncherSettingTag.CHECK_UPDATE),
            Triple(binding.button3, 2, LauncherSettingTag.CHECK_UPDATE)
        )
        buttons.forEach { (button, index, _) ->
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
        val binding = ItemLauncherSettingSpinnerBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.labelRes)
        val adapter = ArrayAdapter(context, R.layout.item_spinner_auto_tint, row.data)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        // 先清 listener 再设 adapter/selection，避免复用行时触发旧监听
        binding.spinner.onItemSelectedListener = null
        binding.spinner.adapter = adapter
        binding.spinner.setSelection(row.selection)
        binding.spinner.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    listener.onSpinnerSelect(row.tag, position)
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
    }

    private fun bindSeekBar(holder: Holder, row: Row.SeekBarRow) {
        val binding = ItemLauncherSettingSeekbarBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.labelRes)
        binding.seekBar.max = row.max
        binding.seekBar.min = row.min
        row.suffix?.let { binding.seekBar.setSuffix(it) }
        binding.seekBar.setOnSeekBarChangeListener(null)
        binding.seekBar.progress = row.value()
        binding.seekBar.setOnSeekBarChangeListener(object :
            android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: android.widget.SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                listener.onSeekBarChange(row.tag, progress)
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar) {}

            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar) {}
        })
    }

    private fun bindEdit(holder: Holder, row: Row.EditRow) {
        val binding = ItemVersionSettingEditBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.labelRes)
        binding.editText.hint = row.hint
        holder.textWatcher?.let { binding.editText.removeTextChangedListener(it) }
        val target = row.value()
        if (binding.editText.text?.toString() != target) {
            binding.editText.setText(target)
        }
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                row.write(s.toString())
            }
        }
        binding.editText.addTextChangedListener(watcher)
        holder.textWatcher = watcher
    }

    private fun bindSource(holder: Holder, row: Row.SourceRow) {
        val binding = ItemLauncherSettingSourceBinding.bind(holder.itemView)
        val auto = row.autoChecked()
        binding.checkAutoSource.setOnCheckedChangeListener(null)
        binding.checkAutoSource.isChecked = auto
        // 自动/手动源交替显示
        binding.sourceAuto.visibility = if (auto) View.VISIBLE else View.GONE
        binding.source.visibility = if (auto) View.GONE else View.VISIBLE
        binding.checkAutoSource.setOnCheckedChangeListener { _, checked ->
            listener.onCheckToggle(LauncherSettingTag.CHECK_AUTO_SOURCE, checked)
            binding.sourceAuto.visibility = if (checked) View.VISIBLE else View.GONE
            binding.source.visibility = if (checked) View.GONE else View.VISIBLE
        }
        bindSourceSpinner(
            binding.sourceAuto,
            row.autoData,
            row.autoSelection(),
            LauncherSettingTag.SPINNER_SOURCE_AUTO
        )
        bindSourceSpinner(
            binding.source,
            row.manualData,
            row.manualSelection(),
            LauncherSettingTag.SPINNER_SOURCE
        )
    }

    private fun bindSourceSpinner(
        spinner: com.tungsten.fcllibrary.component.view.FCLSpinner<*>,
        data: List<String>,
        selection: Int,
        tag: LauncherSettingTag
    ) {
        val adapter = ArrayAdapter(context, R.layout.item_spinner_auto_tint, data)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        // 先清 listener 再设 adapter/selection，避免复用行时触发旧监听
        spinner.onItemSelectedListener = null
        spinner.adapter = adapter
        spinner.setSelection(selection)
        spinner.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    listener.onSpinnerSelect(tag, position)
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
    }

    private fun bindThreads(holder: Holder, row: Row.ThreadsRow) {
        val binding = ItemLauncherSettingThreadsBinding.bind(holder.itemView)
        binding.checkAutoThreads.setOnCheckedChangeListener(null)
        binding.checkAutoThreads.isChecked = row.autoChecked()
        binding.checkAutoThreads.setOnCheckedChangeListener { _, checked ->
            listener.onCheckToggle(LauncherSettingTag.CHECK_AUTO_THREADS, checked)
        }
        binding.threads.setOnSeekBarChangeListener(null)
        binding.threads.progress = row.threads()
        binding.threads.setOnSeekBarChangeListener(object :
            android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: android.widget.SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                listener.onSeekBarChange(LauncherSettingTag.SEEKBAR_THREADS, progress)
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar) {}

            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar) {}
        })
    }
}
