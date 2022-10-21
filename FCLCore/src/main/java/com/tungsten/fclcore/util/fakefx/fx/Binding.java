package com.tungsten.fclcore.util.fakefx.fx;

public interface Binding<T> extends ObservableValue<T> {

    boolean isValid();

    void invalidate();

    void dispose();

}