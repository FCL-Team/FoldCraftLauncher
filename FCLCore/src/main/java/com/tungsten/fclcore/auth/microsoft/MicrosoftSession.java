package com.tungsten.fclcore.auth.microsoft;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Lang.tryCast;
import static com.tungsten.fclcore.util.Pair.pair;

import java.util.Map;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.gson.UUIDTypeAdapter;

public class MicrosoftSession {
    private final String tokenType;
    private final long notAfter;
    private final String accessToken;
    private final String refreshToken;
    private final User user;
    private final GameProfile profile;

    public MicrosoftSession(String tokenType, String accessToken, long notAfter, String refreshToken, User user, GameProfile profile) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.notAfter = notAfter;
        this.refreshToken = refreshToken;
        this.user = user;
        this.profile = profile;

        if (accessToken != null) Logging.registerAccessToken(accessToken);
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getNotAfter() {
        return notAfter;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAuthorization() {
        return String.format("%s %s", getTokenType(), getAccessToken());
    }

    public User getUser() {
        return user;
    }

    public GameProfile getProfile() {
        return profile;
    }

    public static MicrosoftSession fromStorage(Map<?, ?> storage) {
        UUID uuid = tryCast(storage.get("uuid"), String.class).map(UUIDTypeAdapter::fromString)
                .orElseThrow(() -> new IllegalArgumentException("uuid is missing"));
        String name = tryCast(storage.get("displayName"), String.class)
                .orElseThrow(() -> new IllegalArgumentException("displayName is missing"));
        String tokenType = tryCast(storage.get("tokenType"), String.class)
                .orElseThrow(() -> new IllegalArgumentException("tokenType is missing"));
        String accessToken = tryCast(storage.get("accessToken"), String.class)
                .orElseThrow(() -> new IllegalArgumentException("accessToken is missing"));
        String refreshToken = tryCast(storage.get("refreshToken"), String.class)
                .orElseThrow(() -> new IllegalArgumentException("refreshToken is missing"));
        Long notAfter = tryCast(storage.get("notAfter"), Long.class).orElse(0L);
        String userId = tryCast(storage.get("userid"), String.class)
                .orElseThrow(() -> new IllegalArgumentException("userid is missing"));
        return new MicrosoftSession(tokenType, accessToken, notAfter, refreshToken, new User(userId), new GameProfile(uuid, name));
    }

    public Map<Object, Object> toStorage() {
        requireNonNull(profile);
        requireNonNull(user);

        return mapOf(
                pair("uuid", UUIDTypeAdapter.fromUUID(profile.getId())),
                pair("displayName", profile.getName()),
                pair("tokenType", tokenType),
                pair("accessToken", accessToken),
                pair("refreshToken", refreshToken),
                pair("notAfter", notAfter),
                pair("userid", user.id));
    }

    public AuthInfo toAuthInfo() {
        requireNonNull(profile);

        return new AuthInfo(profile.getName(), profile.getId(), accessToken, AuthInfo.USER_TYPE_MSA, "{}");
    }

    public static class User {
        private final String id;

        public User(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public static class GameProfile {
        private final UUID id;
        private final String name;

        public GameProfile(UUID id, String name) {
            this.id = id;
            this.name = name;
        }

        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
