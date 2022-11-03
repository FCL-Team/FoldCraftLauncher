package com.tungsten.fclcore.fakefx.util.converter;

import java.text.NumberFormat;
import java.util.Locale;

public class PercentageStringConverter extends NumberStringConverter {

    /**
     * Constructs a {@code PercentageStringConverter} with the default locale and format.
     */
    public PercentageStringConverter() {
        this(Locale.getDefault());
    }

    /**
     * Constructs a {@code PercentageStringConverter} with the given locale and the default format.
     *
     * @param locale the locale used in determining the number format used to format the string
     */
    public PercentageStringConverter(Locale locale) {
        super(locale, null, null);
    }

    /**
     * Constructs a {@code PercentageStringConverter} with the given number format.
     *
     * @param numberFormat the number format used to format the string
     */
    public PercentageStringConverter(NumberFormat numberFormat) {
        super(null, null, numberFormat);
    }

    /** {@inheritDoc} */
    @Override public NumberFormat getNumberFormat() {
        Locale _locale = locale == null ? Locale.getDefault() : locale;

        if (numberFormat != null) {
            return numberFormat;
        } else {
            return NumberFormat.getPercentInstance(_locale);
        }
    }
}
