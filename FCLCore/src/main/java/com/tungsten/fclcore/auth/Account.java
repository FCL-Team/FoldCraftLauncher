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
package com.tungsten.fclcore.auth;

import com.tungsten.fclcore.auth.yggdrasil.Texture;
import com.tungsten.fclcore.auth.yggdrasil.TextureType;
import com.tungsten.fclcore.util.ToStringBuilder;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public abstract class Account {

    /**
     * @return the name of the account who owns the character
     */
    public abstract String getUsername();

    /**
     * @return the character name
     */
    public abstract String getCharacter();

    /**
     * @return the character UUID
     */
    public abstract UUID getUUID();

    /**
     * Login with stored credentials.
     *
     * @throws CredentialExpiredException when the stored credentials has expired, in which case a password login will be performed
     */
    public abstract AuthInfo logIn() throws AuthenticationException;

    /**
     * Play offline.
     * @return the specific offline player's info.
     */
    public abstract AuthInfo playOffline() throws AuthenticationException;

    public abstract Map<Object, Object> toStorage();

    public void clearCache() {
    }

    private final MutableStateFlow<Boolean> portable = StateFlowKt.MutableStateFlow(false);

    public StateFlow<Boolean> portableFlow() {
        return portable;
    }

    public boolean isPortable() {
        return portable.getValue();
    }

    public void setPortable(boolean value) {
        this.portable.setValue(value);
    }

    public abstract String getIdentifier();

    private final MutableStateFlow<Long> revision = StateFlowKt.MutableStateFlow(0L);

    /**
     * 账户内容变更信号：每次 {@link #invalidate()} 递增。
     *
     * <p>对齐原 {@code Observable} 失效语义，消费方（存盘、UI 刷新）应幂等。
     * Java 调用方用 {@link FlowSubscriptions#subscribe} 订阅；回调在发射线程执行，
     * 不做隐式线程切换。</p>
     */
    public StateFlow<Long> revisionFlow() {
        return revision;
    }

    /**
     * Called when the account has changed.
     * This method can be called from any thread.
     */
    protected void invalidate() {
        revision.setValue(revision.getValue() + 1);
    }

    private static final StateFlow<Optional<Map<TextureType, Texture>>> EMPTY_TEXTURES =
            StateFlowKt.MutableStateFlow(Optional.empty());

    /**
     * 该账户的皮肤/披风纹理。默认恒为空，子类按数据源覆写。
     */
    public StateFlow<Optional<Map<TextureType, Texture>>> texturesFlow() {
        return EMPTY_TEXTURES;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPortable());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Account))
            return false;

        Account another = (Account) obj;
        return isPortable() == another.isPortable();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", getUsername())
                .append("character", getCharacter())
                .append("uuid", getUUID())
                .append("portable", isPortable())
                .toString();
    }
}
