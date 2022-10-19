package com.tungsten.fclcore.download.game;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.CacheRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Task to download Minecraft jar
 */
public final class GameDownloadTask extends Task<Void> {
    private final DefaultDependencyManager dependencyManager;
    private final String gameVersion;
    private final Version version;
    private final List<Task<?>> dependencies = new ArrayList<>();

    public GameDownloadTask(DefaultDependencyManager dependencyManager, String gameVersion, Version version) {
        this.dependencyManager = dependencyManager;
        this.gameVersion = gameVersion;
        this.version = version.resolve(dependencyManager.getGameRepository());

        setSignificance(TaskSignificance.MODERATE);
    }

    @Override
    public Collection<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public void execute() {
        File jar = dependencyManager.getGameRepository().getVersionJar(version);

        FileDownloadTask task = new FileDownloadTask(
                dependencyManager.getDownloadProvider().injectURLWithCandidates(version.getDownloadInfo().getUrl()),
                jar,
                FileDownloadTask.IntegrityCheck.of(CacheRepository.SHA1, version.getDownloadInfo().getSha1()));
        task.setCaching(true);
        task.setCacheRepository(dependencyManager.getCacheRepository());

        if (gameVersion != null)
            task.setCandidate(dependencyManager.getCacheRepository().getCommonDirectory().resolve("jars").resolve(gameVersion + ".jar"));

        dependencies.add(task);
    }
    
}
