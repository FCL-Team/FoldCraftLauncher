package com.tungsten.fclcore.game;

public enum ReleaseType {
    RELEASE("release"),
    SNAPSHOT("snapshot"),
    MODIFIED("modified"),
    OLD_BETA("old-beta"),
    OLD_ALPHA("old-alpha"),
    UNKNOWN("unknown");

    private final String id;

    ReleaseType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
