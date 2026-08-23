package com.tungsten.fclcore.util.gson.fakefx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

public final class FxGson {

    private FxGson() throws InstantiationException {
        throw new InstantiationException("Instances of this type are forbidden.");
    }

    @NotNull
    public static Gson create() {
        return new FxGsonBuilder().create();
    }

    @NotNull
    public static GsonBuilder coreBuilder() {
        return new FxGsonBuilder().builder();
    }

    @NotNull
    public static GsonBuilder fullBuilder() {
        return new FxGsonBuilder().builder();
    }

    @NotNull
    public static GsonBuilder addFxSupport(@NotNull GsonBuilder builder) {
        return new FxGsonBuilder(builder).builder();
    }
}