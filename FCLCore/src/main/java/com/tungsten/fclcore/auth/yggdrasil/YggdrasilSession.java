package com.tungsten.fclcore.auth.yggdrasil;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Lang.tryCast;
import static com.tungsten.fclcore.util.Pair.pair;

import com.google.gson.Gson;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.gson.UUIDTypeAdapter;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class YggdrasilSession {

    private final String clientToken;
    private final String accessToken;
    private final GameProfile selectedProfile;
    private final List<GameProfile> availableProfiles;
    @Nullable
    private final Map<String, String> userProperties;

    public YggdrasilSession(String clientToken, String accessToken, GameProfile selectedProfile, List<GameProfile> availableProfiles, Map<String, String> userProperties) {
        this.clientToken = clientToken;
        this.accessToken = accessToken;
        this.selectedProfile = selectedProfile;
        this.availableProfiles = availableProfiles;
        this.userProperties = userProperties;

        if (accessToken != null) Logging.registerAccessToken(accessToken);
    }

    public String getClientToken() {
        return clientToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @return nullable (null if no character is selected)
     */
    public GameProfile getSelectedProfile() {
        return selectedProfile;
    }

    /**
     * @return nullable (null if the YggdrasilSession is loaded from storage)
     */
    public List<GameProfile> getAvailableProfiles() {
        return availableProfiles;
    }

    public Map<String, String> getUserProperties() {
        return userProperties;
    }

    public static YggdrasilSession fromStorage(Map<?, ?> storage) {
        Objects.requireNonNull(storage);

        UUID uuid = tryCast(storage.get("uuid"), String.class).map(UUIDTypeAdapter::fromString).orElseThrow(() -> new IllegalArgumentException("uuid is missing"));
        String name = tryCast(storage.get("displayName"), String.class).orElseThrow(() -> new IllegalArgumentException("displayName is missing"));
        String clientToken = tryCast(storage.get("clientToken"), String.class).orElseThrow(() -> new IllegalArgumentException("clientToken is missing"));
        String accessToken = tryCast(storage.get("accessToken"), String.class).orElseThrow(() -> new IllegalArgumentException("accessToken is missing"));
        @SuppressWarnings("unchecked")
        Map<String, String> userProperties = tryCast(storage.get("userProperties"), Map.class).orElse(null);
        return new YggdrasilSession(clientToken, accessToken, new GameProfile(uuid, name), null, userProperties);
    }

    public Map<Object, Object> toStorage() {
        if (selectedProfile == null)
            throw new IllegalStateException("No character is selected");

        return mapOf(
                pair("clientToken", clientToken),
                pair("accessToken", accessToken),
                pair("uuid", UUIDTypeAdapter.fromUUID(selectedProfile.getId())),
                pair("displayName", selectedProfile.getName()),
                pair("userProperties", userProperties));
    }

    public AuthInfo toAuthInfo() {
        if (selectedProfile == null)
            throw new IllegalStateException("No character is selected");

        return new AuthInfo(selectedProfile.getName(), selectedProfile.getId(), accessToken, AuthInfo.USER_TYPE_MSA,
                Optional.ofNullable(userProperties)
                        .map(properties -> properties.entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey,
                                        e -> Collections.singleton(e.getValue()))))
                        .map(GSON_PROPERTIES::toJson).orElse("{}"));
    }

    private static final Gson GSON_PROPERTIES = new Gson();
}
