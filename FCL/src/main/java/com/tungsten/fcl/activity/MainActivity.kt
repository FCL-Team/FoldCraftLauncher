package com.tungsten.fcl.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import com.mio.util.AnimUtil
import com.mio.util.AnimUtil.Companion.interpolator
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ActivityMainBinding
import com.tungsten.fcl.game.JarExecutorHelper
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcl.upgrade.UpdateChecker
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.FXUtils
import com.tungsten.fcl.util.WeakListenerHolder
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer
import com.tungsten.fclcore.auth.offline.Skin
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
import java.util.function.Consumer
import java.util.logging.Level
import java.util.stream.Stream

class MainActivity : FCLActivity(), OnSelectListener, View.OnClickListener {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var instance: MainActivity
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
        instance = this
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        bind.background.background = ThemeEngine.getInstance().getTheme().getBackground(this)

        Skin.registerDefaultSkinLoader {
            when (it) {
                Skin.Type.ALEX -> return@registerDefaultSkinLoader Skin::class.java.getResourceAsStream(
                    "/assets/img/alex.png"
                )

                Skin.Type.ARI -> return@registerDefaultSkinLoader Skin::class.java.getResourceAsStream(
                    "/assets/img/ari.png"
                )

                Skin.Type.EFE -> return@registerDefaultSkinLoader Skin::class.java.getResourceAsStream(
                    "/assets/img/efe.png"
                )

                Skin.Type.KAI -> return@registerDefaultSkinLoader Skin::class.java.getResourceAsStream(
                    "/assets/img/kai.png"
                )

                Skin.Type.MAKENA -> return@registerDefaultSkinLoader Skin::class.java.getResourceAsStream(
                    "/assets/img/makena.png"
                )

                Skin.Type.NOOR -> return@registerDefaultSkinLoader Skin::class.java.getResourceAsStream(
                    "/assets/img/noor.png"
                )

                Skin.Type.STEVE -> return@registerDefaultSkinLoader Skin::class.java.getResourceAsStream(
                    "/assets/img/steve.png"
                )

                Skin.Type.SUNNY -> return@registerDefaultSkinLoader Skin::class.java.getResourceAsStream(
                    "/assets/img/sunny.png"
                )

                Skin.Type.ZURI -> return@registerDefaultSkinLoader Skin::class.java.getResourceAsStream(
                    "/assets/img/zuri.png"
                )

                else -> return@registerDefaultSkinLoader null
            }
        }

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
                    val padding = ConvertUtils.dip2px(this@MainActivity, 15f)
                    val editText = FCLEditText(this@MainActivity)
                    val layout = RelativeLayout(this@MainActivity)
                    editText.hint = "-jar xxx"
                    editText.setLines(1)
                    editText.maxLines = 1
                    layout.setPadding(padding, padding, padding, padding)
                    layout.addView(editText)
                    val dialog = AlertDialog.Builder(this@MainActivity)
                        .setTitle(R.string.jar_execute_custom_args)
                        .setView(layout)
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
                    layout.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    editText.layoutParams = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    ThemeEngine.getInstance().applyFullscreen(
                        dialog.window,
                        ThemeEngine.getInstance().getTheme().isFullscreen
                    )
                    dialog.show()
                    true
                }
                launch.setOnClickListener(this@MainActivity)
                launch.setOnLongClickListener {
                    startActivity(Intent(this@MainActivity, ShellActivity::class.java))
                    true
                }

                uiManager = UIManager(this@MainActivity, uiLayout)
                _uiManager = uiManager
                uiManager.registerDefaultBackEvent() {
                    if (uiManager.currentUI === uiManager.mainUI) {
                        val i = Intent(Intent.ACTION_MAIN)
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        i.addCategory(Intent.CATEGORY_HOME)
                        startActivity(i)
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

                    setupAccountDisplay()
                    setupVersionDisplay()
                    UpdateChecker.getInstance().checkAuto(this@MainActivity).start()
                }
                playAnim()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
            _uiManager?.onBackPressed()
        }
        return true
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
        bind.apply {
            when (view) {
                home -> {
                    title.setTextWithAnim(getString(R.string.app_name))
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
        bind.menu.forEach {
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
                Versions.launch(this@MainActivity, Profiles.getSelectedProfile())
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

    @SuppressLint("UseCompatLoadingForDrawables")
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
                val game = Profiles.getSelectedProfile().repository.getGameVersion(version)
                    .orElse(getString(R.string.message_unknown))
                val libraries = StringBuilder(game)
                val analyzer = LibraryAnalyzer.analyze(
                    Profiles.getSelectedProfile().repository.getResolvedPreservingPatchesVersion(version)
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
            bind.icon.setBackgroundDrawable(getDrawable(R.drawable.img_grass))
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

    private fun playAnim() {
        bind.apply {
            AnimUtil.playTranslationX(
                leftMenu,
                ThemeEngine.getInstance().getTheme().animationSpeed * 100L,
                -100f,
                0f
            ).interpolator(BounceInterpolator()).start()
            AnimUtil.playTranslationX(
                rightMenu,
                ThemeEngine.getInstance().getTheme().animationSpeed * 100L,
                100f,
                0f
            ).interpolator(BounceInterpolator()).start()
        }
    }
}