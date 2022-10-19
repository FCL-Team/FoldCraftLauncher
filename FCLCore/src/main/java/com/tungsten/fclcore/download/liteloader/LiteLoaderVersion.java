package com.tungsten.fclcore.download.liteloader;

import com.tungsten.fclcore.game.Library;

import java.util.Collection;
import java.util.Collections;

public final class LiteLoaderVersion {
    private final String tweakClass;
    private final String file;
    private final String version;
    private final String md5;
    private final String timestamp;
    private final int lastSuccessfulBuild;
    private final Collection<Library> libraries;

    /**
     * No-arg constructor for Gson.
     */
    @SuppressWarnings("unused")
    public LiteLoaderVersion() {
        this("", "", "", "", "", 0, Collections.emptySet());
    }

    public LiteLoaderVersion(String tweakClass, String file, String version, String md5, String timestamp, int lastSuccessfulBuild, Collection<Library> libraries) {
        this.tweakClass = tweakClass;
        this.file = file;
        this.version = version;
        this.md5 = md5;
        this.timestamp = timestamp;
        this.lastSuccessfulBuild = lastSuccessfulBuild;
        this.libraries = libraries;
    }

    public String getTweakClass() {
        return tweakClass;
    }

    public String getFile() {
        return file;
    }

    public String getVersion() {
        return version;
    }

    public String getMd5() {
        return md5;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getLastSuccessfulBuild() {
        return lastSuccessfulBuild;
    }

    public Collection<Library> getLibraries() {
        return Collections.unmodifiableCollection(libraries);
    }
    
}
