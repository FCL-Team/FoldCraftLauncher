package com.tungsten.fclcore.auth;

import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.LaunchOptions;

import java.io.IOException;
import java.util.UUID;

public class AuthInfo implements AutoCloseable {

    private final String username;
    private final UUID uuid;
    private final String accessToken;
    private final String userProperties;

    public AuthInfo(String username, UUID uuid, String accessToken, String userProperties) {
        this.username = username;
        this.uuid = uuid;
        this.accessToken = accessToken;
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
