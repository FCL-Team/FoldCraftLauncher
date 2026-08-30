package com.tungsten.fclcore.mod;

import static com.tungsten.fclcore.util.Logging.LOG;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/**
 * 模组前置依赖解析器：从某个远程版本出发，递归解析全部 REQUIRED 前置模组，
 * 为每个前置挑选兼容当前游戏版本的最新文件。
 * BFS + 已访问集合防循环依赖；解析在后台线程执行，单个失败不影响其余。
 * 去重：先按文件名快速检测本地是否已有同一文件（零网络开销），再通过当前下载源的
 * 远程反查（CurseForge 文件指纹 / Modrinth SHA-1）做项目级检测，两者命中都跳过下载；
 * 本体已安装时只跳过本体，前置仍会解析并安装。
 */
public final class ModDependenciesResolver {

    /** 防御性上限：防止病态依赖图产生过大的下载列表 */
    private static final int MAX_DEPENDENCIES = 50;

    /** 一个已选定版本的前置模组 */
    public record ResolvedDependency(RemoteMod mod, RemoteMod.Version version) {
    }

    /** 解析结果：待下载的前置列表、解析失败数、因本地已安装而跳过的数量，以及本体是否已安装 */
    public record Result(List<ResolvedDependency> dependencies, List<String> failedTitles,
                         boolean rootInstalled, int installedSkipped) {
    }

    /** 本地已安装索引：文件名即时收集（零网络），项目 id 反查在首次需要时才执行 */
    private static final class InstalledIndex {
        private final RemoteModRepository repository;
        private final Path modsDirectory;
        private final Set<String> filenames = new HashSet<>();
        private Set<String> projectIds;

        InstalledIndex(RemoteModRepository repository, Path modsDirectory) {
            this.repository = repository;
            this.modsDirectory = modsDirectory;
            if (modsDirectory != null && Files.isDirectory(modsDirectory)) {
                try (Stream<Path> files = Files.list(modsDirectory)) {
                    files.filter(path -> path.toString().endsWith(".jar"))
                            .forEach(path -> filenames.add(path.getFileName().toString()));
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to list installed mods", e);
                }
            }
        }

        boolean containsFilename(@Nullable String filename) {
            return filename != null && filenames.contains(filename);
        }

        boolean containsProject(@Nullable String projectId) {
            if (projectId == null)
                return false;
            if (projectIds == null)
                projectIds = collectProjectIds();
            return projectIds.contains(projectId);
        }

        private Set<String> collectProjectIds() {
            Set<String> ids = new HashSet<>();
            if (repository == null || modsDirectory == null || !Files.isDirectory(modsDirectory))
                return ids;
            try (Stream<Path> files = Files.list(modsDirectory)) {
                files.filter(path -> path.toString().endsWith(".jar")).forEach(path -> {
                    try {
                        repository.getRemoteVersionByLocalFile(null, path)
                                .ifPresent(version -> {
                                    if (version.modid() != null)
                                        ids.add(version.modid());
                                });
                    } catch (IOException e) {
                        LOG.log(Level.FINE, "Skip unidentifiable local mod " + path, e);
                    }
                });
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Failed to scan installed mods for deduplication", e);
            }
            return ids;
        }
    }

    private ModDependenciesResolver() {
    }

