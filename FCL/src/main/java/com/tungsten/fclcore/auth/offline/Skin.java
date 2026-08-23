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
package com.tungsten.fclcore.auth.offline;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Lang.tryCast;
import static com.tungsten.fclcore.util.Pair.pair;

import com.tungsten.fclcore.auth.yggdrasil.TextureModel;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public record Skin(Type type, TextureModel textureModel, String localSkinPath,
                   String localCapePath) {

    public enum Type {
        DEFAULT,
        ALEX,
        STEVE,
        LOCAL_FILE,
        YGGDRASIL_API;

        public static Type fromStorage(String type) {
            return switch (type) {
                case "default" -> DEFAULT;
                case "alex" -> ALEX;
                case "steve" -> STEVE;
                case "local_file" -> LOCAL_FILE;
                case "yggdrasil_api" -> YGGDRASIL_API;
                default -> null;
            };
        }
    }

    private static Function<Type, InputStream> defaultSkinLoader = type -> switch (type) {
        case ALEX -> Skin.class.getResourceAsStream("/assets/img/alex.png");
        default -> Skin.class.getResourceAsStream("/assets/img/steve.png");
    };

    public static void registerDefaultSkinLoader(Function<Type, InputStream> defaultSkinLoader0) {
        defaultSkinLoader = defaultSkinLoader0;
    }

    @Override
    public TextureModel textureModel() {
        return textureModel == null ? TextureModel.STEVE : textureModel;
    }

    public Task<LoadedSkin> load() {
        switch (type) {
            case DEFAULT:
                return Task.supplyAsync(() -> null);
            case ALEX:
            case STEVE:
                if (defaultSkinLoader == null) {
                    return Task.supplyAsync(() -> null);
                }
                TextureModel model = type == Type.ALEX ? TextureModel.ALEX : TextureModel.STEVE;
                return Task.supplyAsync(() -> new LoadedSkin(model, Texture.loadTexture(defaultSkinLoader.apply(type)), null));
            case LOCAL_FILE:
                return Task.supplyAsync(() -> {
                    Texture skin = null, cape = null;
                    Optional<Path> skinPath = FileUtils.tryGetPath(localSkinPath);
                    Optional<Path> capePath = FileUtils.tryGetPath(localCapePath);
                    if (skinPath.isPresent())
                        skin = Texture.loadTexture(Files.newInputStream(skinPath.get()));
                    if (capePath.isPresent())
                        cape = Texture.loadTexture(Files.newInputStream(capePath.get()));
                    return new LoadedSkin(textureModel(), skin, cape);
                });
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Map<?, ?> toStorage() {
        return mapOf(
                pair("type", type.name().toLowerCase(Locale.ROOT)),
                pair("textureModel", textureModel().modelName),
                pair("localSkinPath", localSkinPath),
                pair("localCapePath", localCapePath)
        );
    }

    public static Skin fromStorage(Map<?, ?> storage) {
        if (storage == null) return null;

        Type type = tryCast(storage.get("type"), String.class).flatMap(t -> Optional.ofNullable(Type.fromStorage(t)))
                .orElse(Type.DEFAULT);
        String textureModel = tryCast(storage.get("textureModel"), String.class).orElse("default");
        String localSkinPath = tryCast(storage.get("localSkinPath"), String.class).orElse(null);
        String localCapePath = tryCast(storage.get("localCapePath"), String.class).orElse(null);

        TextureModel model;
        if ("default".equals(textureModel)) {
            model = TextureModel.STEVE;
        } else if ("slim".equals(textureModel)) {
            model = TextureModel.ALEX;
        } else {
            model = TextureModel.STEVE;
        }

        return new Skin(type, model, localSkinPath, localCapePath);
    }

    public record LoadedSkin(TextureModel model, Texture skin, Texture cape) {
    }
}
