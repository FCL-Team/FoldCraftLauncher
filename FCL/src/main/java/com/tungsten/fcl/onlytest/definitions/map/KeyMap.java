package com.tungsten.fcl.onlytest.definitions.map;

public interface KeyMap {
    String
            /* basic keys on main keyboard */
            KEYMAP_KEY_0 = "0",
            KEYMAP_KEY_1 = "1",
            KEYMAP_KEY_2 = "2",
            KEYMAP_KEY_3 = "3",
            KEYMAP_KEY_4 = "4",
            KEYMAP_KEY_5 = "5",
            KEYMAP_KEY_6 = "6",
            KEYMAP_KEY_7 = "7",
            KEYMAP_KEY_8 = "8",
            KEYMAP_KEY_9 = "9",
            KEYMAP_KEY_A = "A",
            KEYMAP_KEY_B = "B",
            KEYMAP_KEY_C = "C",
            KEYMAP_KEY_D = "D",
            KEYMAP_KEY_E = "E",
            KEYMAP_KEY_F = "F",
            KEYMAP_KEY_G = "G",
            KEYMAP_KEY_H = "H",
            KEYMAP_KEY_I = "I",
            KEYMAP_KEY_J = "J",
            KEYMAP_KEY_K = "K",
            KEYMAP_KEY_L = "L",
            KEYMAP_KEY_M = "M",
            KEYMAP_KEY_N = "N",
            KEYMAP_KEY_O = "O",
            KEYMAP_KEY_P = "P",
            KEYMAP_KEY_Q = "Q",
            KEYMAP_KEY_R = "R",
            KEYMAP_KEY_S = "S",
            KEYMAP_KEY_T = "T",
            KEYMAP_KEY_U = "U",
            KEYMAP_KEY_V = "V",
            KEYMAP_KEY_W = "W",
            KEYMAP_KEY_X = "X",
            KEYMAP_KEY_Y = "Y",
            KEYMAP_KEY_Z = "Z",
            KEYMAP_KEY_MINUS = "-",  // - on main keyboard
            KEYMAP_KEY_EQUALS = "=",  // = on main keyboard
            KEYMAP_KEY_LBRACKET = "[",  // [ on main keyboard
            KEYMAP_KEY_RBRACKET = "]",  // ] on main keyboard
            KEYMAP_KEY_SEMICOLON = ";",  // ; on main keyboard
            KEYMAP_KEY_APOSTROPHE = "'",  // ' on main keyboard
            KEYMAP_KEY_GRAVE = "`",  // ` on main keyboard
            KEYMAP_KEY_BACKSLASH = "\\",  // \ on main keyboard
            KEYMAP_KEY_COMMA = ",", // , on main keyboard
            KEYMAP_KEY_PERIOD = ".", // . on main keyboard
            KEYMAP_KEY_SLASH = "/", // / on main keyboard

    /* function keys on main keyboard */
    KEYMAP_KEY_ESC = "ESC",
            KEYMAP_KEY_F1 = "F1",
            KEYMAP_KEY_F2 = "F2",
            KEYMAP_KEY_F3 = "F3",
            KEYMAP_KEY_F4 = "F4",
            KEYMAP_KEY_F5 = "F5",
            KEYMAP_KEY_F6 = "F6",
            KEYMAP_KEY_F7 = "F7",
            KEYMAP_KEY_F8 = "F8",
            KEYMAP_KEY_F9 = "F9",
            KEYMAP_KEY_F10 = "F10",
            KEYMAP_KEY_F11 = "F11",
            KEYMAP_KEY_F12 = "F12",
            KEYMAP_KEY_TAB = "TAB",
            KEYMAP_KEY_BACKSPACE = "BACK",
            KEYMAP_KEY_SPACE = "SPACE",
            KEYMAP_KEY_CAPITAL = "CAPITAL", //Caps Lock
            KEYMAP_KEY_ENTER = "ENTER",
            KEYMAP_KEY_LSHIFT = "LSHIFT",
            KEYMAP_KEY_LCTRL = "LCTRL",
            KEYMAP_KEY_LALT = "LALT",
            KEYMAP_KEY_RSHIFT = "RSHIFT",
            KEYMAP_KEY_RCTRL = "RCTRL",
            KEYMAP_KEY_RALT = "RALT",
            KEYMAP_KEY_UP = "UP",
            KEYMAP_KEY_DOWN = "DOWN",
            KEYMAP_KEY_LEFT = "LEFT",
            KEYMAP_KEY_RIGHT = "RIGHT",
            KEYMAP_KEY_PAGEUP = "PGUP",
            KEYMAP_KEY_PAGEDOWN = "PGDN",
            KEYMAP_KEY_HOME = "HOME",
            KEYMAP_KEY_END = "END",
            KEYMAP_KEY_INSERT = "INSERT",
            KEYMAP_KEY_DELETE = "DELETE",
            KEYMAP_KEY_PAUSE = "PAUSE", // pause & break
            KEYMAP_KEY_PRINT = "PRINT",
            KEYMAP_KEY_LWIN = "LWIN",
            KEYMAP_KEY_RWIN = "RWIN",
            KEYMAP_KEY_RIGHTK = "RIGHTK", // Sec Button in Mouse, how to achieve this in Keyboard?

