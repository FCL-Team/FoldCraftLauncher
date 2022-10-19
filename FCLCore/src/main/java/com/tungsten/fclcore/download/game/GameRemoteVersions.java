package com.tungsten.fclcore.download.game;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.util.gson.Validation;

import java.util.Collections;
import java.util.List;

public final class GameRemoteVersions implements Validation {

    @SerializedName("versions")
    private final List<GameRemoteVersionInfo> versions;

    @SerializedName("latest")
    private final GameRemoteLatestVersions latest;

    /**
     * No-arg constructor for Gson.
     */
    @SuppressWarnings("unused")
    public GameRemoteVersions() {
        this(Collections.emptyList(), null);
    }

    public GameRemoteVersions(List<GameRemoteVersionInfo> versions, GameRemoteLatestVersions latest) {
        this.versions = versions;
        this.latest = latest;
    }

    public GameRemoteLatestVersions getLatest() {
        return latest;
    }

    public List<GameRemoteVersionInfo> getVersions() {
        return versions;
    }

    @Override
    public void validate() throws JsonParseException {
        if (versions == null)
            throw new JsonParseException("GameRemoteVersions.versions cannot be null");
    }
}
