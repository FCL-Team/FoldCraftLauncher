package com.tungsten.fclcore.util.gson;

import com.google.gson.*;

import java.io.File;
import java.lang.reflect.Type;

public final class FileTypeAdapter implements JsonSerializer<File>, JsonDeserializer<File> {

    public static final FileTypeAdapter INSTANCE = new FileTypeAdapter();

    private FileTypeAdapter() {
    }

    @Override
    public JsonElement serialize(File t, Type type, JsonSerializationContext jsc) {
        if (t == null)
            return JsonNull.INSTANCE;
        else
            return new JsonPrimitive(t.getPath());
    }

    @Override
    public File deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        if (je == null)
            return null;
        else
            return new File(je.getAsString());
    }

}
