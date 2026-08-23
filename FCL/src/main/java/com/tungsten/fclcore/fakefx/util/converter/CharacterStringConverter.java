package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

/**
 * <p>{@link StringConverter} implementation for {@link Character}
 * (and char primitive) values.</p>
 * @since JavaFX 2.1
 */
public class CharacterStringConverter extends StringConverter<Character> {

    /**
     * Creates a default {@code CharacterStringConverter}.
     */
    public CharacterStringConverter() {
    }

    /** {@inheritDoc} */
    @Override public Character fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return Character.valueOf(value.charAt(0));
    }

    /** {@inheritDoc} */
    @Override public String toString(Character value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return value.toString();
    }
}
