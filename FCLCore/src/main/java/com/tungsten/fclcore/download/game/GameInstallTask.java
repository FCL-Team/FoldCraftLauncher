package com.tungsten.fclcore.download.game;

import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.MINECRAFT;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.game.DefaultGameRepository;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.gson.JsonUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GameInstallTask extends Task<Version> {

    private final DefaultGameRepository gameRepository;
    private final DefaultDependencyManager dependencyManager;
    private final Version version;
    private final GameRemoteVersion remote;
    private final VersionJsonDownloadTask downloadTask;
    private final List<Task<?>> dependencies = new ArrayList<>(1);

    public GameInstallTask(DefaultDependencyManager dependencyManager, Version version, GameRemoteVersion remoteVersion) {
        this.dependencyManager = dependencyManager;
        this.gameRepository = dependencyManager.getGameRepository();
        this.version = version;
        this.remote = remoteVersion;
        this.downloadTask = new VersionJsonDownloadTask(remoteVersion.getGameVersion(), dependencyManager);
    }

    @Override
    public Collection<Task<?>> getDependents() {
        return Collections.singleton(downloadTask);
    }

    @Override
    public Collection<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean isRelyingOnDependencies() {
        return false;
    }

    @Override
    public void execute() throws Exception {
        Version patch = JsonUtils.fromNonNullJson(downloadTask.getResult(), Version.class)
                .setId(MINECRAFT.getPatchId()).setVersion(remote.getGameVersion()).setJar(null).setPriority(0);
        setResult(patch);

        Version version = new Version(this.version.getId()).addPatch(patch);
        dependencies.add(Task.allOf(
                new GameDownloadTask(dependencyManager, remote.getGameVersion(), version),
                Task.allOf(
                        new GameAssetDownloadTask(dependencyManager, version, GameAssetDownloadTask.DOWNLOAD_INDEX_FORCIBLY, true),
                        new GameLibrariesTask(dependencyManager, version, true)
                ).withRunAsync(() -> {
                    // ignore failure
                })
        ).thenComposeAsync(gameRepository.saveAsync(version)));
    }

}
