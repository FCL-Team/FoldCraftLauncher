package com.tungsten.fcl.control;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.BuildConfig;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.JVMCrashActivity;
import com.tungsten.fcl.control.keyboard.AwtCharSender;
import com.tungsten.fcl.control.keyboard.TouchCharInput;
import com.tungsten.fcl.control.view.LogWindow;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclauncher.keycodes.AWTInputEvent;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class JarExecutorMenu implements MenuCallback, View.OnClickListener, View.OnTouchListener {

    private FCLActivity activity;
    private FCLBridge fclBridge;
    private AWTInput awtInput;

    private View layout;
    private View touchPad;
    private LogWindow logWindow;
    private FCLImageView cursorView;
    private TouchCharInput touchCharInput;
    private FCLButton forceExit;
    private FCLButton showLog;
    private FCLButton mouseMode;
    private FCLButton input;
    private FCLButton copy;
    private FCLButton paste;
    private FCLButton mouseLeft;
    private FCLButton mouseRight;
    private FCLButton moveUp;
    private FCLButton moveDown;
    private FCLButton moveLeft;
    private FCLButton moveRight;

    private boolean clickMode = true;
    private int downX;
    private int downY;
    private long downTime;
    private int initialX;
    private int initialY;

    @Override
    public void setup(FCLActivity activity, FCLBridge fclBridge) {
        this.activity = activity;
        this.fclBridge = fclBridge;

        this.awtInput = new AWTInput(this);

        touchPad = findViewById(R.id.touch_pad);
        logWindow = findViewById(R.id.log_window);
        cursorView = findViewById(R.id.cursor);
        touchCharInput = findViewById(R.id.input_scanner);
        touchPad.setOnTouchListener(this);
        logWindow.setVisibilityValue(true);
        touchCharInput.setCharacterSender(null, new AwtCharSender(awtInput));

        forceExit = findViewById(R.id.force_exit);
        showLog = findViewById(R.id.show_log);
        mouseMode = findViewById(R.id.mouse_mode);
        input = findViewById(R.id.input);
        copy = findViewById(R.id.copy);
        paste = findViewById(R.id.paste);
        mouseLeft = findViewById(R.id.mouse_left);
        mouseRight = findViewById(R.id.mouse_right);
        moveUp = findViewById(R.id.move_up);
        moveDown = findViewById(R.id.move_down);
        moveLeft = findViewById(R.id.move_left);
        moveRight = findViewById(R.id.move_right);
        forceExit.setOnClickListener(this);
        showLog.setOnClickListener(this);
        mouseMode.setOnClickListener(this);
        input.setOnClickListener(this);
        copy.setOnClickListener(this);
        paste.setOnClickListener(this);
        mouseLeft.setOnClickListener(this);
        mouseRight.setOnClickListener(this);
        moveUp.setOnClickListener(this);
        moveDown.setOnClickListener(this);
        moveLeft.setOnClickListener(this);
        moveRight.setOnClickListener(this);
    }

    @Override
    public View getLayout() {
        if (layout == null) {
            layout = LayoutInflater.from(activity).inflate(R.layout.view_jar_executor_menu, null);
        }
        return layout;
    }

    @Override
    @Nullable
    public FCLBridge getBridge() {
        return fclBridge;
    }

    @Override
    public FCLBridgeCallback getCallbackBridge() {
        return new JarExecutorProcessListener(this);
    }

    @Override
    public FCLInput getInput() {
        // Ignore
        return null;
    }

    @Override
    public FCLImageView getCursor() {
        return cursorView;
    }

    @Override
    public int getCursorMode() {
        // Ignore
        return 0;
    }

    @Override
    public void onPause() {
        // Ignore
    }

    @Override
    public void onResume() {
        // Ignore
    }

    @Override
    public void onGraphicOutput() {
        // Ignore
    }

    @Override
    public void onCursorModeChange(int mode) {
        // Ignore
    }

    private boolean firstLog = true;

    @Override
    public void onLog(String log) {
        if (log.contains("OR:") || log.contains("ERROR:") || log.contains("INTERNAL ERROR:")) {
            return;
        }
        logWindow.appendLog(log + "\n");
        if (BuildConfig.DEBUG) {
            Log.d("FCL Debug", log);
        }
        try {
            if (firstLog) {
                FileUtils.writeText(new File(fclBridge.getLogPath()), log + "\n");
                firstLog = false;
            } else {
                FileUtils.writeTextWithAppendMode(new File(fclBridge.getLogPath()), log + "\n");
            }
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Can't log jar executor log to target file", e.getMessage());
        }
    }

    @Override
    public void onExit(int exitCode) {
        if (exitCode != 0) {
            JVMCrashActivity.startCrashActivity(false, activity, exitCode, fclBridge.getLogPath(), fclBridge.getRenderer(), fclBridge.getJava());
            Logging.LOG.log(Level.INFO, "JVM crashed, start jvm crash activity to show errors now!");
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onClick(View view) {
        if (view == forceExit) {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(activity);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setMessage(activity.getString(R.string.menu_settings_force_exit_msg));
            builder.setPositiveButton(() -> android.os.Process.killProcess(android.os.Process.myPid()));
            builder.setNegativeButton(null);
            builder.setCancelable(false);
            builder.create().show();
        }
        if (view == showLog) {
            logWindow.setVisibilityValue(!logWindow.getVisibilityValue());
        }
        if (view == mouseMode) {
            clickMode = !clickMode;
            Toast.makeText(activity, clickMode ? activity.getString(R.string.menu_settings_mouse_mode_click) : activity.getString(R.string.menu_settings_mouse_mode_slide), Toast.LENGTH_SHORT).show();
        }
        if (view == input) {
            touchCharInput.switchKeyboardState();
        }
        if (view == copy) {
            awtInput.sendKey(' ', AWTInputEvent.VK_CONTROL, 1);
            awtInput.sendKey(' ', AWTInputEvent.VK_C);
            awtInput.sendKey(' ', AWTInputEvent.VK_CONTROL, 0);
        }
        if (view == paste) {
            awtInput.sendKey(' ', AWTInputEvent.VK_CONTROL, 1);
            awtInput.sendKey(' ', AWTInputEvent.VK_V);
            awtInput.sendKey(' ', AWTInputEvent.VK_CONTROL, 0);
        }
        if (view == mouseLeft) {
            awtInput.sendMousePress(AWTInputEvent.BUTTON1_DOWN_MASK);
        }
        if (view == mouseRight) {
            awtInput.sendMousePress(AWTInputEvent.BUTTON3_DOWN_MASK);
        }
        if (view == moveUp) {
            fclBridge.nativeMoveWindow(0, -10);
        }
        if (view == moveDown) {
            fclBridge.nativeMoveWindow(0, 10);
        }
        if (view == moveLeft) {
            fclBridge.nativeMoveWindow(-10, 0);
        }
        if (view == moveRight) {
            fclBridge.nativeMoveWindow(10, 0);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == touchPad) {
            if (clickMode) {
                awtInput.sendMousePos((int) motionEvent.getX(), (int) motionEvent.getY());
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        awtInput.sendMousePress(AWTInputEvent.BUTTON1_DOWN_MASK, true);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        awtInput.sendMousePress(AWTInputEvent.BUTTON1_DOWN_MASK, false);
                        break;
                    default:
                        break;
                }
            } else {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = (int) motionEvent.getX();
                        downY = (int) motionEvent.getY();
                        downTime = System.currentTimeMillis();
                        initialX = awtInput.getCursorX();
                        initialY = awtInput.getCursorY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int deltaX = (int) (motionEvent.getX() - downX);
                        int deltaY = (int) (motionEvent.getY() - downY);
                        int targetX = Math.max(0, Math.min(touchPad.getMeasuredWidth(), initialX + deltaX));
                        int targetY = Math.max(0, Math.min(touchPad.getMeasuredHeight(), initialY + deltaY));
                        awtInput.sendMousePos(targetX, targetY);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - downTime <= 100
                                && Math.abs(motionEvent.getX() - downX) <= 10
                                && Math.abs(motionEvent.getY() - downY) <= 10) {
                            awtInput.sendMousePress(AWTInputEvent.BUTTON1_DOWN_MASK);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }

    @NonNull
    public final <T extends View> T findViewById(int id) {
        return getLayout().findViewById(id);
    }

    static class JarExecutorProcessListener implements FCLBridgeCallback {

        private final JarExecutorMenu menu;

        public JarExecutorProcessListener(JarExecutorMenu menu) {
            this.menu = menu;
        }

        @Override
        public void onCursorModeChange(int mode) {
            menu.onCursorModeChange(mode);
        }

        @Override
        public void onHitResultTypeChange(int type) {
            // Ignore
        }

        @Override
        public void onLog(String log) {
            menu.onLog(log);
        }

        @Override
        public void onExit(int code) {
            menu.onExit(code);
        }
    }
}
