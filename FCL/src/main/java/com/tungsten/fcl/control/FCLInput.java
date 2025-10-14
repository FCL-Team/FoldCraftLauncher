package com.tungsten.fcl.control;

import android.view.Choreographer;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.FCLApplication;
import com.tungsten.fcl.control.gamepad.Gamepad;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.keycodes.AndroidKeycodeMap;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;

import java.util.HashMap;

public class FCLInput implements View.OnCapturedPointerListener {

    public static final int MOUSE_LEFT = 1000;
    public static final int MOUSE_MIDDLE = 1001;
    public static final int MOUSE_RIGHT = 1002;
    public static final int MOUSE_SCROLL_UP = 1003;
    public static final int MOUSE_SCROLL_DOWN = 1004;

    public static final String EXTERNAL_MOUSE_ID = "External";

    private final int screenWidth;
    private final int screenHeight;

    private Gamepad gamepad;
    private int currentDirection = -1;
    private long lastFrameTime;
    private Choreographer choreographer;
    private float lastAxisZ;
    private float lastAxisRZ;

    public static final HashMap<Integer, Integer> MOUSE_MAP = new HashMap<Integer, Integer>() {
        {
            put(MOUSE_LEFT, FCLBridge.Button1);
            put(MOUSE_MIDDLE, FCLBridge.Button3);
            put(MOUSE_RIGHT, FCLBridge.Button2);
            put(MOUSE_SCROLL_UP, FCLBridge.Button4);
            put(MOUSE_SCROLL_DOWN, FCLBridge.Button5);
        }
    };

    @NonNull
    private final GameMenu menu;

    private String pointerId;

    public void setPointerId(String pointerId) {
        if (pointerId == null) {
            this.pointerId = null;
        } else if (this.pointerId == null) {
            this.pointerId = pointerId;
        }
    }

    public String getPointerId() {
        return pointerId;
    }

    public FCLInput(@NonNull GameMenu menu) {
        this.menu = menu;

        this.screenWidth = AndroidUtils.getScreenWidth(FCLApplication.getCurrentActivity());
        this.screenHeight = AndroidUtils.getScreenHeight(FCLApplication.getCurrentActivity());
    }

    public void setPointer(int x, int y, String id) {
        if (id.equals(pointerId) || id.equals("Gyro")) {
            setPointer(x, y);
        }
    }

