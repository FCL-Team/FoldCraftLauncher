package org.lwjgl.glfw;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Choreographer;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.tungsten.fcl.FCLApp;
import com.tungsten.fcl.game.sdl.DirectGamepadEnableHandler;
import com.tungsten.fcl.game.sdl.SdlBridge;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.keycodes.EfficientAndroidLWJGLKeycode;
import com.tungsten.fclauncher.keycodes.LwjglGlfwKeycode;
import com.tungsten.fclauncher.keycodes.LwjglKeycodeMap;
import com.tungsten.fclcore.util.Logging;

import org.libsdl.app.SDLActivity;
import org.libsdl.app.SDLSurface;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.logging.Level;

import dalvik.annotation.optimization.CriticalNative;

public class CallbackBridge {
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private static final Choreographer sChoreographer = Choreographer.getInstance();
    private static FCLBridge fclBridge = null;
    /** 游戏线程写、UI 线程读的抓取状态，公开访问器也可能被其它线程调用 */
    private static volatile boolean isGrabbing = false;
    /** 保护 isGrabbing 的写与「检查+应用」的组合操作，防止应用过期状态 */
    private static final Object grabLock = new Object();

    private static void postFrameCallbackDelayed(Choreographer.FrameCallback callback, long delayMillis) {
        MAIN_HANDLER.post(() -> Choreographer.getInstance().postFrameCallbackDelayed(callback, delayMillis));
    }

    public static final int CLIPBOARD_COPY = 2000;
    public static final int CLIPBOARD_PASTE = 2001;
    public static final int CLIPBOARD_OPEN = 2002;

    // SDL launcher integration. See the AAMC reference implementation:
    // https://github.com/AngelAuraMC/Amethyst-Android
    // Notification types
    public static final int NOTIF_TYPE_SDL = 0;

    // Notification actions
    public static final int ACTION_INIT_LAUNCHER_INTEGRATION = 0;
    public static final int ACTION_SEND_TEXTBOX_RECT = 1;

    // org.lwjgl.sdl.SDLInit 通过这两个常量调用 nativeNotifyLauncher
    public static final int SDL = NOTIF_TYPE_SDL;
    public static final int INIT = ACTION_INIT_LAUNCHER_INTEGRATION;

    /**
     * 由 JRE 侧（sdl_hook JNI）调用的通知入口。
     * @return 通知是否处理成功
     */
    @SuppressWarnings("unused")
    @Keep
    public static boolean notifyLauncher(int type, int... action) {
        if (action == null || action.length == 0) {
            Logging.LOG.log(Level.WARNING, "FCL: SDL notification has no action");
            return false;
        }
        switch (type) {
            case NOTIF_TYPE_SDL:
                if (action[0] == ACTION_INIT_LAUNCHER_INTEGRATION) {
                    if (!SdlBridge.markSdlInitialized()) {
                        return true;
                    }
                    try {
                        Logging.LOG.log(Level.INFO, "FCL: loading SDL3");
                        System.loadLibrary("SDL3");
                        Logging.LOG.log(Level.INFO, "FCL: loading SDL2");
                        System.loadLibrary("SDL2");
                        Logging.LOG.log(Level.INFO, "FCL: setting up SDL JNI");
                        SdlBridge.setupJNI();
                        Logging.LOG.log(Level.INFO, "FCL: binding SDL surface");
                        SdlBridge.setSdlEnabled(true);
                        SDLSurface surface = SDLActivity.getSDLSurface();
                        if (surface != null) {
                            surface.surfaceChanged();
                            if (windowWidth > 0 && windowHeight > 0) {
                                surface.nativeResize(windowWidth, windowHeight);
                            }
                        }
                        Logging.LOG.log(Level.INFO, "FCL: SDL support enabled!");
                        return true;
                    } catch (Throwable e) {
                        SdlBridge.setSdlEnabled(false);
                        SdlBridge.clearSdlInitialized();
                        Logging.LOG.log(Level.WARNING, "FCL: SDL launcher integration is unavailable", e);
                    }
                }
                if (action[0] == ACTION_SEND_TEXTBOX_RECT) {
                    // TODO: 输入框位置同步（后续接入）
                }
        }
        return false;
    }

