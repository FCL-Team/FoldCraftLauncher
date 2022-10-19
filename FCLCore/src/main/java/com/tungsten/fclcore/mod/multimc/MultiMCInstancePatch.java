package com.tungsten.fclcore.mod.multimc;

import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.util.Lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MultiMCInstancePatch {

    private final String name;
    private final String version;

    @SerializedName("mcVersion")
    private final String gameVersion;
    private final String mainClass;
    private final String fileId;

    @SerializedName("+tweakers")
    private final List<String> tweakers;

    @SerializedName("+libraries")
    private final List<Library> _libraries;

    @SerializedName("libraries")
    private final List<Library> libraries;

    public MultiMCInstancePatch() {
        this("", "", "", "", "", Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public MultiMCInstancePatch(String name, String version, String gameVersion, String mainClass, String fileId, List<String> tweakers, List<Library> _libraries, List<Library> libraries) {
        this.name = name;
        this.version = version;
        this.gameVersion = gameVersion;
        this.mainClass = mainClass;
        this.fileId = fileId;
        this.tweakers = new ArrayList<>(tweakers);
        this._libraries = new ArrayList<>(_libraries);
        this.libraries = new ArrayList<>(libraries);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getFileId() {
        return fileId;
    }

    public List<String> getTweakers() {
        return Collections.unmodifiableList(tweakers);
    }

    public List<Library> getLibraries() {
        return Lang.merge(_libraries, libraries);
    }

}
