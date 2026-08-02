package com.tungsten.fcl.control.view;

import static com.tungsten.fclauncher.keycodes.MinecraftKeyBindingMapper.BINDING_CHAT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.EditViewDialog;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.GestureMode;
import com.tungsten.fcl.control.MouseMoveMode;
import com.tungsten.fcl.control.data.BaseInfoData;
import com.tungsten.fcl.control.data.ButtonEventData;
import com.tungsten.fcl.control.data.ControlButtonData;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.control.data.CustomControl;
import com.tungsten.fcl.setting.GameOption;
import com.tungsten.fcl.ui.compose.dialog.MiuixEditViewDialog;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * Custom game control button.
 */
@SuppressLint("ViewConstructor")
public class ControlButton extends AppCompatButton implements CustomView {

    private Runnable notifyAction;
    private Consumer<ControlButtonData> dataChangeListener;
    private Runnable boundaryAction;
    private Consumer<Boolean> visibilityListener;
    private Runnable alphaAction;

    // 阶段 4c：menu 属性 / dataFlow / visibilityFlow 的订阅句柄，removeListener 时取消（对齐原 removeListener 摘除）。
    private FlowSubscriptions.Subscription notifySubscription;
    private FlowSubscriptions.Subscription boundarySubscription;
    private FlowSubscriptions.Subscription alphaSubscription;
    private FlowSubscriptions.Subscription dataChangeSubscription;
    private FlowSubscriptions.Subscription visibilitySubscription;

    // 阶段 4c：visibility 派生（原 Bindings.createBooleanBinding）的依赖订阅登记，
    // unbindVisibility 时统一取消（对齐原 unbind()）。
    private final List<FlowSubscriptions.Subscription> visibilityRecomputeSubscriptions = new ArrayList<>();
    private FlowSubscriptions.Subscription parentVisibilitySubscription;

    // 阶段 4b：数据层监听由 getData().addListener(notifyListener) 改为订阅
    // ControlButtonData.revisionFlow（失效即递增，语义等价）；data 切换时换绑，
    // removeListener 时取消（对齐原 removeListener 摘除）。
    private FlowSubscriptions.Subscription dataSubscription;

    private void subscribeDataRevision() {
        if (dataSubscription != null) {
            dataSubscription.cancel();
        }
        dataSubscription = FlowSubscriptions.subscribe(getData().revisionFlow(), v -> Schedulers.androidUIThread().execute(() -> {
            notifyData();
            cancelAllEvent();
        }));
    }

    private final GameMenu menu;
    private Path boundaryPath;
    private final Paint boundaryPaint;
    private final int screenWidth;
    private final int screenHeight;
    private int cursorMode;

    private MutableStateFlow<Boolean> visibilityFlow;

    private final MutableStateFlow<Boolean> parentVisibilityFlow = StateFlowKt.MutableStateFlow(true);

    public MutableStateFlow<Boolean> parentVisibilityFlow() {
        return parentVisibilityFlow;
    }

    public void setParentVisibility(boolean parentVisibility) {
        parentVisibilityFlow.setValue(parentVisibility);
    }

    public boolean isParentVisibility() {
        return parentVisibilityFlow.getValue();
    }

    private final MutableStateFlow<ControlButtonData> dataFlow = StateFlowKt.MutableStateFlow(new ControlButtonData(UUID.randomUUID().toString()));

    public MutableStateFlow<ControlButtonData> dataFlow() {
        return dataFlow;
    }

    public void setData(ControlButtonData data) {
        dataFlow.setValue(data);
    }

    public ControlButtonData getData() {
        return dataFlow.getValue();
    }

