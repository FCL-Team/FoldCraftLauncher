package com.tungsten.fcl.activity

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.mio.JavaManager
import com.mio.util.ImageUtil
import com.tungsten.fcl.R
import com.tungsten.fcl.fragment.EulaFragment
import com.tungsten.fcl.fragment.RuntimeFragment
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.util.RuntimeUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.Locale
import java.util.logging.Level
import androidx.core.content.edit
import com.mio.manager.RendererManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : FCLActivity() {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionResultLauncher: ActivityResultLauncher<Array<String>>
    var lwjgl: Boolean = false
    var cacio: Boolean = false
    var cacio17: Boolean = false
    var java8: Boolean = false
    var java11: Boolean = false
    var java17: Boolean = false
    var java21: Boolean = false
    var jna: Boolean = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_splash)
        sharedPreferences = getSharedPreferences("launcher", MODE_PRIVATE)
        val background = findViewById<ConstraintLayout>(R.id.background)
        ImageUtil.loadInto(
            background,
            ThemeEngine.getInstance().getTheme().getBackground(this)
        )

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                checkPermission()
            }
        permissionResultLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                checkPermission()
            }
        if (sharedPreferences.getBoolean("isAgree", false)) {
            checkPermission()
        } else {
            FCLAlertDialog.Builder(this).apply {
                setCancelable(false)
                setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                setMessage(getString(R.string.splash_agreement))
                setPositiveButton {
                    sharedPreferences.edit { putBoolean("isAgree", true) }
                    checkPermission()
                }
                setNegativeButton(getString(com.tungsten.fcllibrary.R.string.crash_reporter_close)) { finish() }
                create().show()
            }
        }
    }

    private fun checkPermission() {
        if (hasPermission()) {
            init()
            return
        }
        FCLAlertDialog.Builder(this).apply {
            setCancelable(false)
            setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
            setMessage(getString(R.string.splash_permission_msg))
            setPositiveButton { requestPermission() }
            setNegativeButton { finish() }
            create().show()
        }
    }

    private fun init() {
        lifecycleScope.launch {
            async(Dispatchers.IO) {
                FCLPath.loadPaths(this@SplashActivity)
                Logging.start(Paths.get(FCLPath.LOG_DIR))
                initState()
            }.await()
            if (lwjgl && cacio && cacio17 && java8 && java11 && java17 && java21 && jna) {
                enterLauncher()
            } else {
                start()
            }
        }
    }

    fun start() {
        if (sharedPreferences.getBoolean("isFirstLaunch", true)) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.frag_start_anim, R.anim.frag_stop_anim)
                .replace(R.id.fragment, EulaFragment::class.java, null).commit()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.frag_start_anim, R.anim.frag_stop_anim)
                .replace(R.id.fragment, RuntimeFragment::class.java, null).commit()
        }
    }


    fun enterLauncher() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                RendererManager.init(this@SplashActivity)
                JavaManager.init()
                runCatching { ConfigHolder.init() }.exceptionOrNull()?.let {
                    Logging.LOG.log(Level.WARNING, it.message)
                }
            }
            startActivity(
                Intent(this@SplashActivity, MainActivity::class.java),
                ActivityOptionsCompat.makeCustomAnimation(this@SplashActivity, 0, 0).toBundle()
            )
            finish()
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = "package:$packageName".toUri()
                    activityResultLauncher.launch(this)
                }
            } catch (_: Exception) {
                activityResultLauncher.launch(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
            }
        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    permission.WRITE_EXTERNAL_STORAGE
                ) || !ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    permission.READ_EXTERNAL_STORAGE
                )
            ) {
                permissionResultLauncher.launch(
                    arrayOf(
                        permission.WRITE_EXTERNAL_STORAGE,
                        permission.READ_EXTERNAL_STORAGE
                    )
                )
            } else {
                Toast.makeText(this, R.string.splash_permission_settings_msg, Toast.LENGTH_LONG)
                    .show()
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = "package:$packageName".toUri()
                    activityResultLauncher.launch(this)
                }
            }
        }
    }

    private fun hasPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager()
        }
        return ContextCompat.checkSelfPermission(
            this,
            permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initState() {
        try {
            lwjgl = RuntimeUtils.isLatest(
                FCLPath.LWJGL_DIR,
                "/assets/app_runtime/lwjgl"
            ) && RuntimeUtils.isLatest(
                FCLPath.LWJGL_DIR + "-boat",
                "/assets/app_runtime/lwjgl-boat"
            )
            cacio = RuntimeUtils.isLatest(
                FCLPath.CACIOCAVALLO_8_DIR,
                "/assets/app_runtime/caciocavallo"
            )
            cacio17 = RuntimeUtils.isLatest(
                FCLPath.CACIOCAVALLO_17_DIR,
                "/assets/app_runtime/caciocavallo17"
            )
            java8 = RuntimeUtils.isLatest(FCLPath.JAVA_8_PATH, "/assets/app_runtime/java/jre8")
            java11 = RuntimeUtils.isLatest(FCLPath.JAVA_11_PATH, "/assets/app_runtime/java/jre11")
            java17 = RuntimeUtils.isLatest(FCLPath.JAVA_17_PATH, "/assets/app_runtime/java/jre17")
            java21 = RuntimeUtils.isLatest(FCLPath.JAVA_21_PATH, "/assets/app_runtime/java/jre21")
            jna = RuntimeUtils.isLatest(FCLPath.JNA_PATH, "/assets/app_runtime/jna")
            if (!File(FCLPath.JAVA_PATH, "resolv.conf").exists()) {
                if (LocaleUtils.getSystemLocale().displayName != Locale.CHINA.displayName) {
                    FileUtils.writeText(
                        File(FCLPath.JAVA_PATH + "/resolv.conf"), """
     nameserver 1.1.1.1
     nameserver 1.0.0.1
     """.trimIndent()
                    )
                } else {
                    FileUtils.writeText(
                        File(FCLPath.JAVA_PATH + "/resolv.conf"), """
     nameserver 8.8.8.8
     nameserver 8.8.4.4
     """.trimIndent()
                    )
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
