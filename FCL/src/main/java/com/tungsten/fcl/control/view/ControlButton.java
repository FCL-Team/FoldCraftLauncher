package com.tungsten.fcl.control.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.fcl.FCLApplication;
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
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * Custom game control button.
 */
@SuppressLint("ViewConstructor")
public class ControlButton extends AppCompatButton implements CustomView {

    private InvalidationListener notifyListener;
    private InvalidationListener dataChangeListener;
    private InvalidationListener boundaryListener;
    private InvalidationListener visibilityListener;
    private InvalidationListener alphaListener;

    private final GameMenu menu;
    private Path boundaryPath;
    private final Paint boundaryPaint;
    private final int screenWidth;
    private final int screenHeight;

    private BooleanProperty visibilityProperty;

    private final BooleanProperty parentVisibilityProperty = new SimpleBooleanProperty(this, "parentVisibility", true);

    public BooleanProperty parentVisibilityProperty() {
        return parentVisibilityProperty;
    }

    public void setParentVisibility(boolean parentVisibility) {
        parentVisibilityProperty.set(parentVisibility);
    }

    public boolean isParentVisibility() {
        return parentVisibilityProperty.get();
    }

    private final ObjectProperty<ControlButtonData> dataProperty = new SimpleObjectProperty<>(this, "data", new ControlButtonData(UUID.randomUUID().toString()));

    public ObjectProperty<ControlButtonData> dataProperty() {
        return dataProperty;
    }

    public void setData(ControlButtonData data) {
        dataProperty.set(data);
    }

    public ControlButtonData getData() {
        return dataProperty.get();
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
        screenWidth = AndroidUtils.getScreenWidth(FCLApplication.getCurrentActivity());
        screenHeight = AndroidUtils.getScreenHeight(FCLApplication.getCurrentActivity());

        notifyListener = invalidate -> Schedulers.androidUIThread().execute(() -> {
            notifyData();
            cancelAllEvent();
        });
        dataChangeListener = invalidate -> Schedulers.androidUIThread().execute(() -> {
            notifyData();
            cancelAllEvent();
            getData().addListener(notifyListener);
        });
        boundaryListener = invalidate -> Schedulers.androidUIThread().execute(() -> {
            boundaryPath = new Path();
            invalidate();
        });
        visibilityListener = invalidate -> Schedulers.androidUIThread().execute(() -> {
            if (!visibilityProperty.get()) {
                cancelAllEvent();
            }
        });
        alphaListener = invalidate -> Schedulers.androidUIThread().execute(() -> {
            setAlpha(menu.isHideAllViews() ? 0 : 1);
            if (!menu.getMenuSetting().isHideMenuView()) {
                ((DrawerLayout) gameMenu.getLayout()).setDrawerLockMode(menu.isHideAllViews() ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        });

        post(() -> {
            notifyData();
            if (notifyListener == null || dataChangeListener == null || boundaryListener == null || visibilityListener == null) {
                return;
            }
            menu.editModeProperty().addListener(notifyListener);
            dataProperty.addListener(dataChangeListener);
            getData().addListener(notifyListener);
            menu.showViewBoundariesProperty().addListener(boundaryListener);
            setAlpha(menu.isHideAllViews() ? 0 : 1);
            menu.hideAllViewsProperty().addListener(alphaListener);
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
        visibilityProperty().unbind();
        if (menu.isEditMode()) {
            visibilityProperty().bind(Bindings.createBooleanBinding(() -> menu.getViewGroup() != null && (menu.getViewGroup().getViewData().buttonList().stream().anyMatch(it -> it.getId().equals(getData().getId()))),
                    menu.editModeProperty(), menu.viewGroupProperty()));
        } else {
            visibilityProperty().bind(Bindings.createBooleanBinding(() -> isParentVisibility() && (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.ALWAYS ||
                            (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.IN_GAME && menu.getCursorMode() == FCLBridge.CursorDisabled) ||
                            (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.MENU && menu.getCursorMode() == FCLBridge.CursorEnabled)),
                    menu.cursorModeProperty(), parentVisibilityProperty()));
        }
        visibilityProperty().addListener(visibilityListener);
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
                        EditViewDialog dialog = new EditViewDialog(getContext(), getData().clone(), menu, new EditViewDialog.Callback() {
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
                        }, true);
                        dialog.show();
                    } else {
                        getData().getBaseInfo().setXPosition((int) ((1000 * getX()) / (screenWidth - getMeasuredWidth())));
                        getData().getBaseInfo().setYPosition((int) ((1000 * getY()) / (screenHeight - getMeasuredHeight())));
                        menu.getViewManager().saveController();
                    }
                    break;
            }
        } else {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    setPressedStyle();
                    downX = event.getX();
                    downY = event.getY();
                    initialX = menu.getCursorMode() == FCLBridge.CursorEnabled ? menu.getCursorX() : menu.getPointerX();
                    initialY = menu.getCursorMode() == FCLBridge.CursorEnabled ? menu.getCursorY() : menu.getPointerY();
                    positionX = getX();
                    positionY = getY();
                    downTime = System.currentTimeMillis();
                    handlePressEvent(!pressEvent);
                    handler.postDelayed(runnable, 400);
                    break;
                case MotionEvent.ACTION_MOVE:
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
        for (int keycode : event.outputKeycodesList()) {
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
                menu.getInput().sendKeyEvent(FCLKeycodes.KEY_T, true);
                menu.getInput().sendKeyEvent(FCLKeycodes.KEY_T, false);
                new Handler().postDelayed(() -> {
                    for (int i = 0; i < event.getOutputText().length(); i++) {
                        menu.getInput().sendChar(event.getOutputText().charAt(i));
                    }
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ENTER, true);
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ENTER, false);
                }, 50);
            }
        }
        for (String id : event.bindViewGroupList()) {
            if (menu.getController().viewGroups().stream().anyMatch(it -> it.getId().equals(id))) {
                ControlViewGroup viewGroup = menu.getController().viewGroups().stream().filter(it -> it.getId().equals(id)).findFirst().orElse(null);
                menu.getViewManager().switchViewGroupVisibility(viewGroup);
            }
        }
    }

    public final BooleanProperty visibilityProperty() {
        if (visibilityProperty == null) {
            visibilityProperty = new BooleanPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        boolean visible = get();
                        setVisibility(visible ? VISIBLE : GONE);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "visibility";
                }
            };
        }

        return visibilityProperty;
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
        menu.editModeProperty().removeListener(notifyListener);
        dataProperty.removeListener(dataChangeListener);
        getData().removeListener(notifyListener);
        menu.showViewBoundariesProperty().removeListener(boundaryListener);
        visibilityProperty().removeListener(visibilityListener);
        menu.hideAllViewsProperty().removeListener(alphaListener);
        notifyListener = null;
        dataChangeListener = null;
        boundaryListener = null;
        visibilityListener = null;
        alphaListener = null;
    }
}
