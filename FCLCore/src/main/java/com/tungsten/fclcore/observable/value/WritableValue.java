package com.tungsten.fclcore.observable.value;

/**
 * Minimal reimplementation of {@code fakefx.beans.value.WritableValue}.
 */
public interface WritableValue<T> {

    T getValue();

    void setValue(T value);
}
