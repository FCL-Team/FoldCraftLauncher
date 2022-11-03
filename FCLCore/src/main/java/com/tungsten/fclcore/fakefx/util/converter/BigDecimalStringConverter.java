package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

import java.math.BigDecimal;

/**
 * <p>{@link StringConverter} implementation for {@link BigDecimal} values.</p>
 * @since JavaFX 2.1
 */
public class BigDecimalStringConverter extends StringConverter<BigDecimal> {

    /**
     * Creates a default {@code BigDecimalStringConverter}.
     */
    public BigDecimalStringConverter() {
    }

    /** {@inheritDoc} */
    @Override public BigDecimal fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return new BigDecimal(value);
    }

    /** {@inheritDoc} */
    @Override public String toString(BigDecimal value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return ((BigDecimal)value).toString();
    }
}
