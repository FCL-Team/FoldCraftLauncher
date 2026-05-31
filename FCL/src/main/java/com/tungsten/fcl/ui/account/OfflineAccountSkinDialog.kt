package com.tungsten.fcl.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity.Companion.getInstance
import com.tungsten.fcl.databinding.DialogOfflineAccountSkinBinding
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.FXUtils
import com.tungsten.fcl.util.RequestCodes
import com.tungsten.fclcore.auth.offline.OfflineAccount
import com.tungsten.fclcore.auth.offline.Skin
import com.tungsten.fclcore.auth.offline.Skin.LoadedSkin
import com.tungsten.fclcore.auth.yggdrasil.TextureModel
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.browser.FileBrowser
import com.tungsten.fcllibrary.browser.options.LibMode
import com.tungsten.fcllibrary.browser.options.SelectionMode
import com.tungsten.fcllibrary.component.ResultListener
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.skin.SkinRenderer
import java.util.logging.Level

class OfflineAccountSkinDialog(context: Context, private val accountListItem: AccountListItem) :
    FCLDialog(context), View.OnClickListener {
    private val account: OfflineAccount = accountListItem.account as OfflineAccount
    private var binding: DialogOfflineAccountSkinBinding = DialogOfflineAccountSkinBinding.inflate(layoutInflater)
    private val renderer: SkinRenderer
    private val skinBinding: InvalidationListener
    private val typeProperty: ObjectProperty<Skin.Type> =
        SimpleObjectProperty<Skin.Type>(this, "type", Skin.Type.DEFAULT)

    init {
        setContentView(binding.root)
        setCancelable(false)

        renderer = SkinRenderer(getContext())
        binding.skinView.setRenderer(renderer, 5f)
        binding.defaultSkin.setOnClickListener(this)
        binding.steve.setOnClickListener(this)
        binding.alex.setOnClickListener(this)
        binding.local.setOnClickListener(this)
        binding.csl.setOnClickListener(this)

        binding.skinPath.setOnClickListener(this)
        binding.capePath.setOnClickListener(this)
        binding.positive.setOnClickListener(this)
        binding.negative.setOnClickListener(this)
        if (account.skin == null) {
            refreshRadio(0)
            typeProperty.set(Skin.Type.DEFAULT)
        } else {
            when (account.skin.type) {
                Skin.Type.STEVE -> {
                    refreshRadio(1)
                    typeProperty.set(Skin.Type.STEVE)
                }

                Skin.Type.ALEX -> {
                    refreshRadio(2)
                    typeProperty.set(Skin.Type.ALEX)
                }

                Skin.Type.LOCAL_FILE -> {
                    refreshRadio(3)
                    typeProperty.set(Skin.Type.LOCAL_FILE)
                }

                Skin.Type.CUSTOM_SKIN_LOADER_API -> {
                    refreshRadio(4)
                    typeProperty.set(Skin.Type.CUSTOM_SKIN_LOADER_API)
                }

                else -> {
                    refreshRadio(0)
                    typeProperty.set(Skin.Type.DEFAULT)
                }
            }
            binding.skinPathText.string = account.skin.localSkinPath
            binding.capePathText.string = account.skin.localCapePath
            binding.cslUrl.setText(account.skin.cslApi)
        }
        skinBinding = FXUtils.observeWeak(
            { this.refreshSkin() },
            typeProperty,
            binding.cslUrl.stringProperty(),
            binding.skinPathText.stringProperty(),
            binding.capePathText.stringProperty()
        )
    }

    override fun show() {
        val width = AndroidUtils.getScreenWidth()
        var height = AndroidUtils.getScreenHeight()
        height = if (height * 2 < width) {
            WindowManager.LayoutParams.MATCH_PARENT
        } else {
            height * 2 / 3
        }
        window?.setLayout(width * 2 / 3, height)
        super.show()
        binding.skinView.onResume()
        refreshSkin()
    }

    override fun dismiss() {
        binding.skinView.onPause()
        super.dismiss()
    }

    private fun refreshSkin() {
        this.skin.load(account.username)
            .whenComplete(
                Schedulers.androidUIThread()
            ) { result: LoadedSkin?, exception: Exception? ->
                if (exception != null) {
                    Logging.LOG.log(Level.WARNING, "Failed to load skin", exception)
                    Toast.makeText(
                        context,
                        context.getString(R.string.message_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (result == null || result.skin == null && result.cape == null) {
                        renderer.setTexture(
                            TexturesLoader.getDefaultSkin(
                                TextureModel.detectUUID(
                                    account.uuid
                                )
                            ).image, null
                        )
                        return@whenComplete
                    }
                    renderer.setTexture(
                        if (result.skin != null) result.skin
                            .image else TexturesLoader.getDefaultSkin(
                            TextureModel.detectUUID(
                                account.uuid
                            )
                        ).image,
                        if (result.cape != null) result.cape.image else null
                    )
                }
            }.start()
    }

    private fun refreshRadio(position: Int) {
        binding.defaultSkin.setChecked(position == 0)
        binding.steve.setChecked(position == 1)
        binding.alex.setChecked(position == 2)
        binding.local.setChecked(position == 3)
        binding.csl.setChecked(position == 4)
        binding.localLayout.visibility = if (position == 3) View.VISIBLE else View.GONE
        binding.cslLayout.visibility = if (position == 4) View.VISIBLE else View.GONE
    }

    private val skin: Skin
        get() = Skin(
            typeProperty.get(),
            binding.cslUrl.stringValue ?: "",
            null,
            if (StringUtils.isBlank(binding.skinPathText.string)) null else binding.skinPathText.string,
            if (StringUtils.isBlank(binding.capePathText.string)) null else binding.capePathText.string
        )

    override fun onClick(view: View?) {
        if (view === binding.defaultSkin) {
            refreshRadio(0)
            typeProperty.set(Skin.Type.DEFAULT)
        }
        if (view === binding.steve) {
            refreshRadio(1)
            typeProperty.set(Skin.Type.STEVE)
        }
        if (view === binding.alex) {
            refreshRadio(2)
            typeProperty.set(Skin.Type.ALEX)
        }
        if (view === binding.local) {
            refreshRadio(3)
            typeProperty.set(Skin.Type.LOCAL_FILE)
        }
        if (view === binding.csl) {
            refreshRadio(4)
            typeProperty.set(Skin.Type.CUSTOM_SKIN_LOADER_API)
        }

        if (view === binding.skinPath) {
            val builder = FileBrowser.Builder(context)
            builder.setLibMode(LibMode.FILE_CHOOSER)
            builder.setSelectionMode(SelectionMode.SINGLE_SELECTION)
            val suffix = ArrayList<String?>()
            suffix.add(".png")
            builder.setSuffix(suffix)
            builder.create().browse(
                getInstance(),
                RequestCodes.SELECT_SKIN_CODE,
                (ResultListener.Listener { requestCode: Int, resultCode: Int, data: Intent? ->
                    if (requestCode == RequestCodes.SELECT_SKIN_CODE && resultCode == Activity.RESULT_OK && data != null) {
                        val path = FileBrowser.getSelectedFiles(data)[0]
                        binding.skinPathText.string = path
                    }
                })
            )
        }
        if (view === binding.capePath) {
            val builder = FileBrowser.Builder(context)
            builder.setLibMode(LibMode.FILE_CHOOSER)
            builder.setSelectionMode(SelectionMode.SINGLE_SELECTION)
            val suffix = ArrayList<String?>()
            suffix.add(".png")
            builder.setSuffix(suffix)
            builder.create().browse(
                getInstance(),
                RequestCodes.SELECT_CAPE_CODE,
                (ResultListener.Listener { requestCode: Int, resultCode: Int, data: Intent? ->
                    if (requestCode == RequestCodes.SELECT_CAPE_CODE && resultCode == Activity.RESULT_OK && data != null) {
                        val path = FileBrowser.getSelectedFiles(data)[0]
                        binding.capePathText.string = path
                    }
                })
            )
        }

        if (view === binding.positive) {
            account.setSkin(this.skin)
            accountListItem.refreshSkinBinding()
            dismiss()
        }
        if (view === binding.negative) {
            dismiss()
        }
    }
}
