package com.tungsten.fclcore.game;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@JsonAdapter(Argument.Deserializer.class)
public interface Argument extends Cloneable {

    /**
     * Parse this argument in form: ${key name} or simply a string.
     *
     * @param keys the parse map
     * @param features the map that contains some features such as 'is_demo_user', 'has_custom_resolution'
     * @return parsed argument element, empty if this argument is ignored and will not be added.
     */
    List<String> toString(Map<String, String> keys, Map<String, Boolean> features);

    class Deserializer implements JsonDeserializer<Argument> {
        @Override
        public Argument deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonPrimitive())
                return new StringArgument(json.getAsString());
            else
                return context.deserialize(json, RuledArgument.class);
        }
    }
}
