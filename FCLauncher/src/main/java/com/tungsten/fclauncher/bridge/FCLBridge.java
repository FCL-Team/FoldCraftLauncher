package com.tungsten.fclauncher.bridge;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.Surface;

import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fclauncher.utils.LogFileUtil;

import java.io.Serializable;
import java.lang.ref.WeakReference;

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

    private String logPath;

    static {
        System.loadLibrary("xhook");
        System.loadLibrary("fcl");
        System.loadLibrary("glfw");
    }

    private Thread thread;
    private Thread fclLogThread;
    private boolean isLogPipeReady=false;
    private WeakReference<LogReceiver> logReceiver;

    public static int cursorMode=CursorEnabled;

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

    public native void setFCLBridge(FCLBridge fclBridge);

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Thread getThread() {
        return thread;
    }

    public void execute(Surface surface, FCLBridgeCallback callback) {
        this.callback = callback;

        LogFileUtil logFileUtil = LogFileUtil.getInstance();
        logFileUtil.setLogFilePath(getLogPath());
        fclLogThread = new Thread(()->redirectStdio(getLogPath()));
        fclLogThread.setName("FCLLogThread");
        fclLogThread.start();
        while (!isLogPipeReady) {
            //wait for redirectStdio
        }
        setFCLBridge(this);
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
        pushEvent(System.nanoTime(), MotionNotify, (int) (x*0.33), (int) (y*0.3));
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
        cursorMode=mode;
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

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public void setLogPipeReady(){
        this.isLogPipeReady=true;
    }

    public void receiveLog(String log){
        if (logReceiver == null || logReceiver.get() == null) {
            logReceiver = new WeakReference<>(new LogReceiver() {
                @Override
                public void pushLog(String log) {
                    LogFileUtil.getInstance().writeLog(log);
                }
            });
        } else {
            logReceiver.get().pushLog(log);
        }
    }
}
