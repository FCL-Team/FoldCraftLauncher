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
package com.tungsten.fclcore.download.optifine;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fclcore.download.VersionList;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.HttpRequest;
import com.tungsten.fclcore.util.versioning.VersionNumber;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class OptiFineBMCLVersionList extends VersionList<OptiFineRemoteVersion> {
    private final String apiRoot;

    /**
     * @param apiRoot API Root of BMCLAPI implementations
     */
    public OptiFineBMCLVersionList(String apiRoot) {
        this.apiRoot = apiRoot;
    }

    @Override
    public boolean hasType() {
        return true;
    }

    @Override
    public CompletableFuture<?> refreshAsync() {
        return HttpRequest.GET(apiRoot + "/optifine/versionlist").<List<OptiFineVersion>>getJsonAsync(new TypeToken<List<OptiFineVersion>>() {
        }.getType()).thenAcceptAsync(root -> {
            lock.writeLock().lock();

            try {
                versions.clear();
                Set<String> duplicates = new HashSet<>();
                for (OptiFineVersion element : root) {
                    String version = element.type + "_" + element.patch;
                    String mirror = apiRoot + "/optifine/" + element.gameVersion + "/" + element.type + "/" + element.patch;
                    if (!duplicates.add(mirror))
                        continue;

                    boolean isPre = element.patch != null && (element.patch.startsWith("pre") || element.patch.startsWith("alpha"));

                    if (StringUtils.isBlank(element.gameVersion))
                        continue;

                    String gameVersion = VersionNumber.normalize(element.gameVersion);
                    versions.put(gameVersion, new OptiFineRemoteVersion(gameVersion, version, Collections.singletonList(mirror), isPre));
                }
            } finally {
                lock.writeLock().unlock();
            }
        });
    }

    private static final class OptiFineVersion {
        @SerializedName("type")
        private final String type;

        @SerializedName("patch")
        private final String patch;

        @SerializedName("mcversion")
        private final String gameVersion;

        public OptiFineVersion(String type, String patch, String gameVersion) {
            this.type = type;
            this.patch = patch;
            this.gameVersion = gameVersion;
        }
    }
}