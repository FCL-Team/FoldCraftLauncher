package com.tungsten.fcl.control;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.BuildConfig;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.JVMCrashActivity;
import com.tungsten.fcl.control.view.LogWindow;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.view.FCLImageView;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class JarExecutorMenu implements MenuCallback {

    private FCLActivity activity;
    private FCLBridge fclBridge;

    private View layout;
    private LogWindow logWindow;
    private FCLImageView cursorView;

    @Override
    public void setup(FCLActivity activity, FCLBridge fclBridge) {
        this.activity = activity;
        this.fclBridge = fclBridge;

        logWindow = findViewById(R.id.log_window);
        cursorView = findViewById(R.id.cursor);
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
