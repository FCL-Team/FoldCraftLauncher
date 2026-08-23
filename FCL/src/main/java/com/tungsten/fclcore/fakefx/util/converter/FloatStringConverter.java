package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

/**
 * <p>{@link StringConverter} implementation for {@link Float}
 * (and float primitive) values.</p>
 * @since JavaFX 2.1
 */
public class FloatStringConverter extends StringConverter<Float> {

    /**
     * Creates a default {@code FloatStringConverter}.
     */
    public FloatStringConverter() {
    }

    /** {@inheritDoc} */
    @Override public Float fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return Float.valueOf(value);
    }

    /** {@inheritDoc} */
    @Override public String toString(Float value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return Float.toString(((Float)value).floatValue());
    }
}
