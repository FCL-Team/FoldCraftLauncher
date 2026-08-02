package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.value.ObservableValue;
import com.tungsten.fclcore.observable.value.WritableValue;

/**
 * Minimal reimplementation of {@code Property}.
 */
public interface Property<T> extends ReadOnlyProperty<T>, WritableValue<T> {

    void bind(ObservableValue<? extends T> observable);

    void unbind();

    boolean isBound();

    void bindBidirectional(Property<T> other);

    void unbindBidirectional(Property<T> other);
}
