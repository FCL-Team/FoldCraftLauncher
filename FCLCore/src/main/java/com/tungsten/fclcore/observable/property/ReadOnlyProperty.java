package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.value.ObservableValue;

/**
 * Minimal reimplementation of {@code ReadOnlyProperty}.
 */
public interface ReadOnlyProperty<T> extends ObservableValue<T> {

    Object getBean();

    String getName();
}
