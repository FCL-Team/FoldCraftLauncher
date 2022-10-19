package com.tungsten.fclcore.mod.curse;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.Validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CurseManifestMinecraft implements Validation {

    @SerializedName("version")
    private final String gameVersion;

    @SerializedName("modLoaders")
    private final List<CurseManifestModLoader> modLoaders;

    public CurseManifestMinecraft() {
        this.gameVersion = "";
        this.modLoaders = Collections.emptyList();
    }

    public CurseManifestMinecraft(String gameVersion, List<CurseManifestModLoader> modLoaders) {
        this.gameVersion = gameVersion;
        this.modLoaders = new ArrayList<>(modLoaders);
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public List<CurseManifestModLoader> getModLoaders() {
        return Collections.unmodifiableList(modLoaders);
    }

    @Override
    public void validate() throws JsonParseException {
        if (StringUtils.isBlank(gameVersion))
            throw new JsonParseException("CurseForge Manifest.gameVersion cannot be blank.");
    }

}
