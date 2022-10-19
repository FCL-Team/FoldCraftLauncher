package com.tungsten.fclcore.download.liteloader;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.Artifact;
import com.tungsten.fclcore.game.LibrariesDownloadInfo;
import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.LibraryDownloadInfo;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Note: LiteLoader must be installed after Forge.
 */
public final class LiteLoaderInstallTask extends Task<Version> {

    private final DefaultDependencyManager dependencyManager;
    private final Version version;
    private final LiteLoaderRemoteVersion remote;
    private final List<Task<?>> dependents = new ArrayList<>();
    private final List<Task<?>> dependencies = new ArrayList<>(1);

    public LiteLoaderInstallTask(DefaultDependencyManager dependencyManager, Version version, LiteLoaderRemoteVersion remoteVersion) {
        this.dependencyManager = dependencyManager;
        this.version = version;
        this.remote = remoteVersion;
    }

    @Override
    public Collection<Task<?>> getDependents() {
        return dependents;
    }

    @Override
    public Collection<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public void execute() {
        Library library = new Library(
                new Artifact("com.mumfrey", "liteloader", remote.getSelfVersion()),
                "http://dl.liteloader.com/versions/",
                new LibrariesDownloadInfo(new LibraryDownloadInfo(null, remote.getUrls().get(0)))
        );

        setResult(new Version(LibraryAnalyzer.LibraryType.LITELOADER.getPatchId(),
                remote.getSelfVersion(),
                60000,
                new Arguments().addGameArguments("--tweakClass", "com.mumfrey.liteloader.launch.LiteLoaderTweaker"),
                LibraryAnalyzer.LAUNCH_WRAPPER_MAIN,
                Lang.merge(remote.getLibraries(), Collections.singleton(library)))
                .setLogging(Collections.emptyMap()) // Mods may log in malformed format, causing XML parser to crash. So we suppress using official log4j configuration
        );

        dependencies.add(dependencyManager.checkLibraryCompletionAsync(getResult(), true));
    }

}
