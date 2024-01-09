package org.lwjgl.glfw;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Choreographer;
import android.view.Surface;

import androidx.annotation.Nullable;

import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.keycodes.LwjglGlfwKeycode;
import com.tungsten.fclauncher.utils.FCLPath;

import dalvik.annotation.optimization.CriticalNative;

public class CallbackBridge {
    public static final Choreographer sChoreographer = Choreographer.getInstance();
    private static FCLBridge fclBridge;
    private static boolean isGrabbing = false;
    public static final int CLIPBOARD_COPY = 2000;
    public static final int CLIPBOARD_PASTE = 2001;
    public static final int CLIPBOARD_OPEN = 2002;
    public static float mouseX, mouseY;
    public volatile static boolean holdingAlt, holdingCapslock, holdingCtrl,
            holdingNumlock, holdingShift;
    public static final int GLFW_MOD_SHIFT = 0x1;
    public static final int GLFW_MOD_CONTROL = 0x2;
    public static final int GLFW_MOD_ALT = 0x4;
    public static final int GLFW_MOD_CAPS_LOCK = 0x10;
    public static final int GLFW_MOD_NUM_LOCK = 0x20;


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
            nativeSendKey(keycode, scancode, isDown ? 1 : 0, modifiers);
        }
        if (isDown && keychar != '\u0000') {
            nativeSendCharMods(keychar, modifiers);
            nativeSendChar(keychar);
        }
    }

    public static void sendChar(char keychar, int modifiers) {
        nativeSendCharMods(keychar, modifiers);
        nativeSendChar(keychar);
    }

    public static void sendMouseButton(int button, boolean status) {
        CallbackBridge.sendMouseKeycode(button, CallbackBridge.getCurrentMods(), status);
    }

    public static void sendMouseKeycode(int button, int modifiers, boolean isDown) {
        nativeSendMouseButton(button, isDown ? 1 : 0, modifiers);
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
        ClipboardManager clipboard = (ClipboardManager) FCLPath.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
        switch (type) {
            case CLIPBOARD_COPY:
                ClipData clip = ClipData.newPlainText("FCL Clipboard", copy);
                clipboard.setPrimaryClip(clip);
                return null;
            case CLIPBOARD_PASTE:
                if (!clipboard.hasPrimaryClip()) {
                    return "";
                }
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                return item.getText().toString();
            case CLIPBOARD_OPEN:
                FCLBridge.openLink(copy);
                return null;
            default:
                return null;
        }
    }


    public static int getCurrentMods() {
        int currMods = 0;
        if (holdingAlt) {
            currMods |= GLFW_MOD_ALT;
        }
        if (holdingCapslock) {
            currMods |= GLFW_MOD_CAPS_LOCK;
        }
        if (holdingCtrl) {
            currMods |= GLFW_MOD_CONTROL;
        }
        if (holdingNumlock) {
            currMods |= GLFW_MOD_NUM_LOCK;
        }
        if (holdingShift) {
            currMods |= GLFW_MOD_SHIFT;
        }
        return currMods;
    }

    public static void setModifiers(int keyCode, boolean isDown) {
        switch (keyCode) {
            case LwjglGlfwKeycode.KEY_LEFT_SHIFT:
                CallbackBridge.holdingShift = isDown;
                return;

            case LwjglGlfwKeycode.KEY_LEFT_CONTROL:
                CallbackBridge.holdingCtrl = isDown;
                return;

            case LwjglGlfwKeycode.KEY_LEFT_ALT:
                CallbackBridge.holdingAlt = isDown;
                return;

            case LwjglGlfwKeycode.KEY_CAPS_LOCK:
                CallbackBridge.holdingCapslock = isDown;
                return;

            case LwjglGlfwKeycode.KEY_NUM_LOCK:
                CallbackBridge.holdingNumlock = isDown;
        }
    }

    public static void setFCLBridge(FCLBridge fclBridge) {
        CallbackBridge.fclBridge = fclBridge;
    }

    //Called from JRE side
    @SuppressWarnings("unused")
    private static void onGrabStateChanged(final boolean grabbing) {
        isGrabbing = grabbing;
        CallbackBridge.fclBridge.setCursorMode(grabbing ? FCLBridge.CursorDisabled : FCLBridge.CursorEnabled);

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

    public static native void setupBridgeWindow(Surface surface);

    static {
        System.loadLibrary("pojavexec");
    }
}

