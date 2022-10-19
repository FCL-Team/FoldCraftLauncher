package com.tungsten.fclcore.download.game;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.download.RemoteVersion;
import com.tungsten.fclcore.game.ReleaseType;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;

import java.util.Date;
import java.util.List;

public final class GameRemoteVersion extends RemoteVersion {

    private final ReleaseType type;

    public GameRemoteVersion(String gameVersion, String selfVersion, List<String> url, ReleaseType type, Date releaseDate) {
        super(LibraryAnalyzer.LibraryType.MINECRAFT.getPatchId(), gameVersion, selfVersion, releaseDate, getReleaseType(type), url);
        this.type = type;
    }

    public ReleaseType getType() {
        return type;
    }

    @Override
    public Task<Version> getInstallTask(DefaultDependencyManager dependencyManager, Version baseVersion) {
        return new GameInstallTask(dependencyManager, baseVersion, this);
    }

    @Override
    public int compareTo(RemoteVersion o) {
        if (!(o instanceof GameRemoteVersion))
            return 0;

        return o.getReleaseDate().compareTo(getReleaseDate());
    }

    private static Type getReleaseType(ReleaseType type) {
        if (type == null) return Type.UNCATEGORIZED;
        switch (type) {
            case RELEASE:
                return Type.RELEASE;
            case SNAPSHOT:
                return Type.SNAPSHOT;
            case UNKNOWN:
                return Type.UNCATEGORIZED;
            default:
                return Type.OLD;
        }
    }
}
