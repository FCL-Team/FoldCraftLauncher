package com.tungsten.fclcore.download.quilt;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.download.RemoteVersion;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.Task;

import java.util.Date;
import java.util.List;

public class QuiltAPIRemoteVersion extends RemoteVersion {
    private final String fullVersion;
    private final RemoteMod.Version version;

    /**
     * Constructor.
     *
     * @param gameVersion the Minecraft version that this remote version suits.
     * @param selfVersion the version string of the remote version.
     * @param urls        the installer or universal jar original URL.
     */
    QuiltAPIRemoteVersion(String gameVersion, String selfVersion, String fullVersion, Date datePublished, RemoteMod.Version version, List<String> urls) {
        super(LibraryAnalyzer.LibraryType.QUILT_API.getPatchId(), gameVersion, selfVersion, datePublished, urls);

        this.fullVersion = fullVersion;
        this.version = version;
    }

    @Override
    public String getFullVersion() {
        return fullVersion;
    }

    public RemoteMod.Version getVersion() {
        return version;
    }

    @Override
    public Task<Version> getInstallTask(DefaultDependencyManager dependencyManager, Version baseVersion) {
        return new QuiltAPIInstallTask(dependencyManager, baseVersion, this);
    }

    @Override
    public int compareTo(RemoteVersion o) {
        if (!(o instanceof QuiltAPIRemoteVersion)) return 0;
        return -this.getReleaseDate().compareTo(o.getReleaseDate());
    }
}
