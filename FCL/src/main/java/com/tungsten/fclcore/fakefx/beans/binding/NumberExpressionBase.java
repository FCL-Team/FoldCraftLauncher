package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.value.ObservableDoubleValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableFloatValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableIntegerValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableLongValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableNumberValue;
import com.tungsten.fclcore.fakefx.binding.StringFormatter;

import java.util.Locale;

/**
 * {@code NumberExpressionBase} contains convenience methods to generate bindings in a fluent style,
 * that are common to all NumberExpression subclasses.
 * <p>
 * NumberExpressionBase serves as a place for common code of specific NumberExpression subclasses for the
 * specific number type.
 * @see IntegerExpression
 * @see LongExpression
 * @see FloatExpression
 * @see DoubleExpression
 * @since JavaFX 2.0
 */
public abstract class NumberExpressionBase implements NumberExpression {

    /**
     * Creates a default {@code NumberExpressionBase}.
     */
    public NumberExpressionBase() {
    }

    public static <S extends Number> NumberExpressionBase numberExpression(
            final ObservableNumberValue value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        NumberExpressionBase result = (NumberExpressionBase) ((value instanceof NumberExpressionBase) ? value
                : (value instanceof ObservableIntegerValue) ? IntegerExpression
                        .integerExpression((ObservableIntegerValue) value)
                        : (value instanceof ObservableDoubleValue) ? DoubleExpression
                                .doubleExpression((ObservableDoubleValue) value)
                                : (value instanceof ObservableFloatValue) ? FloatExpression
                                        .floatExpression((ObservableFloatValue) value)
                                        : (value instanceof ObservableLongValue) ? LongExpression
                                                .longExpression((ObservableLongValue) value)
                                                : null);
        if (result != null) {
            return result;
        } else {
            throw new IllegalArgumentException("Unsupported Type");
        }
    }

    @Override
    public NumberBinding add(final ObservableNumberValue other) {
        return Bindings.add(this, other);
    }

    @Override
    public NumberBinding subtract(final ObservableNumberValue other) {
        return Bindings.subtract(this, other);
    }

    @Override
    public NumberBinding multiply(final ObservableNumberValue other) {
        return Bindings.multiply(this, other);
    }

    @Override
    public NumberBinding divide(final ObservableNumberValue other) {
        return Bindings.divide(this, other);
    }

    // ===============================================================
    // IsEqualTo

    @Override
    public BooleanBinding isEqualTo(final ObservableNumberValue other) {
        return Bindings.equal(this, other);
    }

    @Override
    public BooleanBinding isEqualTo(final ObservableNumberValue other,
            double epsilon) {
        return Bindings.equal(this, other, epsilon);
    }

    @Override
    public BooleanBinding isEqualTo(final double other, double epsilon) {
        return Bindings.equal(this, other, epsilon);
    }

    @Override
    public BooleanBinding isEqualTo(final float other, double epsilon) {
        return Bindings.equal(this, other, epsilon);
    }

    @Override
    public BooleanBinding isEqualTo(final long other) {
        return Bindings.equal(this, other);
    }

    @Override
    public BooleanBinding isEqualTo(final long other, double epsilon) {
        return Bindings.equal(this, other, epsilon);
    }

    @Override
    public BooleanBinding isEqualTo(final int other) {
        return Bindings.equal(this, other);
    }

    @Override
    public BooleanBinding isEqualTo(final int other, double epsilon) {
        return Bindings.equal(this, other, epsilon);
    }

    // ===============================================================
    // IsNotEqualTo

    @Override
    public BooleanBinding isNotEqualTo(final ObservableNumberValue other) {
        return Bindings.notEqual(this, other);
    }

    @Override
    public BooleanBinding isNotEqualTo(final ObservableNumberValue other,
            double epsilon) {
        return Bindings.notEqual(this, other, epsilon);
    }

    @Override
    public BooleanBinding isNotEqualTo(final double other, double epsilon) {
        return Bindings.notEqual(this, other, epsilon);
    }

    @Override
    public BooleanBinding isNotEqualTo(final float other, double epsilon) {
        return Bindings.notEqual(this, other, epsilon);
    }

    @Override
    public BooleanBinding isNotEqualTo(final long other) {
        return Bindings.notEqual(this, other);
    }

    @Override
    public BooleanBinding isNotEqualTo(final long other, double epsilon) {
        return Bindings.notEqual(this, other, epsilon);
    }

    @Override
    public BooleanBinding isNotEqualTo(final int other) {
        return Bindings.notEqual(this, other);
    }

    @Override
    public BooleanBinding isNotEqualTo(final int other, double epsilon) {
        return Bindings.notEqual(this, other, epsilon);
    }

    // ===============================================================
    // IsGreaterThan

    @Override
    public BooleanBinding greaterThan(final ObservableNumberValue other) {
        return Bindings.greaterThan(this, other);
    }

    @Override
    public BooleanBinding greaterThan(final double other) {
        return Bindings.greaterThan(this, other);
    }

    @Override
    public BooleanBinding greaterThan(final float other) {
        return Bindings.greaterThan(this, other);
    }

    @Override
    public BooleanBinding greaterThan(final long other) {
        return Bindings.greaterThan(this, other);
    }

    @Override
    public BooleanBinding greaterThan(final int other) {
        return Bindings.greaterThan(this, other);
    }

    // ===============================================================
    // IsLesserThan

    @Override
    public BooleanBinding lessThan(final ObservableNumberValue other) {
        return Bindings.lessThan(this, other);
    }

    @Override
    public BooleanBinding lessThan(final double other) {
        return Bindings.lessThan(this, other);
    }

    @Override
    public BooleanBinding lessThan(final float other) {
        return Bindings.lessThan(this, other);
    }

    @Override
    public BooleanBinding lessThan(final long other) {
        return Bindings.lessThan(this, other);
    }

    @Override
    public BooleanBinding lessThan(final int other) {
        return Bindings.lessThan(this, other);
    }

    // ===============================================================
    // IsGreaterThanOrEqualTo

    @Override
    public BooleanBinding greaterThanOrEqualTo(final ObservableNumberValue other) {
        return Bindings.greaterThanOrEqual(this, other);
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(final double other) {
        return Bindings.greaterThanOrEqual(this, other);
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(final float other) {
        return Bindings.greaterThanOrEqual(this, other);
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(final long other) {
        return Bindings.greaterThanOrEqual(this, other);
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(final int other) {
        return Bindings.greaterThanOrEqual(this, other);
    }

    // ===============================================================
    // IsLessThanOrEqualTo

    @Override
    public BooleanBinding lessThanOrEqualTo(final ObservableNumberValue other) {
        return Bindings.lessThanOrEqual(this, other);
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(final double other) {
        return Bindings.lessThanOrEqual(this, other);
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(final float other) {
        return Bindings.lessThanOrEqual(this, other);
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(final long other) {
        return Bindings.lessThanOrEqual(this, other);
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(final int other) {
        return Bindings.lessThanOrEqual(this, other);
    }

    // ===============================================================
    // String conversions

    @Override
    public StringBinding asString() {
        return (StringBinding) StringFormatter.convert(this);
    }

    @Override
    public StringBinding asString(String format) {
        return (StringBinding) Bindings.format(format, this);
    }

    @Override
    public StringBinding asString(Locale locale, String format) {
        return (StringBinding) Bindings.format(locale, format, this);
    }

}
