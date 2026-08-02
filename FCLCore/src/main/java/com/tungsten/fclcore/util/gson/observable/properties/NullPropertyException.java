package com.tungsten.fclcore.util.gson.observable.properties;

public class NullPropertyException extends RuntimeException {

    public NullPropertyException() {
        super("Null properties are forbidden");
    }
}