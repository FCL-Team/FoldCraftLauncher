/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
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
package com.tungsten.fclcore.download;

import com.tungsten.fclcore.game.GameComponentType;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.ToStringBuilder;
import com.tungsten.fclcore.util.versioning.VersionNumber;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The remote version.
 */
public abstract class ComponentRemoteVersion implements Comparable<ComponentRemoteVersion> {

    private final GameComponentType componentType;
    private final String gameVersion;
    private final String selfVersion;
    private final Instant releaseDate;
    private final List<String> urls;
    private final Type type;

    /**
     * Constructor.
     *
     * @param gameVersion the Minecraft version that this remote version suits.
     * @param selfVersion the version string of the remote version.
     * @param urls        the installer or universal jar original URL.
     */
    public ComponentRemoteVersion(GameComponentType componentType, String gameVersion, String selfVersion, Instant releaseDate, List<String> urls) {
        this(componentType, gameVersion, selfVersion, releaseDate, Type.UNCATEGORIZED, urls);
    }

    /**
     * Constructor.
     *
     * @param gameVersion the Minecraft version that this remote version suits.
     * @param selfVersion the version string of the remote version.
     * @param urls        the installer or universal jar URL.
     */
    public ComponentRemoteVersion(GameComponentType componentType, String gameVersion, String selfVersion, Instant releaseDate, Type type, List<String> urls) {
        this.componentType = Objects.requireNonNull(componentType);
        this.gameVersion = Objects.requireNonNull(gameVersion);
        this.selfVersion = Objects.requireNonNull(selfVersion);
        this.releaseDate = releaseDate;
        this.urls = Objects.requireNonNull(urls);
        this.type = Objects.requireNonNull(type);
    }

    public GameComponentType getComponentType() {
        return componentType;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public String getSelfVersion() {
        return selfVersion;
    }

    public String getFullVersion() {
        return getSelfVersion();
    }

    public Instant getReleaseDate() {
        return releaseDate;
    }

    public List<String> getUrls() {
        return urls;
    }

    public Type getVersionType() {
        return type;
    }

    public Task<Version> getInstallTask(DefaultDependencyManager dependencyManager, Version baseVersion) {
        throw new UnsupportedOperationException(this + " cannot be installed yet");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ComponentRemoteVersion && Objects.equals(selfVersion, ((ComponentRemoteVersion) obj).selfVersion);
    }

    @Override
    public int hashCode() {
        return selfVersion.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("selfVersion", selfVersion)
                .append("gameVersion", gameVersion)
                .toString();
    }

    /**
     * 缓存已解析的 {@link VersionNumber}，避免 TreeSet 大量插入（如 Fabric/Quilt 的
     * 游戏版本 × 加载器版本笛卡尔积，可达数万条）时对同一版本串反复执行开销较大的
     * Maven 版本解析。
     */
    private static final ConcurrentHashMap<String, VersionNumber> VERSION_NUMBER_CACHE = new ConcurrentHashMap<>();

    @Override
    public int compareTo(ComponentRemoteVersion o) {
        // newer versions are smaller than older versions
        return VERSION_NUMBER_CACHE.computeIfAbsent(o.selfVersion, VersionNumber::asVersion)
                .compareTo(VERSION_NUMBER_CACHE.computeIfAbsent(selfVersion, VersionNumber::asVersion));
    }

    public enum Type {
        UNCATEGORIZED,
        RELEASE,
        SNAPSHOT,
        OLD,
        PENDING,
        UNOBFUSCATED
    }
}
