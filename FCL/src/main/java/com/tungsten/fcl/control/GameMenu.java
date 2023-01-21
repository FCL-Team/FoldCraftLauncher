package com.tungsten.fcl.control;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.GsonBuilder;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.JVMCrashActivity;
import com.tungsten.fcl.control.view.TouchPad;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fcl.setting.MenuSetting;
import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

public class GameMenu implements MenuCallback {

    private boolean simulated;
    private FCLActivity activity;
    @Nullable
    private FCLBridge fclBridge;
    private FCLInput fclInput;
    private MenuSetting menuSetting;
    private int cursorMode = FCLBridge.CursorEnabled;
    private int cursorX;
    private int cursorY;
    private int pointerX;
    private int pointerY;

    private View layout;
    private TouchPad touchPad;
    private FCLProgressBar launchProgress;
    private FCLImageView cursorView;

    public boolean isSimulated() {
        return simulated;
    }

    public MenuSetting getMenuSetting() {
        return menuSetting;
    }

    public int getCursorMode() {
        return cursorMode;
    }

    public int getCursorX() {
        return cursorX;
    }

    public int getCursorY() {
        return cursorY;
    }

    public int getPointerX() {
        return pointerX;
    }

    public int getPointerY() {
        return pointerY;
    }

    public void setCursorX(int cursorX) {
        this.cursorX = cursorX;
    }

    public void setCursorY(int cursorY) {
        this.cursorY = cursorY;
    }

    public void setPointerX(int pointerX) {
        this.pointerX = pointerX;
    }

    public void setPointerY(int pointerY) {
        this.pointerY = pointerY;
    }

    @Override
    public void setup(FCLActivity activity, FCLBridge fclBridge) {
        this.activity = activity;
        this.fclBridge = fclBridge;
        this.simulated = fclBridge == null;
        this.fclInput = new FCLInput(this);
        if (!Controllers.isInitialized()) {
            Controllers.init();
        }

        if (Files.exists(new File(FCLPath.FILES_DIR + "/menu_setting.json").toPath())) {
            try {
                this.menuSetting = new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .fromJson(FileUtils.readText(new File(FCLPath.FILES_DIR + "/menu_setting.json")), MenuSetting.class);
            } catch (IOException e) {
                Logging.LOG.log(Level.WARNING, "Failed to load menu setting, use default", e);
                this.menuSetting = new MenuSetting();
            }
        } else {
            this.menuSetting = new MenuSetting();
        }

        this.menuSetting.addPropertyChangedListener(observable -> {
            String content = new GsonBuilder().setPrettyPrinting().create().toJson(menuSetting);
            try {
                FileUtils.writeText(new File(FCLPath.FILES_DIR + "/menu_setting.json"), content);
            } catch (IOException e) {
                Logging.LOG.log(Level.SEVERE, "Failed to save menu setting", e);
            }
        });

        touchPad = findViewById(R.id.touch_pad);
        launchProgress = findViewById(R.id.launch_progress);
        cursorView = findViewById(R.id.cursor);
        if (!isSimulated()) {
            touchPad.setBackground(ThemeEngine.getInstance().getTheme().getBackground(activity));
            launchProgress.setVisibility(View.VISIBLE);
        }
        touchPad.init(this);
    }

    @Override
    public View getLayout() {
        if (layout == null) {
            layout = LayoutInflater.from(activity).inflate(R.layout.view_game_menu, null);
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
        return new FCLProcessListener(this);
    }

    @Override
    public FCLInput getInput() {
        return fclInput;
    }

    @Override
    public FCLImageView getCursor() {
        return cursorView;
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
        touchPad.setBackground(new ColorDrawable(Color.TRANSPARENT));
        touchPad.removeView(launchProgress);
    }

    @Override
    public void onCursorModeChange(int mode) {
        this.cursorMode = mode;
        if (mode == FCLBridge.CursorEnabled) {
            getCursor().setVisibility(View.VISIBLE);
        } else {
            getCursor().setVisibility(View.GONE);
        }
    }

    private boolean firstLog = true;

    @Override
    public void onLog(String log) {
        if (fclBridge != null) {
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
    }

    @Override
    public void onExit(int exitCode) {
        if (exitCode != 0) {
            JVMCrashActivity.startCrashActivity(activity, exitCode);
            Logging.LOG.log(Level.INFO, "JVM crashed, start jvm crash activity to show errors now!");
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @NonNull
    public final <T extends View> T findViewById(int id) {
        return getLayout().findViewById(id);
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
