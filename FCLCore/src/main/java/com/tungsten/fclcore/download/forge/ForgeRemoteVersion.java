package com.tungsten.fclcore.download.forge;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.download.RemoteVersion;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;

import java.util.Date;
import java.util.List;

public class ForgeRemoteVersion extends RemoteVersion {
    /**
     * Constructor.
     *
     * @param gameVersion the Minecraft version that this remote version suits.
     * @param selfVersion the version string of the remote version.
     * @param url         the installer or universal jar original URL.
     */
    public ForgeRemoteVersion(String gameVersion, String selfVersion, Date releaseDate, List<String> url) {
        super(LibraryAnalyzer.LibraryType.FORGE.getPatchId(), gameVersion, selfVersion, releaseDate, url);
    }

    @Override
    public Task<Version> getInstallTask(DefaultDependencyManager dependencyManager, Version baseVersion) {
        return new ForgeInstallTask(dependencyManager, baseVersion, this);
    }
}
