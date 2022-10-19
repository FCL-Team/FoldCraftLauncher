package com.tungsten.fclcore.download.optifine;

import com.google.gson.annotations.SerializedName;

public final class OptiFineVersion {

    @SerializedName("dl")
    private final String downloadLink;

    @SerializedName("ver")
    private final String version;

    @SerializedName("date")
    private final String date;

    @SerializedName("type")
    private final String type;

    @SerializedName("patch")
    private final String patch;

    @SerializedName("mirror")
    private final String mirror;

    @SerializedName("mcversion")
    private final String gameVersion;

    public OptiFineVersion() {
        this(null, null, null, null, null, null, null);
    }

    public OptiFineVersion(String downloadLink, String version, String date, String type, String patch, String mirror, String gameVersion) {
        this.downloadLink = downloadLink;
        this.version = version;
        this.date = date;
        this.type = type;
        this.patch = patch;
        this.mirror = mirror;
        this.gameVersion = gameVersion;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getVersion() {
        return version;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getPatch() {
        return patch;
    }

    public String getMirror() {
        return mirror;
    }

    public String getGameVersion() {
        return gameVersion;
    }
}
