package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

/**
 * <p>{@link StringConverter} implementation for {@link Short} values.</p>
 * @since JavaFX 2.1
 */
public class ShortStringConverter extends StringConverter<Short> {

    /**
     * Creates a default {@code ShortStringConverter}.
     */
    public ShortStringConverter() {
    }

    /** {@inheritDoc} */
    @Override public Short fromString(String text) {
        // If the specified value is null or zero-length, return null
        if (text == null) {
            return null;
        }

        text = text.trim();

        if (text.length() < 1) {
            return null;
        }

        return Short.valueOf(text);
    }

    /** {@inheritDoc} */
    @Override public String toString(Short value) {
        // If the specified value is null, return a
        // zero-length String
        if (value == null) {
            return "";
        }

        return Short.toString(((Short)value).shortValue());
    }
}
