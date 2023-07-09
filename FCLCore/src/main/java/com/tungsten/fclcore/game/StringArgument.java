package com.tungsten.fclcore.game;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonAdapter(StringArgument.Serializer.class)
public final class StringArgument implements Argument {

    private final String argument;

    public StringArgument(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }

    @Override
    public List<String> toString(Map<String, String> keys, Map<String, Boolean> features) {
        String res = argument;
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher m = pattern.matcher(argument);
        while (m.find()) {
            String entry = m.group();
            res = res.replace(entry, keys.getOrDefault(entry, entry));
        }
        return Collections.singletonList(res);
    }

    @Override
    public String toString() {
        return argument;
    }

    public static final class Serializer implements JsonSerializer<StringArgument> {
        @Override
        public JsonElement serialize(StringArgument src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getArgument());
        }
    }
}