    public void setPointer(int x, int y) {
        if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
            menu.getCursor().setX(x);
            menu.getCursor().setY(y);
        }
        if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
            menu.setCursorX(x);
            menu.setCursorY(y);
        }
        menu.setPointerX(x);
        menu.setPointerY(y);
        if (menu.getBridge() != null) {
            menu.getBridge().pushEventPointer((int) (x * menu.getBridge().getScaleFactor()), (int) (y * menu.getBridge().getScaleFactor()));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void sendKeyEvent(int keycode, boolean press) {
        if (menu.getBridge() != null) {
            if (MOUSE_MAP.containsKey(keycode) && MOUSE_MAP.get(keycode) != null) {
                menu.getBridge().pushEventMouseButton(MOUSE_MAP.get(keycode), press);
            } else {
                menu.getBridge().pushEventKey(keycode, 0, press);
            }
        }
    }

    public void sendChar(char keyChar) {
        if (menu.getBridge() != null) {
            menu.getBridge().pushEventChar(keyChar);
        }
    }

    private View focusableView;

    public View getFocusableView() {
        return focusableView;
    }

    public void initExternalController(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnCapturedPointerListener(this);
        view.getViewTreeObserver().addOnWindowFocusChangeListener(hasFocus -> {
            if (!menu.getMenuSetting().isPhysicalMouseMode()) {
                view.requestPointerCapture();
            }
        });
        view.requestFocus();

        this.focusableView = view;
    }

    public boolean handleExternalMouseEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_BUTTON_PRESS || event.getActionMasked() == MotionEvent.ACTION_BUTTON_RELEASE) {
            boolean press = event.getActionMasked() == MotionEvent.ACTION_BUTTON_PRESS;
            if (event.getActionButton() == MotionEvent.BUTTON_PRIMARY) {
                sendKeyEvent(MOUSE_LEFT, press);
            } else if (event.getActionButton() == MotionEvent.BUTTON_SECONDARY) {
                sendKeyEvent(MOUSE_RIGHT, press);
            } else if (event.getActionButton() == MotionEvent.BUTTON_TERTIARY) {
                sendKeyEvent(MOUSE_MIDDLE, press);
            }
        } else if (event.getActionMasked() == MotionEvent.ACTION_SCROLL) {
            for (int i = 0; i < Math.abs((int) event.getAxisValue(MotionEvent.AXIS_VSCROLL)); i++) {
                sendKeyEvent(event.getAxisValue(MotionEvent.AXIS_VSCROLL) > 0 ? MOUSE_SCROLL_UP : MOUSE_SCROLL_DOWN, true);
            }
        }
        return true;
    }

    @Override
    public boolean onCapturedPointer(View view, MotionEvent event) {
        return handleMouse(event, 0);
    }

    private boolean handleMouse(MotionEvent event, float deltaTimeScale) {
        if (event == null || event.getAction() == MotionEvent.ACTION_MOVE) {
            int deltaX;
            int deltaY;
            if (event != null) {
                double tX = event.getX();
                double tY = event.getY();
                final int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    tX += event.getHistoricalX(i);
                    tY += event.getHistoricalY(i);
                }
                tX *= menu.getMenuSetting().getMouseSensitivity();
                tY *= menu.getMenuSetting().getMouseSensitivity();
                deltaX = (int) tX;
                deltaY = (int) tY;
            } else {
                deltaX = (int) (lastAxisZ * deltaTimeScale * 10 * menu.getMenuSetting().getMouseSensitivity());
                deltaY = (int) (lastAxisRZ * deltaTimeScale * 10 * menu.getMenuSetting().getMouseSensitivity());
            }
            if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
                int targetX = (int) Math.max(0, Math.min(screenWidth, menu.getCursorX() + deltaX * menu.getMenuSetting().getMouseSensitivityCursor()));
                int targetY = (int) Math.max(0, Math.min(screenHeight, menu.getCursorY() + deltaY * menu.getMenuSetting().getMouseSensitivityCursor()));
                setPointerId(EXTERNAL_MOUSE_ID);
                setPointer(targetX, targetY, EXTERNAL_MOUSE_ID);
                setPointerId(null);
            } else {
                int targetX = menu.getPointerX() + deltaX;
                int targetY = menu.getPointerY() + deltaY;
                if (menu.getMenuSetting().isEnableGyroscope()) {
                    menu.setPointerX(targetX);
                    menu.setPointerY(targetY);
                } else {
                    setPointerId(EXTERNAL_MOUSE_ID);
                    setPointer(targetX, targetY, EXTERNAL_MOUSE_ID);
                    setPointerId(null);
                }
            }
        }
        if (event != null) {
            return handleExternalMouseEvent(event);
        }
        return false;
    }


    public boolean handleKeyEvent(KeyEvent event) {
        int fclKeycode = AndroidKeycodeMap.convertKeycode(event.getKeyCode());
        if (event.getKeyCode() == KeyEvent.KEYCODE_UNKNOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return true;
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)
            return false;
        if (event.getAction() == KeyEvent.ACTION_UP && (event.getFlags() & KeyEvent.FLAG_CANCELED) != 0)
            return true;
        //mouse button right
        if (event.getDevice() != null && ((event.getSource() & InputDevice.SOURCE_MOUSE_RELATIVE) == InputDevice.SOURCE_MOUSE_RELATIVE || (event.getSource() & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE)) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                sendKeyEvent(MOUSE_RIGHT, event.getAction() == KeyEvent.ACTION_DOWN);
                return true;
            }
        }
        //soft keyboard enter
        if ((event.getFlags() & KeyEvent.FLAG_SOFT_KEYBOARD) == KeyEvent.FLAG_SOFT_KEYBOARD) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                return true;
            menu.getTouchCharInput().dispatchKeyEvent(event);
            return true;
        }
        // shift + enter switch soft keyboard state
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && KeyEvent.metaStateHasModifiers(event.getMetaState(), KeyEvent.META_SHIFT_ON)) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                menu.getTouchCharInput().switchKeyboardState();
            }
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                menu.getTouchCharInput().hide();
            }
            return true;
        }
        //gamepad
        if (Gamepad.isGamepadEvent(event)) {
            checkGamepad();
            return gamepad.handleKeyEvent(event);
        }
        //keyboard
        if (fclKeycode == FCLKeycodes.KEY_UNKNOWN)
            return (event.getFlags() & KeyEvent.FLAG_FALLBACK) == KeyEvent.FLAG_FALLBACK;
        sendKeyEvent(fclKeycode, event.getAction() == KeyEvent.ACTION_DOWN);
        if (event.getAction() == KeyEvent.ACTION_DOWN && menu.getCursorMode() == FCLBridge.CursorEnabled) {
            sendChar((char) (event.getUnicodeChar() != 0 ? event.getUnicodeChar() : '\u0000'));
        }
        return true;
    }

    public boolean handleGenericMotionEvent(MotionEvent event) {
        if (Gamepad.isGamepadEvent(event)) {
            checkGamepad();
            if (choreographer == null) {
                choreographer = Choreographer.getInstance();
                Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
                    @Override
                    public void doFrame(long frameTimeNanos) {
                        doTick();
                        choreographer.postFrameCallback(this);
                    }
                };
                choreographer.postFrameCallback(frameCallback);
            }
            return gamepad.handleMotionEventInput(event);
        } else if (event.getSource() == InputDevice.SOURCE_MOUSE && event.getActionMasked() == MotionEvent.ACTION_SCROLL) {
            for (int i = 0; i < Math.abs((int) event.getAxisValue(MotionEvent.AXIS_VSCROLL)); i++) {
                sendKeyEvent(event.getAxisValue(MotionEvent.AXIS_VSCROLL) > 0 ? MOUSE_SCROLL_UP : MOUSE_SCROLL_DOWN, true);
            }
            return true;
        }
        return false;
    }

    public void handleLeftJoyStick(float xAxis, float yAxis) {
        double dist = Math.hypot(Math.abs(xAxis), Math.abs(yAxis));
        if (dist >= menu.getMenuSetting().getGamepadDeadzone()) {
            double degrees = Math.toDegrees(-Math.atan2(yAxis, xAxis));
            if (degrees < 0) {
                degrees += 360;
            }
            int lastDirection = currentDirection;
            currentDirection = ((int) ((degrees + 22.5) / 45)) % 8;
            sendDirection(lastDirection, false);
            sendDirection(currentDirection, true);
        } else {
            sendDirection(0, false);
            sendDirection(2, false);
            sendDirection(4, false);
            sendDirection(6, false);
        }
    }

    private void sendDirection(int direction, boolean press) {
        switch (direction) {
            case 0:
                gamepad.getCurrentMap().DIRECTION_RIGHT.update(press);
                break;
            case 1:
                gamepad.getCurrentMap().DIRECTION_RIGHT.update(press);
                gamepad.getCurrentMap().DIRECTION_FORWARD.update(press);
                break;
            case 2:
                gamepad.getCurrentMap().DIRECTION_FORWARD.update(press);
                break;
            case 3:
                gamepad.getCurrentMap().DIRECTION_FORWARD.update(press);
                gamepad.getCurrentMap().DIRECTION_LEFT.update(press);
                break;
            case 4:
                gamepad.getCurrentMap().DIRECTION_LEFT.update(press);
                break;
            case 5:
                gamepad.getCurrentMap().DIRECTION_BACKWARD.update(press);
                gamepad.getCurrentMap().DIRECTION_LEFT.update(press);
                break;
            case 6:
                gamepad.getCurrentMap().DIRECTION_BACKWARD.update(press);
                break;
            case 7:
                gamepad.getCurrentMap().DIRECTION_BACKWARD.update(press);
                gamepad.getCurrentMap().DIRECTION_RIGHT.update(press);
                break;
        }
    }

    public void handleRightJoyStick(float axisZ, float axisRZ) {
        double dist = Math.hypot(Math.abs(axisZ), Math.abs(axisRZ));
        if (dist < menu.getMenuSetting().getGamepadDeadzone()) {
            lastAxisZ = 0;
            lastAxisRZ = 0;
            return;
        }
        if (lastAxisZ != axisZ || lastAxisRZ != axisRZ) {
            lastAxisZ = axisZ;
            lastAxisRZ = axisRZ;
            doTick();
        }
    }

    private void doTick() {
        long newFrameTime = System.nanoTime();
        if (lastAxisZ != 0 || lastAxisRZ != 0) {
            newFrameTime = System.nanoTime();
            float deltaTimeScale = ((newFrameTime - lastFrameTime) / 16666666f);
            handleMouse(null, deltaTimeScale);
        }
        lastFrameTime = newFrameTime;
    }

    public void resetMapper() {
        if (gamepad != null)
            gamepad.resetMapper();
    }

    public void checkGamepad() {
        if (gamepad == null) {
            gamepad = new Gamepad(menu.getActivity(), this);
        }
    }

    public Gamepad getGamepad() {
        return gamepad;
    }
}
