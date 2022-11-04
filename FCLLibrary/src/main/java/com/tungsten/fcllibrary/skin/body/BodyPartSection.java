package com.tungsten.fcllibrary.skin.body;

public class BodyPartSection {
    String bodyPartName;
    int height;
    String sideName;
    int startX;
    int startY;
    int width;
    
    public BodyPartSection(final String bodyPartName, final String sideName, final int startX, final int startY, final int width, final int height) {
        this.bodyPartName = bodyPartName;
        this.sideName = sideName;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }
    
    public String getBodyPartName() {
        return this.bodyPartName;
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
