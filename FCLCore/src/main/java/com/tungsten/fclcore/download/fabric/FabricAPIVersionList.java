package com.tungsten.fclcore.download.fabric;

import static com.tungsten.fclcore.util.Lang.wrap;

import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.download.VersionList;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.mod.modrinth.ModrinthRemoteModRepository;
import com.tungsten.fclcore.util.Lang;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class FabricAPIVersionList extends VersionList<FabricAPIRemoteVersion> {

    private final DownloadProvider downloadProvider;

    public FabricAPIVersionList(DownloadProvider downloadProvider) {
        this.downloadProvider = downloadProvider;
    }

    @Override
    public boolean hasType() {
        return false;
    }

    @Override
    public CompletableFuture<?> refreshAsync() {
        return CompletableFuture.runAsync(wrap(() -> {
            for (RemoteMod.Version modVersion : Lang.toIterable(ModrinthRemoteModRepository.MODS.getRemoteVersionsById("P7dR8mSH"))) {
                for (String gameVersion : modVersion.getGameVersions()) {
                    versions.put(gameVersion, new FabricAPIRemoteVersion(gameVersion, modVersion.getVersion(), modVersion.getName(), modVersion.getDatePublished(), modVersion,
                            Collections.singletonList(modVersion.getFile().getUrl())));
                }
            }
        }));
    }
}
