package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

import java.math.BigInteger;

/**
 * <p>{@link StringConverter} implementation for {@link BigInteger} values.</p>
 * @since JavaFX 2.1
 */
public class BigIntegerStringConverter extends StringConverter<BigInteger> {

    /**
     * Creates a default {@code BigIntegerStringConverter}.
     */
    public BigIntegerStringConverter() {
    }

    /** {@inheritDoc} */
    @Override public BigInteger fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return new BigInteger(value);
    }

    /** {@inheritDoc} */
    @Override public String toString(BigInteger value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return ((BigInteger)value).toString();
    }
}
