package com.tungsten.fclcore.fakefx.util.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateStringConverter extends DateTimeStringConverter {

    public DateStringConverter() {
        this(null, null, null, DateFormat.DEFAULT);
    }

    public DateStringConverter(int dateStyle) {
        this(null, null, null, dateStyle);
    }

    public DateStringConverter(Locale locale) {
        this(locale, null, null, DateFormat.DEFAULT);
    }

    public DateStringConverter(Locale locale, int dateStyle) {
        this(locale, null, null, dateStyle);
    }

    public DateStringConverter(String pattern) {
        this(null, pattern, null, DateFormat.DEFAULT);
    }

    public DateStringConverter(Locale locale, String pattern) {
        this(locale, pattern, null, DateFormat.DEFAULT);
    }

    public DateStringConverter(DateFormat dateFormat) {
        this(null, null, dateFormat, DateFormat.DEFAULT);
    }

    private DateStringConverter(Locale locale, String pattern, DateFormat dateFormat, int dateStyle) {
        super(locale, pattern, dateFormat, dateStyle, DateFormat.DEFAULT);
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
            df = DateFormat.getDateInstance(dateStyle, locale);
        }

        df.setLenient(false);

        return df;
    }
}
