package com.tungsten.fclcore.auth.yggdrasil;

import com.tungsten.fclcore.util.gson.UUIDTypeAdapter;
import com.tungsten.fclcore.util.io.NetworkUtils;

import java.net.URL;
import java.util.*;

public class MojangYggdrasilProvider implements YggdrasilProvider {

    @Override
    public URL getAuthenticationURL() {
        return NetworkUtils.toURL("https://authserver.mojang.com/authenticate");
    }

    @Override
    public URL getRefreshmentURL() {
        return NetworkUtils.toURL("https://authserver.mojang.com/refresh");
    }

    @Override
    public URL getValidationURL() {
        return NetworkUtils.toURL("https://authserver.mojang.com/validate");
    }

    @Override
    public URL getInvalidationURL() {
        return NetworkUtils.toURL("https://authserver.mojang.com/invalidate");
    }

    @Override
    public URL getSkinUploadURL(UUID uuid) throws UnsupportedOperationException {
        return NetworkUtils.toURL("https://api.mojang.com/user/profile/" + UUIDTypeAdapter.fromUUID(uuid) + "/skin");
    }

    @Override
    public URL getProfilePropertiesURL(UUID uuid) {
        return NetworkUtils.toURL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDTypeAdapter.fromUUID(uuid));
    }

    @Override
    public String toString() {
        return "mojang";
    }
}
