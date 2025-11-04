package com.tungsten.fcl.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.forEach
import androidx.core.view.postDelayed
import androidx.lifecycle.lifecycleScope
import com.mio.manager.RendererManager
import com.mio.ui.dialog.RendererSelectDialog
import com.mio.util.AnimUtil
import com.mio.util.AnimUtil.Companion.interpolator
import com.mio.util.AnimUtil.Companion.startAfter
import com.mio.util.DisplayUtil
import com.mio.util.GuideUtil
import com.mio.util.GuideUtil.Companion.guideTarget
import com.mio.util.ImageUtil
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ActivityMainBinding
import com.tungsten.fcl.game.JarExecutorHelper
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.setting.Controllers
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcl.upgrade.UpdateChecker
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.FXUtils
import com.tungsten.fcl.util.WeakListenerHolder
import com.tungsten.fclauncher.bridge.FCLBridge
import com.tungsten.fclauncher.plugins.DriverPlugin
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer
import com.tungsten.fclcore.auth.yggdrasil.TextureModel
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType
import com.tungsten.fclcore.fakefx.beans.binding.Bindings
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteMod.IMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.fakefx.BindingMapping
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLMenuView
import com.tungsten.fcllibrary.component.view.FCLMenuView.OnSelectListener
import com.tungsten.fcllibrary.util.ConvertUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.logging.Level
import java.util.stream.Stream
import kotlin.system.exitProcess

class MainActivity : FCLActivity(), OnSelectListener, View.OnClickListener {
    companion object {
        private lateinit var instance: WeakReference<MainActivity>

        @JvmStatic
        fun getInstance(): MainActivity {
            return instance.get()!!
        }
    }

