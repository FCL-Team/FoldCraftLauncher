package com.tungsten.fclcore.observable.value;

/**
 * Minimal reimplementation of {@code ObservableBooleanValue}.
 */
public interface ObservableBooleanValue extends ObservableValue<Boolean> {

    boolean get();
}
