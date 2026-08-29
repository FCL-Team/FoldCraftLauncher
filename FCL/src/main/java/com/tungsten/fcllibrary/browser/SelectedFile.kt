package com.tungsten.fcllibrary.browser

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.tungsten.fclcore.util.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * 统一的文件选择结果：既可能是本地文件/目录路径，也可能是 content provider 提供的 content:// uri。
 * 调用方无需区分来源，按需调用对应方法即可。
 */
class SelectedFile(val uri: Uri) {

    /** 是否为 content provider 提供的内容（授权是临时的，需要立即读取或拷贝） */
    val isContent: Boolean
        get() = uri.scheme == ContentResolver.SCHEME_CONTENT

    /** 本地文件/目录，content 时为 null */
    val file: File?
        get() = if (isContent) null else File(uri.path ?: uri.toString())

    /** 原始字符串（本地路径或 content uri） */
    val path: String
        get() = uri.toString()

    /** 文件名（不含目录部分） */
    fun fileName(context: Context): String {
        if (!isContent) return file?.name ?: ""
        val cursor = context.contentResolver.query(uri, null, null, null, null)
            ?: return uri.lastPathSegment ?: ""
        cursor.use {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index == -1 || !it.moveToFirst()) {
                return uri.lastPathSegment ?: ""
            }
            return it.getString(index) ?: uri.lastPathSegment ?: ""
        }
    }

    /** 打开输入流读取内容 */
    fun openInputStream(context: Context): InputStream? {
        return if (isContent) {
            context.contentResolver.openInputStream(uri)
        } else {
            file?.inputStream()
        }
    }

    /**
     * 获取真实文件：本地路径直接返回；content 拷贝到 cacheDir 后返回拷贝结果
     */
    fun toFile(context: Context, cacheDir: File): File {
        file?.let { return it }
        val dest = File(cacheDir, fileName(context))
        copyTo(context, dest)
        return dest
    }

    /** 拷贝内容到指定文件（需要固定文件名时使用） */
    fun copyTo(context: Context, dest: File) {
        try {
            openInputStream(context)?.use { input ->
                FileOutputStream(dest).use { output ->
                    IOUtils.copyTo(input, output)
                }
            }
        } catch (_: Exception) {
        }
    }
}
