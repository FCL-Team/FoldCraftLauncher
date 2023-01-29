package com.tungsten.fcl.control.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.tungsten.fcl.control.FCLInput;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.GestureMode;
import com.tungsten.fcl.control.MouseMoveMode;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.bridge.FCLBridge;

public class TouchPad extends RelativeLayout {

    private final int screenWidth;
    private final int screenHeight;

    private GameMenu gameMenu;

    public void init(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
    }

    public TouchPad(Context context) {
        super(context);
        this.screenWidth = AndroidUtils.getScreenWidth(getContext());
        this.screenHeight = AndroidUtils.getScreenHeight(getContext());
    }

    public TouchPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.screenWidth = AndroidUtils.getScreenWidth(getContext());
        this.screenHeight = AndroidUtils.getScreenHeight(getContext());
    }

    public TouchPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.screenWidth = AndroidUtils.getScreenWidth(getContext());
        this.screenHeight = AndroidUtils.getScreenHeight(getContext());
    }

    private int downX;
    private int downY;
    private long downTime;
    private int initialX;
    private int initialY;
    private boolean cancelMouseLeft = false;
    private boolean cancelMouseRight = false;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameMenu.getCursorMode() == FCLBridge.CursorEnabled) {
            if (gameMenu.getMenuSetting().getMouseMoveMode() == MouseMoveMode.CLICK) {
                gameMenu.getInput().setPointer((int) event.getX(), (int) event.getY());
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, true);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, false);
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
                        gameMenu.getInput().setPointer(targetX, targetY);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - downTime <= 100
                                && Math.abs(event.getX() - downX) <= 10
                                && Math.abs(event.getY() - downY) <= 10) {
                            gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, true);
                            gameMenu.getInput().sendKeyEvent(FCLInput.MOUSE_LEFT, false);
                        }
                        break;
                }
            }
        } else {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    if (gameMenu.getBridge() != null) {
                        gameMenu.getBridge().refreshHitResultType();
                    }
                    downX = (int) event.getX();
                    downY = (int) event.getY();
                    downTime = System.currentTimeMillis();
                    initialX = gameMenu.getPointerX();
                    initialY = gameMenu.getPointerY();
                    handler.postDelayed(runnable, 400);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int deltaX = (int) ((event.getX() - downX) * gameMenu.getMenuSetting().getMouseSensitivity());
                    int deltaY = (int) ((event.getY() - downY) * gameMenu.getMenuSetting().getMouseSensitivity());
                    if (gameMenu.getMenuSetting().isEnableGyroscope()) {
                        gameMenu.setPointerX(initialX + deltaX);
                        gameMenu.setPointerY(initialY + deltaY);
                    } else {
                        gameMenu.getInput().setPointer(initialX + deltaX, initialY + deltaY);
                    }
                    if ((Math.abs(deltaX) > 1 || Math.abs(deltaY) > 1) && System.currentTimeMillis() - downTime < 400) {
                        handler.removeCallbacks(runnable);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
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
            }
        }
        return true;
    }
}
