package com.tungsten.fcl.ui.manage

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mio.manager.RendererManager.getRenderer
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemVersionSettingEditBinding
import com.tungsten.fcl.databinding.ItemVersionSettingIconBinding
import com.tungsten.fcl.databinding.ItemVersionSettingMemoryBinding
import com.tungsten.fcl.databinding.ItemVersionSettingSwitchBinding
import com.tungsten.fcl.databinding.ItemVersionSettingValueBinding
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.setting.Controllers
import com.tungsten.fcl.setting.VersionSetting
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.util.platform.MemoryUtils
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLTextView

/** 设置行操作标签：按钮点击 / 特殊开关 / 编辑行长按按此分发 */
enum class VersionSettingTag {
    // 特殊开关行
    SPECIAL,
    VULKAN,
    FORCE_RESOLUTION,
    // 按钮行
    EDIT_ICON,
    DELETE_ICON,
    EDIT_JAVA,
    INSTALL_JAVA,
    EDIT_CONTROLLER,
    INSTALL_CONTROLLER,
    EDIT_BACKEND,
    EDIT_RENDERER,
    INSTALL_RENDERER,
    EDIT_DRIVER,
    INSTALL_DRIVER,
    EDIT_ENV,
    // 编辑行
    JVM_ARGS,
    MC_ARGS
}

/**
 * 版本设置页 RecyclerView 适配器。
 * 各行按类型复用，扁平列表、行间以分隔线区分；数据读写全部手动，
 * 不再使用 fakefx 绑定（值变化由页面调用 [refreshRow] 局部刷新）。
 */
