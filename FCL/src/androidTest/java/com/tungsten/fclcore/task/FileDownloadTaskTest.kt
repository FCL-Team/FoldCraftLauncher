package com.tungsten.fclcore.task

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.io.NetworkUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.security.MessageDigest
import java.util.Random
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CopyOnWriteArrayList
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

    // ---- 本地可控服务器：带 SHA-1 校验的并行续传回归 ----

    /**
     * 单流限速、分块全速的本地服务器：单流持续低于慢速阈值满一个观测窗口后触发并行续传，
     * 分块连接随即全速拉完剩余区间。下载带 SHA-1 完整性校验——分块写入不经过
     * Context.write，若并行完成时未按最终文件重算摘要，落盘校验必失败。
     */
    @Test(timeout = 60_000)
    fun parallelRemainderPassesIntegrityCheck() {
        val payload = ByteArray(SIZE).also { Random(0).nextBytes(it) }
        val sha1 = MessageDigest.getInstance("SHA-1").digest(payload)
            .joinToString("") { "%02x".format(it.toInt() and 0xff) }

        val server = SlowSingleStreamFastRangeServer(payload, SINGLE_STREAM_SPEED)
        server.start()
        try {
            val file = File(ApplicationProvider.getApplicationContext<Context>().cacheDir, "parallel-integrity-test.bin")
            file.delete()

            val task = FileDownloadTask(
                NetworkUtils.toURL("http://127.0.0.1:${server.port}/file"),
                file,
                FileDownloadTask.IntegrityCheck("SHA-1", sha1)
            )
            val executor = task.executor()
            val success = executor.test()
            assertTrue("success=$success exception=" + executor.exception, success)
            assertEquals(payload.size.toLong(), file.length())
        } finally {
            server.stop()
        }
    }

    // ---- 真实链路：按连接数限速的服务器 ----

    /**
     * alist.8mi.tech 直链 302 到天翼云盘，签名 URL 带 x-amz-limitrate=51200（每连接 50KB/s），
     * 单连接下载该 14.6MB 安装器需要约 5 分钟以上；持续低速后并行续传切换为多连接，
     * 应在一两分钟内完成且产物为完整 jar。
     *
     * 断言依赖外部镜像保持"按连接限速 50KB/s"的行为：镜像提速、更换直链或文件更新后，
     * 耗时与体积断言会假失败，应先核对镜像现状再调整，不视为功能回归。
     * 并行触发与摘要重算的确定性覆盖见 [parallelRemainderPassesIntegrityCheck]。
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
        val samples = ConcurrentLinkedQueue<Double>()
        task.progressProperty().addListener { _, _, newValue -> samples.add(newValue.toDouble()) }

        val executor = task.executor()
        val start = System.currentTimeMillis()
        val success = executor.test()
        val elapsed = System.currentTimeMillis() - start

        assertTrue("success=$success exception=" + executor.exception, success)
        // 等 UI 线程把已提交的进度回调跑完再断言
        InstrumentationRegistry.getInstrumentation().runOnMainSync { }
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

    companion object {
        /** 本地服务器测试的文件大小：触发并行后均分为 4 个 2MB 分块 */
        private const val SIZE = 8 * 1024 * 1024
        /** 单流限速（字节/秒），需低于 BMCLAPIRetryPolicy 的 128KB/s 慢速阈值 */
        private const val SINGLE_STREAM_SPEED = 40 * 1024
    }
}

/**
 * 极简测试服务器：不带 Range 头的请求按指定速度限速返回全量 200，
 * 带 Range 头的请求全速返回精确区间 206——模拟"按连接限速但多连接可突破"的网盘直链。
 */
private class SlowSingleStreamFastRangeServer(
    private val payload: ByteArray,
    private val slowBytesPerSecond: Int,
) {
    private var serverSocket: ServerSocket? = null
    private val workers = CopyOnWriteArrayList<Thread>()

    val port: Int
        get() = serverSocket!!.localPort

    fun start() {
        serverSocket = ServerSocket(0, 16, InetAddress.getByName("127.0.0.1"))
        Thread {
            while (!serverSocket!!.isClosed) {
                val socket = try {
                    serverSocket!!.accept()
                } catch (e: IOException) {
                    break
                }
                Thread { serve(socket) }.also { workers.add(it) }.start()
            }
        }.also { workers.add(it) }.start()
    }

    fun stop() {
        serverSocket?.close()
        workers.forEach { it.interrupt() }
    }

    private fun serve(socket: Socket) {
        try {
            socket.use { s ->
                val reader = BufferedReader(InputStreamReader(s.getInputStream(), Charsets.US_ASCII))
                var range: String? = null
                while (true) {
                    val line = reader.readLine() ?: return
                    if (line.isEmpty()) break
                    if (line.startsWith("Range:", true))
                        range = line.substringAfter(':').trim()
                }

                val out = s.getOutputStream()
                val match = range?.let { RANGE_PATTERN.find(it) }
                if (match != null) {
                    val from = match.groupValues[1].toLong()
                    val end = minOf(match.groupValues[2].toLongOrNull() ?: (payload.size - 1L), payload.size - 1L)
                    val len = (end - from + 1).toInt()
                    out.write(
                        ("HTTP/1.1 206 Partial Content\r\n" +
                            "Content-Range: bytes $from-$end/${payload.size}\r\n" +
                            "Content-Length: $len\r\n" +
                            "Connection: close\r\n\r\n").toByteArray(Charsets.US_ASCII)
                    )
                    out.write(payload, from.toInt(), len)
                    out.flush()
                } else {
                    out.write(
                        ("HTTP/1.1 200 OK\r\n" +
                            "Content-Length: ${payload.size}\r\n" +
                            "Connection: close\r\n\r\n").toByteArray(Charsets.US_ASCII)
                    )
                    out.flush()
                    var written = 0
                    val buffer = ByteArray(4096)
                    while (written < payload.size) {
                        val n = minOf(buffer.size, payload.size - written)
                        out.write(payload, written, n)
                        out.flush()
                        written += n
                        Thread.sleep(1000L * n / slowBytesPerSecond)
                    }
                }
            }
        } catch (ignored: IOException) {
            // 单流连接被客户端在并行切换后断开等场景，静默结束
        }
    }

    companion object {
        private val RANGE_PATTERN = Regex("bytes=(\\d+)-(\\d*)")
    }
}
