package com.tungsten.fclcore.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏组件类型，即版本列表的标识（"game"、"forge"、"fabric" 等），同时对应版本补丁的 patch id。
 */
public enum GameComponentType {
    GAME("game"),
    LEGACY_FABRIC("legacyfabric"),
    LEGACY_FABRIC_API("legacyfabric-api"),
    FABRIC("fabric"),
    FABRIC_API("fabric-api"),
    FORGE("forge"),
    CLEANROOM("cleanroom"),
    NEO_FORGE("neoforge"),
    LITELOADER("liteloader"),
    OPTIFINE("optifine"),
    QUILT("quilt"),
    QUILT_API("quilt-api");

    public static final List<GameComponentType> ALL = List.of(GameComponentType.values());

    private final String patchId;

    private static final Map<String, GameComponentType> PATCH_ID_MAP = new HashMap<>();

    static {
        for (GameComponentType type : values()) {
            PATCH_ID_MAP.put(type.getPatchId(), type);
        }
    }

    GameComponentType(String patchId) {
        this.patchId = patchId;
    }

    public String getPatchId() {
        return patchId;
    }

    public static GameComponentType fromPatchId(String patchId) {
        return PATCH_ID_MAP.get(patchId);
    }
}