class VersionSettingAdapter(
    private val context: Context,
    private val globalSetting: Boolean,
    private val listener: Listener
) : RecyclerView.Adapter<VersionSettingAdapter.Holder>() {

    /** 页面回调：按钮点击 / 特殊开关 / 编辑行长按 */
    interface Listener {
        fun onButtonClick(tag: VersionSettingTag)
        fun onSpecialSwitch(tag: VersionSettingTag, checked: Boolean)
        fun onSwitchLongClick(tag: VersionSettingTag)
        fun onLongPressEdit(tag: VersionSettingTag)
    }

    private val TYPE_SWITCH = 0
    private val TYPE_VALUE = 1
    private val TYPE_EDIT = 2
    private val TYPE_MEMORY = 3
    private val TYPE_ICON = 4

    private lateinit var versionSetting: VersionSetting
    private var modpack = false
    private var enableSpecific = false
    private var usedMemory = 0
    private var iconDrawable: Drawable? = null
    private var rows: List<Row> = emptyList()

    /** 版本/状态变化时全量重建行列表 */
    fun update(versionSetting: VersionSetting, modpack: Boolean, enableSpecific: Boolean, usedMemory: Int) {
        this.versionSetting = versionSetting
        this.modpack = modpack
        this.enableSpecific = enableSpecific
        this.usedMemory = usedMemory
        rebuild()
    }

    fun rebuild() {
        rows = buildRows()
        notifyDataSetChanged()
    }

    /** 对话框修改属性后局部刷新对应行（行未显示时下次 bind 自然读到新值） */
    fun refreshRow(tag: VersionSettingTag) {
        val index = rows.indexOfFirst { it.rowTag == tag }
        if (index >= 0) notifyItemChanged(index)
    }

    /** 版本图标异步加载完成后更新图标行 */
    fun setIcon(drawable: Drawable?) {
        iconDrawable = drawable
        val index = rows.indexOfFirst { it is Row.IconRow }
        if (index >= 0) notifyItemChanged(index)
    }

    private fun buildRows(): List<Row> {
        val result = mutableListOf<Row>()
        if (!globalSetting) {
            result += Row.SwitchRow(
                labelRes = R.string.settings_type_special_enable,
                value = { enableSpecific },
                onToggle = { listener.onSpecialSwitch(VersionSettingTag.SPECIAL, it) },
                disabled = modpack,
                rowTag = VersionSettingTag.SPECIAL,
                descriptionRes = R.string.settings_type_special_enable_desc,
            )
            result += Row.IconRow(R.string.settings_icon_desc)
        }
        if (globalSetting || enableSpecific) {
            result += Row.ValueRow(
                R.string.settings_game_java_version,
                { javaText() },
                VersionSettingTag.EDIT_JAVA,
                VersionSettingTag.INSTALL_JAVA,
                descriptionRes = R.string.settings_game_java_version_desc,
            )
            result += Row.SwitchRow(
                R.string.settings_game_working_directory,
                { versionSetting.isIsolateGameDir },
                { versionSetting.isIsolateGameDir = it },
                disabled = modpack,
                descriptionRes = R.string.settings_game_working_directory_desc,
            )
            result += Row.MemoryRow(R.string.settings_memory_desc)
            result += Row.EditRow(
                R.string.settings_advanced_server_ip,
                { versionSetting.serverIp },
                { versionSetting.serverIp = it },
                hintRes = R.string.settings_advanced_server_ip_prompt,
                descriptionRes = R.string.settings_advanced_server_ip_desc,
            )
            result += Row.ValueRow(
                R.string.settings_fcl_controller,
                { controllerName() },
                VersionSettingTag.EDIT_CONTROLLER,
                VersionSettingTag.INSTALL_CONTROLLER,
                descriptionRes = R.string.settings_fcl_controller_desc,
            )
            result += Row.ValueRow(
                R.string.settings_fcl_graphics_backend,
                { versionSetting.graphicsBackend },
                VersionSettingTag.EDIT_BACKEND,
                null,
                descriptionRes = R.string.settings_fcl_graphics_backend_desc,
            )
            result += Row.ValueRow(
                R.string.settings_fcl_renderer,
                { rendererText() },
                VersionSettingTag.EDIT_RENDERER,
                VersionSettingTag.INSTALL_RENDERER,
                descriptionRes = R.string.settings_fcl_renderer_desc,
            )
            result += Row.SwitchRow(
                R.string.settings_fcl_pojav_bigcore,
                { versionSetting.isPojavBigCore },
                { versionSetting.isPojavBigCore = it },
                descriptionRes = R.string.settings_fcl_pojav_bigcore_desc,
            )
            result += Row.SwitchRow(
                R.string.settings_fcl_vulkan_driver_system,
                { versionSetting.isVKDriverSystem },
                { listener.onSpecialSwitch(VersionSettingTag.VULKAN, it) },
                rowTag = VersionSettingTag.VULKAN,
                descriptionRes = R.string.settings_fcl_vulkan_driver_system_desc,
            )
            if (!versionSetting.isVKDriverSystem) {
                result += Row.ValueRow(
                    R.string.settings_fcl_driver,
                    { versionSetting.driver },
                    VersionSettingTag.EDIT_DRIVER,
                    VersionSettingTag.INSTALL_DRIVER,
                    descriptionRes = R.string.settings_fcl_driver_desc,
                )
            }
            result += Row.SwitchRow(
                R.string.settings_advanced_dont_check_game_completeness,
                { versionSetting.isNotCheckGame },
                { versionSetting.isNotCheckGame = it },
                descriptionRes = R.string.settings_advanced_dont_check_game_completeness_desc,
            )
            result += Row.SwitchRow(
                R.string.settings_advanced_dont_check_jvm_validity,
                { versionSetting.isNotCheckJVM },
                { versionSetting.isNotCheckJVM = it },
                descriptionRes = R.string.settings_advanced_dont_check_jvm_validity_desc,
            )
            result += Row.SwitchRow(
                R.string.settings_advanced_dont_check_mod,
                { versionSetting.isNotCheckMod },
                { versionSetting.isNotCheckMod = it },
                descriptionRes = R.string.settings_advanced_dont_check_mod_desc,
            )
            result += Row.SwitchRow(
                R.string.settings_advanced_debug_log,
                { versionSetting.isDebugLog },
                { versionSetting.isDebugLog = it },
                descriptionRes = R.string.settings_advanced_debug_log_desc,
            )
            result += Row.EditRow(
                R.string.settings_advanced_minecraft_arguments,
                { versionSetting.minecraftArgs },
                { versionSetting.minecraftArgs = it },
                tag = VersionSettingTag.MC_ARGS,
                hintRes = R.string.settings_advanced_minecraft_arguments_prompt,
                longPressEdit = true,
                descriptionRes = R.string.settings_advanced_minecraft_arguments_desc,
            )
            result += Row.EditRow(
                R.string.settings_advanced_jvm_args,
                { versionSetting.javaArgs },
                { versionSetting.javaArgs = it },
                tag = VersionSettingTag.JVM_ARGS,
                longPressEdit = true,
                descriptionRes = R.string.settings_advanced_jvm_args_desc,
            )
            result += Row.ValueRow(
                R.string.settings_advanced_env,
                { "" },
                VersionSettingTag.EDIT_ENV,
                null,
                descriptionRes = R.string.settings_advanced_env_desc,
            )
            result += Row.EditRow(
                R.string.settings_advanced_custom_uuid,
                { versionSetting.uuid },
                { versionSetting.uuid = it },
                descriptionRes = R.string.settings_advanced_custom_uuid_desc,
            )
            result += Row.SwitchRow(
                R.string.settings_advanced_force_resolution,
                { versionSetting.isForceResolution },
                { listener.onSpecialSwitch(VersionSettingTag.FORCE_RESOLUTION, it) },
                longClick = true,
                rowTag = VersionSettingTag.FORCE_RESOLUTION,
                descriptionRes = R.string.settings_advanced_force_resolution_desc,
            )
        }
        return result
    }

    private sealed class Row {
        open val rowTag: VersionSettingTag? = null
        /** 行下方的作用描述文案资源，0 表示无描述 */
        open val descriptionRes: Int = 0

        data class SwitchRow(
            val labelRes: Int,
            val value: () -> Boolean,
            val onToggle: (Boolean) -> Unit,
            val disabled: Boolean = false,
            val longClick: Boolean = false,
            override val rowTag: VersionSettingTag? = null,
            override val descriptionRes: Int = 0
        ) : Row()

        data class ValueRow(
            val labelRes: Int,
            val value: () -> String,
            val editTag: VersionSettingTag?,
            val installTag: VersionSettingTag?,
            override val descriptionRes: Int = 0
        ) : Row() {
            override val rowTag: VersionSettingTag? get() = editTag
        }

        data class EditRow(
            val labelRes: Int,
            val value: () -> String,
            val write: (String) -> Unit,
            val tag: VersionSettingTag? = null,
            val hintRes: Int = 0,
            val longPressEdit: Boolean = false,
            override val descriptionRes: Int = 0
        ) : Row() {
            override val rowTag: VersionSettingTag? get() = tag
        }

        data class MemoryRow(override val descriptionRes: Int = 0) : Row()

        data class IconRow(override val descriptionRes: Int = 0) : Row()
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 编辑行复用时先移除旧 TextWatcher，避免监听累积
        var textWatcher: TextWatcher? = null
    }

    override fun getItemCount(): Int = rows.size

    override fun getItemViewType(position: Int): Int = when (rows[position]) {
        is Row.SwitchRow -> TYPE_SWITCH
        is Row.ValueRow -> TYPE_VALUE
        is Row.EditRow -> TYPE_EDIT
        is Row.MemoryRow -> TYPE_MEMORY
        is Row.IconRow -> TYPE_ICON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            TYPE_SWITCH -> ItemVersionSettingSwitchBinding.inflate(inflater, parent, false).root
            TYPE_VALUE -> ItemVersionSettingValueBinding.inflate(inflater, parent, false).root
            TYPE_EDIT -> ItemVersionSettingEditBinding.inflate(inflater, parent, false).root
            TYPE_MEMORY -> ItemVersionSettingMemoryBinding.inflate(inflater, parent, false).root
            else -> ItemVersionSettingIconBinding.inflate(inflater, parent, false).root
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 行背景为圆角形状（item 布局中定义），颜色 tint 取主题浅色；间隔空隙透出页面背景
        ThemeEngine.getInstance().unregisterEvent(holder.itemView)
        ThemeEngine.getInstance().registerEvent(holder.itemView) {
            holder.itemView.backgroundTintList = ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)
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
            is Row.ValueRow -> bindValue(holder, row)
            is Row.EditRow -> bindEdit(holder, row)
            is Row.MemoryRow -> bindMemory(holder, row)
            is Row.IconRow -> bindIcon(holder, row)
        }
    }

    private fun bindSwitch(holder: Holder, row: Row.SwitchRow) {
        val binding = ItemVersionSettingSwitchBinding.bind(holder.itemView)
        binding.switchView.text = context.getString(row.labelRes)
        binding.switchView.isEnabled = !row.disabled
        binding.switchView.setOnCheckedChangeListener(null)
        binding.switchView.isChecked = row.value()
        binding.switchView.setOnCheckedChangeListener { _, checked -> row.onToggle(checked) }
        binding.switchView.setOnLongClickListener(
            if (row.longClick) View.OnLongClickListener {
                row.rowTag?.let { listener.onSwitchLongClick(it) }
                true
            } else null
        )
    }

    private fun bindValue(holder: Holder, row: Row.ValueRow) {
        val binding = ItemVersionSettingValueBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.labelRes)
        binding.value.text = row.value()
        binding.buttonEdit.visibility = if (row.editTag != null) View.VISIBLE else View.GONE
        binding.buttonInstall.visibility = if (row.installTag != null) View.VISIBLE else View.GONE
        binding.buttonEdit.setOnClickListener { row.editTag?.let { listener.onButtonClick(it) } }
        binding.buttonInstall.setOnClickListener { row.installTag?.let { listener.onButtonClick(it) } }
    }

    private fun bindEdit(holder: Holder, row: Row.EditRow) {
        val binding = ItemVersionSettingEditBinding.bind(holder.itemView)
        binding.label.text = context.getString(row.labelRes)
        binding.editText.hint = if (row.hintRes != 0) context.getString(row.hintRes) else null
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
        binding.editText.setOnLongClickListener(
            if (row.longPressEdit) View.OnLongClickListener {
                row.tag?.let { listener.onLongPressEdit(it) }
                true
            } else null
        )
    }

    private fun bindMemory(holder: Holder, row: Row.MemoryRow) {
        val binding = ItemVersionSettingMemoryBinding.bind(holder.itemView)
        val totalMemory = MemoryUtils.getTotalDeviceMemory(context)
        val freeMemory = MemoryUtils.getFreeDeviceMemory(context)
        binding.barMemory.max = totalMemory
        binding.memoryBar.max = totalMemory

        // 勾选框/滑条变化时重算进度条与文本（原 fakefx 绑定表达式的等价逻辑）
        fun updateDisplay() {
            val auto = binding.checkAutoAllocate.isChecked
            val maxMemory = binding.barMemory.progress
            val allocated = (FCLGameRepository.getAllocatedMemory(
                maxMemory * 1024L * 1024L,
                freeMemory * 1024L * 1024L,
                auto
            ) / 1024.0 / 1024).toInt()
            binding.memoryState.text = context.getString(
                if (auto) R.string.settings_memory_lower_bound else R.string.settings_memory
            )
            binding.memoryBar.progress = usedMemory
            binding.memoryBar.secondaryProgress = usedMemory + if (auto) allocated else maxMemory
            binding.memoryInfoText.text = AndroidUtils.getLocalizedText(
                context,
                "settings_memory_used_per_total",
                usedMemory / 1024.0,
                totalMemory / 1024.0
            )
            binding.memoryAllocateText.text = AndroidUtils.getLocalizedText(
                context,
                if (maxMemory * 1024L * 1024L > freeMemory * 1024L * 1024L) {
                    if (auto) "settings_memory_allocate_auto_exceeded" else "settings_memory_allocate_manual_exceeded"
                } else {
                    if (auto) "settings_memory_allocate_auto" else "settings_memory_allocate_manual"
                },
                maxMemory / 1024.0,
                allocated / 1024.0,
                freeMemory / 1024.0
            )
        }

        binding.checkAutoAllocate.setOnCheckedChangeListener(null)
        binding.checkAutoAllocate.isChecked = versionSetting.isAutoMemory
        binding.barMemory.setOnSeekBarChangeListener(null)
        binding.barMemory.progress = versionSetting.maxMemory
        binding.checkAutoAllocate.setOnCheckedChangeListener { _, checked ->
            versionSetting.isAutoMemory = checked
            updateDisplay()
        }
        binding.barMemory.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                versionSetting.maxMemory = progress
                updateDisplay()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        updateDisplay()
    }

    private fun bindIcon(holder: Holder, row: Row.IconRow) {
        val binding = ItemVersionSettingIconBinding.bind(holder.itemView)
        binding.icon.setImageDrawable(iconDrawable ?: ContextCompat.getDrawable(context, R.drawable.img_grass))
        binding.buttonEdit.setOnClickListener { listener.onButtonClick(VersionSettingTag.EDIT_ICON) }
        binding.buttonDelete.setOnClickListener { listener.onButtonClick(VersionSettingTag.DELETE_ICON) }
    }

    private fun javaText(): String =
        if (versionSetting.java == "Auto") context.getString(R.string.settings_game_java_version_auto) else versionSetting.java

    private fun controllerName(): String = Controllers.findControllerById(versionSetting.controller).name

    private fun rendererText(): String = getRenderer(versionSetting.renderer).des
}
