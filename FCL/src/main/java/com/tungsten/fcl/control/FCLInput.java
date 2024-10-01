package com.tungsten.fcl.control;

import android.view.Choreographer;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.FCLApplication;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.keycodes.AndroidKeycodeMap;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.keycodes.GamepadKeycodeMap;

import java.util.HashMap;

import fr.spse.gamepad_remapper.GamepadHandler;
import fr.spse.gamepad_remapper.RemapperManager;
import fr.spse.gamepad_remapper.RemapperView;

public class FCLInput implements View.OnCapturedPointerListener, GamepadHandler {

    public static final int MOUSE_LEFT           = 1000;
    public static final int MOUSE_MIDDLE         = 1001;
    public static final int MOUSE_RIGHT          = 1002;
    public static final int MOUSE_SCROLL_UP      = 1003;
    public static final int MOUSE_SCROLL_DOWN    = 1004;

    public static final String EXTERNAL_MOUSE_ID = "External";

    private final int screenWidth;
    private final int screenHeight;

    //for gamepad
    private int dpadLastKey = -1;
    private int currentDirection = -1;
    private long lastFrameTime;
    private Choreographer choreographer;
    private float lastXAxis;
    private float lastYAxis;
    private RemapperManager remapperManager;
    private boolean leftTriggerDown = false;
    private boolean rightTriggerDown = false;


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
        resetMapper();
    }

    public void setPointer(int x, int y, String id) {
        if (id.equals(pointerId) || id.equals("Gyro")) {
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
        view.setOnCapturedPointerListener(this);
        view.requestFocus();
        view.requestPointerCapture();

        this.focusableView = view;
    }

    private boolean handleExternalMouseEvent(MotionEvent event) {
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
        int deltaX;
        int deltaY;
        if (event != null) {
            deltaX = (int) (event.getX() * menu.getMenuSetting().getMouseSensitivity());
            deltaY = (int) (event.getY() * menu.getMenuSetting().getMouseSensitivity());
        } else {
            GameMenu gameMenu = menu;
            gameMenu.getBridge().refreshHitResultType();
            deltaX = (int) (lastXAxis * deltaTimeScale * 10 * gameMenu.getMenuSetting().getMouseSensitivity());
            deltaY = (int) (lastYAxis * deltaTimeScale * 10 * gameMenu.getMenuSetting().getMouseSensitivity());
            double hypot = Math.hypot(Math.abs(lastXAxis), Math.abs(lastYAxis));
            if (gameMenu.getHitResultType() == FCLBridge.HIT_RESULT_TYPE_ENTITY) {
                if (hypot <= menu.getMenuSetting().getGamepadAimAssistZone()) {
                    deltaX /= 10;
                    deltaY /= 10;
                }
            }
        }
        if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
            int targetX = Math.max(0, Math.min(screenWidth, menu.getCursorX() + deltaX));
            int targetY = Math.max(0, Math.min(screenHeight, menu.getCursorY() + deltaY));
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
        if (event != null){
            return handleExternalMouseEvent(event);
        }
        return false;
    }


    public boolean handleKeyEvent(KeyEvent event) {
        int fclKeycode = AndroidKeycodeMap.convertKeycode(event.getKeyCode());
        if (event.getKeyCode() == KeyEvent.KEYCODE_UNKNOWN)
            return true;
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
            return false;
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)
            return false;
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return true;
        if (event.getAction() == KeyEvent.ACTION_UP && (event.getFlags() & KeyEvent.FLAG_CANCELED) != 0)
            return true;
        if (event.getDevice() != null && ((event.getSource() & InputDevice.SOURCE_MOUSE_RELATIVE) == InputDevice.SOURCE_MOUSE_RELATIVE || (event.getSource() & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE)) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                sendKeyEvent(MOUSE_RIGHT, event.getAction() == KeyEvent.ACTION_DOWN);
                return true;
            }
        }
        if (event.getDevice() != null && ((event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)) {
            return remapperManager.handleKeyEventInput(menu.getActivity(),event,this);
        }
        if ((event.getFlags() & KeyEvent.FLAG_SOFT_KEYBOARD) == KeyEvent.FLAG_SOFT_KEYBOARD) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                return true;
            menu.getTouchCharInput().dispatchKeyEvent(event);
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && KeyEvent.metaStateHasModifiers(event.getMetaState(), KeyEvent.META_SHIFT_ON)) {
            if (!menu.getTouchCharInput().isLock() && event.getAction() == KeyEvent.ACTION_UP && !menu.getTouchCharInput().isEnabled()) {
                menu.getTouchCharInput().switchKeyboardState();
            } else if (menu.getTouchCharInput().isLock()) {
                menu.getTouchCharInput().setLock(false);
            }
            return true;
        }
        if (fclKeycode == FCLKeycodes.KEY_UNKNOWN)
            return (event.getFlags() & KeyEvent.FLAG_FALLBACK) == KeyEvent.FLAG_FALLBACK;
        sendKeyEvent(fclKeycode, event.getAction() == KeyEvent.ACTION_DOWN);
        if (event.getAction() == KeyEvent.ACTION_DOWN && menu.getCursorMode() == FCLBridge.CursorEnabled) {
            sendChar((char) (event.getUnicodeChar() != 0 ? event.getUnicodeChar() : '\u0000'));
        }
        return true;
    }

    public boolean handleGenericMotionEvent(MotionEvent event) {
        if (!menu.getTouchCharInput().isEnabled()) {
            focusableView.requestFocus();
            focusableView.requestPointerCapture();
        }
        if (event.getDevice() != null && event.getSource() == InputDevice.SOURCE_JOYSTICK) {
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
            remapperManager.handleMotionEventInput(menu.getActivity(),event,this);
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                handleDPad(event);
                handleLeftJoyStick(event);
                handleRightJoyStick(event);
            }
            return true;
        }
        return false;
    }

    private void handleDPad(MotionEvent event) {
        float xAxis = event.getAxisValue(MotionEvent.AXIS_HAT_X);
        float yAxis = event.getAxisValue(MotionEvent.AXIS_HAT_Y);
        if (Float.compare(xAxis, -1.0f) == 0) {
            dpadLastKey = KeyEvent.KEYCODE_DPAD_LEFT;
        } else if (Float.compare(xAxis, 1.0f) == 0) {
            dpadLastKey = KeyEvent.KEYCODE_DPAD_RIGHT;
        } else if (Float.compare(yAxis, -1.0f) == 0) {
            dpadLastKey = KeyEvent.KEYCODE_DPAD_UP;
        } else if (Float.compare(yAxis, 1.0f) == 0) {
            dpadLastKey = KeyEvent.KEYCODE_DPAD_DOWN;
        }
        sendKeyEvent(convertGamepadInput(dpadLastKey), (xAxis != 0 || yAxis != 0));
    }
    
    private int convertGamepadInput(int gamepadKey){
        return GamepadKeycodeMap.convert(menu.getMenuSetting().getGamepadButtonBindingProperty(),gamepadKey);
    }

    private void handleLeftJoyStick(MotionEvent event) {
        float xAxis = event.getAxisValue(MotionEvent.AXIS_X);
        float yAxis = event.getAxisValue(MotionEvent.AXIS_Y);
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
        if ((direction & 1) == 0) {
            sendKeyEvent(convertGamepadInput(GamepadKeycodeMap.LEFT_JOYSTICK_RIGHT + direction), press);
        } else {
            sendKeyEvent(convertGamepadInput(GamepadKeycodeMap.LEFT_JOYSTICK_RIGHT + direction - 1), press);
            int keyCode = GamepadKeycodeMap.LEFT_JOYSTICK_RIGHT + direction + 1;
            if (keyCode == 2008) {
                keyCode = 0;
            }
            sendKeyEvent(convertGamepadInput(keyCode), press);
        }
    }

    private void doTick() {
        long newFrameTime = System.nanoTime();
        if (lastXAxis != 0 || lastYAxis != 0) {
            newFrameTime = System.nanoTime();
            float deltaTimeScale = ((newFrameTime - lastFrameTime) / 16666666f);
            handleMouse(null, deltaTimeScale);
        }
        lastFrameTime = newFrameTime;
    }

    private void handleRightJoyStick(MotionEvent event) {
        float xAxis = event.getAxisValue(MotionEvent.AXIS_Z);
        float yAxis = event.getAxisValue(MotionEvent.AXIS_RZ);
        double dist = Math.hypot(Math.abs(xAxis), Math.abs(yAxis));
        if (dist < menu.getMenuSetting().getGamepadDeadzone()) {
            lastXAxis = 0;
            lastYAxis = 0;
            return;
        }
        if (lastXAxis != xAxis || lastYAxis != yAxis) {
            lastXAxis = xAxis;
            lastYAxis = yAxis;
            doTick();
        }
    }
