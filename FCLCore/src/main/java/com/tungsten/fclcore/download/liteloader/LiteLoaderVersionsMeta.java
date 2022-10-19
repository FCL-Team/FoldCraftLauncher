package com.tungsten.fclcore.download.liteloader;

import com.google.gson.annotations.SerializedName;

public final class LiteLoaderVersionsMeta {

    @SerializedName("description")
    private final String description;

    @SerializedName("authors")
    private final String authors;

    @SerializedName("url")
    private final String url;

    public LiteLoaderVersionsMeta() {
        this("", "", "");
    }

    public LiteLoaderVersionsMeta(String description, String authors, String url) {
        this.description = description;
        this.authors = authors;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthors() {
        return authors;
    }

    public String getUrl() {
        return url;
    }

}
