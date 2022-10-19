package com.tungsten.fclcore.util.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class EnumOrdinalDeserializer<T extends Enum<T>> implements JsonDeserializer<T> {

    private Map<String, T> mapping = new HashMap<>();

    public EnumOrdinalDeserializer(Class<T> enumClass) {
        for (T constant : enumClass.getEnumConstants()) {
            mapping.put(String.valueOf(constant.ordinal()), constant);
            String name = constant.name();
            try {
                SerializedName annotation = enumClass.getField(name).getAnnotation(SerializedName.class);
                if (annotation != null) {
                    name = annotation.value();
                    for (String alternate : annotation.alternate()) {
                        mapping.put(alternate, constant);
                    }
                }
            } catch (NoSuchFieldException e) {
                throw new AssertionError(e);
            }
            mapping.put(name, constant);
        }
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return mapping.get(json.getAsString());
    }

}
