package com.tungsten.fcl.control.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.control.FCLInput;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.GestureMode;
import com.tungsten.fcl.control.MouseMoveMode;
import com.mio.util.AndroidUtilKt;
import com.tungsten.fcl.setting.MenuSetting;
import com.tungsten.fclauncher.bridge.FCLBridge;

import org.lwjgl.glfw.CallbackBridge;

import java.util.Objects;

public class TouchPad extends View {

    private final int screenWidth;
    private final int screenHeight;

    private GameMenu gameMenu;


    public void init(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
    }

    public TouchPad(Context context) {
        this(context, null);
    }

    public TouchPad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.screenWidth = AndroidUtilKt.getScreenWidth();
        this.screenHeight = AndroidUtilKt.getScreenHeight();
        init();
    }

    private void init() {
        path = new Path();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.GREEN);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    // 触摸坐标全程 float：中途取整会让慢移时的小步长增量整体丢失，转视角顿挫
    private float downX;
    private float downY;
    private long downTime;
    private float initialX;
    private float initialY;
    private boolean cancelMouseLeft = false;
    private boolean cancelMouseRight = false;
    private int currentPointerID;
    private int lastPointerCount;
    private boolean shouldBeDown = false;
    private final Handler handler = new Handler();
    // 触控加速：本次按住的累计滑动距离与上一 MOVE 事件时间，用于距离加速与滑动速度计算
    private float acceleratedDistance;
    private long lastMoveTime;

    private final Runnable runnable = () -> {
        if (!gameMenu.getMenuSetting().isDisableGesture()) {
            if (getGestureMode() == GestureMode.BUILD) {
                gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, true);
                cancelMouseLeft = true;
                cancelMouseRight = false;
            } else {
                gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_RIGHT, true);
                cancelMouseRight = true;
                cancelMouseLeft = false;
            }
        }
    };

    private GestureMode getGestureMode() {
        return gameMenu.getMenuSetting().getGestureMode();
    }

    private Path path;
    private final Paint linePaint = new Paint();
    private int prefX;
    private int prefY;
    private int selfX;
    private int selfY;

    private boolean showLineHorizontal = false;
    private boolean showLineVertical = false;

    public void drawLine(int orientation, int pref, int self) {
        if (orientation == 0) {
            showLineHorizontal = true;
            prefX = pref;
            selfX = self;
        } else {
            showLineVertical = true;
            prefY = pref;
            selfY = self;
        }

        init();
        invalidate();
    }

    public void removeLine(int orientation) {
        if (orientation == 0)
            showLineHorizontal = false;
        else
            showLineVertical = false;

        init();
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (showLineHorizontal) {
            path.moveTo(prefX, 0);
            path.lineTo(prefX, getHeight());
            path.moveTo(selfX, 0);
            path.lineTo(selfX, getHeight());
            canvas.drawPath(path, linePaint);
        }
        if (showLineVertical) {
            path.moveTo(0, prefY);
            path.lineTo(getWidth(), prefY);
            path.moveTo(0, selfY);
            path.lineTo(getWidth(), selfY);
            canvas.drawPath(path, linePaint);
        }
    }

    private final static String POINTER_ID = "TouchPad";

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 编辑模式下触摸落到触摸板说明未命中任何控件：取消选中
        if (gameMenu.isEditMode() && event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            gameMenu.getViewManager().clearSelection();
        }
        if (gameMenu.getTouchController() != null) {
            gameMenu.getTouchController().handleTouchEvent(event);
        }
        if (gameMenu.getCursorMode() == FCLBridge.CursorEnabled) {
            if (event.isFromSource(InputDevice.SOURCE_MOUSE)) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    gameMenu.getInput().setPointer(event.getRawX(), event.getRawY());
                }
                // 指针捕获未生效时外接鼠标点击以普通 touch 事件到达，这里补齐按键投递
                gameMenu.getInput().handleExternalTouchButtons(event);
                //防止被外接鼠标触发
                return true;
            }
            if (gameMenu.getMenuSetting().getMouseMoveMode() == MouseMoveMode.CLICK) {
                gameMenu.getInput().setPointerId(POINTER_ID);
                gameMenu.getInput().setPointer(event.getX(), event.getY(), POINTER_ID);
                gameMenu.getInput().setPointerId(null);
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        Choreographer.getInstance().postFrameCallbackDelayed(frameTimeNanos -> gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, true), 33);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        Choreographer.getInstance().postFrameCallbackDelayed(frameTimeNanos -> gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, false), 33);
                        break;
                    default:
                        break;
                }
            } else {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        downTime = System.currentTimeMillis();
                        initialX = gameMenu.getCursorX();
                        initialY = gameMenu.getCursorY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float deltaX = (event.getX() - downX) * (float) gameMenu.getMenuSetting().getMouseSensitivityCursor();
                        float deltaY = (event.getY() - downY) * (float) gameMenu.getMenuSetting().getMouseSensitivityCursor();
                        float targetX = Math.max(0, Math.min(screenWidth, initialX + deltaX));
                        float targetY = Math.max(0, Math.min(screenHeight, initialY + deltaY));
                        gameMenu.getInput().setPointerId(POINTER_ID);
                        gameMenu.getInput().setPointer(targetX, targetY, POINTER_ID);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - downTime <= 100
                                && Math.abs(event.getX() - downX) <= 10
                                && Math.abs(event.getY() - downY) <= 10) {
                            gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, true);
                            gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, false);
                        }
                        if (Objects.equals(gameMenu.getInput().getPointerId(), POINTER_ID)) {
                            gameMenu.getInput().setPointerId(null);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            if (event.isFromSource(InputDevice.SOURCE_MOUSE)) {
                // 游戏捕获视角期间外接鼠标的点击同样以 touch 事件到达，转发按键
                gameMenu.getInput().handleExternalTouchButtons(event);
                return true;
            }
            if (gameMenu.getMenuSetting().isDisableLeftTouch() && event.getX() <= (float) screenWidth / 2) {
                return true;
            }
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    currentPointerID = event.getPointerId(0);
                    downX = event.getX();
                    downY = event.getY();
                    downTime = System.currentTimeMillis();
                    acceleratedDistance = 0;
                    lastMoveTime = event.getEventTime();
                    handler.postDelayed(runnable, 400);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int pointerCount = event.getPointerCount();
                    int pointerIndex = event.findPointerIndex(currentPointerID);
                    if (pointerIndex == -1 || lastPointerCount != pointerCount || !shouldBeDown) {
                        shouldBeDown = true;
                        currentPointerID = event.getPointerId(0);
                        downX = event.getX();
                        downY = event.getY();
                        acceleratedDistance = 0;
                        lastMoveTime = event.getEventTime();
                        break;
                    }
                    float newDownX = event.getX(pointerIndex);
                    float newDownY = event.getY(pointerIndex);
                    float frameDX = newDownX - downX;
                    float frameDY = newDownY - downY;
                    float acceleration = viewAcceleration(event, frameDX, frameDY);
                    // 捕获态统一走相对增量流，与陀螺仪等来源的增量叠加互不干扰
                    // 触摸按历史标定 1:1 下发，不乘 scaleFactor：旧实现的预先除法与 setPointer 内的乘法恰好抵消，保持手感
                    double sensitivity = gameMenu.getMenuSetting().getMouseSensitivity();
                    float gameDX = (float) (frameDX * sensitivity * acceleration);
                    float gameDY = (float) (frameDY * sensitivity * acceleration);
                    CallbackBridge.sendCursorDelta(gameDX, gameDY);
                    if ((Math.abs(gameDX) > 1 || Math.abs(gameDY) > 1) && System.currentTimeMillis() - downTime < 400) {
                        handler.removeCallbacks(runnable);
                    }
                    downX = newDownX;
                    downY = newDownY;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (Objects.equals(gameMenu.getInput().getPointerId(), POINTER_ID)) {
                        gameMenu.getInput().setPointerId(null);
                    }
                    shouldBeDown = false;
                    currentPointerID = -1;
                    handler.removeCallbacks(runnable);
                    if (cancelMouseLeft) {
                        gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, false);
                    }
                    if (cancelMouseRight) {
                        gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_RIGHT, false);
                    }
                    cancelMouseLeft = false;
                    cancelMouseRight = false;
                    if (System.currentTimeMillis() - downTime <= 100
                            && Math.abs(event.getX() - downX) <= 10
                            && Math.abs(event.getY() - downY) <= 10) {
                        if (!gameMenu.getMenuSetting().isDisableGesture()) {
                            if (getGestureMode() == GestureMode.BUILD) {
                                gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_RIGHT, true);
                                gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_RIGHT, false);
                            } else {
                                gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, true);
                                gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, false);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
            lastPointerCount = event.getPointerCount();
        }
        return true;
    }

    /**
     * 转视角的触控加速系数。
     * 滑动加速：滑动速度超过慢拖上限后线性放大，快速甩动时视角转动更远；
     * 距离加速：系数随本次按住的累计滑动距离增长，长距离连续拖动逐渐加快。
     * 两项加速均在松手或重新按下时归零。
     */
    private float viewAcceleration(MotionEvent event, float frameDX, float frameDY) {
        MenuSetting setting = gameMenu.getMenuSetting();
        if (!setting.isSlideAcceleration() && !setting.isDistanceAcceleration()) {
            return 1f;
        }
        float movement = Math.abs(frameDX) + Math.abs(frameDY);
        float speedMult = 1f;
        float distMult = 1f;
        if (setting.isSlideAcceleration()) {
            float dt = Math.max(1, event.getEventTime() - lastMoveTime);
            // 速度单位 px/ms，慢拖约低于 1；线性放大，约 7px/ms 快甩时达到 3 倍封顶
            float speed = movement / dt;
            speedMult = Math.min(1f + Math.max(0, speed - 1f) * 0.3f, 3f);
        }
        if (setting.isDistanceAcceleration()) {
            acceleratedDistance += movement;
            // 累计滑动每 1000px 增加 1 倍，2 倍封顶
            distMult = 1f + Math.min(acceleratedDistance, 1000f) / 1000f;
        }
        lastMoveTime = event.getEventTime();
        return speedMult * distMult;
    }
}
