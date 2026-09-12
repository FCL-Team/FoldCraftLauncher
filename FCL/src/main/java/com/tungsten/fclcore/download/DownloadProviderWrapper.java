/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2025 huangyuhui <huanghongxun2008@126.com> and contributors
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
import com.tungsten.fclcore.task.Task;

import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * 以稳定引用代理当前下载源，设置变更时热替换内部实现，消费方无需重新获取。
 */
public final class DownloadProviderWrapper implements DownloadProvider {

    private volatile DownloadProvider provider;

    public DownloadProviderWrapper(DownloadProvider provider) {
        this.provider = provider;
    }

    public DownloadProvider getProvider() {
        return this.provider;
    }

    public void setProvider(DownloadProvider provider) {
        this.provider = Objects.requireNonNull(provider);
    }

    @Override
    public List<URL> getAssetObjectCandidates(String assetObjectLocation) {
        return getProvider().getAssetObjectCandidates(assetObjectLocation);
    }

    @Override
    public List<URL> getVersionListURLs() {
        return getProvider().getVersionListURLs();
    }

    @Override
    public String injectURL(String baseURL) {
        return getProvider().injectURL(baseURL);
    }

    @Override
    public List<URL> injectURLWithCandidates(String baseURL) {
        return getProvider().injectURLWithCandidates(baseURL);
    }

    @Override
    public List<URL> injectURLsWithCandidates(List<String> urls) {
        return getProvider().injectURLsWithCandidates(urls);
    }

    @Override
    public ComponentVersionList<?> getVersionList(GameComponentType componentType) {
        return new ComponentVersionList<>() {
            @Override
            public boolean hasType() {
                return getProvider().getVersionList(componentType).hasType();
            }

            @Override
            public Task<?> refreshAsync() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Task<?> refreshAsync(String gameVersion) {
                return getProvider().getVersionList(componentType).refreshAsync(gameVersion)
                        .thenRunAsync(() -> {
                            lock.writeLock().lock();
                            try {
                                versions.putAll(gameVersion, getProvider().getVersionList(componentType).getVersions(gameVersion));
                            } finally {
                                lock.writeLock().unlock();
                            }
                        });
            }
        };
    }

    @Override
    public int getConcurrency() {
        return getProvider().getConcurrency();
    }

    @Override
    public String toString() {
        return "DownloadProviderWrapper[provider=" + provider + "]";
    }
}
