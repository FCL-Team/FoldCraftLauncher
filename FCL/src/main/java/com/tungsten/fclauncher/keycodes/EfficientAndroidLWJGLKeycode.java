/*
 * Zalith Launcher 2
 * Copyright (C) 2025 MovTery <movtery228@qq.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.tungsten.fclauncher.keycodes;

import android.view.KeyCharacterMap;
import android.view.KeyEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://github.com/PojavLauncherTeam/PojavLauncher/blob/v3_openjdk/app_pojavlauncher/src/main/java/net/kdt/pojavlaunch/EfficientAndroidLWJGLKeycode.java">Modified from PojavLauncher</a>
 */
public class EfficientAndroidLWJGLKeycode {

    /** GLFW 键码 → Android 键码 */
    private static final Map<Short, Integer> LWJGL_TO_ANDROID = new HashMap<>();

    private static void add(int androidKeycode, short lwjglKeycode) {
        LWJGL_TO_ANDROID.put(lwjglKeycode, androidKeycode);
    }

    static {
        add(KeyEvent.KEYCODE_HOME, LwjglGlfwKeycode.KEY_HOME);
        // Escape key
        add(KeyEvent.KEYCODE_BACK, LwjglGlfwKeycode.KEY_ESCAPE);

        // 0-9 keys
        add(KeyEvent.KEYCODE_0, LwjglGlfwKeycode.KEY_0);
        add(KeyEvent.KEYCODE_1, LwjglGlfwKeycode.KEY_1);
        add(KeyEvent.KEYCODE_2, LwjglGlfwKeycode.KEY_2);
        add(KeyEvent.KEYCODE_3, LwjglGlfwKeycode.KEY_3);
        add(KeyEvent.KEYCODE_4, LwjglGlfwKeycode.KEY_4);
        add(KeyEvent.KEYCODE_5, LwjglGlfwKeycode.KEY_5);
        add(KeyEvent.KEYCODE_6, LwjglGlfwKeycode.KEY_6);
        add(KeyEvent.KEYCODE_7, LwjglGlfwKeycode.KEY_7);
        add(KeyEvent.KEYCODE_8, LwjglGlfwKeycode.KEY_8);
        add(KeyEvent.KEYCODE_9, LwjglGlfwKeycode.KEY_9);

        // Arrow keys
        add(KeyEvent.KEYCODE_DPAD_UP, LwjglGlfwKeycode.KEY_UP);
        add(KeyEvent.KEYCODE_DPAD_DOWN, LwjglGlfwKeycode.KEY_DOWN);
        add(KeyEvent.KEYCODE_DPAD_LEFT, LwjglGlfwKeycode.KEY_LEFT);
        add(KeyEvent.KEYCODE_DPAD_RIGHT, LwjglGlfwKeycode.KEY_RIGHT);

        // A-Z keys
        add(KeyEvent.KEYCODE_A, LwjglGlfwKeycode.KEY_A);
        add(KeyEvent.KEYCODE_B, LwjglGlfwKeycode.KEY_B);
        add(KeyEvent.KEYCODE_C, LwjglGlfwKeycode.KEY_C);
        add(KeyEvent.KEYCODE_D, LwjglGlfwKeycode.KEY_D);
        add(KeyEvent.KEYCODE_E, LwjglGlfwKeycode.KEY_E);
        add(KeyEvent.KEYCODE_F, LwjglGlfwKeycode.KEY_F);
        add(KeyEvent.KEYCODE_G, LwjglGlfwKeycode.KEY_G);
        add(KeyEvent.KEYCODE_H, LwjglGlfwKeycode.KEY_H);
        add(KeyEvent.KEYCODE_I, LwjglGlfwKeycode.KEY_I);
        add(KeyEvent.KEYCODE_J, LwjglGlfwKeycode.KEY_J);
        add(KeyEvent.KEYCODE_K, LwjglGlfwKeycode.KEY_K);
        add(KeyEvent.KEYCODE_L, LwjglGlfwKeycode.KEY_L);
        add(KeyEvent.KEYCODE_M, LwjglGlfwKeycode.KEY_M);
        add(KeyEvent.KEYCODE_N, LwjglGlfwKeycode.KEY_N);
        add(KeyEvent.KEYCODE_O, LwjglGlfwKeycode.KEY_O);
        add(KeyEvent.KEYCODE_P, LwjglGlfwKeycode.KEY_P);
        add(KeyEvent.KEYCODE_Q, LwjglGlfwKeycode.KEY_Q);
        add(KeyEvent.KEYCODE_R, LwjglGlfwKeycode.KEY_R);
        add(KeyEvent.KEYCODE_S, LwjglGlfwKeycode.KEY_S);
        add(KeyEvent.KEYCODE_T, LwjglGlfwKeycode.KEY_T);
        add(KeyEvent.KEYCODE_U, LwjglGlfwKeycode.KEY_U);
        add(KeyEvent.KEYCODE_V, LwjglGlfwKeycode.KEY_V);
        add(KeyEvent.KEYCODE_W, LwjglGlfwKeycode.KEY_W);
        add(KeyEvent.KEYCODE_X, LwjglGlfwKeycode.KEY_X);
        add(KeyEvent.KEYCODE_Y, LwjglGlfwKeycode.KEY_Y);
        add(KeyEvent.KEYCODE_Z, LwjglGlfwKeycode.KEY_Z);

        add(KeyEvent.KEYCODE_COMMA, LwjglGlfwKeycode.KEY_COMMA);
        add(KeyEvent.KEYCODE_PERIOD, LwjglGlfwKeycode.KEY_PERIOD);

        // Alt keys
        add(KeyEvent.KEYCODE_ALT_LEFT, LwjglGlfwKeycode.KEY_LEFT_ALT);
        add(KeyEvent.KEYCODE_ALT_RIGHT, LwjglGlfwKeycode.KEY_RIGHT_ALT);

        // Shift keys
        add(KeyEvent.KEYCODE_SHIFT_LEFT, LwjglGlfwKeycode.KEY_LEFT_SHIFT);
        add(KeyEvent.KEYCODE_SHIFT_RIGHT, LwjglGlfwKeycode.KEY_RIGHT_SHIFT);

        add(KeyEvent.KEYCODE_TAB, LwjglGlfwKeycode.KEY_TAB);
        add(KeyEvent.KEYCODE_SPACE, LwjglGlfwKeycode.KEY_SPACE);
        add(KeyEvent.KEYCODE_ENTER, LwjglGlfwKeycode.KEY_ENTER);
        add(KeyEvent.KEYCODE_DEL, LwjglGlfwKeycode.KEY_BACKSPACE); // Backspace
        add(KeyEvent.KEYCODE_GRAVE, LwjglGlfwKeycode.KEY_GRAVE_ACCENT);
        add(KeyEvent.KEYCODE_MINUS, LwjglGlfwKeycode.KEY_MINUS);
        add(KeyEvent.KEYCODE_EQUALS, LwjglGlfwKeycode.KEY_EQUAL);
        add(KeyEvent.KEYCODE_LEFT_BRACKET, LwjglGlfwKeycode.KEY_LEFT_BRACKET);
        add(KeyEvent.KEYCODE_RIGHT_BRACKET, LwjglGlfwKeycode.KEY_RIGHT_BRACKET);
        add(KeyEvent.KEYCODE_BACKSLASH, LwjglGlfwKeycode.KEY_BACKSLASH);
        add(KeyEvent.KEYCODE_SEMICOLON, LwjglGlfwKeycode.KEY_SEMICOLON);
        add(KeyEvent.KEYCODE_APOSTROPHE, LwjglGlfwKeycode.KEY_APOSTROPHE);
        add(KeyEvent.KEYCODE_SLASH, LwjglGlfwKeycode.KEY_SLASH);
        add(KeyEvent.KEYCODE_PLUS, LwjglGlfwKeycode.KEY_KP_ADD);
        add(KeyEvent.KEYCODE_MENU, LwjglGlfwKeycode.KEY_MENU);

        // Page keys
        add(KeyEvent.KEYCODE_PAGE_UP, LwjglGlfwKeycode.KEY_PAGE_UP);
        add(KeyEvent.KEYCODE_PAGE_DOWN, LwjglGlfwKeycode.KEY_PAGE_DOWN);

        add(KeyEvent.KEYCODE_ESCAPE, LwjglGlfwKeycode.KEY_ESCAPE);
        add(KeyEvent.KEYCODE_FORWARD_DEL, LwjglGlfwKeycode.KEY_DELETE);

        // Control keys
        add(KeyEvent.KEYCODE_CTRL_LEFT, LwjglGlfwKeycode.KEY_LEFT_CONTROL);
        add(KeyEvent.KEYCODE_CTRL_RIGHT, LwjglGlfwKeycode.KEY_RIGHT_CONTROL);

        add(KeyEvent.KEYCODE_CAPS_LOCK, LwjglGlfwKeycode.KEY_CAPS_LOCK);
        add(KeyEvent.KEYCODE_SCROLL_LOCK, LwjglGlfwKeycode.KEY_SCROLL_LOCK);
        add(KeyEvent.KEYCODE_META_LEFT, LwjglGlfwKeycode.KEY_LEFT_SUPER);
        add(KeyEvent.KEYCODE_META_RIGHT, LwjglGlfwKeycode.KEY_RIGHT_SUPER);
        add(KeyEvent.KEYCODE_SYSRQ, LwjglGlfwKeycode.KEY_PRINT_SCREEN);
        add(KeyEvent.KEYCODE_BREAK, LwjglGlfwKeycode.KEY_PAUSE);
        add(KeyEvent.KEYCODE_MOVE_HOME, LwjglGlfwKeycode.KEY_HOME);
        add(KeyEvent.KEYCODE_MOVE_END, LwjglGlfwKeycode.KEY_END);
        add(KeyEvent.KEYCODE_INSERT, LwjglGlfwKeycode.KEY_INSERT);

        // Fn keys
        add(KeyEvent.KEYCODE_F1, LwjglGlfwKeycode.KEY_F1);
        add(KeyEvent.KEYCODE_F2, LwjglGlfwKeycode.KEY_F2);
        add(KeyEvent.KEYCODE_F3, LwjglGlfwKeycode.KEY_F3);
        add(KeyEvent.KEYCODE_F4, LwjglGlfwKeycode.KEY_F4);
        add(KeyEvent.KEYCODE_F5, LwjglGlfwKeycode.KEY_F5);
        add(KeyEvent.KEYCODE_F6, LwjglGlfwKeycode.KEY_F6);
        add(KeyEvent.KEYCODE_F7, LwjglGlfwKeycode.KEY_F7);
        add(KeyEvent.KEYCODE_F8, LwjglGlfwKeycode.KEY_F8);
        add(KeyEvent.KEYCODE_F9, LwjglGlfwKeycode.KEY_F9);
        add(KeyEvent.KEYCODE_F10, LwjglGlfwKeycode.KEY_F10);
        add(KeyEvent.KEYCODE_F11, LwjglGlfwKeycode.KEY_F11);
        add(KeyEvent.KEYCODE_F12, LwjglGlfwKeycode.KEY_F12);

        // Num keys
        add(KeyEvent.KEYCODE_NUM_LOCK, LwjglGlfwKeycode.KEY_NUM_LOCK);
        add(KeyEvent.KEYCODE_NUMPAD_0, LwjglGlfwKeycode.KEY_KP_0);
        add(KeyEvent.KEYCODE_NUMPAD_1, LwjglGlfwKeycode.KEY_KP_1);
        add(KeyEvent.KEYCODE_NUMPAD_2, LwjglGlfwKeycode.KEY_KP_2);
        add(KeyEvent.KEYCODE_NUMPAD_3, LwjglGlfwKeycode.KEY_KP_3);
        add(KeyEvent.KEYCODE_NUMPAD_4, LwjglGlfwKeycode.KEY_KP_4);
        add(KeyEvent.KEYCODE_NUMPAD_5, LwjglGlfwKeycode.KEY_KP_5);
        add(KeyEvent.KEYCODE_NUMPAD_6, LwjglGlfwKeycode.KEY_KP_6);
        add(KeyEvent.KEYCODE_NUMPAD_7, LwjglGlfwKeycode.KEY_KP_7);
        add(KeyEvent.KEYCODE_NUMPAD_8, LwjglGlfwKeycode.KEY_KP_8);
        add(KeyEvent.KEYCODE_NUMPAD_9, LwjglGlfwKeycode.KEY_KP_9);
        add(KeyEvent.KEYCODE_NUMPAD_DIVIDE, LwjglGlfwKeycode.KEY_KP_DIVIDE);
        add(KeyEvent.KEYCODE_NUMPAD_MULTIPLY, LwjglGlfwKeycode.KEY_KP_MULTIPLY);
        add(KeyEvent.KEYCODE_NUMPAD_SUBTRACT, LwjglGlfwKeycode.KEY_KP_SUBTRACT);
        add(KeyEvent.KEYCODE_NUMPAD_ADD, LwjglGlfwKeycode.KEY_KP_ADD);
        add(KeyEvent.KEYCODE_NUMPAD_DOT, LwjglGlfwKeycode.KEY_KP_DECIMAL);
        add(KeyEvent.KEYCODE_NUMPAD_COMMA, LwjglGlfwKeycode.KEY_COMMA);
        add(KeyEvent.KEYCODE_NUMPAD_ENTER, LwjglGlfwKeycode.KEY_KP_ENTER);
        add(KeyEvent.KEYCODE_NUMPAD_EQUALS, LwjglGlfwKeycode.KEY_KP_EQUAL);
    }

