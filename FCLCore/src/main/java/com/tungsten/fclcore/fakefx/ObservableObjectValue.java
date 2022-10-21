package com.tungsten.fclcore.fakefx;

public interface ObservableObjectValue<T> extends ObservableValue<T> {

    T get();
}