    lateinit var binding: ActivityMainBinding
    private var _uiManager: UIManager? = null
    lateinit var uiManager: UIManager
    private lateinit var currentAccount: ObjectProperty<Account?>
    private val holder = WeakListenerHolder()
    private lateinit var profile: Profile
    private lateinit var theme: IntegerProperty
    private lateinit var theme2: IntegerProperty
    private lateinit var theme2Dark: IntegerProperty
    var isVersionLoading = false
    lateinit var permissionResultLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = WeakReference(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ImageUtil.loadInto(
            binding.background,
            ThemeEngine.getInstance().getTheme().getBackground(this)
        )

        RemoteMod.registerEmptyRemoteMod(
            RemoteMod(
                "",
                "",
                getString(R.string.mods_broken_dependency_title),
                getString(R.string.mods_broken_dependency_desc),
                ArrayList(),
                "",
                "",
                object : IMod {
                    @Throws(IOException::class)
                    override fun loadDependencies(modRepository: RemoteModRepository): List<RemoteMod> {
                        throw IOException()
                    }

                    @Throws(IOException::class)
                    override fun loadVersions(modRepository: RemoteModRepository): Stream<RemoteMod.Version> {
                        throw IOException()
                    }

                    override fun loadScreenshots(modRepository: RemoteModRepository): MutableList<RemoteMod.Screenshot> {
                        throw IOException()
                    }
                })
        )

        if (!ConfigHolder.isInit()) {
            try {
                ConfigHolder.init()
                //当强制关闭进程时，不会经过SplashActivity，此时需要重新初始化
                RendererManager.init(this@MainActivity)
            } catch (e: IOException) {
                LOG.log(Level.WARNING, e.message)
            }
        }

        binding.apply {
            initBackground()
            uiLayout.post {
                ThemeEngine.getInstance().registerEvent(leftMenu) {
                    leftMenu.background = GradientDrawable().apply {
                        setColor(ThemeEngine.getInstance().getTheme().color)
                        shape = GradientDrawable.RECTANGLE
                        ConvertUtils.dip2px(this@MainActivity, 8f).toFloat().apply {
                            cornerRadii = floatArrayOf(0f, 0f, this, this, this, this, 0f, 0f)
                        }
                    }
                }

                account.setOnClickListener(this@MainActivity)
                version.setOnClickListener(this@MainActivity)
                start.setOnClickListener(this@MainActivity)
                start.setOnLongClickListener { view ->
                    RendererSelectDialog(this@MainActivity, false) {
                        onClick(view)
                    }.show()
                    true
                }
                jar.setOnClickListener(this@MainActivity)
                jar.setOnLongClickListener {
                    EditDialog(this@MainActivity) {
                        JarExecutorHelper.exec(
                            this@MainActivity,
                            null,
                            JarExecutorHelper.getJava(null),
                            it
                        )
                    }.apply {
                        setTitle(R.string.jar_execute_custom_args)
                        binding.editText.hint = "-jar xxx"
                        binding.editText.setLines(1)
                        binding.editText.maxLines = 1
                    }.show()
                    true
                }

                uiManager = UIManager(this@MainActivity, uiLayout)
                _uiManager = uiManager
                uiManager.registerDefaultBackEvent {
                    if (uiManager.currentUI === uiManager.mainUI) {
                        val i = Intent(Intent.ACTION_MAIN)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        i.addCategory(Intent.CATEGORY_HOME)
                        startActivity(i)
                        exitProcess(0)
                    } else {
                        home.isSelected = true
                    }
                }
                uiManager.init {
                    home.setOnSelectListener(this@MainActivity)
                    manage.setOnSelectListener(this@MainActivity)
                    download.setOnSelectListener(this@MainActivity)
                    controller.setOnSelectListener(this@MainActivity)
                    setting.setOnSelectListener(this@MainActivity)
                    home.setSelected(true)
                    home.setOnLongClickListener {
                        shareLog()
                        true
                    }
                    back.setOnClickListener(this@MainActivity)
                    back.setOnLongClickListener {
                        startActivity(Intent(this@MainActivity, ShellActivity::class.java))
                        true
                    }
                    UpdateChecker.getInstance().checkAuto(this@MainActivity).start()
                    if (!checkNotificationPermission() && getSharedPreferences(
                            "launcher",
                            MODE_PRIVATE
                        ).getBoolean("check_notification_permission", true)
                    ) {
                        getSharedPreferences("launcher", MODE_PRIVATE).edit {
                            putBoolean("check_notification_permission", false)
                        }
                        FCLAlertDialog.Builder(this@MainActivity)
                            .setMessage(getString(R.string.notification_permission))
                            .setPositiveButton {
                                requestNotificationPermission()
                            }
                            .setNegativeButton {}
                            .create()
                            .show()
                    }
                }
                getSharedPreferences("launcher", MODE_PRIVATE).apply {
                    backend.setPosition(if (getBoolean("backend", false)) 1 else 0, true)
                    backend.setOnPositionChangedListener {
                        edit().apply {
                            putBoolean("backend", it == 1)
                            apply()
                        }
                    }
                }
                setupAccountDisplay()
                setupVersionDisplay()
                playAnim()
                uiLayout.postDelayed(1500) {
                    GuideUtil.show(
                        activity = this@MainActivity,
                        GuideUtil.TAG_GUIDE_THEME_2 to setting.guideTarget(title = getString(R.string.guide_theme2)),
                        GuideUtil.TAG_GUIDE_SHARE_LOG to home.guideTarget(title = getString(R.string.guide_share_log))
                    )
                }
            }
        }
        permissionResultLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
            _uiManager?.onBackPressed()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        super.onPause()
        _uiManager?.onPause()
    }

    override fun onResume() {
        super.onResume()
        _uiManager?.onResume()
    }