    public ControlButton(@NonNull Context context, GameMenu gameMenu, ViewListener listener) {
        super(context);
        this.menu = gameMenu;
        setElevation(113.0f);

        setStateListAnimator(null);

        boundaryPath = new Path();
        boundaryPaint = new Paint();
        boundaryPaint.setAntiAlias(true);
        boundaryPaint.setColor(Color.RED);
        boundaryPaint.setStyle(Paint.Style.STROKE);
        boundaryPaint.setStrokeWidth(3);
        screenWidth = AndroidUtils.getScreenWidth();
        screenHeight = AndroidUtils.getScreenHeight();

        notifyAction = () -> Schedulers.androidUIThread().execute(() -> {
            notifyData();
            cancelAllEvent();
        });
        dataChangeListener = v -> Schedulers.androidUIThread().execute(() -> {
            notifyData();
            cancelAllEvent();
            subscribeDataRevision();
        });
        boundaryAction = () -> Schedulers.androidUIThread().execute(() -> {
            boundaryPath = new Path();
            invalidate();
        });
        visibilityListener = v -> Schedulers.androidUIThread().execute(() -> {
            if (!visibilityFlow().getValue()) {
                cancelAllEvent();
            }
        });
        alphaAction = () -> Schedulers.androidUIThread().execute(() -> {
            setAlpha(menu.isHideAllViews() ? 0 : 1);
        });

        post(() -> {
            notifyData();
            if (notifyAction == null || dataChangeListener == null || boundaryAction == null || visibilityListener == null) {
                return;
            }
            notifySubscription = FlowSubscriptions.subscribe(menu.editModeFlow(), v -> notifyAction.run());
            dataChangeSubscription = FlowSubscriptions.subscribe(dataFlow(), dataChangeListener);
            subscribeDataRevision();
            boundarySubscription = FlowSubscriptions.subscribe(menu.showViewBoundariesFlow(), v -> boundaryAction.run());
            setAlpha(menu.isHideAllViews() ? 0 : 1);
            alphaSubscription = FlowSubscriptions.subscribe(menu.hideAllViewsFlow(), v -> alphaAction.run());
            if (listener != null) {
                listener.onReady(this);
            }
        });
    }

    private void notifyData() {
        if (visibilityListener == null) {
            return;
        }
        ControlButtonData data = getData();

        setText(data.getText());
        refreshBaseInfo(data);
        post(() -> {
            refreshStyle(data);
            boundaryPath = new Path();
            invalidate();
        });
    }

