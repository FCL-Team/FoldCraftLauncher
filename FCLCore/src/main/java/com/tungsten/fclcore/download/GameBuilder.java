package com.tungsten.fclcore.download;

import com.tungsten.fclcore.task.Task;

import java.util.*;

/**
 * The builder which provide a task to build Minecraft environment.
 */
public abstract class GameBuilder {

    protected String name = "";
    protected String gameVersion = "";
    protected final Map<String, String> toolVersions = new HashMap<>();
    protected final Set<RemoteVersion> remoteVersions = new HashSet<>();

    public String getName() {
        return name;
    }

    /**
     * The new game version name, for .minecraft/&lt;version name&gt;.
     *
     * @param name the name of new game version.
     */
    public GameBuilder name(String name) {
        this.name = Objects.requireNonNull(name);
        return this;
    }

    public GameBuilder gameVersion(String version) {
        this.gameVersion = Objects.requireNonNull(version);
        return this;
    }

    /**
     * @param id the core library id. i.e. "forge", "liteloader", "optifine"
     * @param version the version of the core library. For documents, you can first try [VersionList.versions]
     */
    public GameBuilder version(String id, String version) {
        if ("game".equals(id))
            gameVersion(version);
        else
            toolVersions.put(id, version);
        return this;
    }

    public GameBuilder version(RemoteVersion remoteVersion) {
        remoteVersions.add(remoteVersion);
        return this;
    }

    /**
     * @return the task that can build thw whole Minecraft environment
     */
    public abstract Task<?> buildAsync();
}
