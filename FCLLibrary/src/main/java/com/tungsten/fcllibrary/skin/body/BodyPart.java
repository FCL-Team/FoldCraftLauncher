package com.tungsten.fcllibrary.skin.body;

public enum BodyPart {
    BODY(2, "Body", new BodyPartSection[] { BodySkin.FRONT.getBodyPartSection(), BodySkin.RIGHT.getBodyPartSection(), BodySkin.BACK.getBodyPartSection(), BodySkin.LEFT.getBodyPartSection(), BodySkin.TOP.getBodyPartSection(), BodySkin.BOTTOM.getBodyPartSection() }), 
    HAT(1, "Hat", new BodyPartSection[] { HatSkin.FRONT.getBodyPartSection(), HatSkin.RIGHT.getBodyPartSection(), HatSkin.BACK.getBodyPartSection(), HatSkin.LEFT.getBodyPartSection(), HatSkin.TOP.getBodyPartSection(), HatSkin.BOTTOM.getBodyPartSection() }), 
    HEAD(0, "Head", new BodyPartSection[] { HeadSkin.FRONT.getBodyPartSection(), HeadSkin.RIGHT.getBodyPartSection(), HeadSkin.BACK.getBodyPartSection(), HeadSkin.LEFT.getBodyPartSection(), HeadSkin.TOP.getBodyPartSection(), HeadSkin.BOTTOM.getBodyPartSection() }), 
    JACKET(3, "Body Overlay", new BodyPartSection[] { JacketSkin.FRONT.getBodyPartSection(), JacketSkin.RIGHT.getBodyPartSection(), JacketSkin.BACK.getBodyPartSection(), JacketSkin.LEFT.getBodyPartSection(), JacketSkin.TOP.getBodyPartSection(), JacketSkin.BOTTOM.getBodyPartSection() }), 
    LEFT_ARM(4, "Left Arm", new BodyPartSection[] { LeftArmSkin.FRONT.getBodyPartSection(), LeftArmSkin.RIGHT.getBodyPartSection(), LeftArmSkin.BACK.getBodyPartSection(), LeftArmSkin.LEFT.getBodyPartSection(), LeftArmSkin.TOP.getBodyPartSection(), LeftArmSkin.BOTTOM.getBodyPartSection() }), 
    LEFT_LEG(8, "Left Leg", new BodyPartSection[] { LeftLegSkin.FRONT.getBodyPartSection(), LeftLegSkin.RIGHT.getBodyPartSection(), LeftLegSkin.BACK.getBodyPartSection(), LeftLegSkin.LEFT.getBodyPartSection(), LeftLegSkin.TOP.getBodyPartSection(), LeftLegSkin.BOTTOM.getBodyPartSection() }), 
    LEFT_LEG_OVERLAY(10, "Left Leg Overlay", new BodyPartSection[] { LeftLegOverlaySkin.FRONT.getBodyPartSection(), LeftLegOverlaySkin.RIGHT.getBodyPartSection(), LeftLegOverlaySkin.BACK.getBodyPartSection(), LeftLegOverlaySkin.LEFT.getBodyPartSection(), LeftLegOverlaySkin.TOP.getBodyPartSection(), LeftLegOverlaySkin.BOTTOM.getBodyPartSection() }), 
    LEFT_SLEEVE(6, "Left Arm Overlay", new BodyPartSection[] { LeftSleeveSkin.FRONT.getBodyPartSection(), LeftSleeveSkin.RIGHT.getBodyPartSection(), LeftSleeveSkin.BACK.getBodyPartSection(), LeftSleeveSkin.LEFT.getBodyPartSection(), LeftSleeveSkin.TOP.getBodyPartSection(), LeftSleeveSkin.BOTTOM.getBodyPartSection() }), 
    RIGHT_ARM(5, "Right Arm", new BodyPartSection[] { RightArmSkin.FRONT.getBodyPartSection(), RightArmSkin.RIGHT.getBodyPartSection(), RightArmSkin.BACK.getBodyPartSection(), RightArmSkin.LEFT.getBodyPartSection(), RightArmSkin.TOP.getBodyPartSection(), RightArmSkin.BOTTOM.getBodyPartSection() }), 
    RIGHT_LEG(9, "Right Leg", new BodyPartSection[] { RightLegSkin.FRONT.getBodyPartSection(), RightLegSkin.RIGHT.getBodyPartSection(), RightLegSkin.BACK.getBodyPartSection(), RightLegSkin.LEFT.getBodyPartSection(), RightLegSkin.TOP.getBodyPartSection(), RightLegSkin.BOTTOM.getBodyPartSection() }), 
    RIGHT_LEG_OVERLAY(11, "Right Leg Overlay", new BodyPartSection[] { RightLegOverlaySkin.FRONT.getBodyPartSection(), RightLegOverlaySkin.RIGHT.getBodyPartSection(), RightLegOverlaySkin.BACK.getBodyPartSection(), RightLegOverlaySkin.LEFT.getBodyPartSection(), RightLegOverlaySkin.TOP.getBodyPartSection(), RightLegOverlaySkin.BOTTOM.getBodyPartSection() }), 
    RIGHT_SLEEVE(7, "Right Arm Overlay", new BodyPartSection[] { RightSleeveSkin.FRONT.getBodyPartSection(), RightSleeveSkin.RIGHT.getBodyPartSection(), RightSleeveSkin.BACK.getBodyPartSection(), RightSleeveSkin.LEFT.getBodyPartSection(), RightSleeveSkin.TOP.getBodyPartSection(), RightSleeveSkin.BOTTOM.getBodyPartSection() });
    