    /** GLFW 键码反查 Android 键码，无映射时返回 KEYCODE_UNKNOWN */
    public static int getAndroidKeycode(int lwjglGlfwKeycode) {
        Integer androidKeycode = LWJGL_TO_ANDROID.get((short) lwjglGlfwKeycode);
        return androidKeycode != null ? androidKeycode : KeyEvent.KEYCODE_UNKNOWN;
    }

    public static int getSdlAndroidKeycode(int lwjglGlfwKeycode) {
        return switch (lwjglGlfwKeycode) {
            case LwjglGlfwKeycode.KEY_ESCAPE -> KeyEvent.KEYCODE_ESCAPE;
            case LwjglGlfwKeycode.KEY_HOME -> KeyEvent.KEYCODE_MOVE_HOME;
            case LwjglGlfwKeycode.KEY_END -> KeyEvent.KEYCODE_MOVE_END;
            case LwjglGlfwKeycode.KEY_KP_ADD -> KeyEvent.KEYCODE_NUMPAD_ADD;
            case LwjglGlfwKeycode.KEY_KP_DECIMAL -> KeyEvent.KEYCODE_NUMPAD_DOT;
            case LwjglGlfwKeycode.KEY_KP_ENTER -> KeyEvent.KEYCODE_NUMPAD_ENTER;
            case LwjglGlfwKeycode.KEY_DELETE -> KeyEvent.KEYCODE_FORWARD_DEL;
            case LwjglGlfwKeycode.KEY_KP_EQUAL -> KeyEvent.KEYCODE_NUMPAD_EQUALS;
            case LwjglGlfwKeycode.KEY_LEFT_SUPER -> KeyEvent.KEYCODE_META_LEFT;
            case LwjglGlfwKeycode.KEY_RIGHT_SUPER -> KeyEvent.KEYCODE_META_RIGHT;
            case LwjglGlfwKeycode.KEY_MENU -> KeyEvent.KEYCODE_MENU;
            default -> getAndroidKeycode(lwjglGlfwKeycode);
        };
    }

    private static final KeyCharacterMap mKcm = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
    private static final char[] buffer = new char[1];

    /** 按字符反查 Android 键码（虚拟键盘字符映射表），无映射时返回 KEYCODE_UNKNOWN */
    public static int getAndroidKeycode(char c) {
        buffer[0] = c;
        KeyEvent[] events = mKcm.getEvents(buffer);
        return events != null && events.length > 0
                ? events[0].getKeyCode()
                : KeyEvent.KEYCODE_UNKNOWN;
    }
}
