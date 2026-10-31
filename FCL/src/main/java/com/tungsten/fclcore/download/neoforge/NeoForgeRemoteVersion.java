package com.tungsten.fclcore.download.neoforge;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.ComponentRemoteVersion;
import com.tungsten.fclcore.game.GameComponentType;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeRemoteVersion extends ComponentRemoteVersion {
    // https://neoforged.forgecdn.net
    public static final String MIRROR_OLD_URL = "https://neoforged.forgecdn.net/api/maven/versions/releases/net/neoforged/forge";
    public static final String MIRROR_META_URL = "https://neoforged.forgecdn.net/api/maven/versions/releases/net/neoforged/neoforge";

    private static final String NEOFORGED_MAVEN = "https://maven.neoforged.net/";
    private static final String NEOFORGED_MAVEN_RELEASE = "https://maven.neoforged.net/releases/";
    private static final String FORGECDN_ROOT = "https://neoforged.forgecdn.net/";

    /**
     * 为 maven.neoforged.net 下的 URL 生成下载候选列表（其余 URL 返回 null）：
     * /releases/ 路径在官方源与 BMCLAPI 镜像之间按 mirrorFirst 排序，并一律追加 CurseForge CDN。
     * 同步自 HMCL #7092 (5ad4880)。
     */
    @Nullable
    public static List<String> getMirrorCandidates(String url, boolean mirrorFirst) {
        if (!url.startsWith(NEOFORGED_MAVEN)) {
            return null;
        }

        List<String> candidates = new ArrayList<>();

        if (url.startsWith(NEOFORGED_MAVEN_RELEASE)) {
            String bmclapiMirror = "https://bmclapi2.bangbang93.com/maven/" + url.substring(NEOFORGED_MAVEN_RELEASE.length());
            if (mirrorFirst) {
                candidates.add(bmclapiMirror);
                candidates.add(url);
            } else {
                candidates.add(url);
                candidates.add(bmclapiMirror);
            }
        } else {
            candidates.add(url);
        }

        candidates.add(FORGECDN_ROOT + url.substring(NEOFORGED_MAVEN.length()));
        return candidates;
    }

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