    private void refreshBaseInfo(ControlButtonData data) {
        // Size
        int width;
        int height;
        if (data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.ABSOLUTE) {
            width = ConvertUtils.dip2px(getContext(), data.getBaseInfo().getAbsoluteWidth());
            height = ConvertUtils.dip2px(getContext(), data.getBaseInfo().getAbsoluteHeight());
        } else {
            width = data.getBaseInfo().getPercentageWidth().getReference() == BaseInfoData.PercentageSize.Reference.SCREEN_WIDTH ?
                    (int) (screenWidth * (data.getBaseInfo().getPercentageWidth().getSize() / 1000f)) :
                    (int) (screenHeight * (data.getBaseInfo().getPercentageWidth().getSize() / 1000f));
            height = data.getBaseInfo().getPercentageHeight().getReference() == BaseInfoData.PercentageSize.Reference.SCREEN_WIDTH ?
                    (int) (screenWidth * (data.getBaseInfo().getPercentageHeight().getSize() / 1000f)) :
                    (int) (screenHeight * (data.getBaseInfo().getPercentageHeight().getSize() / 1000f));
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        setLayoutParams(layoutParams);

        // Position
        post(() -> {
            int x;
            int y;
            x = (int) ((screenWidth - width) * (data.getBaseInfo().getXPosition() / 1000f));
            y = (int) ((screenHeight - height) * (data.getBaseInfo().getYPosition() / 1000f));
            setX(x);
            setY(y);
        });

        // Visibility
        // 阶段 4c：原 visibilityProperty().unbind() + bind(Bindings.createBooleanBinding(...))
        // 改为“初值 + 依赖变化重算”（对齐原 bind 立即求值、依赖失效重算）。
        unbindVisibility();
        if (menu.isEditMode()) {
            Runnable recompute = () -> visibilityFlow().setValue(menu.getViewGroup() != null && (menu.getViewGroup().getViewData().getButtonList().stream().anyMatch(it -> it.getId().equals(getData().getId()))));
            recompute.run();
            visibilityRecomputeSubscriptions.add(FlowSubscriptions.subscribe(menu.editModeFlow(), v -> recompute.run()));
            visibilityRecomputeSubscriptions.add(FlowSubscriptions.subscribe(menu.viewGroupFlow(), v -> recompute.run()));
        } else {
            Runnable recompute = () -> visibilityFlow().setValue(isParentVisibility() && (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.ALWAYS ||
                            (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.IN_GAME && menu.getCursorMode() == FCLBridge.CursorDisabled) ||
                            (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.MENU && menu.getCursorMode() == FCLBridge.CursorEnabled)));
            recompute.run();
            visibilityRecomputeSubscriptions.add(FlowSubscriptions.subscribe(menu.cursorModeFlow(), v -> recompute.run()));
            parentVisibilitySubscription = FlowSubscriptions.subscribe(parentVisibilityFlow(), v -> recompute.run());
        }
        // 原代码每次 refreshBaseInfo 都 addListener(visibilityListener)（重复注册、回调幂等）；
        // Flow 订阅只建一次，语义等价且避免泄漏。
        if (visibilitySubscription == null) {
            visibilitySubscription = FlowSubscriptions.subscribe(visibilityFlow(), visibilityListener);
        }
    }

    /** 对齐原 visibilityProperty().unbind()：摘除当前派生的全部依赖订阅，保留当前值。 */
    private void unbindVisibility() {
        visibilityRecomputeSubscriptions.forEach(FlowSubscriptions.Subscription::cancel);
        visibilityRecomputeSubscriptions.clear();
        if (parentVisibilitySubscription != null) {
            parentVisibilitySubscription.cancel();
            parentVisibilitySubscription = null;
        }
    }

    private GradientDrawable drawableNormal;
    private GradientDrawable drawablePressed;

    private void refreshStyle(ControlButtonData data) {
        drawableNormal = new GradientDrawable();
        drawableNormal.setCornerRadius(ConvertUtils.dip2px(getContext(), data.getStyle().getCornerRadius() / 10f));
        drawableNormal.setStroke(ConvertUtils.dip2px(getContext(), data.getStyle().getStrokeWidth() / 10f), data.getStyle().getStrokeColor());
        drawableNormal.setColor(data.getStyle().getFillColor());
        drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(ConvertUtils.dip2px(getContext(), data.getStyle().getCornerRadiusPressed() / 10f));
        drawablePressed.setStroke(ConvertUtils.dip2px(getContext(), data.getStyle().getStrokeWidthPressed() / 10f), data.getStyle().getStrokeColorPressed());
        drawablePressed.setColor(data.getStyle().getFillColorPressed());
        setGravity(Gravity.CENTER);
        setPadding(0, 0, 0, 0);
        setAllCaps(false);
        setTextSize(data.getStyle().getTextSize());
        setTextColor(data.getStyle().getTextColor());
        setBackground(drawableNormal);
    }

    private void setNormalStyle() {
        setTextSize(getData().getStyle().getTextSize());
        setTextColor(getData().getStyle().getTextColor());
        setBackground(drawableNormal);
    }

    private void setPressedStyle() {
        setTextSize(getData().getStyle().getTextSizePressed());
        setTextColor(getData().getStyle().getTextColorPressed());
        setBackground(drawablePressed);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (menu.isShowViewBoundaries()) {
            boundaryPath.moveTo(0, 0);
            boundaryPath.lineTo(getWidth(), 0);
            boundaryPath.lineTo(getWidth(), getHeight());
            boundaryPath.lineTo(0, getHeight());
            boundaryPath.lineTo(0, 0);
            canvas.drawPath(boundaryPath, boundaryPaint);
        }
    }

    private float downX;
    private float downY;
    private int initialX;
    private int initialY;
    private float positionX;
    private float positionY;
    private long downTime;
    private boolean pressEvent = false;
    private boolean longPress = false;
    private boolean longPressEvent = false;
    private boolean clickEvent = false;
    private int clickCount = 0;
    private long firstClickTime;
    private boolean doubleClickEvent = false;

    private final Handler handler = new Handler();
    private final Runnable runnable = () -> handleLongPressEvent(!longPressEvent);

    private void deleteView() {
        if (menu != null) {
            menu.getViewManager().removeView(getData());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (menu.isEditMode()) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    setPressedStyle();
                    downX = event.getX();
                    downY = event.getY();
                    positionX = getX();
                    positionY = getY();
                    downTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int deltaX = (int) (event.getX() - downX);
                    int deltaY = (int) (event.getY() - downY);
                    float targetX = Math.max(0, Math.min(screenWidth - getWidth(), getX() + deltaX));
                    float targetY = Math.max(0, Math.min(screenHeight - getHeight(), getY() + deltaY));
                    setX(targetX);
                    setY(targetY);
                    autoFitPosition();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    removeLine(0);
                    removeLine(1);
                    setNormalStyle();
                    if (System.currentTimeMillis() - downTime <= 100
                            && Math.abs(event.getX() - downX) <= 10
                            && Math.abs(event.getY() - downY) <= 10) {
                        setX(positionX);
                        setY(positionY);
                        // 3.2 批 4 接入点：Miuix 按钮编辑弹窗
                        EditViewDialog.Callback callback = new EditViewDialog.Callback() {
                            @Override
                            public void onPositive(CustomControl view) {
                                ControlButtonData newData = ((ControlButtonData) view).clone();
                                getData().setText(newData.getText());
                                getData().setBaseInfo(newData.getBaseInfo());
                                getData().setStyle(newData.getStyle());
                                getData().setEvent(newData.getEvent());
                                menu.getViewManager().saveController();
                            }

                            @Override
                            public void onClone(CustomControl view) {
                                menu.getViewManager().addView(view);
                            }

                            @Override
                            public void onDelete() {
                                menu.getViewManager().removeView(getData());
                            }
                        };
                        MiuixEditViewDialog dialog = new MiuixEditViewDialog(getContext(), getData().clone(), menu, callback, true);
                        dialog.show();
                    } else {
                        getData().getBaseInfo().setXPosition((int) ((1000 * getX()) / (screenWidth - getMeasuredWidth())));
                        getData().getBaseInfo().setYPosition((int) ((1000 * getY()) / (screenHeight - getMeasuredHeight())));
                        menu.getViewManager().saveController();
                    }
                    break;
            }
        } else {
            if (menu.getTouchController() != null && getData().getEvent().isPointerFollow()) {
                menu.getTouchController().moveView(event);
            }
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    cursorMode = menu.getCursorMode();
                    setPressedStyle();
                    downX = event.getX();
                    downY = event.getY();
                    setInitialPosition();
                    positionX = getX();
                    positionY = getY();
                    downTime = System.currentTimeMillis();
                    handlePressEvent(!pressEvent);
                    handler.postDelayed(runnable, 400);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (cursorMode != menu.getCursorMode()) {
                        cursorMode = menu.getCursorMode();
                        setInitialPosition();
                    }
                    handleMoveEvent(event);
                    if ((Math.abs(event.getX() - downX) > 2 || Math.abs(event.getY() - downY) > 2) && System.currentTimeMillis() - downTime < 400) {
                        handler.removeCallbacks(runnable);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (!getData().getEvent().getPressEvent().isAutoKeep() && !(getData().getEvent().getLongPressEvent().isAutoKeep() && longPressEvent)) {
                        setNormalStyle();
                    }
                    if (Objects.equals(menu.getInput().getPointerId(), getData().getId())) {
                        menu.getInput().setPointerId(null);
                    }
                    handler.removeCallbacks(runnable);
                    handleUpAfterPressEvent();
                    if (longPress) {
                        handleUpAfterLongPressEvent();
                    }
                    if (System.currentTimeMillis() - downTime <= 100
                            && Math.abs(event.getX() - downX) <= 10
                            && Math.abs(event.getY() - downY) <= 10) {
                        handleClickEvent(!clickEvent);
                        clickCount++;
                        if (clickCount == 1) {
                            firstClickTime = System.currentTimeMillis();
                        }
                        if (clickCount == 2) {
                            if (System.currentTimeMillis() - firstClickTime < 400) {
                                handleDoubleEvent(!doubleClickEvent);
                                clickCount = 0;
                            } else {
                                clickCount = 1;
                                firstClickTime = System.currentTimeMillis();
                            }
                        }
                    }
                    break;
            }
        }
        return true;
    }

