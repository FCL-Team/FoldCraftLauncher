package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

/**
 * <p>{@link StringConverter} implementation for {@link LocalDateTime} values.</p>
 *
 * @see LocalDateStringConverter
 * @see LocalTimeStringConverter
 * @since JavaFX 8u40
 */
public class LocalDateTimeStringConverter extends StringConverter<LocalDateTime> {

    LdtConverter<LocalDateTime> ldtConverter;



   // ------------------------------------------------------------ Constructors

    /**
     * Create a {@link StringConverter} for {@link LocalDateTime} values, using a
     * default formatter and parser based on {@link IsoChronology},
     * {@link FormatStyle#SHORT} for both date and time, and the user's
     * {@link Locale}.
     *
     * <p>This converter ensures symmetry between the toString() and
     * fromString() methods. Many of the default locale based patterns used by
     * {@link DateTimeFormatter} will display only two digits for the year when
     * formatting to a string. This would cause a value like 1955 to be
     * displayed as 55, which in turn would be parsed back as 2055. This
     * converter modifies two-digit year patterns to always use four digits. The
     * input parsing is not affected, so two digit year values can still be
     * parsed as expected in these locales.</p>
     */
    public LocalDateTimeStringConverter() {
        ldtConverter = new LdtConverter<LocalDateTime>(LocalDateTime.class, null, null,
                                                       null, null, null, null);
    }

    /**
     * Create a {@link StringConverter} for {@link LocalDateTime} values, using
     * a default formatter and parser based on {@link IsoChronology}, the
     * specified {@link FormatStyle} values for date and time, and the user's
     * {@link Locale}.
     *
     * @param dateStyle The {@link FormatStyle} that will be used by the default
     * formatter and parser for the date. If null then {@link FormatStyle#SHORT}
     * will be used.
     * @param timeStyle The {@link FormatStyle} that will be used by the default
     * formatter and parser for the time. If null then {@link FormatStyle#SHORT}
     * will be used.
     */
    public LocalDateTimeStringConverter(FormatStyle dateStyle, FormatStyle timeStyle) {
        ldtConverter = new LdtConverter<LocalDateTime>(LocalDateTime.class, null, null,
                                                       dateStyle, timeStyle, null, null);
    }

    /**
     * Create a {@link StringConverter} for {@link LocalDateTime} values using
     * the supplied formatter and parser.
     *
     * <p>For example, to use a fixed pattern for converting both ways:</p>
     * <blockquote><pre>
     * String pattern = "yyyy-MM-dd HH:mm";
     * DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
     * StringConverter&lt;LocalDateTime&gt; converter =
     *     DateTimeStringConverter.getLocalDateTimeConverter(formatter, null);
     * </pre></blockquote>
     *
     * Note that the formatter and parser can be created to handle non-default
     * {@link Locale} and {@link Chronology} as needed.
     *
     * @param formatter An instance of {@link DateTimeFormatter} which will be
     * used for formatting by the toString() method. If null then a default
     * formatter will be used.
     * @param parser An instance of {@link DateTimeFormatter} which will be used
     * for parsing by the fromString() method. This can be identical to
     * formatter. If null then formatter will be used, and if that is also null,
     * then a default parser will be used.
     */
    public LocalDateTimeStringConverter(DateTimeFormatter formatter, DateTimeFormatter parser) {
        ldtConverter = new LdtConverter<LocalDateTime>(LocalDateTime.class, formatter, parser,
                                                       null, null, null, null);
    }

    /**
     * Create a {@link StringConverter} for {@link LocalDateTime} values using a
     * default formatter and parser, which will be based on the supplied
     * {@link FormatStyle}s, {@link Locale}, and {@link Chronology}.
     *
     * @param dateStyle The {@link FormatStyle} that will be used by the default
     * formatter and parser for the date. If null then {@link FormatStyle#SHORT}
     * will be used.
     * @param timeStyle The {@link FormatStyle} that will be used by the default
     * formatter and parser for the time. If null then {@link FormatStyle#SHORT}
     * will be used.
     * @param locale The {@link Locale} that will be used by the
     * default formatter and parser. If null then
     * {@code Locale.getDefault(Locale.Category.FORMAT)} will be used.
     * @param chronology The {@link Chronology} that will be used by the default
     * formatter and parser. If null then {@link IsoChronology#INSTANCE} will be
     * used.
     */
    public LocalDateTimeStringConverter(FormatStyle dateStyle, FormatStyle timeStyle,
                                        Locale locale, Chronology chronology) {
        ldtConverter = new LdtConverter<LocalDateTime>(LocalDateTime.class, null, null,
                                                       dateStyle, timeStyle, locale, chronology);
    }



