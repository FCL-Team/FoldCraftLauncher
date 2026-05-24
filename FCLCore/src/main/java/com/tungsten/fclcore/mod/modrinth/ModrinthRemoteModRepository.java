/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2021  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fclcore.mod.modrinth;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Logging.LOG;
import static com.tungsten.fclcore.util.Pair.pair;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.ModLoaderType;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.util.DigestUtils;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.HttpRequest;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fclcore.util.io.ResponseCodeException;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ModrinthRemoteModRepository implements RemoteModRepository {
    public static final ModrinthRemoteModRepository MODS = new ModrinthRemoteModRepository("mod");
    public static final ModrinthRemoteModRepository MODPACKS = new ModrinthRemoteModRepository("modpack");
    public static final ModrinthRemoteModRepository RESOURCE_PACKS = new ModrinthRemoteModRepository("resourcepack");
    public static final ModrinthRemoteModRepository SHADER_PACKS = new ModrinthRemoteModRepository("shader");

    private static final String PREFIX = "https://api.modrinth.com";

    private final String projectType;

    private ModrinthRemoteModRepository(String projectType) {
        this.projectType = projectType;
    }

    @Override
    public Type getType() {
        return Type.MOD;
    }

    private static String convertSortType(SortType sortType) {
        switch (sortType) {
            case DATE_CREATED:
                return "newest";
            case POPULARITY:
            case NAME:
            case AUTHOR:
                return "relevance";
            case LAST_UPDATED:
                return "updated";
            case TOTAL_DOWNLOADS:
                return "downloads";
            default:
                throw new IllegalArgumentException("Unsupported sort type " + sortType);
        }
    }

    @Override
    public SearchResult search(DownloadProvider downloadProvider, String gameVersion, @Nullable RemoteModRepository.Category category, int pageOffset, int pageSize, String searchFilter, SortType sort, SortOrder sortOrder) throws IOException {
        List<List<String>> facets = new ArrayList<>();
        facets.add(Collections.singletonList("project_type:" + projectType));
        if (StringUtils.isNotBlank(gameVersion)) {
            facets.add(Collections.singletonList("versions:" + gameVersion));
        }
        if (category != null && StringUtils.isNotBlank(category.id())) {
            facets.add(Collections.singletonList("categories:" + category.id()));
        }
        Map<String, String> query = mapOf(
                pair("query", searchFilter),
                pair("facets", JsonUtils.UGLY_GSON.toJson(facets)),
                pair("offset", Integer.toString(pageOffset * pageSize)),
                pair("limit", Integer.toString(pageSize)),
                pair("index", convertSortType(sort))
        );
        List<URL> candidates = downloadProvider.injectURLWithCandidates(NetworkUtils.withQuery(PREFIX + "/v2/search", query));
        IOException exception = null;
        for (URL candidate : candidates) {
            try {
                LOG.info("Fetching " + candidate);
                Response<ProjectSearchResult> response = HttpRequest.GET(candidate.toString())
                        .getJson(new TypeToken<Response<ProjectSearchResult>>() {
                        }.getType());
                return new SearchResult(response.getHits().stream().map(ProjectSearchResult::toMod), (int) Math.ceil((double) response.totalHits / pageSize));
            } catch (IOException e) {
                LOG.warning("Failed to search addons: " + candidate + "\n" + e);
                if (candidates.size() == 1) {
                    exception = e;
                } else {
                    if (exception == null) {
                        exception = new IOException("Failed to search addons");
                    }
                    exception.addSuppressed(e);
                }
            }
        }

        throw exception != null ? exception : new IOException("No candidates found");
    }

    @Override
    public Optional<RemoteMod.Version> getRemoteVersionByLocalFile(LocalModFile localModFile, Path file) throws IOException {
        String sha1 = DigestUtils.digestToString("SHA-1", file);

        try {
            ProjectVersion mod = HttpRequest.GET(PREFIX + "/v2/version_file/" + sha1,
                            pair("algorithm", "sha1"))
                    .getJson(ProjectVersion.class);
            return mod.toVersion();
        } catch (ResponseCodeException e) {
            if (e.getResponseCode() == 404) {
                return Optional.empty();
            } else {
                throw e;
            }
        }
    }

    @Override
    public RemoteMod getModById(String id) throws IOException {
        id = StringUtils.removePrefix(id, "local-");
        Project project = HttpRequest.GET(PREFIX + "/v2/project/" + id).getJson(Project.class);
        return project.toMod();
    }

    @Override
    public RemoteMod.File getModFile(String modId, String fileId) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<RemoteMod.Version> getRemoteVersionsById(String id) throws IOException {
        id = StringUtils.removePrefix(id, "local-");
        List<ProjectVersion> versions = HttpRequest.GET(PREFIX + "/v2/project/" + id + "/version")
                .getJson(new TypeToken<List<ProjectVersion>>() {
                }.getType());
        return versions.stream().map(ProjectVersion::toVersion).flatMap(Lang::toStream);
    }

    public List<Category> getCategoriesImpl() throws IOException {
        List<Category> categories = HttpRequest.GET(PREFIX + "/v2/tag/category").getJson(new TypeToken<List<Category>>() {
        }.getType());
        return categories.stream().filter(category -> category.projectType().equals(projectType)).collect(Collectors.toList());
    }

    @Override
    public Stream<RemoteModRepository.Category> getCategories() throws IOException {
        return getCategoriesImpl().stream().map(Category::toCategory);
    }

    public record Category(String icon, String name,
                           @SerializedName("project_type") String projectType) {
            public Category() {
                this("", "", "");
            }

        @Override
        public String projectType() {
                return projectType;
            }

            public RemoteModRepository.Category toCategory() {
                return new RemoteModRepository.Category(
                        this,
                        name,
                        Collections.emptyList());
            }
        }

    /**
     * @param body A long body describing project in detail.
     */
    public record Project(String slug, String title, String description, List<String> categories,
                          String body, @SerializedName("project_type") String projectType,
                          int downloads, @SerializedName("icon_url") String iconUrl, String id,
                          String team, Instant published, Instant updated, List<String> versions,
                          @SerializedName("gallery") List<Screenshot> screenshots) implements RemoteMod.IMod {

        @Override
        public String projectType() {
                return projectType;
            }

            @Override
            public String iconUrl() {
                return iconUrl;
            }

            @Override
            public List<Screenshot> screenshots() {
                return screenshots;
            }

            @Override
            public List<RemoteMod> loadDependencies(RemoteModRepository modRepository) throws IOException {
                Set<RemoteMod.Dependency> dependencies = modRepository.getRemoteVersionsById(id())
                        .flatMap(version -> version.getDependencies().stream())
                        .collect(Collectors.toSet());
                List<RemoteMod> mods = new ArrayList<>();
                for (RemoteMod.Dependency dependency : dependencies) {
                    mods.add(dependency.load());
                }
                return mods;
            }

            @Override
            public Stream<RemoteMod.Version> loadVersions(RemoteModRepository modRepository) throws IOException {
                return modRepository.getRemoteVersionsById(id());
            }

            @Override
            public List<RemoteMod.Screenshot> loadScreenshots(RemoteModRepository modRepository) {
                List<RemoteMod.Screenshot> screenshotList = new ArrayList<>();
                for (Screenshot screenshot : this.screenshots) {
                    screenshotList.add(new RemoteMod.Screenshot(screenshot.url, screenshot.title, screenshot.description));
                }
                return screenshotList;
            }

            public RemoteMod toMod() {
                return new RemoteMod(
                        slug,
                        "",
                        title,
                        description,
                        categories,
                        String.format("https://modrinth.com/%s/%s", projectType, id),
                        iconUrl,
                        this,
                        id
                );
            }
        }

    public record Dependency(@SerializedName("version_id") String versionId,
                             @SerializedName("project_id") String projectId,
                             @SerializedName("dependency_type") String dependencyType) {

        @Override
        public String versionId() {
                return versionId;
            }

            @Override
            public String projectId() {
                return projectId;
            }

            @Override
            public String dependencyType() {
                return dependencyType;
            }
        }

    public record ProjectVersion(String name,
                                 @SerializedName("version_number") String versionNumber,
                                 String changelog, List<Dependency> dependencies,
                                 @SerializedName("game_versions") List<String> gameVersions,
                                 @SerializedName("version_type") String versionType,
                                 List<String> loaders, boolean featured, String id,
                                 @SerializedName("project_id") String projectId,
                                 @SerializedName("author_id") String authorId,
                                 @SerializedName("date_published") Instant datePublished,
                                 int downloads,
                                 @SerializedName("changelog_url") String changelogUrl,
                                 List<ProjectVersionFile> files) implements RemoteMod.IVersion {
            private static final Map<String, RemoteMod.DependencyType> DEPENDENCY_TYPE = mapOf(
                    pair("required", RemoteMod.DependencyType.REQUIRED),
                    pair("optional", RemoteMod.DependencyType.OPTIONAL),
                    pair("embedded", RemoteMod.DependencyType.EMBEDDED),
                    pair("incompatible", RemoteMod.DependencyType.INCOMPATIBLE)
            );

        @Override
        public String versionNumber() {
                return versionNumber;
            }

            @Override
            public List<String> gameVersions() {
                return gameVersions;
            }

            @Override
            public String versionType() {
                return versionType;
            }

            @Override
            public String projectId() {
                return projectId;
            }

            @Override
            public String authorId() {
                return authorId;
            }

            @Override
            public Instant datePublished() {
                return datePublished;
            }

            @Override
            public String changelogUrl() {
                return changelogUrl;
            }

            @Override
            public RemoteMod.Type getType() {
                return RemoteMod.Type.MODRINTH;
            }

            public Optional<RemoteMod.Version> toVersion() {
                RemoteMod.VersionType type;
                if ("release".equals(versionType)) {
                    type = RemoteMod.VersionType.Release;
                } else if ("beta".equals(versionType)) {
                    type = RemoteMod.VersionType.Beta;
                } else if ("alpha".equals(versionType)) {
                    type = RemoteMod.VersionType.Alpha;
                } else {
                    type = RemoteMod.VersionType.Release;
                }

                if (files.size() == 0) {
                    return Optional.empty();
                }

                return Optional.of(new RemoteMod.Version(
                        this,
                        projectId,
                        name,
                        versionNumber,
                        changelog,
                        datePublished,
                        type,
                        files.get(0).toFile(),
                        dependencies.stream().map(dependency -> {
                            if (dependency.projectId == null) {
                                return RemoteMod.Dependency.ofBroken();
                            }

                            if (!DEPENDENCY_TYPE.containsKey(dependency.dependencyType)) {
                                throw new IllegalStateException("Broken datas");
                            }

                            return RemoteMod.Dependency.ofGeneral(DEPENDENCY_TYPE.get(dependency.dependencyType), MODS, dependency.projectId);
                        }).filter(Objects::nonNull).collect(Collectors.toList()),
                        gameVersions,
                        loaders.stream().flatMap(loader -> {
                            if ("fabric".equalsIgnoreCase(loader))
                                return Stream.of(ModLoaderType.FABRIC);
                            else if ("forge".equalsIgnoreCase(loader))
                                return Stream.of(ModLoaderType.FORGE);
                            else if ("neoforge".equalsIgnoreCase(loader))
                                return Stream.of(ModLoaderType.NEO_FORGED);
                            else if ("quilt".equalsIgnoreCase(loader))
                                return Stream.of(ModLoaderType.QUILT);
                            else if ("liteloader".equalsIgnoreCase(loader))
                                return Stream.of(ModLoaderType.LITE_LOADER);
                            else return Stream.empty();
                        }).collect(Collectors.toList())
                ));
            }
        }

    public record ProjectVersionFile(Map<String, String> hashes, String url, String filename,
                                     boolean primary, int size) {

        public RemoteMod.File toFile() {
                return new RemoteMod.File(hashes, url, filename);
            }
        }

    public record ProjectSearchResult(String slug, String title, String description,
                                      List<String> categories,
                                      @SerializedName("project_type") String projectType,
                                      int downloads, @SerializedName("icon_url") String iconUrl,
                                      @SerializedName("project_id") String projectId, String author,
                                      List<String> versions,
                                      @SerializedName("date_created") Instant dateCreated,
                                      @SerializedName("date_modified") Instant dateModified,
                                      @SerializedName("latest_version") String latestVersion) implements RemoteMod.IMod {

        @Override
        public String projectType() {
                return projectType;
            }

            @Override
            public String iconUrl() {
                return iconUrl;
            }

            @Override
            public String projectId() {
                return projectId;
            }

            @Override
            public Instant dateCreated() {
                return dateCreated;
            }

            @Override
            public Instant dateModified() {
                return dateModified;
            }

            @Override
            public String latestVersion() {
                return latestVersion;
            }

            @Override
            public List<RemoteMod> loadDependencies(RemoteModRepository modRepository) throws IOException {
                Set<RemoteMod.Dependency> dependencies = modRepository.getRemoteVersionsById(projectId())
                        .flatMap(version -> version.getDependencies().stream())
                        .collect(Collectors.toSet());
                List<RemoteMod> mods = new ArrayList<>();
                for (RemoteMod.Dependency dependency : dependencies) {
                    mods.add(dependency.load());
                }
                return mods;
            }

            @Override
            public Stream<RemoteMod.Version> loadVersions(RemoteModRepository modRepository) throws IOException {
                return modRepository.getRemoteVersionsById(projectId());
            }

            @Override
            public List<RemoteMod.Screenshot> loadScreenshots(RemoteModRepository modRepository) throws IOException {
                //由于直接搜索得到的截图信息只有链接，没有标题、描述等信息，所以需要直接获取这个Mod的详细信息
                return modRepository.getModById(projectId()).getData().loadScreenshots(modRepository);
            }

            public RemoteMod toMod() {
                return new RemoteMod(
                        slug,
                        author,
                        title,
                        description,
                        categories,
                        String.format("https://modrinth.com/%s/%s", projectType, projectId),
                        iconUrl,
                        this,
                        projectId
                );
            }
        }

    public static class Response<T> {
        private final int offset;

        private final int limit;

        @SerializedName("total_hits")
        private final int totalHits;

        private final List<T> hits;

        public Response() {
            this(0, 0, Collections.emptyList());
        }

        public Response(int offset, int limit, List<T> hits) {
            this.offset = offset;
            this.limit = limit;
            this.totalHits = hits.size();
            this.hits = hits;
        }

        public int getOffset() {
            return offset;
        }

        public int getLimit() {
            return limit;
        }

        public int getTotalHits() {
            return totalHits;
        }

        public List<T> getHits() {
            return hits;
        }
    }

    public record Screenshot(String url, @SerializedName("raw_url") String rawUrl, boolean featured,
                             String title, String description, Instant created, int ordering) {

        @Override
        public String rawUrl() {
                return rawUrl;
            }
        }
}
