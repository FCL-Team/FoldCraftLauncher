/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2026 huangyuhui <huanghongxun2008@126.com> and contributors
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
package com.tungsten.fcl.terracotta;

import static com.tungsten.fclcore.util.Logging.LOG;

import com.google.gson.JsonParseException;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.gson.TolerableValidationException;
import com.tungsten.fclcore.util.gson.Validation;
import com.tungsten.fclcore.util.io.HttpRequest;
import com.tungsten.fcllibrary.util.LocaleUtils;

import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

/// @author Glavo
public final class TerracottaNodeList {
    private static final String NODE_LIST_URL = "https://terracotta.glavo.site/nodes";

    private static final class TerracottaNode implements Validation {
        private final String url;
        @Nullable
        private final String region;

        TerracottaNode(String url, @Nullable String region) {
            this.url = url;
            this.region = region;
        }

        String url() {
            return url;
        }

        @Nullable
        String region() {
            return region;
        }

        @Override
        public void validate() throws JsonParseException, TolerableValidationException {
            Validation.requireNonNull(url, "TerracottaNode.url cannot be null");
            try {
                new URI(url);
            } catch (URISyntaxException e) {
                throw new JsonParseException("Invalid URL: " + url, e);
            }
        }
    }

    private static volatile List<URI> list;

    public static List<URI> fetch() {
        List<URI> local = TerracottaNodeList.list;
        if (local != null) {
            return local;
        }

        synchronized (TerracottaNodeList.class) {
            local = TerracottaNodeList.list;
            if (local != null) {
                return local;
            }

            try {
                List<TerracottaNode> nodes = HttpRequest.GET(NODE_LIST_URL)
                        .getJson(JsonUtils.listTypeOf(TerracottaNode.class));

                if (nodes == null) {
                    local = List.of();
                    LOG.log(Level.INFO, "No available Terracotta nodes found");
                } else {
                    List<URI> tmp = nodes.stream()
                            .filter(node -> {
                                if (node == null)
                                    return false;

                                try {
                                    node.validate();
                                } catch (Exception e) {
                                    LOG.log(Level.WARNING, "Invalid terracotta node: " + node, e);
                                    return false;
                                }

                                return StringUtils.isBlank(node.region())
                                        || (LocaleUtils.IS_CHINA_MAINLAND
                                        == "CN".equalsIgnoreCase(node.region()));
                            })
                            .map(node -> URI.create(node.url()))
                            .collect(Collectors.toList());

                    local = List.copyOf(tmp);
                    LOG.log(Level.INFO, "Terracotta node list: " + local);
                }
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Failed to fetch terracotta node list", e);
                local = List.of();
            }

            TerracottaNodeList.list = local;
            return local;
        }
    }

    private TerracottaNodeList() {
    }
}