package com.aof.mcinabox.gamecontroller.definitions.id.key;

public interface KeyEvent {
    //Define Type ID
    int KEYBOARD_BUTTON = 11;
    int MOUSE_BUTTON = 12;
    int MOUSE_POINTER = 13;
    int MOUSE_POINTER_INC = 15;
    int TYPE_WORDS = 14;

    int KEYMAP_TO_LWJGL = 21;
    int KEYMAP_TO_GLFW = 22;
    int KEYMAP_TO_X = 23;
    int ANDROID_TO_KEYMAP = 24;

    String MARK_KEYNAME_SPLIT = "\\|";
    String MARK_KEYNAME_SPLIT_STRING = "|";
}
