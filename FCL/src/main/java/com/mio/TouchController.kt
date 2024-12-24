package com.mio

import android.content.Context
import android.os.Vibrator
import android.system.Os
import android.util.Log
import android.util.SparseIntArray
import android.view.MotionEvent
import com.tungsten.fclcore.util.Logging
import top.fifthlight.touchcontroller.proxy.client.LauncherProxyClient
import top.fifthlight.touchcontroller.proxy.client.android.SimpleVibrationHandler
import top.fifthlight.touchcontroller.proxy.client.android.transport.UnixSocketTransport
import top.fifthlight.touchcontroller.proxy.data.Offset
import java.util.logging.Level

class TouchController(context: Context, val width: Int, val height: Int) {
    private var client: LauncherProxyClient? = null
    private val socketName = "FoldCraftLauncher"
    private val pointerIdMap = SparseIntArray()
    private var nextPointerId = 1

    init {
        createProxy(context)
    }

    private fun createProxy(context: Context) {
        try {
            val transport = UnixSocketTransport(socketName)
            Os.setenv("TOUCH_CONTROLLER_PROXY_SOCKET", socketName, true)
            client = LauncherProxyClient(transport)
            val vibrator = context.getSystemService<Vibrator>(Vibrator::class.java)
            val handler = SimpleVibrationHandler(vibrator)
            client?.vibrationHandler = handler
            client?.run()
        } catch (ex: Throwable) {
            Logging.LOG.log(
                Level.WARNING,
                "TouchController: TouchController proxy client create failed",
                ex
            );
        }
    }

    private fun MotionEvent.getOffset(index: Int) = Offset(
        getX(index) / width,
        getY(index) / height
    )

    fun handleTouchEvent(event: MotionEvent) {
        val client = client ?: return
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val pointerId = nextPointerId++
                pointerIdMap.put(event.getPointerId(0), pointerId)
                client.addPointer(pointerId, event.getOffset(0))
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                val pointerId = nextPointerId++
                val i = event.actionIndex
                pointerIdMap.put(event.getPointerId(i), pointerId)
                client.addPointer(pointerId, event.getOffset(i))
            }

            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    val pointerId = pointerIdMap.get(event.getPointerId(i))
                    client.addPointer(pointerId, event.getOffset(i))
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                client.clearPointer()
                pointerIdMap.clear()
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val i = event.actionIndex
                val pointerId = pointerIdMap.get(event.getPointerId(i))
                if (pointerId != 0) {
                    pointerIdMap.delete(pointerId)
                    client.removePointer(pointerId)
                }
            }
        }
    }
}