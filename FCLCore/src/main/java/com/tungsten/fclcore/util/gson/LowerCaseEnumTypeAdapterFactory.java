package com.tungsten.fclcore.util.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public final class LowerCaseEnumTypeAdapterFactory implements TypeAdapterFactory {

    public static final LowerCaseEnumTypeAdapterFactory INSTANCE = new LowerCaseEnumTypeAdapterFactory();

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> tt) {
        Class<? super T> rawType = tt.getRawType();
        if (!rawType.isEnum())
            return null;

        HashMap<String, T> lowercaseToConstant = new HashMap<>();
        for (Object constant : rawType.getEnumConstants())
            lowercaseToConstant.put(toLowercase(constant), (T) constant);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter writer, T t) throws IOException {
                if (t == null)
                    writer.nullValue();
                else
                    writer.value(toLowercase(t));
            }

            @Override
            public T read(JsonReader reader) throws IOException {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return null;
                }
                return lowercaseToConstant.get(reader.nextString().toLowerCase(Locale.ROOT));
            }
        };
    }

    private static String toLowercase(Object o) {
        return o.toString().toLowerCase(Locale.ROOT);
    }
}
