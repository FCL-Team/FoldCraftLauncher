package com.tungsten.fclcore.fakefx.beans.value;

/**
 * A writable long value.
 *
 * @see WritableValue
 * @see WritableNumberValue
 *
 *
 * @since JavaFX 2.0
 */
public interface WritableLongValue extends WritableNumberValue {

    /**
     * Get the wrapped value.
     * Unlike {@link #getValue()},
     * this method returns primitive long.
     * Needs to be identical to {@link #getValue()}.
     *
     * @return The current value
     */
    long get();

    /**
     * Set the wrapped value.
     * Unlike {@link #setValue(Number) },
     * this method uses primitive long.
     *
     * @param value
     *            The new value
     */
    void set(long value);

    /**
     * Set the wrapped value.
     * <p>
     * Note: this method should accept null without throwing an exception,
     * setting "0" instead.
     *
     * @param value
     *            The new value
     */
    @Override
    void setValue(Number value);
}
