package com.tungsten.fclcore.mod.curse;

import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.mod.ModpackManifest;
import com.tungsten.fclcore.mod.ModpackProvider;

import java.util.Collections;
import java.util.List;

public final class CurseManifest implements ModpackManifest {

    @SerializedName("manifestType")
    private final String manifestType;

    @SerializedName("manifestVersion")
    private final int manifestVersion;

    @SerializedName("name")
    private final String name;

    @SerializedName("version")
    private final String version;

    @SerializedName("author")
    private final String author;

    @SerializedName("overrides")
    private final String overrides;

    @SerializedName("minecraft")
    private final CurseManifestMinecraft minecraft;

    @SerializedName("files")
    private final List<CurseManifestFile> files;

    public CurseManifest() {
        this(MINECRAFT_MODPACK, 1, "", "1.0", "", "overrides", new CurseManifestMinecraft(), Collections.emptyList());
    }

    public CurseManifest(String manifestType, int manifestVersion, String name, String version, String author, String overrides, CurseManifestMinecraft minecraft, List<CurseManifestFile> files) {
        this.manifestType = manifestType;
        this.manifestVersion = manifestVersion;
        this.name = name;
        this.version = version;
        this.author = author;
        this.overrides = overrides;
        this.minecraft = minecraft;
        this.files = files;
    }

    public String getManifestType() {
        return manifestType;
    }

    public int getManifestVersion() {
        return manifestVersion;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public String getOverrides() {
        return overrides;
    }

    public CurseManifestMinecraft getMinecraft() {
        return minecraft;
    }

    public List<CurseManifestFile> getFiles() {
        return files;
    }

    public CurseManifest setFiles(List<CurseManifestFile> files) {
        return new CurseManifest(manifestType, manifestVersion, name, version, author, overrides, minecraft, files);
    }

    @Override
    public ModpackProvider getProvider() {
        return CurseModpackProvider.INSTANCE;
    }

    public static final String MINECRAFT_MODPACK = "minecraftModpack";
}
