package com.tungsten.fclcore.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.tungsten.fclcore.game.GameComponentType;

import org.junit.Test;

import java.net.URL;
import java.util.List;

public class BMCLAPIDownloadProviderTest {

    private final BMCLAPIDownloadProvider provider = new BMCLAPIDownloadProvider("https://bmclapi2.bangbang93.com");

    @Test
    public void injectURLRewritesMojangMeta() {
        assertEquals("https://bmclapi2.bangbang93.com/mc/game/version_manifest.json",
                provider.injectURL("https://piston-meta.mojang.com/mc/game/version_manifest.json"));
        assertEquals("https://bmclapi2.bangbang93.com/libraries/net/minecraftforge/forge.jar",
                provider.injectURL("https://libraries.minecraft.net/net/minecraftforge/forge.jar"));
    }

    @Test
    public void injectURLKeepsUnmatchedURL() {
        assertEquals("https://example.org/some/file.json",
                provider.injectURL("https://example.org/some/file.json"));
    }

    @Test
    public void modrinthCandidatesPreferOfficialThenMirror() {
        List<URL> candidates = provider.injectURLWithCandidates("https://api.modrinth.com/v2/search?query=sodium");

        assertEquals(2, candidates.size());
        assertEquals("https://api.modrinth.com/v2/search?query=sodium", candidates.get(0).toString());
        assertEquals("https://mod.mcimirror.top/modrinth/v2/search?query=sodium", candidates.get(1).toString());
    }

    @Test
    public void curseForgeCandidatesPreferOfficialThenMirror() {
        List<URL> candidates = provider.injectURLWithCandidates("https://api.curseforge.com/v1/mods/search");

        assertEquals(2, candidates.size());
        assertEquals("https://api.curseforge.com/v1/mods/search", candidates.get(0).toString());
        assertEquals("https://mod.mcimirror.top/curseforge/v1/mods/search", candidates.get(1).toString());
    }

    @Test
    public void rewrittenURLHasSingleCandidate() {
        List<URL> candidates = provider.injectURLWithCandidates("https://meta.fabricmc.net/v2/versions/loader");

        assertEquals(1, candidates.size());
        assertEquals("https://bmclapi2.bangbang93.com/fabric-meta/v2/versions/loader", candidates.get(0).toString());
    }

    @Test
    public void injectURLsWithCandidatesDeduplicates() {
        List<URL> candidates = provider.injectURLsWithCandidates(List.of(
                "https://api.modrinth.com/v2/search",
                "https://api.modrinth.com/v2/search"));

        assertEquals(2, candidates.size());
    }

    @Test
    public void fmllibsMirrorPairConfigured() {
        assertEquals("https://alist.8mi.tech/d/mirror/HMCL-Metadata/Auto/fmllibs",
                provider.injectURL("https://hmcl.glavo.site/metadata/fmllibs"));
    }

    @Test
    public void versionListAndAssetURLsUseApiRoot() {
        assertEquals("https://bmclapi2.bangbang93.com/mc/game/version_manifest.json",
                provider.getVersionListURLs().get(0).toString());
        assertEquals("https://bmclapi2.bangbang93.com/assets/objects/ab/cdabcdef",
                provider.getAssetObjectCandidates("objects/ab/cdabcdef").get(0).toString());
    }

    @Test
    public void getVersionListCoversAllComponentTypes() {
        for (GameComponentType type : GameComponentType.ALL) {
            ComponentVersionList<?> list = provider.getVersionList(type);
            assertNotNull(list);
            assertSame(list, provider.getVersionList(type));
        }
    }

    @Test
    public void concurrencyAtLeastCpuCount() {
        assertTrue(provider.getConcurrency() >= Runtime.getRuntime().availableProcessors());
    }
}
