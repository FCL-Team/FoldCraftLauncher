package com.tungsten.fclcore.download.quilt;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.download.RemoteVersion;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;

import java.util.List;

public class QuiltRemoteVersion extends RemoteVersion {
    /**
     * Constructor.
     *
     * @param gameVersion the Minecraft version that this remote version suits.
     * @param selfVersion the version string of the remote version.
     * @param urls        the installer or universal jar original URL.
     */
    QuiltRemoteVersion(String gameVersion, String selfVersion, List<String> urls) {
        super(LibraryAnalyzer.LibraryType.QUILT.getPatchId(), gameVersion, selfVersion, null, urls);
    }

    @Override
    public Task<Version> getInstallTask(DefaultDependencyManager dependencyManager, Version baseVersion) {
        return new QuiltInstallTask(dependencyManager, baseVersion, this);
    }
}
