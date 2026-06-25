package com.tungsten.fcl.ui.manage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.core.net.toUri
import com.mio.manager.RendererManager.getRenderer
import com.mio.ui.dialog.DriverSelectDialog
import com.mio.ui.dialog.JavaManageDialog
import com.mio.ui.dialog.RendererSelectDialog
import com.mio.util.showErrorDialog
import com.mio.util.showItemSelectionDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.activity.MainActivity.Companion.getInstance
import com.tungsten.fcl.control.SelectControllerDialog
import com.tungsten.fcl.databinding.PageVersionSettingBinding
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.setting.Controllers
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles.getSelectedProfile
import com.tungsten.fcl.setting.VersionSetting
import com.tungsten.fcl.ui.controller.ControllerPageManager
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.FXUtils
import com.tungsten.fcl.util.WeakListenerHolder
import com.tungsten.fclauncher.plugins.DriverPlugin.driverList
import com.tungsten.fclauncher.plugins.DriverPlugin.selected
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.event.Event
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.binding.Bindings
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty
import com.tungsten.fclcore.fakefx.beans.property.StringProperty
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Lang
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fclcore.util.platform.MemoryUtils
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FullEditDialog
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLEditText
import com.tungsten.fcllibrary.component.view.FCLProgressBar
import com.tungsten.fcllibrary.component.view.FCLSwitch
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.component.view.FCLUILayout
import java.io.File
import java.io.IOException
import java.util.Locale
import java.util.logging.Level

