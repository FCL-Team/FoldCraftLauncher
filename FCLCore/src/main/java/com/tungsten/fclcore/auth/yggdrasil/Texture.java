package com.tungsten.fclcore.auth.yggdrasil;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class Texture {

    private final String url;
    private final Map<String, String> metadata;
    @Expose(serialize = false)
    private final Bitmap img;

    public Texture() {
        this(null, null, null);
    }

    public Texture(String url, Map<String, String> metadata, Bitmap img) {
        this.url = url;
        this.metadata = metadata;
        this.img = img;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @Nullable
    public Map<String, String> getMetadata() {
        return metadata;
    }

    public Bitmap getImg() {
        return img;
    }
}