    override fun onSelect(view: FCLMenuView) {
        refreshMenuView(view)
        val speed = ThemeEngine.getInstance().getTheme().animationSpeed
        AnimUtil.playRotation(view, speed * 100L, 0f, 360f)
            .interpolator(OvershootInterpolator()).start()
        AnimUtil.playScaleX(view, speed * 100L, 1f, 2f, 1f)
            .start()
        AnimUtil.playScaleY(view, speed * 100L, 1f, 2f, 1f)
            .start()
        binding.apply {
            when (view) {
                home -> {
                    title.setTextWithAnim(getString(R.string.app_name) + " " + getString(R.string.app_version))
                    uiManager.switchUI(uiManager.mainUI)
                }

                manage -> {
                    val version = Profiles.getSelectedProfile().selectedVersion
                    if (version == null) {
                        refreshMenuView(null)
                        title.setTextWithAnim(getString(R.string.version))
                        uiManager.switchUI(uiManager.versionUI)
                    } else {
                        title.setTextWithAnim(getString(R.string.manage))
                        uiManager.manageUI.setVersion(version, Profiles.getSelectedProfile())
                        uiManager.switchUI(uiManager.manageUI)
                    }
                }

                download -> {
                    title.setTextWithAnim(getString(R.string.download))
                    uiManager.switchUI(uiManager.downloadUI)
                }

                controller -> {
                    title.setTextWithAnim(getString(R.string.controller))
                    uiManager.switchUI(uiManager.controllerUI)
                }

                setting -> {
                    title.setTextWithAnim(getString(R.string.setting))
                    uiManager.switchUI(uiManager.settingUI)
                }
            }
        }
    }

    fun refreshMenuView(view: FCLMenuView?) {
        binding.leftMenu.forEach {
            if (it is FCLMenuView && it != view) {
                it.isSelected = false
            }
        }
    }

    override fun onClick(view: View) {
        binding.apply {
            if (view === account && uiManager.currentUI !== uiManager.accountUI) {
                refreshMenuView(null)
                title.setTextWithAnim(getString(R.string.account))
                uiManager.switchUI(uiManager.accountUI)
            }
            if (view === version && uiManager.currentUI !== uiManager.versionUI) {
                refreshMenuView(null)
                title.setTextWithAnim(getString(R.string.version))
                uiManager.switchUI(uiManager.versionUI)
            }
            if (view === back) {
                uiManager.onBackPressed()
            }
            if (view === jar) {
                jar.isSelected = false
                JarExecutorHelper.start(this@MainActivity, this@MainActivity)
            }
            if (view === start) {
                if (!Controllers.isInitialized()) {
                    title.setTextWithAnim(getString(R.string.message_loading_controllers))
                    AnimUtil.playTranslationX(start, 700, 0f, 50f, -50f, 50f, -50f, 0f)
                        .interpolator(OvershootInterpolator()).start()
                    return
                }
                FCLBridge.BACKEND_IS_BOAT = binding.backend.position == 1
                val selectedProfile = Profiles.getSelectedProfile()
                DriverPlugin.selected = runCatching {
                    DriverPlugin.driverList.find {
                        it.driver == selectedProfile.getVersionSetting(selectedProfile.selectedVersion).driver
                    }
                }.getOrNull() ?: DriverPlugin.driverList[0]
                DisplayUtil.refreshDisplayMetrics(this@MainActivity)
                Versions.launch(this@MainActivity, selectedProfile)
            }
        }
    }

    private fun setupAccountDisplay() {
        binding.apply {
            currentAccount = object : SimpleObjectProperty<Account?>() {
                override fun invalidated() {
                    val account = get()
                    if (account == null) {
                        accountName.stringProperty().unbind()
                        accountHint.stringProperty().unbind()
                        avatar.imageProperty().unbind()
                        accountName.text = getString(R.string.account_state_no_account)
                        accountHint.text = getString(R.string.account_state_add)
                        avatar.setBackgroundDrawable(
                            TexturesLoader.toAvatar(
                                TexturesLoader.getDefaultSkin(TextureModel.ALEX).image,
                                ConvertUtils.dip2px(
                                    this@MainActivity, 30f
                                )
                            ).toDrawable(resources)
                        )
                    } else {
                        accountName.stringProperty()
                            .bind(BindingMapping.of(account) { obj: Account -> obj.character })
                        accountHint.stringProperty()
                            .bind(accountSubtitle(this@MainActivity, account))
                        avatar.imageProperty().unbind()
                        avatar.imageProperty().bind(
                            TexturesLoader.avatarBinding(
                                account, ConvertUtils.dip2px(
                                    this@MainActivity, 30f
                                )
                            )
                        )
                    }
                }
            }
            (currentAccount as SimpleObjectProperty<Account?>).bind(Accounts.selectedAccountProperty())
        }
    }

