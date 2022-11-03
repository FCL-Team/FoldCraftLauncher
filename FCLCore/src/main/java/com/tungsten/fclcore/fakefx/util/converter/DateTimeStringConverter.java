package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>{@link StringConverter} implementation for {@link Date} values that
 * represent a date and time.</p>
 *
 * @see DateStringConverter
 * @see TimeStringConverter
 * @since JavaFX 2.1
 */
public class DateTimeStringConverter extends StringConverter<Date> {

    // ------------------------------------------------------ Private properties

    final Locale locale;

    final String pattern;

    final DateFormat dateFormat;

    final int dateStyle;

    final int timeStyle;


    // ------------------------------------------------------------ Constructors

    /**
     * Create a {@link StringConverter} for {@link Date} values, using
     * {@link DateFormat#DEFAULT} styles for date and time.
     */
    public DateTimeStringConverter() {
        this(null, null, null, DateFormat.DEFAULT, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using specified
     * {@link DateFormat} styles for date and time.
     *
     * @param dateStyle the given formatting style. For example,
     * {@link DateFormat#SHORT} for "M/d/yy" in the US locale.
     * @param timeStyle the given formatting style. For example,
     * {@link DateFormat#SHORT} for "h:mm a" in the US locale.
     *
     * @since JavaFX 8u40
     */
    public DateTimeStringConverter(int dateStyle, int timeStyle) {
        this(null, null, null, dateStyle, timeStyle);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified locale and {@link DateFormat#DEFAULT} styles for date and time.
     *
     * @param locale the given locale.
     */
    public DateTimeStringConverter(Locale locale) {
        this(locale, null, null, DateFormat.DEFAULT, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using specified
     * locale and {@link DateFormat} styles for date and time.
     *
     * @param locale the given locale.
     * @param dateStyle the given formatting style. For example,
     * {@link DateFormat#SHORT} for "M/d/yy" in the US locale.
     * @param timeStyle the given formatting style. For example,
     * {@link DateFormat#SHORT} for "h:mm a" in the US locale.
     *
     * @since JavaFX 8u40
     */
    public DateTimeStringConverter(Locale locale, int dateStyle, int timeStyle) {
        this(locale, null, null, dateStyle, timeStyle);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified pattern.
     *
     * @param pattern the pattern describing the date and time format.
     */
    public DateTimeStringConverter(String pattern) {
        this(null, pattern, null, DateFormat.DEFAULT, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified locale and pattern.
     *
     * @param locale the given locale.
     * @param pattern the pattern describing the date and time format.
     */
    public DateTimeStringConverter(Locale locale, String pattern) {
        this(locale, pattern, null, DateFormat.DEFAULT, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified {@link DateFormat} formatter.
     *
     * @param dateFormat the {@link DateFormat} to be used for formatting and
     * parsing.
     */
    public DateTimeStringConverter(DateFormat dateFormat) {
        this(null, null, dateFormat, DateFormat.DEFAULT, DateFormat.DEFAULT);
    }

    DateTimeStringConverter(Locale locale, String pattern, DateFormat dateFormat,
                            int dateStyle, int timeStyle) {
        this.locale = (locale != null) ? locale : Locale.getDefault(Locale.Category.FORMAT);
        this.pattern = pattern;
        this.dateFormat = dateFormat;
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
    }


    // ------------------------------------------------------- Converter Methods

    /** {@inheritDoc} */
    @Override public Date fromString(String value) {
        try {
            // If the specified value is null or zero-length, return null
            if (value == null) {
                return (null);
            }

            value = value.trim();

            if (value.length() < 1) {
                return (null);
            }

            // Create and configure the parser to be used
            DateFormat parser = getDateFormat();

            // Perform the requested parsing
            return parser.parse(value);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** {@inheritDoc} */
    @Override public String toString(Date value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        // Create and configure the formatter to be used
        DateFormat formatter = getDateFormat();

        // Perform the requested formatting
        return formatter.format(value);
    }

    // --------------------------------------------------------- Private Methods

    /**
     * <p>Return a <code>DateFormat</code> instance to use for formatting
     * and parsing in this {@link StringConverter}.</p>
     *
     * @return a {@code DateFormat} instance for formatting and parsing in this
     * {@link StringConverter}
     */
    DateFormat getDateFormat() {
        DateFormat df = null;

        if (dateFormat != null) {
            return dateFormat;
        } else if (pattern != null) {
            df = new SimpleDateFormat(pattern, locale);
        } else {
            df = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        }

        df.setLenient(false);

        return df;
    }
}
