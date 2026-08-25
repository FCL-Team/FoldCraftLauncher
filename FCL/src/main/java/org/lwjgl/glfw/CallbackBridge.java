package org.lwjgl.glfw;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Choreographer;

import androidx.annotation.Nullable;

import com.tungsten.fcl.FCLApp;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.keycodes.LwjglGlfwKeycode;
import com.tungsten.fclauncher.keycodes.LwjglKeycodeMap;

import dalvik.annotation.optimization.CriticalNative;

public class CallbackBridge {
    public static final Choreographer sChoreographer = Choreographer.getInstance();
    private static FCLBridge fclBridge = null;
    /** 游戏线程写、UI 线程读的抓取状态，公开访问器也可能被其它线程调用 */
    private static volatile boolean isGrabbing = false;
    /** 保护 isGrabbing 的写与「检查+应用」的组合操作，防止应用过期状态 */
    private static final Object grabLock = new Object();

    public static final int CLIPBOARD_COPY = 2000;
    public static final int CLIPBOARD_PASTE = 2001;
    public static final int CLIPBOARD_OPEN = 2002;

    public static volatile int windowWidth, windowHeight;
    public static volatile int physicalWidth, physicalHeight;
    public static float mouseX, mouseY;
    public volatile static boolean holdingAlt, holdingCapslock, holdingCtrl,
            holdingNumlock, holdingShift;

    public static void putMouseEventWithCoords(int button, float x, float y) {
        putMouseEventWithCoords(button, true, x, y);
        sChoreographer.postFrameCallbackDelayed(l -> putMouseEventWithCoords(button, false, x, y), 33);
    }

    public static void putMouseEventWithCoords(int button, boolean isDown, float x, float y /* , int dz, long nanos */) {
        sendCursorPos(x, y);
        sendMouseKeycode(button, CallbackBridge.getCurrentMods(), isDown);
    }


    public static void sendCursorPos(float x, float y) {
        mouseX = x;
        mouseY = y;
        nativeSendCursorPos(mouseX, mouseY);
    }

    public static void sendKeycode(int keycode, char keychar, int scancode, int modifiers, boolean isDown) {
        // TODO CHECK: This may cause input issue, not receive input!
        if (keycode != 0) {
            int code = LwjglKeycodeMap.convertKeycode(keycode);
            if (code <= 0) {
                return;
            }
            nativeSendKey(code, scancode, isDown ? 1 : 0, modifiers);
        }
        if (isDown && !Character.isISOControl(keychar)) {
            nativeSendCharMods(keychar, modifiers);
            nativeSendChar(keychar);
        }
    }

    public static void sendChar(char keychar, int modifiers) {
        nativeSendCharMods(keychar, modifiers);
        nativeSendChar(keychar);
    }

    public static void sendKeyPress(int keyCode, int modifiers, boolean status) {
        sendKeyPress(keyCode, 0, modifiers, status);
    }

    public static void sendKeyPress(int keyCode, int scancode, int modifiers, boolean status) {
        sendKeyPress(keyCode, '\u0000', scancode, modifiers, status);
    }

    public static void sendKeyPress(int keyCode, char keyChar, int scancode, int modifiers, boolean status) {
        CallbackBridge.sendKeycode(keyCode, keyChar, scancode, modifiers, status);
    }

    public static void sendKeyPress(int keyCode) {
        sendKeyPress(keyCode, CallbackBridge.getCurrentMods(), true);
        sendKeyPress(keyCode, CallbackBridge.getCurrentMods(), false);
    }

    public static void sendMouseButton(int button, boolean status) {
        CallbackBridge.sendMouseKeycode(button, CallbackBridge.getCurrentMods(), status);
    }

    public static void sendMouseKeycode(int button, int modifiers, boolean isDown) {
        // if (isGrabbing()) DEBUG_STRING.append("MouseGrabStrace: " + android.util.Log.getStackTraceString(new Throwable()) + "\n");
        nativeSendMouseButton(button, isDown ? 1 : 0, modifiers);
    }

    public static void sendMouseKeycode(int keycode) {
        sendMouseKeycode(keycode, CallbackBridge.getCurrentMods(), true);
        sendMouseKeycode(keycode, CallbackBridge.getCurrentMods(), false);
    }

    public static void sendScroll(double xoffset, double yoffset) {
        nativeSendScroll(xoffset, yoffset);
    }