class VersionSettingPage(
    context: Context?,
    id: Int,
    parent: FCLUILayout?,
    resId: Int,
    private val globalSetting: Boolean
) : FCLCommonPage(context, id, parent, resId), VersionLoadable, View.OnClickListener {
    private lateinit var lastVersionSetting: VersionSetting
    private lateinit var profile: Profile
    private lateinit var listenerHolder: WeakListenerHolder
    private var versionId: String? = null

    private lateinit var binding: PageVersionSettingBinding

    private val specificSettingsListener: InvalidationListener
    private val selectedVersion: StringProperty = SimpleStringProperty()
    private val enableSpecificSettings: BooleanProperty = SimpleBooleanProperty(false)
    private val maxMemory: IntegerProperty = SimpleIntegerProperty()
    private val usedMemory: IntegerProperty = SimpleIntegerProperty(0)
    private val modpack: BooleanProperty = SimpleBooleanProperty()

    init {
        create()
        specificSettingsListener =
            InvalidationListener { enableSpecificSettings.set(!lastVersionSetting.isUsesGlobal) }
    }

    private fun create() {
        binding = PageVersionSettingBinding.bind(contentView)


        val specialSettingSwitch = findViewById<FCLSwitch>(R.id.enable_per_instance_setting)
        specialSettingSwitch.addCheckedChangeListener()
        binding.switchGameDir.disableProperty().bind(modpack)

        binding.buttonEditJava.setOnClickListener(this)
        binding.buttonInstallJava.setOnClickListener(this)
        binding.buttonEditIcon.setOnClickListener(this)
        binding.buttonDeleteIcon.setOnClickListener(this)
        binding.buttonEditController.setOnClickListener(this)
        binding.buttonInstallController.setOnClickListener(this)
        binding.buttonEditGraphicsBackend.setOnClickListener(this)
        binding.buttonEditRenderer.setOnClickListener(this)
        binding.buttonInstallRenderer.setOnClickListener(this)
        binding.buttonEditDriver.setOnClickListener(this)
        binding.buttonInstallDriver.setOnClickListener(this)
        binding.buttonEditEnv.setOnClickListener(this)

        val memoryBar = findViewById<FCLProgressBar>(R.id.memory_bar)

        val memoryStateText = findViewById<FCLTextView>(R.id.memory_state)
        val memoryInfoText = findViewById<FCLTextView>(R.id.memory_info_text)
        val memoryAllocateText = findViewById<FCLTextView>(R.id.memory_allocate_text)

        memoryStateText.stringProperty().bind(Bindings.createStringBinding({
            if (binding.checkAutoAllocate.isChecked) {
                return@createStringBinding context.getString(R.string.settings_memory_lower_bound)
            } else {
                return@createStringBinding context.getString(R.string.settings_memory)
            }
        }, binding.checkAutoAllocate.checkProperty()))

        binding.barMemory.setMax(MemoryUtils.getTotalDeviceMemory(context))
        memoryBar.setMax(MemoryUtils.getTotalDeviceMemory(context))

        binding.barMemory.addProgressListener()
        binding.barMemory.progressProperty().bindBidirectional(maxMemory)

        memoryBar.firstProgressProperty().bind(usedMemory)
        memoryBar.secondProgressProperty().bind(Bindings.createIntegerBinding({
            val allocate = (FCLGameRepository.getAllocatedMemory(
                maxMemory.intValue() * 1024L * 1024L,
                MemoryUtils.getFreeDeviceMemory(context) * 1024L * 1024L,
                binding.checkAutoAllocate.isChecked
            ) / 1024.0 / 1024).toInt()
            usedMemory.intValue() + (if (binding.checkAutoAllocate.isChecked) allocate else maxMemory.intValue())
        }, usedMemory, maxMemory, binding.checkAutoAllocate.checkProperty()))

        memoryInfoText.stringProperty().bind(Bindings.createStringBinding({
            AndroidUtils.getLocalizedText(
                context,
                "settings_memory_used_per_total",
                MemoryUtils.getUsedDeviceMemory(context) / 1024.0,
                MemoryUtils.getTotalDeviceMemory(context) / 1024.0
            )
        }, usedMemory))

        memoryAllocateText.stringProperty().bind(Bindings.createStringBinding({
            val maxMemory = Lang.parseInt(this.maxMemory.get(), 0) * 1024L * 1024L
            AndroidUtils.getLocalizedText(
                context,
                if (maxMemory / 1024.0 / 1024.0 > MemoryUtils.getFreeDeviceMemory(context))
                    (if (binding.checkAutoAllocate.isChecked) "settings_memory_allocate_auto_exceeded" else "settings_memory_allocate_manual_exceeded")
                else
                    (if (binding.checkAutoAllocate.isChecked) "settings_memory_allocate_auto" else "settings_memory_allocate_manual"),
                maxMemory / 1024.0 / 1024.0 / 1024.0,
                FCLGameRepository.getAllocatedMemory(
                    maxMemory,
                    MemoryUtils.getFreeDeviceMemory(context) * 1024L * 1024L,
                    binding.checkAutoAllocate.isChecked
                ) / 1024.0 / 1024.0 / 1024.0,
                MemoryUtils.getFreeDeviceMemory(context) / 1024.0
            )
        }, usedMemory, maxMemory, binding.checkAutoAllocate.checkProperty()))

        binding.specialSettingLayout.visibility = if (globalSetting) View.GONE else View.VISIBLE

        if (!globalSetting) {
            specialSettingSwitch.disableProperty().bind(modpack)
            specialSettingSwitch.checkProperty().bindBidirectional(enableSpecificSettings)
            binding.settingLayout.visibilityProperty().bind(enableSpecificSettings)
        }

        enableSpecificSettings.addListener(ChangeListener { _, _, newValue ->
            if (versionId == null) return@ChangeListener
            // do not call versionSettings.setUsesGlobal(true/false)
            // because versionSettings can be the global one.
            // global versionSettings.usesGlobal is always true.
            if (newValue) profile.repository.specializeVersionSetting(versionId)
            else profile.repository.globalizeVersionSetting(versionId)
            Schedulers.androidUIThread().execute { loadVersion(profile, versionId) }
        })
        binding.switchVulkanDriverSystem.setOnClickListener {
            if (binding.switchVulkanDriverSystem.checkProperty()
                    .get() && AndroidUtils.isAdrenoGPU()
            ) {
                val builder = FCLAlertDialog.Builder(context)
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
                builder.setMessage(context.getString(R.string.message_vulkan_driver_system))
                builder.setNegativeButton(
                    context.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                    null
                )
                builder.create().show()
            }
            binding.driverContainer.visibility =
                if (binding.switchVulkanDriverSystem.checkProperty()
                        .get()
                ) View.GONE else View.VISIBLE
        }
        val listener = OnLongClickListener { view: View? ->
            val dialog = FullEditDialog(
                context
            ) { str: String? -> (view as FCLEditText).setText(str) }
            dialog.getEditText().setText((view as FCLEditText).getText())
            dialog.show()
            true
        }
        binding.editJvmArgs.setOnLongClickListener(listener)
        binding.editMinecraftArgs.setOnLongClickListener(listener)
        binding.switchForceResolution.setOnClickListener { editForceResolution() }
        binding.switchForceResolution.setOnLongClickListener {
            editForceResolution()
            true
        }
    }

    private fun editForceResolution() {
        if (binding.switchForceResolution.checkProperty().get()) {
            val preferences = context.getSharedPreferences("launcher", MODE_PRIVATE)
            val dialog = EditDialog(context) { str ->
                try {
                    val split =
                        str.lowercase(Locale.getDefault()).split("x".toRegex())
                            .dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (split.size == 2) {
                        val w = split[0].toInt()
                        val h = split[1].toInt()
                        preferences.edit {
                            putString("force_resolution", w.toString() + "x" + h)
                        }
                    }
                } catch (e: Exception) {
                    showErrorDialog(context, e.toString())
                }
            }
            dialog.getEditText().setText(preferences.getString("force_resolution", "1920x1080"))
            dialog.onCancelListener = {
                binding.switchForceResolution.isChecked = false
            }
            dialog.show()
        }
    }

    override fun refresh(vararg param: Any?): Task<*>? {
        return null
    }

    override fun onResume() {
        super.onResume()
        usedMemory.set(MemoryUtils.getUsedDeviceMemory(context))
    }

    override fun loadVersion(profile: Profile, versionId: String?) {
        this.profile = profile
        this.versionId = versionId
        this.listenerHolder = WeakListenerHolder()

        if (versionId == null) {
            enableSpecificSettings.set(true)
            listenerHolder.add(
                FXUtils.onWeakChangeAndOperate<String?>(
                    profile.selectedVersionProperty()
                ) { v: String? -> this.selectedVersion.value = v }
            )
        }

        val versionSetting = profile.getVersionSetting(versionId)
        versionSetting.checkController()

        modpack.set(versionId != null && profile.repository.isModpack(versionId))
        usedMemory.set(MemoryUtils.getUsedDeviceMemory(context))

        val listener = InvalidationListener {
            ManagePageManager.instance!!.onRunDirectoryChange(
                profile,
                versionId
            )
        }

        // unbind data fields
        if (::lastVersionSetting.isInitialized) {
            lastVersionSetting.isolateGameDirProperty.removeListener(listener)
            FXUtils.unbind(binding.editJvmArgs, lastVersionSetting.javaArgsProperty)
            FXUtils.unbind(binding.editMinecraftArgs, lastVersionSetting.minecraftArgsProperty)
            FXUtils.unbind(binding.editUuid, lastVersionSetting.uuidProperty)
            FXUtils.unbind(binding.editServer, lastVersionSetting.serverIpProperty)
            FXUtils.unbindBoolean(
                binding.checkAutoAllocate,
                lastVersionSetting.autoMemoryProperty
            )
            FXUtils.unbindBoolean(
                binding.switchGameDir,
                lastVersionSetting.isolateGameDirProperty
            )
            FXUtils.unbindBoolean(
                binding.switchPojavBigCore,
                lastVersionSetting.pojavBigCoreProperty
            )
            FXUtils.unbindBoolean(
                binding.switchNotCheckGame,
                lastVersionSetting.notCheckGameProperty
            )
            FXUtils.unbindBoolean(
                binding.switchNotCheckJava,
                lastVersionSetting.notCheckJVMProperty
            )
            FXUtils.unbindBoolean(binding.switchNotCheckMod, lastVersionSetting.notCheckModProperty)
            FXUtils.unbindBoolean(binding.switchDebugLog, lastVersionSetting.debugLogProperty)
            FXUtils.unbindBoolean(
                binding.switchForceResolution,
                lastVersionSetting.forceResolutionProperty
            )
            FXUtils.unbindBoolean(
                binding.switchControllerInjector,
                lastVersionSetting.beGestureProperty
            )
            FXUtils.unbindBoolean(
                binding.switchVulkanDriverSystem,
                lastVersionSetting.vkDriverSystemProperty
            )
            maxMemory.unbindBidirectional(lastVersionSetting.maxMemoryProperty)

            lastVersionSetting.usesGlobalProperty.removeListener(specificSettingsListener)
        }

        // bind new data fields
        if (id == ManagePageManager.PAGE_ID_MANAGE_SETTING) {
            versionSetting.isolateGameDirProperty.addListener(listener)
        }
        FXUtils.bindString(binding.editJvmArgs, versionSetting.javaArgsProperty)
        FXUtils.bindString(binding.editMinecraftArgs, versionSetting.minecraftArgsProperty)
        FXUtils.bindString(binding.editUuid, versionSetting.uuidProperty)
        FXUtils.bindString(binding.editServer, versionSetting.serverIpProperty)
        FXUtils.bindBoolean(binding.checkAutoAllocate, versionSetting.autoMemoryProperty)
        FXUtils.bindBoolean(binding.switchGameDir, versionSetting.isolateGameDirProperty)
        FXUtils.bindBoolean(binding.switchPojavBigCore, versionSetting.pojavBigCoreProperty)
        FXUtils.bindBoolean(binding.switchNotCheckGame, versionSetting.notCheckGameProperty)
        FXUtils.bindBoolean(binding.switchNotCheckJava, versionSetting.notCheckJVMProperty)
        FXUtils.bindBoolean(binding.switchNotCheckMod, versionSetting.notCheckModProperty)
        FXUtils.bindBoolean(binding.switchDebugLog, versionSetting.debugLogProperty)
        FXUtils.bindBoolean(binding.switchForceResolution, versionSetting.forceResolutionProperty)
        FXUtils.bindBoolean(binding.switchControllerInjector, versionSetting.beGestureProperty)
        FXUtils.bindBoolean(binding.switchVulkanDriverSystem, versionSetting.vkDriverSystemProperty)
        maxMemory.bindBidirectional(versionSetting.maxMemoryProperty)

        binding.checkAutoAllocate.setChecked(versionSetting.isAutoMemory)

        binding.java.text =
            if (versionSetting.java == "Auto") context.getString(R.string.settings_game_java_version_auto) else versionSetting.java
        Controllers.addCallback {
            binding.controller.text = Controllers.findControllerById(
                versionSetting.controller
            ).name
        }
        binding.graphicsBackend.text = versionSetting.graphicsBackend
        val renderer = getRenderer(versionSetting.renderer)
        binding.renderer.setSelected(true)
        binding.renderer.text = renderer.des
        binding.driverContainer.visibility =
            if (binding.switchVulkanDriverSystem.checkProperty().get()) View.GONE else View.VISIBLE
        if (versionSetting.driver != "Turnip") {
            var isSelected = false
            for (driver in driverList) {
                if (driver.driver == versionSetting.driver) {
                    selected = driver
                    versionSetting.driver = driver.driver
                    isSelected = true
                }
            }
            if (!isSelected) {
                versionSetting.driver = "Turnip"
            }
        }
        binding.driver.text = versionSetting.driver

        versionSetting.usesGlobalProperty.addListener(specificSettingsListener)
        if (versionId != null) enableSpecificSettings.set(!versionSetting.isUsesGlobal)

        lastVersionSetting = versionSetting

        loadIcon()
    }

    private fun onExploreIcon() {
        if (versionId == null) return

        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, listOf(".png")) {
            var path = it[0]
            val uri = path.toUri()
            if (AndroidUtils.isDocUri(uri)) {
                path =
                    AndroidUtils.copyFileToDir(activity, uri, File(FCLPath.CACHE_DIR))
            }
            if (path == null) return@launchSingleSelection

            val selectedFile = File(path)
            val iconFile = profile.repository.getVersionIconFile(versionId)
            try {
                FileUtils.copyFile(selectedFile, iconFile)

                profile.repository.onVersionIconChanged.fireEvent(Event(this))
                loadIcon()
            } catch (e: IOException) {
                Logging.LOG.log(
                    Level.SEVERE,
                    "Failed to copy icon file from $selectedFile to $iconFile",
                    e
                )
            }
        }
    }

    private fun onDeleteIcon() {
        if (versionId == null) return

        val iconFile = profile.repository.getVersionIconFile(versionId)
        if (iconFile.exists()) iconFile.delete()
        profile.repository.onVersionIconChanged.fireEvent(Event(this))
        loadIcon()
    }

    private fun loadIcon() {
        if (versionId == null) {
            return
        }
        Schedulers.defaultScheduler().execute {
            val icon = profile.repository.getVersionIconImage(versionId)
            Schedulers.androidUIThread().execute { binding.icon.setImageDrawable(icon) }
        }
    }

    override fun onClick(view: View?) {
        if (view === binding.buttonEditIcon) {
            onExploreIcon()
        }
        if (view === binding.buttonDeleteIcon) {
            onDeleteIcon()
        }
        if (view === binding.buttonEditController) {
            if (Controllers.isInitialized()) {
                val dialog = SelectControllerDialog(
                    context,
                    lastVersionSetting.controller
                ) {
                    lastVersionSetting.controller = it.id
                    binding.controller.text = it.name
                }
                dialog.show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.message_data_is_loading),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (view === binding.buttonInstallController) {
            val uiManager = getInstance().uiManager
            getInstance().binding.controller.setSelected(true)
            uiManager.controllerUI.runAfterInit {
                uiManager.controllerUI.pageManager
                    .switchPage(ControllerPageManager.PAGE_ID_CONTROLLER_REPO)
            }
        }
        if (view === binding.buttonEditJava) {
            JavaManageDialog(context) {
                lastVersionSetting.java = it
                if (it == "Auto") {
                    binding.java.setText(R.string.settings_game_java_version_auto)
                } else {
                    binding.java.text = it
                }
            }.show()
        }
        if (view === binding.buttonInstallJava) {
            AlertDialog.Builder(context)
                .setTitle(R.string.message_install_java)
                .setItems(
                    arrayOf(
                        "Github",
                        context.getString(R.string.settings_download_netdisk)
                    )
                ) { _: DialogInterface?, w: Int ->
                    val url = when (w) {
                        0 -> "https://github.com/FCL-Team/FoldCraftLauncher/releases/tag/java"
                        1 -> "https://pan.quark.cn/s/1a25ca305bda"
                        else -> null
                    }
                    if (url != null) {
                        AndroidUtils.openLink(context, url)
                    }
                }
                .setPositiveButton(R.string.button_cancel, null)
                .create()
                .show()
        }
        if (view === binding.buttonEditGraphicsBackend) {
            showItemSelectionDialog(
                context,
                context.getString(R.string.settings_fcl_graphics_backend),
                mutableListOf("default", "opengl", "vulkan")
            ) { backendName: String ->
                binding.graphicsBackend.text = backendName
                lastVersionSetting.graphicsBackend = backendName
            }
        }
        if (view === binding.buttonEditRenderer) {
            RendererSelectDialog(context, globalSetting) { name: String? ->
                if (globalSetting && getSelectedProfile().versionSetting != null && !getSelectedProfile().versionSetting.isGlobal) {
                    val builder = FCLAlertDialog.Builder(context)
                    builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
                    builder.setMessage(context.getString(R.string.message_warn_renderer_global_setting))
                    builder.setNegativeButton(
                        context.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                        null
                    )
                    builder.create().show()
                }
                binding.renderer.text = name
            }.show()
        }
        if (view === binding.buttonEditDriver) {
            DriverSelectDialog(
                context,
                globalSetting
            ) { binding.driver.text = it }.show()
        }
        if (view === binding.buttonInstallRenderer) {
            AlertDialog.Builder(context)
                .setTitle(R.string.message_install_plugin)
                .setItems(
                    arrayOf(
                        "Github",
                        context.getString(R.string.settings_download_netdisk)
                    )
                ) { _, w ->
                    val url = when (w) {
                        0 -> "https://github.com/ShirosakiMio/FCLRendererPlugin/releases/tag/Renderer"
                        1 -> "https://pan.quark.cn/s/a9f6e9d860d9"
                        else -> null
                    }
                    if (url != null) {
                        AndroidUtils.openLink(context, url)
                    }
                }
                .setPositiveButton(R.string.button_cancel, null)
                .create()
                .show()
        }
        if (view === binding.buttonInstallDriver) {
            AlertDialog.Builder(context)
                .setTitle(R.string.message_install_plugin)
                .setItems(
                    arrayOf(
                        "Github",
                        context.getString(R.string.settings_download_netdisk)
                    )
                ) { _, w ->
                    val url = when (w) {
                        0 -> "https://github.com/FCL-Team/FCLDriverPlugin/releases/tag/Turnip"
                        1 -> "https://pan.quark.cn/s/d87c59695250"
                        else -> null
                    }
                    if (url != null) {
                        AndroidUtils.openLink(context, url)
                    }
                }
                .setPositiveButton(R.string.button_cancel, null)
                .create()
                .show()
        }
        if (view == binding.buttonEditEnv) {
            val preferences = context.getSharedPreferences("launcher", MODE_PRIVATE)
            val dialog = FullEditDialog(context, true) {
                val env = getEnvironmentFromString(it)
                preferences.edit {
                    putString("env", env.joinToString("\n"))
                }
            }
            dialog.binding.editText.setText(preferences.getString("env", ""))
            dialog.show()
        }
    }

    fun getEnvironmentFromString(input: String): List<String> {
        val result = mutableListOf<String>()
        val lines = input.trim().lines()

        lines.forEachIndexed { _, rawLine ->
            val line = rawLine.trim()

            // 跳过空行
            if (line.isEmpty()) {
                return@forEachIndexed
            }

            // 检查是否包含 '='
            val firstEq = line.indexOf('=')
            if (firstEq == -1) {
                return@forEachIndexed
            }

            val name = line.substring(0, firstEq).trim()
            val value = line.substring(firstEq + 1).trim() // 值可以为空

            // 变量名不能为空
            if (name.isEmpty()) {
                return@forEachIndexed
            }

            // 变量名规则：字母、数字、下划线，且不能以数字开头
            val validNameRegex = Regex("^[a-zA-Z_][a-zA-Z0-9_]*$")
            if (!validNameRegex.matches(name)) {
                return@forEachIndexed
            }

            // 长度检查（通常环境变量名不超过 255 字符）
            if (name.length > 255) {
                return@forEachIndexed
            }
            result.add("$name=$value")
        }

        return result
    }
}
