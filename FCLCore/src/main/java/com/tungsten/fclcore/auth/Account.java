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
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.binding.ObjectBinding;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.util.ToStringBuilder;
import com.tungsten.fclcore.util.fakefx.ObservableHelper;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public abstract class Account implements Observable {

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

    private final BooleanProperty portable = new SimpleBooleanProperty(false);

    public BooleanProperty portableProperty() {
        return portable;
    }

    public boolean isPortable() {
        return portable.get();
    }

    public void setPortable(boolean value) {
        this.portable.set(value);
    }

    public abstract String getIdentifier();

    private final ObservableHelper helper = new ObservableHelper(this);

    @Override
    public void addListener(InvalidationListener listener) {
        helper.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper.removeListener(listener);
    }

    /**
     * Called when the account has changed.
     * This method can be called from any thread.
     */
    protected void invalidate() {
        helper.invalidate();
    }

    public ObjectBinding<Optional<Map<TextureType, Texture>>> getTextures() {
        return Bindings.createObjectBinding(Optional::empty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portable);
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
