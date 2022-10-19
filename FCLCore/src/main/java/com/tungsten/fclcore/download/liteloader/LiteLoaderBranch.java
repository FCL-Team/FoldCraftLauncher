package com.tungsten.fclcore.download.liteloader;

import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.game.Library;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public final class LiteLoaderBranch {

    @SerializedName("libraries")
    private final Collection<Library> libraries;

    @SerializedName("com.mumfrey:liteloader")
    private final Map<String, LiteLoaderVersion> liteLoader;

    /**
     * No-arg constructor for Gson.
     */
    @SuppressWarnings("unused")
    public LiteLoaderBranch() {
        this(Collections.emptySet(), Collections.emptyMap());
    }

    public LiteLoaderBranch(Collection<Library> libraries, Map<String, LiteLoaderVersion> liteLoader) {
        this.libraries = libraries;
        this.liteLoader = liteLoader;
    }

    public Collection<Library> getLibraries() {
        return Collections.unmodifiableCollection(libraries);
    }

    public Map<String, LiteLoaderVersion> getLiteLoader() {
        return Collections.unmodifiableMap(liteLoader);
    }

}
