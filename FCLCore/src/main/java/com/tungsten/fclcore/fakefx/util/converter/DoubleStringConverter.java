package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

/**
 * <p>{@link StringConverter} implementation for {@link Double}
 * (and double primitive) values.</p>
 * @since JavaFX 2.1
 */
public class DoubleStringConverter extends StringConverter<Double> {

    /**
     * Creates a default {@code DoubleStringConverter}.
     */
    public DoubleStringConverter() {
    }

    /** {@inheritDoc} */
    @Override public Double fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return Double.valueOf(value);
    }

    /** {@inheritDoc} */
    @Override public String toString(Double value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return Double.toString(value.doubleValue());
    }
}
