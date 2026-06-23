package com.tungsten.fcl.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.View
import android.view.WindowManager
import androidx.core.content.FileProvider
import com.mio.util.showErrorDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ActivityJvmCrashBinding
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.util.uploadLog
import java.io.File
import java.io.IOException
import java.util.Arrays
import java.util.logging.Level
import java.util.stream.Collectors
import kotlin.system.exitProcess

class JVMCrashActivity : FCLActivity(), View.OnClickListener {
    private var exitCode = 0
    private lateinit var logPath: String
    private lateinit var binding: ActivityJvmCrashBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJvmCrashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!getSharedPreferences("launcher", MODE_PRIVATE).getBoolean("allowScreenshots", false)) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }

        binding.restart.setOnClickListener(this)
        binding.close.setOnClickListener(this)
        binding.upload.setOnClickListener(this)
        binding.share.setOnClickListener(this)


        val game = intent.extras!!.getBoolean("isGame")
        exitCode = intent.extras!!.getInt("exitCode")
        logPath = intent.extras!!.getString("logPath").toString()

        binding.title.text =
            if (game) getString(R.string.game_crash_title) + getString(R.string.game_crash_title_add) else getString(
                R.string.jar_executor_crash_title
            )

        try {
            init()
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, "Failed to read log file", e)
            binding.error.text = e.message
        }
    }

    @Throws(IOException::class)
    private fun init() {
        var summarize = "Exit Normally, exit code = $exitCode"
        val log = readLog()
        val error = findFabricIncompatibleModsError(log)
        error?.let {
            showErrorDialog(this@JVMCrashActivity, it)
        }
        val errorLines =
            Arrays.stream(log.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()).collect(
                Collectors.toList()
            )
        if (exitCode != 0 && StringUtils.containsOne(
                errorLines,
                "Could not create the Java Virtual Machine.",
                "Error occurred during initialization of VM",
                "A fatal exception has occurred. Program will exit."
            )
        ) {
            summarize = "JVM launch failed, exit code = $exitCode"
        } else if (exitCode != 0 || StringUtils.containsOne(errorLines, "Unable to launch")) {
            summarize = "Application error, unable to launch, exit code = $exitCode"
        }
        errorLines.add(0, "")
        errorLines.add(0, "")
        errorLines.add(0, "Summarize: $summarize")
        errorLines.forEach { it: String? -> binding.error.append(it + "\n") }

    }

    override fun onClick(v: View?) {
        if (v === binding.restart) {
            val intent = Intent(this, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            if (intent.component != null) {
                intent.setAction(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
            }
            finish()
            startActivity(intent)
            Process.killProcess(Process.myPid())
            exitProcess(10)
        }
        if (v === binding.close) {
            finish()
            Process.killProcess(Process.myPid())
            exitProcess(10)
        }
        if (v === binding.upload) {
            try {
                uploadLog(this, readLog())
            } catch (e: IOException) {
                showErrorDialog(this, R.string.upload_failed, e.message)
            }
        }
        if (v === binding.share) {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                val file = File.createTempFile("fcl-latest", ".log")
                file.delete()
                FileUtils.copyFile(File(logPath), file)
                val uri = FileProvider.getUriForFile(
                    this,
                    application.packageName + ".provider",
                    file
                )
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(
                    Intent.createChooser(
                        intent,
                        getString(com.tungsten.fcllibrary.R.string.crash_reporter_share)
                    )
                )
            } catch (e: Exception) {
                Logging.LOG.log(Level.INFO, "Share error: $e")
            }
        }
    }

    @Throws(IOException::class)
    private fun readLog(): String {
        if (File(logPath).length() < 8 * 1024 * 1024) {
            return FileUtils.readText(File(logPath))
        }
        throw IOException("Log file is too large, please check the log file manually.")
    }

    fun findFabricIncompatibleModsError(text: String): String? {
        val pattern = Regex(
            """start net\.fabricmc\.loader\.impl\.gui\.FabricGuiEntry\.displayError(.*?)end net\.fabricmc\.loader\.impl\.gui\.FabricGuiEntry\.displayError""",
            RegexOption.DOT_MATCHES_ALL
        )
        return pattern.find(text)?.groupValues?.get(1)
    }

    companion object {
        @JvmStatic
        fun startCrashActivity(game: Boolean, context: Context, exitCode: Int, logPath: String?) {
            val intent = Intent(context, JVMCrashActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean("isGame", game)
            bundle.putInt("exitCode", exitCode)
            bundle.putString("logPath", logPath)
            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
