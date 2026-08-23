package com.tungsten.fcl.ui.account

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.DialogMicrosoftAccountSkinBinding
import com.tungsten.fclcore.auth.AuthenticationException
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccount
import com.tungsten.fclcore.auth.microsoft.MicrosoftService.MinecraftProfileResponseCape
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.skin.InvalidSkinException
import com.tungsten.fclcore.util.skin.NormalizedSkin
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLButton
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.skin.SkinRenderer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import java.util.logging.Level
import com.mio.util.getScreenHeight
import com.mio.util.getScreenWidth

class MicrosoftAccountSkinDialog(
    context: Context,
    private val accountListItem: AccountListItem
) : FCLDialog(context), View.OnClickListener {

    private val account: MicrosoftAccount = accountListItem.account as MicrosoftAccount
    private var binding: DialogMicrosoftAccountSkinBinding =
        DialogMicrosoftAccountSkinBinding.inflate(layoutInflater)
    private val renderer: SkinRenderer

    private var selectedSkinFile: String? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var capes: List<MinecraftProfileResponseCape> = emptyList()
    private var capeLoading = false

    init {
        setContentView(binding.root)
        setCancelable(false)

        renderer = SkinRenderer(context)
        binding.skinView.setRenderer(renderer, 5f)

        // Button listeners
        binding.skinFilePick.setOnClickListener(this)
        binding.upload.setOnClickListener(this)
        binding.resetSkinBtn.setOnClickListener(this)
        binding.hideCapeBtn.setOnClickListener(this)
        binding.positive.setOnClickListener(this)
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
        detectCurrentModel()
        refreshPreview()
        loadCapes()
    }

    override fun dismiss() {
        binding.skinView.onPause()
        scope.cancel()
        super.dismiss()
    }

    private fun refreshPreview() {
        val texture = accountListItem.texture.get()
        if (texture != null && texture.size >= 2) {
            renderer.setTexture(
                texture[0],
                texture[1]
            )
        }
    }

    /**
     * Detect the current skin model (classic or slim) from the loaded skin texture
     * and update the radio button accordingly.
     */
    private fun detectCurrentModel() {
        val texture = accountListItem.texture.get()
        val skinBitmap = texture?.getOrNull(0) ?: return
        try {
            val normalized = NormalizedSkin(skinBitmap)
            if (normalized.isSlim) {
                binding.modelSlim.isChecked = true
            } else {
                binding.modelClassic.isChecked = true
            }
        } catch (_: InvalidSkinException) {
            // Fallback: default to classic if skin can't be analyzed
            binding.modelClassic.isChecked = true
        }
    }

    /**
     * Load a local skin file into the 3D preview and auto-detect model.
     */
    private fun updatePreviewFromFile(filePath: String) {
        try {
            val skinImg: Bitmap = BitmapFactory.decodeFile(filePath)
                ?: return
            val normalized = NormalizedSkin(skinImg)
            // Update model radio to match detected model
            if (normalized.isSlim) {
                binding.modelSlim.isChecked = true
            } else {
                binding.modelClassic.isChecked = true
            }
            // Update 3D preview: new skin, keep current cape
            val currentCape = accountListItem.texture.get()?.getOrNull(1)
            renderer.setTexture(normalized.normalizedTexture, currentCape)
        } catch (_: Exception) {
            // Ignore preview errors
        }
    }

    private fun loadCapes() {
        scope.launch {
            try {
                val profile = account.profile
                capes = profile.map { it.capes.filterNotNull() }.orElse(emptyList())
                withContext(Dispatchers.Main) {
                    buildCapeList()
                }
            } catch (e: AuthenticationException) {
                LOG.log(Level.WARNING, "Failed to load capes", e)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun buildCapeList() {
        binding.capeListLayout.removeAllViews()
        if (capes.isEmpty()) {
            val noCapes = FCLTextView(context)
            noCapes.text = context.getString(R.string.account_cape_none)
            noCapes.setPadding(8, 4, 8, 4)
            binding.capeListLayout.addView(noCapes)
            return
        }

        for (cape in capes) {
            val capeBtn = FCLButton(context)
            val aliasText =
                if (cape.alias != null && cape.alias.isNotEmpty()) cape.alias else cape.id
            val stateText = when (cape.state) {
                "ACTIVE" -> " [${context.getString(R.string.account_cape_active)}]"
                else -> ""
            }
            capeBtn.text = "$aliasText$stateText"
            capeBtn.setAllCaps(false)
            capeBtn.setOnClickListener {
                activateCape(cape.id)
            }
            binding.capeListLayout.addView(capeBtn)
        }
    }

    private fun activateCape(capeId: String) {
        if (capeLoading) return
        // Save current skin to restore after cape change
        val currentSkin = renderer.texture[0]
        val capeUrl = capes.find { it.id == capeId }?.url
        capeLoading = true
        setLoading(true)
        scope.launch {
            try {
                account.showCape(capeId)
                // Load cape bitmap from URL for immediate preview update
                val capeBitmap = if (capeUrl != null) {
                    withContext(Dispatchers.IO) {
                        try {
                            val conn = URL(capeUrl).openConnection()
                            conn.connectTimeout = 8000
                            conn.readTimeout = 8000
                            BitmapFactory.decodeStream(conn.getInputStream())
                        } catch (e: Exception) {
                            LOG.log(Level.WARNING, "Failed to load cape image for preview", e)
                            null
                        }
                    }
                } else null
                withContext(Dispatchers.Main) {
                    capeLoading = false
                    setLoading(false)
                    // Update preview: keep skin, set new cape
                    if (currentSkin != null) {
                        renderer.setTexture(currentSkin, capeBitmap)
                    }
                    Toast.makeText(context, R.string.account_cape_activated, Toast.LENGTH_SHORT)
                        .show()
                    loadCapes()
                }
            } catch (e: Exception) {
                LOG.log(Level.WARNING, "Failed to activate cape", e)
                withContext(Dispatchers.Main) {
                    capeLoading = false
                    setLoading(false)
                    showError(e)
                }
            }
        }
    }

    private fun hideCape() {
        if (capeLoading) return
        // Save current skin to restore after cape change
        val currentSkin = renderer.texture[0]
        capeLoading = true
        setLoading(true)
        scope.launch {
            try {
                account.hideCape()
                withContext(Dispatchers.Main) {
                    capeLoading = false
                    setLoading(false)
                    // Update preview: keep skin, remove cape
                    if (currentSkin != null) {
                        renderer.setTexture(currentSkin, null)
                    }
                    Toast.makeText(context, R.string.account_cape_hidden, Toast.LENGTH_SHORT).show()
                    loadCapes()
                }
            } catch (e: Exception) {
                LOG.log(Level.WARNING, "Failed to hide cape", e)
                withContext(Dispatchers.Main) {
                    capeLoading = false
                    setLoading(false)
                    showError(e)
                }
            }
        }
    }

    private fun uploadFromFile() {
        val filePath = selectedSkinFile
        if (filePath == null) {
            Toast.makeText(context, R.string.account_skin_no_file_selected, Toast.LENGTH_SHORT)
                .show()
            return
        }

        setLoading(true)
        scope.launch {
            try {
                val skinImg: Bitmap = BitmapFactory.decodeFile(filePath)
                    ?: throw InvalidSkinException("Failed to read skin image")
                val skin = NormalizedSkin(skinImg)
                val detectedModel = if (skin.isSlim) "slim" else "classic"
                LOG.info("Uploading skin [$filePath], model [$detectedModel]")

                account.uploadSkin(detectedModel, File(filePath).toPath())

                withContext(Dispatchers.Main) {
                    setLoading(false)
                    // Update model radio to match detected model
                    if (detectedModel == "slim") {
                        binding.modelSlim.isChecked = true
                    } else {
                        binding.modelClassic.isChecked = true
                    }
                    Toast.makeText(context, R.string.account_skin_uploaded, Toast.LENGTH_SHORT)
                        .show()
                    accountListItem.refreshSkinBinding()
                    // Don't call refreshPreview() here — the binding resets to fallback
                    // before async fetch completes. The preview from updatePreviewFromFile()
                    // already shows the correct uploaded skin.
                }
            } catch (e: Exception) {
                LOG.log(Level.WARNING, "Failed to upload skin", e)
                withContext(Dispatchers.Main) {
                    setLoading(false)
                    showError(e)
                }
            }
        }
    }

    private fun resetSkin() {
        setLoading(true)
        scope.launch {
            try {
                account.resetSkin()

                withContext(Dispatchers.Main) {
                    setLoading(false)
                    Toast.makeText(context, R.string.account_skin_reset_done, Toast.LENGTH_SHORT)
                        .show()
                    accountListItem.refreshSkinBinding()
                    refreshPreview()
                }
            } catch (e: Exception) {
                LOG.log(Level.WARNING, "Failed to reset skin", e)
                withContext(Dispatchers.Main) {
                    setLoading(false)
                    showError(e)
                }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.upload.isEnabled = !loading
        binding.resetSkinBtn.isEnabled = !loading
        binding.hideCapeBtn.isEnabled = !loading
        // Disable cape list buttons during loading
        for (i in 0 until binding.capeListLayout.childCount) {
            binding.capeListLayout.getChildAt(i).isEnabled = !loading
        }
    }

    private fun showError(e: Exception) {
        val builder = FCLAlertDialog.Builder(context)
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
        builder.setMessage(
            com.tungsten.fcl.setting.Accounts.localizeErrorMessage(context, e)
        )
        builder.setNegativeButton(
            context.getString(com.tungsten.fcl.R.string.dialog_positive),
            null
        )
        builder.create().show()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.skinFilePick -> {
                MainActivity.getInstance().fileLauncher.launchSingleSelection(
                    null,
                    listOf(".png")
                ) { result ->
                    val path = result?.get(0)
                    if (path != null) {
                        selectedSkinFile = path
                        binding.skinFilePath.text = path.substringAfterLast('/')
                        updatePreviewFromFile(path)
                    }
                }
            }

            binding.upload -> uploadFromFile()
            binding.resetSkinBtn -> {
                val builder = FCLAlertDialog.Builder(context)
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                builder.setMessage(context.getString(R.string.account_skin_reset_confirm))
                builder.setPositiveButton { resetSkin() }
                builder.setNegativeButton(null)
                builder.create().show()
            }
            binding.hideCapeBtn -> hideCape()
            binding.positive -> dismiss()
        }
    }
}
