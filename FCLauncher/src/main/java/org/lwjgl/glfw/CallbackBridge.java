package org.lwjgl.glfw;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Choreographer;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.tungsten.fcl.FCLApplication;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.keycodes.AndroidKeycodeMap;
import com.tungsten.fclauncher.keycodes.LwjglGlfwKeycode;
import com.tungsten.fclauncher.keycodes.LwjglKeycodeMap;

import org.libsdl.app.SDLActivity;
import org.libsdl.app.SDLSurface;

import java.util.function.Consumer;

import dalvik.annotation.optimization.CriticalNative;

public class CallbackBridge {
    public static final Choreographer sChoreographer = Choreographer.getInstance();
    private static FCLBridge fclBridge = null;
    private static boolean isGrabbing = false;
    private static final Consumer<Boolean> grabListener = isGrabbing -> CallbackBridge.fclBridge.setCursorMode(isGrabbing ? FCLBridge.CursorDisabled : FCLBridge.CursorEnabled);

    public static final int CLIPBOARD_COPY = 2000;
    public static final int CLIPBOARD_PASTE = 2001;
    public static final int CLIPBOARD_OPEN = 2002;

    // SDL 通知类型与动作（与 JNI 侧 native_hooks 对应）
    public static final int NOTIF_TYPE_SDL = 0;
    public static final int ACTION_INIT_LAUNCHER_INTEGRATION = 0;
    public static final int ACTION_SEND_TEXTBOX_RECT = 1;

