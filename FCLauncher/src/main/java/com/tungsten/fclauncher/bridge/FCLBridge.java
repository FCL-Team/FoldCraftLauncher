package com.tungsten.fclauncher.bridge;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.tungsten.fclauncher.utils.FCLPath;

import java.io.File;
import java.io.Serializable;

public class FCLBridge implements Serializable {

    public static final int HIT_RESULT_TYPE_UNKNOWN          = 0;
    public static final int HIT_RESULT_TYPE_MISS             = 1;
    public static final int HIT_RESULT_TYPE_BLOCK            = 2;
    public static final int HIT_RESULT_TYPE_ENTITY           = 3;

    public static final int INJECTOR_MODE_ENABLE             = 1;
    public static final int INJECTOR_MODE_DISABLE            = 0;

    public static final int KeyPress                         = 2;
    public static final int KeyRelease                       = 3;
    public static final int ButtonPress                      = 4;
    public static final int ButtonRelease                    = 5;
    public static final int MotionNotify                     = 6;
    public static final int ConfigureNotify                  = 22;
    public static final int FCLMessage                       = 37;

    public static final int Button1                          = 1;
    public static final int Button2                          = 2;
    public static final int Button3                          = 3;
    public static final int Button4                          = 4;
    public static final int Button5                          = 5;
    public static final int Button6                          = 6;
    public static final int Button7                          = 7;

    public static final int CursorEnabled                    = 1;
    public static final int CursorDisabled                   = 0;

    public static final int ShiftMask                        = 1 << 0;
    public static final int LockMask                         = 1 << 1;
    public static final int ControlMask                      = 1 << 2;
    public static final int Mod1Mask                         = 1 << 3;
    public static final int Mod2Mask                         = 1 << 4;
    public static final int Mod3Mask                         = 1 << 5;
    public static final int Mod4Mask                         = 1 << 6;
    public static final int Mod5Mask                         = 1 << 7;

    public static final int CloseRequest                     = 0;

    private FCLBridgeCallback callback;

    private double scaleFactor = 1f;
    private String controller = "Default";
    private String gameDir;
    private String logPath;
    private String renderer;
    private String java;
    private Surface surface;
    private Handler handler;
    private Thread thread;
    private Thread fclLogThread;

    static {
        System.loadLibrary("xhook");
        System.loadLibrary("fcl");
        System.loadLibrary("fcl_awt");
    }

    public FCLBridge() {
    }

    public native void setFCLNativeWindow(Surface surface);
    public native int redirectStdio(String path);
    public native int chdir(String path);
    public native void setenv(String key, String value);
    public native int dlopen(String path);
    public native void setLdLibraryPath(String path);
    public native int setupExitTrap(FCLBridge bridge);
    public native void setEventPipe();
    public native void pushEvent(long time, int type, int keycode, int keyChar);
    public native void refreshHitResultType();
    public native void setupJLI();
    public native int jliLaunch(String[] args);

    public native void setFCLBridge(FCLBridge fclBridge);

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Thread getThread() {
        return thread;
    }

    public FCLBridgeCallback getCallback() {
        return callback;
    }

    public void execute(Surface surface, FCLBridgeCallback callback) {
        this.handler = new Handler();
        this.callback = callback;
        this.surface = surface;
        fclLogThread = new Thread(() -> {
            receiveLog("invoke redirectStdio");
            int errorCode = redirectStdio(getLogPath());
            if (errorCode != 0) {
                receiveLog("Can't exec redirectStdio! Error code: " + errorCode);
            }
        });
        fclLogThread.setName("FCLLogThread");
        fclLogThread.start();
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

    // FCLBridge callbacks
    public void onExit(int code) {
        if (callback != null) {
            callback.onLog("OpenJDK exited with code : " + code);
            callback.onExit(code);
        }
    }

    public void setHitResultType(int type) {
        if (callback != null) {
            callback.onHitResultTypeChange(type);
        }
    }

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

    public static void openLink(final String link) {
        Context context = FCLPath.CONTEXT;
        ((Activity) context).runOnUiThread(() -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String targetLink = link;
                if (targetLink.startsWith("file://")) {
                    targetLink = targetLink.replace("file://", "");
                } else if (targetLink.startsWith("file:")) {
                    targetLink = targetLink.replace("file:", "");
                }
                Uri uri;
                if (targetLink.startsWith("http")) {
                    uri = Uri.parse(targetLink);
                } else {
                    //can`t get authority by R.string.file_browser_provider
                    uri = FileProvider.getUriForFile(context, "com.tungsten.fcl.provider", new File(targetLink));
                }
                intent.setDataAndType(uri, "*/*");
                context.startActivity(intent);
            } catch (Exception e) {
                Log.e("openLink error", e.toString());
            }
        });
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getController() {
        return controller;
    }

    public void setGameDir(String gameDir) {
        this.gameDir = gameDir;
    }

    @Nullable
    public String getGameDir() {
        return gameDir;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setJava(String java) {
        this.java = java;
    }

    public String getJava() {
        return java;
    }

    @NonNull
    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public void setLogPipeReady() {
        receiveLog("invoke setLogPipeReady");
        handler.post(() -> {
            receiveLog("invoke setFCLBridge");
            setFCLBridge(this);
            // set graphic output and event pipe
            if (surface != null) {
                receiveLog("invoke setFCLNativeWindow");
                setFCLNativeWindow(surface);
            }
            receiveLog("invoke setEventPipe");
            setEventPipe();

            // start
            if (thread != null) {
                thread.start();
            }
        });
    }

    public void receiveLog(String log) {
        if (callback != null) {
            callback.onLog(log);
        }
    }
}
