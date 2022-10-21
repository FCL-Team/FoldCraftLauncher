package com.tungsten.fclcore.auth.yggdrasil;

import java.util.Map;
import java.util.UUID;

public enum TextureModel {
    STEVE("default"), ALEX("slim");

    public final String modelName;

    TextureModel(String modelName) {
        this.modelName = modelName;
    }

    public static TextureModel detectModelName(Map<String, String> metadata) {
        if (metadata != null && "slim".equals(metadata.get("model"))) {
            return ALEX;
        } else {
            return STEVE;
        }
    }

    public static TextureModel detectUUID(UUID uuid) {
        return (uuid.hashCode() & 1) == 1 ? ALEX : STEVE;
    }
}
