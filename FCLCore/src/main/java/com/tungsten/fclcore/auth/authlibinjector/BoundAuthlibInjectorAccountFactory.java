package com.tungsten.fclcore.auth.authlibinjector;

import com.tungsten.fclcore.auth.AccountFactory;
import com.tungsten.fclcore.auth.AuthenticationException;
import com.tungsten.fclcore.auth.CharacterSelector;

import java.util.Map;
import java.util.Objects;

public class BoundAuthlibInjectorAccountFactory extends AccountFactory<AuthlibInjectorAccount> {
    private final AuthlibInjectorArtifactProvider downloader;
    private final AuthlibInjectorServer server;

    /**
     * @param server Authlib-Injector Server
     */
    public BoundAuthlibInjectorAccountFactory(AuthlibInjectorArtifactProvider downloader, AuthlibInjectorServer server) {
        this.downloader = downloader;
        this.server = server;
    }

    @Override
    public AccountLoginType getLoginType() {
        return AccountLoginType.USERNAME_PASSWORD;
    }

    public AuthlibInjectorServer getServer() {
        return server;
    }

    @Override
    public AuthlibInjectorAccount create(CharacterSelector selector, String username, String password, ProgressCallback progressCallback, Object additionalData) throws AuthenticationException {
        Objects.requireNonNull(selector);
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        return new AuthlibInjectorAccount(server, downloader, username, password, selector);
    }

    @Override
    public AuthlibInjectorAccount fromStorage(Map<Object, Object> storage) {
        return AuthlibInjectorAccountFactory.fromStorage(storage, downloader, server);
    }
}