    private void setInitialPosition() {
        initialX = cursorMode == FCLBridge.CursorEnabled ? menu.getCursorX() : menu.getPointerX();
        initialY = cursorMode == FCLBridge.CursorEnabled ? menu.getCursorY() : menu.getPointerY();
    }

    private void showLine(int orientation, int pref, int self) {
        if (menu == null)
            return;

        menu.getTouchPad().drawLine(orientation, pref, self);
    }

    private void removeLine(int orientation) {
        if (menu == null)
            return;

        menu.getTouchPad().removeLine(orientation);
    }

    private void autoFitPosition() {
        if (menu == null || !menu.getMenuSetting().isAutoFit())
            return;

        ViewGroup viewGroup = (ViewGroup) getParent();

        int dist = ConvertUtils.dip2px(getContext(), menu.getMenuSetting().getAutoFitDist());
        final int autoFitDist = Math.max(dist, ConvertUtils.dip2px(getContext(), 2));

        boolean[] xyPref = {false, false};
        int[] prefXY = {0, 0};
        int[] selfXY = {0, 0};
        int[] xyDist = {autoFitDist, autoFitDist};
        int left = (int) getX();
        int right = (int) (getX() + getWidth());
        int up = (int) getY();
        int down = (int) (getY() + getHeight());
        int[] posArr = {left, right, up, down};

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i).getVisibility() == VISIBLE) {
                View button = viewGroup.getChildAt(i);
                if (button == this || (!(button instanceof ControlButton) && !(button instanceof ControlDirection))) {
                    continue;
                }
                //buttonLeft, buttonRight, buttonUp, buttonDown
                int[] buttonPosArr = {
                        (int) button.getX(),
                        (int) (button.getX() + button.getWidth()),
                        (int) button.getY(),
                        (int) (button.getY() + button.getHeight())
                };
                /*
                left - buttonLeft, left - buttonRight
                right - buttonRight, right - buttonLeft
                up - buttonUp, up - buttonDown
                down - buttonDown, down - buttonUp
                */
                int flag = -1;
                for (int j = 0; j < posArr.length; j++) {
                    flag *= -1;
                    int xyIndex = j / 2 % 2;
                    if (Math.abs(posArr[j] - buttonPosArr[j]) < xyDist[xyIndex]) {
                        xyPref[xyIndex] = true;
                        prefXY[xyIndex] = buttonPosArr[j];
                        xyDist[xyIndex] = posArr[j] - buttonPosArr[j];
                        selfXY[xyIndex] = posArr[j] - xyDist[xyIndex];
                    }
                    int buttonDist = posArr[j] - buttonPosArr[j + flag];
                    if (flag * buttonDist >= 0 && flag * buttonDist < xyDist[xyIndex]) {
                        xyPref[xyIndex] = true;
                        prefXY[xyIndex] = buttonPosArr[j + flag];
                        xyDist[xyIndex] = buttonDist - flag * dist;
                        selfXY[xyIndex] = posArr[j] - xyDist[xyIndex];
                    }
                }
            }
        }

        if (xyPref[0]) {
            setX(left - xyDist[0]);
            showLine(0, prefXY[0], selfXY[0]);
        } else {
            removeLine(0);
        }
        if (xyPref[1]) {
            setY(up - xyDist[1]);
            showLine(1, prefXY[1], selfXY[1]);
        } else {
            removeLine(1);
        }
    }

    private void cancelAllEvent() {
        handleUpAfterPressEvent();
        handleUpAfterLongPressEvent();
        cancelTickEvent(getData().getEvent().getPressEvent());
        cancelTickEvent(getData().getEvent().getLongPressEvent());
        cancelTickEvent(getData().getEvent().getClickEvent());
        cancelTickEvent(getData().getEvent().getDoubleClickEvent());
        setNormalStyle();
        pressEvent = false;
        longPress = false;
        longPressEvent = false;
        clickEvent = false;
        clickCount = 0;
        doubleClickEvent = false;
    }

    private void handleMoveEvent(MotionEvent event) {
        if (getData().getEvent().isPointerFollow()) {
            int deltaX = (int) ((event.getX() - downX) * menu.getMenuSetting().getMouseSensitivity());
            int deltaY = (int) ((event.getY() - downY) * menu.getMenuSetting().getMouseSensitivity());
            if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
                int targetX = Math.max(0, Math.min(screenWidth, initialX + deltaX));
                int targetY = Math.max(0, Math.min(screenHeight, initialY + deltaY));
                menu.getInput().setPointerId(getData().getId());
                menu.getInput().setPointer(targetX, targetY, getData().getId());
            } else {
                if (menu.getMenuSetting().isEnableGyroscope()) {
                    menu.setPointerX(initialX + deltaX);
                    menu.setPointerY(initialY + deltaY);
                } else {
                    menu.getInput().setPointerId(getData().getId());
                    menu.getInput().setPointer(initialX + deltaX, initialY + deltaY, getData().getId());
                }
            }
        }
        if (getData().getEvent().isMovable()) {
            int deltaX = (int) (event.getX() - downX);
            int deltaY = (int) (event.getY() - downY);
            float targetX = Math.max(0, Math.min(screenWidth - getWidth(), getX() + deltaX));
            float targetY = Math.max(0, Math.min(screenHeight - getHeight(), getY() + deltaY));
            setX(targetX);
            setY(targetY);
        }
    }

    private void handlePressEvent(boolean enable) {
        pressEvent = enable;
        handleTickEvent(enable, getData().getEvent().getPressEvent(), 0);
    }

    private void handleUpAfterPressEvent() {
        handleUpEvent(getData().getEvent().getPressEvent());
    }

    private void handleLongPressEvent(boolean enable) {
        longPress = true;
        longPressEvent = enable;
        handleTickEvent(enable, getData().getEvent().getLongPressEvent(), 1);
    }

    private void handleUpAfterLongPressEvent() {
        longPress = false;
        handleUpEvent(getData().getEvent().getLongPressEvent());
    }

    private void handleClickEvent(boolean enable) {
        clickEvent = enable;
        handleTickEvent(enable, getData().getEvent().getClickEvent(), 2);
    }

    private void handleDoubleEvent(boolean enable) {
        doubleClickEvent = enable;
        handleTickEvent(enable, getData().getEvent().getDoubleClickEvent(), 3);
    }

    private void handleUpEvent(ButtonEventData.Event event) {
        if (!event.isAutoKeep()) {
            if (event.isAutoClick()) {
                handleAutoClick(event, false);
            } else {
                handleKeyEvent(event, false);
            }
        }
    }

    private boolean keycodeOutputting = false;

    private void handleKeyEvent(ButtonEventData.Event event, boolean press) {
        if (!press && !keycodeOutputting) {
            return;
        }
        if (event.getOutputKeycodes().isEmpty()) {
            return;
        }
        for (int keycode : event.getOutputKeycodes()) {
            keycodeOutputting = press;
            menu.getInput().sendKeyEvent(keycode, press);
        }
    }

    private boolean autoClick = false;
    private ButtonEventData.Event autoClickEvent;
    private final Handler autoClickHandler = new Handler();
    private final Runnable autoClickRunnable = new Runnable() {
        @Override
        public void run() {
            final ButtonEventData.Event event = autoClickEvent;
            handleKeyEvent(event, true);
            handleKeyEvent(event, false);
            if (autoClick) {
                autoClickHandler.postDelayed(autoClickRunnable, 20);
            }
        }
    };

    private void handleAutoClick(ButtonEventData.Event event, boolean enable) {
        autoClick = enable;
        if (enable) {
            autoClickEvent = event;
            autoClickHandler.post(autoClickRunnable);
        }
    }

    private void cancelTickEvent(ButtonEventData.Event event) {
        if (event.isAutoKeep()) {
            if (event.isAutoClick()) {
                handleAutoClick(event, false);
            } else {
                handleKeyEvent(event, false);
            }
        }
    }

    /**
     * Handle event
     *
     * @param enable    true is start event, false is end event
     * @param event     event data
     * @param eventType 0 is press, 1 is long press, 2 is click, 3 is double click
     */
    private void handleTickEvent(boolean enable, ButtonEventData.Event event, int eventType) {
        if (event.isAutoKeep()) {
            if (event.isAutoClick()) {
                handleAutoClick(event, enable);
            } else {
                handleKeyEvent(event, enable);
            }
            if (enable) {
                setPressedStyle();
            } else {
                setNormalStyle();
            }
        } else {
            switch (eventType) {
                case 0:
                case 1:
                    if (event.isAutoClick()) {
                        handleAutoClick(event, true);
                    } else {
                        handleKeyEvent(event, true);
                    }
                    break;
                case 2:
                case 3:
                    handleKeyEvent(event, true);
                    handleKeyEvent(event, false);
                    break;
            }
        }

        if (event.isOpenMenu()) {
            ((DrawerLayout) menu.getLayout()).openDrawer(GravityCompat.START, true);
            ((DrawerLayout) menu.getLayout()).openDrawer(GravityCompat.END, true);
        }
        if (event.isSwitchTouchMode()) {
            menu.getMenuSetting().setGestureMode(menu.getMenuSetting().getGestureMode() == GestureMode.BUILD ? GestureMode.FIGHT : GestureMode.BUILD);
            Toast.makeText(getContext(), AndroidUtils.getLocalizedText(getContext(), "menu_settings_gesture_current",
                    menu.getMenuSetting().getGestureMode() == GestureMode.BUILD ?
                            getContext().getString(R.string.menu_settings_gesture_mode_build) :
                            getContext().getString(R.string.menu_settings_gesture_mode_fight)), Toast.LENGTH_SHORT).show();
        }
        if (event.isSwitchMouseMode()) {
            menu.getMenuSetting().setMouseMoveMode(menu.getMenuSetting().getMouseMoveMode() == MouseMoveMode.CLICK ? MouseMoveMode.SLIDE : MouseMoveMode.CLICK);
            Toast.makeText(getContext(), AndroidUtils.getLocalizedText(getContext(), "menu_settings_gesture_current",
                    menu.getMenuSetting().getMouseMoveMode() == MouseMoveMode.CLICK ?
                            getContext().getString(R.string.menu_settings_mouse_mode_click) :
                            getContext().getString(R.string.menu_settings_mouse_mode_slide)), Toast.LENGTH_SHORT).show();

        }
        if (event.isInput()) {
            menu.getTouchCharInput().switchKeyboardState();
        }
        if (event.isQuickInput()) {
            menu.openQuickInput();
        }
        if (StringUtils.isNotBlank(event.getOutputText())) {
            if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
                for (int i = 0; i < event.getOutputText().length(); i++) {
                    menu.getInput().sendChar(event.getOutputText().charAt(i));
                }
            } else {
                GameOption gameOption = menu.getGameOption();
                menu.getInput().sendBoundKeyEvent(gameOption, BINDING_CHAT, FCLKeycodes.KEY_T, true);
                menu.getInput().sendBoundKeyEvent(gameOption, BINDING_CHAT, FCLKeycodes.KEY_T, false);
                new Handler().postDelayed(() -> {
                    for (int i = 0; i < event.getOutputText().length(); i++) {
                        menu.getInput().sendChar(event.getOutputText().charAt(i));
                    }
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ENTER, true);
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ENTER, false);
                }, 150);
            }
        }
        for (String id : event.getBindViewGroups()) {
            if (menu.getController().viewGroups().stream().anyMatch(it -> it.getId().equals(id))) {
                ControlViewGroup viewGroup = menu.getController().viewGroups().stream().filter(it -> it.getId().equals(id)).findFirst().orElse(null);
                menu.getViewManager().switchViewGroupVisibility(viewGroup);
            }
        }
    }

    public final MutableStateFlow<Boolean> visibilityFlow() {
        if (visibilityFlow == null) {
            visibilityFlow = StateFlowKt.MutableStateFlow(false);
            // 对齐原 BooleanPropertyBase 匿名类的 invalidated()：值变化时在 UI 线程应用可见性。
            // subscribeWithCurrent 对齐原 bind() 的立即失效求值（初次派生即使值为 false 也会应用一次）；
            // 弱引用自身：订阅挂在自身持有的 flow 上，避免循环引用阻碍 GC。
            WeakReference<ControlButton> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribeWithCurrent(visibilityFlow, v -> {
                ControlButton self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        ControlButton s = ref.get();
                        if (s != null) {
                            boolean visible = s.visibilityFlow.getValue();
                            s.setVisibility(visible ? VISIBLE : GONE);
                        }
                    });
                }
            });
        }

        return visibilityFlow;
    }

    @Override
    public CustomControl.ViewType getType() {
        return CustomControl.ViewType.CONTROL_BUTTON;
    }

    @Override
    public String getViewId() {
        return getData().getId();
    }

    @Override
    public void switchParentVisibility() {
        setParentVisibility(!isParentVisibility());
    }

    @Override
    public void removeListener() {
        if (notifySubscription != null) {
            notifySubscription.cancel();
            notifySubscription = null;
        }
        if (dataChangeSubscription != null) {
            dataChangeSubscription.cancel();
            dataChangeSubscription = null;
        }
        if (dataSubscription != null) {
            dataSubscription.cancel();
            dataSubscription = null;
        }
        if (boundarySubscription != null) {
            boundarySubscription.cancel();
            boundarySubscription = null;
        }
        if (visibilitySubscription != null) {
            visibilitySubscription.cancel();
            visibilitySubscription = null;
        }
        unbindVisibility();
        if (alphaSubscription != null) {
            alphaSubscription.cancel();
            alphaSubscription = null;
        }
        notifyAction = null;
        dataChangeListener = null;
        boundaryAction = null;
        visibilityListener = null;
        alphaAction = null;
    }
}
