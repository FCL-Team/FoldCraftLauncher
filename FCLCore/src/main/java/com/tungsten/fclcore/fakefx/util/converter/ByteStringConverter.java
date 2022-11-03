package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

/**
 * <p>{@link StringConverter} implementation for {@link Byte}
 * (and byte primitive) values.</p>
 * @since JavaFX 2.1
 */
public class ByteStringConverter extends StringConverter<Byte> {

    /**
     * Creates a default {@code ByteStringConverter}.
     */
    public ByteStringConverter() {
    }

    /** {@inheritDoc} */
    @Override public Byte fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return Byte.valueOf(value);
    }

    /** {@inheritDoc} */
    @Override public String toString(Byte value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return Byte.toString(value.byteValue());
    }
}
