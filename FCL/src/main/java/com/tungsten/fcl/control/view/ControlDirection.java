package com.tungsten.fcl.control.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.data.BaseInfoData;
import com.tungsten.fcl.control.data.ControlDirectionData;
import com.tungsten.fcl.control.data.ControlDirectionStyle;
import com.tungsten.fcl.control.data.CustomControl;
import com.tungsten.fcl.control.data.DirectionEventData;
import com.mio.util.AndroidUtilKt;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.UUID;

/**
 * Custom control direction view.
 */
@SuppressLint("ViewConstructor")
public class ControlDirection extends RelativeLayout implements CustomView {

    private InvalidationListener notifyListener;
    private InvalidationListener dataChangeListener;
    private InvalidationListener boundaryListener;
    private InvalidationListener visibilityListener;
    private InvalidationListener alphaListener;

    @Nullable
    private final GameMenu menu;
    private final boolean displayMode;
    private Path boundaryPath;
    private final Paint boundaryPaint;
    private final int screenWidth;
    private final int screenHeight;

    private static final double ANGLE_0 = 0;
    private static final double ANGLE_360 = 360;
    private static final double ANGLE_8D_OF_0P = 22.5;
    private static final double ANGLE_8D_OF_1P = 67.5;
    private static final double ANGLE_8D_OF_2P = 112.5;
    private static final double ANGLE_8D_OF_3P = 157.5;
    private static final double ANGLE_8D_OF_4P = 202.5;
    private static final double ANGLE_8D_OF_5P = 247.5;
    private static final double ANGLE_8D_OF_6P = 292.5;
    private static final double ANGLE_8D_OF_7P = 337.5;

    private BooleanProperty visibilityProperty;

    private boolean ghost = false;

    @Override
    public void setGhost(boolean ghost) {
        this.ghost = ghost;
        updateAlpha();
    }

    @Override
    public boolean isGhost() {
        return ghost;
    }

    /** alpha 合并优先级：一键隐藏 > 编辑参考组 > 全局不透明度 */
    private void updateAlpha() {
        if (menu != null) {
            setAlpha(menu.getViewManager().resolveAlpha(this));
        }
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            // 线框与手柄跟随主题色
            int color = ThemeEngine.getInstance().getTheme().getColor();
            selectedPaint.setColor(color);
            handlePaint.setColor(color);
        }
        invalidate();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

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

    private final ObjectProperty<ControlDirectionData> dataProperty = new SimpleObjectProperty<>(this, "data", new ControlDirectionData(UUID.randomUUID().toString()));

    public ObjectProperty<ControlDirectionData> dataProperty() {
        return dataProperty;
    }

    public void setData(ControlDirectionData data) {
        dataProperty.set(data);
    }

    public ControlDirectionData getData() {
        return dataProperty.get();
    }

    public ControlDirection(Context context, @Nullable GameMenu menu, boolean displayMode, ViewListener listener) {
        super(context);
        this.menu = menu;
        this.displayMode = displayMode;
        setClickable(true);

        for (AppCompatButton b : buttons) {
            b.setStateListAnimator(null);
        }

        boundaryPath = new Path();
        boundaryPaint = new Paint();
        boundaryPaint.setAntiAlias(true);
        boundaryPaint.setColor(Color.RED);
        boundaryPaint.setStyle(Paint.Style.STROKE);
        boundaryPaint.setStrokeWidth(3);
        selectedPaint.setAntiAlias(true);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(4);
        handlePaint.setAntiAlias(true);
        handlePaint.setStyle(Paint.Style.FILL);
        screenWidth = AndroidUtilKt.getScreenWidth();
        screenHeight = AndroidUtilKt.getScreenHeight();

        setWillNotDraw(false);

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
        alphaListener = invalidate -> Schedulers.androidUIThread().execute(this::updateAlpha);

        post(() -> {
            notifyData();
            if (notifyListener == null || dataChangeListener == null || boundaryListener == null || visibilityListener == null) {
                return;
            }
            if (menu != null) {
                menu.editModeProperty().addListener(notifyListener);
            }
            dataProperty.addListener(dataChangeListener);
            getData().addListener(notifyListener);
            if (menu != null) {
                menu.showViewBoundariesProperty().addListener(boundaryListener);
                updateAlpha();
                menu.hideAllViewsProperty().addListener(alphaListener);
            }
            if (listener != null) {
                listener.onReady(this);
            }
        });
    }

    public ControlDirection(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.menu = null;
        this.displayMode = true;
        setClickable(true);
        setElevation(112.0f);

        boundaryPath = new Path();
        boundaryPaint = new Paint();
        boundaryPaint.setAntiAlias(true);
        boundaryPaint.setColor(Color.RED);
        boundaryPaint.setStyle(Paint.Style.STROKE);
        boundaryPaint.setStrokeWidth(3);
        screenWidth = AndroidUtilKt.getScreenWidth();
        screenHeight = AndroidUtilKt.getScreenHeight();

        notifyListener = invalidate -> Schedulers.androidUIThread().execute(this::notifyData);
        dataChangeListener = invalidate -> Schedulers.androidUIThread().execute(() -> {
            notifyData();
            getData().addListener(notifyListener);
        });
        boundaryListener = null;
        visibilityListener = observable -> {
        };
        alphaListener = null;

        post(() -> {
            notifyData();
            dataProperty.addListener(dataChangeListener);
            getData().addListener(notifyListener);
        });
    }

