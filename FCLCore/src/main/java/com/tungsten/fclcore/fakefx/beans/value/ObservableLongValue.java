package com.tungsten.fclcore.fakefx.beans.value;

/**
 * An observable long value.
 *
 * @see ObservableValue
 * @see ObservableNumberValue
 *
 *
 * @since JavaFX 2.0
 */
public interface ObservableLongValue extends ObservableNumberValue {

    /**
     * Returns the current value of this {@code ObservableLongValue}.
     *
     * @return The current value
     */
    long get();
}
