package com.tungsten.fclauncher.bridge;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclauncher.keycodes.LwjglGlfwKeycode;
import com.tungsten.fclauncher.utils.FCLPath;

import org.lwjgl.glfw.CallbackBridge;

import java.io.File;
import java.io.Serializable;

public class FCLBridge implements Serializable {

    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;

    public static final int HIT_RESULT_TYPE_UNKNOWN = 0;
    public static final int HIT_RESULT_TYPE_MISS = 1;
    public static final int HIT_RESULT_TYPE_BLOCK = 2;
    public static final int HIT_RESULT_TYPE_ENTITY = 3;

    public static final int INJECTOR_MODE_ENABLE = 1;
    public static final int INJECTOR_MODE_DISABLE = 0;

    public static final int KeyPress = 2;
    public static final int KeyRelease = 3;
    public static final int ButtonPress = 4;
    public static final int ButtonRelease = 5;
    public static final int MotionNotify = 6;
    public static final int ConfigureNotify = 22;
    public static final int FCLMessage = 37;

    public static final int Button1 = LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_1;
    public static final int Button2 = LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_2;
    public static final int Button3 = LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_3;
    public static final int Button4 = LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_4;
    public static final int Button5 = LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_5;
    public static final int Button6 = LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_6;
    public static final int Button7 = LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_7;

    public static final int CursorEnabled = 1;
    public static final int CursorDisabled = 0;

    public static final int ShiftMask = 1 << 0;
    public static final int LockMask = 1 << 1;
    public static final int ControlMask = 1 << 2;
    public static final int Mod1Mask = 1 << 3;
    public static final int Mod2Mask = 1 << 4;
    public static final int Mod3Mask = 1 << 5;
    public static final int Mod4Mask = 1 << 6;
    public static final int Mod5Mask = 1 << 7;

    public static final int CloseRequest = 0;

    private FCLBridgeCallback callback;

    private double scaleFactor = 1f;
    private String controller = "Default";
    private String gameDir;
    private String logPath;
    private String renderer;
    private String java;
    private Surface surface;
    private boolean surfaceDestroyed;
    private Handler handler;
    private Thread thread;

    static {
        System.loadLibrary("xhook");
        System.loadLibrary("fcl");
        System.loadLibrary("fcl_awt");
    }

    public FCLBridge() {
    }

    public native int[] renderAWTScreenFrame();

    public native void nativeSendData(int type, int i1, int i2, int i3, int i4);

    public static native void nativeClipboardReceived(String data, String mimeTypeSub);

    public native void nativeMoveWindow(int x, int y);

    public native int redirectStdio(String path);

    public native int chdir(String path);

    public native void setenv(String key, String value);

    public native int dlopen(String path);

    public native void setLdLibraryPath(String path);

    public native int setupExitTrap(FCLBridge bridge);

    public native void refreshHitResultType();

    public native void setupJLI();

    public native int jliLaunch(String[] args);

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
        CallbackBridge.setFCLBridge(this);
        receiveLog("invoke redirectStdio");
        int errorCode = redirectStdio(getLogPath());
        if (errorCode != 0) {
            receiveLog("Can't exec redirectStdio! Error code: " + errorCode);
        }
        receiveLog("invoke setLogPipeReady");
        // set graphic output and event pipe
        if (surface != null) {
            handleWindow();
        }
        // start
        if (thread != null) {
            thread.start();
        }
    }

    public void pushEventMouseButton(int button, boolean press) {
        switch (button) {
            case Button4:
                if (press) {
                    CallbackBridge.sendScroll(0, 1d);
                }
                break;
            case Button5:
                if (press) {
                    CallbackBridge.sendScroll(0, -1d);
                }
                break;
            default:
                CallbackBridge.sendMouseButton(button, press);
        }
    }

    public void pushEventPointer(int x, int y) {
        CallbackBridge.sendCursorPos(x, y);
    }

    public void pushEventKey(int keyCode, int keyChar, boolean press) {
        CallbackBridge.sendKeycode(keyCode, (char) keyChar, 0, 0, press);
    }

    public void pushEventWindow(int width, int height) {
        CallbackBridge.sendUpdateWindowSize(width, height);
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

    public void setSurfaceDestroyed(boolean surfaceDestroyed) {
        this.surfaceDestroyed = surfaceDestroyed;
    }

    public boolean isSurfaceDestroyed() {
        return surfaceDestroyed;
    }

    @NonNull
    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public void receiveLog(String log) {
        if (callback != null) {
            callback.onLog(log);
        }
    }

    public void setFCLNativeWindow(Surface surface) {
        CallbackBridge.setupBridgeWindow(surface);
    }

    public static void querySystemClipboard() {
        Context context = FCLPath.CONTEXT;
        ((Activity) context).runOnUiThread(() -> {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData == null) {
                nativeClipboardReceived(null, null);
                return;
            }
            ClipData.Item firstClipItem = clipData.getItemAt(0);
            CharSequence clipItemText = firstClipItem.getText();
            if (clipItemText == null) {
                nativeClipboardReceived(null, null);
                return;
            }
            nativeClipboardReceived(clipItemText.toString(), "plain");
        });
    }

    public static void putClipboardData(String data, String mimeType) {
        Context context = FCLPath.CONTEXT;
        ((Activity) context).runOnUiThread(() -> {
            ClipData clipData = null;
            switch (mimeType) {
                case "text/plain":
                    clipData = ClipData.newPlainText("AWT Paste", data);
                    break;
                case "text/html":
                    clipData = ClipData.newHtmlText("AWT Paste", data, data);
            }
            if (clipData != null) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(clipData);
            }
        });
    }

    private void handleWindow() {
        if (gameDir != null) {
            receiveLog("invoke setFCLNativeWindow");
            setFCLNativeWindow(surface);
        } else {
            receiveLog("start Android AWT Renderer thread");
            Thread canvasThread = new Thread(() -> {
                Canvas canvas;
                Bitmap rgbArrayBitmap = Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888);
                Paint paint = new Paint();
                try {
                    while (!surfaceDestroyed && surface.isValid()) {
                        canvas = surface.lockCanvas(null);
                        canvas.drawRGB(0, 0, 0);
                        int[] rgbArray = renderAWTScreenFrame();
                        if (rgbArray != null) {
                            canvas.save();
                            rgbArrayBitmap.setPixels(rgbArray, 0, DEFAULT_WIDTH, 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                            canvas.drawBitmap(rgbArrayBitmap, 0, 0, paint);
                            canvas.restore();
                        }
                        surface.unlockCanvasAndPost(canvas);
                    }
                } catch (Throwable throwable) {
                    handler.post(() -> receiveLog(throwable.toString()));
                }
                rgbArrayBitmap.recycle();
                surface.release();
            }, "AndroidAWTRenderer");
            canvasThread.start();
        }
    }
}
