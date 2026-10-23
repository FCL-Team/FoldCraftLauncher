package com.tungsten.fclcore.task

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fclcore.util.io.NetworkUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.zip.ZipFile

/**
 * FileDownloadTask 并行续传（多连接分块）行为验证。
 */
@RunWith(AndroidJUnit4::class)
class FileDownloadTaskTest {

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FCLPath.loadPaths(context)
        if (!ConfigHolder.isInit()) {
            ConfigHolder.init()
        }
    }

    // ---- Content-Range 解析 ----

    @Test
    fun parseContentRangeTotalHandlesWellFormedAndMalformedInput() {
        assertEquals(15263656L, FileDownloadTask.parseContentRangeTotal("bytes 0-0/15263656"))
        assertEquals(15263656L, FileDownloadTask.parseContentRangeTotal("bytes 1024-2047/15263656"))
        assertEquals(-1L, FileDownloadTask.parseContentRangeTotal(null))
        assertEquals(-1L, FileDownloadTask.parseContentRangeTotal("bytes 0-0/*"))
        assertEquals(-1L, FileDownloadTask.parseContentRangeTotal("bytes 0-0"))
        assertEquals(-1L, FileDownloadTask.parseContentRangeTotal("garbage"))
    }

    // ---- 真实链路：按连接数限速的服务器 ----

    /**
     * alist.8mi.tech 直链 302 到天翼云盘，签名 URL 带 x-amz-limitrate=51200（每连接 50KB/s），
     * 单连接下载该 14.6MB 安装器需要约 5 分钟以上；持续低速后并行续传切换为多连接，
     * 应在一两分钟内完成且产物为完整 jar。
     */
    @Test(timeout = 280_000)
    fun slowPerConnectionLinkDownloadsInParallel() {
        val url = NetworkUtils.toURL(
            "https://alist.8mi.tech/d/mirror/HMCL-Metadata/Auto/cleanroom/files/cleanroom-0.7.12-alpha-installer.jar"
        )
        val file = File(ApplicationProvider.getApplicationContext<Context>().cacheDir, "cleanroom-parallel-test.jar")
        file.delete()

        val task = FileDownloadTask(url, file, null)
        // 进度必须严格单调：分块重试若重复计字节，进度会冲破 100% 触发异常、
        // 或整轮失败清零重来（进度条跑满后重新跑）
        val samples = java.util.concurrent.ConcurrentLinkedQueue<Double>()
        task.progressProperty().addListener { _, _, newValue -> samples.add(newValue.toDouble()) }

        val executor = task.executor()
        val start = System.currentTimeMillis()
        val success = executor.test()
        val elapsed = System.currentTimeMillis() - start

        assertTrue("success=$success exception=" + executor.exception, success)
        // 等 UI 线程把已提交的进度回调跑完再断言
        androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().runOnMainSync { }
        val seq = samples.toList()
        assertTrue("no progress samples", seq.isNotEmpty())
        assertTrue("progress not monotonic: $seq", seq.zipWithNext().all { (a, b) -> b >= a - 1e-9 })
        assertTrue("final progress ${seq.last()} not near 1.0", seq.last() > 0.9)
        assertTrue("file too small: " + file.length(), file.length() > 10_000_000L)
        // 完整性：安装器是合法 zip/jar
        ZipFile(file).use { zip ->
            assertTrue(zip.entries().asSequence().any { it.name == "install_profile.json" })
        }
        // 并行续传生效的判据：单连接被服务端硬限 50KB/s（≈300s 起），
        // 多连接应在 240s 内完成；超时说明并行路径未触发
        assertTrue("elapsed=$elapsed ms, parallel boost did not engage", elapsed < 240_000L)
    }
}