    private void notifyData() {
        if (visibilityListener == null) {
            return;
        }
        ControlDirectionData data = getData();

        refreshBaseInfo(data);
        post(() -> {
            refreshStyle(data);
            boundaryPath = new Path();
            invalidate();
        });
    }

    public void setSize(int size) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = size;
        params.height = size;
        setLayoutParams(params);
    }

    public int getSize() {
        int viewSize;
        if (getData().getBaseInfo().getSizeType() == BaseInfoData.SizeType.ABSOLUTE) {
            viewSize = ConvertUtils.dip2px(getContext(), getData().getBaseInfo().getAbsoluteWidth());
        } else {
            viewSize = getData().getBaseInfo().getPercentageWidth().getReference() == BaseInfoData.PercentageSize.Reference.SCREEN_WIDTH ?
                    (int) (screenWidth * (getData().getBaseInfo().getPercentageWidth().getSize() / 1000f)) :
                    (int) (screenHeight * (getData().getBaseInfo().getPercentageWidth().getSize() / 1000f));
        }
        return viewSize;
    }

    private void refreshBaseInfo(ControlDirectionData data) {
        // Size
        setSize(getSize());

        // Position
        post(() -> {
            int x;
            int y;
            x = Math.round((screenWidth - getSize()) * (data.getBaseInfo().getXPosition() / 1000f));
            y = Math.round((screenHeight - getSize()) * (data.getBaseInfo().getYPosition() / 1000f));
            if (!displayMode) {
                setX(x);
                setY(y);
            }
        });

        // Visibility
        if (!displayMode && menu != null) {
            visibilityProperty().unbind();
            if (ghost) {
                // 编辑参考组控件始终可见
                visibilityProperty().set(true);
            } else if (menu.isEditMode()) {
                visibilityProperty().bind(Bindings.createBooleanBinding(() -> menu.getViewGroup() != null && menu.getViewGroup().getViewData().directionList().stream().anyMatch(it -> it.getId().equals(getData().getId())),
                        menu.editModeProperty(), menu.viewGroupProperty()));
            } else {
                visibilityProperty().bind(Bindings.createBooleanBinding(() -> isParentVisibility() && (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.ALWAYS ||
                                (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.IN_GAME && menu.getCursorMode() == FCLBridge.CursorDisabled) ||
                                (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.MENU && menu.getCursorMode() == FCLBridge.CursorEnabled)),
                        menu.cursorModeProperty(), parentVisibilityProperty()));
            }
            visibilityProperty().addListener(visibilityListener);
        }
    }

    private final AppCompatButton centerBtn = new AppCompatButton(getContext());
    private final AppCompatButton upBtn = new AppCompatButton(getContext());
    private final AppCompatButton downBtn = new AppCompatButton(getContext());
    private final AppCompatButton leftBtn = new AppCompatButton(getContext());
    private final AppCompatButton rightBtn = new AppCompatButton(getContext());
    private final AppCompatButton upLeftBtn = new AppCompatButton(getContext());
    private final AppCompatButton upRightBtn = new AppCompatButton(getContext());
    private final AppCompatButton downLeftBtn = new AppCompatButton(getContext());
    private final AppCompatButton downRightBtn = new AppCompatButton(getContext());

    private final AppCompatButton[] buttons = new AppCompatButton[]{
            centerBtn,
            upBtn,
            downBtn,
            leftBtn,
            rightBtn,
            upLeftBtn,
            upRightBtn,
            downLeftBtn,
            downRightBtn
    };

    private final AppCompatButton area = new AppCompatButton(getContext());
    private final AppCompatButton rocker = new AppCompatButton(getContext());

    private int rockerSize;

    private GradientDrawable drawableNormal;
    private GradientDrawable drawablePressed;
    private GradientDrawable drawableArea;
    private GradientDrawable drawableRocker;

    private void refreshStyle(ControlDirectionData data) {
        int viewSize = getSize();
        if (data.getStyle().getStyleType() == ControlDirectionStyle.Type.BUTTON) {
            drawableNormal = new GradientDrawable();
            drawableNormal.setCornerRadius(ConvertUtils.dip2px(getContext(), data.getStyle().getButtonStyle().getCornerRadius() / 10f));
            drawableNormal.setStroke(ConvertUtils.dip2px(getContext(), data.getStyle().getButtonStyle().getStrokeWidth() / 10f), data.getStyle().getButtonStyle().getStrokeColor());
            drawableNormal.setColor(data.getStyle().getButtonStyle().getFillColor());
            drawablePressed = new GradientDrawable();
            drawablePressed.setCornerRadius(ConvertUtils.dip2px(getContext(), data.getStyle().getButtonStyle().getCornerRadiusPressed() / 10f));
            drawablePressed.setStroke(ConvertUtils.dip2px(getContext(), data.getStyle().getButtonStyle().getStrokeWidthPressed() / 10f), data.getStyle().getButtonStyle().getStrokeColorPressed());
            drawablePressed.setColor(data.getStyle().getButtonStyle().getFillColorPressed());
            removeAllViews();
            for (AppCompatButton b : buttons) {
                addView(b);
                b.setClickable(false);
                b.setGravity(Gravity.CENTER);
                b.setPadding(0, 0, 0, 0);
                b.setAllCaps(false);
                b.setTextSize(data.getStyle().getButtonStyle().getTextSize());
                b.setTextColor(data.getStyle().getButtonStyle().getTextColor());
                b.setBackground(drawableNormal);
            }
            layoutButtonChildren(viewSize);
            centerBtn.setText("◆");
            upBtn.setText("▲");
            downBtn.setText("▼");
            leftBtn.setText("◀");
            rightBtn.setText("▶");
            upLeftBtn.setText("◤");
            upRightBtn.setText("◥");
            downLeftBtn.setText("◣");
            downRightBtn.setText("◢");
            upLeftBtn.setVisibility(GONE);
            upRightBtn.setVisibility(GONE);
            downLeftBtn.setVisibility(GONE);
            downRightBtn.setVisibility(GONE);
        } else {
            drawableArea = new GradientDrawable();
            drawableArea.setStroke(ConvertUtils.dip2px(getContext(), data.getStyle().getRockerStyle().getBgStrokeWidth() / 10f), data.getStyle().getRockerStyle().getBgStrokeColor());
            drawableArea.setColor(data.getStyle().getRockerStyle().getBgFillColor());
            drawableRocker = new GradientDrawable();
            drawableRocker.setStroke(ConvertUtils.dip2px(getContext(), data.getStyle().getRockerStyle().getRockerStrokeWidth() / 10f), data.getStyle().getRockerStyle().getRockerStrokeColor());
            drawableRocker.setColor(data.getStyle().getRockerStyle().getRockerFillColor());
            removeAllViews();
            addView(area, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            addView(rocker, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            area.setClickable(false);
            rocker.setClickable(false);
            area.setBackground(drawableArea);
            rocker.setBackground(drawableRocker);
            layoutRockerChildren(viewSize);
        }
    }

    /** 按指定尺寸摆放九宫格方向按钮（样式刷新与缩放实时预览共用） */
    private void layoutButtonChildren(int viewSize) {
        int size = (viewSize * (1000 - (2 * getData().getStyle().getButtonStyle().getInterval()))) / 3000;
        int p1 = size + ((viewSize * getData().getStyle().getButtonStyle().getInterval()) / 1000);
        int p2 = viewSize - size;
        for (AppCompatButton b : buttons) {
            ViewGroup.LayoutParams layoutParams = b.getLayoutParams();
            layoutParams.width = size;
            layoutParams.height = size;
            b.setLayoutParams(layoutParams);
        }
        setButtonPosition(centerBtn, p1, p1);
        setButtonPosition(upBtn, p1, 0);
        setButtonPosition(downBtn, p1, p2);
        setButtonPosition(leftBtn, 0, p1);
        setButtonPosition(rightBtn, p2, p1);
        setButtonPosition(upLeftBtn, 0, 0);
        setButtonPosition(upRightBtn, p2, 0);
        setButtonPosition(downLeftBtn, 0, p2);
        setButtonPosition(downRightBtn, p2, p2);
    }

    /** 按指定尺寸摆放摇杆背景与杆体，圆角按千分比依赖尺寸需同步更新（样式刷新与缩放实时预览共用） */
    private void layoutRockerChildren(int viewSize) {
        rockerSize = (viewSize * getData().getStyle().getRockerStyle().getRockerSize()) / 1000;
        drawableArea.setCornerRadius((float) (viewSize * getData().getStyle().getRockerStyle().getBgCornerRadius()) / 1000);
        drawableRocker.setCornerRadius((float) (rockerSize * getData().getStyle().getRockerStyle().getRockerCornerRadius()) / 1000);
        rocker.setLayoutParams(new LayoutParams(rockerSize, rockerSize));
        setButtonPosition(area, 0, 0);
        setButtonPosition(rocker, (viewSize / 2) - (rockerSize / 2), (viewSize / 2) - (rockerSize / 2));
    }

    private void setButtonPosition(AppCompatButton button, int x, int y) {
        button.setX(x);
        button.setY(y);
    }

    private void setButtonStyle(AppCompatButton button, boolean press) {
        if (press) {
            button.setTextSize(getData().getStyle().getButtonStyle().getTextSizePressed());
            button.setTextColor(getData().getStyle().getButtonStyle().getTextColorPressed());
            button.setBackground(drawablePressed);
        } else {
            button.setTextSize(getData().getStyle().getButtonStyle().getTextSize());
            button.setTextColor(getData().getStyle().getButtonStyle().getTextColor());
            button.setBackground(drawableNormal);
        }
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(() -> {
            // 缩放期间数据尚未写回，按缩放中的像素尺寸测量并同步 frame
            int target = resizingSize >= 0 ? resizingSize : getSize();
            measure(MeasureSpec.makeMeasureSpec(target, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(target, MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getLeft() + target, getTop() + target);
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (menu != null && menu.isShowViewBoundaries() && !displayMode && !ghost) {
            boundaryPath.moveTo(0, 0);
            boundaryPath.lineTo(getSize(), 0);
            boundaryPath.lineTo(getSize(), getSize());
            boundaryPath.lineTo(0, getSize());
            boundaryPath.lineTo(0, 0);
            canvas.drawPath(boundaryPath, boundaryPaint);
        }
        if (selected && menu != null && menu.isEditMode() && !displayMode) {
            // 用实际布局尺寸绘制；选中框外扩间隔，手柄画在控件内角（与命中区重合）
            float gap = ConvertUtils.dip2px(getContext(), SELECT_GAP_DP);
            float visual = ConvertUtils.dip2px(getContext(), HANDLE_VISUAL_DP);
            int size = getWidth();
            canvas.drawRect(-gap, -gap, size + gap, size + gap, selectedPaint);
            canvas.drawRect(0, 0, visual, visual, handlePaint);
            canvas.drawRect(size - visual, size - visual, size, size, handlePaint);
        }
    }

    private float downX;
    private float downY;
    private float positionX;
    private float positionY;
    private long downTime;
    private int clickCount = 0;
    private long firstClickTime;
    private boolean doubleClickEvent = false;
    private boolean startClick = false;
    private boolean startRecord = false;

    // 前进锁：lockArmed 为推杆满足锁定条件（正北且超过阈值），UP 时进入 forwardLocked 保持前进
    private boolean lockArmed = false;
    private boolean forwardLocked = false;

    // 编辑模式选中态与双角手柄缩放：手柄画在控件内角（命中区与视觉重合，仅手柄可缩放），选中框外扩间隔
    private static final float SELECT_GAP_DP = 4f;
    private static final float HANDLE_VISUAL_DP = 12f;
    private static final float HANDLE_TOUCH_DP = HANDLE_VISUAL_DP;
    private boolean selected = false;
    private boolean resizing = false;
    private boolean resizeFromTopLeft = false;
    private float resizeStartX;
    private float resizeStartY;
    private int resizeStartSize;
    // 缩放位移用屏幕坐标：拖左上手柄时 view 自身移动，view 坐标会形成正反馈抖动
    private float downRawX;
    private float downRawY;

    /** 缩放中的临时像素尺寸（requestLayout 测量优先使用），-1 表示未在缩放 */
    private int resizingSize = -1;

    private final Paint selectedPaint = new Paint();
    private final Paint handlePaint = new Paint();

    private final Handler handler = new Handler();

    private void deleteView() {
        if (menu != null) {
            menu.getViewManager().removeView(getData());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (ghost) {
            return true;
        }
        if (menu != null && menu.isEditMode()) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    positionX = getX();
                    positionY = getY();
                    downTime = System.currentTimeMillis();
                    downRawX = event.getRawX();
                    downRawY = event.getRawY();
                    // 按下点落在缩放手柄上：本次手势为缩放而非移动
                    resizeFromTopLeft = inTopLeftHandle(event.getX(), event.getY());
                    resizing = resizeFromTopLeft || inBottomRightHandle(event.getX(), event.getY());
                    if (resizing) {
                        resizeStartX = getX();
                        resizeStartY = getY();
                        resizeStartSize = getSize();
                    }
                    menu.getViewManager().hideEditBar();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (resizing) {
                        float dx = event.getRawX() - downRawX;
                        float dy = event.getRawY() - downRawY;
                        // 方向键强制等比：左上手柄取较小位移（向外拖为负，取负后尺寸增大），右下手柄取较大位移
                        int delta = (int) (resizeFromTopLeft ? -Math.min(dx, dy) : Math.max(dx, dy));
                        resizingSize = Math.max(ConvertUtils.dip2px(getContext(), 5), resizeStartSize + delta);
                        if (resizeFromTopLeft) {
                            // 右下角锚定
                            setX(resizeStartX + resizeStartSize - resizingSize);
                            setY(resizeStartY + resizeStartSize - resizingSize);
                        }
                        setSize(resizingSize);
                        // 实时重排内部控件（九宫格/摇杆按尺寸绝对定位，容器变化不会自动跟随）
                        if (getData().getStyle().getStyleType() == ControlDirectionStyle.Type.BUTTON) {
                            layoutButtonChildren(resizingSize);
                        } else {
                            layoutRockerChildren(resizingSize);
                        }
                    } else {
                        // 拖动基准与吸附解耦：期望位置每帧由手指屏幕坐标独立计算，
                        // 吸附修正不参与下一帧基准，避免"吸附→弹回"的位置抖动
                        float desiredX = positionX + (float) ((event.getRawX() - downRawX) * menu.getMenuSetting().getMouseSensitivity());
                        float desiredY = positionY + (float) ((event.getRawY() - downRawY) * menu.getMenuSetting().getMouseSensitivity());
                        float[] p = menu.getViewManager().snapPosition(this, desiredX, desiredY);
                        setX(p[0]);
                        setY(p[1]);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    menu.getTouchPad().removeLine(0);
                    menu.getTouchPad().removeLine(1);
                    if (resizing) {
                        resizing = false;
                        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                            // 缩放结束：把最终像素尺寸与位置写回数据（位置与视觉一致，避免刷新后跳变）
                            writeBackResizeSize();
                        } else {
                            // 手势中断：未写回数据，按数据恢复
                            resizingSize = -1;
                            notifyData();
                        }
                        menu.getViewManager().showEditBar(this);
                        break;
                    }
                    if (System.currentTimeMillis() - downTime <= 100
                            && Math.abs(event.getX() - downX) <= 10
                            && Math.abs(event.getY() - downY) <= 10) {
                        setX(positionX);
                        setY(positionY);
                        // 轻点切换选中：已选中（操作栏显示中）时取消选中，未选中时选中
                        if (selected) {
                            menu.getViewManager().clearSelection();
                        } else {
                            menu.getViewManager().selectView(this);
                        }
                    } else {
                        getData().getBaseInfo().setXPosition(Math.round((1000 * getX()) / (screenWidth - getSize())));
                        getData().getBaseInfo().setYPosition(Math.round((1000 * getY()) / (screenHeight - getSize())));
                        menu.getViewManager().saveController();
                    }
                    menu.getViewManager().showEditBar(this);
                    break;
            }
        } else if (menu != null && !menu.isEditMode()) {
            if (getData().getStyle().getStyleType() == ControlDirectionStyle.Type.BUTTON) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        downTime = System.currentTimeMillis();
                        int size = (getSize() * (1000 - (2 * getData().getStyle().getButtonStyle().getInterval()))) / 3000;
                        int p1 = size + ((getSize() * getData().getStyle().getButtonStyle().getInterval()) / 1000);
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        handleButtonEvent(x, y);
                        startClick = x >= p1 && x <= p1 + size && y >= p1 && y <= p1 + size;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        handleButtonEvent((int) event.getX(), (int) event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (startClick &&
                                System.currentTimeMillis() - downTime <= 100
                                && Math.abs(event.getX() - downX) <= 10
                                && Math.abs(event.getY() - downY) <= 10) {
                            startClick = false;
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
                        cancelAllEvent();
                        break;
                }
            } else {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        if (forwardLocked) {
                            // 解除前进锁：释放方向键并复位杆体，随后正常处理本次触摸
                            forwardLocked = false;
                            applyRockerLockVisual(false);
                            cancelAllEvent();
                        }
                        if (getData().getEvent().getFollowOption() == DirectionEventData.FollowOption.FOLLOW ||
                                (getData().getEvent().getFollowOption() == DirectionEventData.FollowOption.CENTER_FOLLOW
                                        && event.getX() >= (float) ((getSize() / 2) - (rockerSize / 2))
                                        && event.getX() <= (float) ((getSize() / 2) + (rockerSize / 2))
                                        && event.getY() >= (float) ((getSize() / 2) - (rockerSize / 2))
                                        && event.getY() <= (float) ((getSize() / 2) + (rockerSize / 2)))) {
                            downTime = System.currentTimeMillis();
                            startClick = true;
                            int deltaX = (int) (event.getX() - (getSize() / 2));
                            int deltaY = (int) (event.getY() - (getSize() / 2));
                            int targetX = (int) (getX() + deltaX);
                            int targetY = (int) (getY() + deltaY);
                            setX(targetX);
                            setY(targetY);
                        } else {
                            startClick = false;
                            handleRockerEvent((int) event.getX(), (int) event.getY());
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!startRecord) {
                            downX = event.getX();
                            downY = event.getY();
                            startRecord = true;
                        } else if (!startClick || Math.abs(event.getX() - downX) > 10 || Math.abs(event.getY() - downY) > 10) {
                            handleRockerEvent((int) event.getX(), (int) event.getY());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        startRecord = false;
                        if (startClick
                                && System.currentTimeMillis() - downTime <= 100
                                && Math.abs(event.getX() - downX) <= 10
                                && Math.abs(event.getY() - downY) <= 10) {
                            startClick = false;
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
                        if (event.getActionMasked() == MotionEvent.ACTION_UP && lockArmed && !forwardLocked) {
                            // 进入前进锁：保持方向键按下，杆体留在当前位置等待再次触摸解除
                            lockArmed = false;
                            forwardLocked = true;
                            applyRockerLockVisual(true);
                            break;
                        }
                        lockArmed = false;
                        forwardLocked = false;
                        cancelAllEvent();
                        break;
                }
            }
        } else {
            return true;
        }
        return true;
    }

    /** 触点是否落在左上/右下缩放手柄的命中区域（区域覆盖控件内侧角） */
    private boolean inTopLeftHandle(float x, float y) {
        if (!selected) {
            return false;
        }
        int touch = ConvertUtils.dip2px(getContext(), HANDLE_TOUCH_DP);
        return x <= touch && y <= touch;
    }

    private boolean inBottomRightHandle(float x, float y) {
        if (!selected) {
            return false;
        }
        int touch = ConvertUtils.dip2px(getContext(), HANDLE_TOUCH_DP);
        return x >= getWidth() - touch && y >= getHeight() - touch;
    }

    /** 缩放结束：把最终像素尺寸与当前位置写回宽高同值，并刷新视图 */
    private void writeBackResizeSize() {
        BaseInfoData baseInfo = getData().getBaseInfo();
        int pixel = Math.max(ConvertUtils.dip2px(getContext(), 5), getWidth());
        // 位置夹到屏内后与尺寸一起写回，保证刷新后视觉位置不变
        float x = Math.max(0, Math.min(screenWidth - pixel, getX()));
        float y = Math.max(0, Math.min(screenHeight - pixel, getY()));
        setX(x);
        setY(y);
        if (baseInfo.getSizeType() == BaseInfoData.SizeType.ABSOLUTE) {
            int dp = Math.max(5, Math.round(pixel / getResources().getDisplayMetrics().density));
            baseInfo.setAbsoluteWidth(dp);
            baseInfo.setAbsoluteHeight(dp);
        } else {
            int size = pixelToPermille(pixel, baseInfo.getPercentageWidth().getReference());
            baseInfo.getPercentageWidth().setSize(size);
            baseInfo.getPercentageHeight().setSize(size);
        }
        baseInfo.setXPosition(Math.round((1000 * x) / (screenWidth - pixel)));
        baseInfo.setYPosition(Math.round((1000 * y) / (screenHeight - pixel)));
        resizingSize = -1;
        notifyData();
        menu.getViewManager().saveController();
    }

    private int pixelToPermille(int pixel, BaseInfoData.PercentageSize.Reference reference) {
        int base = reference == BaseInfoData.PercentageSize.Reference.SCREEN_WIDTH ? screenWidth : screenHeight;
        return Math.max(1, Math.min(1000, Math.round(pixel * 1000f / base)));
    }

    private void handleButtonEvent(int x, int y) {
        int size = (getSize() * (1000 - (2 * getData().getStyle().getButtonStyle().getInterval()))) / 3000;
        int p1 = size + ((getSize() * getData().getStyle().getButtonStyle().getInterval()) / 1000);
        int p2 = getSize() - size;
        if (x <= size && y <= size) {
            // up left
            handleMoveEvent(true, false, true, false);
        } else if (x >= p1 && x <= p1 + size && y <= size) {
            // up
            handleMoveEvent(true, false, false, false);
        } else if (x >= p2 && y <= size) {
            // up right
            handleMoveEvent(true, false, false, true);
        } else if (x <= size && y >= p1 && y <= p1 + size) {
            // left
            handleMoveEvent(false, false, true, false);
        } else if (x >= p1 && x <= p1 + size && y >= p1 && y <= p1 + size) {
            // center
            handleMoveEvent(false, false, false, false);
        } else if (x >= p2 && y >= p1 && y <= p1 + size) {
            // right
            handleMoveEvent(false, false, false, true);
        } else if (x < size && y >= p2) {
            // down left
            handleMoveEvent(false, true, true, false);
        } else if (x >= p1 && x <= p1 + size && y >= p2) {
            // down
            handleMoveEvent(false, true, false, false);
        } else if (x >= p2 && y >= p2) {
            // down right
            handleMoveEvent(false, true, false, true);
        }
    }

    private void handleRockerEvent(int x, int y) {
        int maxDistance = (getSize() / 2) - (rockerSize / 2);
        Point centerPoint = new Point(getSize() / 2, getSize() / 2);
        Point touchPoint = new Point(x, y);
        // 死区：位移比例不足视为未推动，杆体回中并释放全部方向
        int deadZone = getData().getEvent().getDeadZone();
        if (deadZone > 0) {
            float lenX = touchPoint.x - centerPoint.x;
            float lenY = touchPoint.y - centerPoint.y;
            float ratio = (float) Math.sqrt(lenX * lenX + lenY * lenY) / maxDistance * 100;
            if (ratio < deadZone) {
                lockArmed = false;
                resetRockerToCenter();
                return;
            }
        }
        Point position = getRockerPositionPoint(centerPoint, touchPoint, maxDistance);
        rocker.setX(position.x - (float) (rockerSize / 2));
        rocker.setY(position.y - (float) (rockerSize / 2));
        // 前进锁武装判定：正北方向且位移达到阈值
        if (getData().getEvent().isCanLock()) {
            float lenX = touchPoint.x - centerPoint.x;
            float lenY = touchPoint.y - centerPoint.y;
            float ratio = (float) Math.sqrt(lenX * lenX + lenY * lenY) / maxDistance * 100;
            lockArmed = tempDirection == Direction.DIRECTION_UP && ratio >= getData().getEvent().getLockThreshold();
        } else {
            lockArmed = false;
        }
    }

    /** 杆体回中并释放全部方向键（死区或解除锁定时） */
    private void resetRockerToCenter() {
        if (tempDirection != Direction.DIRECTION_CENTER) {
            tempDirection = Direction.DIRECTION_CENTER;
            handleMoveEvent(false, false, false, false);
        }
        setButtonPosition(rocker, (getSize() / 2) - (rockerSize / 2), (getSize() / 2) - (rockerSize / 2));
    }

    /** 前进锁视觉提示：锁定时杆体描边变为主题色 */
    private void applyRockerLockVisual(boolean locked) {
        if (drawableRocker == null) {
            return;
        }
        if (locked) {
            drawableRocker.setStroke(ConvertUtils.dip2px(getContext(), 2), ThemeEngine.getInstance().getTheme().getColor());
        } else {
            drawableRocker.setStroke(ConvertUtils.dip2px(getContext(), getData().getStyle().getRockerStyle().getRockerStrokeWidth() / 10f), getData().getStyle().getRockerStyle().getRockerStrokeColor());
        }
    }

    private Point getRockerPositionPoint(Point centerPoint, Point touchPoint, float maxDistance) {
        float lenX = (float) (touchPoint.x - centerPoint.x);
        float lenY = (float) (touchPoint.y - centerPoint.y);
        float lenXY = (float) Math.sqrt(lenX * lenX + lenY * lenY);
        double radian = Math.acos(lenX / lenXY) * (touchPoint.y < centerPoint.y ? -1 : 1);
        double angle = ConvertUtils.radian2Angle(radian);
        if (lenXY <= maxDistance) {
            handleAngleEvent(angle);
            return touchPoint;
        } else {
            int showPointX = (int) (centerPoint.x + maxDistance * Math.cos(radian));
            int showPointY = (int) (centerPoint.y + maxDistance * Math.sin(radian));
            handleAngleEvent(angle);
            return new Point(showPointX, showPointY);
        }
    }

    public enum Direction {
        DIRECTION_LEFT,
        DIRECTION_RIGHT,
        DIRECTION_UP,
        DIRECTION_DOWN,
        DIRECTION_UP_LEFT,
        DIRECTION_UP_RIGHT,
        DIRECTION_DOWN_LEFT,
        DIRECTION_DOWN_RIGHT,
        DIRECTION_CENTER
    }

    private Direction tempDirection = Direction.DIRECTION_CENTER;

    private void handleAngleEvent(double angle) {
        if (menu != null) {
            if ((ANGLE_0 <= angle && ANGLE_8D_OF_0P > angle || ANGLE_8D_OF_7P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                // right
                tempDirection = Direction.DIRECTION_RIGHT;
                handleMoveEvent(false, false, false, true);
            } else if (ANGLE_8D_OF_0P <= angle && ANGLE_8D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN_RIGHT) {
                // down right
                tempDirection = Direction.DIRECTION_DOWN_RIGHT;
                handleMoveEvent(false, true, false, true);
            } else if (ANGLE_8D_OF_1P <= angle && ANGLE_8D_OF_2P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                // down
                tempDirection = Direction.DIRECTION_DOWN;
                handleMoveEvent(false, true, false, false);
            } else if (ANGLE_8D_OF_2P <= angle && ANGLE_8D_OF_3P > angle && tempDirection != Direction.DIRECTION_DOWN_LEFT) {
                // down left
                tempDirection = Direction.DIRECTION_DOWN_LEFT;
                handleMoveEvent(false, true, true, false);
            } else if (ANGLE_8D_OF_3P <= angle && ANGLE_8D_OF_4P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                // left
                tempDirection = Direction.DIRECTION_LEFT;
                handleMoveEvent(false, false, true, false);
            } else if (ANGLE_8D_OF_4P <= angle && ANGLE_8D_OF_5P > angle && tempDirection != Direction.DIRECTION_UP_LEFT) {
                // up left
                tempDirection = Direction.DIRECTION_UP_LEFT;
                handleMoveEvent(true, false, true, false);
            } else if (ANGLE_8D_OF_5P <= angle && ANGLE_8D_OF_6P > angle && tempDirection != Direction.DIRECTION_UP) {
                // up
                tempDirection = Direction.DIRECTION_UP;
                handleMoveEvent(true, false, false, false);
            } else if (ANGLE_8D_OF_6P <= angle && ANGLE_8D_OF_7P > angle && tempDirection != Direction.DIRECTION_UP_RIGHT) {
                // up right
                tempDirection = Direction.DIRECTION_UP_RIGHT;
                handleMoveEvent(true, false, false, true);
            }
        }
    }

    private void handleMoveEvent(boolean up, boolean down, boolean left, boolean right) {
        if (menu != null) {
            for (Integer code : getData().getEvent().upKeycodeList()) {
                menu.getInput().sendKeyEvent(code, up);
            }
            for (Integer code : getData().getEvent().downKeycodeList()) {
                menu.getInput().sendKeyEvent(code, down);
            }
            for (Integer code : getData().getEvent().leftKeycodeList()) {
                menu.getInput().sendKeyEvent(code, left);
            }
            for (Integer code : getData().getEvent().rightKeycodeList()) {
                menu.getInput().sendKeyEvent(code, right);
            }
        }
        if (getData().getStyle().getStyleType() == ControlDirectionStyle.Type.BUTTON) {
            if (up && !down && !left && !right) {
                upLeftBtn.setVisibility(VISIBLE);
                upRightBtn.setVisibility(VISIBLE);
                downLeftBtn.setVisibility(GONE);
                downRightBtn.setVisibility(GONE);
            } else if (!up && down && !left && !right) {
                upLeftBtn.setVisibility(GONE);
                upRightBtn.setVisibility(GONE);
                downLeftBtn.setVisibility(VISIBLE);
                downRightBtn.setVisibility(VISIBLE);
            } else if (!up && !down && left && !right) {
                upLeftBtn.setVisibility(VISIBLE);
                upRightBtn.setVisibility(GONE);
                downLeftBtn.setVisibility(VISIBLE);
                downRightBtn.setVisibility(GONE);
            } else if (!up && !down && !left && right) {
                upLeftBtn.setVisibility(GONE);
                upRightBtn.setVisibility(VISIBLE);
                downLeftBtn.setVisibility(GONE);
                downRightBtn.setVisibility(VISIBLE);
            } else if (!up && !down && !left) {
                upLeftBtn.setVisibility(GONE);
                upRightBtn.setVisibility(GONE);
                downLeftBtn.setVisibility(GONE);
                downRightBtn.setVisibility(GONE);
            }
            setButtonStyle(centerBtn, !up && !down && !left && !right);
            setButtonStyle(upBtn, up && !down && !left && !right);
            setButtonStyle(downBtn, !up && down && !left && !right);
            setButtonStyle(leftBtn, !up && !down && left && !right);
            setButtonStyle(rightBtn, !up && !down && !left && right);
            setButtonStyle(upLeftBtn, up && !down && left && !right);
            setButtonStyle(upRightBtn, up && !down && !left && right);
            setButtonStyle(downLeftBtn, !up && down && left && !right);
            setButtonStyle(downRightBtn, !up && down && !left && right);
        }
    }

    private void handleDoubleEvent(boolean enable) {
        if (getData().getEvent().isSneak() && menu != null) {
            doubleClickEvent = enable;
            menu.getInput().sendKeyEvent(getData().getEvent().getSneakKeycode(), enable);
        }
    }

    private void cancelAllEvent() {
        Schedulers.androidUIThread().execute(() -> {
            if (getData().getStyle().getStyleType() == ControlDirectionStyle.Type.BUTTON) {
                upLeftBtn.setVisibility(GONE);
                upRightBtn.setVisibility(GONE);
                downLeftBtn.setVisibility(GONE);
                downRightBtn.setVisibility(GONE);
                for (AppCompatButton b : buttons) {
                    setButtonStyle(b, false);
                }
            } else {
                int x;
                int y;
                x = Math.round((screenWidth - getSize()) * (getData().getBaseInfo().getXPosition() / 1000f));
                y = Math.round((screenHeight - getSize()) * (getData().getBaseInfo().getYPosition() / 1000f));
                if (!displayMode) {
                    setX(x);
                    setY(y);
                }
                setButtonPosition(area, 0, 0);
                setButtonPosition(rocker, (getSize() / 2) - (rockerSize / 2), (getSize() / 2) - (rockerSize / 2));
                tempDirection = Direction.DIRECTION_CENTER;
                lockArmed = false;
                forwardLocked = false;
                applyRockerLockVisual(false);
            }
            if (menu != null) {
                for (Integer code : getData().getEvent().upKeycodeList()) {
                    menu.getInput().sendKeyEvent(code, false);
                }
                for (Integer code : getData().getEvent().downKeycodeList()) {
                    menu.getInput().sendKeyEvent(code, false);
                }
                for (Integer code : getData().getEvent().leftKeycodeList()) {
                    menu.getInput().sendKeyEvent(code, false);
                }
                for (Integer code : getData().getEvent().rightKeycodeList()) {
                    menu.getInput().sendKeyEvent(code, false);
                }
            }
        });
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
        return CustomControl.ViewType.CONTROL_DIRECTION;
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
        if (menu != null) {
            menu.editModeProperty().removeListener(notifyListener);
            menu.showViewBoundariesProperty().removeListener(boundaryListener);
            visibilityProperty().removeListener(visibilityListener);
            menu.hideAllViewsProperty().removeListener(alphaListener);
        }
        dataProperty.removeListener(dataChangeListener);
        getData().removeListener(notifyListener);
        notifyListener = null;
        dataChangeListener = null;
        boundaryListener = null;
        visibilityListener = null;
        alphaListener = null;
    }
}