package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

/**
 * <p>{@link StringConverter} implementation for {@link Boolean}
 * (and boolean primitive) values.</p>
 * @since JavaFX 2.1
 */
public class BooleanStringConverter extends StringConverter<Boolean> {

    /**
     * Creates a default {@code BooleanStringConverter}.
     */
    public BooleanStringConverter() {
    }

    /** {@inheritDoc} */
    @Override public Boolean fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return Boolean.valueOf(value);
    }

    /** {@inheritDoc} */
    @Override public String toString(Boolean value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return value.toString();
    }
}
