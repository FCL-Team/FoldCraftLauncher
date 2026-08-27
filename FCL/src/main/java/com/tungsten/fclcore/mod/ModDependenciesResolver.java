package com.tungsten.fclcore.mod;

import static com.tungsten.fclcore.util.Logging.LOG;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * 模组前置依赖解析器：从某个远程版本出发，递归解析全部 REQUIRED 前置模组，
 * 为每个前置挑选兼容当前游戏版本的最新文件。
 * BFS + 已访问集合防循环依赖；解析在后台线程执行，单个失败不影响其余。
 */
public final class ModDependenciesResolver {

    /** 防御性上限：防止病态依赖图产生过大的下载列表 */
    private static final int MAX_DEPENDENCIES = 50;

    /** 一个已选定版本的前置模组 */
    public record ResolvedDependency(RemoteMod mod, RemoteMod.Version version) {
    }

    /** 解析结果：待下载的前置列表 + 解析失败被跳过的名称 */
    public record Result(List<ResolvedDependency> dependencies, List<String> failedTitles) {
    }

    private ModDependenciesResolver() {
    }

    /**
     * 解析 root 版本的全部必需前置（含传递依赖）。
     * 前置的兼容性以 root 自身的 gameVersions / loaders 元数据为准：
     * 只有与 root 适用版本存在交集的前置文件才被选用（loader 存在精确匹配时优先）。
     */
    public static Result resolve(RemoteMod.Version root) {
        List<ResolvedDependency> resolved = new ArrayList<>();
        List<String> failedTitles = new ArrayList<>();
        if (root.getGameVersions() == null || root.getGameVersions().isEmpty()
                || root.getDependencies() == null || root.getDependencies().isEmpty())
            return new Result(resolved, failedTitles);

        Set<String> visited = new HashSet<>();
        if (root.getModid() != null)
            visited.add(root.getModid());

        Deque<RemoteMod.Dependency> queue = new ArrayDeque<>(root.getDependencies());
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

            try {
                RemoteMod mod = dependency.load();
                RemoteMod.Version best = findLatestCompatible(dependency.getRemoteModRepository(), id,
                        root.getGameVersions(), root.getLoaders());
                if (best == null) {
                    failedTitles.add(mod.getTitle());
                    continue;
                }
                resolved.add(new ResolvedDependency(mod, best));
                // 传递依赖：选定版本自身的必需前置也加入队列（visited 保证闭环收敛）
                if (best.getDependencies() != null)
                    queue.addAll(best.getDependencies());
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Failed to resolve mod dependency " + id, e);
                failedTitles.add(id);
            }
        }
        return new Result(resolved, failedTitles);
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
                .filter(v -> v.getDatePublished() != null && v.getFile() != null && v.getFile().getUrl() != null)
                .filter(v -> intersects(v.getGameVersions(), gameVersions))
                .toList());
        if (candidates.isEmpty())
            return null;
        if (loaders != null && !loaders.isEmpty()) {
            List<RemoteMod.Version> precise = candidates.stream()
                    .filter(v -> intersects(v.getLoaders(), loaders))
                    .collect(Collectors.toList());
            if (!precise.isEmpty())
                candidates = precise;
        }
        return candidates.stream()
                .max(Comparator.comparing(RemoteMod.Version::getDatePublished))
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