package com.tungsten.fcl.control;

public enum MouseMoveMode {
    CLICK(0),
    SLIDE(1);

    private final int id;

    MouseMoveMode(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MouseMoveMode getById(int id) {
        return id == 0 ? CLICK : SLIDE;
    }
}
