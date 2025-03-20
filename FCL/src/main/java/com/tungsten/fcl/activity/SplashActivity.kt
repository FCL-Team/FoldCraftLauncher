package com.tungsten.fcl.activity

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
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
import com.mio.JavaManager
import com.mio.util.ImageUtil
import com.tungsten.fcl.R
import com.tungsten.fcl.fragment.EulaFragment
import com.tungsten.fcl.fragment.RuntimeFragment
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.util.RuntimeUtils
import com.tungsten.fclauncher.plugins.DriverPlugin
import com.tungsten.fclauncher.plugins.RendererPlugin
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog.ButtonListener
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.LocaleUtils
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.Locale
import java.util.logging.Level

@SuppressLint("CustomSplashScreen")
class SplashActivity : FCLActivity() {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionResultLauncher: ActivityResultLauncher<Array<String>>
    var lwjgl: Boolean = false
    var cacio: Boolean = false
    var cacio11: Boolean = false
    var cacio17: Boolean = false
    var java8: Boolean = false
    var java11: Boolean = false
    var java17: Boolean = false
    var java21: Boolean = false
    var jna: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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
        checkPermission()
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
            setPositiveButton(ButtonListener { requestPermission() })
            setNegativeButton(ButtonListener { finish() })
            create().show()
        }
    }

    private fun init() {
        FCLPath.loadPaths(this)
        Logging.start(Paths.get(FCLPath.LOG_DIR))
        Task.runAsync {
            initState()
        }.whenComplete(Schedulers.androidUIThread()) {
            if (lwjgl && cacio && cacio11 && cacio17 && java8 && java11 && java17 && java21 && jna) {
                enterLauncher()
            } else {
                start()
            }
        }.start()
    }

    fun start() {
        val sharedPreferences = getSharedPreferences("launcher", MODE_PRIVATE)
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
        Task.runAsync {
            RendererPlugin.init(this)
            DriverPlugin.init(this)
            JavaManager.init()
            try {
                ConfigHolder.init()
            } catch (e: IOException) {
                Logging.LOG.log(Level.WARNING, e.message)
            }
        }.whenComplete(Schedulers.androidUIThread()) {
            startActivity(
                Intent(this, MainActivity::class.java),
                ActivityOptionsCompat.makeCustomAnimation(this, 0, 0).toBundle()
            )
            finish()
        }.start()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.parse("package:$packageName")
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
                    data = Uri.parse("package:$packageName")
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
            cacio11 = RuntimeUtils.isLatest(
                FCLPath.CACIOCAVALLO_11_DIR,
                "/assets/app_runtime/caciocavallo11"
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
