package com.tungsten.fclcore.auth;

import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.LaunchOptions;

import java.io.IOException;
import java.util.UUID;

public class AuthInfo implements AutoCloseable {
    public static final String USER_TYPE_MSA = "msa";
    public static final String USER_TYPE_MOJANG = "mojang";
    public static final String USER_TYPE_LEGACY = "legacy";


    private final String username;
    private final UUID uuid;
    private final String accessToken;
    private final String userType;
    private final String userProperties;

    public AuthInfo(String username, UUID uuid, String accessToken, String userType, String userProperties) {
        this.username = username;
        this.uuid = uuid;
        this.accessToken = accessToken;
        this.userType = userType;
        this.userProperties = userProperties;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUserType() {
        return userType;
    }

    /**
     * Properties of this user.
     * Don't know the difference between user properties and user property map.
     *
     * @return the user property map in JSON.
     */
    public String getUserProperties() {
        return userProperties;
    }

    /**
     * Called when launching game.
     * @return null if no argument is specified
     */
    public Arguments getLaunchArguments(LaunchOptions options) throws IOException {
        return null;
    }

    @Override
    public void close() throws Exception {
    }
}
