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
            case "key.keyboard.unknown": return LwjglGlfwKeycode.KEY_UNKNOWN;
            case "key.mouse.left": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_LEFT;
            case "key.mouse.right": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_RIGHT;
            case "key.mouse.middle": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_MIDDLE;
            case "key.mouse.4": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_4;
            case "key.mouse.5": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_5;
            case "key.mouse.6": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_6;
            case "key.mouse.7": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_7;
            case "key.mouse.8": return LwjglGlfwKeycode.GLFW_MOUSE_BUTTON_8;
            case "key.keyboard.0": return LwjglGlfwKeycode.KEY_0;
            case "key.keyboard.1": return LwjglGlfwKeycode.KEY_1;
            case "key.keyboard.2": return LwjglGlfwKeycode.KEY_2;
            case "key.keyboard.3": return LwjglGlfwKeycode.KEY_3;
            case "key.keyboard.4": return LwjglGlfwKeycode.KEY_4;
            case "key.keyboard.5": return LwjglGlfwKeycode.KEY_5;
            case "key.keyboard.6": return LwjglGlfwKeycode.KEY_6;
            case "key.keyboard.7": return LwjglGlfwKeycode.KEY_7;
            case "key.keyboard.8": return LwjglGlfwKeycode.KEY_8;
            case "key.keyboard.9": return LwjglGlfwKeycode.KEY_9;
            case "key.keyboard.a": return LwjglGlfwKeycode.KEY_A;
            case "key.keyboard.b": return LwjglGlfwKeycode.KEY_B;
            case "key.keyboard.c": return LwjglGlfwKeycode.KEY_C;
            case "key.keyboard.d": return LwjglGlfwKeycode.KEY_D;
            case "key.keyboard.e": return LwjglGlfwKeycode.KEY_E;
            case "key.keyboard.f": return LwjglGlfwKeycode.KEY_F;
            case "key.keyboard.g": return LwjglGlfwKeycode.KEY_G;
            case "key.keyboard.h": return LwjglGlfwKeycode.KEY_H;
            case "key.keyboard.i": return LwjglGlfwKeycode.KEY_I;
            case "key.keyboard.j": return LwjglGlfwKeycode.KEY_J;
            case "key.keyboard.k": return LwjglGlfwKeycode.KEY_K;
            case "key.keyboard.l": return LwjglGlfwKeycode.KEY_L;
            case "key.keyboard.m": return LwjglGlfwKeycode.KEY_M;
            case "key.keyboard.n": return LwjglGlfwKeycode.KEY_N;
            case "key.keyboard.o": return LwjglGlfwKeycode.KEY_O;
            case "key.keyboard.p": return LwjglGlfwKeycode.KEY_P;
            case "key.keyboard.q": return LwjglGlfwKeycode.KEY_Q;
            case "key.keyboard.r": return LwjglGlfwKeycode.KEY_R;
            case "key.keyboard.s": return LwjglGlfwKeycode.KEY_S;
            case "key.keyboard.t": return LwjglGlfwKeycode.KEY_T;
            case "key.keyboard.u": return LwjglGlfwKeycode.KEY_U;
            case "key.keyboard.v": return LwjglGlfwKeycode.KEY_V;
            case "key.keyboard.w": return LwjglGlfwKeycode.KEY_W;
            case "key.keyboard.x": return LwjglGlfwKeycode.KEY_X;
            case "key.keyboard.y": return LwjglGlfwKeycode.KEY_Y;
            case "key.keyboard.z": return LwjglGlfwKeycode.KEY_Z;
            case "key.keyboard.f1": return LwjglGlfwKeycode.KEY_F1;
            case "key.keyboard.f2": return LwjglGlfwKeycode.KEY_F2;
            case "key.keyboard.f3": return LwjglGlfwKeycode.KEY_F3;
            case "key.keyboard.f4": return LwjglGlfwKeycode.KEY_F4;
            case "key.keyboard.f5": return LwjglGlfwKeycode.KEY_F5;
            case "key.keyboard.f6": return LwjglGlfwKeycode.KEY_F6;
            case "key.keyboard.f7": return LwjglGlfwKeycode.KEY_F7;
            case "key.keyboard.f8": return LwjglGlfwKeycode.KEY_F8;
            case "key.keyboard.f9": return LwjglGlfwKeycode.KEY_F9;
            case "key.keyboard.f10": return LwjglGlfwKeycode.KEY_F10;
            case "key.keyboard.f11": return LwjglGlfwKeycode.KEY_F11;
            case "key.keyboard.f12": return LwjglGlfwKeycode.KEY_F12;
            case "key.keyboard.f13": return LwjglGlfwKeycode.KEY_F13;
            case "key.keyboard.f14": return LwjglGlfwKeycode.KEY_F14;
            case "key.keyboard.f15": return LwjglGlfwKeycode.KEY_F15;
            case "key.keyboard.f16": return LwjglGlfwKeycode.KEY_F16;
            case "key.keyboard.f17": return LwjglGlfwKeycode.KEY_F17;
            case "key.keyboard.f18": return LwjglGlfwKeycode.KEY_F18;
            case "key.keyboard.f19": return LwjglGlfwKeycode.KEY_F19;
            case "key.keyboard.f20": return LwjglGlfwKeycode.KEY_F20;
            case "key.keyboard.f21": return LwjglGlfwKeycode.KEY_F21;
            case "key.keyboard.f22": return LwjglGlfwKeycode.KEY_F22;
            case "key.keyboard.f23": return LwjglGlfwKeycode.KEY_F23;
            case "key.keyboard.f24": return LwjglGlfwKeycode.KEY_F24;
            case "key.keyboard.f25": return LwjglGlfwKeycode.KEY_F25;
            case "key.keyboard.num.lock": return LwjglGlfwKeycode.KEY_NUM_LOCK;
            case "key.keyboard.keypad.0": return LwjglGlfwKeycode.KEY_KP_0;
            case "key.keyboard.keypad.1": return LwjglGlfwKeycode.KEY_KP_1;
            case "key.keyboard.keypad.2": return LwjglGlfwKeycode.KEY_KP_2;
            case "key.keyboard.keypad.3": return LwjglGlfwKeycode.KEY_KP_3;
            case "key.keyboard.keypad.4": return LwjglGlfwKeycode.KEY_KP_4;
            case "key.keyboard.keypad.5": return LwjglGlfwKeycode.KEY_KP_5;
            case "key.keyboard.keypad.6": return LwjglGlfwKeycode.KEY_KP_6;
            case "key.keyboard.keypad.7": return LwjglGlfwKeycode.KEY_KP_7;
            case "key.keyboard.keypad.8": return LwjglGlfwKeycode.KEY_KP_8;
            case "key.keyboard.keypad.9": return LwjglGlfwKeycode.KEY_KP_9;
            case "key.keyboard.keypad.add": return LwjglGlfwKeycode.KEY_KP_ADD;
            case "key.keyboard.keypad.decimal": return LwjglGlfwKeycode.KEY_KP_DECIMAL;
            case "key.keyboard.keypad.enter": return LwjglGlfwKeycode.KEY_KP_ENTER;
            case "key.keyboard.keypad.equal": return LwjglGlfwKeycode.KEY_KP_EQUAL;
            case "key.keyboard.keypad.multiply": return LwjglGlfwKeycode.KEY_KP_MULTIPLY;
            case "key.keyboard.keypad.divide": return LwjglGlfwKeycode.KEY_KP_DIVIDE;
            case "key.keyboard.keypad.subtract": return LwjglGlfwKeycode.KEY_KP_SUBTRACT;
            case "key.keyboard.down": return LwjglGlfwKeycode.KEY_DOWN;
            case "key.keyboard.left": return LwjglGlfwKeycode.KEY_LEFT;
            case "key.keyboard.right": return LwjglGlfwKeycode.KEY_RIGHT;
            case "key.keyboard.up": return LwjglGlfwKeycode.KEY_UP;
            case "key.keyboard.apostrophe": return LwjglGlfwKeycode.KEY_APOSTROPHE;
            case "key.keyboard.backslash": return LwjglGlfwKeycode.KEY_BACKSLASH;
            case "key.keyboard.comma": return LwjglGlfwKeycode.KEY_COMMA;
            case "key.keyboard.equal": return LwjglGlfwKeycode.KEY_EQUAL;
            case "key.keyboard.grave.accent": return LwjglGlfwKeycode.KEY_GRAVE_ACCENT;
            case "key.keyboard.left.bracket": return LwjglGlfwKeycode.KEY_LEFT_BRACKET;
            case "key.keyboard.minus": return LwjglGlfwKeycode.KEY_MINUS;
            case "key.keyboard.period": return LwjglGlfwKeycode.KEY_PERIOD;
            case "key.keyboard.right.bracket": return LwjglGlfwKeycode.KEY_RIGHT_BRACKET;
            case "key.keyboard.semicolon": return LwjglGlfwKeycode.KEY_SEMICOLON;
            case "key.keyboard.slash": return LwjglGlfwKeycode.KEY_SLASH;
            case "key.keyboard.space": return LwjglGlfwKeycode.KEY_SPACE;
            case "key.keyboard.tab": return LwjglGlfwKeycode.KEY_TAB;
            case "key.keyboard.left.alt": return LwjglGlfwKeycode.KEY_LEFT_ALT;
            case "key.keyboard.left.control": return LwjglGlfwKeycode.KEY_LEFT_CONTROL;
            case "key.keyboard.left.shift": return LwjglGlfwKeycode.KEY_LEFT_SHIFT;
            case "key.keyboard.left.win": return LwjglGlfwKeycode.KEY_LEFT_SUPER;
            case "key.keyboard.right.alt": return LwjglGlfwKeycode.KEY_RIGHT_ALT;
            case "key.keyboard.right.control": return LwjglGlfwKeycode.KEY_RIGHT_CONTROL;
            case "key.keyboard.right.shift": return LwjglGlfwKeycode.KEY_RIGHT_SHIFT;
            case "key.keyboard.right.win": return LwjglGlfwKeycode.KEY_RIGHT_SUPER;
            case "key.keyboard.enter": return LwjglGlfwKeycode.KEY_ENTER;
            case "key.keyboard.escape": return LwjglGlfwKeycode.KEY_ESCAPE;
            case "key.keyboard.backspace": return LwjglGlfwKeycode.KEY_BACKSPACE;
            case "key.keyboard.delete": return LwjglGlfwKeycode.KEY_DELETE;
            case "key.keyboard.end": return LwjglGlfwKeycode.KEY_END;
            case "key.keyboard.home": return LwjglGlfwKeycode.KEY_HOME;
            case "key.keyboard.insert": return LwjglGlfwKeycode.KEY_INSERT;
            case "key.keyboard.page.down": return LwjglGlfwKeycode.KEY_PAGE_DOWN;
            case "key.keyboard.page.up": return LwjglGlfwKeycode.KEY_PAGE_UP;
            case "key.keyboard.caps.lock": return LwjglGlfwKeycode.KEY_CAPS_LOCK;
            case "key.keyboard.pause": return LwjglGlfwKeycode.KEY_PAUSE;
            case "key.keyboard.scroll.lock": return LwjglGlfwKeycode.KEY_SCROLL_LOCK;
            case "key.keyboard.menu": return LwjglGlfwKeycode.KEY_MENU;
            case "key.keyboard.print.screen": return LwjglGlfwKeycode.KEY_PRINT_SCREEN;
            case "key.keyboard.world.1": return LwjglGlfwKeycode.KEY_WORLD_1;
            case "key.keyboard.world.2": return LwjglGlfwKeycode.KEY_WORLD_2;
            default: return null;
        }
    }
}
