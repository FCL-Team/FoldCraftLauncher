package com.mio.flite

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.tungsten.fcl.FCLApp
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

/**
 * 游戏复述功能的安卓 TTS 后端，替代缺失的 flite 语音引擎。
 */
object FliteTts {

    private const val TAG = "FliteTTS"

    /** init 的快速判定窗口：无引擎时 onInit(ERROR) 几乎立即到达，超时则视为引擎仍在冷启动 */
    private const val FAST_CHECK_SECONDS = 2L

    /** 朗读阶段等待引擎就绪的上限，TTS 引擎进程冷启动在部分 ROM 上可达数十秒 */
    private const val READY_WAIT_SECONDS = 20L

    private const val STATE_UNINIT = 0
    private const val STATE_INITIALIZING = 1
    private const val STATE_READY = 2
    private const val STATE_FAILED = 3

    // TextToSpeech 需要在带 Looper 的线程上构建与回调，游戏侧调用线程没有 Looper
    private val ttsThread = HandlerThread(TAG).apply { start() }

    @Volatile
    private var state = STATE_UNINIT

    @Volatile
    private var tts: TextToSpeech? = null

    @Volatile
    private var readySignal = CountDownLatch(1)

    private val utteranceCounter = AtomicLong()
    private val pendingUtterances = ConcurrentHashMap<String, CountDownLatch>()

    /** 初始化 TTS 引擎；引擎冷启动时乐观返回 true，就绪等待由朗读阶段承担 */
    @JvmStatic
    fun init(): Boolean {
        synchronized(this) {
            when (state) {
                STATE_READY -> return true
                STATE_FAILED -> return false
                STATE_INITIALIZING -> return true
                else -> {
                    readySignal = CountDownLatch(1)
                    state = STATE_INITIALIZING
                    val signal = readySignal
                    Handler(ttsThread.looper).post { constructTts(signal) }
                }
            }
        }
        // 快速判定期间不得持有对象锁：onInit 回调与 tts 赋值都依赖锁外的执行进度
        val arrived = try {
            readySignal.await(FAST_CHECK_SECONDS, TimeUnit.SECONDS)
        } catch (_: InterruptedException) {
            false
        }
        return if (arrived) state == STATE_READY else true
    }

    /** 朗读一段文本并阻塞至播放完成，返回耗时秒数；gain 为相对音量（1.0 为默认）；不可用或失败返回 -1 */
    @JvmStatic
    fun speak(text: String, gain: Float): Float {
        if (state == STATE_FAILED) return -1f
        if (state == STATE_INITIALIZING) {
            // 朗读发生在游戏侧串行队列上，阻塞等待与 flite 的同步播放语义一致
            try {
                readySignal.await(READY_WAIT_SECONDS, TimeUnit.SECONDS)
            } catch (_: InterruptedException) {
                return -1f
            }
        }
        if (state != STATE_READY) return -1f
        val instance = tts ?: return -1f
        val utteranceId = "fcl-flite-${utteranceCounter.incrementAndGet()}"
        val done = CountDownLatch(1)
        pendingUtterances[utteranceId] = done
        try {
            val params = Bundle().apply { putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, gain) }
            if (instance.speak(text, TextToSpeech.QUEUE_ADD, params, utteranceId) != TextToSpeech.SUCCESS) return -1f
            val start = System.nanoTime()
            done.await()
            return (System.nanoTime() - start) / 1_000_000_000f
        } catch (_: InterruptedException) {
            return -1f
        } finally {
            pendingUtterances.remove(utteranceId)
        }
    }

    /** 释放 TTS 引擎并唤醒所有阻塞中的朗读 */
    @JvmStatic
    @Synchronized
    fun shutdown() {
        state = STATE_UNINIT
        releaseInstance()
        readySignal.countDown()
        pendingUtterances.keys.toList().forEach { id ->
            pendingUtterances.remove(id)?.countDown()
        }
    }

    private fun constructTts(signal: CountDownLatch) {
        val ref = AtomicReference<TextToSpeech>()
        try {
            val instance = TextToSpeech(FCLApp.getAppContext()) { code ->
                if (state == STATE_INITIALIZING) {
                    if (code == TextToSpeech.SUCCESS) {
                        ref.get()?.let { tts = it }
                        state = STATE_READY
                        Log.i(TAG, "TextToSpeech ready")
                    } else {
                        state = STATE_FAILED
                        Log.e(TAG, "init failed: status=$code")
                    }
                    signal.countDown()
                }
                // 过期回调（期间已 shutdown/重新初始化）直接忽略
            }
            ref.set(instance)
            instance.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}

                override fun onDone(utteranceId: String?) {
                    release(utteranceId)
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    release(utteranceId)
                }

                override fun onStop(utteranceId: String?, interrupted: Boolean) {
                    release(utteranceId)
                }
            })
            synchronized(this) {
                if (state != STATE_INITIALIZING) {
                    // 构建期间已被 shutdown，立即释放避免泄漏
                    instance.shutdown()
                    return
                }
                tts = instance
            }
        } catch (e: Throwable) {
            Log.e(TAG, "create TextToSpeech failed", e)
            state = STATE_FAILED
            signal.countDown()
        }
    }

    private fun release(utteranceId: String?) {
        utteranceId?.let(pendingUtterances::remove)?.countDown()
    }

    private fun releaseInstance() {
        val instance = tts ?: return
        tts = null
        Handler(ttsThread.looper).post {
            try {
                instance.shutdown()
            } catch (e: Throwable) {
                Log.e(TAG, "shutdown TextToSpeech failed", e)
            }
        }
    }
}
