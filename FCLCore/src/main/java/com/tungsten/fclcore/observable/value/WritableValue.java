package com.tungsten.fclcore.observable.value;

/**
 * Minimal reimplementation of {@code WritableValue}.
 */
public interface WritableValue<T> {

    T getValue();

    void setValue(T value);
}
