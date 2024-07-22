package com.tungsten.fclauncher.keycodes;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mio
 */
public class LwjglKeycodeMap {
    private static final Map<Integer, Integer> KEY_MAP = new HashMap<>();

    private static void add(int lwjglKeycode, int fclKeycode) {
        KEY_MAP.put(fclKeycode, lwjglKeycode);
    }

    public static int convertKeycode(int fclKeycode) {
        Integer key = KEY_MAP.get(fclKeycode);
        if (key != null) {
            return key;
        }
        return LwjglGlfwKeycode.KEY_UNKNOWN;
    }

    static {
        add(LwjglGlfwKeycode.KEY_HOME, FCLKeycodes.KEY_HOME);
        add(LwjglGlfwKeycode.KEY_ESCAPE, FCLKeycodes.KEY_ESC);
        add(LwjglGlfwKeycode.KEY_0, FCLKeycodes.KEY_0);
        add(LwjglGlfwKeycode.KEY_1, FCLKeycodes.KEY_1);
        add(LwjglGlfwKeycode.KEY_2, FCLKeycodes.KEY_2);
        add(LwjglGlfwKeycode.KEY_3, FCLKeycodes.KEY_3);
        add(LwjglGlfwKeycode.KEY_4, FCLKeycodes.KEY_4);
        add(LwjglGlfwKeycode.KEY_5, FCLKeycodes.KEY_5);
        add(LwjglGlfwKeycode.KEY_6, FCLKeycodes.KEY_6);
        add(LwjglGlfwKeycode.KEY_7, FCLKeycodes.KEY_7);
        add(LwjglGlfwKeycode.KEY_8, FCLKeycodes.KEY_8);
        add(LwjglGlfwKeycode.KEY_9, FCLKeycodes.KEY_9);
        add(LwjglGlfwKeycode.KEY_3, FCLKeycodes.KEY_3);
        add(LwjglGlfwKeycode.KEY_UP, FCLKeycodes.KEY_UP);
        add(LwjglGlfwKeycode.KEY_DOWN, FCLKeycodes.KEY_DOWN);
        add(LwjglGlfwKeycode.KEY_LEFT, FCLKeycodes.KEY_LEFT);
        add(LwjglGlfwKeycode.KEY_RIGHT, FCLKeycodes.KEY_RIGHT);
        add(LwjglGlfwKeycode.KEY_A, FCLKeycodes.KEY_A);
        add(LwjglGlfwKeycode.KEY_B, FCLKeycodes.KEY_B);
        add(LwjglGlfwKeycode.KEY_C, FCLKeycodes.KEY_C);
        add(LwjglGlfwKeycode.KEY_D, FCLKeycodes.KEY_D);
        add(LwjglGlfwKeycode.KEY_E, FCLKeycodes.KEY_E);
        add(LwjglGlfwKeycode.KEY_F, FCLKeycodes.KEY_F);
        add(LwjglGlfwKeycode.KEY_G, FCLKeycodes.KEY_G);
        add(LwjglGlfwKeycode.KEY_H, FCLKeycodes.KEY_H);
        add(LwjglGlfwKeycode.KEY_I, FCLKeycodes.KEY_I);
        add(LwjglGlfwKeycode.KEY_J, FCLKeycodes.KEY_J);
        add(LwjglGlfwKeycode.KEY_K, FCLKeycodes.KEY_K);
        add(LwjglGlfwKeycode.KEY_L, FCLKeycodes.KEY_L);
        add(LwjglGlfwKeycode.KEY_M, FCLKeycodes.KEY_M);
        add(LwjglGlfwKeycode.KEY_N, FCLKeycodes.KEY_N);
        add(LwjglGlfwKeycode.KEY_O, FCLKeycodes.KEY_O);
        add(LwjglGlfwKeycode.KEY_P, FCLKeycodes.KEY_P);
        add(LwjglGlfwKeycode.KEY_Q, FCLKeycodes.KEY_Q);
        add(LwjglGlfwKeycode.KEY_R, FCLKeycodes.KEY_R);
        add(LwjglGlfwKeycode.KEY_S, FCLKeycodes.KEY_S);
        add(LwjglGlfwKeycode.KEY_T, FCLKeycodes.KEY_T);
        add(LwjglGlfwKeycode.KEY_U, FCLKeycodes.KEY_U);
        add(LwjglGlfwKeycode.KEY_V, FCLKeycodes.KEY_V);
        add(LwjglGlfwKeycode.KEY_W, FCLKeycodes.KEY_W);
        add(LwjglGlfwKeycode.KEY_X, FCLKeycodes.KEY_X);
        add(LwjglGlfwKeycode.KEY_Y, FCLKeycodes.KEY_Y);
        add(LwjglGlfwKeycode.KEY_Z, FCLKeycodes.KEY_Z);
        add(LwjglGlfwKeycode.KEY_COMMA, FCLKeycodes.KEY_COMMA);
        add(LwjglGlfwKeycode.KEY_PERIOD, FCLKeycodes.KEY_DOT);
        add(LwjglGlfwKeycode.KEY_LEFT_ALT, FCLKeycodes.KEY_LEFTALT);
        add(LwjglGlfwKeycode.KEY_RIGHT_ALT, FCLKeycodes.KEY_RIGHTALT);
        add(LwjglGlfwKeycode.KEY_LEFT_SHIFT, FCLKeycodes.KEY_LEFTSHIFT);
        add(LwjglGlfwKeycode.KEY_RIGHT_SHIFT, FCLKeycodes.KEY_RIGHTSHIFT);
        add(LwjglGlfwKeycode.KEY_TAB, FCLKeycodes.KEY_TAB);
        add(LwjglGlfwKeycode.KEY_SPACE, FCLKeycodes.KEY_SPACE);
        add(LwjglGlfwKeycode.KEY_ENTER, FCLKeycodes.KEY_ENTER);
        add(LwjglGlfwKeycode.KEY_BACKSPACE, FCLKeycodes.KEY_BACKSPACE);
        add(LwjglGlfwKeycode.KEY_GRAVE_ACCENT, FCLKeycodes.KEY_GRAVE);
        add(LwjglGlfwKeycode.KEY_MINUS, FCLKeycodes.KEY_MINUS);
        add(LwjglGlfwKeycode.KEY_EQUAL, FCLKeycodes.KEY_EQUAL);
        add(LwjglGlfwKeycode.KEY_LEFT_BRACKET, FCLKeycodes.KEY_LEFTBRACE);
        add(LwjglGlfwKeycode.KEY_RIGHT_BRACKET, FCLKeycodes.KEY_RIGHTBRACE);
        add(LwjglGlfwKeycode.KEY_BACKSLASH, FCLKeycodes.KEY_BACKSLASH);
        add(LwjglGlfwKeycode.KEY_SEMICOLON, FCLKeycodes.KEY_SEMICOLON);
        add(LwjglGlfwKeycode.KEY_APOSTROPHE, FCLKeycodes.KEY_APOSTROPHE);
        add(LwjglGlfwKeycode.KEY_SLASH, FCLKeycodes.KEY_SLASH);
        add(LwjglGlfwKeycode.KEY_PAGE_UP, FCLKeycodes.KEY_PAGEUP);
        add(LwjglGlfwKeycode.KEY_PAGE_DOWN, FCLKeycodes.KEY_PAGEDOWN);
        add(LwjglGlfwKeycode.KEY_ESCAPE, FCLKeycodes.KEY_ESC);
        add(LwjglGlfwKeycode.KEY_LEFT_CONTROL, FCLKeycodes.KEY_LEFTCTRL);
        add(LwjglGlfwKeycode.KEY_RIGHT_CONTROL, FCLKeycodes.KEY_RIGHTCTRL);
        add(LwjglGlfwKeycode.KEY_CAPS_LOCK, FCLKeycodes.KEY_CAPSLOCK);
        add(LwjglGlfwKeycode.KEY_PAUSE, FCLKeycodes.KEY_PAUSE);
        add(LwjglGlfwKeycode.KEY_END, FCLKeycodes.KEY_END);
        add(LwjglGlfwKeycode.KEY_INSERT, FCLKeycodes.KEY_INSERT);
        add(LwjglGlfwKeycode.KEY_F1, FCLKeycodes.KEY_F1);
        add(LwjglGlfwKeycode.KEY_F2, FCLKeycodes.KEY_F2);
        add(LwjglGlfwKeycode.KEY_F3, FCLKeycodes.KEY_F3);
        add(LwjglGlfwKeycode.KEY_F4, FCLKeycodes.KEY_F4);
        add(LwjglGlfwKeycode.KEY_F5, FCLKeycodes.KEY_F5);
        add(LwjglGlfwKeycode.KEY_F6, FCLKeycodes.KEY_F6);
        add(LwjglGlfwKeycode.KEY_F7, FCLKeycodes.KEY_F7);
        add(LwjglGlfwKeycode.KEY_F8, FCLKeycodes.KEY_F8);
        add(LwjglGlfwKeycode.KEY_F9, FCLKeycodes.KEY_F9);
        add(LwjglGlfwKeycode.KEY_F10, FCLKeycodes.KEY_F10);
        add(LwjglGlfwKeycode.KEY_F11, FCLKeycodes.KEY_F11);
        add(LwjglGlfwKeycode.KEY_F12, FCLKeycodes.KEY_F12);
        add(LwjglGlfwKeycode.KEY_NUM_LOCK, FCLKeycodes.KEY_NUMLOCK);
        add(LwjglGlfwKeycode.KEY_KP_0, FCLKeycodes.KEY_KP0);
        add(LwjglGlfwKeycode.KEY_KP_1, FCLKeycodes.KEY_KP1);
        add(LwjglGlfwKeycode.KEY_KP_2, FCLKeycodes.KEY_KP2);
        add(LwjglGlfwKeycode.KEY_KP_3, FCLKeycodes.KEY_KP3);
        add(LwjglGlfwKeycode.KEY_KP_4, FCLKeycodes.KEY_KP4);
        add(LwjglGlfwKeycode.KEY_KP_5, FCLKeycodes.KEY_KP5);
        add(LwjglGlfwKeycode.KEY_KP_6, FCLKeycodes.KEY_KP6);
        add(LwjglGlfwKeycode.KEY_KP_7, FCLKeycodes.KEY_KP7);
        add(LwjglGlfwKeycode.KEY_KP_8, FCLKeycodes.KEY_KP8);
        add(LwjglGlfwKeycode.KEY_KP_9, FCLKeycodes.KEY_KP9);
        add(LwjglGlfwKeycode.KEY_KP_DECIMAL, FCLKeycodes.KEY_KPDOT);
        add(LwjglGlfwKeycode.KEY_KP_SUBTRACT, FCLKeycodes.KEY_KPMINUS);
        add(LwjglGlfwKeycode.KEY_KP_MULTIPLY, FCLKeycodes.KEY_KPASTERISK);
        add(LwjglGlfwKeycode.KEY_KP_ADD, FCLKeycodes.KEY_KPPLUS);
        add(LwjglGlfwKeycode.KEY_KP_DIVIDE, FCLKeycodes.KEY_KPSLASH);
        add(LwjglGlfwKeycode.KEY_KP_ENTER, FCLKeycodes.KEY_KPENTER);
        add(LwjglGlfwKeycode.KEY_KP_EQUAL, FCLKeycodes.KEY_KPEQUAL);
    }
}