package com.tungsten.fclcore.fakefx;

public interface WritableValue<T> {

    T getValue();

    void setValue(T value);

}