    /* keys on numeric keypad */
    KEYMAP_KEY_NUMPAD0 = "NUMPAD0",
            KEYMAP_KEY_NUMPAD1 = "NUMPAD1",
            KEYMAP_KEY_NUMPAD2 = "NUMPAD2",
            KEYMAP_KEY_NUMPAD3 = "NUMPAD3",
            KEYMAP_KEY_NUMPAD4 = "NUMPAD4",
            KEYMAP_KEY_NUMPAD5 = "NUMPAD5",
            KEYMAP_KEY_NUMPAD6 = "NUMPAD6",
            KEYMAP_KEY_NUMPAD7 = "NUMPAD7",
            KEYMAP_KEY_NUMPAD8 = "NUMPAD8",
            KEYMAP_KEY_NUMPAD9 = "NUMPAD9",
            KEYMAP_KEY_NUMLOCK = "NUMLOCK",  //Num Lock
            KEYMAP_KEY_SCROLL = "SCROLL",  //Scroll Lock
            KEYMAP_KEY_SUBTRACT = "NUMPAD-",  // -
            KEYMAP_KEY_ADD = "NUMPAD+",  // +
            KEYMAP_KEY_DECIMAL = "NUMPAD.",  // .
            KEYMAP_KEY_NUMPADENTER = "NUMPADENTER",  // Enter on numeric keypad
            KEYMAP_KEY_DIVIDE = "NUMPAD\\", // /
            KEYMAP_KEY_MULTIPLY = "NUMPAD*", // *

    /* keys on stand gamepad */
    KEYMAP_BUTTON_1 = "BUTTON_1",
            KEYMAP_BUTTON_2 = "BUTTON_2",
            KEYMAP_BUTTON_3 = "BUTTON_3",
            KEYMAP_BUTTON_4 = "BUTTON_4",
            KEYMAP_BUTTON_5 = "BUTTON_5",
            KEYMAP_BUTTON_6 = "BUTTON_6",
            KEYMAP_BUTTON_7 = "BUTTON_7",
            KEYMAP_BUTTON_8 = "BUTTON_8",
            KEYMAP_BUTTON_9 = "BUTTON_9",
            KEYMAP_BUTTON_10 = "BUTTON_10",
            KEYMAP_BUTTON_11 = "BUTTON_11",
            KEYMAP_BUTTON_12 = "BUTTON_12",
            KEYMAP_BUTTON_13 = "BUTTON_13",
            KEYMAP_BUTTON_14 = "BUTTON_14",
            KEYMAP_BUTTON_15 = "BUTTON_15",
            KEYMAP_BUTTON_16 = "BUTTON_16",
            KEYMAP_BUTTON_A = "BUTTON_A",
            KEYMAP_BUTTON_B = "BUTTON_B",
            KEYMAP_BUTTON_C = "BUTTON_C",
            KEYMAP_BUTTON_X = "BUTTON_X",
            KEYMAP_BUTTON_Y = "BUTTON_Y",
            KEYMAP_BUTTON_Z = "BUTTON_Z",
            KEYMAP_BUTTON_L1 = "BUTTON_L1",
            KEYMAP_BUTTON_L2 = "BUTTON_L2",
            KEYMAP_BUTTON_R1 = "BUTTON_R1",
            KEYMAP_BUTTON_R2 = "BUTTON_R2",
            KEYMAP_BUTTON_MODE = "BUTTON_MODE",
            KEYMAP_BUTTON_SELECT = "BUTTON_SELECT",
            KEYMAP_BUTTON_START = "BUTTON_START",
            KEYMAP_BUTTON_THUMBL = "BUTTON_THUMBL",
            KEYMAP_BUTTON_THUMBR = "BUTTON_THUMBR";

}
