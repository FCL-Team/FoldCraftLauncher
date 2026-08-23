package com.tungsten.fclcore.fakefx.beans.value;

/**
 * An observable integer value.
 *
 * @see ObservableValue
 * @see ObservableNumberValue
 *
 *
 * @since JavaFX 2.0
 */
public interface ObservableIntegerValue extends ObservableNumberValue {

    /**
     * Returns the current value of this {@code ObservableIntegerValue}.
     *
     * @return The current value
     */
    int get();
}