    public static void sendUpdateWindowSize(int w, int h) {
        nativeSendScreenSize(w, h);
    }

    public static boolean isGrabbing() {
        // Avoid going through the JNI each time.
        return isGrabbing;
    }

    // Called from JRE side
    @SuppressWarnings("unused")
    public static @Nullable String accessAndroidClipboard(int type, String copy) {
        Activity activity = FCLApp.getActivity();
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        String result = null;
        switch (type) {
            case CLIPBOARD_COPY:
                ClipData clip = ClipData.newPlainText("FCL Clipboard", copy);
                clipboard.setPrimaryClip(clip);
                break;
            case CLIPBOARD_PASTE:
                if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    result = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                } else {
                    result = "";
                }
                break;
            case CLIPBOARD_OPEN:
                FCLBridge.openLink(copy);
                break;
        }
        return result;
    }


    public static int getCurrentMods() {
        int currMods = 0;
        if (holdingAlt) {
            currMods |= LwjglGlfwKeycode.GLFW_MOD_ALT;
        }
        if (holdingCapslock) {
            currMods |= LwjglGlfwKeycode.GLFW_MOD_CAPS_LOCK;
        }
        if (holdingCtrl) {
            currMods |= LwjglGlfwKeycode.GLFW_MOD_CONTROL;
        }
        if (holdingNumlock) {
            currMods |= LwjglGlfwKeycode.GLFW_MOD_NUM_LOCK;
        }
        if (holdingShift) {
            currMods |= LwjglGlfwKeycode.GLFW_MOD_SHIFT;
        }
        return currMods;
    }

    public static void setModifiers(int keyCode, boolean isDown) {
        switch (keyCode) {
            case LwjglGlfwKeycode.KEY_LEFT_SHIFT:
                CallbackBridge.holdingShift = isDown;
                break;

            case LwjglGlfwKeycode.KEY_LEFT_CONTROL:
                CallbackBridge.holdingCtrl = isDown;
                break;

            case LwjglGlfwKeycode.KEY_LEFT_ALT:
                CallbackBridge.holdingAlt = isDown;
                break;

            case LwjglGlfwKeycode.KEY_CAPS_LOCK:
                CallbackBridge.holdingCapslock = isDown;
                break;

            case LwjglGlfwKeycode.KEY_NUM_LOCK:
                CallbackBridge.holdingNumlock = isDown;
                break;
        }
    }

    public static void setFCLBridge(FCLBridge fclBridge) {
        CallbackBridge.fclBridge = fclBridge;
    }

    //Called from JRE side
    @SuppressWarnings("unused")
    private static void onGrabStateChanged(final boolean grabbing) {
        synchronized (grabLock) {
            isGrabbing = grabbing;
        }
        sChoreographer.postFrameCallbackDelayed((time) -> {
            synchronized (grabLock) {
                // 防抖：延迟期间状态再次变化则说明本次回调已过期，跳过，最终状态由最后一次调用应用
                if (isGrabbing != grabbing || fclBridge == null) {
                    return;
                }
                // 延迟回调不可取消，游戏退出/Activity 销毁后仍会执行，此时不再触碰 UI
                Activity activity = FCLApp.getActivity();
                if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
                fclBridge.setCursorMode(grabbing ? FCLBridge.CursorDisabled : FCLBridge.CursorEnabled);
            }
        }, 16);
    }

    @CriticalNative
    public static native void nativeSetUseInputStackQueue(boolean useInputStackQueue);

    @CriticalNative
    private static native boolean nativeSendChar(char codepoint);

    // GLFW: GLFWCharModsCallback deprecated, but is Minecraft still use?
    @CriticalNative
    private static native boolean nativeSendCharMods(char codepoint, int mods);

    @CriticalNative
    private static native void nativeSendKey(int key, int scancode, int action, int mods);

    // private static native void nativeSendCursorEnter(int entered);
    @CriticalNative
    private static native void nativeSendCursorPos(float x, float y);

    @CriticalNative
    private static native void nativeSendMouseButton(int button, int action, int mods);

    @CriticalNative
    private static native void nativeSendScroll(double xoffset, double yoffset);

    @CriticalNative
    private static native void nativeSendScreenSize(int width, int height);

    public static native void nativeSetWindowAttrib(int attrib, int value);

    public static native void setupBridgeWindow(Object surface);

    public static native int getFps();

    static {
        System.loadLibrary("pojavexec");
    }
}

