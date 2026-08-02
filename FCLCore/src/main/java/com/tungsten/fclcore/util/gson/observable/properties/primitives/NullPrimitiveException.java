package com.tungsten.fclcore.util.gson.observable.properties.primitives;

public class NullPrimitiveException extends RuntimeException {

    public NullPrimitiveException(String pathInJson) {
        super("Illegal null value for a primitive type at path " + pathInJson);
    }
}