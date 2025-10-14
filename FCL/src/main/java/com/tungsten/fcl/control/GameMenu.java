package com.tungsten.fcl.control;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.gson.GsonBuilder;
import com.mio.touchcontroller.TouchController;
import com.mio.touchcontroller.TouchControllerInputView;
import com.mio.ui.dialog.GamepadMapDialog;
import com.mio.ui.view.DraggableTextView;
import com.mio.util.ImageUtil;
import com.tungsten.fcl.BuildConfig;
import com.tungsten.fcl.FCLApplication;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.JVMCrashActivity;
import com.tungsten.fcl.control.data.ButtonStyles;
import com.tungsten.fcl.control.data.ControlButtonData;
import com.tungsten.fcl.control.data.ControlDirectionData;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.control.data.CustomControl;
import com.tungsten.fcl.control.data.DirectionStyles;
import com.tungsten.fcl.control.data.QuickInputTexts;
import com.tungsten.fcl.control.keyboard.LwjglCharSender;
import com.tungsten.fcl.control.keyboard.TouchCharInput;
import com.tungsten.fcl.control.view.GameItemBar;
import com.tungsten.fcl.control.view.LogWindow;
import com.tungsten.fcl.control.view.MenuView;
import com.tungsten.fcl.control.view.TouchPad;
import com.tungsten.fcl.control.view.ViewManager;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fcl.setting.GameOption;
import com.tungsten.fcl.setting.MenuSetting;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLNumberSeekBar;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

import fr.spse.gamepad_remapper.Remapper;
import kotlin.Unit;

public class GameMenu implements MenuCallback, View.OnClickListener {

    private boolean simulated;
    private FCLActivity activity;
    @Nullable
    private FCLBridge fclBridge;
    private FCLInput fclInput;
    private MenuSetting menuSetting;
    private int hitResultType = FCLBridge.HIT_RESULT_TYPE_UNKNOWN;
    private int cursorX;
    private int cursorY;
    private int pointerX;
    private int pointerY;

    private View layout;
    private RelativeLayout baseLayout;
    private TouchPad touchPad;
    private GameItemBar gameItemBar;
    private LogWindow logWindow;
    private DraggableTextView fpsText;
    private TouchCharInput touchCharInput;
    private TouchControllerInputView touchControllerInputView;
    private FCLProgressBar launchProgress;
    private FCLImageView cursorView;
    private ViewManager viewManager;
    private Gyroscope gyroscope;

    private FCLButton manageViewGroups;
    private FCLButton addButton;
    private FCLButton addDirection;
    private FCLButton manageButtonStyle;
    private FCLButton manageDirectionStyle;

    private FCLButton manageQuickInput;
    private FCLButton sendKeycode;
    private FCLButton gamepadResetMapper;
    private FCLButton gamepadButtonBinding;
    private FCLButton forceExit;

    private long time = 0;

    private MenuView menuView;

    private TouchController touchController;

    public void setMenuView(MenuView menuView) {
        this.menuView = menuView;
    }

    public MenuView getMenuView() {
        return menuView;
    }

    public FCLActivity getActivity() {
        return activity;
    }

    public boolean isSimulated() {
        return simulated;
    }

    public MenuSetting getMenuSetting() {
        return menuSetting;
    }

