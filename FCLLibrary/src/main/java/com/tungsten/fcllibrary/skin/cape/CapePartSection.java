package com.tungsten.fcllibrary.skin.cape;

public class CapePartSection {

    String capePartName;
    int height;
    String sideName;
    int startX;
    int startY;
    int width;

    public CapePartSection(final String capePartName, final String sideName, final int startX, final int startY, final int width, final int height) {
        this.capePartName = capePartName;
        this.sideName = sideName;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    public String getCapePartName() {
        return this.capePartName;
    }

    public int getHeight() {
        return this.height;
    }

    public String getSideName() {
        return this.sideName;
    }

    public int getStartX() {
        return this.startX;
    }

    public int getStartY() {
        return this.startY;
    }

    public int getWidth() {
        return this.width;
    }

}
