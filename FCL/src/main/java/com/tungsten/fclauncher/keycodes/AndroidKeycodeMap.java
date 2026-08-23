package com.tungsten.fclauncher.keycodes;

import android.view.KeyEvent;

import java.util.Arrays;

public class AndroidKeycodeMap {

    private static final int[] ANDROID_KEYCODES = new int[99];
    private static final int[] FCL_KEYCODES = new int[99];

    private static int count = 0;

    private static void add(int androidKeycode, int fclKeycode) {
        ANDROID_KEYCODES[count] = androidKeycode;
        FCL_KEYCODES[count] = fclKeycode;
        count++;
    }

    public static int convertKeycode(int androidKeycode) {
        int index = Arrays.binarySearch(ANDROID_KEYCODES, androidKeycode);
        if (index >= 0)
            return FCL_KEYCODES[index];
        return FCLKeycodes.KEY_UNKNOWN;
    }

    static {
        add(KeyEvent.KEYCODE_HOME,                           FCLKeycodes.KEY_HOME);
        add(KeyEvent.KEYCODE_BACK,                           FCLKeycodes.KEY_ESC);

        add(KeyEvent.KEYCODE_0,                              FCLKeycodes.KEY_0);
        add(KeyEvent.KEYCODE_1,                              FCLKeycodes.KEY_1);
        add(KeyEvent.KEYCODE_2,                              FCLKeycodes.KEY_2);
        add(KeyEvent.KEYCODE_3,                              FCLKeycodes.KEY_3);
        add(KeyEvent.KEYCODE_4,                              FCLKeycodes.KEY_4);
        add(KeyEvent.KEYCODE_5,                              FCLKeycodes.KEY_5);
        add(KeyEvent.KEYCODE_6,                              FCLKeycodes.KEY_6);
        add(KeyEvent.KEYCODE_7,                              FCLKeycodes.KEY_7);
        add(KeyEvent.KEYCODE_8,                              FCLKeycodes.KEY_8);
        add(KeyEvent.KEYCODE_9,                              FCLKeycodes.KEY_9);

        add(KeyEvent.KEYCODE_POUND,                          FCLKeycodes.KEY_3);

        add(KeyEvent.KEYCODE_DPAD_UP,                        FCLKeycodes.KEY_UP);
        add(KeyEvent.KEYCODE_DPAD_DOWN,                      FCLKeycodes.KEY_DOWN);
        add(KeyEvent.KEYCODE_DPAD_LEFT,                      FCLKeycodes.KEY_LEFT);
        add(KeyEvent.KEYCODE_DPAD_RIGHT,                     FCLKeycodes.KEY_RIGHT);

        add(KeyEvent.KEYCODE_A,                              FCLKeycodes.KEY_A);
        add(KeyEvent.KEYCODE_B,                              FCLKeycodes.KEY_B);
        add(KeyEvent.KEYCODE_C,                              FCLKeycodes.KEY_C);
        add(KeyEvent.KEYCODE_D,                              FCLKeycodes.KEY_D);
        add(KeyEvent.KEYCODE_E,                              FCLKeycodes.KEY_E);
        add(KeyEvent.KEYCODE_F,                              FCLKeycodes.KEY_F);
        add(KeyEvent.KEYCODE_G,                              FCLKeycodes.KEY_G);
        add(KeyEvent.KEYCODE_H,                              FCLKeycodes.KEY_H);
        add(KeyEvent.KEYCODE_I,                              FCLKeycodes.KEY_I);
        add(KeyEvent.KEYCODE_J,                              FCLKeycodes.KEY_J);
        add(KeyEvent.KEYCODE_K,                              FCLKeycodes.KEY_K);
        add(KeyEvent.KEYCODE_L,                              FCLKeycodes.KEY_L);
        add(KeyEvent.KEYCODE_M,                              FCLKeycodes.KEY_M);
        add(KeyEvent.KEYCODE_N,                              FCLKeycodes.KEY_N);
        add(KeyEvent.KEYCODE_O,                              FCLKeycodes.KEY_O);
        add(KeyEvent.KEYCODE_P,                              FCLKeycodes.KEY_P);
        add(KeyEvent.KEYCODE_Q,                              FCLKeycodes.KEY_Q);
        add(KeyEvent.KEYCODE_R,                              FCLKeycodes.KEY_R);
        add(KeyEvent.KEYCODE_S,                              FCLKeycodes.KEY_S);
        add(KeyEvent.KEYCODE_T,                              FCLKeycodes.KEY_T);
        add(KeyEvent.KEYCODE_U,                              FCLKeycodes.KEY_U);
        add(KeyEvent.KEYCODE_V,                              FCLKeycodes.KEY_V);
        add(KeyEvent.KEYCODE_W,                              FCLKeycodes.KEY_W);
        add(KeyEvent.KEYCODE_X,                              FCLKeycodes.KEY_X);
        add(KeyEvent.KEYCODE_Y,                              FCLKeycodes.KEY_Y);
        add(KeyEvent.KEYCODE_Z,                              FCLKeycodes.KEY_Z);
        
        add(KeyEvent.KEYCODE_COMMA,                          FCLKeycodes.KEY_COMMA);
        add(KeyEvent.KEYCODE_PERIOD,                         FCLKeycodes.KEY_DOT);

        add(KeyEvent.KEYCODE_ALT_LEFT,                       FCLKeycodes.KEY_LEFTALT);
        add(KeyEvent.KEYCODE_ALT_RIGHT,                      FCLKeycodes.KEY_RIGHTALT);

        add(KeyEvent.KEYCODE_SHIFT_LEFT,                     FCLKeycodes.KEY_LEFTSHIFT);
        add(KeyEvent.KEYCODE_SHIFT_RIGHT,                    FCLKeycodes.KEY_RIGHTSHIFT);

        add(KeyEvent.KEYCODE_TAB,                            FCLKeycodes.KEY_TAB);
        add(KeyEvent.KEYCODE_SPACE,                          FCLKeycodes.KEY_SPACE);
        add(KeyEvent.KEYCODE_ENTER,                          FCLKeycodes.KEY_ENTER);
        add(KeyEvent.KEYCODE_DEL,                            FCLKeycodes.KEY_BACKSPACE);
        add(KeyEvent.KEYCODE_GRAVE,                          FCLKeycodes.KEY_GRAVE);
        add(KeyEvent.KEYCODE_MINUS,                          FCLKeycodes.KEY_MINUS);
        add(KeyEvent.KEYCODE_EQUALS,                         FCLKeycodes.KEY_EQUAL);
        add(KeyEvent.KEYCODE_LEFT_BRACKET,                   FCLKeycodes.KEY_LEFTBRACE);
        add(KeyEvent.KEYCODE_RIGHT_BRACKET,                  FCLKeycodes.KEY_RIGHTBRACE);
        add(KeyEvent.KEYCODE_BACKSLASH,                      FCLKeycodes.KEY_BACKSLASH);
        add(KeyEvent.KEYCODE_SEMICOLON,                      FCLKeycodes.KEY_SEMICOLON);
        add(KeyEvent.KEYCODE_APOSTROPHE,                     FCLKeycodes.KEY_APOSTROPHE);
        add(KeyEvent.KEYCODE_SLASH,                          FCLKeycodes.KEY_SLASH);
        add(KeyEvent.KEYCODE_AT,                             FCLKeycodes.KEY_2);

        add(KeyEvent.KEYCODE_PAGE_UP,                        FCLKeycodes.KEY_PAGEUP);
        add(KeyEvent.KEYCODE_PAGE_DOWN,                      FCLKeycodes.KEY_PAGEDOWN);

        add(KeyEvent.KEYCODE_ESCAPE,                         FCLKeycodes.KEY_ESC);

        add(KeyEvent.KEYCODE_CTRL_LEFT,                      FCLKeycodes.KEY_LEFTCTRL);
        add(KeyEvent.KEYCODE_CTRL_RIGHT,                     FCLKeycodes.KEY_RIGHTCTRL);

        add(KeyEvent.KEYCODE_CAPS_LOCK,                      FCLKeycodes.KEY_CAPSLOCK);
        add(KeyEvent.KEYCODE_BREAK,                          FCLKeycodes.KEY_PAUSE);
        add(KeyEvent.KEYCODE_MOVE_END,                       FCLKeycodes.KEY_END);
        add(KeyEvent.KEYCODE_INSERT,                         FCLKeycodes.KEY_INSERT);

        add(KeyEvent.KEYCODE_F1,                             FCLKeycodes.KEY_F1);
        add(KeyEvent.KEYCODE_F2,                             FCLKeycodes.KEY_F2);
        add(KeyEvent.KEYCODE_F3,                             FCLKeycodes.KEY_F3);
        add(KeyEvent.KEYCODE_F4,                             FCLKeycodes.KEY_F4);
        add(KeyEvent.KEYCODE_F5,                             FCLKeycodes.KEY_F5);
        add(KeyEvent.KEYCODE_F6,                             FCLKeycodes.KEY_F6);
        add(KeyEvent.KEYCODE_F7,                             FCLKeycodes.KEY_F7);
        add(KeyEvent.KEYCODE_F8,                             FCLKeycodes.KEY_F8);
        add(KeyEvent.KEYCODE_F9,                             FCLKeycodes.KEY_F9);
        add(KeyEvent.KEYCODE_F10,                            FCLKeycodes.KEY_F10);
        add(KeyEvent.KEYCODE_F11,                            FCLKeycodes.KEY_F11);
        add(KeyEvent.KEYCODE_F12,                            FCLKeycodes.KEY_F12);

        add(KeyEvent.KEYCODE_NUM_LOCK,                       FCLKeycodes.KEY_NUMLOCK);
        add(KeyEvent.KEYCODE_NUMPAD_0,                       FCLKeycodes.KEY_KP0);
        add(KeyEvent.KEYCODE_NUMPAD_1,                       FCLKeycodes.KEY_KP1);
        add(KeyEvent.KEYCODE_NUMPAD_2,                       FCLKeycodes.KEY_KP2);
        add(KeyEvent.KEYCODE_NUMPAD_3,                       FCLKeycodes.KEY_KP3);
        add(KeyEvent.KEYCODE_NUMPAD_4,                       FCLKeycodes.KEY_KP4);
        add(KeyEvent.KEYCODE_NUMPAD_5,                       FCLKeycodes.KEY_KP5);
        add(KeyEvent.KEYCODE_NUMPAD_6,                       FCLKeycodes.KEY_KP6);
        add(KeyEvent.KEYCODE_NUMPAD_7,                       FCLKeycodes.KEY_KP7);
        add(KeyEvent.KEYCODE_NUMPAD_8,                       FCLKeycodes.KEY_KP8);
        add(KeyEvent.KEYCODE_NUMPAD_9,                       FCLKeycodes.KEY_KP9);
        add(KeyEvent.KEYCODE_NUMPAD_DOT,                     FCLKeycodes.KEY_KPDOT);
        add(KeyEvent.KEYCODE_NUMPAD_COMMA,                   FCLKeycodes.KEY_KPCOMMA);
        add(KeyEvent.KEYCODE_NUMPAD_ENTER,                   FCLKeycodes.KEY_KPENTER);
        add(KeyEvent.KEYCODE_NUMPAD_EQUALS,                  FCLKeycodes.KEY_KPEQUAL);
    }

}
