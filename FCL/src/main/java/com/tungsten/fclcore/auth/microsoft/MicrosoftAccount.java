/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fclcore.auth.microsoft;

import static com.tungsten.fclcore.util.Logging.LOG;
import static java.util.Objects.requireNonNull;

import com.tungsten.fclcore.auth.AccountFactory;
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

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public class MicrosoftAccount extends OAuthAccount {

    protected final MicrosoftService service;
    protected UUID characterUUID;

    /** 登录进度回调，UI 线程注入、后台登录线程读取，故为 volatile */
    private volatile AccountFactory.ProgressCallback progressCallback = AccountFactory.ProgressCallback.NO_OP;

    private boolean authenticated = false;
    private MicrosoftSession session;

    protected MicrosoftAccount(MicrosoftService service, MicrosoftSession session) {
        this.service = requireNonNull(service);
        this.session = requireNonNull(session);
        this.characterUUID = requireNonNull(session.getProfile().getId());
    }

    /**
     * 注入登录进度回调，仅在登录任务进行期间由 UI 设置，任务结束后应置回 null。
     */
    public void setProgressCallback(AccountFactory.ProgressCallback progressCallback) {
        this.progressCallback = progressCallback != null ? progressCallback : AccountFactory.ProgressCallback.NO_OP;
    }

    protected MicrosoftAccount(MicrosoftService service, CharacterSelector characterSelector, AccountFactory.ProgressCallback progressCallback) throws AuthenticationException {
        this.service = requireNonNull(service);
        // 构造器内即开始首次认证，进度回调必须在认证发起前就位
        setProgressCallback(progressCallback);

        MicrosoftSession acquiredSession = service.authenticate(this.progressCallback);
        if (acquiredSession.getProfile() == null) {
            session = service.refresh(acquiredSession, this.progressCallback);
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
        if (!authenticated || System.currentTimeMillis() > session.getNotAfter()) {
            if (service.validate(session.getNotAfter(), session.getTokenType(), session.getAccessToken())) {
                authenticated = true;
            } else {
                MicrosoftSession acquiredSession = service.refresh(session, progressCallback);
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
        MicrosoftSession acquiredSession = service.authenticate(progressCallback);
        if (!Objects.equals(characterUUID, acquiredSession.getProfile().getId())) {
            throw new WrongAccountException(characterUUID, acquiredSession.getProfile().getId());
        }

        if (acquiredSession.getProfile() == null) {
            session = service.refresh(acquiredSession, progressCallback);
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

    /**
     * Upload a new skin from a local file.
     *
     * @param model skin model: "classic" for Steve, "slim" for Alex
     * @param file  path to the skin PNG file
     * @throws AuthenticationException on API errors
     */
    public void uploadSkin(String model, Path file) throws AuthenticationException {
        requireNonNull(model);
        requireNonNull(file);
        logIn();
        MinecraftSkinService.uploadSkin(session.getAccessToken(), model, file);
        clearCache();
    }

    /**
     * Reset the active skin (remove custom skin, revert to default).
     *
     * @throws AuthenticationException on API errors
     */
    public void resetSkin() throws AuthenticationException {
        logIn();
        MinecraftSkinService.resetSkin(session.getAccessToken());
        clearCache();
    }

    /**
     * Show a specific cape by setting it as active.
     *
     * @param capeId the UUID of the cape to show
     * @throws AuthenticationException on API errors
     */
    public void showCape(String capeId) throws AuthenticationException {
        requireNonNull(capeId);
        logIn();
        MinecraftSkinService.showCape(session.getAccessToken(), capeId);
        clearCache();
    }

    /**
     * Hide the active cape.
     *
     * @throws AuthenticationException on API errors
     */
    public void hideCape() throws AuthenticationException {
        logIn();
        MinecraftSkinService.hideCape(session.getAccessToken());
        clearCache();
    }

    /**
     * Get the full Minecraft profile including skin and cape details with IDs.
     *
     * @return the profile response with skins and capes lists
     * @throws AuthenticationException on API errors
     */
    public Optional<MicrosoftService.MinecraftProfileResponse> getProfile() throws AuthenticationException {
        logIn();
        return service.getCompleteProfile(session.getAuthorization());
    }

    @Override
    public void clearCache() {
        authenticated = false;
        service.getProfileRepository().invalidate(characterUUID);
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
