package com.tungsten.fclcore.fakefx.util.converter;

import com.tungsten.fclcore.fakefx.util.StringConverter;

import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * <p>{@link StringConverter} implementation for {@link LocalDate} values.</p>
 *
 * @see LocalTimeStringConverter
 * @see LocalDateTimeStringConverter
 * @since JavaFX 8u40
 */
public class LocalDateStringConverter extends StringConverter<LocalDate> {

    LocalDateTimeStringConverter.LdtConverter<LocalDate> ldtConverter;

    // ------------------------------------------------------------ Constructors

    /**
     * Create a {@link StringConverter} for {@link LocalDate} values, using a
     * default formatter and parser based on {@link IsoChronology},
     * {@link FormatStyle#SHORT}, and the user's {@link Locale}.
     *
     * <p>This converter ensures symmetry between the toString() and
     * fromString() methods. Many of the default locale based patterns used by
     * {@link DateTimeFormatter} will display only two digits for the year when
     * formatting to a string. This would cause a value like 1955 to be
     * displayed as 55, which in turn would be parsed back as 2055. This
     * converter modifies two-digit year patterns to always use four digits. The
     * input parsing is not affected, so two digit year values can still be
     * parsed leniently as expected in these locales.</p>
     */
    public LocalDateStringConverter() {
        ldtConverter = new LocalDateTimeStringConverter.LdtConverter<LocalDate>(LocalDate.class, null, null,
                                                  null, null, null, null);
    }

    /**
     * Create a {@link StringConverter} for {@link LocalDate} values, using a
     * default formatter and parser based on {@link IsoChronology},
     * the specified {@link FormatStyle}, and the user's {@link Locale}.
     *
     * @param dateStyle The {@link FormatStyle} that will be used by the default
     * formatter and parser. If null then {@link FormatStyle#SHORT} will be used.
     */
    public LocalDateStringConverter(FormatStyle dateStyle) {
        ldtConverter = new LocalDateTimeStringConverter.LdtConverter<LocalDate>(LocalDate.class, null, null,
                                                  dateStyle, null, null, null);
    }

    /**
     * Create a {#link StringConverter} for {@link LocalDate} values using the supplied
     * formatter and parser.
     *
     * <p>For example, to use a fixed pattern for converting both ways:</p>
     * <blockquote><pre>
     * String pattern = "yyyy-MM-dd";
     * DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
     * StringConverter&lt;LocalDate&gt; converter =
     *     DateTimeStringConverter.getLocalDateStringConverter(formatter, null);
     * </pre></blockquote>
     *
     * Note that the formatter and parser can be created to handle non-default
     * {@link Locale} and {@link Chronology} as needed.
     *
     * @param formatter An instance of {@link DateTimeFormatter} that will be
     * used for formatting by the toString() method. If null then a default
     * formatter will be used.
     * @param parser An instance of {@link DateTimeFormatter} that will be used
     * for parsing by the fromString() method. This can be identical to
     * formatter. If null then formatter will be used, and if that is also null,
     * then a default parser will be used.
     */
    public LocalDateStringConverter(DateTimeFormatter formatter, DateTimeFormatter parser) {
        ldtConverter = new LocalDateTimeStringConverter.LdtConverter<LocalDate>(LocalDate.class, formatter, parser,
                                                   null, null, null, null);
    }


    /**
     * Create a StringConverter for {@link LocalDate} values using a default
     * formatter and parser, which will be based on the supplied
     * {@link FormatStyle}, {@link Locale}, and {@link Chronology}.
     *
     * @param dateStyle The {@link FormatStyle} that will be used by the default
     * formatter and parser. If null then {@link FormatStyle#SHORT} will be used.
     * @param locale The {@link Locale} that will be used by the default
     * formatter and parser. If null then
     * {@code Locale.getDefault(Locale.Category.FORMAT)} will be used.
     * @param chronology The {@link Chronology} that will be used by the default
     * formatter and parser. If null then {@link IsoChronology#INSTANCE} will be used.
     */
    public LocalDateStringConverter(FormatStyle dateStyle, Locale locale, Chronology chronology) {
        ldtConverter = new LocalDateTimeStringConverter.LdtConverter<LocalDate>(LocalDate.class, null, null,
                                                  dateStyle, null, locale, chronology);
    }

    // ------------------------------------------------------- Converter Methods

    /** {@inheritDoc} */
    @Override public LocalDate fromString(String value) {
        return ldtConverter.fromString(value);
    }

    /** {@inheritDoc} */
    @Override public String toString(LocalDate value) {
        return ldtConverter.toString(value);
    }

}
