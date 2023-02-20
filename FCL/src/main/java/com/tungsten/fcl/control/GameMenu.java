package com.tungsten.fcl.control;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.GsonBuilder;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.JVMCrashActivity;
import com.tungsten.fcl.control.keyboard.LwjglCharSender;
import com.tungsten.fcl.control.keyboard.TouchCharInput;
import com.tungsten.fcl.control.view.GameItemBar;
import com.tungsten.fcl.control.view.LogWindow;
import com.tungsten.fcl.control.view.TouchPad;
import com.tungsten.fcl.control.view.ViewManager;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fcl.setting.MenuSetting;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fclauncher.FCLKeycodes;
import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLSeekBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;

public class GameMenu implements MenuCallback, View.OnClickListener {

    private boolean simulated;
    private FCLActivity activity;
    @Nullable
    private FCLBridge fclBridge;
    private FCLInput fclInput;
    private MenuSetting menuSetting;
    private int cursorMode = FCLBridge.CursorEnabled;
    private int hitResultType = FCLBridge.HIT_RESULT_TYPE_UNKNOWN;
    private int cursorX;
    private int cursorY;
    private int pointerX;
    private int pointerY;

    private View layout;
    private TouchPad touchPad;
    private GameItemBar gameItemBar;
    private LogWindow logWindow;
    private TouchCharInput touchCharInput;
    private FCLProgressBar launchProgress;
    private FCLImageView cursorView;
    private ViewManager viewManager;
    private Gyroscope gyroscope;

    private FCLButton openMultiplayerMenu;
    private FCLButton manageQuickInput;
    private FCLButton forceExit;

    public FCLActivity getActivity() {
        return activity;
    }

    public boolean isSimulated() {
        return simulated;
    }

    public MenuSetting getMenuSetting() {
        return menuSetting;
    }

    public int getCursorMode() {
        return cursorMode;
    }

    public int getHitResultType() {
        return hitResultType;
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

    public ViewManager getViewManager() {
        return viewManager;
    }

    public TouchPad getTouchPad() {
        return touchPad;
    }

    private void initLeftMenu() {

    }

    private void initRightMenu() {
        FCLSwitch lockMenuSwitch = findViewById(R.id.switch_lock_view);
        FCLSwitch disableGestureSwitch = findViewById(R.id.switch_gesture);
        FCLSwitch disableBEGestureSwitch = findViewById(R.id.switch_be_gesture);
        FCLSwitch gyroSwitch = findViewById(R.id.switch_gyro);
        FCLSwitch showLogSwitch = findViewById(R.id.switch_show_log);

        FCLSpinner<GestureMode> gestureModeSpinner = findViewById(R.id.gesture_mode_spinner);
        FCLSpinner<MouseMoveMode> mouseMoveModeSpinner = findViewById(R.id.mouse_mode_spinner);

        FCLSeekBar mouseSensitivitySeekbar = findViewById(R.id.mouse_sensitivity);
        FCLSeekBar mouseSizeSeekbar = findViewById(R.id.mouse_size);
        FCLSeekBar gyroSensitivitySeekbar = findViewById(R.id.gyro_sensitivity);

        FCLTextView mouseSensitivityText = findViewById(R.id.mouse_sensitivity_text);
        FCLTextView mouseSizeText = findViewById(R.id.mouse_size_text);
        FCLTextView gyroSensitivityText = findViewById(R.id.gyro_sensitivity_text);

        openMultiplayerMenu = findViewById(R.id.open_multiplayer_menu);
        manageQuickInput = findViewById(R.id.open_quick_input);
        forceExit = findViewById(R.id.force_exit);

        FXUtils.bindBoolean(lockMenuSwitch, menuSetting.lockMenuViewProperty());
        FXUtils.bindBoolean(disableGestureSwitch, menuSetting.disableGestureProperty());
        FXUtils.bindBoolean(disableBEGestureSwitch, menuSetting.disableBEGestureProperty());
        FXUtils.bindBoolean(gyroSwitch, menuSetting.enableGyroscopeProperty());
        FXUtils.bindBoolean(showLogSwitch, logWindow.visibilityProperty());

        ArrayList<GestureMode> gestureModeDataList = new ArrayList<>();
        gestureModeDataList.add(GestureMode.BUILD);
        gestureModeDataList.add(GestureMode.FIGHT);
        gestureModeSpinner.setDataList(gestureModeDataList);
        ArrayList<MouseMoveMode> mouseMoveModeDataList = new ArrayList<>();
        mouseMoveModeDataList.add(MouseMoveMode.CLICK);
        mouseMoveModeDataList.add(MouseMoveMode.SLIDE);
        mouseMoveModeSpinner.setDataList(mouseMoveModeDataList);
        ArrayList<String> gestureModeList = new ArrayList<>();
        gestureModeList.add(activity.getString(R.string.menu_settings_gesture_mode_build));
        gestureModeList.add(activity.getString(R.string.menu_settings_gesture_mode_fight));
        ArrayAdapter<String> gestureModeAdapter = new ArrayAdapter<>(activity, R.layout.item_spinner_small, gestureModeList);
        gestureModeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown_small);
        gestureModeSpinner.setAdapter(gestureModeAdapter);
        ArrayList<String> mouseMoveModeList = new ArrayList<>();
        mouseMoveModeList.add(activity.getString(R.string.menu_settings_mouse_mode_click));
        mouseMoveModeList.add(activity.getString(R.string.menu_settings_mouse_mode_slide));
        ArrayAdapter<String> mouseMoveModeAdapter = new ArrayAdapter<>(activity, R.layout.item_spinner_small, mouseMoveModeList);
        mouseMoveModeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown_small);
        mouseMoveModeSpinner.setAdapter(mouseMoveModeAdapter);
        FXUtils.bindSelection(gestureModeSpinner, menuSetting.gestureModeProperty());
        FXUtils.bindSelection(mouseMoveModeSpinner, menuSetting.mouseMoveModeProperty());

