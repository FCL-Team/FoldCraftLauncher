package com.tungsten.fclcore.download.game;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.RemoteVersion;
import com.tungsten.fclcore.download.VersionList;
import com.tungsten.fclcore.task.GetTask;
import com.tungsten.fclcore.task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class VersionJsonDownloadTask extends Task<String> {
    private final String gameVersion;
    private final DefaultDependencyManager dependencyManager;
    private final List<Task<?>> dependents = new ArrayList<>(1);
    private final List<Task<?>> dependencies = new ArrayList<>(1);
    private final VersionList<?> gameVersionList;

    public VersionJsonDownloadTask(String gameVersion, DefaultDependencyManager dependencyManager) {
        this.gameVersion = gameVersion;
        this.dependencyManager = dependencyManager;
        this.gameVersionList = dependencyManager.getVersionList("game");

        dependents.add(Task.fromCompletableFuture(gameVersionList.loadAsync(gameVersion)));

        setSignificance(TaskSignificance.MODERATE);
    }

    @Override
    public Collection<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public Collection<Task<?>> getDependents() {
        return dependents;
    }

    @Override
    public void execute() throws IOException {
        RemoteVersion remoteVersion = gameVersionList.getVersion(gameVersion, gameVersion)
                .orElseThrow(() -> new IOException("Cannot find specific version " + gameVersion + " in remote repository"));
        dependencies.add(new GetTask(dependencyManager.getDownloadProvider().injectURLsWithCandidates(remoteVersion.getUrls())).storeTo(this::setResult));
    }
}