    // SDL 集成是否已启用（由 notifyLauncher 在 SDL 初始化时置位）
    public static volatile boolean sdlEnabled = false;
    // 手柄直通模式已启用
    public static boolean sGamepadDirectInput = false;

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
        // HOVER_MOVE 和 MOVE 在 SDL 中等价
        if (!sdlEnabled) return;
        SDLActivity.onNativeMouse(0, MotionEvent.ACTION_MOVE, x, y, false);
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
        if (!sdlEnabled) return;
        int androidKeycode = AndroidKeycodeMap.getAndroidKeycode(keycode);
        if (isDown) {
            SDLActivity.onNativeKeyDown(androidKeycode);
        } else {
            SDLActivity.onNativeKeyUp(androidKeycode);
        }
    }

    public static void sendChar(char keychar, int modifiers) {
        nativeSendCharMods(keychar, modifiers);
        nativeSendChar(keychar);
        if (!sdlEnabled) return;
        int androidKeycode = getAndroidKeycode(keychar);
        SDLActivity.onNativeKeyDown(androidKeycode);
        SDLActivity.onNativeKeyUp(androidKeycode);
    }

    private static final android.view.KeyCharacterMap KEY_CHARACTER_MAP = android.view.KeyCharacterMap.load(android.view.KeyCharacterMap.VIRTUAL_KEYBOARD);
    private static final char[] KEY_BUFFER = new char[1];

    private static int getAndroidKeycode(char c) {
        KEY_BUFFER[0] = c;
        android.view.KeyEvent[] events = KEY_CHARACTER_MAP.getEvents(KEY_BUFFER);
        return events != null && events.length > 0 ? events[0].getKeyCode() : KeyEvent.KEYCODE_UNKNOWN;
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
        if (!sdlEnabled) return;
        SDLActivity.onNativeMouse(button, isDown ? MotionEvent.ACTION_DOWN : MotionEvent.ACTION_UP, mouseX, mouseY, false);
    }

    public static void sendMouseKeycode(int keycode) {
        sendMouseKeycode(keycode, CallbackBridge.getCurrentMods(), true);
        sendMouseKeycode(keycode, CallbackBridge.getCurrentMods(), false);
    }

    public static void sendScroll(double xoffset, double yoffset) {
        nativeSendScroll(xoffset, yoffset);
        if (!sdlEnabled) return;
        SDLActivity.onNativeMouse(0, MotionEvent.ACTION_SCROLL, (float) xoffset, (float) yoffset, false);
    }

    public static void sendUpdateWindowSize(int w, int h) {
        nativeSendScreenSize(w, h);
        if (sdlEnabled && SDLActivity.getSDLSurface() != null) {
            // 通知 SDL 原生 surface 尺寸变化，保证输入处理正确
            SDLActivity.getSDLSurface().nativeResize(w, h);
        }
    }

    public static boolean isGrabbing() {
        // Avoid going through the JNI each time.
        return isGrabbing;
    }

    // Called from JRE side
    @SuppressWarnings("unused")
    public static @Nullable String accessAndroidClipboard(int type, String copy) {
        Activity activity = FCLApplication.getCurrentActivity();
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

    /**
     * 由 JNI 侧（native_hooks）调用：SDL_InitSubSystem 被调用时通知启动器初始化 SDL 集成。
     * @return 通知是否成功
     */
    @SuppressWarnings("unused")
    public static boolean notifyLauncher(int type, int... action) {
        switch (type) {
            case NOTIF_TYPE_SDL:
                if (action.length > 0 && action[0] == ACTION_INIT_LAUNCHER_INTEGRATION) {
                    try {
                        // SDL3 库已由 APK 打包（FCL/libs/SDL-release.aar），
                        // 从 dalvik 加载并触发 ART 调用 JNI_OnLoad（应用 classloader 上下文）
                        System.loadLibrary("SDL3");
                        System.loadLibrary("SDL2");
                        org.libsdl.app.SDL.setupJNI();
                        onDirectInputEnable();
                        sdlEnabled = true;
                        SDLSurface surface = SDLActivity.getSDLSurface();
                        if (surface != null) {
                            // 尺寸兜底：windowWidth/Height 可能尚未由 JVMActivity 赋值，
                            // 用屏幕真实像素，避免 SDL 原生侧拿到 0x0 导致黑屏
                            if (windowWidth <= 0 || windowHeight <= 0) {
                                DisplayMetrics metrics = new DisplayMetrics();
                                Activity activity = FCLApplication.getCurrentActivity();
                                if (activity != null) {
                                    activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
                                    windowWidth = metrics.widthPixels;
                                    windowHeight = metrics.heightPixels;
                                }
                            }
                            // 补发 surface 就绪：externalInitialize 时 sdlEnabled 还是 false，
                            // SDLSurface.surfaceCreated/surfaceChanged 被早退跳过，SDL 原生侧
                            // 从未收到 onNativeSurfaceCreated/Changed，这里在 sdlEnabled 之后补发
                            try {
                                surface.surfaceChanged(null, 0, windowWidth > 0 ? windowWidth : 1, windowHeight > 0 ? windowHeight : 1);
                            } catch (Throwable ignored) {
                            }
                            if (windowWidth > 0 && windowHeight > 0) {
                                // 通知 SDL 原生 surface 尺寸，输入处理需要
                                surface.nativeResize(windowWidth, windowHeight);
                            }
                        }
                        Log.i("CallbackBridge", "SDL support enabled!");
                        return true;
                    } catch (Exception e) {
                        Log.e("CallbackBridge", "Failed to initialize SDL launcher-side integration!", e);
                    }
                }
                if (action.length > 0 && action[0] == ACTION_SEND_TEXTBOX_RECT) {
                    // 预留：文本输入框位置同步
                }
        }
        return false;
    }

    // 由 JNI 侧调用：手柄直通模式启用
    @SuppressWarnings("unused")
    private static void onDirectInputEnable() {
        Log.i("CallbackBridge", "onDirectInputEnable()");
        sGamepadDirectInput = true;
    }

    // 由 JNI 侧调用：用于实现 glfwGetWindowContentScale（imgui-java 等需要）
    @SuppressWarnings("unused")
    private static float getAndroidDPI() {
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.setToDefaults();
        // 分辨率被缩放，DPI 也要同步缩放
        float scaleFactor = fclBridge != null ? (float) fclBridge.getScaleFactor() : 1f;
        return metrics.density * scaleFactor;
    }

    //Called from JRE side
    @SuppressWarnings("unused")
    private static void onGrabStateChanged(final boolean grabbing) {
        isGrabbing = grabbing;
        sChoreographer.postFrameCallbackDelayed((time) -> {
            // If the grab re-changed, skip notify process
            if (isGrabbing != grabbing) {
                return;
            }
            synchronized (grabListener) {
                grabListener.accept(isGrabbing);
            }
        }, 16);

    }

    @CriticalNative
    public static native void nativeSetUseInputStackQueue(boolean useInputStackQueue);

    // SDL 相关 JNI
    public static native boolean nativeEnableGamepadDirectInput();

    public static native float nativeGetAndroidDPI();

    public static native boolean nativeNotifyLauncher(int type, int[] action);

    public static native void nativeInitializeSDLSubsystems();

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