        mouseSensitivitySeekbar.addProgressListener();
        IntegerProperty mouseSensitivityProperty = new SimpleIntegerProperty((int) (menuSetting.getMouseSensitivity() * 100)) {
            @Override
            protected void invalidated() {
                super.invalidated();
                double doubleValue = get() / 100d;
                menuSetting.setMouseSensitivity(doubleValue);
            }
        };
        mouseSensitivitySeekbar.progressProperty().bindBidirectional(mouseSensitivityProperty);
        mouseSizeSeekbar.addProgressListener();
        mouseSizeSeekbar.progressProperty().bindBidirectional(menuSetting.mouseSizeProperty());
        gyroSensitivitySeekbar.addProgressListener();
        gyroSensitivitySeekbar.progressProperty().bindBidirectional(menuSetting.gyroscopeSensitivityProperty());

        mouseSensitivityText.stringProperty().bind(Bindings.createStringBinding(() -> mouseSensitivityProperty.get() + " %", mouseSensitivityProperty));
        mouseSizeText.stringProperty().bind(Bindings.createStringBinding(() -> menuSetting.mouseSizeProperty().get() + " dp", menuSetting.mouseSizeProperty()));
        gyroSensitivityText.stringProperty().bind(Bindings.createStringBinding(() -> menuSetting.gyroscopeSensitivityProperty().get() + "", menuSetting.gyroscopeSensitivityProperty()));

        openMultiplayerMenu.setOnClickListener(this);
        manageQuickInput.setOnClickListener(this);
        forceExit.setOnClickListener(this);
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
        gameItemBar = findViewById(R.id.game_item_bar);
        logWindow = findViewById(R.id.log_window);
        touchCharInput = findViewById(R.id.input_scanner);
        launchProgress = findViewById(R.id.launch_progress);
        cursorView = findViewById(R.id.cursor);
        if (!isSimulated()) {
            touchPad.setBackground(ThemeEngine.getInstance().getTheme().getBackground(activity));
            launchProgress.setVisibility(View.VISIBLE);
            touchPad.post(() -> gameItemBar.setup(this));
        }
        touchPad.init(this);
        touchCharInput.setCharacterSender(new LwjglCharSender(this));
        ViewGroup.LayoutParams layoutParams = cursorView.getLayoutParams();
        layoutParams.width = ConvertUtils.dip2px(activity, menuSetting.mouseSizeProperty().get());
        layoutParams.height = ConvertUtils.dip2px(activity, menuSetting.mouseSizeProperty().get());
        cursorView.setLayoutParams(layoutParams);
        menuSetting.mouseSizeProperty().addListener(observable -> {
            ViewGroup.LayoutParams params = cursorView.getLayoutParams();
            params.width = ConvertUtils.dip2px(activity, menuSetting.mouseSizeProperty().get());
            params.height = ConvertUtils.dip2px(activity, menuSetting.mouseSizeProperty().get());
            cursorView.setLayoutParams(params);
        });

        gyroscope = new Gyroscope(this);
        gyroscope.enableProperty().bind(menuSetting.enableGyroscopeProperty());

        viewManager = new ViewManager(this);
        viewManager.setup();

        initLeftMenu();
        initRightMenu();
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
        if (cursorMode == FCLBridge.CursorDisabled) {
            fclInput.sendKeyEvent(FCLKeycodes.KEY_ESC, true);
            fclInput.sendKeyEvent(FCLKeycodes.KEY_ESC, false);
        }
        gyroscope.disableSensor();
    }

    @Override
    public void onResume() {
        if (menuSetting != null && menuSetting.isEnableGyroscope() && gyroscope != null) {
            gyroscope.enableSensor();
        }
    }

    @Override
    public void onBackPressed() {
        boolean mouse = false;
        final int[] devices = InputDevice.getDeviceIds();
        for (int j : devices) {
            InputDevice device = InputDevice.getDevice(j);
            if (device != null && !device.isVirtual()) {
                if (device.getName().contains("Mouse") || (touchCharInput != null && !touchCharInput.isEnabled())) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || device.isExternal()) {
                        mouse = true;
                        break;
                    }
                }
            }
        }
        if (!mouse) {
            fclInput.sendKeyEvent(FCLKeycodes.KEY_ESC, true);
            fclInput.sendKeyEvent(FCLKeycodes.KEY_ESC, false);
        }
    }

    @Override
    public void onGraphicOutput() {
        touchPad.setBackground(new ColorDrawable(Color.TRANSPARENT));
        touchPad.removeView(launchProgress);
    }

    @Override
    public void onCursorModeChange(int mode) {
        this.cursorMode = mode;
        activity.runOnUiThread(() -> {
            if (mode == FCLBridge.CursorEnabled) {
                getCursor().setVisibility(View.VISIBLE);
                gameItemBar.setVisibility(View.GONE);
            } else {
                getCursor().setVisibility(View.GONE);
                gameItemBar.setVisibility(View.VISIBLE);
            }
        });
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

    @Override
    public void onClick(View v) {
        if (v == openMultiplayerMenu) {

        }
        if (v == manageQuickInput) {

        }
        if (v == forceExit) {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(activity);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setMessage(activity.getString(R.string.menu_settings_force_exit_msg));
            builder.setPositiveButton(() -> android.os.Process.killProcess(android.os.Process.myPid()));
            builder.setNegativeButton(null);
            builder.setCancelable(false);
            builder.create().show();
        }
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
        public void onHitResultTypeChange(int type) {
            gameMenu.hitResultType = type;
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
