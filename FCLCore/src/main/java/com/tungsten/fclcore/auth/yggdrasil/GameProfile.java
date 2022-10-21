package com.tungsten.fclcore.auth.yggdrasil;

import java.util.Objects;
import java.util.UUID;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import com.tungsten.fclcore.util.gson.UUIDTypeAdapter;
import com.tungsten.fclcore.util.gson.Validation;

public class GameProfile implements Validation {

    @JsonAdapter(UUIDTypeAdapter.class)
    private final UUID id;

    private final String name;

    public GameProfile(UUID id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public void validate() throws JsonParseException {
        Validation.requireNonNull(id, "Game profile id cannot be null");
        Validation.requireNonNull(name, "Game profile name cannot be null");
    }
}
