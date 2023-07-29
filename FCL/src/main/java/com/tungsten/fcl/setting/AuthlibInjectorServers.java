package com.tungsten.fcl.setting;

import static com.tungsten.fcl.setting.ConfigHolder.config;
import static com.tungsten.fclcore.util.Logging.LOG;

import com.google.gson.JsonParseException;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.gson.Validation;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class AuthlibInjectorServers implements Validation {

    public static final String CONFIG_FILENAME = "authlib-injectors.json";

    private final List<String> urls;

    public AuthlibInjectorServers(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getUrls() {
        return urls;
    }

    @Override
    public void validate() throws JsonParseException {
        if (urls == null)
            throw new JsonParseException("authlib-injectors.json -> urls cannot be null");
    }

    private static final Path configLocation = new File(FCLPath.FILES_DIR + "/" + CONFIG_FILENAME).toPath();
    private static AuthlibInjectorServers configInstance;

    public synchronized static void init() {
        if (configInstance != null) {
            throw new IllegalStateException("AuthlibInjectorServers is already loaded");
        }

        configInstance = new AuthlibInjectorServers(Collections.emptyList());

        if (Files.exists(configLocation)) {
            try {
                String content = FileUtils.readText(configLocation);
                configInstance = JsonUtils.GSON.fromJson(content, AuthlibInjectorServers.class);
            } catch (IOException | JsonParseException e) {
                LOG.log(Level.WARNING, "Malformed authlib-injectors.json", e);
            }
        }

        if (ConfigHolder.isNewlyCreated() && !AuthlibInjectorServers.getConfigInstance().getUrls().isEmpty()) {
            config().setPreferredLoginType(Accounts.getLoginType(Accounts.FACTORY_AUTHLIB_INJECTOR));
            for (String url : AuthlibInjectorServers.getConfigInstance().getUrls()) {
                Task.supplyAsync(Schedulers.io(), () -> AuthlibInjectorServer.locateServer(url))
                        .thenAcceptAsync(Schedulers.androidUIThread(), server -> config().getAuthlibInjectorServers().add(server))
                        .start();
            }
        }
    }

    public static AuthlibInjectorServers getConfigInstance() {
        return configInstance;
    }
}
