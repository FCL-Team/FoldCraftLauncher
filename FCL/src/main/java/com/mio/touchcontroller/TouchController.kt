package com.mio.touchcontroller

import android.content.Context
import android.graphics.PointF
import android.os.VibrationEffect
import android.os.Vibrator
import android.system.Os
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.MotionEvent
import com.tungsten.fclcore.util.Logging
import top.fifthlight.touchcontroller.proxy.client.LauncherProxyClient
import top.fifthlight.touchcontroller.proxy.client.LauncherProxyClient.VibrationHandler
import top.fifthlight.touchcontroller.proxy.client.PlatformCapability
import top.fifthlight.touchcontroller.proxy.client.android.transport.UnixSocketTransport
import top.fifthlight.touchcontroller.proxy.message.VibrateMessage
import java.util.logging.Level

class TouchController(
    context: Context,
    val width: Int,
    val height: Int,
    val vibrationDuration: Long = 100,
) {
    var client: LauncherProxyClient? = null
        private set
    private val socketName = "FoldCraftLauncher"

    //handleTouchEvent
    private val pointerIdMap = SparseIntArray()
    private var nextPointerId = 1

    //moveView
    // 存储每个手指的初始位置（key = pointerId）
    private val initialPoints = SparseArray<PointF>()

    // 存储每个手指的上次位置（用于计算增量位移）
    private val lastPoints = SparseArray<PointF>()

    init {
        createProxy(context)
    }

    private fun createProxy(context: Context) {
        try {
            val transport = UnixSocketTransport(socketName)
            Os.setenv("TOUCH_CONTROLLER_PROXY_SOCKET", socketName, true)
            client = LauncherProxyClient(
                transport = transport,
                capabilities = setOf(PlatformCapability.TEXT_STATUS),
            )
            val vibrator = context.getSystemService<Vibrator>(Vibrator::class.java)
            val handler = object : VibrationHandler {
                override fun vibrate(kind: VibrateMessage.Kind) {
                    runCatching {
                        val effect = VibrationEffect.createOneShot(
                            vibrationDuration,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                        vibrator.vibrate(effect)
                    }
                }
            }

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

    private fun LauncherProxyClient.addPointer(pointerId: Int, event: MotionEvent, index: Int) =
        addPointer(pointerId, event.getX(index) / width, event.getY(index) / height)

    fun handleTouchEvent(event: MotionEvent) {
        val client = client ?: return
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val pointerId = nextPointerId++
                pointerIdMap.put(event.getPointerId(0), pointerId)
                client.addPointer(pointerId, event, 0)
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                val pointerId = nextPointerId++
                val i = event.actionIndex
                pointerIdMap.put(event.getPointerId(i), pointerId)
                client.addPointer(pointerId, event, i)
            }

            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    val pointerId = pointerIdMap.get(event.getPointerId(i))
                    client.addPointer(pointerId, event, i)
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

    fun moveView(event: MotionEvent) {
        val client = client ?: return
        val pointerIndex = event.actionIndex
        val pointerId = event.getPointerId(pointerIndex)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                // 新手指按下，记录初始位置和上次位置
                val x = event.getX(pointerIndex)
                val y = event.getY(pointerIndex)
                initialPoints.put(pointerId, PointF(x, y))
                lastPoints.put(pointerId, PointF(x, y))
            }

            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    val activePointerId = event.getPointerId(i)
                    val initialPoint = initialPoints.get(activePointerId)
                    val lastPoint = lastPoints.get(activePointerId)
                    if (initialPoint != null && lastPoint != null) {
                        val deltaX = event.getX(i) - lastPoint.x
                        val deltaY = event.getY(i) - lastPoint.y
                        // 更新上次位置
                        lastPoint.x = event.getX(i)
                        lastPoint.y = event.getY(i)
                        client.moveView(true, deltaY / height, deltaX / width)
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                initialPoints.remove(pointerId)
                lastPoints.remove(pointerId)
            }

            MotionEvent.ACTION_CANCEL -> {
                initialPoints.clear()
                lastPoints.clear()
            }
        }
    }
}