package com.tungsten.fclcore.fakefx.beans.value;

/**
 * A writable typed value.
 *
 * @param <T> the type of the wrapped value
 *
 * @see WritableValue
 *
 *
 * @since JavaFX 2.0
 */
public interface WritableObjectValue<T> extends WritableValue<T> {

    /**
     * Get the wrapped value. This must be identical to
     * the value returned from {@link #getValue()}.
     * <p>
     * This method exists only to align WritableObjectValue API with
     * {@link WritableBooleanValue} and subclasses of {@link WritableNumberValue}
     * @return The current value
     */
    T get();

    /**
     * Set the wrapped value.
     * Should be equivalent to {@link #setValue(Object) }
     * @see #get()
     *
     * @param value
     *            The new value
     */
    void set(T value);

}
