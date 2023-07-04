package com.tungsten.fclcore.util.png;

public enum PNGType {
    GRAYSCALE(0, 1),
    RGB(2, 3),
    PALETTE(3, 1),
    GRAYSCALE_ALPHA(4, 2),
    RGBA(6, 4);

    final int id;
    final int cpp;

    PNGType(int id, int cpp) {
        this.id = id;
        this.cpp = cpp;
    }
}