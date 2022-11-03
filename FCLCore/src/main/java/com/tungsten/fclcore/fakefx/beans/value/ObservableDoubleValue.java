package com.tungsten.fclcore.fakefx.beans.value;

/**
 * An observable double value.
 *
 * @see ObservableValue
 * @see ObservableNumberValue
 *
 *
 * @since JavaFX 2.0
 */
public interface ObservableDoubleValue extends ObservableNumberValue {

    /**
     * Returns the current value of this {@code ObservableDoubleValue}.
     *
     * @return The current value
     */
    double get();
}
