package com.tungsten.fclcore.fakefx.beans.value;

/**
 * A writable boolean value.
 *
 * @see WritableValue
 *
 *
 * @since JavaFX 2.0
 */
public interface WritableBooleanValue extends WritableValue<Boolean> {

    /**
     * Get the wrapped value.
     * Unlike {@link #getValue()},
     * this method returns primitive boolean.
     * Needs to be identical to {@link #getValue()}.
     *
     * @return The current value
     */
    boolean get();

    /**
     * Set the wrapped value.
     * Unlike {@link #setValue(Boolean) },
     * this method uses primitive boolean.
     *
     * @param value
     *            The new value
     */
    void set(boolean value);

    /**
     * Set the wrapped value.
     * <p>
     * Note: this method should accept null without throwing an exception,
     * setting "false" instead.
     *
     * @param value
     *            The new value
     */
    @Override
    void setValue(Boolean value);

}
