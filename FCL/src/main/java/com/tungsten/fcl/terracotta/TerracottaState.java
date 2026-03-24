/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2025  huangyuhui <huanghongxun2008@126.com> and contributors
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
package com.tungsten.fcl.terracotta;

import androidx.annotation.NonNull;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.tungsten.fcl.terracotta.profile.TerracottaProfile;
import com.tungsten.fclcore.util.gson.JsonSubtype;
import com.tungsten.fclcore.util.gson.JsonType;
import com.tungsten.fclcore.util.gson.TolerableValidationException;
import com.tungsten.fclcore.util.gson.Validation;

import java.util.List;
import java.util.Locale;

public abstract class TerracottaState {
    protected TerracottaState() {
    }

    public boolean isForkOf(TerracottaState state) {
        return false;
    }

    @JsonType(
            property = "state",
            subtypes = {
                    @JsonSubtype(clazz = Waiting.class, name = "waiting"),
                    @JsonSubtype(clazz = HostScanning.class, name = "host-scanning"),
                    @JsonSubtype(clazz = HostStarting.class, name = "host-starting"),
                    @JsonSubtype(clazz = HostOK.class, name = "host-ok"),
                    @JsonSubtype(clazz = GuestConnecting.class, name = "guest-connecting"),
                    @JsonSubtype(clazz = GuestStarting.class, name = "guest-starting"),
                    @JsonSubtype(clazz = GuestOK.class, name = "guest-ok"),
                    @JsonSubtype(clazz = Exception.class, name = "exception"),
            }
    )
    public static abstract class Ready extends TerracottaState {
        @SerializedName("index")
        final int index;

        @SerializedName("state")
        private final String state;

        public Ready(int index, String state) {
            this.index = index;
            this.state = state;
        }

        @NonNull
        @Override
        public String toString() {
            String simple = getClass().getSimpleName();
            String withUnderscore = simple.replaceAll("([a-z])([A-Z])", "$1_$2");
            return withUnderscore.toLowerCase(Locale.ROOT);
        }
    }

    public static final class Waiting extends Ready {
        Waiting(int index, String state) {
            super(index, state);
        }
    }

    public static final class HostScanning extends Ready {
        HostScanning(int index, String state) {
            super(index, state);
        }
    }

    public static final class HostStarting extends Ready {
        HostStarting(int index, String state) {
            super(index, state);
        }
    }

    public static final class HostOK extends Ready implements Validation {
        @SerializedName("room")
        private final String code;

        @SerializedName("profile_index")
        private final int profileIndex;

        @SerializedName("profiles")
        private final List<TerracottaProfile> profiles;

        HostOK(int index, String state, String code, int profileIndex, List<TerracottaProfile> profiles) {
            super(index, state);
            this.code = code;
            this.profileIndex = profileIndex;
            this.profiles = profiles;
        }

        @Override
        public void validate() throws JsonParseException {
            if (code == null) {
                throw new JsonParseException("code is null");
            }
            if (profiles == null) {
                throw new JsonParseException("profiles is null");
            }
        }

        public String getCode() {
            return code;
        }

        public List<TerracottaProfile> getProfiles() {
            return profiles;
        }

        @Override
        public boolean isForkOf(TerracottaState state) {
            if (state instanceof HostOK) {
                HostOK hostOK = (HostOK) state;
                return this.index - hostOK.index <= profileIndex;
            }
            return false;
        }
    }

    public static final class GuestConnecting extends Ready {
        GuestConnecting(int index, String state) {
            super(index, state);
        }
    }

    public static final class GuestStarting extends Ready {
        public enum Difficulty {
            UNKNOWN,
            EASIEST,
            SIMPLE,
            MEDIUM,
            TOUGH
        }

        @SerializedName("difficulty")
        private final Difficulty difficulty;

        GuestStarting(int index, String state, Difficulty difficulty) {
            super(index, state);
            this.difficulty = difficulty;

        }

        public Difficulty getDifficulty() {
            return difficulty;
        }
    }

    public static final class GuestOK extends Ready implements Validation {
        @SerializedName("url")
        private final String url;

        @SerializedName("profile_index")
        private final int profileIndex;

        @SerializedName("profiles")
        private final List<TerracottaProfile> profiles;

        GuestOK(int index, String state, String url, int profileIndex, List<TerracottaProfile> profiles) {
            super(index, state);
            this.url = url;
            this.profileIndex = profileIndex;
            this.profiles = profiles;
        }

        @Override
        public void validate() throws JsonParseException, TolerableValidationException {
            if (profiles == null) {
                throw new JsonParseException("profiles is null");
            }
        }

        public String getUrl() {
            return url;
        }

        public List<TerracottaProfile> getProfiles() {
            return profiles;
        }

        @Override
        public boolean isForkOf(TerracottaState state) {
            if (state instanceof GuestOK) {
                GuestOK guestOK = (GuestOK) state;
                return this.index - guestOK.index <= profileIndex;
            }
            return false;
        }
    }

    public static final class Exception extends Ready implements Validation {
        public enum Type {
            PING_HOST_FAIL,
            PING_HOST_RST,
            GUEST_ET_CRASH,
            HOST_ET_CRASH,
            PING_SERVER_RST,
            SCAFFOLDING_INVALID_RESPONSE
        }

        private static final Type[] LOOKUP = Type.values();

        @SerializedName("type")
        private final int type;

        Exception(int index, String state, int type) {
            super(index, state);
            this.type = type;
        }

        @Override
        public void validate() throws JsonParseException, TolerableValidationException {
            if (type < 0 || type >= LOOKUP.length) {
                throw new JsonParseException(String.format("Type must between [0, %s)", LOOKUP.length));
            }
        }

        public Type getType() {
            return LOOKUP[type];
        }
    }
}
