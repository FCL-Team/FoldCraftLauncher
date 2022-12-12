package com.tungsten.fcl.onlytest.codes;

import static com.tungsten.fcl.onlytest.codes.BoatMousecodes.*;
import static com.tungsten.fcl.onlytest.definitions.map.MouseMap.*;
import static com.tungsten.fclauncher.FCLKeycodes.*;

import com.tungsten.fcl.onlytest.definitions.map.KeyMap;

import java.util.HashMap;


public class XKeyMap implements KeyMap, CoKeyMap {

    private final HashMap<String, Integer> xKeyMap;

    public XKeyMap() {
        xKeyMap = new HashMap<>();
        init();
    }

    private void init() {
        xKeyMap.put(KEYMAP_KEY_0, KEY_0);
        xKeyMap.put(KEYMAP_KEY_1, KEY_1);
        xKeyMap.put(KEYMAP_KEY_2, KEY_2);
        xKeyMap.put(KEYMAP_KEY_3, KEY_3);
        xKeyMap.put(KEYMAP_KEY_4, KEY_4);
        xKeyMap.put(KEYMAP_KEY_5, KEY_5);
        xKeyMap.put(KEYMAP_KEY_6, KEY_6);
        xKeyMap.put(KEYMAP_KEY_7, KEY_7);
        xKeyMap.put(KEYMAP_KEY_8, KEY_8);
        xKeyMap.put(KEYMAP_KEY_9, KEY_9);
        xKeyMap.put(KEYMAP_KEY_A, KEY_A);
        xKeyMap.put(KEYMAP_KEY_B, KEY_B);
        xKeyMap.put(KEYMAP_KEY_C, KEY_C);
        xKeyMap.put(KEYMAP_KEY_D, KEY_D);
        xKeyMap.put(KEYMAP_KEY_E, KEY_E);
        xKeyMap.put(KEYMAP_KEY_F, KEY_F);
        xKeyMap.put(KEYMAP_KEY_G, KEY_G);
        xKeyMap.put(KEYMAP_KEY_H, KEY_H);
        xKeyMap.put(KEYMAP_KEY_I, KEY_I);
        xKeyMap.put(KEYMAP_KEY_J, KEY_J);
        xKeyMap.put(KEYMAP_KEY_K, KEY_K);
        xKeyMap.put(KEYMAP_KEY_L, KEY_L);
        xKeyMap.put(KEYMAP_KEY_M, KEY_M);
        xKeyMap.put(KEYMAP_KEY_N, KEY_N);
        xKeyMap.put(KEYMAP_KEY_O, KEY_O);
        xKeyMap.put(KEYMAP_KEY_P, KEY_P);
        xKeyMap.put(KEYMAP_KEY_Q, KEY_Q);
        xKeyMap.put(KEYMAP_KEY_R, KEY_R);
        xKeyMap.put(KEYMAP_KEY_S, KEY_S);
        xKeyMap.put(KEYMAP_KEY_T, KEY_T);
        xKeyMap.put(KEYMAP_KEY_U, KEY_U);
        xKeyMap.put(KEYMAP_KEY_V, KEY_V);
        xKeyMap.put(KEYMAP_KEY_W, KEY_W);
        xKeyMap.put(KEYMAP_KEY_X, KEY_X);
        xKeyMap.put(KEYMAP_KEY_Y, KEY_Y);
        xKeyMap.put(KEYMAP_KEY_Z, KEY_Z);
        xKeyMap.put(KEYMAP_KEY_MINUS, KEY_MINUS);
        xKeyMap.put(KEYMAP_KEY_EQUALS, KEY_EQUAL);
        xKeyMap.put(KEYMAP_KEY_LBRACKET, KEY_LEFTBRACE);
        xKeyMap.put(KEYMAP_KEY_RBRACKET, KEY_RIGHTBRACE);
        xKeyMap.put(KEYMAP_KEY_SEMICOLON, KEY_SEMICOLON);
        xKeyMap.put(KEYMAP_KEY_APOSTROPHE, KEY_APOSTROPHE);
        xKeyMap.put(KEYMAP_KEY_GRAVE, KEY_GRAVE);
        xKeyMap.put(KEYMAP_KEY_BACKSLASH, KEY_BACKSLASH);
        xKeyMap.put(KEYMAP_KEY_COMMA, KEY_COMMA);
        xKeyMap.put(KEYMAP_KEY_PERIOD, KEY_DOT);
        xKeyMap.put(KEYMAP_KEY_SLASH, KEY_SLASH);
        xKeyMap.put(KEYMAP_KEY_ESC, 1);
        xKeyMap.put(KEYMAP_KEY_F1, KEY_F1);
        xKeyMap.put(KEYMAP_KEY_F2, KEY_F2);
        xKeyMap.put(KEYMAP_KEY_F3, KEY_F3);
        xKeyMap.put(KEYMAP_KEY_F4, KEY_F4);
        xKeyMap.put(KEYMAP_KEY_F5, KEY_F5);
        xKeyMap.put(KEYMAP_KEY_F6, KEY_F6);
        xKeyMap.put(KEYMAP_KEY_F7, KEY_F7);
        xKeyMap.put(KEYMAP_KEY_F8, KEY_F8);
        xKeyMap.put(KEYMAP_KEY_F9, KEY_F9);
        xKeyMap.put(KEYMAP_KEY_F10, KEY_F10);
        xKeyMap.put(KEYMAP_KEY_F11, KEY_F11);
        xKeyMap.put(KEYMAP_KEY_F12, KEY_F12);
        xKeyMap.put(KEYMAP_KEY_TAB, KEY_TAB);
        xKeyMap.put(KEYMAP_KEY_BACKSPACE, KEY_BACKSPACE);
        xKeyMap.put(KEYMAP_KEY_SPACE, KEY_SPACE);
        xKeyMap.put(KEYMAP_KEY_CAPITAL, KEY_CAPSLOCK);
        xKeyMap.put(KEYMAP_KEY_ENTER, KEY_ENTER);
        xKeyMap.put(KEYMAP_KEY_LSHIFT, KEY_LEFTSHIFT);
        xKeyMap.put(KEYMAP_KEY_LCTRL, KEY_LEFTCTRL);
        xKeyMap.put(KEYMAP_KEY_LALT, KEY_LEFTALT);
        xKeyMap.put(KEYMAP_KEY_RSHIFT, KEY_RIGHTSHIFT);
        xKeyMap.put(KEYMAP_KEY_RCTRL, KEY_RIGHTCTRL);
        xKeyMap.put(KEYMAP_KEY_RALT, KEY_RIGHTALT);
        xKeyMap.put(KEYMAP_KEY_UP, KEY_UP);
        xKeyMap.put(KEYMAP_KEY_DOWN, KEY_DOWN);
        xKeyMap.put(KEYMAP_KEY_LEFT, KEY_LEFT);
        xKeyMap.put(KEYMAP_KEY_RIGHT, KEY_RIGHT);
        xKeyMap.put(KEYMAP_KEY_PAGEUP, KEY_PAGEUP);
        xKeyMap.put(KEYMAP_KEY_PAGEDOWN, KEY_PAGEDOWN);
        xKeyMap.put(KEYMAP_KEY_HOME, KEY_HOME);
        xKeyMap.put(KEYMAP_KEY_END, KEY_END);
        xKeyMap.put(KEYMAP_KEY_INSERT, KEY_INSERT);
        xKeyMap.put(KEYMAP_KEY_DELETE, KEY_DELETE);
        xKeyMap.put(KEYMAP_KEY_PAUSE, KEY_PAUSE);
        xKeyMap.put(KEYMAP_KEY_NUMPAD0, KEY_KP0);
        xKeyMap.put(KEYMAP_KEY_NUMPAD1, KEY_KP1);
        xKeyMap.put(KEYMAP_KEY_NUMPAD2, KEY_KP2);
        xKeyMap.put(KEYMAP_KEY_NUMPAD3, KEY_KP3);
        xKeyMap.put(KEYMAP_KEY_NUMPAD4, KEY_KP4);
        xKeyMap.put(KEYMAP_KEY_NUMPAD5, KEY_KP5);
        xKeyMap.put(KEYMAP_KEY_NUMPAD6, KEY_KP6);
        xKeyMap.put(KEYMAP_KEY_NUMPAD7, KEY_KP7);
        xKeyMap.put(KEYMAP_KEY_NUMPAD8, KEY_KP8);
        xKeyMap.put(KEYMAP_KEY_NUMPAD9, KEY_KP9);
        xKeyMap.put(KEYMAP_KEY_NUMLOCK, KEY_NUMLOCK);
        xKeyMap.put(KEYMAP_KEY_SCROLL, KEY_SCROLLLOCK);
        xKeyMap.put(KEYMAP_KEY_SUBTRACT, KEY_KPMINUS);
        xKeyMap.put(KEYMAP_KEY_ADD, KEY_KPPLUS);
        xKeyMap.put(KEYMAP_KEY_DECIMAL, KEY_KPDOT);
        xKeyMap.put(KEYMAP_KEY_NUMPADENTER, KEY_KPENTER);
        xKeyMap.put(KEYMAP_KEY_DIVIDE, KEY_KPSLASH);
        xKeyMap.put(KEYMAP_KEY_MULTIPLY, KEY_KPASTERISK);
        xKeyMap.put(KEYMAP_KEY_PRINT, KEY_P);
//        xKeyMap.put(KEYMAP_KEY_LWIN, KEY_SUPER_L);
//        xKeyMap.put(KEYMAP_KEY_RWIN, KEY_SUPER_R);
        /* missing RightK in BoatKeycodes.java */

        /* Mouse buttons codes */
        xKeyMap.put(MOUSEMAP_BUTTON_LEFT, BOAT_MOUSE_BUTTON_left);
        xKeyMap.put(MOUSEMAP_BUTTON_RIGHT, BOAT_MOUSE_BUTTON_right);
        xKeyMap.put(MOUSEMAP_BUTTON_MIDDLE, BOAT_MOUSE_BUTTON_middle);
        xKeyMap.put(MOUSEMAP_WHEEL_UP, BOAT_MOUSE_WHEEL_up);
        xKeyMap.put(MOUSEMAP_WHEEL_DOWN, BOAT_MOUSE_WHEEL_down);
    }

    @Override
    public Object translate(Object keyCode) {
        if (xKeyMap.containsKey(keyCode)) {
            return xKeyMap.get(keyCode);
        } else {
            return -1;
        }
    }
}
