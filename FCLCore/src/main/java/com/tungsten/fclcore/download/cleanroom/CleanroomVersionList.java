package com.tungsten.fclcore.download.cleanroom;

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

import static com.tungsten.fclcore.util.Lang.wrap;

import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.download.VersionList;
import com.tungsten.fclcore.task.GetTask;
import com.tungsten.fclcore.util.gson.JsonUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public final class CleanroomVersionList extends VersionList<CleanroomRemoteVersion> {
    private final DownloadProvider downloadProvider;
    private static final String LOADER_LIST_URL = "https://hmcl-dev.github.io/metadata/cleanroom/index.json";
    private static final String INSTALLER_URL = "https://hmcl-dev.github.io/metadata/cleanroom/files/cleanroom-%s-installer.jar";

    public CleanroomVersionList(DownloadProvider downloadProvider) {
        this.downloadProvider = downloadProvider;
    }

    @Override
    public boolean hasType() {
        return false;
    }

    @Override
    public CompletableFuture<?> refreshAsync() {
        return CompletableFuture.completedFuture((Void) null)
                .thenApplyAsync(wrap(unused -> {
                    GetTask task = new GetTask(downloadProvider.injectURLWithCandidates(LOADER_LIST_URL));
                    task.execute();
                    String result = task.getResult();
                    return JsonUtils.GSON.fromJson(result, ReleaseResult[].class);
                }))
                .thenAcceptAsync(results -> {
                    lock.writeLock().lock();

                    try {
                        versions.clear();
                        for (ReleaseResult version : results) {
                            versions.put("1.12.2", new CleanroomRemoteVersion(
                                    "1.12.2", version.name, Instant.parse(version.created_at),
                                    Collections.singletonList(
                                            String.format(INSTALLER_URL, version.name)
                                    )
                            ));
                        }
                    } finally {
                        lock.writeLock().unlock();
                    }
                });
    }

    private final static class ReleaseResult {
        String name;
        String created_at;
    }
}
