package com.tungsten.fclcore.download.liteloader;

import com.google.gson.annotations.SerializedName;

public final class LiteLoaderGameVersions {

    @SerializedName("repo")
    private final LiteLoaderRepository repoitory;

    @SerializedName("artefacts")
    private final LiteLoaderBranch artifacts;

    @SerializedName("snapshots")
    private final LiteLoaderBranch snapshots;

    /**
     * No-arg constructor for Gson.
     */
    @SuppressWarnings("unused")
    public LiteLoaderGameVersions() {
        this(null, null, null);
    }

    public LiteLoaderGameVersions(LiteLoaderRepository repoitory, LiteLoaderBranch artifacts, LiteLoaderBranch snapshots) {
        this.repoitory = repoitory;
        this.artifacts = artifacts;
        this.snapshots = snapshots;
    }

    public LiteLoaderRepository getRepoitory() {
        return repoitory;
    }

    public LiteLoaderBranch getArtifacts() {
        return artifacts;
    }

    public LiteLoaderBranch getSnapshots() {
        return snapshots;
    }

}
