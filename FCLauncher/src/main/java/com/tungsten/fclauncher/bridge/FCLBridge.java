package com.tungsten.fclauncher.bridge;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
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
    public static final int KeyChar = 7;
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

    public static boolean BACKEND_IS_BOAT = false;

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
    private SurfaceTexture surfaceTexture;
    private String modSummary;
    private boolean hasTouchController = false;

    static {
        System.loadLibrary("fcl");
        System.loadLibrary("pojavexec_awt");
    }

    public FCLBridge() {
    }

    public static native void nativeClipboardReceived(String data, String mimeTypeSub);

    public native int[] renderAWTScreenFrame();

    public native void nativeSendData(int type, int i1, int i2, int i3, int i4);

    public native void nativeMoveWindow(int x, int y);

    public native int redirectStdio(String path);

    public native int chdir(String path);

    public native void setenv(String key, String value);

    public native long dlopen(String path);

    public native void setLdLibraryPath(String path);

    public native void setupExitTrap(FCLBridge bridge);

    public native void refreshHitResultType();

    public native void setFCLBridge(FCLBridge fclBridge);

    //boat backend
    public native void setFCLNativeWindow(Surface surface);

    public native void setEventPipe();

    public native void pushEvent(long time, int type, int keycode, int keyChar);

    public static native int nativeGetFps();


    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Thread getThread() {
        return thread;
    }

    public SurfaceTexture getSurfaceTexture() {
        return surfaceTexture;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
    }

    public FCLBridgeCallback getCallback() {
        return callback;
    }

    public void execute(Surface surface, FCLBridgeCallback callback) {
        this.handler = new Handler();
        this.callback = callback;
        this.surface = surface;
        setFCLBridge(this);
        CallbackBridge.setFCLBridge(this);
        receiveLog("invoke redirectStdio" + "\n");
        int errorCode = redirectStdio(getLogPath());
        if (errorCode != 0) {
            receiveLog("Can't exec redirectStdio! Error code: " + errorCode + "\n");
        }
        receiveLog("invoke setLogPipeReady" + "\n");
        // set graphic output and event pipe
        if (surface != null) {
            handleWindow();
        }
        receiveLog("invoke setEventPipe" + "\n");
        if (BACKEND_IS_BOAT) {
            setEventPipe();
        }

        // start
        if (thread != null) {
            thread.start();
        }
    }

    public void pushEventMouseButton(int button, boolean press) {
        if (BACKEND_IS_BOAT) {
            if (button == Button2) {
                button = Button3;
            } else if (button == Button3) {
                button = Button2;
            }
            pushEvent(System.nanoTime(), press ? ButtonPress : ButtonRelease, button + 1, 0);
        } else {
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
    }

    public void pushEventPointer(int x, int y) {
        if (BACKEND_IS_BOAT) {
            pushEvent(System.nanoTime(), MotionNotify, x, y);
        } else {
            CallbackBridge.sendCursorPos(x, y);
        }
    }

    public void pushEventPointer(float x, float y) {
        if (BACKEND_IS_BOAT) {
            pushEventPointer((int) x, (int) y);
        } else {
            CallbackBridge.sendCursorPos(x, y);
        }
    }

    public void pushEventKey(int keyCode, int keyChar, boolean press) {
        if (BACKEND_IS_BOAT) {
            pushEvent(System.nanoTime(), press ? KeyPress : KeyRelease, keyCode, keyChar);
        } else {
            CallbackBridge.sendKeycode(keyCode, (char) keyChar, 0, CallbackBridge.getCurrentMods(), press);
        }
    }

    public void pushEventChar(char keyChar) {
        if (BACKEND_IS_BOAT) {
            pushEvent(System.nanoTime(), KeyChar, FCLKeycodes.KEY_RESERVED, keyChar);
        } else {
            CallbackBridge.sendChar(keyChar, 0);
        }
    }

    public void pushEventWindow(int width, int height) {
        if (BACKEND_IS_BOAT) {
            pushEvent(System.nanoTime(), ConfigureNotify, width, height);
        } else {
            CallbackBridge.sendUpdateWindowSize(width, height);
        }
    }

    public void pushEventMessage(int msg) {
        pushEvent(System.nanoTime(), FCLMessage, msg, 0);
    }

    // FCLBridge callbacks
    public void onExit(int code) {
        if (callback != null) {
            callback.onLog("OpenJDK exited with code : " + code + "\n");
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
                    uri = FileProvider.getUriForFile(context, ((Activity) context).getApplication().getPackageName() + ".provider", new File(targetLink));
                }
                intent.setDataAndType(uri, "*/*");
                context.startActivity(Intent.createChooser(intent, ""));
            } catch (Exception e) {
                Log.e("openLink error", "link:" + link + " err:" + e.toString());
            }
        });
    }

    public static void querySystemClipboard() {
        Context context = FCLPath.CONTEXT;
        ClipboardManager clipboard = (ClipboardManager) FCLPath.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
        ((Activity) context).runOnUiThread(() -> {
            ClipData clipData = clipboard.getPrimaryClip();
            if (clipData == null) {
                nativeClipboardReceived(null, null);
                return;
            }
            ClipData.Item firstClipItem = clipData.getItemAt(0);
            //TODO: coerce to HTML if the clip item is styled
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
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = null;
            switch (mimeType) {
                case "text/plain":
                    clipData = ClipData.newPlainText("AWT Paste", data);
                    break;
                case "text/html":
                    clipData = ClipData.newHtmlText("AWT Paste", data, data);
            }
            if (clipData != null) clipboard.setPrimaryClip(clipData);
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

    private void handleWindow() {
        if (gameDir != null) {
            receiveLog("invoke setFCLNativeWindow" + "\n");
            if (BACKEND_IS_BOAT) {
                setFCLNativeWindow(surface);
            } else {
                CallbackBridge.setupBridgeWindow(surface);
            }
        } else {
            receiveLog("start Android AWT Renderer thread" + "\n");
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
                    handler.post(() -> receiveLog(throwable + "\n"));
                }
                rgbArrayBitmap.recycle();
                surface.release();
            }, "AndroidAWTRenderer");
            canvasThread.start();
        }
    }

    public static int getFps() {
        if (BACKEND_IS_BOAT) {
            return nativeGetFps();
        } else {
            return CallbackBridge.getFps();
        }
    }

    public String getModSummary() {
        return modSummary;
    }

    public void setModSummary(String modSummary) {
        this.modSummary = modSummary;
    }

    public boolean hasTouchController() {
        return hasTouchController;
    }

    public void setHasTouchController(boolean hasTouchController) {
        this.hasTouchController = hasTouchController;
    }
}
