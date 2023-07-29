package com.tungsten.fcl.control;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.tungsten.fcl.FCLApplication;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.keycodes.AndroidKeycodeMap;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclauncher.bridge.FCLBridge;

import java.util.HashMap;

public class FCLInput implements View.OnCapturedPointerListener, View.OnGenericMotionListener {

    public static final int MOUSE_LEFT           = 1000;
    public static final int MOUSE_MIDDLE         = 1001;
    public static final int MOUSE_RIGHT          = 1002;
    public static final int MOUSE_SCROLL_UP      = 1003;
    public static final int MOUSE_SCROLL_DOWN    = 1004;

    public static final String EXTERNAL_MOUSE_ID = "External";

    private final int screenWidth;
    private final int screenHeight;

    public static final HashMap<Integer, Integer> MOUSE_MAP = new HashMap<Integer, Integer>() {
        {
            put(MOUSE_LEFT, FCLBridge.Button1);
            put(MOUSE_MIDDLE, FCLBridge.Button2);
            put(MOUSE_RIGHT, FCLBridge.Button3);
            put(MOUSE_SCROLL_UP, FCLBridge.Button4);
            put(MOUSE_SCROLL_DOWN, FCLBridge.Button5);
        }
    };

    private final MenuCallback menu;

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

    public FCLInput(MenuCallback menu) {
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
            if (menu instanceof GameMenu) {
                if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
                    ((GameMenu) menu).setCursorX(x);
                    ((GameMenu) menu).setCursorY(y);
                }
                ((GameMenu) menu).setPointerX(x);
                ((GameMenu) menu).setPointerY(y);
            }
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
            menu.getBridge().pushEventKey(FCLKeycodes.KEY_RESERVED, keyChar, true);
            menu.getBridge().pushEventKey(FCLKeycodes.KEY_RESERVED, keyChar, false);
        }
    }

    public void initExternalController(View view) {
        view.setFocusable(true);
        view.setOnCapturedPointerListener(this);
        view.setOnGenericMotionListener(this);
        view.requestFocus();
        view.requestPointerCapture();
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
        if (menu instanceof GameMenu) {
            int deltaX = (int) (event.getX() * ((GameMenu) menu).getMenuSetting().getMouseSensitivity());
            int deltaY = (int) (event.getY() * ((GameMenu) menu).getMenuSetting().getMouseSensitivity());
            if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
                int targetX = Math.max(0, Math.min(screenWidth, ((GameMenu) menu).getCursorX() + deltaX));
                int targetY = Math.max(0, Math.min(screenHeight, ((GameMenu) menu).getCursorY() + deltaY));
                setPointerId(EXTERNAL_MOUSE_ID);
                setPointer(targetX, targetY, EXTERNAL_MOUSE_ID);
                setPointerId(null);
            } else {
                int targetX = ((GameMenu) menu).getPointerX() + deltaX;
                int targetY = ((GameMenu) menu).getPointerY() + deltaY;
                if (((GameMenu) menu).getMenuSetting().isEnableGyroscope()) {
                    ((GameMenu) menu).setPointerX(targetX);
                    ((GameMenu) menu).setPointerY(targetY);
                } else {
                    setPointerId(EXTERNAL_MOUSE_ID);
                    setPointer(targetX, targetY, EXTERNAL_MOUSE_ID);
                    setPointerId(null);
                }
            }
            return handleExternalMouseEvent(event);
        } else {
            return false;
        }
    }

    @Override
    public boolean onGenericMotion(View v, MotionEvent event) {
        if (menu instanceof GameMenu && !((GameMenu) menu).getTouchCharInput().isEnabled()) {
            ((GameMenu) menu).getBaseLayout().requestFocus();
            ((GameMenu) menu).getBaseLayout().requestPointerCapture();
        }
        return true;
    }

    public boolean handleKeyEvent(KeyEvent event) {
        int fclKeycode = AndroidKeycodeMap.convertKeycode(event.getKeyCode());
        if (event.getKeyCode() == KeyEvent.KEYCODE_UNKNOWN)
            return true;
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
            return false;
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)
            return false;
        if (event.getRepeatCount() != 0)
            return true;
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
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
            ((GameMenu) menu).getTouchCharInput().dispatchKeyEvent(event);
            return true;
        }
        if (fclKeycode == FCLKeycodes.KEY_UNKNOWN)
            return (event.getFlags() & KeyEvent.FLAG_FALLBACK) == KeyEvent.FLAG_FALLBACK;
        sendKeyEvent(fclKeycode, event.getAction() == KeyEvent.ACTION_DOWN);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            sendChar((char) (event.getUnicodeChar() != 0 ? event.getUnicodeChar() : '\u0000'));
        }
        return true;
    }
}
