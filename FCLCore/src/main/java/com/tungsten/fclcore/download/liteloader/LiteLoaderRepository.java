package com.tungsten.fclcore.download.liteloader;

import com.google.gson.annotations.SerializedName;

public final class LiteLoaderRepository {

    @SerializedName("stream")
    private final String stream;

    @SerializedName("type")
    private final String type;

    @SerializedName("url")
    private final String url;

    @SerializedName("classifier")
    private final String classifier;

    /**
     * No-arg constructor for Gson.
     */
    @SuppressWarnings("unused")
    public LiteLoaderRepository() {
        this("", "", "", "");
    }

    public LiteLoaderRepository(String stream, String type, String url, String classifier) {
        this.stream = stream;
        this.type = type;
        this.url = url;
        this.classifier = classifier;
    }

    public String getStream() {
        return stream;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getClassifier() {
        return classifier;
    }

}
