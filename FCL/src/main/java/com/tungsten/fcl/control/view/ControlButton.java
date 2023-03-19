package com.tungsten.fcl.control.view;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.fcl.FCLApplication;
import com.tungsten.fcl.R;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.GestureMode;
import com.tungsten.fcl.control.data.BaseInfoData;
import com.tungsten.fcl.control.data.ButtonEventData;
import com.tungsten.fcl.control.data.ControlButtonData;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.FCLKeycodes;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * Custom game control button.
 */
@SuppressLint("ViewConstructor")
public class ControlButton extends AppCompatButton {

    private final GameMenu menu;
    private final Path boundaryPath;
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

    public ControlButton(@NonNull Context context, GameMenu gameMenu) {
        super(context);
        this.menu = gameMenu;

        boundaryPath = new Path();
        boundaryPaint = new Paint();
        boundaryPaint.setAntiAlias(true);
        boundaryPaint.setColor(Color.RED);
        boundaryPaint.setStyle(Paint.Style.STROKE);
        boundaryPaint.setStrokeWidth(3);
        screenWidth = AndroidUtils.getScreenWidth(FCLApplication.getCurrentActivity());
        screenHeight = AndroidUtils.getScreenHeight(FCLApplication.getCurrentActivity());

        post(() -> {
            notifyData();
            menu.editModeProperty().addListener(invalidate -> cancelAllEvent());
            dataProperty.addListener(invalidate -> Schedulers.androidUIThread().execute(() -> {
                notifyData();
                cancelAllEvent();
            }));
            menu.showViewBoundariesProperty().addListener(invalidate -> invalidate());
        });
    }

    private void notifyData() {
        ControlButtonData data = getData();

        setText(data.getText());
        refreshBaseInfo(data);
        refreshStyle(data);
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
            height = data.getBaseInfo().getPercentageWidth().getReference() == BaseInfoData.PercentageSize.Reference.SCREEN_WIDTH ?
                    (int) (screenWidth * (data.getBaseInfo().getPercentageHeight().getSize() / 1000f)) :
                    (int) (screenHeight * (data.getBaseInfo().getPercentageHeight().getSize() / 1000f));
        }
        setWidth(width);
        setHeight(height);

        // Position
        int x;
        int y;
        x = (int) (screenWidth * (data.getBaseInfo().getXPosition() / 1000f)) - (width / 2);
        y = (int) (screenHeight * (data.getBaseInfo().getYPosition() / 1000f)) - (height / 2);
        setX(x);
        setY(y);

        // Visibility
        visibilityProperty().unbind();
        visibilityProperty().bind(Bindings.createBooleanBinding(() -> isParentVisibility() && (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.ALWAYS ||
                (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.IN_GAME && menu.getCursorMode() == FCLBridge.CursorDisabled) ||
                (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.MENU && menu.getCursorMode() == FCLBridge.CursorEnabled)),
                menu.cursorModeProperty(), parentVisibilityProperty()));
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
    private final Runnable deleteRunnable = () -> {

    };

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
                    handler.postDelayed(deleteRunnable, 400);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int deltaX = (int) ((event.getX() - downX) * menu.getMenuSetting().getMouseSensitivity());
                    int deltaY = (int) ((event.getY() - downY) * menu.getMenuSetting().getMouseSensitivity());
                    float targetX = Math.max(0, Math.min(screenWidth - getWidth(), positionX + deltaX));
                    float targetY = Math.max(0, Math.min(screenHeight - getHeight(), positionY + deltaY));
                    setX(targetX);
                    setY(targetY);
                    if ((Math.abs(event.getX() - downX) > 1 || Math.abs(event.getY() - downY) > 1) && System.currentTimeMillis() - downTime < 400) {
                        handler.removeCallbacks(deleteRunnable);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    setNormalStyle();
                    handler.removeCallbacks(deleteRunnable);
                    if (System.currentTimeMillis() - downTime <= 100
                            && Math.abs(event.getX() - downX) <= 10
                            && Math.abs(event.getY() - downY) <= 10) {
                        
                    }
                    break;
            }
        } else {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    setPressedStyle();
                    downX = event.getX();
                    downY = event.getY();
                    initialX = menu.getCursorX();
                    initialY = menu.getCursorY();
                    positionX = getX();
                    positionY = getY();
                    downTime = System.currentTimeMillis();
                    handlePressEvent(!pressEvent);
                    handler.postDelayed(runnable, 400);
                    break;
                case MotionEvent.ACTION_MOVE:
                    handleMoveEvent(event);
                    if ((Math.abs(event.getX() - downX) > 1 || Math.abs(event.getY() - downY) > 1) && System.currentTimeMillis() - downTime < 400) {
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

    private void cancelAllEvent() {
        handleUpAfterPressEvent();
        handleUpAfterLongPressEvent();
        handleTickEvent(false, getData().getEvent().getPressEvent(), 0);
        handleTickEvent(false, getData().getEvent().getLongPressEvent(), 1);
        handleTickEvent(false, getData().getEvent().getClickEvent(), 2);
        handleTickEvent(false, getData().getEvent().getDoubleClickEvent(), 3);
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
            int deltaX = (int) ((event.getX() - downX) * menu.getMenuSetting().getMouseSensitivity());
            int deltaY = (int) ((event.getY() - downY) * menu.getMenuSetting().getMouseSensitivity());
            float targetX = Math.max(0, Math.min(screenWidth - getWidth(), positionX + deltaX));
            float targetY = Math.max(0, Math.min(screenHeight - getHeight(), positionY + deltaY));
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

    private void handleKeyEvent(ButtonEventData.Event event, boolean press) {
        for (int keycode : event.outputKeycodesList()) {
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

    /**
     * Handle event
     * @param enable true is start event, false is end event
     * @param event event data
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
                case 3:
                case 4:
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
                @SuppressWarnings("OptionalGetWithoutIsPresent") ControlViewGroup viewGroup = menu.getController().viewGroups().stream().filter(it -> it.getId().equals(id)).findFirst().get();
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
}