    @Override
    public int getCursorMode() {
        return cursorModeProperty.get();
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

    public RelativeLayout getBaseLayout() {
        return baseLayout;
    }

    public TouchPad getTouchPad() {
        return touchPad;
    }

    public TouchCharInput getTouchCharInput() {
        return touchCharInput;
    }

    private final BooleanProperty editModeProperty = new SimpleBooleanProperty(this, "editMode", false);

    public BooleanProperty editModeProperty() {
        return editModeProperty;
    }

    public void setEditMode(boolean editMode) {
        editModeProperty.set(editMode);
    }

    public boolean isEditMode() {
        return editModeProperty.get();
    }

    private final IntegerProperty cursorModeProperty = new SimpleIntegerProperty(this, "cursorMode", FCLBridge.CursorEnabled);

    public IntegerProperty cursorModeProperty() {
        return cursorModeProperty;
    }

    private final BooleanProperty showViewBoundariesProperty = new SimpleBooleanProperty(this, "showViewBoundaries", false);

    public BooleanProperty showViewBoundariesProperty() {
        return showViewBoundariesProperty;
    }

    public void setShowViewBoundaries(boolean showViewBoundaries) {
        showViewBoundariesProperty.set(showViewBoundaries);
    }

    public boolean isShowViewBoundaries() {
        return showViewBoundariesProperty.get();
    }

    private final BooleanProperty hideAllViewsProperty = new SimpleBooleanProperty(this, "hideAllViews", false);

    public BooleanProperty hideAllViewsProperty() {
        return hideAllViewsProperty;
    }

    public void setHideAllViews(boolean viewVisible) {
        hideAllViewsProperty.set(viewVisible);
    }

    public boolean isHideAllViews() {
        return hideAllViewsProperty.get();
    }

    private final ObjectProperty<Controller> controllerProperty = new SimpleObjectProperty<>(this, "controller", null);

    public ObjectProperty<Controller> controllerProperty() {
        return controllerProperty;
    }

    public void setController(Controller controller) {
        controllerProperty.set(controller);
    }

    public Controller getController() {
        return controllerProperty.get();
    }

    private final ObjectProperty<ControlViewGroup> viewGroupProperty = new SimpleObjectProperty<>(this, "viewGroup", null);

    public ObjectProperty<ControlViewGroup> viewGroupProperty() {
        return viewGroupProperty;
    }

    public void setViewGroup(ControlViewGroup viewGroup) {
        viewGroupProperty.set(viewGroup);
    }

    @Nullable
    public ControlViewGroup getViewGroup() {
        return viewGroupProperty.get();
    }

    private void initLeftMenu() {
        FCLSwitch editMode = findViewById(R.id.edit_mode);
        FCLSwitch showViewBoundaries = findViewById(R.id.show_boundary);
        FCLSwitch hideAllViews = findViewById(R.id.hide_all);
        FCLSwitch autoFit = findViewById(R.id.auto_fit);

        FCLNumberSeekBar autoFitDist = findViewById(R.id.auto_fit_dist);

        FCLSpinner<Controller> currentControllerSpinner = findViewById(R.id.current_controller);
        FCLSpinner<ControlViewGroup> currentViewGroupSpinner = findViewById(R.id.current_view_group);

        FCLLinearLayout editLayout = findViewById(R.id.edit_layout);

        manageViewGroups = findViewById(R.id.manage_view_groups);
        addButton = findViewById(R.id.add_button);
        addDirection = findViewById(R.id.add_direction);
        manageButtonStyle = findViewById(R.id.manage_button_style);
        manageDirectionStyle = findViewById(R.id.manage_direction_style);

        FXUtils.bindBoolean(editMode, editModeProperty);
        FXUtils.bindBoolean(showViewBoundaries, showViewBoundariesProperty);
        FXUtils.bindBoolean(hideAllViews, hideAllViewsProperty);
        FXUtils.bindBoolean(autoFit, menuSetting.getAutoFitProperty());

        autoFitDist.addProgressListener();
        autoFitDist.progressProperty().bindBidirectional(menuSetting.getAutoFitDistProperty());

        ArrayList<String> controllerNameList = Controllers.getControllers().stream().map(Controller::getName).collect(Collectors.toCollection(ArrayList::new));
        currentControllerSpinner.setDataList(new ArrayList<>(Controllers.getControllers()));
        ArrayAdapter<String> controllerNameAdapter = new ArrayAdapter<>(activity, R.layout.item_spinner_small, controllerNameList);
        controllerNameAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown_small);
        currentControllerSpinner.setAdapter(controllerNameAdapter);
        FXUtils.bindSelection(currentControllerSpinner, controllerProperty);

        refreshViewGroupList(currentViewGroupSpinner);
        getController().addListener(i -> refreshViewGroupList(currentViewGroupSpinner));
        controllerProperty.addListener(invalidate -> {
            refreshViewGroupList(currentViewGroupSpinner);
            getController().addListener(i -> refreshViewGroupList(currentViewGroupSpinner));
        });

        hideAllViewsProperty.addListener(i -> {
            if (isHideAllViews()) {
                Toast.makeText(activity, R.string.tip_hide_menu_view, Toast.LENGTH_SHORT).show();
            }
        });

        editLayout.visibilityProperty().bind(editModeProperty);

        manageViewGroups.setOnClickListener(this);
        addButton.setOnClickListener(this);
        addDirection.setOnClickListener(this);
        manageButtonStyle.setOnClickListener(this);
        manageDirectionStyle.setOnClickListener(this);
    }

