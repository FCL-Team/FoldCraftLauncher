package com.tungsten.fcl.onlytest.codes;

import static com.tungsten.fcl.onlytest.definitions.map.KeyMap.*;

import android.view.KeyEvent;


import com.tungsten.fcl.onlytest.definitions.map.KeyMap;

import java.util.HashMap;

public class AndroidKeyMap implements KeyMap, CoKeyMap {
    private final HashMap<Integer, String> androidKeyMap;

    public AndroidKeyMap() {
        androidKeyMap = new HashMap<>();
        init();
    }

    private void init() {
        androidKeyMap.put(KeyEvent.KEYCODE_0, KEYMAP_KEY_0);
        androidKeyMap.put(KeyEvent.KEYCODE_1, KEYMAP_KEY_1);
        androidKeyMap.put(KeyEvent.KEYCODE_2, KEYMAP_KEY_2);
        androidKeyMap.put(KeyEvent.KEYCODE_3, KEYMAP_KEY_3);
        androidKeyMap.put(KeyEvent.KEYCODE_4, KEYMAP_KEY_4);
        androidKeyMap.put(KeyEvent.KEYCODE_5, KEYMAP_KEY_5);
        androidKeyMap.put(KeyEvent.KEYCODE_6, KEYMAP_KEY_6);
        androidKeyMap.put(KeyEvent.KEYCODE_7, KEYMAP_KEY_7);
        androidKeyMap.put(KeyEvent.KEYCODE_8, KEYMAP_KEY_8);
        androidKeyMap.put(KeyEvent.KEYCODE_9, KEYMAP_KEY_9);
        androidKeyMap.put(KeyEvent.KEYCODE_A, KEYMAP_KEY_A);
        androidKeyMap.put(KeyEvent.KEYCODE_B, KEYMAP_KEY_B);
        androidKeyMap.put(KeyEvent.KEYCODE_C, KEYMAP_KEY_C);
        androidKeyMap.put(KeyEvent.KEYCODE_D, KEYMAP_KEY_D);
        androidKeyMap.put(KeyEvent.KEYCODE_E, KEYMAP_KEY_E);
        androidKeyMap.put(KeyEvent.KEYCODE_F, KEYMAP_KEY_F);
        androidKeyMap.put(KeyEvent.KEYCODE_G, KEYMAP_KEY_G);
        androidKeyMap.put(KeyEvent.KEYCODE_H, KEYMAP_KEY_H);
        androidKeyMap.put(KeyEvent.KEYCODE_I, KEYMAP_KEY_I);
        androidKeyMap.put(KeyEvent.KEYCODE_J, KEYMAP_KEY_J);
        androidKeyMap.put(KeyEvent.KEYCODE_K, KEYMAP_KEY_K);
        androidKeyMap.put(KeyEvent.KEYCODE_L, KEYMAP_KEY_L);
        androidKeyMap.put(KeyEvent.KEYCODE_M, KEYMAP_KEY_M);
        androidKeyMap.put(KeyEvent.KEYCODE_N, KEYMAP_KEY_N);
        androidKeyMap.put(KeyEvent.KEYCODE_O, KEYMAP_KEY_O);
        androidKeyMap.put(KeyEvent.KEYCODE_P, KEYMAP_KEY_P);
        androidKeyMap.put(KeyEvent.KEYCODE_Q, KEYMAP_KEY_Q);
        androidKeyMap.put(KeyEvent.KEYCODE_R, KEYMAP_KEY_R);
        androidKeyMap.put(KeyEvent.KEYCODE_S, KEYMAP_KEY_S);
        androidKeyMap.put(KeyEvent.KEYCODE_T, KEYMAP_KEY_T);
        androidKeyMap.put(KeyEvent.KEYCODE_U, KEYMAP_KEY_U);
        androidKeyMap.put(KeyEvent.KEYCODE_V, KEYMAP_KEY_V);
        androidKeyMap.put(KeyEvent.KEYCODE_W, KEYMAP_KEY_W);
        androidKeyMap.put(KeyEvent.KEYCODE_X, KEYMAP_KEY_X);
        androidKeyMap.put(KeyEvent.KEYCODE_Y, KEYMAP_KEY_Y);
        androidKeyMap.put(KeyEvent.KEYCODE_Z, KEYMAP_KEY_Z);
        androidKeyMap.put(KeyEvent.KEYCODE_MINUS, KEYMAP_KEY_MINUS);
        androidKeyMap.put(KeyEvent.KEYCODE_EQUALS, KEYMAP_KEY_EQUALS);
        androidKeyMap.put(KeyEvent.KEYCODE_LEFT_BRACKET, KEYMAP_KEY_LBRACKET);
        androidKeyMap.put(KeyEvent.KEYCODE_RIGHT_BRACKET, KEYMAP_KEY_RBRACKET);
        androidKeyMap.put(KeyEvent.KEYCODE_SEMICOLON, KEYMAP_KEY_SEMICOLON);
        androidKeyMap.put(KeyEvent.KEYCODE_APOSTROPHE, KEYMAP_KEY_APOSTROPHE);
        androidKeyMap.put(KeyEvent.KEYCODE_GRAVE, KEYMAP_KEY_GRAVE);
        androidKeyMap.put(KeyEvent.KEYCODE_BACKSLASH, KEYMAP_KEY_BACKSLASH);
        androidKeyMap.put(KeyEvent.KEYCODE_COMMA, KEYMAP_KEY_COMMA);
        androidKeyMap.put(KeyEvent.KEYCODE_PERIOD, KEYMAP_KEY_PERIOD);
        androidKeyMap.put(KeyEvent.KEYCODE_SLASH, KEYMAP_KEY_SLASH);
        androidKeyMap.put(KeyEvent.KEYCODE_ESCAPE, KEYMAP_KEY_ESC);
        androidKeyMap.put(KeyEvent.KEYCODE_F1, KEYMAP_KEY_F1);
        androidKeyMap.put(KeyEvent.KEYCODE_F2, KEYMAP_KEY_F2);
        androidKeyMap.put(KeyEvent.KEYCODE_F3, KEYMAP_KEY_F3);
        androidKeyMap.put(KeyEvent.KEYCODE_F4, KEYMAP_KEY_F4);
        androidKeyMap.put(KeyEvent.KEYCODE_F5, KEYMAP_KEY_F5);
        androidKeyMap.put(KeyEvent.KEYCODE_F6, KEYMAP_KEY_F6);
        androidKeyMap.put(KeyEvent.KEYCODE_F7, KEYMAP_KEY_F7);
        androidKeyMap.put(KeyEvent.KEYCODE_F8, KEYMAP_KEY_F8);
        androidKeyMap.put(KeyEvent.KEYCODE_F9, KEYMAP_KEY_F9);
        androidKeyMap.put(KeyEvent.KEYCODE_F10, KEYMAP_KEY_F10);
        androidKeyMap.put(KeyEvent.KEYCODE_F11, KEYMAP_KEY_F11);
        androidKeyMap.put(KeyEvent.KEYCODE_F12, KEYMAP_KEY_F12);
        androidKeyMap.put(KeyEvent.KEYCODE_TAB, KEYMAP_KEY_TAB);
        androidKeyMap.put(KeyEvent.KEYCODE_BACK, KEYMAP_KEY_BACKSPACE);
        androidKeyMap.put(KeyEvent.KEYCODE_SPACE, KEYMAP_KEY_SPACE);
        androidKeyMap.put(KeyEvent.KEYCODE_CAPS_LOCK, KEYMAP_KEY_CAPITAL);
        androidKeyMap.put(KeyEvent.KEYCODE_ENTER, KEYMAP_KEY_ENTER);
        androidKeyMap.put(KeyEvent.KEYCODE_SHIFT_LEFT, KEYMAP_KEY_LSHIFT);
        androidKeyMap.put(KeyEvent.KEYCODE_CTRL_LEFT, KEYMAP_KEY_LCTRL);
        androidKeyMap.put(KeyEvent.KEYCODE_ALT_LEFT, KEYMAP_KEY_LALT);
        androidKeyMap.put(KeyEvent.KEYCODE_SHIFT_RIGHT, KEYMAP_KEY_RSHIFT);
        androidKeyMap.put(KeyEvent.KEYCODE_CTRL_RIGHT, KEYMAP_KEY_RCTRL);
        androidKeyMap.put(KeyEvent.KEYCODE_ALT_RIGHT, KEYMAP_KEY_RALT);
        androidKeyMap.put(KeyEvent.KEYCODE_DPAD_UP, KEYMAP_KEY_UP);
        androidKeyMap.put(KeyEvent.KEYCODE_DPAD_DOWN, KEYMAP_KEY_DOWN);
        androidKeyMap.put(KeyEvent.KEYCODE_DPAD_LEFT, KEYMAP_KEY_LEFT);
        androidKeyMap.put(KeyEvent.KEYCODE_DPAD_RIGHT, KEYMAP_KEY_RIGHT);
        androidKeyMap.put(KeyEvent.KEYCODE_PAGE_UP, KEYMAP_KEY_PAGEUP);
        androidKeyMap.put(KeyEvent.KEYCODE_PAGE_DOWN, KEYMAP_KEY_PAGEDOWN);
        androidKeyMap.put(KeyEvent.KEYCODE_HOME, KEYMAP_KEY_HOME);
        androidKeyMap.put(KeyEvent.KEYCODE_MOVE_END, KEYMAP_KEY_END);
        androidKeyMap.put(KeyEvent.KEYCODE_INSERT, KEYMAP_KEY_INSERT);
        androidKeyMap.put(KeyEvent.KEYCODE_DEL, KEYMAP_KEY_DELETE);
        androidKeyMap.put(KeyEvent.KEYCODE_BREAK, KEYMAP_KEY_PAUSE);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_0, KEYMAP_KEY_NUMPAD0);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_1, KEYMAP_KEY_NUMPAD1);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_2, KEYMAP_KEY_NUMPAD2);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_3, KEYMAP_KEY_NUMPAD3);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_4, KEYMAP_KEY_NUMPAD4);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_5, KEYMAP_KEY_NUMPAD5);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_6, KEYMAP_KEY_NUMPAD6);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_7, KEYMAP_KEY_NUMPAD7);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_8, KEYMAP_KEY_NUMPAD8);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_9, KEYMAP_KEY_NUMPAD9);
        androidKeyMap.put(KeyEvent.KEYCODE_NUM_LOCK, KEYMAP_KEY_NUMLOCK);
        androidKeyMap.put(KeyEvent.KEYCODE_SCROLL_LOCK, KEYMAP_KEY_SCROLL);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_SUBTRACT, KEYMAP_KEY_SUBTRACT);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_ADD, KEYMAP_KEY_ADD);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_DOT, KEYMAP_KEY_DECIMAL);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_ENTER, KEYMAP_KEY_NUMPADENTER);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_DIVIDE, KEYMAP_KEY_DIVIDE);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_MULTIPLY, KEYMAP_KEY_MULTIPLY);
        androidKeyMap.put(KeyEvent.KEYCODE_SYSRQ, KEYMAP_KEY_PRINT);
        /* missing RWIN in /android/view/KeyEvent.java */
        /* missing LWIN in /android/view/KeyEvent.java */
        /* missing RightK in GLFW.java */

        //手柄
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_1, KEYMAP_BUTTON_1);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_2, KEYMAP_BUTTON_2);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_3, KEYMAP_BUTTON_3);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_4, KEYMAP_BUTTON_4);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_5, KEYMAP_BUTTON_5);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_6, KEYMAP_BUTTON_6);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_7, KEYMAP_BUTTON_7);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_8, KEYMAP_BUTTON_8);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_9, KEYMAP_BUTTON_9);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_10, KEYMAP_BUTTON_10);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_11, KEYMAP_BUTTON_11);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_12, KEYMAP_BUTTON_12);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_13, KEYMAP_BUTTON_13);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_14, KEYMAP_BUTTON_14);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_15, KEYMAP_BUTTON_15);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_16, KEYMAP_BUTTON_16);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_A, KEYMAP_BUTTON_A);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_B, KEYMAP_BUTTON_B);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_C, KEYMAP_BUTTON_C);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_X, KEYMAP_BUTTON_X);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_Y, KEYMAP_BUTTON_Y);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_Z, KEYMAP_BUTTON_Z);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_L1, KEYMAP_BUTTON_L1);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_L2, KEYMAP_BUTTON_L2);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_R1, KEYMAP_BUTTON_R1);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_R2, KEYMAP_BUTTON_R2);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_MODE, KEYMAP_BUTTON_MODE);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_SELECT, KEYMAP_BUTTON_SELECT);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_START, KEYMAP_BUTTON_START);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_THUMBL, KEYMAP_BUTTON_THUMBL);
        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_THUMBR, KEYMAP_BUTTON_THUMBR);

    }

    @Override
    public Object translate(Object keyCode) {
        if (androidKeyMap.containsKey(keyCode)) {
            return androidKeyMap.get(keyCode);
        } else {
            return null;
        }
    }
}