    // ------------------------------------------------------- Converter Methods

    /** {@inheritDoc} */
    @Override public LocalDateTime fromString(String value) {
        return ldtConverter.fromString(value);
    }

    /** {@inheritDoc} */
    @Override public String toString(LocalDateTime value) {
        return ldtConverter.toString(value);
    }



    static class LdtConverter<T extends Temporal> extends StringConverter<T> {
        private Class<T> type;
        Locale locale;
        Chronology chronology;
        DateTimeFormatter formatter;
        DateTimeFormatter parser;
        FormatStyle dateStyle;
        FormatStyle timeStyle;

        LdtConverter(Class<T> type, DateTimeFormatter formatter, DateTimeFormatter parser,
                     FormatStyle dateStyle, FormatStyle timeStyle, Locale locale, Chronology chronology) {
            this.type = type;
            this.formatter = formatter;
            this.parser = (parser != null) ? parser : formatter;
            this.locale = (locale != null) ? locale : Locale.getDefault(Locale.Category.FORMAT);
            this.chronology = (chronology != null) ? chronology : IsoChronology.INSTANCE;

            if (type == LocalDate.class || type == LocalDateTime.class) {
                this.dateStyle = (dateStyle != null) ? dateStyle : FormatStyle.SHORT;
            }

            if (type == LocalTime.class || type == LocalDateTime.class) {
                this.timeStyle = (timeStyle != null) ? timeStyle : FormatStyle.SHORT;
            }
        }

        /** {@inheritDoc} */
        @SuppressWarnings({"unchecked"})
        @Override public T fromString(String text) {
            if (text == null || text.isEmpty()) {
                return null;
            }

            text = text.trim();

            if (parser == null) {
                parser = getDefaultParser();
            }

            TemporalAccessor temporal = parser.parse(text);

            if (type == LocalDate.class) {
                return (T) LocalDate.from(temporal);
            } else if (type == LocalTime.class) {
                return (T) LocalTime.from(temporal);
            } else {
                return (T) LocalDateTime.from(temporal);
            }
        }


        /** {@inheritDoc} */
        @Override public String toString(T value) {
            // If the specified value is null, return a zero-length String
            if (value == null) {
                return "";
            }

            if (formatter == null) {
                formatter = getDefaultFormatter();
            }

            return formatter.format(value);
        }


        private DateTimeFormatter getDefaultParser() {
            String pattern =
                DateTimeFormatterBuilder.getLocalizedDateTimePattern(dateStyle, timeStyle,
                                                                     chronology, locale);
            return new DateTimeFormatterBuilder().parseLenient()
                                                 .appendPattern(pattern)
                                                 .toFormatter()
                                                 .withChronology(chronology)
                                                 .withDecimalStyle(DecimalStyle.of(locale));
        }

        /**
         * <p>Return a default <code>DateTimeFormatter</code> instance to use for formatting
         * and parsing in this {@link StringConverter}.</p>
         */
        private DateTimeFormatter getDefaultFormatter() {
            DateTimeFormatter formatter;

            if (dateStyle != null && timeStyle != null) {
                formatter = DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle);
            } else if (dateStyle != null) {
                formatter = DateTimeFormatter.ofLocalizedDate(dateStyle);
            } else {
                formatter = DateTimeFormatter.ofLocalizedTime(timeStyle);
            }

            formatter = formatter.withLocale(locale)
                                 .withChronology(chronology)
                                 .withDecimalStyle(DecimalStyle.of(locale));

            if (dateStyle != null) {
                formatter = fixFourDigitYear(formatter, dateStyle, timeStyle,
                                             chronology, locale);
            }

            return formatter;
        }

        private DateTimeFormatter fixFourDigitYear(DateTimeFormatter formatter,
                                                   FormatStyle dateStyle, FormatStyle timeStyle,
                                                   Chronology chronology, Locale locale) {
            String pattern =
                DateTimeFormatterBuilder.getLocalizedDateTimePattern(dateStyle, timeStyle,
                                                                     chronology, locale);
            if (pattern.contains("yy") && !pattern.contains("yyy")) {
                // Modify pattern to show four-digit year, including leading zeros.
                String newPattern = pattern.replace("yy", "yyyy");
                formatter = DateTimeFormatter.ofPattern(newPattern)
                                             .withDecimalStyle(DecimalStyle.of(locale));
            }

            return formatter;
        }
    }
}
