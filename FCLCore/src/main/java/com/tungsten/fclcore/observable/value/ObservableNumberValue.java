package com.tungsten.fclcore.observable.value;

/**
 * Minimal reimplementation of {@code fakefx.beans.value.ObservableNumberValue}.
 */
public interface ObservableNumberValue extends ObservableValue<Number> {

    int intValue();

    long longValue();

    float floatValue();

    double doubleValue();
}
