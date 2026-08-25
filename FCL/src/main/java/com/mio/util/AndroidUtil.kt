package com.mio.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.GLES20
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.MotionEvent
import android.webkit.CookieManager
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.WebActivity
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fclcore.util.io.IOUtils
import net.fornwall.jelf.ElfFile
import java.io.DataInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level
import java.util.zip.ZipFile

@SuppressLint("DiscouragedApi")
fun getElfArchFromSo(filePath: String): String {
    RandomAccessFile(filePath, "r").use { file ->
        val magic = ByteArray(4)
        file.readFully(magic)
        if (magic[0] != 0x7F.toByte() || magic[1] != 'E'.code.toByte()
            || magic[2] != 'L'.code.toByte() || magic[3] != 'F'.code.toByte()
        ) {
            return ""
        }
        file.seek(0x05)
        val eiData = file.readByte().toInt() and 0xFF
        if (eiData !in listOf(1, 2)) {
            return ""
        }
        file.seek(0x12)
        val eMachineBytes = ByteArray(2)
        file.readFully(eMachineBytes)
        val buffer = ByteBuffer.wrap(eMachineBytes).apply {
            order(if (eiData == 1) ByteOrder.LITTLE_ENDIAN else ByteOrder.BIG_ENDIAN)
        }
        val eMachine = buffer.short.toInt() and 0xFFFF
        return when (eMachine) {
            0x03 -> "x86"
            0x3E -> "x86_64"
            0x28 -> "ARM"
            0xB7 -> "AArch64"
            0x08 -> "MIPS"
            0xF3 -> "RISC-V"
            0x2A -> "SuperH"
            0x32 -> "IA-64"
            else -> ""
        }
    }
}

fun checkElfIsAndroid(file: File): Boolean {
    val elfFile = ElfFile.from(file)
    var isAndroid = true
    elfFile.dynamicSection.neededLibraries.forEach {
        if (Regex("lib[^.]+\\.so\\.\\d+").matches(it)) {
            isAndroid = false
        }
    }
    return isAndroid
}

fun getElfArchFromZip(zipFile: File, elfEntryPath: String): String {
    var arch = ""
    try {
        ZipFile(zipFile).let { zip ->
            val entry = zip.entries().toList().find { it.name == elfEntryPath }
            if (entry == null || entry.isDirectory) {
                return@let
            }
            zip.getInputStream(entry).let { stream ->
                DataInputStream(stream).let { dataStream ->
                    val magic = ByteArray(4)
                    dataStream.readFully(magic)
                    if (!(magic[0] == 0x7F.toByte() && magic[1] == 'E'.code.toByte() &&
                                magic[2] == 'L'.code.toByte() && magic[3] == 'F'.code.toByte())
                    ) {
                        return@let
                    }
                    val eIdentRest = ByteArray(12)
                    dataStream.readFully(eIdentRest)
                    val eiData = eIdentRest[1].toInt()
                    dataStream.skipBytes(2)
                    val machineBytes = ByteArray(2)
                    dataStream.readFully(machineBytes)
                    val byteOrder = when (eiData) {
                        1 -> ByteOrder.LITTLE_ENDIAN
                        2 -> ByteOrder.BIG_ENDIAN
                        else -> ByteOrder.LITTLE_ENDIAN
                    }
                    val machineType = ByteBuffer.wrap(machineBytes)
                        .order(byteOrder)
                        .short.toInt() and 0xFFFF
                    arch = when (machineType) {
                        0x03 -> "x86"
                        0x3E -> "x86_64"
                        0x28 -> "ARM"
                        0xB7 -> "AArch64"
                        0x08 -> "MIPS"
                        0xF3 -> "RISC-V"
                        0x2A -> "SPARC"
                        0x18 -> "ARM64"
                        else -> ""
                    }
                }
            }
        }
    } catch (_: Exception) {
    }
    return arch
}

private fun getMemoryInfo(context: Context): ActivityManager.MemoryInfo {
    return ActivityManager.MemoryInfo().apply {
        ((context.getSystemService(Context.ACTIVITY_SERVICE)) as ActivityManager).getMemoryInfo(this)
    }
}

fun getTotalMemory(context: Context): Long {
    return getMemoryInfo(context).totalMem
}

fun getUsedMemory(context: Context): Long {
    val info = getMemoryInfo(context)
    return info.totalMem - info.availMem
}

fun copyToClipBoard(context: Context, text: String) {
    val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clip.setPrimaryClip(ClipData.newPlainText("", text))
}

fun openLink(context: Context, link: String) {
    val uri = link.toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    val componentName = intent.resolveActivity(context.packageManager)
    if (componentName != null) {
        context.startActivity(Intent.createChooser(intent, ""))
    } else {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("FCL Clipboard", link))
        Toast.makeText(context, context.getString(R.string.open_link_failed), Toast.LENGTH_LONG)
            .show()
    }
}

fun openLinkWithBuiltinWebView(context: Context, link: String) {
    val intent = Intent(context, WebActivity::class.java)
    val bundle = Bundle()
    bundle.putString("url", link)
    intent.putExtras(bundle)
    context.startActivity(intent)
}

fun copyText(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(null, text))
    Toast.makeText(context, context.getString(R.string.message_copy), Toast.LENGTH_SHORT).show()
}

fun clearWebViewCache(context: Context) {
    val cache = context.getDir("webview", 0)
    FileUtils.deleteDirectoryQuietly(cache)
    CookieManager.getInstance().removeAllCookies(null)
}

fun getLocalizedText(context: Context, key: String, vararg formatArgs: Any): String {
    return String.format(getLocalizedText(context, key), formatArgs)
}

