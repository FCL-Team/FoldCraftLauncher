package com.tungsten.fclcore.util.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class InstantTypeAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
    public static final InstantTypeAdapter INSTANCE = new InstantTypeAdapter();

    private InstantTypeAdapter() {
    }

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The instant should be a string value");
        } else {
            Instant instant = Instant.parse(json.getAsString());
            if (typeOfT == Instant.class) {
                return instant;
            } else {
                throw new IllegalArgumentException(this.getClass() + " cannot be deserialized to " + typeOfT);
            }
        }
    }

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault()).format(src));
    }
}
