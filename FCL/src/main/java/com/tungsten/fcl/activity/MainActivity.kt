package com.tungsten.fcl.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
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
import com.tungsten.fcl.ui.compose.dialog.MiuixRendererSelectDialog
import com.mio.util.AnimUtil
import com.mio.util.AnimUtil.Companion.interpolator
import com.mio.util.AnimUtil.Companion.startAfter
import com.mio.util.DisplayUtil
import com.mio.util.GuideUtil
import com.mio.util.GuideUtil.Companion.guideTarget
import com.mio.util.ImageUtil
import com.mio.util.showWarningDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ActivityMainBinding
import com.tungsten.fcl.game.JarExecutorHelper
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.setting.Controllers
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.download.modpack.compose.ComposeLocalModpackPage
import com.tungsten.fcl.ui.main.compose.MainRightMenu
import com.tungsten.fcl.ui.main.compose.MainRightMenuBridge
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcl.upgrade.UpdateChecker
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.NavigationBus
import com.tungsten.fclauncher.plugins.DriverPlugin
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer
import com.tungsten.fclcore.auth.yggdrasil.TextureModel
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteMod.IMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLMenuView
import com.tungsten.fcllibrary.component.view.FCLMenuView.OnSelectListener
import com.tungsten.fcllibrary.util.ConvertUtils
import kotlinx.coroutines.Dispatchers
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private lateinit var currentAccount: MutableStateFlow<Account?>
    private lateinit var profile: Profile
    private val themeSubscriptions = ArrayList<FlowSubscriptions.Subscription>()
    var isVersionLoading = false
    private var modpackHandled = false
    lateinit var permissionResultLauncher: ActivityResultLauncher<String>
    private lateinit var sharedPreferences: SharedPreferences
    var mediaPlayer: MediaPlayer? = null
    private var videoPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modpackHandled = savedInstanceState?.getBoolean("modpack_handled") ?: false
        instance = WeakReference(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("launcher", MODE_PRIVATE)
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
                },
                114514,
                ""
            )
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
                goSetting.setOnClickListener(this@MainActivity)
                start.setOnClickListener(this@MainActivity)
                start.setOnLongClickListener { view ->
                    MiuixRendererSelectDialog(this@MainActivity, false) {
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

                NavigationBus.selectedMenu
                    .onEach { menu ->
                        val selectedView = when (menu) {
                            NavigationBus.Menu.HOME -> home
                            NavigationBus.Menu.MANAGE -> manage
                            NavigationBus.Menu.DOWNLOAD -> download
                            NavigationBus.Menu.CONTROLLER -> controller
                            NavigationBus.Menu.MULTIPLAYER -> multiplayer
                            NavigationBus.Menu.SETTING -> setting
                            null -> null
                        }
                        if (selectedView != null) {
                            refreshMenuView(selectedView)
                            selectedView.isSelected = true
                        } else {
                            refreshMenuView(null)
                        }
                    }
                    .launchIn(lifecycleScope)
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
                    // 启动等待遮罩：主 UI 加载完成（onLoad，主线程回调）后淡出移除
                    dismissSplashOverlay()
                    home.setOnSelectListener(this@MainActivity)
                    manage.setOnSelectListener(this@MainActivity)
                    download.setOnSelectListener(this@MainActivity)
                    controller.setOnSelectListener(this@MainActivity)
                    multiplayer.setOnSelectListener(this@MainActivity)
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
                    if (!modpackHandled) {
                        handleModpack(intent)
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
        setupLiveBackground()
        setupComposeRightMenu()
    }

    /**
     * Compose 右侧栏接线（迁移开关已固化，批 3：旧 account/start/version/jar View 常置 GONE）。
     * 全部交互经回调转回本 Activity 的既有方法（启动游戏/切 UI/JAR 执行逻辑零改动）。
     */
    private fun setupComposeRightMenu() {
        binding.apply {
            account.visibility = View.GONE
            start.visibility = View.GONE
            version.visibility = View.GONE
            jar.visibility = View.GONE
            rightMenuCompose.visibility = View.VISIBLE
            rightMenuCompose.addView(
                LegacyBridge.createComposeView(this@MainActivity) {
                    MainRightMenu(
                        onAccountClick = ::onAccountAreaClicked,
                        onVersionClick = ::onVersionAreaClicked,
                        onStartClick = ::onLaunchClicked,
                        onStartLongClick = ::onStartAreaLongClicked,
                        onJarClick = ::onJarAreaClicked,
                        onJarLongClick = ::onJarAreaLongClicked,
                        onGoSettingClick = ::onGoSettingClicked,
                    )
                },
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 启动等待遮罩显示期间直接消费返回键，不传给 UIManager
            if (binding.splashOverlay.visibility == View.VISIBLE) return true
            _uiManager?.onBackPressed()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    /** 移除启动等待遮罩：短淡出后 GONE，动画时长对齐 ThemeEngine.animationSpeed 既有约定（speed*100ms）。 */
    private fun dismissSplashOverlay() {
        val overlay = binding.splashOverlay
        if (overlay.visibility != View.VISIBLE) return
        val duration = ThemeEngine.getInstance().theme.animationSpeed * 100L
        overlay.animate()
            .alpha(0f)
            .setDuration(duration)
            .withEndAction { overlay.visibility = View.GONE }
            .start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("modpack_handled", modpackHandled)
    }

    override fun onPause() {
        super.onPause()
        _uiManager?.onPause()
        if (shouldPlayVideo() && binding.videoView.isPlaying) {
            videoPosition = binding.videoView.currentPosition
            binding.videoView.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        _uiManager?.onResume()
        if (shouldPlayVideo() && !binding.videoView.isPlaying) {
            binding.videoView.seekTo(videoPosition)
            binding.videoView.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        selectedAccountSubscription?.cancel()
        selectedAccountSubscription = null
        accountDisplaySubscription?.cancel()
        accountDisplaySubscription = null
        accountNameSubscription?.cancel()
        accountNameSubscription = null
        accountHintSubscription?.cancel()
        accountHintSubscription = null
        avatarSubscription?.cancel()
        avatarSubscription = null
        themeSubscriptions.forEach { it.cancel() }
        themeSubscriptions.clear()
        if (shouldPlayVideo()) {
            mediaPlayer = null
            binding.videoView.stopPlayback()
        }
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

                multiplayer -> {
                    title.setTextWithAnim(getString(R.string.terracotta))
                    uiManager.switchUI(uiManager.multiplayerUI)
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
            when (view) {
                account -> onAccountAreaClicked()
                version -> onVersionAreaClicked()
                back -> uiManager.onBackPressed()
                jar -> onJarAreaClicked()
                start -> onLaunchClicked()
                goSetting -> onGoSettingClicked()
            }
        }
    }

    /** 账户区点击（3.6 抽取：旧 account View 与 Compose 右侧栏共用）。 */
    internal fun onAccountAreaClicked() {
        binding.apply {
            if (uiManager.currentUI !== uiManager.accountUI) {
                refreshMenuView(null)
                title.setTextWithAnim(getString(R.string.account))
                uiManager.switchUI(uiManager.accountUI)
            }
        }
    }

    /** 版本管理按钮点击（3.6 抽取：旧 version View 与 Compose 右侧栏共用）。 */
    internal fun onVersionAreaClicked() {
        binding.apply {
            if (uiManager.currentUI !== uiManager.versionUI) {
                refreshMenuView(null)
                title.setTextWithAnim(getString(R.string.version))
                uiManager.switchUI(uiManager.versionUI)
            }
        }
    }

    /** 启动按钮点击（3.6 抽取：旧 start View 与 Compose 右侧栏共用；启动链路零改动）。 */
    internal fun onLaunchClicked() {
        binding.apply {
            if (!Controllers.isInitialized()) {
                title.setTextWithAnim(getString(R.string.message_loading_controllers))
                // Compose 右侧栏：经桥接节拍触发抖动（对齐旧 AnimUtil 抖动）
                MainRightMenuBridge.startShakeTick.value =
                    MainRightMenuBridge.startShakeTick.value + 1
                return
            }
            val selectedProfile = Profiles.getSelectedProfile()
            DriverPlugin.selected = runCatching {
                DriverPlugin.driverList.find {
                    it.driver == selectedProfile.getVersionSetting(selectedProfile.selectedVersion).driver
                }
            }.getOrNull() ?: DriverPlugin.driverList[0]
            refreshScreenSize()
            DisplayUtil.refreshDisplayMetrics(this@MainActivity)
            Versions.launch(this@MainActivity, selectedProfile)
        }
    }

    /** 启动按钮长按：选择渲染器后启动（3.6 抽取，逻辑对齐原 setOnLongClickListener）。 */
    internal fun onStartAreaLongClicked() {
        MiuixRendererSelectDialog(this@MainActivity, false) {
            onLaunchClicked()
        }.show()
    }

    /** JAR 执行按钮点击（3.6 抽取：旧 jar View 与 Compose 右侧栏共用）。 */
    internal fun onJarAreaClicked() {
        binding.apply {
            if (sharedPreferences.getBoolean("showJarExecutorWarnDialog", true)) {
                showWarningDialog(this@MainActivity, getString(R.string.jar_executor_warn)) {
                    sharedPreferences.edit {
                        putBoolean("showJarExecutorWarnDialog", false)
                    }
                }
                return
            }
            jar.isSelected = false
            JarExecutorHelper.start(this@MainActivity)
        }
    }

    /** JAR 执行按钮长按：自定义参数执行（3.6 抽取，逻辑对齐原 setOnLongClickListener）。 */
    internal fun onJarAreaLongClicked() {
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
    }

    /** 启动区设置入口点击（3.6 抽取：旧 goSetting View 与 Compose 右侧栏共用）。 */
    internal fun onGoSettingClicked() {
        binding.apply {
            val profile = Profiles.getSelectedProfile()
            if (profile.versionSetting.isGlobal) {
                setting.isSelected = true
                uiManager.settingUI.runAfterInit {
                    val tab = uiManager.settingUI.tabLayout.getTabAt(0)
                    uiManager.settingUI.tabLayout.selectTab(tab)
                }
            } else {
                manage.isSelected = true
                uiManager.manageUI.runAfterInit {
                    val tab = uiManager.manageUI.tabLayout.getTabAt(0)
                    uiManager.manageUI.tabLayout.selectTab(tab)
                }
            }
        }
    }

    private var accountDisplaySubscription: FlowSubscriptions.Subscription? = null
    private var accountNameSubscription: FlowSubscriptions.Subscription? = null
    private var accountHintSubscription: FlowSubscriptions.Subscription? = null
    private var avatarSubscription: FlowSubscriptions.Subscription? = null

    private fun setupAccountDisplay() {
        binding.apply {
            currentAccount = MutableStateFlow(null)
            // 阶段 4c：原匿名 SimpleObjectProperty 的 invalidated() 改为订阅 currentAccount。
            accountDisplaySubscription?.cancel()
            accountDisplaySubscription = FlowSubscriptions.subscribe(currentAccount) { account ->
                if (account == null) {
                    accountNameSubscription?.cancel()
                    accountNameSubscription = null
                    accountHintSubscription?.cancel()
                    accountHintSubscription = null
                    avatarSubscription?.cancel()
                    avatarSubscription = null
                    accountName.text = getString(R.string.account_state_no_account)
                    accountHint.text = getString(R.string.account_state_add)
                    avatar.setBackgroundDrawable(
                        TexturesLoader.toAvatar(
                            TexturesLoader.getDefaultSkin(TextureModel.ALEX).image,
                            ConvertUtils.dip2px(
                                this@MainActivity, 52f
                            )
                        ).toDrawable(resources)
                    )
                } else {
                    // 对齐原 bind(createObjectBinding)：先同步当前值，再跟随 revisionFlow 重算
                    accountNameSubscription?.cancel()
                    accountName.stringFlow().value = account.character
                    accountNameSubscription = FlowSubscriptions.subscribe(account.revisionFlow()) {
                        accountName.stringFlow().value = account.character
                    }
                    accountHintSubscription?.cancel()
                    accountHint.stringFlow().value = accountSubtitle(this@MainActivity, account)
                    accountHintSubscription = if (account is AuthlibInjectorAccount) {
                        FlowSubscriptions.subscribe(account.server.revisionFlow()) {
                            accountHint.stringFlow().value = account.server.name
                        }
                    } else {
                        null
                    }
                    avatarSubscription?.cancel()
                    avatarSubscription = FlowSubscriptions.subscribeWithCurrent(
                        TexturesLoader.avatarFlow(
                            account, ConvertUtils.dip2px(
                                this@MainActivity, 52f
                            )
                        )
                    ) {
                        avatar.imageFlow().value = it
                    }
                }
            }
            // 阶段 4a：Accounts.selectedAccount 已 StateFlow 化；subscribeWithCurrent 等价
            // 原 bind（先同步当前值再跟随），onDestroy 取消订阅（对齐 bind 弱引用自动摘除）。
            selectedAccountSubscription?.cancel()
            selectedAccountSubscription = FlowSubscriptions.subscribeWithCurrent(Accounts.selectedAccountFlow()) {
                currentAccount.value = it
            }
        }
    }

    private var selectedAccountSubscription: FlowSubscriptions.Subscription? = null

    fun refreshAvatar(account: Account) {
        lifecycleScope.launch {
            if (currentAccount.value === account) {
                avatarSubscription?.cancel()
                avatarSubscription = FlowSubscriptions.subscribeWithCurrent(
                    TexturesLoader.avatarFlow(
                        currentAccount.value, ConvertUtils.dip2px(
                            this@MainActivity, 52f
                        )
                    )
                ) {
                    binding.avatar.imageFlow().value = it
                }
                // 3.6：通知 Compose 右侧栏重建头像绑定（旧 View 重绑逻辑保留，开关回滚可用）
                MainRightMenuBridge.avatarRefreshTick.value =
                    MainRightMenuBridge.avatarRefreshTick.value + 1
            }
        }
    }

    private fun loadVersion(version: String?) {
        isVersionLoading = true
        binding.versionProgress.visibility = View.VISIBLE
        // 3.6：镜像到 Compose 右侧栏（旧 View 更新逻辑保留，开关回滚可用）
        MainRightMenuBridge.versionDisplay.value =
            MainRightMenuBridge.versionDisplay.value.copy(loading = true)
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
                    MainRightMenuBridge.versionDisplay.value = MainRightMenuBridge.VersionDisplay(
                        loading = false,
                        name = version,
                        icon = drawable,
                    )
                }
            }
        } else {
            isVersionLoading = false
            binding.versionProgress.visibility = View.GONE
            binding.versionName.text = getString(R.string.version_no_version)
            val fallbackIcon = AppCompatResources.getDrawable(
                this,
                R.drawable.img_grass
            )
            binding.icon.setBackgroundDrawable(fallbackIcon)
            MainRightMenuBridge.versionDisplay.value = MainRightMenuBridge.VersionDisplay(
                loading = false,
                name = getString(R.string.version_no_version),
                icon = fallbackIcon,
            )
        }
    }

    private fun setupVersionDisplay() {
        // 阶段 4a：Profiles.selectedVersion 已 StateFlow 化；launchIn(lifecycleScope)
        // 等价原弱引用监听（Activity 销毁自动退订），collect 含当前值对齐 onChangeAndOperate。
        Profiles.selectedVersionFlow()
            .onEach { s -> lifecycleScope.launch { loadVersion(s) } }
            .launchIn(lifecycleScope)
    }

    private fun accountSubtitle(context: Context, account: Account): String {
        return if (account is AuthlibInjectorAccount) {
            account.server.name
        } else {
            Accounts.getLocalizedLoginTypeName(
                context,
                Accounts.getAccountFactory(account)
            )
        }
    }

    private fun updateColor() {
        binding.apply {
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
        // 阶段 4c：原三个匿名 IntegerPropertyBase（invalidated → updateColor）+
        // bind(colorProperty/color2Property/color2DarkProperty) 改为直接订阅主题 Flow；
        // subscribeWithCurrent 对齐 bind 的"先同步当前值再跟随"（初始化即各触发一次）。
        themeSubscriptions.add(FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().theme.colorFlow()) { updateColor() })
        themeSubscriptions.add(FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().theme.color2Flow()) { updateColor() })
        themeSubscriptions.add(FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().theme.color2DarkFlow()) { updateColor() })
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
                listOf(home, manage, download, controller, multiplayer, setting, back),
                speed * 100L,
                -300f,
                0f
            ).forEachIndexed { index, objectAnimator ->
                objectAnimator.interpolator(BounceInterpolator()).startAfter((index + 1) * 100L)
            }
            AnimUtil.playTranslationX(
                listOf(home, manage, download, controller, multiplayer, setting, back),
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

    fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_DENIED
        }
    }

    fun requestNotificationPermission() {
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

    fun shouldPlayVideo(): Boolean {
        return File(FCLPath.LIVE_BACKGROUND_PATH).exists()
    }

    fun setupLiveBackground() {
        if (shouldPlayVideo()) {
            binding.videoView.visibility = View.VISIBLE
            binding.videoView.setVideoPath(FCLPath.LIVE_BACKGROUND_PATH)
            binding.videoView.setOnPreparedListener {
                mediaPlayer = it
                it.isLooping = true
                setLiveBackgroundVolume()
                binding.videoView.start()
            }
            binding.videoView.setOnCompletionListener {
                binding.videoView.seekTo(0)
                binding.videoView.start()
            }
            binding.videoView.setOnErrorListener { _, _, _ ->
                mediaPlayer = null
                return@setOnErrorListener true
            }
        } else {
            mediaPlayer = null
            binding.videoView.visibility = View.GONE
            binding.videoView.stopPlayback()
        }
    }

    fun setLiveBackgroundVolume() {
        mediaPlayer?.let {
            val volume = sharedPreferences.getInt("videoBackgroundVolume", 100) / 100f
            it.setVolume(volume, volume)
        }
    }

    private fun handleModpack(intent: Intent) {
        val path = intent.getStringExtra("modpack_cache_path") ?: return
        modpackHandled = true
        val file = File(path)
        if (!file.exists()) return
        Toast.makeText(
            this,
            getString(R.string.modpack_external_detected, file.name),
            Toast.LENGTH_SHORT
        ).show()
        NavigationBus.select(NavigationBus.Menu.DOWNLOAD)
        val downloadUI = uiManager.downloadUI
        downloadUI.runAfterInit {
            val page = ComposeLocalModpackPage(
                this,
                PageManager.PAGE_ID_TEMP,
                downloadUI.container,
                profile,
                null,
                file
            )
            downloadUI.pageManager.showTempPage(page)
        }
    }

    private fun refreshScreenSize() {
        DisplayUtil.screenWidth =  binding.root.width
        DisplayUtil.screenHeight = binding.root.height
    }
}