package com.tungsten.fclcore.download;

import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.function.ExceptionalFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultGameBuilder extends GameBuilder {

    private final DefaultDependencyManager dependencyManager;

    public DefaultGameBuilder(DefaultDependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    public DefaultDependencyManager getDependencyManager() {
        return dependencyManager;
    }

    @Override
    public Task<?> buildAsync() {
        List<String> stages = new ArrayList<>();

        Task<Version> libraryTask = Task.supplyAsync(() -> new Version(name));
        libraryTask = libraryTask.thenComposeAsync(libraryTaskHelper(gameVersion, "game", gameVersion));
        stages.add("fcl.install.game:" + gameVersion);
        stages.add("fcl.install.assets");

        for (Map.Entry<String, String> entry : toolVersions.entrySet()) {
            libraryTask = libraryTask.thenComposeAsync(libraryTaskHelper(gameVersion, entry.getKey(), entry.getValue()));
            stages.add(String.format("fcl.install.%s:%s", entry.getKey(), entry.getValue()));
        }

        for (RemoteVersion remoteVersion : remoteVersions) {
            libraryTask = libraryTask.thenComposeAsync(version -> dependencyManager.installLibraryAsync(version, remoteVersion));
            stages.add(String.format("fcl.install.%s:%s", remoteVersion.getLibraryId(), remoteVersion.getSelfVersion()));
        }

        return libraryTask.thenComposeAsync(dependencyManager.getGameRepository()::saveAsync).whenComplete(exception -> {
            if (exception != null)
                dependencyManager.getGameRepository().removeVersionFromDisk(name);
        }).withStagesHint(stages);
    }

    private ExceptionalFunction<Version, Task<Version>, ?> libraryTaskHelper(String gameVersion, String libraryId, String libraryVersion) {
        return version -> dependencyManager.installLibraryAsync(gameVersion, version, libraryId, libraryVersion);
    }
}
