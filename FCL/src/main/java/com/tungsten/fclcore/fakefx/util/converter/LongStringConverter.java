package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

/**
 * <p>{@link StringConverter} implementation for {@link Long}
 * (and long primitive) values.</p>
 * @since JavaFX 2.1
 */
public class LongStringConverter extends StringConverter<Long> {

    /**
     * Creates a default {@code LongStringConverter}.
     */
    public LongStringConverter() {
    }

    /** {@inheritDoc} */
    @Override public Long fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return Long.valueOf(value);
    }

    /** {@inheritDoc} */
    @Override public String toString(Long value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return Long.toString(((Long)value).longValue());
    }
}
