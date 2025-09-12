package com.tungsten.fclauncher.keycodes;

import androidx.annotation.Nullable;

public class MinecraftKeyBindingMapper {
    /**
     * Minecraft 绑定按键映射表
     *
     * @param keybinding 绑定的按键
     * @return 对应的GLFW按键
     */
    public static @Nullable Short getGlfwKeycode(String keybinding) {
        switch (keybinding) {
            case "key.keyboard.unknown": return FCLKeycodes.KEY_UNKNOWN;
            case "key.mouse.left": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_LEFT;
            case "key.mouse.right": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_RIGHT;
            case "key.mouse.middle": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_MIDDLE;
            case "key.mouse.4": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_4;
            case "key.mouse.5": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_5;
            case "key.mouse.6": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_6;
            case "key.mouse.7": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_7;
            case "key.mouse.8": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_8;
            case "key.keyboard.0": return FCLKeycodes.KEY_0;
            case "key.keyboard.1": return FCLKeycodes.KEY_1;
            case "key.keyboard.2": return FCLKeycodes.KEY_2;
            case "key.keyboard.3": return FCLKeycodes.KEY_3;
            case "key.keyboard.4": return FCLKeycodes.KEY_4;
            case "key.keyboard.5": return FCLKeycodes.KEY_5;
            case "key.keyboard.6": return FCLKeycodes.KEY_6;
            case "key.keyboard.7": return FCLKeycodes.KEY_7;
            case "key.keyboard.8": return FCLKeycodes.KEY_8;
            case "key.keyboard.9": return FCLKeycodes.KEY_9;
            case "key.keyboard.a": return FCLKeycodes.KEY_A;
            case "key.keyboard.b": return FCLKeycodes.KEY_B;
            case "key.keyboard.c": return FCLKeycodes.KEY_C;
            case "key.keyboard.d": return FCLKeycodes.KEY_D;
            case "key.keyboard.e": return FCLKeycodes.KEY_E;
            case "key.keyboard.f": return FCLKeycodes.KEY_F;
            case "key.keyboard.g": return FCLKeycodes.KEY_G;
            case "key.keyboard.h": return FCLKeycodes.KEY_H;
            case "key.keyboard.i": return FCLKeycodes.KEY_I;
            case "key.keyboard.j": return FCLKeycodes.KEY_J;
            case "key.keyboard.k": return FCLKeycodes.KEY_K;
            case "key.keyboard.l": return FCLKeycodes.KEY_L;
            case "key.keyboard.m": return FCLKeycodes.KEY_M;
            case "key.keyboard.n": return FCLKeycodes.KEY_N;
            case "key.keyboard.o": return FCLKeycodes.KEY_O;
            case "key.keyboard.p": return FCLKeycodes.KEY_P;
            case "key.keyboard.q": return FCLKeycodes.KEY_Q;
            case "key.keyboard.r": return FCLKeycodes.KEY_R;
            case "key.keyboard.s": return FCLKeycodes.KEY_S;
            case "key.keyboard.t": return FCLKeycodes.KEY_T;
            case "key.keyboard.u": return FCLKeycodes.KEY_U;
            case "key.keyboard.v": return FCLKeycodes.KEY_V;
            case "key.keyboard.w": return FCLKeycodes.KEY_W;
            case "key.keyboard.x": return FCLKeycodes.KEY_X;
            case "key.keyboard.y": return FCLKeycodes.KEY_Y;
            case "key.keyboard.z": return FCLKeycodes.KEY_Z;
            case "key.keyboard.f1": return FCLKeycodes.KEY_F1;
            case "key.keyboard.f2": return FCLKeycodes.KEY_F2;
            case "key.keyboard.f3": return FCLKeycodes.KEY_F3;
            case "key.keyboard.f4": return FCLKeycodes.KEY_F4;
            case "key.keyboard.f5": return FCLKeycodes.KEY_F5;
            case "key.keyboard.f6": return FCLKeycodes.KEY_F6;
            case "key.keyboard.f7": return FCLKeycodes.KEY_F7;
            case "key.keyboard.f8": return FCLKeycodes.KEY_F8;
            case "key.keyboard.f9": return FCLKeycodes.KEY_F9;
            case "key.keyboard.f10": return FCLKeycodes.KEY_F10;
            case "key.keyboard.f11": return FCLKeycodes.KEY_F11;
            case "key.keyboard.f12": return FCLKeycodes.KEY_F12;
            case "key.keyboard.f13": return FCLKeycodes.KEY_F13;
            case "key.keyboard.f14": return FCLKeycodes.KEY_F14;
            case "key.keyboard.f15": return FCLKeycodes.KEY_F15;
            case "key.keyboard.f16": return FCLKeycodes.KEY_F16;
            case "key.keyboard.f17": return FCLKeycodes.KEY_F17;
            case "key.keyboard.f18": return FCLKeycodes.KEY_F18;
            case "key.keyboard.f19": return FCLKeycodes.KEY_F19;
            case "key.keyboard.f20": return FCLKeycodes.KEY_F20;
            case "key.keyboard.f21": return FCLKeycodes.KEY_F21;
            case "key.keyboard.f22": return FCLKeycodes.KEY_F22;
            case "key.keyboard.f23": return FCLKeycodes.KEY_F23;
            case "key.keyboard.f24": return FCLKeycodes.KEY_F24;
            case "key.keyboard.num.lock": return FCLKeycodes.KEY_NUMLOCK;
            case "key.keyboard.keypad.0": return FCLKeycodes.KEY_KP0;
            case "key.keyboard.keypad.1": return FCLKeycodes.KEY_KP1;
            case "key.keyboard.keypad.2": return FCLKeycodes.KEY_KP2;
            case "key.keyboard.keypad.3": return FCLKeycodes.KEY_KP3;
            case "key.keyboard.keypad.4": return FCLKeycodes.KEY_KP4;
            case "key.keyboard.keypad.5": return FCLKeycodes.KEY_KP5;
            case "key.keyboard.keypad.6": return FCLKeycodes.KEY_KP6;
            case "key.keyboard.keypad.7": return FCLKeycodes.KEY_KP7;
            case "key.keyboard.keypad.8": return FCLKeycodes.KEY_KP8;
            case "key.keyboard.keypad.9": return FCLKeycodes.KEY_KP9;
            case "key.keyboard.down": return FCLKeycodes.KEY_DOWN;
            case "key.keyboard.left": return FCLKeycodes.KEY_LEFT;
            case "key.keyboard.right": return FCLKeycodes.KEY_RIGHT;
            case "key.keyboard.up": return FCLKeycodes.KEY_UP;
            case "key.keyboard.apostrophe": return FCLKeycodes.KEY_APOSTROPHE;
            case "key.keyboard.backslash": return FCLKeycodes.KEY_BACKSLASH;
            case "key.keyboard.comma": return FCLKeycodes.KEY_COMMA;
            case "key.keyboard.equal": return FCLKeycodes.KEY_EQUAL;
            case "key.keyboard.grave.accent": return FCLKeycodes.KEY_GRAVE;
            case "key.keyboard.left.bracket": return FCLKeycodes.KEY_LEFTBRACE;
            case "key.keyboard.minus": return FCLKeycodes.KEY_MINUS;
            case "key.keyboard.semicolon": return FCLKeycodes.KEY_SEMICOLON;
            case "key.keyboard.slash": return FCLKeycodes.KEY_SLASH;
            case "key.keyboard.space": return FCLKeycodes.KEY_SPACE;
            case "key.keyboard.tab": return FCLKeycodes.KEY_TAB;
            case "key.keyboard.left.alt": return FCLKeycodes.KEY_LEFTALT;
            case "key.keyboard.left.control": return FCLKeycodes.KEY_LEFTCTRL;
            case "key.keyboard.left.shift": return FCLKeycodes.KEY_LEFTSHIFT;
            case "key.keyboard.right.alt": return FCLKeycodes.KEY_RIGHTALT;
            case "key.keyboard.right.control": return FCLKeycodes.KEY_RIGHTCTRL;
            case "key.keyboard.right.shift": return FCLKeycodes.KEY_RIGHTSHIFT;
            case "key.keyboard.enter": return FCLKeycodes.KEY_ENTER;
            case "key.keyboard.escape": return FCLKeycodes.KEY_ESC;
            case "key.keyboard.backspace": return FCLKeycodes.KEY_BACKSPACE;
            case "key.keyboard.delete": return FCLKeycodes.KEY_DELETE;
            case "key.keyboard.end": return FCLKeycodes.KEY_END;
            case "key.keyboard.home": return FCLKeycodes.KEY_HOME;
            case "key.keyboard.insert": return FCLKeycodes.KEY_INSERT;
            case "key.keyboard.page.down": return FCLKeycodes.KEY_PAGEDOWN;
            case "key.keyboard.page.up": return FCLKeycodes.KEY_PAGEUP;
            case "key.keyboard.caps.lock": return FCLKeycodes.KEY_CAPSLOCK;
            case "key.keyboard.pause": return FCLKeycodes.KEY_PAUSE;
            case "key.keyboard.scroll.lock": return FCLKeycodes.KEY_SCROLLLOCK;
            default: return null;
        }
    }
}
