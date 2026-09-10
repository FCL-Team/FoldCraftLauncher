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
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.mio.JavaManager
import com.mio.manager.RendererManager
import com.mio.util.ImageUtil
import com.mio.util.getFileName
import com.mio.util.getSystemDnsServerAddresses
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ActivitySplashBinding
import com.tungsten.fcl.fragment.EulaFragment
import com.tungsten.fcl.fragment.RuntimeFragment
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.setting.Controllers
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

@SuppressLint("CustomSplashScreen")
class SplashActivity : FCLActivity() {
    var lwjgl: Boolean = false
    var cacio: Boolean = false
    var cacio17: Boolean = false
    var java8: Boolean = false
    var java17: Boolean = false
    var java21: Boolean = false
    var java25: Boolean = false
    var jna: Boolean = false
    lateinit var binding: ActivitySplashBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("launcher", MODE_PRIVATE)
        setContentView(binding.root)
        ImageUtil.loadInto(
            binding.background, ThemeEngine.getInstance().getTheme().getBackground(this)
        )
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
                setNegativeButton(getString(R.string.crash_reporter_close)) { finish() }
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
            if (lwjgl && cacio && cacio17 && java8 && java17 && java21 && java25 && jna) {
                enterLauncher()
            } else {
                start()
            }
        }
    }

    fun start() {
        // init 协程可能在 Activity 转后台（onSaveInstanceState 之后）才恢复，Splash 流程无需保留事务状态，允许状态丢失
        if (sharedPreferences.getBoolean("isFirstLaunch", true)) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.frag_start_anim, R.anim.frag_stop_anim)
                .replace(R.id.fragment, EulaFragment::class.java, null).commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.frag_start_anim, R.anim.frag_stop_anim)
                .replace(R.id.fragment, RuntimeFragment::class.java, null)
                .commitAllowingStateLoss()
        }
    }

    fun enterLauncher() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                RendererManager.init(this@SplashActivity)
                JavaManager.init()
                Controllers.init()
                runCatching { ConfigHolder.init() }.exceptionOrNull()?.let {
                    Logging.LOG.log(Level.WARNING, it.message)
                }
                if (System.currentTimeMillis() - sharedPreferences.getLong(
                        "clear_cache", 0L
                    ) >= 3 * 1000 * 60 * 60 * 24
                ) {
                    FileUtils.cleanDirectoryQuietly(File(FCLPath.CACHE_DIR).getParentFile())
                    sharedPreferences.edit {
                        putLong("clear_cache", System.currentTimeMillis())
                    }
                }
            }
            startActivity(
                handleModpack(Intent(this@SplashActivity, MainActivity::class.java)),
                ActivityOptionsCompat.makeCustomAnimation(this@SplashActivity, 0, 0).toBundle()
            )
            finish()
        }
    }

    private fun handleModpack(newIntent: Intent): Intent {
        val intent = intent
        val action = intent.action
        val data = intent.data

        if (Intent.ACTION_VIEW == action && data != null) {
            try {
                val fileName = getFileName(this, data)
                val cacheFile = File(cacheDir, fileName)
                contentResolver.openInputStream(data)?.use { input ->
                    cacheFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                newIntent.putExtra("modpack_cache_path", cacheFile.absolutePath)
            } catch (e: Exception) {
                Logging.LOG.log(
                    Level.WARNING, "Failed to handle modpack intent: ${e.message}"
                )
            }
        }
        return newIntent
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = "package:$packageName".toUri()
                    startActivityForResult(this) {
                        checkPermission()
                    }
                }
            } catch (_: Exception) {
                startActivityForResult(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)) {
                    checkPermission()
                }
            }
        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this, permission.WRITE_EXTERNAL_STORAGE
                ) || !ActivityCompat.shouldShowRequestPermissionRationale(
                    this, permission.READ_EXTERNAL_STORAGE
                )
            ) {
                requestPermissions(
                    arrayOf(
                        permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    checkPermission()
                }
            } else {
                Toast.makeText(this, R.string.splash_permission_settings_msg, Toast.LENGTH_LONG)
                    .show()
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = "package:$packageName".toUri()
                    startActivityForResult(this) {
                        checkPermission()
                    }
                }
            }
        }
    }

    private fun hasPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager()
        }
        return ContextCompat.checkSelfPermission(
            this, permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this, permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initState() {
        try {
            lwjgl = RuntimeUtils.isLatest(
                FCLPath.LWJGL_DIR + "/3.3.3",
                "/assets/app_runtime/lwjgl/3.3.3"
            ) && RuntimeUtils.isLatest(
                FCLPath.LWJGL_DIR + "/3.4.1",
                "/assets/app_runtime/lwjgl/3.4.1"
            )
            cacio = RuntimeUtils.isLatest(
                FCLPath.CACIOCAVALLO_8_DIR, "/assets/app_runtime/caciocavallo"
            )
            cacio17 = RuntimeUtils.isLatest(
                FCLPath.CACIOCAVALLO_17_DIR, "/assets/app_runtime/caciocavallo17"
            )
            java8 = RuntimeUtils.isLatest(FCLPath.JAVA_8_PATH, "/assets/app_runtime/java/jre8")
            java17 = RuntimeUtils.isLatest(FCLPath.JAVA_17_PATH, "/assets/app_runtime/java/jre17")
            java21 = RuntimeUtils.isLatest(FCLPath.JAVA_21_PATH, "/assets/app_runtime/java/jre21")
            java25 = RuntimeUtils.isLatest(FCLPath.JAVA_25_PATH, "/assets/app_runtime/java/jre25")
            jna = RuntimeUtils.isLatest(FCLPath.JNA_PATH, "/assets/app_runtime/jna")
            val resolvFile = File(FCLPath.JAVA_PATH, "resolv.conf")
            val servers = buildSet {
                getSystemDnsServerAddresses()
                    // JNDI DNS 的 nameserver 解析无法处理裸 IPv6 地址，仅保留 IPv4
                    ?.filterNot { it.contains(':') }
                    ?.let { addAll(it) }

                // 按地区获取公共 DNS
                if (LocaleUtils.getSystemLocale().displayName != Locale.CHINA.displayName) {
                    add("1.1.1.1")
                    add("1.0.0.1")
                } else {
                    add("223.5.5.5")
                    add("119.29.29.29")
                }
            }
            Logging.LOG.log(Level.INFO, "Using DNS servers for game: $servers")
            val configText = servers.joinToString(separator = "\n") { "nameserver $it" }
            runCatching {
                // 配置文件不存在或内容不一致时覆写一次
                if (!resolvFile.exists() || resolvFile.readText().trim() != configText.trim()) {
                    resolvFile.writeText(configText)
                }
            }.onFailure {
                Logging.LOG.log(Level.WARNING, "Failed to create resolv.conf", it)
                resolvFile.delete()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
