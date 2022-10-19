package com.tungsten.fclcore.util.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public final class JsonUtils {

    public static final Gson GSON = defaultGsonBuilder().create();

    public static final Gson UGLY_GSON = new GsonBuilder()
            .registerTypeAdapterFactory(JsonTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(ValidationTypeAdapterFactory.INSTANCE)
            .registerTypeAdapterFactory(LowerCaseEnumTypeAdapterFactory.INSTANCE)
            .create();

    private JsonUtils() {
    }

    public static <T> T fromNonNullJson(String json, Class<T> classOfT) throws JsonParseException {
        T parsed = GSON.fromJson(json, classOfT);
        if (parsed == null)
            throw new JsonParseException("Json object cannot be null.");
        return parsed;
    }

    public static <T> T fromNonNullJson(String json, Type type) throws JsonParseException {
        T parsed = GSON.fromJson(json, type);
        if (parsed == null)
            throw new JsonParseException("Json object cannot be null.");
        return parsed;
    }

    public static <T> T fromMaybeMalformedJson(String json, Class<T> classOfT) throws JsonParseException {
        try {
            return GSON.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static <T> T fromMaybeMalformedJson(String json, Type type) throws JsonParseException {
        try {
            return GSON.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static GsonBuilder defaultGsonBuilder() {
        return new GsonBuilder()
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .registerTypeAdapter(Instant.class, InstantTypeAdapter.INSTANCE)
                .registerTypeAdapter(Date.class, DateTypeAdapter.INSTANCE)
                .registerTypeAdapter(UUID.class, UUIDTypeAdapter.INSTANCE)
                .registerTypeAdapter(File.class, FileTypeAdapter.INSTANCE)
                .registerTypeAdapterFactory(ValidationTypeAdapterFactory.INSTANCE)
                .registerTypeAdapterFactory(LowerCaseEnumTypeAdapterFactory.INSTANCE)
                .registerTypeAdapterFactory(JsonTypeAdapterFactory.INSTANCE);
    }
}
