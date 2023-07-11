package com.aof.mcinabox.gamecontroller.codes;

public class Translation {

    private final CoKeyMap xKeyMap;

    public Translation(int mode) {
        xKeyMap = new XKeyMap();
    }

    public int trans(String s) {
        return  (int) xKeyMap.translate(s);
    }

}
