package com.tungsten.fcl.control;

import com.tungsten.fclauncher.FCLKeycodes;
import com.tungsten.fclauncher.bridge.FCLBridge;

import java.util.HashMap;

public class FCLInput {

    public static final int MOUSE_LEFT        = 1000;
    public static final int MOUSE_MIDDLE      = 1001;
    public static final int MOUSE_RIGHT       = 1002;
    public static final int MOUSE_SCROLL_UP   = 1003;
    public static final int MOUSE_SCROLL_DOWN = 1004;

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

    public FCLInput(MenuCallback menu) {
        this.menu = menu;
    }

    public void setPointer(int x, int y) {
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

}
