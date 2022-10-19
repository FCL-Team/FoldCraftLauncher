package com.tungsten.fclcore.mod.curse;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.Validation;

public final class CurseManifestModLoader implements Validation {

    @SerializedName("id")
    private final String id;

    @SerializedName("primary")
    private final boolean primary;

    public CurseManifestModLoader() {
        this("", false);
    }

    public CurseManifestModLoader(String id, boolean primary) {
        this.id = id;
        this.primary = primary;
    }

    public String getId() {
        return id;
    }

    public boolean isPrimary() {
        return primary;
    }

    @Override
    public void validate() throws JsonParseException {
        if (StringUtils.isBlank(id))
            throw new JsonParseException("Curse Forge modpack manifest Mod loader id cannot be blank.");
    }

}