    /**
     * 解析 root 版本的全部必需前置（含传递依赖）。
     * 前置的兼容性以 root 自身的 gameVersions / loaders 元数据为准：
     * 只有与 root 适用版本存在交集的前置文件才被选用（loader 存在精确匹配时优先）。
     * 本体已安装时跳过本体下载，但仍会解析并安装其缺失的前置。
     *
     * @param modsDirectory mods 目录，用于本地去重检测；repository 为当前下载源
     */
    public static Result resolve(RemoteMod.Version root, @Nullable Path modsDirectory,
                                 @Nullable RemoteModRepository repository) {
        List<ResolvedDependency> resolved = new ArrayList<>();
        List<String> failedTitles = new ArrayList<>();
        InstalledIndex installed = modsDirectory != null && repository != null
                ? new InstalledIndex(repository, modsDirectory)
                : null;

        // 本体已安装：跳过本体下载，但前置仍照常解析安装
        boolean rootInstalled = installed != null &&
                (installed.containsFilename(getFilename(root)) || installed.containsProject(root.modid()));
        int installedSkipped = 0;

        if (root.gameVersions() == null || root.gameVersions().isEmpty()
                || root.dependencies() == null || root.dependencies().isEmpty())
            return new Result(resolved, failedTitles, rootInstalled, installedSkipped);

        Set<String> visited = new HashSet<>();
        if (root.modid() != null)
            visited.add(root.modid());

        Deque<RemoteMod.Dependency> queue = new ArrayDeque<>(root.dependencies());
        while (!queue.isEmpty()) {
            if (resolved.size() >= MAX_DEPENDENCIES) {
                LOG.log(Level.WARNING, "Too many mod dependencies (> " + MAX_DEPENDENCIES + "), truncating");
                break;
            }
            RemoteMod.Dependency dependency = queue.poll();
            // 仅自动下载必需的前置；其余类型（可选/内嵌/工具/不兼容/损坏）一律忽略
            if (dependency.getType() != RemoteMod.DependencyType.REQUIRED)
                continue;
            String id = dependency.getId();
            if (id == null || !visited.add(id))
                continue;
            // 项目 id 已在本地（零网络；文件名在选版本前未知，无法提前检测）
            if (installed != null && installed.containsProject(id)) {
                installedSkipped++;
                continue;
            }

            try {
                RemoteMod mod = dependency.load();
                RemoteMod.Version best = findLatestCompatible(dependency.getRemoteModRepository(), id,
                        root.gameVersions(), root.loaders());
                if (best == null) {
                    failedTitles.add(mod.getTitle());
                    continue;
                }
                // 选定版本后再按文件名快速检测：本地已有同一文件则跳过（零网络开销）
                if (installed.containsFilename(getFilename(best))) {
                    installedSkipped++;
                    continue;
                }
                resolved.add(new ResolvedDependency(mod, best));
                // 传递依赖：选定版本自身的必需前置也加入队列（visited 保证闭环收敛）
                if (best.dependencies() != null)
                    queue.addAll(best.dependencies());
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Failed to resolve mod dependency " + id, e);
                failedTitles.add(id);
            }
        }
        return new Result(resolved, failedTitles, rootInstalled, installedSkipped);
    }

    private static String getFilename(RemoteMod.Version version) {
        return version.file() != null ? version.file().filename() : null;
    }

    /**
     * 在仓库中挑选与 root 兼容的最新版本（发布日期最大者）：
     * gameVersions 与 root 存在交集即兼容；loaders 能与 root 交集的前置候选优先
     * （多数文件缺少 loader 标注，缺省时仍可入选）。过滤掉无法下载或无日期信息的条目。
     */
    @Nullable
    private static RemoteMod.Version findLatestCompatible(RemoteModRepository repository, String modId,
                                                          List<String> gameVersions, @Nullable List<ModLoaderType> loaders) throws IOException {
        List<RemoteMod.Version> candidates = new ArrayList<>(repository.getRemoteVersionsById(modId)
                .filter(v -> v.datePublished() != null && v.file() != null && v.file().url() != null)
                .filter(v -> intersects(v.gameVersions(), gameVersions))
                .collect(Collectors.toList()));
        if (candidates.isEmpty())
            return null;
        if (loaders != null && !loaders.isEmpty()) {
            List<RemoteMod.Version> precise = candidates.stream()
                    .filter(v -> intersects(v.loaders(), loaders))
                    .collect(Collectors.toList());
            if (!precise.isEmpty())
                candidates = precise;
        }
        return candidates.stream()
                .max(Comparator.comparing(RemoteMod.Version::datePublished))
                .orElse(null);
    }

    private static boolean intersects(@Nullable List<?> a, @Nullable List<?> b) {
        if (a == null || b == null)
            return false;
        for (Object item : a)
            if (b.contains(item))
                return true;
        return false;
    }
}