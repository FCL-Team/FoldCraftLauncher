package com.tungsten.fclcore.fakefx.beans.value;

/**
 * An observable typed {@code Object} value.
 *
 * @see ObservableValue
 *
 *
 * @since JavaFX 2.0
 */
public interface ObservableObjectValue<T> extends ObservableValue<T> {

    /**
     * Returns the current value of this {@code ObservableObjectValue<T>}.
     *
     * @return The current value
     */
    T get();
}
