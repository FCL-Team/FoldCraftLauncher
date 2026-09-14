package com.tungsten.fclcore.download.neoforge;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.ComponentRemoteVersion;
import com.tungsten.fclcore.game.GameComponentType;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;

import java.util.List;

public class NeoForgeRemoteVersion extends ComponentRemoteVersion {
    public NeoForgeRemoteVersion(String gameVersion, String selfVersion, List<String> urls) {
        super(GameComponentType.NEO_FORGE, gameVersion, selfVersion, null, getType(selfVersion), urls);
    }

    @Override
    public Task<Version> getInstallTask(DefaultDependencyManager dependencyManager, Version baseVersion) {
        return new NeoForgeInstallTask(dependencyManager, baseVersion, this);
    }

    private static Type getType(String version) {
        return version.contains("beta") ? Type.SNAPSHOT : Type.RELEASE;
    }

    public static String normalize(String version) {
        if (version.startsWith("1.20.1-")) {
            if (version.startsWith("forge-", "1.20.1-".length())) {
                return version.substring("1.20.1-forge-".length());
            } else {
                return version.substring("1.20.1-".length());
            }
        } else {
            return version;
        }
    }
}
