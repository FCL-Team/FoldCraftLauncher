package com.tungsten.fclauncher.bridge;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Surface;

import com.tungsten.fclauncher.FCLPath;

import java.io.Serializable;

public class FCLBridge implements Serializable {

    public static final int KeyPress              = 2;
    public static final int KeyRelease            = 3;
    public static final int ButtonPress           = 4;
    public static final int ButtonRelease         = 5;
    public static final int MotionNotify          = 6;
    public static final int ConfigureNotify       = 22;
    public static final int FCLMessage            = 37;

    public static final int Button1               = 1;
    public static final int Button2               = 2;
    public static final int Button3               = 3;
    public static final int Button4               = 4;
    public static final int Button5               = 5;
    public static final int Button6               = 6;
    public static final int Button7               = 7;

    public static final int CursorEnabled         = 1;
    public static final int CursorDisabled        = 0;

    public static final int ShiftMask             = 1 << 0;
    public static final int LockMask              = 1 << 1;
    public static final int ControlMask           = 1 << 2;
    public static final int Mod1Mask              = 1 << 3;
    public static final int Mod2Mask              = 1 << 4;
    public static final int Mod3Mask              = 1 << 5;
    public static final int Mod4Mask              = 1 << 6;
    public static final int Mod5Mask              = 1 << 7;

    public static final int CloseRequest          = 0;

    public FCLBridgeCallback callback;

    static {
        System.loadLibrary("xhook");
        System.loadLibrary("fcl");
        System.loadLibrary("glfw");
    }

    private Thread thread;

    public FCLBridge(FCLBridgeCallback callback) {
        this.callback = callback;
    }

    public native void setFCLNativeWindow(Surface surface);
    public native void redirectStdio(String path);
    public native int chdir(String path);
    public native void setenv(String key, String value);
    public native int dlopen(String path);
    public native void setLdLibraryPath(String path);
    public native int setupExitTrap(FCLBridge bridge);
    public native void setEventPipe();
    public native void pushEvent(long time, int type, int keycode, int keyChar);
    public native void setupJLI();
    public native int jliLaunch(String[] args);

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Thread getThread() {
        return thread;
    }

    public void execute(Surface surface, FCLBridgeCallback callback) {
        this.callback = callback;

        // set graphic output and event pipe
        if (surface != null) {
            setFCLNativeWindow(surface);
        }
        setEventPipe();

        // start
        if (thread != null) {
            thread.start();
        }
    }

    public void pushEventMouseButton(int button, boolean press) {
        pushEvent(System.nanoTime(), press ? ButtonPress : ButtonRelease, button, 0);
    }

    public void pushEventPointer(int x, int y) {
        pushEvent(System.nanoTime(), MotionNotify, x, y);
    }

    public void pushEventKey(int keyCode, int keyChar, boolean press) {
        pushEvent(System.nanoTime(), press ? KeyPress : KeyRelease, keyCode, keyChar);
    }

    public void pushEventWindow(int width, int height) {
        pushEvent(System.nanoTime(), ConfigureNotify, width, height);
    }

    public void pushEventMessage(int msg) {
        pushEvent(System.nanoTime(), FCLMessage, msg, 0);
    }

    // Loader function
    public void onExit(int code) {
        if (callback != null) {
            callback.onExit(code);
        }
    }

    // FCLBridge callbacks
    public void setCursorMode(int mode) {
        if (callback != null) {
            callback.onCursorModeChange(mode);
        }
    }

    public void setPrimaryClipString(String string) {
        ClipboardManager clipboard = (ClipboardManager) FCLPath.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("FCL Clipboard", string);
        clipboard.setPrimaryClip(clip);
    }

    public String getPrimaryClipString() {
        ClipboardManager clipboard = (ClipboardManager) FCLPath.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
        if (!clipboard.hasPrimaryClip()) {
            return null;
        }
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        return item.getText().toString();
    }

}