@SuppressLint("DiscouragedApi")
fun getLocalizedText(context: Context, key: String): String {
    val resId = context.resources.getIdentifier(key, "string", context.packageName)
    return if (resId != 0) context.getString(resId) else key
}

/** 字符串资源 ID 存在性缓存（getIdentifier 查询开销大，key 集合有限） */
private val stringIdCache = ConcurrentHashMap<String, Boolean>()

@SuppressLint("DiscouragedApi")
fun hasStringId(context: Context, key: String): Boolean {
    return stringIdCache.computeIfAbsent(key) { k ->
        context.resources.getIdentifier(k, "string", context.packageName) != 0
    }
}

fun getScreenHeight(): Int {
    if (DisplayUtil.screenHeight != -1) return DisplayUtil.screenHeight
    return DisplayUtil.currentDisplayMetrics.heightPixels
}

fun getScreenWidth(): Int {
    if (DisplayUtil.screenWidth != -1) return DisplayUtil.screenWidth
    return DisplayUtil.currentDisplayMetrics.widthPixels
}

fun getMimeType(filePath: String): String {
    val mmr = MediaMetadataRetriever()
    var mime = "*/*"
    try {
        mmr.setDataSource(filePath)
        mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE) ?: "*/*"
    } catch (_: RuntimeException) {
        return mime
    }
    return mime
}

fun copyFileToDir(activity: Activity, uri: Uri, destDir: File): String {
    val name = getFileName(activity, uri)
    val dest = File(destDir, name)
    try {
        activity.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(dest).use { output ->
                IOUtils.copyTo(input, output)
            }
        }
    } catch (_: Exception) {
    }
    return dest.absolutePath
}

fun copyFile(context: Context, uri: Uri, dest: File) {
    try {
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(dest).use { output ->
                IOUtils.copyTo(input, output)
            }
        }
    } catch (_: Exception) {
    }
}

fun isDocUri(uri: Uri): Boolean {
    return uri.scheme == ContentResolver.SCHEME_FILE || uri.scheme == ContentResolver.SCHEME_CONTENT
}

fun getFileName(context: Context, uri: Uri): String {
    val cursor =
        context.contentResolver.query(uri, null, null, null, null) ?: return uri.lastPathSegment
            ?: ""
    cursor.use {
        it.moveToFirst()
        val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        return if (columnIndex == -1) {
            uri.lastPathSegment ?: ""
        } else {
            it.getString(columnIndex) ?: uri.lastPathSegment ?: ""
        }
    }
}

fun isAdrenoGPU(): Boolean {
    val eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
    if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
        Logging.LOG.log(Level.SEVERE, "CheckVendor: Failed to get EGL display")
        return false
    }

    if (!EGL14.eglInitialize(eglDisplay, null, 0, null, 0)) {
        Logging.LOG.log(Level.SEVERE, "CheckVendor: Failed to initialize EGL")
        return false
    }

    val eglAttributes = intArrayOf(
        EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
        EGL14.EGL_NONE
    )

    val configs = arrayOfNulls<EGLConfig>(1)
    val numConfigs = IntArray(1)
    if (!EGL14.eglChooseConfig(
            eglDisplay,
            eglAttributes,
            0,
            configs,
            0,
            1,
            numConfigs,
            0
        ) || numConfigs[0] == 0
    ) {
        EGL14.eglTerminate(eglDisplay)
        Logging.LOG.log(Level.SEVERE, "CheckVendor: Failed to choose an EGL config")
        return false
    }

    val contextAttributes = intArrayOf(
        EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
        EGL14.EGL_NONE
    )

    val context =
        EGL14.eglCreateContext(eglDisplay, configs[0], EGL14.EGL_NO_CONTEXT, contextAttributes, 0)
    if (context == EGL14.EGL_NO_CONTEXT) {
        EGL14.eglTerminate(eglDisplay)
        Logging.LOG.log(Level.SEVERE, "CheckVendor: Failed to create EGL context")
        return false
    }

    if (!EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, context)) {
        EGL14.eglDestroyContext(eglDisplay, context)
        EGL14.eglTerminate(eglDisplay)
        Logging.LOG.log(Level.SEVERE, "CheckVendor: Failed to make EGL context current")
        return false
    }

    val vendor = GLES20.glGetString(GLES20.GL_VENDOR)
    val renderer = GLES20.glGetString(GLES20.GL_RENDERER)
    val isAdreno = (vendor != null && renderer != null &&
            vendor.equals("Qualcomm", ignoreCase = true) &&
            renderer.lowercase().contains("adreno"))

    EGL14.eglMakeCurrent(
        eglDisplay,
        EGL14.EGL_NO_SURFACE,
        EGL14.EGL_NO_SURFACE,
        EGL14.EGL_NO_CONTEXT
    )
    EGL14.eglDestroyContext(eglDisplay, context)
    EGL14.eglTerminate(eglDisplay)
    Logging.LOG.log(Level.SEVERE, "CheckVendor: Running on Adreno GPU:$isAdreno")
    return isAdreno
}

/**
 * 禁止鼠标滚轮翻页。
 * ViewPager2 为 final 类无法继承，而滚轮事件由其内部 RecyclerView 处理，
 * 因此给该子视图挂通用运动事件监听消费滚轮；
 * 触摸滑动、tab 切换与程序化滚动不受影响，页面内列表的滚轮滚动也照常工作。
 * 需在 ViewPager2 构造/inflate 之后调用（内部 RecyclerView 在构造时即已挂载）。
 */
fun ViewPager2.disableMouseWheelScroll() {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is RecyclerView) {
            child.setOnGenericMotionListener { _, event ->
                event.action == MotionEvent.ACTION_SCROLL
            }
        }
    }
}