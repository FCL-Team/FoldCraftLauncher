package com.tungsten.fcl.control;

public enum GestureMode {
    BUILD(0),
    FIGHT(1);

    private final int id;

    GestureMode(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static GestureMode getById(int id) {
        return id == 0 ? BUILD : FIGHT;
    }
}
