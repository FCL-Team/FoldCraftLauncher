package com.tungsten.fclcore.auth.yggdrasil;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class Texture {

    private final String url;
    private final Map<String, String> metadata;

    public Texture() {
        this(null, null);
    }

    public Texture(String url, Map<String, String> metadata) {
        this.url = url;
        this.metadata = metadata;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @Nullable
    public Map<String, String> getMetadata() {
        return metadata;
    }
}
