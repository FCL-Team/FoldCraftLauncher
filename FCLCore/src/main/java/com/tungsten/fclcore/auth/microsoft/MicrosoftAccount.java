package com.tungsten.fclcore.auth.microsoft;

import static com.tungsten.fclcore.util.Logging.LOG;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import static java.util.Objects.requireNonNull;

import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.auth.AuthenticationException;
import com.tungsten.fclcore.auth.CharacterSelector;
import com.tungsten.fclcore.auth.OAuthAccount;
import com.tungsten.fclcore.auth.ServerResponseMalformedException;
import com.tungsten.fclcore.auth.yggdrasil.Texture;
import com.tungsten.fclcore.auth.yggdrasil.TextureType;
import com.tungsten.fclcore.auth.yggdrasil.YggdrasilService;
import com.tungsten.fclcore.fakefx.beans.binding.ObjectBinding;
import com.tungsten.fclcore.util.fakefx.BindingMapping;

public class MicrosoftAccount extends OAuthAccount {

    protected final MicrosoftService service;
    protected UUID characterUUID;

    private boolean authenticated = false;
    private MicrosoftSession session;

    protected MicrosoftAccount(MicrosoftService service, MicrosoftSession session) {
        this.service = requireNonNull(service);
        this.session = requireNonNull(session);
        this.characterUUID = requireNonNull(session.getProfile().getId());
    }

    protected MicrosoftAccount(MicrosoftService service, CharacterSelector characterSelector) throws AuthenticationException {
        this.service = requireNonNull(service);

        MicrosoftSession acquiredSession = service.authenticate();
        if (acquiredSession.getProfile() == null) {
            session = service.refresh(acquiredSession);
        } else {
            session = acquiredSession;
        }

        characterUUID = session.getProfile().getId();
        authenticated = true;
    }

    @Override
    public String getUsername() {
        // TODO: email of Microsoft account is blocked by oauth.
        return "";
    }

    @Override
    public String getCharacter() {
        return session.getProfile().getName();
    }

    @Override
    public UUID getUUID() {
        return session.getProfile().getId();
    }

    @Override
    public String getIdentifier() {
        return "microsoft:" + getUUID();
    }

    @Override
    public AuthInfo logIn() throws AuthenticationException {
        if (!authenticated) {
            if (service.validate(session.getNotAfter(), session.getTokenType(), session.getAccessToken())) {
                authenticated = true;
            } else {
                MicrosoftSession acquiredSession = service.refresh(session);
                if (!Objects.equals(acquiredSession.getProfile().getId(), session.getProfile().getId())) {
                    throw new ServerResponseMalformedException("Selected profile changed");
                }

                session = acquiredSession;

                authenticated = true;
                invalidate();
            }
        }

        return session.toAuthInfo();
    }

    @Override
    public AuthInfo logInWhenCredentialsExpired() throws AuthenticationException {
        MicrosoftSession acquiredSession = service.authenticate();
        if (!Objects.equals(characterUUID, acquiredSession.getProfile().getId())) {
            throw new WrongAccountException(characterUUID, acquiredSession.getProfile().getId());
        }

        if (acquiredSession.getProfile() == null) {
            session = service.refresh(acquiredSession);
        } else {
            session = acquiredSession;
        }

        authenticated = true;
        invalidate();
        return session.toAuthInfo();
    }

    @Override
    public AuthInfo playOffline() {
        return session.toAuthInfo();
    }

    @Override
    public Map<Object, Object> toStorage() {
        return session.toStorage();
    }

    public MicrosoftService getService() {
        return service;
    }

    @Override
    public ObjectBinding<Optional<Map<TextureType, Texture>>> getTextures() {
        return BindingMapping.of(service.getProfileRepository().binding(getUUID()))
                .map(profile -> profile.flatMap(it -> {
                    try {
                        return YggdrasilService.getTextures(it);
                    } catch (ServerResponseMalformedException e) {
                        LOG.log(Level.WARNING, "Failed to parse texture payload", e);
                        return Optional.empty();
                    }
                }));
    }

    @Override
    public void clearCache() {
        authenticated = false;
    }

    @Override
    public String toString() {
        return "MicrosoftAccount[uuid=" + characterUUID + ", name=" + getCharacter() + "]";
    }

    @Override
    public int hashCode() {
        return characterUUID.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MicrosoftAccount that = (MicrosoftAccount) o;
        return this.isPortable() == that.isPortable() && characterUUID.equals(that.characterUUID);
    }
}
