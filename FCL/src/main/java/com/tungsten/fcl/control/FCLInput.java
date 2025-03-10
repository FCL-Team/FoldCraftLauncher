package com.tungsten.fcl.control;

import android.view.Choreographer;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.FCLApplication;
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

    private long lastFrameTime;
    private Choreographer choreographer;
    private float lastXAxis;
    private float lastYAxis;


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
        if (event != null) {
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
        return false;
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

}
