package com.tungsten.fcl.control;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.GsonBuilder;
import com.mio.touchcontroller.TouchController;
import com.mio.touchcontroller.TouchControllerInputView;
import com.mio.ui.dialog.GamepadMapDialog;
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
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclauncher.utils.FCLPath;
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
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import fr.spse.gamepad_remapper.Remapper;
import kotlin.Unit;

public class GameMenu implements MenuCallback, FCLBridgeCallback {

    private boolean simulated;
    private FCLActivity activity;
    @Nullable
    private FCLBridge fclBridge;
    private FCLInput fclInput;
    private MenuSetting menuSetting;
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

    private LeftMenuAdapter leftMenuAdapter;
    private RightMenuAdapter rightMenuAdapter;
    private FCLTextView rightMenuTitle;
    private FCLTextView rightMenuBack;
    private RecyclerView rightMenuList;

    private MultiplayerDialog multiplayerDialog;

    /** 右菜单切换动画进行中标记，避免动画叠加 */
    private boolean rightMenuAnimating;

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
        return cursorModeProperty.get();
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

    /** 编辑模式下未选中视图组时选中第一个，保证编辑视图立即可加载，不依赖菜单列表绑定时的兜底回调 */
    private void selectDefaultViewGroup() {
        if (editModeProperty.get() && getViewGroup() == null && !getController().viewGroups().isEmpty()) {
            setViewGroup(getController().viewGroups().get(0));
        }
    }

    @Nullable
    public ControlViewGroup getViewGroup() {
        return viewGroupProperty.get();
    }

    public boolean isGamepadDisabled() {
        return gamepadDisabled;
    }

    public void setGamepadDisabled(boolean gamepadDisabled) {
        this.gamepadDisabled = gamepadDisabled;
    }

