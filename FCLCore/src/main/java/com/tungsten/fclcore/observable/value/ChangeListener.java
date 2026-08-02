package com.tungsten.fclcore.observable.value;

/**
 * Minimal reimplementation of {@code ChangeListener}.
 */
@FunctionalInterface
public interface ChangeListener<T> {

    void changed(ObservableValue<? extends T> observable, T oldValue, T newValue);
}
