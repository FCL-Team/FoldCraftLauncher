package com.tungsten.fclcore.auth.offline;

import static com.tungsten.fclcore.util.Lang.tryCast;

import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.tungsten.fclcore.auth.AccountFactory;
import com.tungsten.fclcore.auth.CharacterSelector;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorArtifactProvider;
import com.tungsten.fclcore.util.gson.UUIDTypeAdapter;

public final class OfflineAccountFactory extends AccountFactory<OfflineAccount> {
    private final AuthlibInjectorArtifactProvider downloader;

    public OfflineAccountFactory(AuthlibInjectorArtifactProvider downloader) {
        this.downloader = downloader;
    }

    @Override
    public AccountLoginType getLoginType() {
        return AccountLoginType.USERNAME;
    }

    public OfflineAccount create(String username, UUID uuid) {
        return new OfflineAccount(downloader, username, uuid, null);
    }

    @Override
    public OfflineAccount create(CharacterSelector selector, String username, String password, ProgressCallback progressCallback, Object additionalData) {
        AdditionalData data;
        UUID uuid;
        Skin skin;
        if (additionalData != null) {
            data = (AdditionalData) additionalData;
            uuid = data.uuid == null ? getUUIDFromUserName(username) : data.uuid;
            skin = data.skin;
        } else {
            uuid = getUUIDFromUserName(username);
            skin = null;
        }
        return new OfflineAccount(downloader, username, uuid, skin);
    }

    @Override
    public OfflineAccount fromStorage(Map<Object, Object> storage) {
        String username = tryCast(storage.get("username"), String.class)
                .orElseThrow(() -> new IllegalStateException("Offline account configuration malformed."));
        UUID uuid = tryCast(storage.get("uuid"), String.class)
                .map(UUIDTypeAdapter::fromString)
                .orElse(getUUIDFromUserName(username));
        Skin skin = Skin.fromStorage(tryCast(storage.get("skin"), Map.class).orElse(null));

        return new OfflineAccount(downloader, username, uuid, skin);
    }

    public static UUID getUUIDFromUserName(String username) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(UTF_8));
    }

    public static class AdditionalData {
        private final UUID uuid;
        private final Skin skin;

        public AdditionalData(UUID uuid, Skin skin) {
            this.uuid = uuid;
            this.skin = skin;
        }
    }

}