    private void refreshViewGroupList(FCLSpinner<ControlViewGroup> spinner) {
        if (getViewGroup() != null) {
            setViewGroup(null);
        }
        ArrayList<String> viewGroupNameList = controllerProperty.get().viewGroups().stream().map(ControlViewGroup::getName).collect(Collectors.toCollection(ArrayList::new));
        spinner.setDataList(new ArrayList<>(controllerProperty.get().viewGroups()));
        ArrayAdapter<String> viewGroupNameAdapter = new ArrayAdapter<>(activity, R.layout.item_spinner_small, viewGroupNameList);
        viewGroupNameAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown_small);
        spinner.setAdapter(viewGroupNameAdapter);
        FXUtils.bindSelection(spinner, viewGroupProperty);
    }

    @SuppressLint("SetTextI18n")
    private void initRightMenu() {
        FCLSwitch lockMenuSwitch = findViewById(R.id.switch_lock_view);
        FCLSwitch hideMenuSwitch = findViewById(R.id.switch_hide_view);
        FCLSwitch showFps = findViewById(R.id.switch_show_fps);
        FCLSwitch disableSoftKeyAdjustSwitch = findViewById(R.id.switch_soft_keyboard_adjust);
        FCLSwitch disableGestureSwitch = findViewById(R.id.switch_gesture);
        FCLSwitch disableBEGestureSwitch = findViewById(R.id.switch_be_gesture);
        FCLSwitch disableLeftTouchSwitch = findViewById(R.id.switch_left_touch);
        FCLSwitch gyroSwitch = findViewById(R.id.switch_gyro);
        FCLSwitch physicalMouseSwitch = findViewById(R.id.switch_physical_mouse_mode);
        FCLSwitch showLogSwitch = findViewById(R.id.switch_show_log);
        FCLSwitch performanceModeSwitch = findViewById(R.id.switch_performance);
        FCLSwitch autoShowLogSwitch = findViewById(R.id.switch_auto_show_log);

        FCLSpinner<GestureMode> gestureModeSpinner = findViewById(R.id.gesture_mode_spinner);
        FCLSpinner<MouseMoveMode> mouseMoveModeSpinner = findViewById(R.id.mouse_mode_spinner);

        FCLNumberSeekBar itemBarScaleSeekbar = findViewById(R.id.item_bar_scale);
        FCLNumberSeekBar windowScaleSeekbar = findViewById(R.id.window_scale);
        FCLNumberSeekBar cursorOffsetSeekbar = findViewById(R.id.cursor_offset);
        FCLNumberSeekBar mouseSensitivitySeekbar = findViewById(R.id.mouse_sensitivity);
        FCLNumberSeekBar mouseSensitivityCursorSeekbar = findViewById(R.id.mouse_sensitivity_cursor);
        FCLNumberSeekBar mouseSizeSeekbar = findViewById(R.id.mouse_size);
        FCLNumberSeekBar gamepadDeadzoneSeekbar = findViewById(R.id.gamepad_deadzone_size);
        FCLNumberSeekBar gyroSensitivitySeekbar = findViewById(R.id.gyro_sensitivity);

        manageQuickInput = findViewById(R.id.open_quick_input);
        sendKeycode = findViewById(R.id.open_send_key);
        gamepadResetMapper = findViewById(R.id.gamepad_reset_mapper);
        gamepadButtonBinding = findViewById(R.id.gamepad_reset_button_binding);
        forceExit = findViewById(R.id.force_exit);

        FXUtils.bindBoolean(lockMenuSwitch, menuSetting.getLockMenuViewProperty());
        FXUtils.bindBoolean(hideMenuSwitch, menuSetting.getHideMenuViewViewProperty());
        FXUtils.bindBoolean(disableSoftKeyAdjustSwitch, menuSetting.getDisableSoftKeyAdjustProperty());
        FXUtils.bindBoolean(disableGestureSwitch, menuSetting.getDisableGestureProperty());
        FXUtils.bindBoolean(disableBEGestureSwitch, menuSetting.getDisableBEGestureProperty());
        FXUtils.bindBoolean(disableLeftTouchSwitch, menuSetting.getDisableLeftTouchProperty());
        FXUtils.bindBoolean(gyroSwitch, menuSetting.getEnableGyroscopeProperty());
        FXUtils.bindBoolean(physicalMouseSwitch, menuSetting.getPhysicalMouseMode());
        FXUtils.bindBoolean(showLogSwitch, menuSetting.getShowLogProperty());
        FXUtils.bindBoolean(autoShowLogSwitch, menuSetting.getAutoShowLogProperty());

        performanceModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            menuSetting.getPerformanceModeProperty().setValue(isChecked);
            activity.getWindow().setSustainedPerformanceMode(isChecked);
        });
        performanceModeSwitch.setChecked(menuSetting.isPerformanceMode());

        menuSetting.getHideMenuViewViewProperty().addListener(i -> {
            menuView.setVisibility(menuSetting.isHideMenuView() ? View.INVISIBLE : View.VISIBLE);
            if (menuSetting.isHideMenuView()) {
                Toast.makeText(activity, R.string.tip_hide_menu_view, Toast.LENGTH_SHORT).show();
            }
        });

        showFps.setOnCheckedChangeListener((buttonView, isChecked) -> {
            menuSetting.getShowFpsProperty().setValue(isChecked);
            if (isSimulated()) {
                return;
            }
            if (isChecked) {
                Schedulers.io().execute(() -> {
                    FCLBridge.getFps();
                    while (showFps.isChecked()) {
                        if (System.currentTimeMillis() - time >= 1000) {
                            Schedulers.androidUIThread().execute(() -> fpsText.setText("FPS:" + FCLBridge.getFps()));
                            time = System.currentTimeMillis();
                        }
                    }
                });
            } else {
                fpsText.setText("");
            }
        });
        showFps.setChecked(menuSetting.isShowFps());
        showFps.setOnLongClickListener((view -> {
            fpsText.resetPosition();
            return true;
        }));

        logWindow.visibilityProperty().setValue(menuSetting.isShowLog() || (!isSimulated() && menuSetting.isAutoShowLog()));
        menuSetting.getShowLogProperty().addListener(observable -> {
            logWindow.visibilityProperty().setValue(menuSetting.isShowLog());
        });
        menuSetting.getAutoShowLogProperty().addListener(observable -> {
            if (baseLayout.getBackground() != null) {
                logWindow.visibilityProperty().setValue(menuSetting.isAutoShowLog());
            }
        });

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
        FXUtils.bindSelection(gestureModeSpinner, menuSetting.getGestureModeProperty());
        FXUtils.bindSelection(mouseMoveModeSpinner, menuSetting.getMouseMoveModeProperty());

        initSeekbar(itemBarScaleSeekbar, menuSetting.getItemBarScale(), observable -> {
            menuSetting.setItemBarScale(itemBarScaleSeekbar.progressProperty().get());
            GameOption.GameOptionListener optionListener = gameItemBar.getOptionListener();
            if (optionListener != null) {
                optionListener.onOptionChanged();
            }
        });

        initSeekbar(windowScaleSeekbar, (int) (menuSetting.getWindowScale() * 100), observable -> {
            double doubleValue = windowScaleSeekbar.progressProperty().get() / 100d;
            menuSetting.setWindowScale(doubleValue);
            refreshWindowsSize(doubleValue);
        });

        initSeekbar(cursorOffsetSeekbar, (int) (menuSetting.getCursorOffset()), observable -> {
            menuSetting.setCursorOffset(cursorOffsetSeekbar.progressProperty().get());
            int screenWidth = AndroidUtils.getScreenWidth(FCLApplication.getCurrentActivity());
            int screenHeight = AndroidUtils.getScreenHeight(FCLApplication.getCurrentActivity());
            if (fclBridge != null) {
                double scaleFactor = fclBridge.getScaleFactor();
                int width = (int) ((screenWidth + cursorOffsetSeekbar.progressProperty().get()) * scaleFactor);
                int height = (int) (screenHeight * scaleFactor);
                fclBridge.getSurfaceTexture().setDefaultBufferSize(width, height);
                fclBridge.pushEventWindow(width, height);
            }
        });

        initSeekbar(mouseSensitivitySeekbar, (int) (menuSetting.getMouseSensitivity() * 100), observable -> menuSetting.setMouseSensitivity(mouseSensitivitySeekbar.progressProperty().get() / 100d));
        initSeekbar(mouseSensitivityCursorSeekbar, (int) (menuSetting.getMouseSensitivityCursor() * 100), observable -> menuSetting.setMouseSensitivityCursor(mouseSensitivityCursorSeekbar.progressProperty().get() / 100d));
        initSeekbar(mouseSizeSeekbar, menuSetting.getMouseSizeProperty().get(), observable -> menuSetting.setMouseSize(mouseSizeSeekbar.progressProperty().get()));
        initSeekbar(gamepadDeadzoneSeekbar, (int) (menuSetting.getGamepadDeadzone() * 100), observable -> menuSetting.setGamepadDeadzone(gamepadDeadzoneSeekbar.progressProperty().get() / 100d));
        initSeekbar(gyroSensitivitySeekbar, menuSetting.getGyroscopeSensitivityProperty().get(), observable -> menuSetting.setGyroscopeSensitivity(gyroSensitivitySeekbar.progressProperty().get()));

        manageQuickInput.setOnClickListener(this);
        sendKeycode.setOnClickListener(this);
        gamepadResetMapper.setOnClickListener(this);
        gamepadButtonBinding.setOnClickListener(this);
        forceExit.setOnClickListener(this);
    }

    private void initSeekbar(FCLNumberSeekBar bar, int initValue, InvalidationListener listener) {
        bar.addProgressListener();
        bar.progressProperty().set(initValue);
        bar.progressProperty().addListener(listener);
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
        if (!ButtonStyles.isInitialized()) {
            ButtonStyles.init();
        }
        if (!DirectionStyles.isInitialized()) {
            DirectionStyles.init();
        }
        if (!QuickInputTexts.isInitialized()) {
            QuickInputTexts.init();
        }

        if (Files.exists(new File(FCLPath.FILES_DIR + "/menu_setting.json").toPath())) {
            try {
                this.menuSetting = new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .fromJson(FileUtils.readText(new File(FCLPath.FILES_DIR + "/menu_setting.json")), MenuSetting.class);
                //如果文件损坏，menuSetting可能为空
                if (this.menuSetting == null) {
                    this.menuSetting = new MenuSetting();
                    new File(FCLPath.FILES_DIR + "/menu_setting.json").delete();
                }
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

        editModeProperty.set(isSimulated());
        controllerProperty.set(Controllers.findControllerById(activity.getIntent().getExtras().getString("controller")));

        baseLayout = findViewById(R.id.base_layout);
        touchPad = findViewById(R.id.touch_pad);
        gameItemBar = findViewById(R.id.game_item_bar);
        logWindow = findViewById(R.id.log_window);
        fpsText = findViewById(R.id.fps);
        touchCharInput = findViewById(R.id.input_scanner);
        touchControllerInputView = findViewById(R.id.touchcontroller_input_view);
        launchProgress = findViewById(R.id.launch_progress);
        cursorView = findViewById(R.id.cursor);

        if (!isSimulated()) {
            ImageUtil.loadInto(baseLayout, ThemeEngine.getInstance().getTheme().getBackground(activity));
            launchProgress.setVisibility(View.VISIBLE);
            touchPad.post(() -> gameItemBar.setup(this));
        }
        touchPad.init(this);
        touchCharInput.setCharacterSender(this, new LwjglCharSender(this));
        ViewGroup.LayoutParams layoutParams = cursorView.getLayoutParams();
        layoutParams.width = ConvertUtils.dip2px(activity, menuSetting.getMouseSizeProperty().get());
        layoutParams.height = ConvertUtils.dip2px(activity, menuSetting.getMouseSizeProperty().get());
        cursorView.setLayoutParams(layoutParams);
        menuSetting.getMouseSizeProperty().addListener(observable -> {
            ViewGroup.LayoutParams params = cursorView.getLayoutParams();
            params.width = ConvertUtils.dip2px(activity, menuSetting.getMouseSizeProperty().get());
            params.height = ConvertUtils.dip2px(activity, menuSetting.getMouseSizeProperty().get());
            cursorView.setLayoutParams(params);
        });

        gyroscope = new Gyroscope(this);
        gyroscope.enableProperty().bind(menuSetting.getEnableGyroscopeProperty());

        viewManager = new ViewManager(this);

        initLeftMenu();
        initRightMenu();

        viewManager.setup();

        if (new File(FCLPath.FILES_DIR, "cursor.png").exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(new File(FCLPath.FILES_DIR, "cursor.png").getAbsolutePath());
            BitmapDrawable drawable = new BitmapDrawable(getActivity().getResources(), bitmap);
            getCursor().setImageDrawable(drawable);
        }

        if (getBridge() != null && getBridge().hasTouchController()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("launcher", MODE_PRIVATE);
            touchController = new TouchController(getActivity(), AndroidUtils.getScreenWidth(getActivity()), AndroidUtils.getScreenHeight(getActivity()), (int) sharedPreferences.getInt("vibrationDuration", 100));

            touchControllerInputView.setClient(touchController.getClient());
            touchControllerInputView.setFclInput(fclInput);
            touchControllerInputView.setSize(AndroidUtils.getScreenWidth(getActivity()), AndroidUtils.getScreenHeight(getActivity()));
            touchControllerInputView.setDisableFullScreenInput(sharedPreferences.getBoolean("disableFullscreenInput", true));
        }

        touchPad.setOnGenericMotionListener((view, motionEvent) -> {
            if (motionEvent.isFromSource(InputDevice.SOURCE_MOUSE) && menuSetting.isPhysicalMouseMode()) {
                if (getCursorMode() == FCLBridge.CursorEnabled && motionEvent.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
                    getInput().setPointer((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
                    return true;
                }
                return fclInput.handleExternalMouseEvent(motionEvent);
            }
            return false;
        });

    }

    @Override
    public View getLayout() {
        if (layout == null) {
            layout = LayoutInflater.from(activity).inflate(R.layout.view_game_menu, null);
            ((DrawerLayout) layout).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
        if (cursorModeProperty.get() == FCLBridge.CursorDisabled) {
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
    public void onGraphicOutput() {
        baseLayout.setBackground(null);
        baseLayout.removeView(launchProgress);
        if (!menuSetting.isShowLog() && menuSetting.isAutoShowLog()) {
            logWindow.visibilityProperty().setValue(false);
        }
        refreshWindowsSize(menuSetting.getWindowScale());
    }

    @Override
    public void onCursorModeChange(int mode) {
        this.cursorModeProperty.set(mode);
        activity.runOnUiThread(() -> {
            if (mode == FCLBridge.CursorEnabled) {
                getCursor().setVisibility(View.VISIBLE);
                gameItemBar.setVisibility(View.GONE);
                getInput().setPointer(AndroidUtils.getScreenWidth(FCLApplication.getCurrentActivity()) / 2, AndroidUtils.getScreenHeight(FCLApplication.getCurrentActivity()) / 2, "Gyro");
                if (menuSetting.isPhysicalMouseMode()) {
                    getInput().getFocusableView().releasePointerCapture();
                    getInput().getFocusableView().clearFocus();
                }
            } else {
                getCursor().setVisibility(View.GONE);
                if (getBridge() != null && !getBridge().hasTouchController()) {
                    gameItemBar.setVisibility(View.VISIBLE);
                }
                if (menuSetting.isPhysicalMouseMode()) {
                    getInput().getFocusableView().requestFocus();
                    getInput().getFocusableView().requestPointerCapture();
                }
            }
        });
    }

    private boolean firstLog = true;

    @Override
    public void onLog(String log) {
        if (fclBridge != null) {
            if (log.contains("version string:") || log.contains("OR:") || log.contains("ERROR:") || log.contains("INTERNAL ERROR:")) {
                return;
            }
            logWindow.appendLog(log);
            if (BuildConfig.DEBUG) {
                Log.d("FCL Debug", log);
            }
            try {
                if (firstLog) {
                    FileUtils.writeText(new File(fclBridge.getLogPath()), log);
                    firstLog = false;
                } else {
                    FileUtils.writeTextWithAppendMode(new File(fclBridge.getLogPath()), log);
                }
            } catch (IOException e) {
                Logging.LOG.log(Level.WARNING, "Can't log game log to target file", e.getMessage());
            }
        }
    }

    @Override
    public void onExit(int exitCode) {
        if (exitCode != 0 && fclBridge != null) {
            JVMCrashActivity.startCrashActivity(true, activity, exitCode, fclBridge.getLogPath(), fclBridge.getRenderer(), fclBridge.getJava());
            Logging.LOG.log(Level.INFO, "JVM crashed, start jvm crash activity to show errors now!");
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @NonNull
    public final <T extends View> T findViewById(int id) {
        return getLayout().findViewById(id);
    }

    public void openQuickInput() {
        QuickInputDialog dialog = new QuickInputDialog(activity, this);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == manageViewGroups) {
            ViewGroupDialog dialog = new ViewGroupDialog(getActivity(), this, false, FXCollections.observableList(new ArrayList<>()), null);
            dialog.show();
        }
        if (v == addButton) {
            if (getViewGroup() == null) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.edit_view_no_group), Toast.LENGTH_SHORT).show();
            } else {
                EditViewDialog dialog = new EditViewDialog(getActivity(), new ControlButtonData(UUID.randomUUID().toString()), this, new EditViewDialog.Callback() {
                    @Override
                    public void onPositive(CustomControl view) {
                        viewManager.addView(view);
                    }

                    @Override
                    public void onClone(CustomControl view) {
                        // Ignore
                    }
                }, false);
                dialog.show();
            }
        }
        if (v == addDirection) {
            if (getViewGroup() == null) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.edit_view_no_group), Toast.LENGTH_SHORT).show();
            } else {
                EditViewDialog dialog = new EditViewDialog(getActivity(), new ControlDirectionData(UUID.randomUUID().toString()), this, new EditViewDialog.Callback() {
                    @Override
                    public void onPositive(CustomControl view) {
                        viewManager.addView(view);
                    }

                    @Override
                    public void onClone(CustomControl view) {
                        // Ignore
                    }
                }, false);
                dialog.show();
            }
        }
        if (v == manageButtonStyle) {
            ButtonStyleDialog dialog = new ButtonStyleDialog(getActivity(), false, null, null);
            dialog.show();
        }
        if (v == manageDirectionStyle) {
            DirectionStyleDialog dialog = new DirectionStyleDialog(getActivity(), false, null, null);
            dialog.show();
        }
        if (v == manageQuickInput) {
            openQuickInput();
        }
        if (v == sendKeycode) {
            ObservableList<Integer> list = FXCollections.observableList(new ArrayList<>());
            new SelectKeycodeDialog(getActivity(), list, false, true, (dialog) -> {
                Schedulers.io().execute(() -> {
                    list.forEach(key -> getInput().sendKeyEvent(key, true));
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignore) {
                    }
                    list.forEach(key -> getInput().sendKeyEvent(key, false));
                });
                return Unit.INSTANCE;
            }).show();
        }
        if (v == gamepadResetMapper) {
            Remapper.wipePreferences(getActivity());
            getInput().resetMapper();
        }
        if (v == gamepadButtonBinding) {
            fclInput.checkGamepad();
            if (fclInput.getGamepad() != null) {
                new GamepadMapDialog(getActivity(), fclInput).show();
            }
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

    private void refreshWindowsSize(double factor) {
        int screenWidth = AndroidUtils.getScreenWidth(FCLApplication.getCurrentActivity());
        int screenHeight = AndroidUtils.getScreenHeight(FCLApplication.getCurrentActivity());
        if (fclBridge != null) {
            fclBridge.setScaleFactor(factor);
            int width = (int) ((screenWidth + menuSetting.getCursorOffset()) * factor);
            int height = (int) (screenHeight * factor);
            fclBridge.getSurfaceTexture().setDefaultBufferSize(width, height);
            fclBridge.pushEventWindow(width, height);
        }
    }

    @Nullable
    public TouchController getTouchController() {
        return touchController;
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
