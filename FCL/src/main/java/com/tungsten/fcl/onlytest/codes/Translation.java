package com.tungsten.fcl.onlytest.codes;

import static com.tungsten.fcl.onlytest.definitions.id.key.KeyEvent.*;

public class Translation {

    private final CoKeyMap xKeyMap;
    private final CoKeyMap aKeyMap;
    private int mode;

    public Translation(int mode) {
        xKeyMap = new XKeyMap();
        aKeyMap = new AndroidKeyMap();
        this.mode = mode;
    }

    public int trans(String s) {
        switch (mode) {
            case KEYMAP_TO_X:
                return (int) xKeyMap.translate(s);
            default:
                return -1;
        }
    }

    public String trans(int i) {
        switch (mode) {
            case ANDROID_TO_KEYMAP:
                return (String) aKeyMap.translate(i);
            default:
                return null;
        }
    }

    public Translation setMode(int mode) {
        this.mode = mode;
        return this;
    }
}
