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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mio.JavaManager
import com.tungsten.fcl.R
import com.tungsten.fcl.fragment.EulaFragment
import com.tungsten.fcl.fragment.RuntimeFragment
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.util.RequestCodes
import com.tungsten.fcl.util.RuntimeUtils
import com.tungsten.fclauncher.plugins.DriverPlugin
import com.tungsten.fclauncher.plugins.RendererPlugin
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.component.ResultListener
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog.ButtonListener
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.LocaleUtils
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.Locale
import java.util.logging.Level
import kotlin.system.exitProcess

@SuppressLint("CustomSplashScreen")
class SplashActivity : FCLActivity() {

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
        background.background = ThemeEngine.getInstance().getTheme().getBackground(this)

        checkPermission()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                init()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.setData(Uri.parse("package:$packageName"))
                ResultListener.startActivityForResult(
                    this,
                    intent,
                    RequestCodes.PERMISSION_REQUEST_CODE,
                    ResultListener.Listener { requestCode: Int, resultCode: Int, data: Intent? ->
                        if (requestCode == RequestCodes.PERMISSION_REQUEST_CODE) {
                            if (Environment.isExternalStorageManager()) {
                                init()
                            } else {
                                recheckPermission()
                            }
                        }
                    })
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                init()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        permission.WRITE_EXTERNAL_STORAGE
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    enableAlertDialog(
                        ButtonListener {
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf<String>(
                                    permission.WRITE_EXTERNAL_STORAGE,
                                    permission.READ_EXTERNAL_STORAGE
                                ),
                                RequestCodes.PERMISSION_REQUEST_CODE
                            )
                        },
                        getString(R.string.splash_permission_title),
                        getString(R.string.splash_permission_msg),
                        getString(R.string.splash_permission_grant),
                        getString(R.string.splash_permission_close)
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf<String>(
                            permission.WRITE_EXTERNAL_STORAGE,
                            permission.READ_EXTERNAL_STORAGE
                        ),
                        RequestCodes.PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    private fun recheckPermission() {
        val builder = FCLAlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
        builder.setMessage(getString(R.string.splash_permission_msg))
        builder.setPositiveButton(ButtonListener { this.checkPermission() })
        builder.setNegativeButton(ButtonListener { this.finish() })
        builder.create().show()
    }

    private fun init() {
        FCLPath.loadPaths(this)
        transFile()
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

    private fun transFile() {
        try {
            val controlDir = Paths.get(FCLPath.FILES_DIR + "/control")
            if (controlDir.toFile().exists()) {
                FileUtils.copyDirectory(controlDir, Paths.get(FCLPath.CONTROLLER_DIR))
                FileUtils.deleteDirectory(controlDir.toFile())
            }
        } catch (_: IOException) {
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0, Color.TRANSPARENT)
            } else {
                overridePendingTransition(0, 0)
            }
            finish()
        }.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RequestCodes.PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        permission.WRITE_EXTERNAL_STORAGE
                    ) || !ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    enableAlertDialog(
                        ButtonListener {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.setData(uri)
                            ResultListener.startActivityForResult(
                                this,
                                intent,
                                RequestCodes.PERMISSION_REQUEST_CODE,
                                ResultListener.Listener { requestCode1: Int, resultCode: Int, data: Intent? ->
                                    if (requestCode1 == RequestCodes.PERMISSION_REQUEST_CODE) {
                                        if (ActivityCompat.checkSelfPermission(
                                                this,
                                                permission.READ_EXTERNAL_STORAGE
                                            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                                                this,
                                                permission.WRITE_EXTERNAL_STORAGE
                                            ) == PackageManager.PERMISSION_GRANTED
                                        ) {
                                            init()
                                        } else {
                                            onRequestPermissionsResult(
                                                requestCode1,
                                                permissions,
                                                grantResults
                                            )
                                        }
                                    }
                                })
                        },
                        getString(R.string.splash_permission_title),
                        getString(R.string.splash_permission_settings_msg),
                        getString(R.string.splash_permission_settings),
                        getString(R.string.splash_permission_close)
                    )
                } else {
                    checkPermission()
                }
            }
        }
    }

    private fun enableAlertDialog(positiveButtonEvent: ButtonListener?, vararg strings: String?) {
        FCLAlertDialog.Builder(this)
            .setTitle(strings[0].toString())
            .setMessage(strings[1].toString())
            .setPositiveButton(strings[2].toString(), positiveButtonEvent)
            .setNegativeButton(strings[3].toString(), ButtonListener { exitProcess(0) })
            .setCancelable(false)
            .create()
            .show()
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