    /**
     * org.lwjgl.sdl.SDLInit（LWJGL 3.4.1 的 SDL Java 绑定）调用的入口，转发到 {@link #notifyLauncher}。
     * 注意：LWJGL 组件内声明为 native，运行时以本实现为准（避免依赖额外 C 符号）。
     */
    @SuppressWarnings("unused")
    @Keep
    public static void nativeNotifyLauncher(int type, int... action) {
        notifyLauncher(type, action);
    }

    public static volatile int windowWidth, windowHeight;
    public static volatile int physicalWidth, physicalHeight;
    public static float mouseX, mouseY, deltaX, deltaY;
    private static int sMouseButtonState = 0;
    public volatile static boolean holdingAlt, holdingCapslock, holdingCtrl,
            holdingNumlock, holdingShift;

    // GLFW direct gamepad 共享缓冲
    public static final ByteBuffer sGamepadButtonBuffer;
    public static final FloatBuffer sGamepadAxisBuffer;
    public static boolean sGamepadDirectInput = false;
    // Use a weak reference here to avoid possibly statically referencing a Context.
    private static @Nullable WeakReference<DirectGamepadEnableHandler> sDirectGamepadEnableHandler;

    public static void putMouseEventWithCoords(int button, float x, float y) {
        putMouseEventWithCoords(button, true, x, y);
        postFrameCallbackDelayed(l -> putMouseEventWithCoords(button, false, x, y), 33);
    }

    public static void putMouseEventWithCoords(int button, boolean isDown, float x, float y /* , int dz, long nanos */) {
        sendCursorPos(x, y);
        sendMouseKeycode(button, CallbackBridge.getCurrentMods(), isDown);
    }


    public static void sendCursorPos(float x, float y) {
        mouseX = x;
        mouseY = y;
        deltaX = 0f;
        deltaY = 0f;
        nativeSendCursorPos(mouseX, mouseY);
        if (!SdlBridge.getSdlEnabled()) return;
        SDLActivity.onNativeMouse(0, MotionEvent.ACTION_MOVE, x, y, false);
    }

    public static void sendCursorDelta(float x, float y) {
        deltaX = x;
        deltaY = y;
        mouseX += x;
        mouseY += y;
        nativeSendCursorPos(mouseX, mouseY);
        if (!SdlBridge.getSdlEnabled()) return;
        SDLActivity.onNativeMouse(0, MotionEvent.ACTION_MOVE, x, y, true);
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
        if (!SdlBridge.getSdlEnabled()) return;
        int androidKeycode = EfficientAndroidLWJGLKeycode.getSdlAndroidKeycode(keycode);
        if (androidKeycode == KeyEvent.KEYCODE_UNKNOWN) return;
        try {
            if (isDown) {
                SDLActivity.onNativeKeyDown(androidKeycode);
            } else {
                SDLActivity.onNativeKeyUp(androidKeycode);
            }
        } catch (Throwable ignored) {
        }
    }

    public static void sendChar(char keychar, int modifiers) {
        nativeSendCharMods(keychar, modifiers);
        nativeSendChar(keychar);
        if (!SdlBridge.getSdlEnabled()) return;
        SDLActivity.onNativeKeyDown(EfficientAndroidLWJGLKeycode.getAndroidKeycode(keychar));
        SDLActivity.onNativeKeyUp(EfficientAndroidLWJGLKeycode.getAndroidKeycode(keychar));
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
        // SDL 输入双路（按键状态累积后一次性上报，SDL 需要 MotionEvent.getButtonState()）
        if (!SdlBridge.getSdlEnabled()) return;
        int aKey = -1;
        switch (button) {
            case LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_LEFT:
                aKey = MotionEvent.BUTTON_PRIMARY;
                break;
            case LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_RIGHT:
                aKey = MotionEvent.BUTTON_SECONDARY;
                break;
            case LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_MIDDLE:
                aKey = MotionEvent.BUTTON_TERTIARY;
                break;
            // Yes, back and forward are flipped, for some reason it's just flipped on SDL, don't ask
            case LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_5:
                aKey = MotionEvent.BUTTON_BACK;
                break;
            case LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_4:
                aKey = MotionEvent.BUTTON_FORWARD;
                break;
        }
        if (aKey != -1) {
            if (isDown) {
                sMouseButtonState |= aKey;
            } else {
                sMouseButtonState &= ~aKey;
            }
            SDLActivity.onNativeMouse(sMouseButtonState, isDown ? MotionEvent.ACTION_DOWN : MotionEvent.ACTION_UP, mouseX, mouseY, false);
        }
    }

