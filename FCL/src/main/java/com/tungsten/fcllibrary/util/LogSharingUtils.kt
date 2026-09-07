package com.tungsten.fcllibrary.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.mio.util.showErrorDialog
import com.tungsten.fclcore.util.Pair.pair
import com.tungsten.fclcore.util.io.HttpRequest
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.ui.ProgressDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.nio.file.Files

fun uploadLog(activity: AppCompatActivity, log: String) {
    val progress = ProgressDialog(activity)
    val url = getLogUploadApiUrl(activity)
    activity.lifecycleScope.launch(Dispatchers.Default) {
        val result = runCatching {
            HttpRequest.POST(url)
                .form(pair("content", log))
                .string
        }
        withContext(Dispatchers.Main) {
            result.onSuccess {
                progress.dismiss()
                try {
                    val response = JSONObject(it)
                    if (response.getBoolean("success")) {
                        val logUrl = response.getString("url")
                        showLogUploadSuccessDialog(activity, logUrl)
                    } else {
                        showErrorDialog(
                            activity,
                            R.string.upload_failed,
                            response.getString("error")
                        )
                    }
                } catch (ex: Exception) {
                    showErrorDialog(
                        activity,
                        R.string.upload_failed,
                        "${ex.toString()}\n$it"
                    )
                }
            }.onFailure {
                progress.dismiss()
                showErrorDialog(
                    activity,
                    R.string.upload_failed,
                    it.toString()
                )
            }
        }
    }
}

/**
 * 分享日志文件。合并写入时会从日志内容中自动查找 JVM 崩溃报告 hs_err 一并附带，
 * 并对 --accessToken 后的 token 做脱敏（与 latest_game.log 的 *** 一致）。
 */
fun shareLogFile(activity: AppCompatActivity, file: File) {
    if (!file.exists()) return
    try {
        val merged = Files.createTempFile("fcl-latest-",".log").toFile()
        merged.bufferedWriter(Charsets.UTF_8).use { writer ->
            var hsErrFile: File? = null
            file.bufferedReader(Charsets.UTF_8).useLines { lines ->
                lines.forEach { line ->
                    if (hsErrFile == null) {
                        findFatalErrorLogPath(line)?.let { hsErrFile = File(it) }
                    }
                    writer.write(maskAccessToken(line))
                    writer.newLine()
                }
            }
            hsErrFile?.takeIf { it.exists() }?.let { hsErr ->
                writer.newLine()
                hsErr.bufferedReader(Charsets.UTF_8).useLines { lines ->
                    lines.forEach { line ->
                        writer.write(maskAccessToken(line))
                        writer.newLine()
                    }
                }
            }
        }
        val uri = FileProvider.getUriForFile(activity, "${activity.packageName}.provider", merged)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        activity.startActivity(
            Intent.createChooser(
                intent,
                activity.getString(R.string.crash_reporter_share)
            )
        )
    } catch (e: Exception) {
        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
    }
}

/**
 * 从日志内容中解析 JVM 崩溃时打印的 hs_err 报告路径。
 */
fun findFatalErrorLogPath(log: String): String? {
    val pattern = Regex("^\\s*#?\\s*(.*hs_err_pid\\d+\\.log.*)\\s*$", RegexOption.MULTILINE)
    return pattern.find(log)?.groupValues?.get(1)?.trim()
}

private val ACCESS_TOKEN_REGEX = Regex("--accessToken(?:\\s+|\\s*=\\s*)\\S+")

private fun maskAccessToken(line: String): String =
    line.replace(ACCESS_TOKEN_REGEX, "--accessToken ***")

private fun showLogUploadSuccessDialog(context: Context, url: String) {
    FCLAlertDialog.Builder(context)
        .setTitle(context.getString(R.string.log_upload_success_title))
        .setMessage(url)
        .setPositiveButton(
            context.getString(R.string.log_upload_copy)
        ) { copyToClipboard(context, url) }
        .setNeutralButton(
            context.getString(R.string.log_upload_open)
        ) { openInBrowser(context, url) }
        .setExtraButton(
            context.getString(R.string.log_upload_share)
        ) { shareLog(context, url) }
        .setNegativeButton(context.getString(R.string.dialog_negative), null)
        .create()
        .show()
}

private fun getLogUploadApiUrl(context: Context): String {
    return if (LocaleUtils.isChinese(context)) {
        "https://api.logshare.cn/1/log"
    } else {
        "https://api.mclo.gs/1/log"
    }
}

private fun copyToClipboard(context: Context, text: String?) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    clipboard?.let {
        val clip = ClipData.newPlainText(null, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(
            context,
            context.getString(R.string.crash_reporter_toast),
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun openInBrowser(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
}

private fun shareLog(context: Context, url: String) {
    try {
        val shareText = context.getString(R.string.log_upload_share_template, url)
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, shareText)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.crash_reporter_share)
            )
        )
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
}
