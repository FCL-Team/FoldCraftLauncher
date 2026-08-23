package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * A {@link StringConverter} implementation for {@link Number} values. Instances of this class are immutable.
 *
 * @since JavaFX 2.1
 */
public class NumberStringConverter extends StringConverter<Number> {

    final Locale locale;
    final String pattern;
    final NumberFormat numberFormat;

    /**
     * Constructs a {@code NumberStringConverter} with the default locale and format.
     */
    public NumberStringConverter() {
        this(Locale.getDefault());
    }

    /**
     * Constructs a {@code NumberStringConverter} with the given locale and the default format.
     *
     * @param locale the locale used in determining the number format used to format the string
     */
    public NumberStringConverter(Locale locale) {
        this(locale, null);
    }

    /**
     * Constructs a {@code NumberStringConverter} with the default locale and the given decimal format pattern.
     *
     * @param pattern the string pattern used in determining the number format used to format the string
     *
     * @see DecimalFormat
     */
    public NumberStringConverter(String pattern) {
        this(Locale.getDefault(), pattern);
    }

    /**
     * Constructs a {@code NumberStringConverter} with the given locale and decimal format pattern.
     *
     * @param locale the locale used in determining the number format used to format the string
     * @param pattern the string pattern used in determining the number format used to format the string
     *
     * @see DecimalFormat
     */
    public NumberStringConverter(Locale locale, String pattern) {
        this(locale, pattern, null);
    }

    /**
     * Constructs a {@code NumberStringConverter} with the given number format.
     *
     * @param numberFormat the number format used to format the string
     */
    public NumberStringConverter(NumberFormat numberFormat) {
        this(null, null, numberFormat);
    }

    NumberStringConverter(Locale locale, String pattern, NumberFormat numberFormat) {
        this.locale = locale;
        this.pattern = pattern;
        this.numberFormat = numberFormat;
    }

    /** {@inheritDoc} */
    @Override public Number fromString(String value) {
        try {
            // If the specified value is null or zero-length, return null
            if (value == null) {
                return null;
            }

            value = value.trim();

            if (value.length() < 1) {
                return null;
            }

            // Create and configure the parser to be used
            NumberFormat parser = getNumberFormat();

            // Perform the requested parsing
            return parser.parse(value);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** {@inheritDoc} */
    @Override public String toString(Number value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        // Create and configure the formatter to be used
        NumberFormat formatter = getNumberFormat();

        // Perform the requested formatting
        return formatter.format(value);
    }

    /**
     * Returns a {@code NumberFormat} instance to use for formatting
     * and parsing in this {@code StringConverter}.
     *
     * @return a {@code NumberFormat} instance for formatting and parsing in this
     * {@code StringConverter}
     */
    protected NumberFormat getNumberFormat() {
        Locale _locale = locale == null ? Locale.getDefault() : locale;

        if (numberFormat != null) {
            return numberFormat;
        } else if (pattern != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(_locale);
            return new DecimalFormat(pattern, symbols);
        } else {
            return NumberFormat.getNumberInstance(_locale);
        }
    }
}
