package com.tungsten.fcl.util

import android.content.Context
import com.tungsten.fclauncher.utils.Architecture
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.NetworkUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.logging.Level
import java.util.zip.ZipFile

/**
 * JNA natives 兜底。MC 升级引入 assets 未携带的 JNA 版本时（如 26.4 快照的 5.19.1），
 * jna.boot.library.path 指向的 RUNTIME_DIR/jna/<版本> 会缺失 libjnidispatch.so，
 * JNA 转而从 jar 内抽取 glibc 链接的桌面版 so，在 Android 上 dlopen 必然失败。
 * 此处保证该 so 存在：优先复制 APK 内 assets（应对未触发重装的升级），缺失再从
 * Maven Central 的 jna-<版本>.aar 提取当前架构 natives。全程失败仅记日志，不阻塞启动。
 */
object JnaNatives {

    private const val AAR_PATH = $$"net/java/dev/jna/jna/%1$s/jna-%1$s.aar"

    /**
     * Maven Central 镜像候选，按国内可达性排序，均为 central 原始路径的直通代理。
     * 注意 BMCLAPI 的 /maven 只代理启动器常用产物，不含 aar，不可作候选。
     */
    private val MIRRORS = listOf(
        "https://maven.aliyun.com/repository/central/",
        "https://repo.huaweicloud.com/repository/maven/",
        "https://repo1.maven.org/maven2/",
    )

    @JvmStatic
    fun ensureNativeLibrary(context: Context, jnaVersion: String?) {
        if (jnaVersion.isNullOrEmpty()) {
            return
        }
        val abi = Architecture.archAsStringAndroid(Architecture.getDeviceArchitecture())
        val target = File(FCLPath.JNA_PATH, "$jnaVersion/libjnidispatch.so")
        if (target.isFile && target.length() > 0) {
            return
        }
        try {
            if (copyFromAssets(context, jnaVersion, abi, target)) {
                Logging.LOG.log(Level.INFO, "JNA natives copied from assets: $jnaVersion/$abi")
            } else {
                downloadFromMavenCentral(jnaVersion, abi, target)
                Logging.LOG.log(Level.INFO, "JNA natives downloaded from Maven Central: $jnaVersion/$abi")
            }
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, "Unable to provide JNA natives $jnaVersion, will fall back to JNA's own resolution", e)
        }
    }

    private fun copyFromAssets(context: Context, jnaVersion: String, abi: String, target: File): Boolean {
        val tmp = File(target.parentFile, target.name + ".tmp")
        return try {
            context.assets.open("app_runtime/jna/$jnaVersion/natives/$abi/libjnidispatch.so").use { input ->
                copy(input, tmp)
            }
            move(tmp, target)
            true
        } catch (_: IOException) {
            tmp.delete()
            // assets 未携带该版本属预期情况，交由下载兜底
            false
        }
    }

    private fun downloadFromMavenCentral(jnaVersion: String, abi: String, target: File) {
        val versionDir = target.parentFile
        versionDir?.mkdirs()
        val aar = File(versionDir, "jna-$jnaVersion.aar.tmp")
        try {
            var failure: IOException? = null
            for (mirror in MIRRORS) {
                val url = mirror + String.format(AAR_PATH, jnaVersion)
                try {
                    download(url, aar)
                    extract(aar, "jni/$abi/libjnidispatch.so", target)
                    return
                } catch (e: IOException) {
                    Logging.LOG.log(Level.WARNING, "JNA natives mirror unavailable: $url", e)
                    failure = e
                }
            }
            throw failure ?: IOException("No Maven Central mirror available")
        } finally {
            aar.delete()
        }
    }

    private fun download(url: String, dest: File) {
        val conn = NetworkUtils.resolveConnection(NetworkUtils.createHttpConnection(NetworkUtils.toURL(url)))
        conn.connectTimeout = 8000
        conn.readTimeout = 20000
        val code = conn.responseCode
        if (code != HttpURLConnection.HTTP_OK) {
            throw IOException("HTTP $code from $url")
        }
        try {
            conn.inputStream.use { copy(it, dest) }
        } finally {
            conn.disconnect()
        }
    }

    private fun extract(aar: File, entryName: String, target: File) {
        val tmp = File(target.parentFile, target.name + ".tmp")
        try {
            ZipFile(aar).use { zip ->
                val entry = zip.getEntry(entryName)
                    ?: throw IOException("Entry not found in ${aar.name}: $entryName")
                zip.getInputStream(entry).use { copy(it, tmp) }
                move(tmp, target)
            }
        } catch (e: IOException) {
            tmp.delete()
            throw e
        }
    }

    private fun copy(input: InputStream, dest: File) {
        FileOutputStream(dest).use { output ->
            input.copyTo(output, DEFAULT_BUFFER_SIZE)
        }
    }

    private fun move(source: File, target: File) {
        if (!source.renameTo(target)) {
            throw IOException("Unable to move $source to $target")
        }
    }
}
