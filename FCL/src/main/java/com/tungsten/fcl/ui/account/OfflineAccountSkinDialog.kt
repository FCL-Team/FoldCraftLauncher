package com.tungsten.fcl.ui.account

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.DialogOfflineAccountSkinBinding
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fclcore.auth.offline.OfflineAccount
import com.tungsten.fclcore.auth.offline.Skin
import com.tungsten.fclcore.auth.offline.Skin.LoadedSkin
import com.tungsten.fclcore.auth.yggdrasil.TextureModel
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.skin.SkinRenderer
import java.io.File
import java.util.logging.Level
import com.mio.util.getScreenHeight
import com.mio.util.getScreenWidth

class OfflineAccountSkinDialog(context: Context, private val accountListItem: AccountListItem) :
    FCLDialog(context), View.OnClickListener {
    private val account: OfflineAccount = accountListItem.account as OfflineAccount
    private var binding: DialogOfflineAccountSkinBinding =
        DialogOfflineAccountSkinBinding.inflate(layoutInflater)
    private val renderer: SkinRenderer

    /** Texture model chosen via the classic/slim RadioGroup */
    private var model: TextureModel = TextureModel.STEVE

    init {
        setContentView(binding.root)
        setCancelable(false)

        renderer = SkinRenderer(getContext())
        binding.skinView.setRenderer(renderer, 5f)

        binding.modelClassic.setOnClickListener(this)
        binding.modelSlim.setOnClickListener(this)
        binding.skinPath.setOnClickListener(this)
        binding.skinPathReset.setOnClickListener(this)
        binding.capePath.setOnClickListener(this)
        binding.capePathReset.setOnClickListener(this)
        binding.positive.setOnClickListener(this)
        binding.negative.setOnClickListener(this)

        // Restore state from existing skin
        val skin = account.skin
        if (skin == null) {
            model = TextureModel.detectUUID(account.uuid)
        } else {
            // Restore model: legacy ALEX -> slim, otherwise from stored texture model
            model = if (skin.type() == Skin.Type.ALEX) TextureModel.ALEX else skin.textureModel()
            binding.skinPathText.string = skin.localSkinPath()
            binding.capePathText.string = skin.localCapePath()
        }
        refreshModelRadio()
    }

    override fun show() {
        val width = getScreenWidth()
        var height = getScreenHeight()
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
        this.skin.load()
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
                    // Model is controlled by the user's classic/slim selection
                    val slim = model == TextureModel.ALEX
                    if (result == null || result.skin() == null && result.cape() == null) {
                        // No custom skin selected: fall back to the default skin of the chosen model
                        renderer.updateTexture(
                            TexturesLoader.getDefaultSkin(model).image(),
                            null,
                            slim
                        )
                        return@whenComplete
                    }
                    renderer.updateTexture(
                        if (result.skin() != null) result.skin()
                            .image else TexturesLoader.getDefaultSkin(
                            model
                        ).image(),
                        if (result.cape() != null) result.cape().image else null,
                        slim
                    )
                }
            }.start()
    }

    private fun refreshModelRadio() {
        binding.modelClassic.setChecked(model == TextureModel.STEVE)
        binding.modelSlim.setChecked(model == TextureModel.ALEX)
    }

    private val skin: Skin
        get() {
            val skinPath = binding.skinPathText.string
            val capePath = binding.capePathText.string
            val hasSkin = StringUtils.isNotBlank(skinPath)
            val hasCape = StringUtils.isNotBlank(capePath)
            return if (hasSkin || hasCape) {
                Skin(
                    Skin.Type.LOCAL_FILE,
                    model,
                    if (hasSkin) skinPath else null,
                    if (hasCape) capePath else null
                )
            } else {
                Skin(Skin.Type.DEFAULT, model, null, null)
            }
        }

    override fun onClick(view: View?) {
        when (view) {
            // Model selection
            binding.modelClassic -> {
                model = TextureModel.STEVE
                refreshModelRadio()
                refreshSkin()
            }

            binding.modelSlim -> {
                model = TextureModel.ALEX
                refreshModelRadio()
                refreshSkin()
            }
            // File selection：先放缓存，点确认时才落位 SKIN_DIR，取消不影响已有皮肤文件
            binding.skinPath -> MainActivity.getInstance().fileLauncher.launchSingleSelection(
                null,
                listOf(".png")
            ) {
                binding.skinPathText.string =
                    it?.get(0)?.toFile(context, File(FCLPath.CACHE_DIR))?.absolutePath
                        ?: return@launchSingleSelection
                refreshSkin()
            }

            binding.skinPathReset -> {
                binding.skinPathText.string = null
                refreshSkin()
            }

            binding.capePath -> MainActivity.getInstance().fileLauncher.launchSingleSelection(
                null,
                listOf(".png")
            ) {
                binding.capePathText.string =
                    it?.get(0)?.toFile(context, File(FCLPath.CACHE_DIR))?.absolutePath
                        ?: return@launchSingleSelection
                refreshSkin()
            }

            binding.capePathReset -> {
                binding.capePathText.string = null
                refreshSkin()
            }
            // Buttons
            binding.positive -> {
                commitSkinFile(binding.skinPathText, "${account.uuid}.png")
                commitSkinFile(binding.capePathText, "${account.uuid}_cape.png")
                account.setSkin(this.skin)
                accountListItem.refreshSkinBinding()
                dismiss()
            }

            binding.negative -> dismiss()
        }
    }

    /** 把临时选中的皮肤/披风文件落位到 SKIN_DIR 固定名，并更新为最终路径 */
    private fun commitSkinFile(text: FCLTextView, fileName: String) {
        val srcPath = text.string ?: return
        val src = File(srcPath)
        val dest = File(FCLPath.SKIN_DIR, fileName)
        if (src.canonicalPath != dest.canonicalPath) {
            runCatching { src.copyTo(dest, overwrite = true) }
                .onSuccess { text.string = dest.absolutePath }
        }
    }
}
