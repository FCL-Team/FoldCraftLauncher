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
package com.tungsten.fclcore.mod.curse;

import com.tungsten.fclcore.mod.ModLoaderType;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.Pair;
import com.tungsten.fclcore.util.versioning.GameVersionNumber;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record CurseAddon(int id, int gameId, String name, String slug, Links links, String summary,
                         int status, int downloadCount, boolean isFeatured, int primaryCategoryId,
                         List<Category> categories, int classId, List<Author> authors, Logo logo,
                         int mainFileId, List<LatestFile> latestFiles,
                         List<LatestFileIndex> latestFileIndices, Instant dateCreated,
                         Instant dateModified, Instant dateReleased, boolean allowModDistribution,
                         int gamePopularityRank, boolean isAvailable, int thumbsUpCount,
                         List<Screenshot> screenshots) implements RemoteMod.IMod {
    public static final Map<Integer, RemoteMod.DependencyType> RELATION_TYPE = Lang.mapOf(
            Pair.pair(1, RemoteMod.DependencyType.EMBEDDED),
            Pair.pair(2, RemoteMod.DependencyType.OPTIONAL),
            Pair.pair(3, RemoteMod.DependencyType.REQUIRED),
            Pair.pair(4, RemoteMod.DependencyType.TOOL),
            Pair.pair(5, RemoteMod.DependencyType.INCOMPATIBLE),
            Pair.pair(6, RemoteMod.DependencyType.INCLUDE)
    );

    @Override
    public List<RemoteMod> loadDependencies(RemoteModRepository modRepository) throws IOException {
        Set<Integer> dependencies = latestFiles.stream()
                .flatMap(latestFile -> latestFile.dependencies().stream())
                .filter(dep -> dep.relationType() == 3)
                .map(Dependency::modId)
                .collect(Collectors.toSet());
        List<RemoteMod> mods = new ArrayList<>();
        for (int dependencyId : dependencies) {
            mods.add(modRepository.getModById(Integer.toString(dependencyId)));
        }
        return mods;
    }

    @Override
    public Stream<RemoteMod.Version> loadVersions(RemoteModRepository modRepository) throws IOException {
        return modRepository.getRemoteVersionsById(Integer.toString(id));
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
        String iconUrl = Optional.ofNullable(logo).map(Logo::thumbnailUrl).orElse("");

        return new RemoteMod(
                slug,
                "",
                name,
                summary,
                categories.stream().map(category -> Integer.toString(category.getId())).collect(Collectors.toList()),
                links.websiteUrl,
                iconUrl,
                this,
                downloadCount,
                String.valueOf(id)
        );
    }

    public record Links(String websiteUrl, String wikiUrl, String issuesUrl, String sourceUrl) {

        @Override
        @Nullable
        public String issuesUrl() {
            return issuesUrl;
        }

        @Override
        @Nullable
        public String sourceUrl() {
            return sourceUrl;
        }
    }

    public record Author(int id, String name, String url) {
    }

    public record Logo(int id, int modId, String title, String description, String thumbnailUrl,
                       String url) {
    }

    public record Attachment(int id, int projectId, String description, boolean isDefault,
                             String thumbnailUrl, String title, String url, int status) {
    }

    public record Dependency(int modId, int relationType) {
        public Dependency() {
            this(0, 1);
        }

    }

    /**
     * @see <a href="https://docs.curseforge.com/#schemafilehash">Schema</a>
     */
    public record LatestFileHash(String value, int algo) {
    }

    /**
     * @see <a href="https://docs.curseforge.com/#tocS_File">Schema</a>
     */
    public record LatestFile(int id, int gameId, int modId, boolean isAvailable, String displayName,
                             String fileName, int releaseType, int fileStatus,
                             List<LatestFileHash> hashes, Instant fileDate, int fileLength,
                             int downloadCount, String downloadUrl, List<String> gameVersions,
                             List<Dependency> dependencies, int alternateFileId,
                             boolean isServerPack,
                             long fileFingerprint) implements RemoteMod.IVersion {

        @Override
        public String downloadUrl() {
            // This addon is not allowed for distribution, and downloadUrl will be null.
            // We try to find its download url.
            return Objects.requireNonNullElseGet(downloadUrl, () -> String.format(Locale.getDefault(), "https://edge.forgecdn.net/files/%d/%d/%s", id / 1000, id % 1000, fileName));
        }

        @Override
        public RemoteMod.Type getType() {
            return RemoteMod.Type.CURSEFORGE;
        }

        public RemoteMod.Version toVersion() {
            RemoteMod.VersionType versionType = switch (releaseType()) {
                case 2 -> RemoteMod.VersionType.Beta;
                case 3 -> RemoteMod.VersionType.Alpha;
                default -> RemoteMod.VersionType.Release;
            };

            return new RemoteMod.Version(
                    this,
                    Integer.toString(modId),
                    displayName(),
                    fileName(),
                    null,
                    fileDate(),
                    versionType,
                    new RemoteMod.File(Collections.emptyMap(), downloadUrl(), fileName()),
                    dependencies.stream().map(dependency -> {
                        if (!RELATION_TYPE.containsKey(dependency.relationType())) {
                            throw new IllegalStateException("Broken datas.");
                        }
                        return RemoteMod.Dependency.ofGeneral(RELATION_TYPE.get(dependency.relationType()), CurseForgeRemoteModRepository.MODS, Integer.toString(dependency.modId()));
                    }).distinct().filter(Objects::nonNull).collect(Collectors.toList()),
                    gameVersions.stream().filter(GameVersionNumber::isKnown).collect(Collectors.toList()),
                    gameVersions.stream().flatMap(version -> {
                        if ("fabric".equalsIgnoreCase(version))
                            return Stream.of(ModLoaderType.FABRIC);
                        else if ("forge".equalsIgnoreCase(version))
                            return Stream.of(ModLoaderType.FORGE);
                        else if ("quilt".equalsIgnoreCase(version))
                            return Stream.of(ModLoaderType.QUILT);
                        else if ("neoforge".equalsIgnoreCase(version))
                            return Stream.of(ModLoaderType.NEO_FORGED);
                        else return Stream.empty();
                    }).collect(Collectors.toList())
            );
        }
    }

    /**
     * @see <a href="https://docs.curseforge.com/#tocS_FileIndex">Schema</a>
     */
    public record LatestFileIndex(String gameVersion, int fileId, String filename, int releaseType,
                                  int gameVersionTypeId, int modLoader) {

        @Override
        @Nullable
        public int gameVersionTypeId() {
            return gameVersionTypeId;
        }
    }

    public static class Category {
        private final int id;
        private final int gameId;
        private final String name;
        private final String slug;
        private final String url;
        private final String iconUrl;
        private final Instant dateModified;
        private final boolean isClass;
        private final int classId;
        private final int parentCategoryId;

        private transient final List<Category> subcategories;

        public Category() {
            this(0, 0, "", "", "", "", Instant.now(), false, 0, 0);
        }

        public Category(int id, int gameId, String name, String slug, String url, String iconUrl, Instant dateModified, boolean isClass, int classId, int parentCategoryId) {
            this.id = id;
            this.gameId = gameId;
            this.name = name;
            this.slug = slug;
            this.url = url;
            this.iconUrl = iconUrl;
            this.dateModified = dateModified;
            this.isClass = isClass;
            this.classId = classId;
            this.parentCategoryId = parentCategoryId;

            this.subcategories = new ArrayList<>();
        }

        public int getId() {
            return id;
        }

        public int getGameId() {
            return gameId;
        }

        public String getName() {
            return name;
        }

        public String getSlug() {
            return slug;
        }

        public String getUrl() {
            return url;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public Instant getDateModified() {
            return dateModified;
        }

        public boolean isClass() {
            return isClass;
        }

        public int getClassId() {
            return classId;
        }

        public int getParentCategoryId() {
            return parentCategoryId;
        }

        public List<Category> getSubcategories() {
            return subcategories;
        }

        public RemoteModRepository.Category toCategory() {
            return new RemoteModRepository.Category(
                    this,
                    Integer.toString(id),
                    getSubcategories().stream().map(Category::toCategory).collect(Collectors.toList()));
        }
    }

    public static class Screenshot {
        private final int id;
        private final int modid;
        private final String title;
        private final String description;
        private final String thumbnailUrl;
        private final String url;

        public Screenshot() {
            this.id = 0;
            this.modid = 0;
            this.title = "";
            this.description = "";
            this.thumbnailUrl = "";
            this.url = "";
        }

        public Screenshot(int id, int modid, String title, String description, String thumbnailUrl, String url) {
            this.id = id;
            this.modid = modid;
            this.title = title;
            this.description = description;
            this.thumbnailUrl = thumbnailUrl;
            this.url = url;
        }
    }
}