    private void initLeftMenu() {
        RecyclerView leftMenuList = findViewById(R.id.left_menu_list);
        leftMenuList.setLayoutManager(new LinearLayoutManager(activity));
        leftMenuAdapter = new LeftMenuAdapter(activity, this, new LeftMenuAdapter.Listener() {
            @Override
            public void onButtonClick(@NonNull LeftMenuTag tag) {
                handleLeftButtonClick(tag);
            }

            @Override
            public void onSwitchToggle(@NonNull LeftMenuTag tag, boolean checked) {
                handleLeftSwitchToggle(tag, checked);
            }

            @Override
            public void onSpinnerSelect(@NonNull LeftMenuTag tag, int position) {
                handleLeftSpinnerSelect(tag, position);
            }

            @Override
            public void onSeekBarChange(@NonNull LeftMenuTag tag, int progress) {
                handleLeftSeekBarChange(tag, progress);
            }
        });
        leftMenuList.setAdapter(leftMenuAdapter);
        leftMenuAdapter.rebuild();

        getController().addListener(i -> leftMenuAdapter.rebuild());
        controllerProperty.addListener(invalidate -> {
            setViewGroup(null);
            leftMenuAdapter.rebuild();
            getController().addListener(i -> leftMenuAdapter.rebuild());
        });
        editModeProperty.addListener(i -> leftMenuAdapter.rebuild());

        hideAllViewsProperty.addListener(i -> {
            if (isHideAllViews()) {
                Toast.makeText(activity, R.string.tip_hide_menu_view, Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initRightMenu() {
        rightMenuList = findViewById(R.id.right_menu_list);
        rightMenuList.setLayoutManager(new LinearLayoutManager(activity));
        rightMenuAdapter = new RightMenuAdapter(activity, this, new RightMenuAdapter.Listener() {
            @Override
            public void onCategoryClick(@NonNull RightMenuCategory category) {
                showCategory(category);
            }

            @Override
            public void onButtonClick(@NonNull RightMenuTag tag) {
                handleRightButtonClick(tag);
            }

            @Override
            public void onSwitchToggle(@NonNull RightMenuTag tag, boolean checked) {
                handleRightSwitchToggle(tag, checked);
            }

            @Override
            public void onSwitchLongClick(@NonNull RightMenuTag tag) {
                handleRightSwitchLongClick(tag);
            }

            @Override
            public void onSpinnerSelect(@NonNull RightMenuTag tag, int position) {
                handleRightSpinnerSelect(tag, position);
            }

            @Override
            public void onSeekBarChange(@NonNull RightMenuTag tag, int progress) {
                handleRightSeekBarChange(tag, progress);
            }
        });
        rightMenuList.setAdapter(rightMenuAdapter);
        rightMenuAdapter.rebuild();

        rightMenuTitle = findViewById(R.id.menu_title);
        rightMenuBack = findViewById(R.id.menu_back);
        rightMenuBack.setOnClickListener(v -> showCategories());

        logWindow.setVisibility(menuSetting.isShowLog() || (!isSimulated() && menuSetting.isAutoShowLog()));
    }

    private void showCategory(RightMenuCategory category) {
        switchRightMenuContent(true, () -> {
            rightMenuAdapter.showCategory(category);
            rightMenuTitle.setText(category.getTitleRes());
            rightMenuBack.setVisibility(View.VISIBLE);
        });
    }

    private void showCategories() {
        switchRightMenuContent(false, () -> {
            rightMenuAdapter.showCategories();
            rightMenuTitle.setText(R.string.menu_settings);
            rightMenuBack.setVisibility(View.GONE);
        });
    }

    /** 菜单切换动画：旧列表淡出后切换内容，新列表滑入淡入 */
    private void switchRightMenuContent(boolean toCategory, Runnable refresh) {
        if (rightMenuAnimating) {
            refresh.run();
            return;
        }
        rightMenuAnimating = true;
        AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
        fadeOut.setDuration(150);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                refresh.run();
                rightMenuList.scrollToPosition(0);
                AnimationSet enter = new AnimationSet(true);
                enter.addAnimation(new AlphaAnimation(0f, 1f));
                // 进入二级菜单从右侧滑入，返回一级从左侧滑入
                enter.addAnimation(new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, toCategory ? 0.15f : -0.15f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f));
                enter.setDuration(180);
                enter.setInterpolator(new DecelerateInterpolator());
                enter.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        rightMenuAnimating = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                rightMenuList.startAnimation(enter);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        rightMenuList.startAnimation(fadeOut);
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

        this.menuSetting.addOnChangeListener(() -> {
            String content = new GsonBuilder().setPrettyPrinting().create().toJson(menuSetting);
            try {
                FileUtils.writeText(new File(FCLPath.FILES_DIR + "/menu_setting.json"), content);
            } catch (IOException e) {
                Logging.LOG.log(Level.SEVERE, "Failed to save menu setting", e);
            }
        });

        editModeProperty.set(isSimulated());
        controllerProperty.set(Controllers.findControllerById(activity.getIntent().getExtras().getString("controller")));
        selectDefaultViewGroup();

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

        gyroscope = new Gyroscope(this);

        viewManager = new ViewManager(this);

        initLeftMenu();
        initRightMenu();

        viewManager.setup();

        // 初始化时应用开关副作用（FPS/内存线程、持续性能模式），
        // 使重启后已开启的设置保持生效（开关行绑定不触发副作用回调）
        toggleFps(menuSetting.isShowFps());
        toggleMemory(menuSetting.isShowMemory());
        activity.getWindow().setSustainedPerformanceMode(menuSetting.isPerformanceMode());

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
            touchController = new TouchController(getActivity(), AndroidUtilKt.getScreenWidth(), AndroidUtilKt.getScreenHeight(), sharedPreferences.getInt("vibrationDuration", 100));

            touchControllerInputView.setClient(touchController.getClient());
            touchControllerInputView.setFclInput(fclInput);
            touchControllerInputView.setSize(AndroidUtilKt.getScreenWidth(), AndroidUtilKt.getScreenHeight());
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
        return this;
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
            logWindow.setVisibility(false);
        }
    }

    @Override
    public void onCursorModeChange(int mode) {
        activity.runOnUiThread(() -> {
            if (lastCursorMode == mode)
                return;
            lastCursorMode = mode;
            this.cursorModeProperty.set(mode);
            if (mode == FCLBridge.CursorEnabled) {
                getCursor().setVisibility(View.VISIBLE);
                gameItemBar.setVisibility(View.GONE);
                getInput().setPointer(AndroidUtilKt.getScreenWidth() / 2, AndroidUtilKt.getScreenHeight() / 2, "Gyro");
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
        QuickInputDialog dialog = new QuickInputDialog(activity, this);
        dialog.show();
    }

    private void handleLeftButtonClick(LeftMenuTag tag) {
        switch (tag) {
            case MANAGE_VIEW_GROUPS: {
                ViewGroupDialog dialog = new ViewGroupDialog(getActivity(), this, false, FXCollections.observableList(new ArrayList<>()), null);
                dialog.show();
                break;
            }
            case ADD_BUTTON: {
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
                break;
            }
            case ADD_DIRECTION: {
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
                break;
            }
            case MANAGE_BUTTON_STYLE: {
                ButtonStyleDialog dialog = new ButtonStyleDialog(getActivity(), false, null, null);
                dialog.show();
                break;
            }
            case MANAGE_DIRECTION_STYLE: {
                DirectionStyleDialog dialog = new DirectionStyleDialog(getActivity(), false, null, null);
                dialog.show();
                break;
            }
        }
    }

    private void handleLeftSwitchToggle(LeftMenuTag tag, boolean checked) {
        switch (tag) {
            case EDIT_MODE:
                setEditMode(checked);
                if (checked) {
                    selectDefaultViewGroup();
                }
                break;
            case SHOW_BOUNDARY:
                setShowViewBoundaries(checked);
                break;
            case HIDE_ALL:
                setHideAllViews(checked);
                break;
            case AUTO_FIT:
                menuSetting.setAutoFit(checked);
                break;
        }
    }

    private void handleLeftSpinnerSelect(LeftMenuTag tag, int position) {
        if (tag == LeftMenuTag.CURRENT_CONTROLLER) {
            setController(Controllers.getControllers().get(position));
        } else if (tag == LeftMenuTag.CURRENT_VIEW_GROUP) {
            ControlViewGroup viewGroup = getController().viewGroups().get(position);
            setViewGroup(viewGroup);
            if (viewGroup != null) {
                viewGroup.getViewData().buttonList().forEach(it -> {
                    String name = it.getStyle().getName();
                    ControlButtonStyle style = ButtonStyles.findStyleByName(name);
                    if (name.equals(style.getName())) {
                        it.setStyle(style);
                    }
                });
                viewGroup.getViewData().directionList().forEach(it -> {
                    String name = it.getStyle().getName();
                    ControlDirectionStyle style = DirectionStyles.findStyleByName(name);
                    if (name.equals(style.getName())) {
                        it.setStyle(style);
                    }
                });
            }
        }
    }

    private void handleLeftSeekBarChange(LeftMenuTag tag, int progress) {
        if (tag == LeftMenuTag.AUTO_FIT_DIST) {
            menuSetting.setAutoFitDist(progress);
        }
    }

    private void handleRightButtonClick(RightMenuTag tag) {
        switch (tag) {
            case OPEN_MULTIPLAYER: {
                if (multiplayerDialog == null) {
                    int width = (int) (AndroidUtilKt.getScreenWidth() * 0.7);
                    int height = (int) (AndroidUtilKt.getScreenHeight() * 0.9);
                    multiplayerDialog = new MultiplayerDialog(getActivity(), getActivity(), width, height);
                }
                multiplayerDialog.show();
                break;
            }
            case OPEN_QUICK_INPUT:
                openQuickInput();
                break;
            case OPEN_SEND_KEY: {
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
                break;
            }
            case GAMEPAD_RESET_MAPPER:
                Remapper.wipePreferences(getActivity());
                getInput().resetMapper();
                break;
            case GAMEPAD_BUTTON_BINDING:
                fclInput.checkGamepad();
                if (fclInput.getGamepad() != null) {
                    new GamepadMapDialog(getActivity(), fclInput).show();
                }
                break;
            case FORCE_EXIT: {
                FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(activity);
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                builder.setMessage(activity.getString(R.string.menu_settings_force_exit_msg));
                builder.setPositiveButton(() -> android.os.Process.killProcess(android.os.Process.myPid()));
                builder.setNegativeButton(null);
                builder.setCancelable(false);
                builder.create().show();
                break;
            }
        }
    }

    private void handleRightSwitchToggle(RightMenuTag tag, boolean checked) {
        switch (tag) {
            case LOCK_VIEW:
                menuSetting.setLockMenuView(checked);
                break;
            case HIDE_VIEW:
                menuSetting.setHideMenuView(checked);
                menuView.setVisibility(checked ? View.INVISIBLE : View.VISIBLE);
                if (checked) {
                    Toast.makeText(activity, R.string.tip_hide_menu_view, Toast.LENGTH_LONG).show();
                }
                break;
            case SHOW_FPS:
                toggleFps(checked);
                break;
            case SHOW_MEMORY:
                toggleMemory(checked);
                break;
            case SOFT_KEYBOARD_ADJUST:
                menuSetting.setDisableSoftKeyAdjust(checked);
                break;
            case DISABLE_GESTURE:
                menuSetting.setDisableGesture(checked);
                break;
            case DISABLE_LEFT_TOUCH:
                menuSetting.setDisableLeftTouch(checked);
                break;
            case GYRO:
                menuSetting.setEnableGyroscope(checked);
                if (checked) {
                    gyroscope.enableSensor();
                } else {
                    gyroscope.disableSensor();
                }
                break;
            case GYRO_INVERT:
                menuSetting.setInvertGyroscope(checked);
                break;
            case PHYSICAL_MOUSE:
                menuSetting.setPhysicalMouseMode(checked);
                break;
            case DISABLE_GAMEPAD_MAPPING:
                gamepadDisabled = checked;
                break;
            case PERFORMANCE_MODE:
                menuSetting.setPerformanceMode(checked);
                activity.getWindow().setSustainedPerformanceMode(checked);
                break;
            case SHOW_LOG:
                menuSetting.setShowLog(checked);
                logWindow.setVisibility(menuSetting.isShowLog());
                break;
            case AUTO_SHOW_LOG:
                menuSetting.setAutoShowLog(checked);
                if (baseLayout.getBackground() != null) {
                    logWindow.setVisibility(menuSetting.isAutoShowLog());
                }
                break;
        }
    }

    private void handleRightSwitchLongClick(RightMenuTag tag) {
        if (tag == RightMenuTag.SHOW_FPS) {
            fpsText.resetPosition();
        } else if (tag == RightMenuTag.SHOW_MEMORY) {
            memoryText.resetPosition();
        }
    }

    private void handleRightSpinnerSelect(RightMenuTag tag, int position) {
        if (tag == RightMenuTag.GESTURE_MODE) {
            menuSetting.setGestureMode(GestureMode.getById(position));
        } else if (tag == RightMenuTag.MOUSE_MODE) {
            menuSetting.setMouseMoveMode(MouseMoveMode.getById(position));
        }
    }

    private void handleRightSeekBarChange(RightMenuTag tag, int progress) {
        int screenWidth = AndroidUtilKt.getScreenWidth();
        int screenHeight = AndroidUtilKt.getScreenHeight();
        switch (tag) {
            case ITEM_BAR_WIDTH:
                menuSetting.setItemBarWidth((int) (screenWidth / 100f * progress));
                GameOption.GameOptionListener widthListener = gameItemBar.getOptionListener();
                if (widthListener != null) {
                    widthListener.onOptionChanged(true);
                }
                break;
            case ITEM_BAR_HEIGHT:
                menuSetting.setItemBarHeight((int) (screenHeight / 100f * progress));
                GameOption.GameOptionListener heightListener = gameItemBar.getOptionListener();
                if (heightListener != null) {
                    heightListener.onOptionChanged(true);
                }
                break;
            case WINDOW_SCALE: {
                double doubleValue = progress / 100d;
                menuSetting.setWindowScale(doubleValue);
                refreshWindowsSize(doubleValue);
                break;
            }
            case CURSOR_OFFSET:
                menuSetting.setCursorOffset(progress);
                if (fclBridge != null) {
                    refreshWindowsSize(menuSetting.getWindowScale());
                }
                break;
            case MOUSE_SENSITIVITY:
                menuSetting.setMouseSensitivity(progress / 100d);
                break;
            case MOUSE_CURSOR_SENSITIVITY:
                menuSetting.setMouseSensitivityCursor(progress / 100d);
                break;
            case MOUSE_SIZE:
                menuSetting.setMouseSize(progress);
                initCursorView(activity);
                break;
            case MOUSE_OFFSET_X:
                menuSetting.setMouseOffsetX(progress);
                cursorView.setOffsetX(menuSetting.getMouseOffsetX());
                break;
            case MOUSE_OFFSET_Y:
                menuSetting.setMouseOffsetY(progress);
                cursorView.setOffsetY(menuSetting.getMouseOffsetY());
                break;
            case GAMEPAD_DEADZONE:
                menuSetting.setGamepadDeadzone(progress / 100d);
                break;
            case GYRO_SENSITIVITY:
                menuSetting.setGyroscopeSensitivity(progress);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void toggleFps(boolean checked) {
        menuSetting.setShowFps(checked);
        if (isSimulated()) {
            return;
        }
        if (checked) {
            fpsThread = new Thread(() -> {
                FCLBridge.getFps();
                while (menuSetting.isShowFps() && !Thread.currentThread().isInterrupted()) {
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
    }

    @SuppressLint("SetTextI18n")
    private void toggleMemory(boolean checked) {
        menuSetting.setShowMemory(checked);
        if (isSimulated()) {
            return;
        }
        if (checked) {
            memoryThread = new Thread(() -> {
                while (menuSetting.isShowMemory() && !Thread.currentThread().isInterrupted()) {
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
    }

    private void refreshWindowsSize(double factor) {
        int screenWidth = AndroidUtilKt.getScreenWidth();
        int screenHeight = AndroidUtilKt.getScreenHeight();
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
}
