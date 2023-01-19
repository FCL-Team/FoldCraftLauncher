package com.tungsten.fcl.control;

import android.view.KeyEvent;
import android.view.View;

import com.tungsten.fcl.activity.JVMActivity;
import com.tungsten.fcl.activity.JVMCrashActivity;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class GameMenu implements MenuCallback {

    private JVMActivity activity;
    private FCLBridge fclBridge;

    @Override
    public View getLayout() {
        return null;
    }

    @Override
    public void setup(JVMActivity activity, FCLBridge fclBridge) {
        this.activity = activity;
        this.fclBridge = fclBridge;
        if (!Controllers.isInitialized()) {
            Controllers.init();
        }
    }

    @Override
    public FCLBridgeCallback getCallbackBridge() {
        return new FCLProcessListener(this);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onGraphicOutput() {

    }

    @Override
    public void onCursorModeChange(int mode) {

    }

    private boolean firstLog = true;

    @Override
    public void onLog(String log) {
        try {
            if (firstLog) {
                FileUtils.writeText(new File(fclBridge.getLogPath()), log + "\n");
                firstLog = false;
            } else {
                FileUtils.writeTextWithAppendMode(new File(fclBridge.getLogPath()), log + "\n");
            }
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Can't log game log to target file", e.getMessage());
        }
    }

    @Override
    public void onExit(int exitCode) {
        if (exitCode != 0) {
            JVMCrashActivity.startCrashActivity(activity, exitCode);
            Logging.LOG.log(Level.INFO, "JVM crashed, start jvm crash activity to show errors now!");
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    static class FCLProcessListener implements FCLBridgeCallback {

        private final GameMenu gameMenu;

        public FCLProcessListener(GameMenu gameMenu) {
            this.gameMenu = gameMenu;
        }

        @Override
        public void onCursorModeChange(int mode) {
            gameMenu.onCursorModeChange(mode);
        }

        @Override
        public void onLog(String log) {
            gameMenu.onLog(log);
        }

        @Override
        public void onExit(int code) {
            gameMenu.onExit(code);
        }
    }
}
