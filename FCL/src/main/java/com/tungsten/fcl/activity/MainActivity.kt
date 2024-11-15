package com.tungsten.fcl.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.forEach
import androidx.core.view.postDelayed
import androidx.databinding.DataBindingUtil
import com.mio.util.AnimUtil
import com.mio.util.AnimUtil.Companion.interpolator
import com.mio.util.AnimUtil.Companion.startAfter
import com.mio.util.GuideUtil
import com.mio.util.RendererUtil
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
import com.tungsten.fclauncher.FCLConfig
import com.tungsten.fclauncher.bridge.FCLBridge
import com.tungsten.fclauncher.plugins.RendererPlugin
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer
import com.tungsten.fclcore.auth.yggdrasil.TextureModel
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType
import com.tungsten.fclcore.event.Event
import com.tungsten.fclcore.fakefx.beans.binding.Bindings
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteMod.IMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.fakefx.BindingMapping
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLEditText
import com.tungsten.fcllibrary.component.view.FCLMenuView
import com.tungsten.fcllibrary.component.view.FCLMenuView.OnSelectListener
import com.tungsten.fcllibrary.util.ConvertUtils
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.function.Consumer
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

    lateinit var bind: ActivityMainBinding
    private var _uiManager: UIManager? = null
    private lateinit var uiManager: UIManager
    private lateinit var currentAccount: ObjectProperty<Account?>
    private val holder = WeakListenerHolder()
    private var profile: Profile? = null
    private var onVersionIconChangedListener: Consumer<Event>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = WeakReference(this)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        bind.background.background = ThemeEngine.getInstance().getTheme().getBackground(this)

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

        try {
            ConfigHolder.init()
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, e.message)
        }

        bind.apply {
            uiLayout.post {
                ThemeEngine.getInstance().registerEvent(leftMenu) {
                    leftMenu.setBackgroundColor(
                        ThemeEngine.getInstance().getTheme().color
                    )
                }

                account.setOnClickListener(this@MainActivity)
                version.setOnClickListener(this@MainActivity)
                executeJar.setOnClickListener(this@MainActivity)
                executeJar.setOnLongClickListener {
                    val editText = FCLEditText(this@MainActivity).apply {
                        hint = "-jar xxx"
                        setLines(1)
                        maxLines = 1
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle(R.string.jar_execute_custom_args)
                        .setView(editText)
                        .setPositiveButton(com.tungsten.fcllibrary.R.string.dialog_positive) { _: DialogInterface?, _: Int ->
                            JarExecutorHelper.exec(
                                this@MainActivity,
                                null,
                                JarExecutorHelper.getJava(null),
                                editText.text.toString()
                            )
                        }
                        .setNegativeButton(com.tungsten.fcllibrary.R.string.dialog_negative, null)
                        .create()
                        .show()
                    true
                }
                launch.setOnClickListener(this@MainActivity)
                launchBoat.setOnClickListener(this@MainActivity)
                OnLongClickListener { openRendererMenu(it);true }.apply {
                    launch.setOnLongClickListener(this)
                    launchBoat.setOnLongClickListener(this)
                }

                uiManager = UIManager(this@MainActivity, uiLayout)
                _uiManager = uiManager
                uiManager.registerDefaultBackEvent() {
                    if (uiManager.currentUI === uiManager.mainUI) {
                        val i = Intent(Intent.ACTION_MAIN)
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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
                    multiplayer.setOnSelectListener(this@MainActivity)
                    setting.setOnSelectListener(this@MainActivity)
                    back.setOnClickListener(this@MainActivity)
                    home.setSelected(true)
                    back.setOnLongClickListener {
                        startActivity(Intent(this@MainActivity, ShellActivity::class.java))
                        true
                    }

                    setupAccountDisplay()
                    setupVersionDisplay()
                    UpdateChecker.getInstance().checkAuto(this@MainActivity).start()
                }
                playAnim()
                uiLayout.postDelayed(1500) {
                    GuideUtil.show(
                        this@MainActivity,
                        setting,
                        getString(R.string.guide_theme2),
                        GuideUtil.TAG_GUIDE_THEME_2
                    )
                }
            }
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
        bind.apply {
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
                    title.setTextWithAnim(getString(R.string.multiplayer))
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
        bind.leftMenu.forEach {
            if (it is FCLMenuView && it != view) {
                it.isSelected = false
            }
        }
    }

    override fun onClick(view: View) {
        bind.apply {
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
            if (view === executeJar) {
                JarExecutorHelper.start(this@MainActivity, this@MainActivity)
            }
            if (view === launch) {
                if (!Controllers.isInitialized()) {
                    title.setTextWithAnim(getString(R.string.message_loading_controllers))
                    AnimUtil.playTranslationX(launch, 700, 0f, 50f, -50f, 50f, -50f, 0f)
                        .interpolator(OvershootInterpolator()).start()
                    AnimUtil.playTranslationX(launchBoat, 700, 0f, 50f, -50f, 50f, -50f, 0f)
                        .interpolator(OvershootInterpolator()).start()
                    return
                }
                val selectedProfile = Profiles.getSelectedProfile()
                RendererPlugin.rendererList.forEach {
                    if (it.des == selectedProfile.getVersionSetting(selectedProfile.selectedVersion).customRenderer) {
                        RendererPlugin.selected = it
                    }
                }
                Versions.launch(this@MainActivity, selectedProfile)
            }
            if (view === launchBoat) {
                FCLBridge.BACKEND_IS_BOAT = true;
                onClick(launch)
            }
        }
    }

    private fun setupAccountDisplay() {
        bind.apply {
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
                            BitmapDrawable(
                                resources,
                                TexturesLoader.toAvatar(
                                    TexturesLoader.getDefaultSkin(TextureModel.ALEX).image,
                                    ConvertUtils.dip2px(
                                        this@MainActivity, 30f
                                    )
                                )
                            )
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
        Schedulers.androidUIThread().execute {
            if (currentAccount.get() === account) {
                bind.avatar.imageProperty().unbind()
                bind.avatar.imageProperty().bind(
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
        bind.versionProgress.visibility = View.VISIBLE
        if (Profiles.getSelectedProfile() != profile) {
            profile = Profiles.getSelectedProfile()
            if (profile != null) {
                onVersionIconChangedListener =
                    profile!!.repository.onVersionIconChanged.registerWeak {
                        this.loadVersion(Profiles.getSelectedVersion())
                    }
            }
        }
        if (version != null && Profiles.getSelectedProfile() != null && Profiles.getSelectedProfile().repository.hasVersion(
                version
            )
        ) {
            Schedulers.defaultScheduler().execute {
                var game: String? = null
                kotlin.runCatching {
                    game = Profiles.getSelectedProfile().repository.getGameVersion(version)
                        .orElse(getString(R.string.message_unknown))
                }
                if (game == null) return@execute
                val libraries = StringBuilder(game!!)
                val analyzer = LibraryAnalyzer.analyze(
                    Profiles.getSelectedProfile().repository.getResolvedPreservingPatchesVersion(
                        version
                    )
                )
                for (mark in analyzer) {
                    val libraryId = mark.libraryId
                    val libraryVersion = mark.libraryVersion
                    if (libraryId == LibraryType.MINECRAFT.patchId) continue
                    if (AndroidUtils.hasStringId(
                            this,
                            "install_installer_" + libraryId.replace("-", "_")
                        )
                    ) {
                        libraries.append(", ").append(
                            AndroidUtils.getLocalizedText(
                                this,
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
                val drawable = Profiles.getSelectedProfile().repository.getVersionIconImage(version)
                Schedulers.androidUIThread().execute {
                    bind.versionProgress.visibility = View.GONE
                    bind.versionName.text = version
                    bind.versionHint.text = libraries.toString()
                    bind.icon.setBackgroundDrawable(drawable)
                }
            }
        } else {
            bind.versionProgress.visibility = View.GONE
            bind.versionName.text = getString(R.string.version_no_version)
            bind.versionHint.text = getString(R.string.version_manage)
            bind.icon.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.img_grass
                )
            )
        }
    }

    private fun setupVersionDisplay() {
        holder.add(FXUtils.onWeakChangeAndOperate(Profiles.selectedVersionProperty()) { s: String? ->
            Schedulers.androidUIThread().execute { loadVersion(s) }
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

    private fun openRendererMenu(view: View) {
        RendererUtil.openRendererMenu(
            this, view, bind.rightMenu.width, bind.launch.y.toInt()
        ) {
            onClick(view)
        }
    }

    private fun playAnim() {
        bind.apply {
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
                listOf(rightMenu, splitRight),
                speed * 100L,
                100f,
                0f
            ).forEach {
                it.interpolator(BounceInterpolator()).start()
            }
            AnimUtil.playTranslationY(
                listOf(executeJar, launch, launchBoat),
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
}