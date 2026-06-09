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
package com.tungsten.fcl.game;

import static com.tungsten.fclcore.util.Lang.threadPool;
import static com.tungsten.fclcore.util.Logging.LOG;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

import com.tungsten.fcl.util.ResourceNotFoundError;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.ServerResponseMalformedException;
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccount;
import com.tungsten.fclcore.auth.offline.OfflineAccount;
import com.tungsten.fclcore.auth.offline.Skin;
import com.tungsten.fclcore.auth.yggdrasil.Texture;
import com.tungsten.fclcore.auth.yggdrasil.TextureModel;
import com.tungsten.fclcore.auth.yggdrasil.TextureType;
import com.tungsten.fclcore.auth.yggdrasil.YggdrasilAccount;
import com.tungsten.fclcore.auth.yggdrasil.YggdrasilService;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.binding.ObjectBinding;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.fakefx.BindingMapping;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public final class TexturesLoader {

    private TexturesLoader() {
    }

    // ==== Texture Loading ====
    public static class LoadedTexture {
        private final Bitmap image;
        private final Map<String, String> metadata;

        public LoadedTexture(Bitmap image, Map<String, String> metadata) {
            this.image = requireNonNull(image);
            this.metadata = requireNonNull(metadata);
        }

        public Bitmap getImage() {
            return image;
        }

        public Map<String, String> getMetadata() {
            return metadata;
        }
    }

    private static final ThreadPoolExecutor POOL = threadPool("TexturesDownload", true, 2, 10, TimeUnit.SECONDS);
    private static final Path TEXTURES_DIR = new File(FCLPath.FILES_DIR).toPath().resolve("assets").resolve("skins");

    private static Path getTexturePath(Texture texture) {
        String url = texture.getUrl();
        int slash = url.lastIndexOf('/');
        int dot = url.lastIndexOf('.');
        if (dot < slash) {
            dot = url.length();
        }
        String hash = url.substring(slash + 1, dot);
        String prefix = hash.length() > 2 ? hash.substring(0, 2) : "xx";
        return TEXTURES_DIR.resolve(prefix).resolve(hash);
    }

    public static LoadedTexture loadTexture(Texture texture, OfflineAccount... accounts) throws Exception {
        Map<String, String> metadata = texture.getMetadata();
        if (metadata == null) {
            metadata = emptyMap();
        }

        if (StringUtils.isBlank(texture.getUrl())) {
            throw new IOException("Texture url is empty");
        }

        if (texture.getUrl().equals("offline") && accounts.length == 1) {
            OfflineAccount account = accounts[0];
            Skin skin = account.getSkin();
            if (skin != null) {
                Skin.LoadedSkin loadedSkin = skin.load(account.getUsername()).run();
                if (loadedSkin != null) {
                    Bitmap img = loadedSkin.getSkin() == null ? null : loadedSkin.getSkin().getImage();
                    if (img == null) {
                        img = getDefaultSkin(TextureModel.detectUUID(account.getUUID())).getImage();
                    }
                    return new LoadedTexture(img, metadata);
                }
            }
            return getDefaultSkin(TextureModel.detectUUID(account.getUUID()));
        }

        Path file = getTexturePath(texture);
        if (!Files.isRegularFile(file)) {
            // download it
            try {
                new FileDownloadTask(new URL(texture.getUrl()), file.toFile()).run();
                LOG.info("Texture downloaded: " + texture.getUrl());
            } catch (Exception e) {
                if (Files.isRegularFile(file)) {
                    // concurrency conflict?
                    LOG.log(Level.WARNING, "Failed to download texture " + texture.getUrl() + ", but the file is available", e);
                } else {
                    throw new IOException("Failed to download texture " + texture.getUrl());
                }
            }
        }
        Bitmap img;
        try (InputStream in = Files.newInputStream(file)) {
            img = BitmapFactory.decodeStream(in);
        }
        if (img == null) {
            throw new IOException("Texture is malformed");
        }

        return new LoadedTexture(img, metadata);
    }

    private static Bitmap loadCape(Texture texture, OfflineAccount... accounts) throws Exception {
        if (StringUtils.isBlank(texture.getUrl())) {
            throw new Exception("Texture url is empty");
        }

        if (texture.getUrl().equals("offline") && accounts.length == 1) {
            OfflineAccount account = accounts[0];
            Skin skin = account.getSkin();
            if (skin != null) {
                Skin.LoadedSkin loadedSkin = skin.load(account.getUsername()).run();
                if (loadedSkin != null) {
                    return loadedSkin.getSkin() == null ? null : loadedSkin.getCape().getImage();
                }
            }
            return null;
        }

        Path file = getTexturePath(texture);
        if (!Files.isRegularFile(file)) {
            // download it
            try {
                new FileDownloadTask(new URL(texture.getUrl()), file.toFile()).run();
                LOG.info("Texture downloaded: " + texture.getUrl());
            } catch (Exception e) {
                if (Files.isRegularFile(file)) {
                    // concurrency conflict?
                    LOG.log(Level.WARNING, "Failed to download texture " + texture.getUrl() + ", but the file is available", e);
                } else {
                    throw new IOException("Failed to download texture " + texture.getUrl());
                }
            }
        }

        Bitmap img;
        try (InputStream in = Files.newInputStream(file)) {
            img = BitmapFactory.decodeStream(in);
        }
        if (img == null) {
            throw new IOException("Texture is malformed");
        }

        return img;
    }
    // ====

    // ==== Skins ====
    private final static Map<TextureModel, LoadedTexture> DEFAULT_SKINS = new EnumMap<>(TextureModel.class);

    static {
        loadDefaultSkin("/assets/img/steve.png", TextureModel.STEVE);
        loadDefaultSkin("/assets/img/alex.png", TextureModel.ALEX);
    }

    private static void loadDefaultSkin(String path, TextureModel model) {
        try (InputStream in = ResourceNotFoundError.getResourceAsStream(path)) {
            DEFAULT_SKINS.put(model, new LoadedTexture(BitmapFactory.decodeStream(in), singletonMap("model", model.modelName)));
        } catch (Throwable e) {
            throw new ResourceNotFoundError("Cannot load default skin from " + path, e);
        }
    }

    public static LoadedTexture getDefaultSkin(TextureModel model) {
        return DEFAULT_SKINS.get(model);
    }

    public static ObjectBinding<LoadedTexture> skinBinding(YggdrasilService service, UUID uuid) {
        LoadedTexture uuidFallback = getDefaultSkin(TextureModel.detectUUID(uuid));
        return BindingMapping.of(service.getProfileRepository().binding(uuid))
                .map(profile -> profile
                        .flatMap(it -> {
                            try {
                                return YggdrasilService.getTextures(it);
                            } catch (ServerResponseMalformedException e) {
                                LOG.log(Level.WARNING, "Failed to parse texture payload", e);
                                return Optional.empty();
                            }
                        })
                        .flatMap(it -> Optional.ofNullable(it.get(TextureType.SKIN)))
                        .filter(it -> StringUtils.isNotBlank(it.getUrl())))
                .asyncMap(it -> {
                    if (it.isPresent()) {
                        Texture texture = it.get();
                        return CompletableFuture.supplyAsync(() -> {
                            try {
                                return loadTexture(texture);
                            } catch (Exception e) {
                                LOG.log(Level.WARNING, "Failed to load texture " + texture.getUrl() + ", using fallback texture", e);
                                return uuidFallback;
                            }
                        }, POOL);
                    } else {
                        return CompletableFuture.completedFuture(uuidFallback);
                    }
                }, uuidFallback);
    }

    public static ObjectBinding<LoadedTexture> skinBinding(Account account) {
        LoadedTexture uuidFallback = getDefaultSkin(TextureModel.detectUUID(account.getUUID()));
        return BindingMapping.of(account.getTextures())
                .map(textures -> textures
                        .flatMap(it -> Optional.ofNullable(it.get(TextureType.SKIN)))
                        .filter(it -> StringUtils.isNotBlank(it.getUrl())))
                .asyncMap(it -> {
                    if (it.isPresent()) {
                        Texture texture = it.get();
                        return CompletableFuture.supplyAsync(() -> {
                            try {
                                if (account instanceof OfflineAccount) {
                                    return loadTexture(texture, (OfflineAccount) account);
                                }
                                return loadTexture(texture);
                            } catch (Exception e) {
                                LOG.log(Level.WARNING, "Failed to load texture " + texture.getUrl() + ", using fallback texture", e);
                                return uuidFallback;
                            }
                        }, POOL);
                    } else {
                        return CompletableFuture.completedFuture(uuidFallback);
                    }
                }, uuidFallback);
    }

    public static ObjectBinding<Bitmap[]> textureBinding(Account account) {
        Bitmap[] fallback = new Bitmap[] { getDefaultSkin(TextureModel.detectUUID(account.getUUID())).getImage(), null };
        return BindingMapping.of(account.getTextures())
                .asyncMap(it -> {
                    if (it.isPresent()) {
                        Texture skin = it.get().get(TextureType.SKIN);
                        Texture cape = it.get().get(TextureType.CAPE);
                        boolean loadSkin = skin != null && StringUtils.isNotBlank(skin.getUrl());
                        boolean loadCape = cape != null && StringUtils.isNotBlank(cape.getUrl());
                        return CompletableFuture.supplyAsync(() -> {
                            Bitmap finalSkin = getDefaultSkin(TextureModel.detectUUID(account.getUUID())).getImage();
                            Bitmap finalCape = null;
                            try {
                                if (loadSkin) {
                                    if (account instanceof OfflineAccount) {
                                        finalSkin = loadTexture(skin, (OfflineAccount) account).getImage();
                                        finalCape = loadCape(skin, (OfflineAccount) account);
                                    } else {
                                        finalSkin = loadTexture(skin).getImage();
                                    }
                                }
                                if (loadCape) {
                                    finalCape = loadCape(cape);
                                }
                            } catch (Exception e) {
                                LOG.log(Level.WARNING, "Failed to load texture, using default", e);
                            }
                            return new Bitmap[] { finalSkin, finalCape };
                        }, POOL);
                    } else {
                        return CompletableFuture.completedFuture(fallback);
                    }
                }, fallback);
    }

    // ====

    // ==== Avatar ====
    private static final Paint AVATAR_PAINT = new Paint();

    static {
        AVATAR_PAINT.setFilterBitmap(false);
    }

    public static Bitmap toAvatar(
            final Bitmap skin,
            final int pixelSize
    ) {
        float faceOffset = (float) Math.round(pixelSize / 18.0);
        float scaleFactor = skin.getWidth() / 64.0f;
        int faceSize = Math.round(8 * scaleFactor);
        int faceEnd = faceSize * 2;
        int hatSrcX = Math.round(40 * scaleFactor);
        Bitmap avatar = Bitmap.createBitmap(pixelSize, pixelSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(avatar);

        float innerEnd = pixelSize - faceOffset;
        canvas.drawBitmap(
                skin,
                new Rect(faceSize, faceSize, faceEnd, faceEnd),
                new RectF(faceOffset, faceOffset, innerEnd, innerEnd),
                AVATAR_PAINT
        );

        canvas.drawBitmap(
                skin,
                new Rect(hatSrcX, faceSize, hatSrcX + faceSize, faceEnd),
                new RectF(0f, 0f, pixelSize, pixelSize),
                AVATAR_PAINT
        );

        return avatar;
    }

    public static ObjectBinding<BitmapDrawable> avatarBinding(YggdrasilService service, UUID uuid, int size) {
        return BindingMapping.of(skinBinding(service, uuid))
                .map(it -> toAvatar(it.image, size))
                .map(BitmapDrawable::new);
    }

    public static ObjectBinding<BitmapDrawable> avatarBinding(Account account, int size) {
        if (account instanceof YggdrasilAccount || account instanceof MicrosoftAccount || account instanceof OfflineAccount) {
            return BindingMapping.of(skinBinding(account))
                    .map(it -> toAvatar(it.image, size))
                    .map(BitmapDrawable::new);
        } else {
            return Bindings.createObjectBinding(
                    () -> new BitmapDrawable(toAvatar(getDefaultSkin(account == null ? TextureModel.ALEX : TextureModel.detectUUID(account.getUUID())).image, size)));
        }
    }
    // ====
}
