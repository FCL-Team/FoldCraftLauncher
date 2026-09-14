package com.tungsten.fclcore.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GameComponentTypeTest {

    @Test
    public void fromPatchIdRoundTrip() {
        for (GameComponentType type : GameComponentType.ALL) {
            assertEquals(type, GameComponentType.fromPatchId(type.getPatchId()));
        }
    }

    @Test
    public void fromPatchIdCoversLibraryAnalyzerIds() {
        // LibraryAnalyzer.LibraryType 的 patch id 必须能在 GameComponentType 中找到，
        // DefaultDependencyManager.installLibraryAsync 依赖该映射
        assertEquals(GameComponentType.GAME, GameComponentType.fromPatchId("game"));
        assertEquals(GameComponentType.FORGE, GameComponentType.fromPatchId("forge"));
        assertEquals(GameComponentType.NEO_FORGE, GameComponentType.fromPatchId("neoforge"));
        assertEquals(GameComponentType.OPTIFINE, GameComponentType.fromPatchId("optifine"));
        assertEquals(GameComponentType.LITELOADER, GameComponentType.fromPatchId("liteloader"));
        assertEquals(GameComponentType.FABRIC, GameComponentType.fromPatchId("fabric"));
        assertEquals(GameComponentType.FABRIC_API, GameComponentType.fromPatchId("fabric-api"));
        assertEquals(GameComponentType.QUILT, GameComponentType.fromPatchId("quilt"));
        assertEquals(GameComponentType.QUILT_API, GameComponentType.fromPatchId("quilt-api"));
        assertEquals(GameComponentType.CLEANROOM, GameComponentType.fromPatchId("cleanroom"));
        assertEquals(GameComponentType.LEGACY_FABRIC, GameComponentType.fromPatchId("legacyfabric"));
        assertEquals(GameComponentType.LEGACY_FABRIC_API, GameComponentType.fromPatchId("legacyfabric-api"));
    }

    @Test
    public void fromPatchIdReturnsNullForUnknown() {
        assertNull(GameComponentType.fromPatchId("unknown"));
        assertNull(GameComponentType.fromPatchId(null));
    }
}