    public static void sendMouseKeycode(int keycode) {
        sendMouseKeycode(keycode, CallbackBridge.getCurrentMods(), true);
        sendMouseKeycode(keycode, CallbackBridge.getCurrentMods(), false);
    }

    public static void sendScroll(double xoffset, double yoffset) {
        nativeSendScroll(xoffset, yoffset);
        // SDL 输入双路
        if (!SdlBridge.getSdlEnabled()) return;
        SDLActivity.onNativeMouse(0, MotionEvent.ACTION_SCROLL, (float) xoffset, (float) yoffset, false);
    }

    public static void sendUpdateWindowSize(int w, int h) {
        windowWidth = w;
        windowHeight = h;
        nativeSendScreenSize(w, h);
    }

    public static boolean isGrabbing() {
        // Avoid going through the JNI each time.
        return isGrabbing;
    }

    public static void resetInputState() {
        nativeResetInputState();
        if (SdlBridge.getSdlEnabled() && sMouseButtonState != 0) {
            SDLActivity.onNativeMouse(0, MotionEvent.ACTION_UP, mouseX, mouseY, false);
        }
        deltaX = 0f;
        deltaY = 0f;
        sMouseButtonState = 0;
        holdingAlt = false;
        holdingCapslock = false;
        holdingCtrl = false;
        holdingNumlock = false;
        holdingShift = false;
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

    public static void setDirectGamepadEnableHandler(@Nullable DirectGamepadEnableHandler handler) {
        sDirectGamepadEnableHandler = new WeakReference<>(handler);
    }

    public static void clearSdlBridgeState() {
        sGamepadDirectInput = false;
        sDirectGamepadEnableHandler = null;
        sMouseButtonState = 0;
        deltaX = 0f;
        deltaY = 0f;
    }

    //Called from JRE side
    @SuppressWarnings("unused")
    @Keep
    private static void onDirectInputEnable() {
        Logging.LOG.log(Level.INFO, "FCL: Direct gamepad input enabled");
        sGamepadDirectInput = true;
        DirectGamepadEnableHandler enableHandler =
                sDirectGamepadEnableHandler == null ? null : sDirectGamepadEnableHandler.get();
        if (enableHandler != null) {
            enableHandler.onDirectGamepadEnabled();
        }
    }

    //Called from JRE side
    @SuppressWarnings("unused")
    private static void onGrabStateChanged(final boolean grabbing) {
        synchronized (grabLock) {
            isGrabbing = grabbing;
        }
        deltaX = 0f;
        deltaY = 0f;
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
    private static native void nativeResetInputState();

    @CriticalNative
    private static native void nativeSendScroll(double xoffset, double yoffset);

    @CriticalNative
    private static native void nativeSendScreenSize(int width, int height);

    public static native void nativeSetWindowAttrib(int attrib, int value);

    public static native void setupBridgeWindow(Object surface);

    public static native void nativeSetGrabbing(boolean grab);

    public static native int getFps();

    private static native ByteBuffer nativeCreateGamepadButtonBuffer();
    private static native ByteBuffer nativeCreateGamepadAxisBuffer();

    static {
        System.loadLibrary("pojavexec");
        sGamepadButtonBuffer = nativeCreateGamepadButtonBuffer();
        sGamepadAxisBuffer = nativeCreateGamepadAxisBuffer().order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
    }
}
