package com.tungsten.fclcore.observable.value;

/**
 * Minimal reimplementation of {@code fakefx.beans.value.ObservableBooleanValue}.
 */
public interface ObservableBooleanValue extends ObservableValue<Boolean> {

    boolean get();
}