public void resetMapper() {
    remapperManager = new RemapperManager(menu.getActivity(), new RemapperView.Builder(null)
            .remapA(true)
            .remapB(true)
            .remapX(true)
            .remapY(true)
            .remapLeftJoystick(true)
            .remapRightJoystick(true)
            .remapStart(true)
            .remapSelect(true)
            .remapLeftShoulder(true)
            .remapRightShoulder(true)
            .remapLeftTrigger(true)
            .remapRightTrigger(true));
}
    /**
     * Function handling all gamepad actions.
     *
     * @param code  Either a keycode (Eg. KEYBODE_BUTTON_A), either an axis (Eg. AXIS_HAT_X)
     * @param value For keycodes, 0 for released state, 1 for pressed state.
     *              For Axis, the value of the axis. Varies between 0/1 or -1/1 depending on the axis.
     */
    @Override
    public void handleGamepadInput(int code, float value) {
        boolean isKeyDown = value == 1f;
        switch (code) {
            case KeyEvent.KEYCODE_BUTTON_A:
            case KeyEvent.KEYCODE_BUTTON_B:
            case KeyEvent.KEYCODE_BUTTON_X:
            case KeyEvent.KEYCODE_BUTTON_Y:
                //Shoulders
            case KeyEvent.KEYCODE_BUTTON_L1:
            case KeyEvent.KEYCODE_BUTTON_R1:
                //Triggers
            case KeyEvent.KEYCODE_BUTTON_L2:
            case KeyEvent.KEYCODE_BUTTON_R2:
                //L3 || R3
            case KeyEvent.KEYCODE_BUTTON_THUMBL:
            case KeyEvent.KEYCODE_BUTTON_THUMBR:
                //DPAD
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                //Start/select
            case KeyEvent.KEYCODE_BUTTON_START:
            case KeyEvent.KEYCODE_BUTTON_SELECT:
                sendKeyEvent(convertGamepadInput(code), isKeyDown);
                break;
            // Triggers
            case MotionEvent.AXIS_LTRIGGER:
                if (!leftTriggerDown && value > 0.5) {
                    leftTriggerDown = true;
                    sendKeyEvent(convertGamepadInput(KeyEvent.KEYCODE_BUTTON_L2), leftTriggerDown);
                } else if (leftTriggerDown && value < 0.5) {
                    leftTriggerDown = false;
                    sendKeyEvent(convertGamepadInput(KeyEvent.KEYCODE_BUTTON_L2), leftTriggerDown);
                }
                break;
            case MotionEvent.AXIS_RTRIGGER:
                if (!rightTriggerDown && value > 0.5) {
                    rightTriggerDown = true;
                    sendKeyEvent(convertGamepadInput(KeyEvent.KEYCODE_BUTTON_R2), rightTriggerDown);
                } else if (rightTriggerDown && value < 0.5) {
                    rightTriggerDown = false;
                    sendKeyEvent(convertGamepadInput(KeyEvent.KEYCODE_BUTTON_R2), rightTriggerDown);
                }
                break;
            default:
                break;
        }

    }
}
