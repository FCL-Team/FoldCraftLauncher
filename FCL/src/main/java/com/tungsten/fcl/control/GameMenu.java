package com.tungsten.fcl.control;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.GsonBuilder;
import com.mio.touchcontroller.TouchController;
import com.mio.touchcontroller.TouchControllerInputView;
import com.tungsten.fcl.ui.compose.dialog.MiuixButtonStyleDialog;
import com.tungsten.fcl.ui.compose.dialog.MiuixDirectionStyleDialog;
import com.tungsten.fcl.ui.compose.dialog.MiuixEditViewDialog;
import com.tungsten.fcl.ui.compose.dialog.MiuixGamepadMapDialog;
import com.tungsten.fcl.ui.compose.dialog.MiuixQuickInputDialog;
import com.tungsten.fcl.ui.compose.dialog.MiuixSelectKeycodeDialog;
import com.tungsten.fcl.ui.compose.dialog.MiuixViewGroupDialog;
import com.mio.ui.view.CursorView;
import com.mio.ui.view.DraggableTextView;
import com.mio.util.AndroidUtilKt;
import com.mio.util.ImageUtil;
import com.tungsten.fcl.BuildConfig;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.JVMCrashActivity;
import com.tungsten.fcl.control.data.ButtonStyles;
import com.tungsten.fcl.control.data.ControlButtonData;
import com.tungsten.fcl.control.data.ControlButtonStyle;
import com.tungsten.fcl.control.data.ControlDirectionData;
import com.tungsten.fcl.control.data.ControlDirectionStyle;
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
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.flow.FlowBindings;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLNumberSeekBar;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
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

    // 阶段 4a：MenuSetting 属性已 StateFlow 化。下面两个 helper 对齐原
    // FXUtils.bindBoolean/bindSelection 双向绑定（初值同步 + 双向跟随后续变化，
    // 两侧同值写入均不触发，天然防回环）；订阅在 onDestroy 统一取消
    //（对齐原 bindBidirectional 弱引用自动摘除，防止 Activity 泄漏）。
    private final List<FlowSubscriptions.Subscription> menuSettingSubscriptions = new ArrayList<>();

    private void bindMenuSettingSwitch(FCLSwitch fclSwitch, MutableStateFlow<Boolean> flow) {
        fclSwitch.addCheckedChangeListener();
        menuSettingSubscriptions.add(FlowBindings.bindBidirectional(fclSwitch.checkFlow(), flow));
    }

    private <T> void bindMenuSettingSelection(FCLSpinner<T> spinner, MutableStateFlow<T> flow) {
        spinner.addSelectListener();
        menuSettingSubscriptions.add(FlowBindings.bindBidirectional(spinner.selectedItemFlow(), flow));
    }

    /** 由宿主 Activity onDestroy 调用：取消 MenuSetting Flow 订阅（防泄漏）。 */
    public void onDestroy() {
        menuSettingSubscriptions.forEach(FlowSubscriptions.Subscription::cancel);
        menuSettingSubscriptions.clear();
        if (controllerRevisionSubscription != null) {
            controllerRevisionSubscription.cancel();
            controllerRevisionSubscription = null;
        }
        if (viewManager != null) {
            viewManager.onDestroy();
        }
    }

    private FlowSubscriptions.Subscription controllerRevisionSubscription;

    private void subscribeControllerRevision(FCLSpinner<ControlViewGroup> currentViewGroupSpinner) {
        if (controllerRevisionSubscription != null)
            controllerRevisionSubscription.cancel();
        controllerRevisionSubscription = getController() == null ? null :
                FlowSubscriptions.subscribe(getController().revisionFlow(), v -> refreshViewGroupList(currentViewGroupSpinner));
    }
    private int cursorX;
    private int cursorY;
    private int pointerX;
    private int pointerY;

    private View layout;
    private RelativeLayout baseLayout;
    private TouchPad touchPad;
    private GameItemBar gameItemBar;
    private LogWindow logWindow;
    public DraggableTextView fpsText;
    public DraggableTextView memoryText;
    private TouchCharInput touchCharInput;
    private TouchControllerInputView touchControllerInputView;
    private FCLProgressBar launchProgress;
    private CursorView cursorView;
    private ViewManager viewManager;
    private Gyroscope gyroscope;
    private GameOption gameOption;

    private FCLButton manageViewGroups;
    private FCLButton addButton;
    private FCLButton addDirection;
    private FCLButton manageButtonStyle;
    private FCLButton manageDirectionStyle;

    private FCLButton openMultiplayerButton;
    private FCLButton manageQuickInput;
    private FCLButton sendKeycode;
    private FCLButton gamepadResetMapper;
    private FCLButton gamepadButtonBinding;
    private FCLButton forceExit;

    private MultiplayerDialog multiplayerDialog;

    private MenuView menuView;

    private TouchController touchController;

    private boolean gamepadDisabled = false;
    private Thread fpsThread;
    private Thread memoryThread;
    private int lastCursorMode = FCLBridge.CursorEnabled;

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
        return cursorModeFlow.getValue();
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

    private final MutableStateFlow<Boolean> editModeFlow = StateFlowKt.MutableStateFlow(false);

    public MutableStateFlow<Boolean> editModeFlow() {
        return editModeFlow;
    }

    public void setEditMode(boolean editMode) {
        editModeFlow.setValue(editMode);
    }

    public boolean isEditMode() {
        return editModeFlow.getValue();
    }

    private final MutableStateFlow<Integer> cursorModeFlow = StateFlowKt.MutableStateFlow(FCLBridge.CursorEnabled);

    public MutableStateFlow<Integer> cursorModeFlow() {
        return cursorModeFlow;
    }

    private final MutableStateFlow<Boolean> showViewBoundariesFlow = StateFlowKt.MutableStateFlow(false);

    public MutableStateFlow<Boolean> showViewBoundariesFlow() {
        return showViewBoundariesFlow;
    }

    public void setShowViewBoundaries(boolean showViewBoundaries) {
        showViewBoundariesFlow.setValue(showViewBoundaries);
    }

    public boolean isShowViewBoundaries() {
        return showViewBoundariesFlow.getValue();
    }

    private final MutableStateFlow<Boolean> hideAllViewsFlow = StateFlowKt.MutableStateFlow(false);

    public MutableStateFlow<Boolean> hideAllViewsFlow() {
        return hideAllViewsFlow;
    }

    public void setHideAllViews(boolean viewVisible) {
        hideAllViewsFlow.setValue(viewVisible);
    }

    public boolean isHideAllViews() {
        return hideAllViewsFlow.getValue();
    }

    private final MutableStateFlow<Controller> controllerFlow = StateFlowKt.MutableStateFlow(null);

    public MutableStateFlow<Controller> controllerFlow() {
        return controllerFlow;
    }

    public void setController(Controller controller) {
        controllerFlow.setValue(controller);
    }

    public Controller getController() {
        return controllerFlow.getValue();
    }

    private final MutableStateFlow<ControlViewGroup> viewGroupFlow = StateFlowKt.MutableStateFlow(null);

    public MutableStateFlow<ControlViewGroup> viewGroupFlow() {
        return viewGroupFlow;
    }

    public void setViewGroup(ControlViewGroup viewGroup) {
        viewGroupFlow.setValue(viewGroup);
    }

    @Nullable
    public ControlViewGroup getViewGroup() {
        return viewGroupFlow.getValue();
    }

    public boolean isGamepadDisabled() {
        return gamepadDisabled;
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

        editMode.addCheckedChangeListener();
        FlowBindings.bindBidirectional(editMode.checkFlow(), editModeFlow);
        showViewBoundaries.addCheckedChangeListener();
        FlowBindings.bindBidirectional(showViewBoundaries.checkFlow(), showViewBoundariesFlow);
        hideAllViews.addCheckedChangeListener();
        FlowBindings.bindBidirectional(hideAllViews.checkFlow(), hideAllViewsFlow);
        bindMenuSettingSwitch(autoFit, menuSetting.getAutoFitFlow());

        autoFitDist.addProgressListener();
        autoFitDist.progressFlow().setValue(menuSetting.getAutoFitDist());
        menuSettingSubscriptions.add(FlowSubscriptions.subscribe(menuSetting.getAutoFitDistFlow(), v -> autoFitDist.progressFlow().setValue(v)));
        menuSettingSubscriptions.add(FlowSubscriptions.subscribe(autoFitDist.progressFlow(), v -> menuSetting.setAutoFitDist(v)));

        ArrayList<String> controllerNameList = Controllers.getControllers().stream().map(Controller::getName).collect(Collectors.toCollection(ArrayList::new));
        currentControllerSpinner.setDataList(new ArrayList<>(Controllers.getControllers()));
        ArrayAdapter<String> controllerNameAdapter = new ArrayAdapter<>(activity, R.layout.item_spinner_small, controllerNameList);
        controllerNameAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown_small);
        currentControllerSpinner.setAdapter(controllerNameAdapter);
        currentControllerSpinner.addSelectListener();
        FlowBindings.bindBidirectional(currentControllerSpinner.selectedItemFlow(), controllerFlow);

        refreshViewGroupList(currentViewGroupSpinner);
        // 阶段 4a：Controller 失效已改为 revisionFlow；跟随当前控制器（切换时换绑并取消旧订阅，
        // 对齐原"控制器内部变更即刷新分组列表"，且不积累监听）。
        subscribeControllerRevision(currentViewGroupSpinner);
        FlowSubscriptions.subscribe(controllerFlow, invalidate -> {
            refreshViewGroupList(currentViewGroupSpinner);
            subscribeControllerRevision(currentViewGroupSpinner);
        });

        FlowSubscriptions.subscribe(hideAllViewsFlow, i -> {
            if (isHideAllViews()) {
                Toast.makeText(activity, R.string.tip_hide_menu_view, Toast.LENGTH_LONG).show();
            }
        });

        editLayout.visibilityFlow().setValue(editModeFlow.getValue());
        FlowSubscriptions.subscribe(editModeFlow, v -> editLayout.visibilityFlow().setValue(v));

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
        ArrayList<String> viewGroupNameList = controllerFlow.getValue().viewGroups().stream().map(ControlViewGroup::getName).collect(Collectors.toCollection(ArrayList::new));
        spinner.setDataList(new ArrayList<>(controllerFlow.getValue().viewGroups()));
        ArrayAdapter<String> viewGroupNameAdapter = new ArrayAdapter<>(activity, R.layout.item_spinner_small, viewGroupNameList);
        viewGroupNameAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown_small);
        spinner.setAdapter(viewGroupNameAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setViewGroup(spinner.getDataList().get(position));
                if (getViewGroup() != null) {
                    getViewGroup().getViewData().getButtonList().forEach(it -> {
                        String name = it.getStyle().getName();
                        ControlButtonStyle style = ButtonStyles.findStyleByName(name);
                        if (name.equals(style.getName())) {
                            it.setStyle(style);
                        }
                    });
                    getViewGroup().getViewData().getDirectionList().forEach(it -> {
                        String name = it.getStyle().getName();
                        ControlDirectionStyle style = DirectionStyles.findStyleByName(name);
                        if (name.equals(style.getName())) {
                            it.setStyle(style);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initRightMenu() {
        FCLSwitch lockMenuSwitch = findViewById(R.id.switch_lock_view);
        FCLSwitch hideMenuSwitch = findViewById(R.id.switch_hide_view);
        FCLSwitch showFps = findViewById(R.id.switch_show_fps);
        FCLSwitch showMemory = findViewById(R.id.switch_show_memory);
        FCLSwitch disableSoftKeyAdjustSwitch = findViewById(R.id.switch_soft_keyboard_adjust);
        FCLSwitch disableGestureSwitch = findViewById(R.id.switch_gesture);
        FCLSwitch disableLeftTouchSwitch = findViewById(R.id.switch_left_touch);
        FCLSwitch gyroSwitch = findViewById(R.id.switch_gyro);
        FCLSwitch gyroInvertSwitch = findViewById(R.id.switch_gyro_invert);
        FCLSwitch physicalMouseSwitch = findViewById(R.id.switch_physical_mouse_mode);
        FCLSwitch showLogSwitch = findViewById(R.id.switch_show_log);
        FCLSwitch performanceModeSwitch = findViewById(R.id.switch_performance);
        FCLSwitch autoShowLogSwitch = findViewById(R.id.switch_auto_show_log);
        FCLSwitch disableGamepadMapping = findViewById(R.id.switch_disable_gamepad_mapping);

        FCLSpinner<GestureMode> gestureModeSpinner = findViewById(R.id.gesture_mode_spinner);
        FCLSpinner<MouseMoveMode> mouseMoveModeSpinner = findViewById(R.id.mouse_mode_spinner);

        FCLNumberSeekBar itemBarWidthSeekbar = findViewById(R.id.item_bar_width);
        FCLNumberSeekBar itemBarHeightSeekbar = findViewById(R.id.item_bar_height);
        FCLNumberSeekBar windowScaleSeekbar = findViewById(R.id.window_scale);
        FCLNumberSeekBar cursorOffsetSeekbar = findViewById(R.id.cursor_offset);
        FCLNumberSeekBar mouseSensitivitySeekbar = findViewById(R.id.mouse_sensitivity);
        FCLNumberSeekBar mouseSensitivityCursorSeekbar = findViewById(R.id.mouse_sensitivity_cursor);
        FCLNumberSeekBar mouseSizeSeekbar = findViewById(R.id.mouse_size);
        FCLNumberSeekBar mouseOffsetXSeekbar = findViewById(R.id.mouse_offset_x);
        FCLNumberSeekBar mouseOffsetYSeekbar = findViewById(R.id.mouse_offset_y);
        FCLNumberSeekBar gamepadDeadzoneSeekbar = findViewById(R.id.gamepad_deadzone_size);
        FCLNumberSeekBar gyroSensitivitySeekbar = findViewById(R.id.gyro_sensitivity);

        FCLTextView openMultiplayer = findViewById(R.id.open_multiplayer_menu_text);
        openMultiplayerButton = findViewById(R.id.open_multiplayer_menu);
        manageQuickInput = findViewById(R.id.open_quick_input);
        sendKeycode = findViewById(R.id.open_send_key);
        gamepadResetMapper = findViewById(R.id.gamepad_reset_mapper);
        gamepadButtonBinding = findViewById(R.id.gamepad_reset_button_binding);
        forceExit = findViewById(R.id.force_exit);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("third_party", Context.MODE_PRIVATE);
        boolean multiplayerEnabled = sharedPreferences.getBoolean("terracotta", false);
        openMultiplayer.setVisibility((isSimulated() || !multiplayerEnabled) ? View.GONE : View.VISIBLE);
        openMultiplayerButton.setVisibility((isSimulated() || !multiplayerEnabled) ? View.GONE : View.VISIBLE);

        disableGamepadMapping.setOnCheckedChangeListener((buttonView, isChecked) -> {
            gamepadDisabled = isChecked;
        });

        bindMenuSettingSwitch(lockMenuSwitch, menuSetting.getLockMenuViewFlow());
        bindMenuSettingSwitch(hideMenuSwitch, menuSetting.getHideMenuViewFlow());
        bindMenuSettingSwitch(disableSoftKeyAdjustSwitch, menuSetting.getDisableSoftKeyAdjustFlow());
        bindMenuSettingSwitch(disableGestureSwitch, menuSetting.getDisableGestureFlow());
        bindMenuSettingSwitch(disableLeftTouchSwitch, menuSetting.getDisableLeftTouchFlow());
        bindMenuSettingSwitch(gyroSwitch, menuSetting.getEnableGyroscopeFlow());
        bindMenuSettingSwitch(gyroInvertSwitch, menuSetting.getInvertGyroscopeFlow());
        bindMenuSettingSwitch(physicalMouseSwitch, menuSetting.getPhysicalMouseModeFlow());
        bindMenuSettingSwitch(showLogSwitch, menuSetting.getShowLogFlow());
        bindMenuSettingSwitch(autoShowLogSwitch, menuSetting.getAutoShowLogFlow());

        performanceModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            menuSetting.setPerformanceMode(isChecked);
            activity.getWindow().setSustainedPerformanceMode(isChecked);
        });
        performanceModeSwitch.setChecked(menuSetting.isPerformanceMode());

        menuSettingSubscriptions.add(FlowSubscriptions.subscribe(menuSetting.getHideMenuViewFlow(), v -> {
            menuView.setVisibility(menuSetting.isHideMenuView() ? View.INVISIBLE : View.VISIBLE);
            if (menuSetting.isHideMenuView()) {
                Toast.makeText(activity, R.string.tip_hide_menu_view, Toast.LENGTH_LONG).show();
            }
        }));

        showFps.setOnCheckedChangeListener((buttonView, isChecked) -> {
            menuSetting.setShowFps(isChecked);
            if (isSimulated()) {
                return;
            }
            if (isChecked) {
                fpsThread = new Thread(() -> {
                    FCLBridge.getFps();
                    while (showFps.isChecked() && !Thread.currentThread().isInterrupted()) {
                        Schedulers.androidUIThread().execute(() -> fpsText.setText("FPS:" + FCLBridge.getFps()));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                });
                fpsThread.setName("FCL FPS Thread");
                fpsThread.start();
            } else {
                if (fpsThread != null) {
                    fpsThread.interrupt();
                    fpsThread = null;
                }
                fpsText.setText("");
            }
        });
        showFps.setChecked(menuSetting.isShowFps());
        showFps.setOnLongClickListener((view -> {
            fpsText.resetPosition();
            return true;
        }));

        showMemory.setOnCheckedChangeListener((buttonView, isChecked) -> {
            menuSetting.setShowMemory(isChecked);
            if (isSimulated()) {
                return;
            }
            if (isChecked) {
                memoryThread = new Thread(() -> {
                    while (showMemory.isChecked() && !Thread.currentThread().isInterrupted()) {
                        long usedMemory = AndroidUtilKt.getUsedMemory(getActivity()) / 1024 / 1024;
                        long totalMemory = AndroidUtilKt.getTotalMemory(getActivity()) / 1024 / 1024;
                        long usage;
                        if (totalMemory > 0) {
                            usage = usedMemory * 100 / totalMemory;
                        } else {
                            usage = -1;
                        }
                        Schedulers.androidUIThread().execute(() -> memoryText.setText("Mem(" + usage + "%): " + usedMemory + " / " + totalMemory + " MB"));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                });
                memoryThread.setName("FCL Memory Thread");
                memoryThread.start();
            } else {
                if (memoryThread != null) {
                    memoryThread.interrupt();
                    memoryThread = null;
                }
                memoryText.setText("");
            }
        });
        showMemory.setChecked(menuSetting.isShowMemory());
        showMemory.setOnLongClickListener((view -> {
            memoryText.resetPosition();
            return true;
        }));

        logWindow.setVisibility(menuSetting.isShowLog() || (!isSimulated() && menuSetting.isAutoShowLog()));
        menuSettingSubscriptions.add(FlowSubscriptions.subscribe(menuSetting.getShowLogFlow(), v -> {
            logWindow.setVisibility(menuSetting.isShowLog());
        }));
        menuSettingSubscriptions.add(FlowSubscriptions.subscribe(menuSetting.getAutoShowLogFlow(), v -> {
            if (baseLayout.getBackground() != null) {
                logWindow.setVisibility(menuSetting.isAutoShowLog());
            }
        }));

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
        bindMenuSettingSelection(gestureModeSpinner, menuSetting.getGestureModeFlow());
        bindMenuSettingSelection(mouseMoveModeSpinner, menuSetting.getMouseMoveModeFlow());

        int screenWidth = AndroidUtils.getScreenWidth();
        initSeekbar(itemBarWidthSeekbar, (int) (menuSetting.getItemBarWidth() * 100f / screenWidth), () -> {
            menuSetting.setItemBarWidth((int) (screenWidth / 100f * itemBarWidthSeekbar.progressFlow().getValue()));
            GameOption.GameOptionListener optionListener = gameItemBar.getOptionListener();
            if (optionListener != null) {
                optionListener.onOptionChanged(true);
            }
        });
        int screenHeight = AndroidUtils.getScreenHeight();
        initSeekbar(itemBarHeightSeekbar, (int) (menuSetting.getItemBarHeight() * 100f / screenHeight), () -> {
            menuSetting.setItemBarHeight((int) (screenHeight / 100f * itemBarHeightSeekbar.progressFlow().getValue()));
            GameOption.GameOptionListener optionListener = gameItemBar.getOptionListener();
            if (optionListener != null) {
                optionListener.onOptionChanged(true);
            }
        });

        initSeekbar(windowScaleSeekbar, (int) (menuSetting.getWindowScale() * 100), () -> {
            double doubleValue = windowScaleSeekbar.progressFlow().getValue() / 100d;
            menuSetting.setWindowScale(doubleValue);
            refreshWindowsSize(doubleValue);
        });

        initSeekbar(cursorOffsetSeekbar, (int) (menuSetting.getCursorOffset()), () -> {
            menuSetting.setCursorOffset(cursorOffsetSeekbar.progressFlow().getValue());
            if (fclBridge != null) {
                refreshWindowsSize(menuSetting.getWindowScale());
            }
        });

        initSeekbar(mouseSensitivitySeekbar, (int) (menuSetting.getMouseSensitivity() * 100), () -> menuSetting.setMouseSensitivity(mouseSensitivitySeekbar.progressFlow().getValue() / 100d));
        initSeekbar(mouseSensitivityCursorSeekbar, (int) (menuSetting.getMouseSensitivityCursor() * 100), () -> menuSetting.setMouseSensitivityCursor(mouseSensitivityCursorSeekbar.progressFlow().getValue() / 100d));
        initSeekbar(mouseSizeSeekbar, menuSetting.getMouseSize(), () -> menuSetting.setMouseSize(mouseSizeSeekbar.progressFlow().getValue()));
        initSeekbar(mouseOffsetXSeekbar, menuSetting.getMouseOffsetX(), () -> {
            menuSetting.setMouseOffsetX(mouseOffsetXSeekbar.progressFlow().getValue());
            cursorView.setOffsetX(menuSetting.getMouseOffsetX());
        });
        initSeekbar(mouseOffsetYSeekbar, menuSetting.getMouseOffsetY(), () -> {
            menuSetting.setMouseOffsetY(mouseOffsetYSeekbar.progressFlow().getValue());
            cursorView.setOffsetY(menuSetting.getMouseOffsetY());
        });
        initSeekbar(gamepadDeadzoneSeekbar, (int) (menuSetting.getGamepadDeadzone() * 100), () -> menuSetting.setGamepadDeadzone(gamepadDeadzoneSeekbar.progressFlow().getValue() / 100d));
        initSeekbar(gyroSensitivitySeekbar, menuSetting.getGyroscopeSensitivity(), () -> menuSetting.setGyroscopeSensitivity(gyroSensitivitySeekbar.progressFlow().getValue()));

        openMultiplayerButton.setOnClickListener(this);
        manageQuickInput.setOnClickListener(this);
        sendKeycode.setOnClickListener(this);
        gamepadResetMapper.setOnClickListener(this);
        gamepadButtonBinding.setOnClickListener(this);
        forceExit.setOnClickListener(this);
    }

    private void initSeekbar(FCLNumberSeekBar bar, int initValue, Runnable listener) {
        bar.addProgressListener();
        bar.progressFlow().setValue(initValue);
        menuSettingSubscriptions.add(FlowSubscriptions.subscribe(bar.progressFlow(), v -> listener.run()));
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

        this.menuSetting.addPropertyChangedListener(() -> {
            String content = new GsonBuilder().setPrettyPrinting().create().toJson(menuSetting);
            try {
                FileUtils.writeText(new File(FCLPath.FILES_DIR + "/menu_setting.json"), content);
            } catch (IOException e) {
                Logging.LOG.log(Level.SEVERE, "Failed to save menu setting", e);
            }
        });

        editModeFlow.setValue(isSimulated());
        controllerFlow.setValue(Controllers.findControllerById(activity.getIntent().getExtras().getString("controller")));

        baseLayout = findViewById(R.id.base_layout);
        touchPad = findViewById(R.id.touch_pad);
        gameItemBar = findViewById(R.id.game_item_bar);
        logWindow = findViewById(R.id.log_window);
        fpsText = findViewById(R.id.fps);
        memoryText = findViewById(R.id.memory);
        touchCharInput = findViewById(R.id.input_scanner);
        touchControllerInputView = findViewById(R.id.touchcontroller_input_view);
        launchProgress = findViewById(R.id.launch_progress);
        cursorView = findViewById(R.id.cursor);

        if (!isSimulated()) {
            ImageUtil.loadInto(baseLayout, ThemeEngine.getInstance().getTheme().getBackground(activity));
            launchProgress.setVisibility(View.VISIBLE);
            assert getBridge() != null;
            gameOption = new GameOption(getBridge().getGameDir());
            touchPad.post(() -> gameItemBar.setup(this, gameOption));
        }
        touchPad.init(this);
        touchCharInput.setCharacterSender(this, new LwjglCharSender(this));
        initCursorView(activity);
        menuSettingSubscriptions.add(FlowSubscriptions.subscribe(menuSetting.getMouseSizeFlow(), v -> initCursorView(activity)));

        gyroscope = new Gyroscope(this);
        menuSettingSubscriptions.add(FlowSubscriptions.subscribeWithCurrent(menuSetting.getEnableGyroscopeFlow(), v -> gyroscope.enableFlow().setValue(v)));

        viewManager = new ViewManager(this);

        initLeftMenu();
        initRightMenu();

        viewManager.setup();

        if (new File(FCLPath.FILES_DIR, "cursor.gif").exists()) {
            Glide.with(getCursor()).asGif().skipMemoryCache(true).load(new File(FCLPath.FILES_DIR, "cursor.gif")).into(new CustomViewTarget<CursorView, GifDrawable>(getCursor()) {
                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                }

                @Override
                public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                    getCursor().setImageDrawable(resource);
                    resource.start();
                }

                @Override
                protected void onResourceCleared(@Nullable Drawable placeholder) {
                }
            });
        } else if (new File(FCLPath.FILES_DIR, "cursor.png").exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(new File(FCLPath.FILES_DIR, "cursor.png").getAbsolutePath());
            BitmapDrawable drawable = new BitmapDrawable(getActivity().getResources(), bitmap);
            getCursor().setImageDrawable(drawable);
        }

        if (getBridge() != null && getBridge().hasTouchController()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("launcher", MODE_PRIVATE);
            touchController = new TouchController(getActivity(), AndroidUtils.getScreenWidth(), AndroidUtils.getScreenHeight(), sharedPreferences.getInt("vibrationDuration", 100));

            touchControllerInputView.setClient(touchController.getClient());
            touchControllerInputView.setFclInput(fclInput);
            touchControllerInputView.setSize(AndroidUtils.getScreenWidth(), AndroidUtils.getScreenHeight());
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
        if (menuSetting.isHideMenuView()) {
            Toast.makeText(activity, R.string.tip_hide_menu_view, Toast.LENGTH_LONG).show();
        }
    }

    private void initCursorView(FCLActivity activity) {
        ViewGroup.LayoutParams layoutParams = cursorView.getLayoutParams();
        layoutParams.width = ConvertUtils.dip2px(activity, menuSetting.getMouseSize());
        layoutParams.height = ConvertUtils.dip2px(activity, menuSetting.getMouseSize());
        cursorView.setLayoutParams(layoutParams);
        cursorView.setOffsetX(menuSetting.getMouseOffsetX());
        cursorView.setOffsetY(menuSetting.getMouseOffsetX());
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
    public CursorView getCursor() {
        return cursorView;
    }

    public GameOption getGameOption() {
        return gameOption;
    }

    @Override
    public void onPause() {
        if (cursorModeFlow.getValue() == FCLBridge.CursorDisabled) {
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
            logWindow.setVisibility(false);
        }
    }

    @Override
    public void onCursorModeChange(int mode) {
        activity.runOnUiThread(() -> {
            if (lastCursorMode == mode)
                return;
            lastCursorMode = mode;
            this.cursorModeFlow.setValue(mode);
            if (mode == FCLBridge.CursorEnabled) {
                getCursor().setVisibility(View.VISIBLE);
                gameItemBar.setVisibility(View.GONE);
                getInput().setPointer(AndroidUtils.getScreenWidth() / 2, AndroidUtils.getScreenHeight() / 2, "Gyro");
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
            JVMCrashActivity.startCrashActivity(true, activity, exitCode, fclBridge.getLogPath());
            Logging.LOG.log(Level.INFO, "JVM crashed, start jvm crash activity to show errors now!");
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @NonNull
    public final <T extends View> T findViewById(int id) {
        return getLayout().findViewById(id);
    }

    public void openQuickInput() {
        // 3.2 批 3 接入点：Miuix 游戏内快捷输入面板
        new MiuixQuickInputDialog(activity, this).show();
    }

    @Override
    public void onClick(View v) {
        if (v == manageViewGroups) {
            // 3.2 批 4 接入点：Miuix 视图组管理弹窗
            new MiuixViewGroupDialog(getActivity(), this, false, java.util.Collections.emptyList(), null).show();
        }
        if (v == addButton) {
            if (getViewGroup() == null) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.edit_view_no_group), Toast.LENGTH_SHORT).show();
            } else {
                EditViewDialog.Callback callback = new EditViewDialog.Callback() {
                    @Override
                    public void onPositive(CustomControl view) {
                        viewManager.addView(view);
                    }

                    @Override
                    public void onClone(CustomControl view) {
                        // Ignore
                    }
                };
                // 3.2 批 4 接入点：Miuix 控件属性编辑弹窗（按钮）
                new MiuixEditViewDialog(getActivity(), new ControlButtonData(UUID.randomUUID().toString()), this, callback, false).show();
            }
        }
        if (v == addDirection) {
            if (getViewGroup() == null) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.edit_view_no_group), Toast.LENGTH_SHORT).show();
            } else {
                EditViewDialog.Callback callback = new EditViewDialog.Callback() {
                    @Override
                    public void onPositive(CustomControl view) {
                        viewManager.addView(view);
                    }

                    @Override
                    public void onClone(CustomControl view) {
                        // Ignore
                    }
                };
                // 3.2 批 4 接入点：Miuix 控件属性编辑弹窗（方向键）
                new MiuixEditViewDialog(getActivity(), new ControlDirectionData(UUID.randomUUID().toString()), this, callback, false).show();
            }
        }
        if (v == manageButtonStyle) {
            // 3.2 批 4 接入点：Miuix 按钮样式管理弹窗
            new MiuixButtonStyleDialog(getActivity(), false, null, null).show();
        }
        if (v == manageDirectionStyle) {
            // 3.2 批 4 接入点：Miuix 方向键样式管理弹窗
            new MiuixDirectionStyleDialog(getActivity(), false, null, null).show();
        }
        if (v == openMultiplayerButton) {
            if (multiplayerDialog == null) {
                int width = (int) (AndroidUtils.getScreenWidth() * 0.7);
                int height = (int) (AndroidUtils.getScreenHeight() * 0.9);
                multiplayerDialog = new MultiplayerDialog(getActivity(), getActivity(), width, height);
            }
            multiplayerDialog.show();
        }
        if (v == manageQuickInput) {
            openQuickInput();
        }
        if (v == sendKeycode) {
            List<Integer> list = new ArrayList<>();
            // 4.1 接入点：Miuix 键码选择弹窗（发送键码回调）
            new MiuixSelectKeycodeDialog(getActivity(), list, false, true, (dialog) -> {
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
                // 3.2 批 2 接入点：Miuix 手柄按键映射弹窗
                new MiuixGamepadMapDialog(getActivity(), fclInput).show();
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
        int screenWidth = AndroidUtils.getScreenWidth();
        int screenHeight = AndroidUtils.getScreenHeight();
        if (fclBridge != null) {
            fclBridge.setScaleFactor(factor);
            int width = (int) ((screenWidth + menuSetting.getCursorOffset()) * factor);
            int height = (int) (screenHeight * factor);
            if (FCLBridge.FORCE_RESOLUTION) {
                width = FCLBridge.FORCE_RESOLUTION_WIDTH;
                height = FCLBridge.FORCE_RESOLUTION_HEIGHT;
            }
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
        public void onLog(String log) {
            gameMenu.onLog(log);
        }

        @Override
        public void onExit(int code) {
            gameMenu.onExit(code);
        }
    }
}