    fun refreshAvatar(account: Account) {
        lifecycleScope.launch {
            if (currentAccount.get() === account) {
                binding.avatar.imageProperty().unbind()
                binding.avatar.imageProperty().bind(
                    TexturesLoader.avatarBinding(
                        currentAccount.get(), ConvertUtils.dip2px(
                            this@MainActivity, 30f
                        )
                    )
                )
            }
        }
    }

    private fun loadVersion(version: String?) {
        isVersionLoading = true
        binding.versionProgress.visibility = View.VISIBLE
        profile = Profiles.getSelectedProfile()
        if (version != null && profile.repository.hasVersion(version)) {
            lifecycleScope.launch(Dispatchers.IO) {
                var game: String? = null
                runCatching {
                    game = profile.repository.getGameVersion(version)
                        .orElse(getString(R.string.message_unknown))
                }
                if (game == null) return@launch
                val libraries = StringBuilder(game)
                val analyzer = LibraryAnalyzer.analyze(
                    profile.repository.getResolvedPreservingPatchesVersion(
                        version
                    ),
                    profile.repository.getGameVersion(version).orElse(null)
                )
                for (mark in analyzer) {
                    val libraryId = mark.libraryId
                    val libraryVersion = mark.libraryVersion
                    if (libraryId == LibraryType.MINECRAFT.patchId) continue
                    if (AndroidUtils.hasStringId(
                            this@MainActivity,
                            "install_installer_" + libraryId.replace("-", "_")
                        )
                    ) {
                        libraries.append(", ").append(
                            AndroidUtils.getLocalizedText(
                                this@MainActivity,
                                "install_installer_" + libraryId.replace("-", "_")
                            )
                        )
                        if (libraryVersion != null) libraries.append(": ").append(
                            libraryVersion.replace(
                                "(?i)$libraryId".toRegex(), ""
                            )
                        )
                    }
                }
                val drawable = profile.repository.getVersionIconImage(version)
                withContext(Dispatchers.Main) {
                    isVersionLoading = false
                    binding.versionProgress.visibility = View.GONE
                    binding.versionName.text = version
                    binding.versionName.isSelected = true
//                    binding.versionHint.text = libraries.toString()
                    binding.icon.setBackgroundDrawable(drawable)
                }
            }
        } else {
            isVersionLoading = false
            binding.versionProgress.visibility = View.GONE
            binding.versionName.text = getString(R.string.version_no_version)
            binding.icon.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.img_grass
                )
            )
        }
    }

    private fun setupVersionDisplay() {
        holder.add(FXUtils.onWeakChangeAndOperate(Profiles.selectedVersionProperty()) { s: String? ->
            lifecycleScope.launch { loadVersion(s) }
        })
    }

    private fun accountSubtitle(context: Context, account: Account): ObservableValue<String> {
        return if (account is AuthlibInjectorAccount) {
            BindingMapping.of(account.server) { obj: AuthlibInjectorServer -> obj.name }
        } else {
            Bindings.createStringBinding({
                Accounts.getLocalizedLoginTypeName(
                    context,
                    Accounts.getAccountFactory(account)
                )
            })
        }
    }

    private fun updateColor() {
        binding.apply {
            backend.setSelectedBackground(ThemeEngine.getInstance().theme.ltColor)
            backend.setDivider(
                ThemeEngine.getInstance().theme.color2,
                ConvertUtils.dip2px(this@MainActivity, 1f),
                1,
                1
            )
            backend.setBorder(
                ConvertUtils.dip2px(this@MainActivity, 1f),
                ThemeEngine.getInstance().theme.color2,
                0,
                0
            )
            backend.setRipple(ThemeEngine.getInstance().theme.color2)
            pojav.textColor = ThemeEngine.getInstance().theme.color2
            boat.textColor = ThemeEngine.getInstance().theme.color2
            start.background = createBackground()
            createBackground().apply {
                version.background = this
                jar.background = this
            }
            version.backgroundTintList =
                ColorStateList.valueOf(ThemeEngine.getInstance().theme.color2).apply {
                    version.backgroundTintList = this
                    jar.backgroundTintList = this
                }
            version.setTextColor(ThemeEngine.getInstance().theme.color2)
            jar.setTextColor(ThemeEngine.getInstance().theme.color2)
        }

    }
    private fun initBackground() {
        theme = object : IntegerPropertyBase() {
            override fun invalidated() {
                get()
                updateColor()
            }

            override fun getBean(): Any? {
                return this
            }

            override fun getName(): String? {
                return "theme"
            }
        }
        theme2 = object : IntegerPropertyBase() {
            override fun invalidated() {
                get()
                updateColor()
            }

            override fun getBean(): Any? {
                return this
            }

            override fun getName(): String? {
                return "theme2"
            }
        }
        theme2Dark = object : IntegerPropertyBase() {
            override fun invalidated() {
                get()
                updateColor()
            }

            override fun getBean(): Any? {
                return this
            }

            override fun getName(): String? {
                return "theme2Dark"
            }
        }
        theme.bind(ThemeEngine.getInstance().theme.colorProperty())
        theme2.bind(ThemeEngine.getInstance().theme.color2Property())
        theme2Dark.bind(ThemeEngine.getInstance().theme.color2DarkProperty())
        binding.backend.setOnApplyWindowInsetsListener { _, insets ->
            insets
        }
    }

    private fun createBackground(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.TRANSPARENT)
            shape = GradientDrawable.RECTANGLE
            cornerRadius = ConvertUtils.dip2px(this@MainActivity, 8f).toFloat()
            setStroke(
                ConvertUtils.dip2px(this@MainActivity, 1f),
                ThemeEngine.getInstance().theme.color2
            )
        }
    }

    private fun playAnim() {
        binding.apply {
            val speed = ThemeEngine.getInstance().theme.animationSpeed
            AnimUtil.playTranslationX(
                listOf(leftMenu),
                speed * 100L,
                -100f,
                0f
            ).forEach {
                it.interpolator(BounceInterpolator()).start()
            }
            AnimUtil.playTranslationX(
                listOf(rightMenu),
                speed * 100L,
                100f,
                0f
            ).forEach {
                it.interpolator(BounceInterpolator()).start()
            }
            AnimUtil.playTranslationY(
                listOf(start, version, jar),
                speed * 100L,
                -200f,
                0f
            ).forEachIndexed { index, objectAnimator ->
                objectAnimator.interpolator(BounceInterpolator()).startAfter((index + 1) * 100L)
            }
            AnimUtil.playTranslationY(
                listOf(home, manage, download, controller, setting, back),
                speed * 100L,
                -300f,
                0f
            ).forEachIndexed { index, objectAnimator ->
                objectAnimator.interpolator(BounceInterpolator()).startAfter((index + 1) * 100L)
            }
            AnimUtil.playTranslationX(
                listOf(home, manage, download, controller, setting, back),
                speed * 100L,
                -100f,
                0f
            ).forEachIndexed { index, objectAnimator ->
                objectAnimator.interpolator(BounceInterpolator()).startAfter((index + 1) * 100L)
            }
        }
    }

    private fun shareLog() {
        try {
            val file = File(FCLPath.LOG_DIR).resolve("latest_game.log")
            if (!file.exists()) return
            val intent = Intent(Intent.ACTION_SEND)

            val uri = FileProvider.getUriForFile(
                this,
                "${application.packageName}.provider",
                file
            )
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(
                Intent.createChooser(
                    intent,
                    getString(com.tungsten.fcllibrary.R.string.crash_reporter_share)
                )
            )
        } catch (e: Exception) {
            LOG.log(Level.INFO, "Share error: $e")
        }
    }

    private fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_DENIED
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || !ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, applicationInfo.uid)
                startActivity(intent)
            } catch (_: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
        } else {
            permissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}