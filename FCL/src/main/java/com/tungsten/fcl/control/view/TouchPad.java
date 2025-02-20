package com.tungsten.fcl.control.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.FCLApplication;
import com.tungsten.fcl.control.FCLInput;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.GestureMode;
import com.tungsten.fcl.control.MouseMoveMode;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.bridge.FCLBridge;

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
        this.screenWidth = AndroidUtils.getScreenWidth(FCLApplication.getCurrentActivity());
        this.screenHeight = AndroidUtils.getScreenHeight(FCLApplication.getCurrentActivity());
        init();
    }

    private void init() {
        path = new Path();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.GREEN);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    private int downX;
    private int downY;
    private long downTime;
    private int initialX;
    private int initialY;
    private boolean cancelMouseLeft = false;
    private boolean cancelMouseRight = false;
    private int currentPointerID;
    private int lastPointerCount;
    private boolean shouldBeDown = false;
    private final Handler handler = new Handler();

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
        if (gameMenu.getMenuSetting().isDisableBEGesture()) {
            return gameMenu.getMenuSetting().getGestureMode();
        } else {
            if (gameMenu.getBridge() != null) {
                if (gameMenu.getHitResultType() == FCLBridge.HIT_RESULT_TYPE_UNKNOWN) {
                    return gameMenu.getMenuSetting().getGestureMode();
                } else if (gameMenu.getHitResultType() == FCLBridge.HIT_RESULT_TYPE_BLOCK) {
                    return GestureMode.BUILD;
                } else {
                    return GestureMode.FIGHT;
                }
            } else {
                return gameMenu.getMenuSetting().getGestureMode();
            }
        }
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
        if (gameMenu.getTouchController() != null) {
            gameMenu.getTouchController().handleTouchEvent(event);
        }
        if (gameMenu.getCursorMode() == FCLBridge.CursorEnabled) {
            if (gameMenu.getMenuSetting().getMouseMoveMode() == MouseMoveMode.CLICK) {
                gameMenu.getInput().setPointerId(POINTER_ID);
                gameMenu.getInput().setPointer((int) event.getX(), (int) event.getY(), POINTER_ID);
                gameMenu.getInput().setPointerId(null);
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        Choreographer.getInstance().postFrameCallbackDelayed(frameTimeNanos -> {
                            gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, true);
                        }, 33);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        Choreographer.getInstance().postFrameCallbackDelayed(frameTimeNanos -> {
                            gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, false);
                        }, 33);
                        break;
                    default:
                        break;
                }
            } else {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = (int) event.getX();
                        downY = (int) event.getY();
                        downTime = System.currentTimeMillis();
                        initialX = gameMenu.getCursorX();
                        initialY = gameMenu.getCursorY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int deltaX = (int) ((event.getX() - downX) * gameMenu.getMenuSetting().getMouseSensitivity());
                        int deltaY = (int) ((event.getY() - downY) * gameMenu.getMenuSetting().getMouseSensitivity());
                        int targetX = Math.max(0, Math.min(screenWidth, initialX + deltaX));
                        int targetY = Math.max(0, Math.min(screenHeight, initialY + deltaY));
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
            initialX = gameMenu.getPointerX();
            initialY = gameMenu.getPointerY();
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    currentPointerID = event.getPointerId(0);
                    if (gameMenu.getBridge() != null) {
                        gameMenu.getBridge().refreshHitResultType();
                    }
                    downX = (int) event.getX();
                    downY = (int) event.getY();
                    downTime = System.currentTimeMillis();
                    handler.postDelayed(runnable, 400);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int pointerCount = event.getPointerCount();
                    int pointerIndex = event.findPointerIndex(currentPointerID);
                    if (pointerIndex == -1 || lastPointerCount != pointerCount || !shouldBeDown) {
                        shouldBeDown = true;
                        currentPointerID = event.getPointerId(0);
                        downX = (int) event.getX();
                        downY = (int) event.getY();
                        break;
                    }
                    int newDownX = (int) event.getX(pointerIndex);
                    int newDownY = (int) event.getY(pointerIndex);
                    int deltaX = (int) ((newDownX - downX) * gameMenu.getMenuSetting().getMouseSensitivity() / gameMenu.getBridge().getScaleFactor());
                    int deltaY = (int) ((newDownY - downY) * gameMenu.getMenuSetting().getMouseSensitivity() / gameMenu.getBridge().getScaleFactor());
                    if (gameMenu.getMenuSetting().isEnableGyroscope()) {
                        gameMenu.setPointerX(initialX + deltaX);
                        gameMenu.setPointerY(initialY + deltaY);
                    } else {
                        gameMenu.getInput().setPointerId(POINTER_ID);
                        gameMenu.getInput().setPointer(initialX + deltaX, initialY + deltaY, POINTER_ID);
                    }
                    if ((Math.abs(deltaX) > 1 || Math.abs(deltaY) > 1) && System.currentTimeMillis() - downTime < 400) {
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
}
