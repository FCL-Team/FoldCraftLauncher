package com.tungsten.fclcore.fakefx;

public interface Binding<T> extends ObservableValue<T> {

    boolean isValid();

    void invalidate();

    void dispose();

}