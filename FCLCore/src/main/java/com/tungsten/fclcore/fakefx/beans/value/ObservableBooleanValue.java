package com.tungsten.fclcore.fakefx.beans.value;

/**
 * An observable boolean value.
 *
 * @see ObservableValue
 *
 *
 * @since JavaFX 2.0
 */
public interface ObservableBooleanValue extends ObservableValue<Boolean> {

    /**
     * Returns the current value of this {@code ObservableBooleanValue}.
     *
     * @return The current value
     */
    boolean get();
}
