package com.tungsten.fclcore.util.gson;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

public abstract class GsonSerializerHelper<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    protected static void add(JsonObject object, String property, JsonElement value) {
        if (value == null) return;
        object.add(property, value);
    }

}
