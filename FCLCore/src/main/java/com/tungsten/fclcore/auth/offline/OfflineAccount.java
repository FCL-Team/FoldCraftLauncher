package com.tungsten.fclcore.auth.offline;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Pair.pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.requireNonNull;

import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.auth.AuthenticationException;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorArtifactInfo;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorArtifactProvider;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorDownloadException;
import com.tungsten.fclcore.auth.yggdrasil.Texture;
import com.tungsten.fclcore.auth.yggdrasil.TextureModel;
import com.tungsten.fclcore.auth.yggdrasil.TextureType;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.binding.ObjectBinding;
import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.LaunchOptions;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.ToStringBuilder;
import com.tungsten.fclcore.util.gson.UUIDTypeAdapter;

public class OfflineAccount extends Account {

    private final AuthlibInjectorArtifactProvider downloader;
    private final String username;
    private final UUID uuid;
    private Skin skin;

    protected OfflineAccount(AuthlibInjectorArtifactProvider downloader, String username, UUID uuid, Skin skin) {
        this.downloader = requireNonNull(downloader);
        this.username = requireNonNull(username);
        this.uuid = requireNonNull(uuid);
        this.skin = skin;

        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
    }

    public AuthlibInjectorArtifactProvider getDownloader() {
        return downloader;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getCharacter() {
        return username;
    }

    @Override
    public String getIdentifier() {
        return username + ":" + username;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
        invalidate();
    }

    protected boolean loadAuthlibInjector(Skin skin) {
        if (skin == null) return false;
        if (skin.getType() == Skin.Type.DEFAULT) return false;
        TextureModel defaultModel = TextureModel.detectUUID(getUUID());
        if (skin.getType() == Skin.Type.ALEX && defaultModel == TextureModel.ALEX ||
            skin.getType() == Skin.Type.STEVE && defaultModel == TextureModel.STEVE) {
            return false;
        }
        return true;
    }

    @Override
    public AuthInfo logIn() throws AuthenticationException {
        // Using "legacy" user type here because "mojang" user type may cause "invalid session token" or "disconnected" when connecting to a game server.
        AuthInfo authInfo = new AuthInfo(username, uuid, UUIDTypeAdapter.fromUUID(UUID.randomUUID()), AuthInfo.USER_TYPE_MSA, "{}");

        if (loadAuthlibInjector(skin)) {
            CompletableFuture<AuthlibInjectorArtifactInfo> artifactTask = CompletableFuture.supplyAsync(() -> {
                try {
                    return downloader.getArtifactInfo();
                } catch (IOException e) {
                    throw new CompletionException(new AuthlibInjectorDownloadException(e));
                }
            });

            AuthlibInjectorArtifactInfo artifact;
            try {
                artifact = artifactTask.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AuthenticationException(e);
            } catch (ExecutionException e) {
                if (e.getCause() instanceof AuthenticationException) {
                    throw (AuthenticationException) e.getCause();
                } else {
                    throw new AuthenticationException(e.getCause());
                }
            }

            try {
                return new OfflineAuthInfo(authInfo, artifact);
            } catch (Exception e) {
                throw new AuthenticationException(e);
            }
        } else {
            return authInfo;
        }
    }

    private class OfflineAuthInfo extends AuthInfo {
        private final AuthlibInjectorArtifactInfo artifact;
        private YggdrasilServer server;

        public OfflineAuthInfo(AuthInfo authInfo, AuthlibInjectorArtifactInfo artifact) {
            super(authInfo.getUsername(), authInfo.getUUID(), authInfo.getAccessToken(), USER_TYPE_MSA, authInfo.getUserProperties());

            this.artifact = artifact;
        }

        @Override
        public Arguments getLaunchArguments(LaunchOptions options) throws IOException {

            server = new YggdrasilServer(0);
            server.start();

            try {
                server.addCharacter(new YggdrasilServer.Character(uuid, username,
                        skin != null ? skin.load(username).run() : null));
            } catch (IOException e) {
                // ignore
            } catch (Exception e) {
                throw new IOException(e);
            }

            return new Arguments().addJVMArguments(
                    "-javaagent:" + artifact.getLocation().toString() + "=" + "http://localhost:" + server.getListeningPort(),
                    "-Dauthlibinjector.side=client"
            );
        }

        @Override
        public void close() throws Exception {
            super.close();

            if (server != null)
                server.stop();
        }
    }

    @Override
    public AuthInfo playOffline() throws AuthenticationException {
        return logIn();
    }

    @Override
    public Map<Object, Object> toStorage() {
        return mapOf(
                pair("uuid", UUIDTypeAdapter.fromUUID(uuid)),
                pair("username", username),
                pair("skin", skin == null ? null : skin.toStorage())
        );
    }

    @Override
    public ObjectBinding<Optional<Map<TextureType, Texture>>> getTextures() {
        Map<TextureType, Texture> map = new HashMap<>();
        map.put(TextureType.SKIN, new Texture("offline", null));
        return Bindings.createObjectBinding(() -> Optional.of(map));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .append("uuid", uuid)
                .toString();
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflineAccount))
            return false;
        OfflineAccount another = (OfflineAccount) obj;
        return isPortable() == another.isPortable() && username.equals(another.username);
    }
}
