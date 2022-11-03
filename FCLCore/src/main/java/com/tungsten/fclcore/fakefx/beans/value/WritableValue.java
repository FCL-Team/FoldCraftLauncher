package com.tungsten.fclcore.fakefx.beans.value;

/**
 * A {@code WritableValue} is an entity that wraps a value that can be read and
 * set. In general this interface should not be implemented directly but one of
 * its sub-interfaces ({@code WritableBooleanValue} etc.).
 *
 * @see WritableBooleanValue
 * @see WritableDoubleValue
 * @see WritableFloatValue
 * @see WritableIntegerValue
 * @see WritableLongValue
 * @see WritableNumberValue
 * @see WritableObjectValue
 * @see WritableStringValue
 *
 * @param <T>
 *            The type of the wrapped value
 *
 *
 * @since JavaFX 2.0
 */
public interface WritableValue<T> {

    /**
     * Get the wrapped value.
     *
     * @return The current value
     */
    T getValue();

    /**
     * Set the wrapped value.
     *
     * @param value
     *            The new value
     */
    void setValue(T value);

}
