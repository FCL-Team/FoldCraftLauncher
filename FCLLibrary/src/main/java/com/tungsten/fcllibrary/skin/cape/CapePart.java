package com.tungsten.fcllibrary.skin.cape;

public enum CapePart {

    CAPE(0, "Cape", new CapePartSection[] { CapeSkin.FRONT.getCapePartSection(), CapeSkin.RIGHT.getCapePartSection(), CapeSkin.BACK.getCapePartSection(), CapeSkin.LEFT.getCapePartSection(), CapeSkin.TOP.getCapePartSection(), CapeSkin.BOTTOM.getCapePartSection() });

    private CapePartSection[] capeParts;
    private String displayName;
    private int id;

    private CapePart(final int id, final String displayName, final CapePartSection[] capeParts) {
        this.id = id;
        this.displayName = displayName;
        this.capeParts = capeParts;
    }

    CapePartSection getCapePartSection(final int n) {
        return this.capeParts[n];
    }

    String getDisplayName() {
        return this.displayName;
    }

    int getPartId() {
        return this.id;
    }

    public enum CapeSkin
    {
        BACK("BACK", 2, 2, "Back", 1, 1, 10, 16),
        BOTTOM("BOTTOM", 5, 5, "Bottom", 11, 0, 10, 1),
        FRONT("FRONT", 0, 0, "Front", 12, 1, 10, 16),
        LEFT("LEFT", 3, 3, "Left", 0, 1, 1, 16),
        RIGHT("RIGHT", 1, 1, "Right", 11, 1, 1, 16),
        TOP("TOP", 4, 4, "Top", 1, 0, 10, 1);

        private String displayName;
        private int height;
        private int id;
        private int startX;
        private int startY;
        private int width;

        private CapeSkin(final String s2, final int n2, final int id, final String displayName, final int startX, final int startY, final int width, final int height) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.displayName = displayName;
            this.startX = startX;
            this.startY = startY;
        }

        CapePartSection getCapePartSection() {
            return new CapePartSection("Cape", this.displayName, this.startX, this.startY, this.width, this.height);
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
