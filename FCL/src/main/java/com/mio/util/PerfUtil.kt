package com.mio.util

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.util.Printer

class PerfUtil : Printer {
    companion object {
        @JvmStatic
        fun install() {
            Looper.getMainLooper().setMessageLogging(PerfUtil())
        }

        @JvmStatic
        fun printStackTrace() {
            val stackTrace = Thread.currentThread().stackTrace
            var str = ""
            for (element in stackTrace) {
                str += element.toString()
            }
            Log.e("PerfUtil-printStackTrace", str)
        }
    }

    private val sampler = StackSampler(300)
    private var isStarted = false
    private var startTime = 0L

    override fun println(x: String?) {
        if (!isStarted) {
            isStarted = true
            startTime = System.currentTimeMillis()
            sampler.startDump()
        } else {
            isStarted = false
            val endTime = System.currentTimeMillis()
            if (endTime - startTime > 300) {
                Log.e("FCL PerfUtil", "block time = ${endTime - startTime}")
            }
            sampler.stopDump()
        }
    }

    inner class StackSampler(val interval: Long) {
        private val handler: Handler
        private val runnable = Runnable {
            val sb = StringBuilder()
            Looper.getMainLooper().thread.stackTrace.forEach {
                sb.append(it.toString())
                sb.append("\n")
            }
            Log.e("FCL PerfUtil", sb.toString())
        }

        init {
            val handlerThread = HandlerThread("")
            handlerThread.start()
            handler = Handler(handlerThread.looper)
        }

        fun startDump() {
            handler.postDelayed(runnable, interval)
        }

        fun stopDump() {
            handler.removeCallbacks(runnable)
        }
    }
}