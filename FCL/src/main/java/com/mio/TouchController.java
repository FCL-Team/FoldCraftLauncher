package com.mio;

import android.system.ErrnoException;
import android.system.Os;
import android.util.SparseIntArray;
import android.view.MotionEvent;

import java.util.concurrent.ThreadLocalRandom;

import top.fifthlight.touchcontroller.proxy.client.LauncherSocketProxyClient;
import top.fifthlight.touchcontroller.proxy.client.LauncherSocketProxyClientKt;
import top.fifthlight.touchcontroller.proxy.data.Offset;
import top.fifthlight.touchcontroller.proxy.message.AddPointerMessage;
import top.fifthlight.touchcontroller.proxy.message.ClearPointerMessage;
import top.fifthlight.touchcontroller.proxy.message.RemovePointerMessage;

public class TouchController {
    private final int screenWidth;
    private final int screenHeight;
    private LauncherSocketProxyClient touchControllerProxy = null;
    private int port = -1;
    private final SparseIntArray pointerIdMap = new SparseIntArray();
    private int nextPointerId = 1;

    public TouchController(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        createProxy();
    }

    private void createProxy() {
        try {
            port = ThreadLocalRandom.current().nextInt(32768) + 32768;
            Os.setenv("TOUCH_CONTROLLER_PROXY", String.valueOf(port), true);
            touchControllerProxy = LauncherSocketProxyClientKt.localhostLauncherSocketProxyClient(port);
            new Thread(() -> LauncherSocketProxyClientKt.runProxy(touchControllerProxy)).start();
        } catch (Throwable ignore) {
        }
    }

    public void handleTouchController(MotionEvent event) {
        if (touchControllerProxy == null) {
            return;
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                int pointerId = nextPointerId++;
                pointerIdMap.put(event.getPointerId(0), pointerId);
                touchControllerProxy.trySend(new AddPointerMessage(pointerId, new Offset(event.getX(0) / screenWidth, event.getY(0) / screenHeight)));
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                int pointerId = nextPointerId++;
                int i = event.getActionIndex();
                pointerIdMap.put(event.getPointerId(i), pointerId);
                touchControllerProxy.trySend(new AddPointerMessage(pointerId, new Offset(event.getX(i) / screenWidth, event.getY(i) / screenHeight)));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int pointerId = pointerIdMap.get(event.getPointerId(i));
                    touchControllerProxy.trySend(new AddPointerMessage(pointerId, new Offset(event.getX(i) / screenWidth, event.getY(i) / screenHeight)));
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                touchControllerProxy.trySend(ClearPointerMessage.INSTANCE);
                pointerIdMap.clear();
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                int i = event.getActionIndex();
                int pointerId = pointerIdMap.get(event.getPointerId(i));
                if (pointerId != 0) {
                    pointerIdMap.delete(pointerId);
                    touchControllerProxy.trySend(new RemovePointerMessage(pointerId));
                }
                break;
            }
        }
    }
}
