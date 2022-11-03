package com.tungsten.fclcore.fakefx.util.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.tungsten.fclcore.fakefx.util.StringConverter;

/**
 * <p>{@link StringConverter} implementation for {@link Date} values that
 * represent time.</p>
 *
 * @see DateStringConverter
 * @see DateTimeStringConverter
 * @since JavaFX 2.1
 */
public class TimeStringConverter extends DateTimeStringConverter {

    // ------------------------------------------------------------ Constructors

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * {@link DateFormat#DEFAULT} time style.
     */
    public TimeStringConverter() {
        this(null, null, null, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified {@link DateFormat} time style.
     *
     * @param timeStyle the given formatting style. For example,
     * {@link DateFormat#SHORT} for "h:mm a" in the US locale.
     *
     * @since JavaFX 8u40
     */
    public TimeStringConverter(int timeStyle) {
        this(null, null, null, timeStyle);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified locale and the {@link DateFormat#DEFAULT} time style.
     *
     * @param locale the given locale.
     */
    public TimeStringConverter(Locale locale) {
        this(locale, null, null, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified locale and {@link DateFormat} time style.
     *
     * @param locale the given locale.
     * @param timeStyle the given formatting style. For example,
     * {@link DateFormat#SHORT} for "h:mm a" in the US locale.
     *
     * @since JavaFX 8u40
     */
    public TimeStringConverter(Locale locale, int timeStyle) {
        this(locale, null, null, timeStyle);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified pattern.
     *
     * @param pattern the pattern describing the time format.
     */
    public TimeStringConverter(String pattern) {
        this(null, pattern, null, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified locale and pattern.
     *
     * @param locale the given locale.
     * @param pattern the pattern describing the time format.
     */
    public TimeStringConverter(Locale locale, String pattern) {
        this(locale, pattern, null, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified {@link DateFormat} formatter.
     *
     * @param dateFormat the {@link DateFormat} to be used for formatting and
     * parsing.
     */
    public TimeStringConverter(DateFormat dateFormat) {
        this(null, null, dateFormat, DateFormat.DEFAULT);
    }

    private TimeStringConverter(Locale locale, String pattern, DateFormat dateFormat, int timeStyle) {
        super(locale, pattern, dateFormat, DateFormat.DEFAULT, timeStyle);
    }


    // --------------------------------------------------------- Private Methods

    /** {@inheritDoc} */
    @SuppressWarnings("removal")
    @Override protected DateFormat getDateFormat() {
        DateFormat df = null;

        if (dateFormat != null) {
            return dateFormat;
        } else if (pattern != null) {
            df = new SimpleDateFormat(pattern, locale);
        } else {
            df = DateFormat.getTimeInstance(timeStyle, locale);
        }

        df.setLenient(false);

        return df;
    }
}
