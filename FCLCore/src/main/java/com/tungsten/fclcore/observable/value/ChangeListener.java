package com.tungsten.fclcore.observable.value;

/**
 * Minimal reimplementation of {@code fakefx.beans.value.ChangeListener}.
 */
@FunctionalInterface
public interface ChangeListener<T> {

    void changed(ObservableValue<? extends T> observable, T oldValue, T newValue);
}
