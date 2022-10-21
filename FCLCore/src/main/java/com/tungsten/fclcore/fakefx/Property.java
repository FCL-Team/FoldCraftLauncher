package com.tungsten.fclcore.fakefx;

public interface Property<T> extends ReadOnlyProperty<T>, WritableValue<T> {

    void bind(ObservableValue<? extends T> observable);

    void unbind();

    boolean isBound();

    void bindBidirectional(Property<T> other);

    void unbindBidirectional(Property<T> other);

}