    private BodyPartSection[] bodyParts;
    private String displayName;
    private int id;
    
    private BodyPart(final int id, final String displayName, final BodyPartSection[] bodyParts) {
        this.id = id;
        this.displayName = displayName;
        this.bodyParts = bodyParts;
    }
    
    BodyPartSection getBodyPartSection(final int n) {
        return this.bodyParts[n];
    }
    
    String getDisplayName() {
        return this.displayName;
    }
    
    int getPartId() {
        return this.id;
    }
    
    public enum AlexLeftArmSkin {
        BACK(2, "Back", 51, 20, 3, 12), 
        BOTTOM(5, "Bottom", 47, 16, 3, 4), 
        FRONT(0, "Front", 44, 20, 3, 12), 
        LEFT(3, "Left", 47, 20, 4, 12), 
        RIGHT(1, "Right", 40, 20, 4, 12), 
        TOP(4, "Top", 44, 16, 3, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        AlexLeftArmSkin(final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Left Arm", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum AlexLeftSleeveSkin {
        BACK(2, "Back", 51, 36, 3, 12), 
        BOTTOM(5, "Bottom", 47, 32, 3, 4), 
        FRONT(0, "Front", 44, 36, 3, 12), 
        LEFT(3, "Left", 47, 36, 4, 12), 
        RIGHT(1, "Right", 40, 36, 4, 12), 
        TOP(4, "Top", 44, 32, 3, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        AlexLeftSleeveSkin(final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Left Arm Overlay", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum AlexRightArmSkin {
        BACK(2, "Back", 43, 52, 3, 12), 
        BOTTOM(5, "Bottom", 39, 48, 3, 4), 
        FRONT(0, "Front", 36, 52, 3, 12), 
        LEFT(3, "Left", 39, 52, 4, 12), 
        RIGHT(1, "Right", 32, 52, 4, 12), 
        TOP(4, "Top", 36, 48, 3, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        AlexRightArmSkin(final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Right Arm", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum AlexRightSleeveSkin {
        BACK(2, "Back", 59, 52, 3, 12), 
        BOTTOM(5, "Bottom", 55, 48, 3, 4), 
        FRONT(0, "Front", 52, 52, 3, 12), 
        LEFT(3, "Left", 55, 52, 4, 12), 
        RIGHT(1, "Right", 48, 52, 4, 12), 
        TOP(4, "Top", 52, 48, 3, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        AlexRightSleeveSkin(final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Right Arm Overlay", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum BodySkin {
        BACK("BACK", 2, 2, "Back", 32, 20, 8, 12), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 28, 14, 8, 4), 
        FRONT("FRONT", 0, 0, "Front", 20, 20, 8, 12), 
        LEFT("LEFT", 3, 3, "Left", 28, 20, 4, 12), 
        RIGHT("RIGHT", 1, 1, "Right", 16, 20, 4, 12), 
        TOP("TOP", 4, 4, "Top", 20, 14, 8, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        BodySkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Body", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum HatSkin {
        BACK("BACK", 2, 2, "Back", 56, 8, 8, 8), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 48, 0, 8, 8), 
        FRONT("FRONT", 0, 0, "Front", 40, 8, 8, 8), 
        LEFT("LEFT", 3, 3, "Left", 48, 8, 8, 8), 
        RIGHT("RIGHT", 1, 1, "Right", 32, 8, 8, 8), 
        TOP("TOP", 4, 4, "Top", 40, 0, 8, 8);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        HatSkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Hat", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum HeadSkin {
        BACK("BACK", 2, 2, "Back", 24, 8, 8, 8), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 16, 0, 8, 8), 
        FRONT("FRONT", 0, 0, "Front", 8, 8, 8, 8), 
        LEFT("LEFT", 3, 3, "Left", 16, 8, 8, 8), 
        RIGHT("RIGHT", 1, 1, "Right", 0, 8, 8, 8), 
        TOP("TOP", 4, 4, "Top", 8, 0, 8, 8);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        HeadSkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Head", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum JacketSkin {
        BACK("BACK", 2, 2, "Back", 32, 36, 8, 12), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 28, 32, 8, 4), 
        FRONT("FRONT", 0, 0, "Front", 20, 36, 8, 12), 
        LEFT("LEFT", 3, 3, "Left", 28, 36, 4, 12), 
        RIGHT("RIGHT", 1, 1, "Right", 16, 36, 4, 12), 
        TOP("TOP", 4, 4, "Top", 20, 32, 8, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        JacketSkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Body Overlay", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum LeftArmSkin {
        BACK("BACK", 2, 2, "Back", 44, 52, 4, 12), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 40, 48, 4, 4), 
        FRONT("FRONT", 0, 0, "Front", 36, 52, 4, 12), 
        LEFT("LEFT", 3, 3, "Left", 40, 52, 4, 12), 
        RIGHT("RIGHT", 1, 1, "Right", 32, 52, 4, 12), 
        TOP("TOP", 4, 4, "Top", 36, 48, 4, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        LeftArmSkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Left Arm", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum LeftLegOverlaySkin {
        BACK("BACK", 2, 2, "Back", 12, 52, 4, 12), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 8, 48, 4, 4), 
        FRONT("FRONT", 0, 0, "Front", 4, 52, 4, 12), 
        LEFT("LEFT", 3, 3, "Left", 8, 52, 4, 12), 
        RIGHT("RIGHT", 1, 1, "Right", 0, 52, 4, 12), 
        TOP("TOP", 4, 4, "Top", 4, 48, 4, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        LeftLegOverlaySkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Left Leg Overlay", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum LeftLegSkin {
        BACK("BACK", 2, 2, "Back", 28, 52, 4, 12), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 24, 48, 4, 4), 
        FRONT("FRONT", 0, 0, "Front", 20, 52, 4, 12), 
        LEFT("LEFT", 3, 3, "Left", 24, 52, 4, 12), 
        RIGHT("RIGHT", 1, 1, "Right", 16, 52, 4, 12), 
        TOP("TOP", 4, 4, "Top", 20, 48, 4, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        LeftLegSkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Left Leg", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum LeftSleeveSkin {
        BACK("BACK", 2, 2, "Back", 60, 52, 4, 12), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 56, 48, 4, 4), 
        FRONT("FRONT", 0, 0, "Front", 52, 52, 4, 12), 
        LEFT("LEFT", 3, 3, "Left", 56, 52, 4, 12), 
        RIGHT("RIGHT", 1, 1, "Right", 48, 52, 4, 12), 
        TOP("TOP", 4, 4, "Top", 52, 48, 4, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        LeftSleeveSkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Left Arm Overlay", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum RightArmSkin {
        BACK("BACK", 2, 2, "Back", 52, 20, 4, 12), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 48, 16, 4, 4), 
        FRONT("FRONT", 0, 0, "Front", 44, 20, 4, 12), 
        LEFT("LEFT", 3, 3, "Left", 48, 20, 4, 12), 
        RIGHT("RIGHT", 1, 1, "Right", 40, 20, 4, 12), 
        TOP("TOP", 4, 4, "Top", 44, 16, 4, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        RightArmSkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Right Arm", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum RightLegOverlaySkin {
        BACK("BACK", 2, 2, "Back", 12, 36, 4, 12), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 8, 32, 4, 4), 
        FRONT("FRONT", 0, 0, "Front", 4, 36, 4, 12), 
        LEFT("LEFT", 3, 3, "Left", 8, 36, 4, 12), 
        RIGHT("RIGHT", 1, 1, "Right", 0, 36, 4, 12), 
        TOP("TOP", 4, 4, "Top", 4, 32, 4, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        RightLegOverlaySkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Right Leg Overlay", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum RightLegSkin {
        BACK("BACK", 2, 2, "Back", 12, 20, 4, 12), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 8, 16, 4, 4), 
        FRONT("FRONT", 0, 0, "Front", 4, 20, 4, 12), 
        LEFT("LEFT", 3, 3, "Left", 8, 20, 4, 12), 
        RIGHT("RIGHT", 1, 1, "Right", 0, 20, 4, 12), 
        TOP("TOP", 4, 4, "Top", 4, 16, 4, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        RightLegSkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Right Leg", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
    
    public enum RightSleeveSkin {
        BACK("BACK", 2, 2, "Back", 52, 36, 4, 12), 
        BOTTOM("BOTTOM", 5, 5, "Bottom", 48, 32, 4, 4), 
        FRONT("FRONT", 0, 0, "Front", 44, 36, 4, 12), 
        LEFT("LEFT", 3, 3, "Left", 48, 36, 4, 12), 
        RIGHT("RIGHT", 1, 1, "Right", 40, 36, 4, 12), 
        TOP("TOP", 4, 4, "Top", 44, 32, 4, 4);
        
        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;
        
        RightSleeveSkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }
        
        BodyPartSection getBodyPartSection() {
            return new BodyPartSection("Right Arm Overlay", this.displayName, this.startX, this.startY, this.width, this.height);
        }
        
        String getDisplayName() {
            return this.displayName;
        }
        
        int getHeight() {
            return this.height;
        }
        
        int getPartId() {
            return this.id;
        }
        
        int getStartX() {
            return this.startX;
        }
        
        int getStartY() {
            return this.startY;
        }
        
        int getWidth() {
            return this.width;
        }
    }
}
