package com.tungsten.fclcore.observable.util;

/**
 * Minimal reimplementation of {@code fakefx.util.StringConverter}.
 */
public abstract class StringConverter<T> {

    public StringConverter() {
    }

    public abstract String toString(T object);

    public abstract T fromString(String string);
}
