package com.tungsten.fclcore.download.game;

import com.google.gson.annotations.SerializedName;

public final class GameRemoteLatestVersions {

    @SerializedName("snapshot")
    private final String snapshot;

    @SerializedName("release")
    private final String release;

    public GameRemoteLatestVersions() {
        this(null, null);
    }

    public GameRemoteLatestVersions(String snapshot, String release) {
        this.snapshot = snapshot;
        this.release = release;
    }

    public String getRelease() {
        return release;
    }

    public String getSnapshot() {
        return snapshot;
    }
}
