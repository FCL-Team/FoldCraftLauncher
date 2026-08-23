package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.value.ObservableNumberValue;

import java.util.Locale;

public interface NumberExpression extends ObservableNumberValue {

    // ===============================================================
    // Negation

    NumberBinding negate();

    // ===============================================================
    // Plus

    NumberBinding add(final ObservableNumberValue other);

    NumberBinding add(final double other);

    NumberBinding add(final float other);

    NumberBinding add(final long other);

    NumberBinding add(final int other);

    // ===============================================================
    // Minus

    NumberBinding subtract(final ObservableNumberValue other);

    NumberBinding subtract(final double other);

    NumberBinding subtract(final float other);

    NumberBinding subtract(final long other);

    NumberBinding subtract(final int other);

    // ===============================================================
    // Times

    NumberBinding multiply(final ObservableNumberValue other);

    NumberBinding multiply(final double other);

    NumberBinding multiply(final float other);

    NumberBinding multiply(final long other);

    NumberBinding multiply(final int other);

    // ===============================================================
    // DividedBy

    NumberBinding divide(final ObservableNumberValue other);

    NumberBinding divide(final double other);

    NumberBinding divide(final float other);

    NumberBinding divide(final long other);

    NumberBinding divide(final int other);

    // ===============================================================
    // IsEqualTo

    BooleanBinding isEqualTo(final ObservableNumberValue other);

    BooleanBinding isEqualTo(final ObservableNumberValue other, double epsilon);

    BooleanBinding isEqualTo(final double other, double epsilon);

    BooleanBinding isEqualTo(final float other, double epsilon);

    BooleanBinding isEqualTo(final long other);

    BooleanBinding isEqualTo(final long other, double epsilon);

    BooleanBinding isEqualTo(final int other);

    BooleanBinding isEqualTo(final int other, double epsilon);

    // ===============================================================
    // IsNotEqualTo

    BooleanBinding isNotEqualTo(final ObservableNumberValue other);

    BooleanBinding isNotEqualTo(final ObservableNumberValue other,
            double epsilon);

    BooleanBinding isNotEqualTo(final double other, double epsilon);

    BooleanBinding isNotEqualTo(final float other, double epsilon);

    BooleanBinding isNotEqualTo(final long other);

    BooleanBinding isNotEqualTo(final long other, double epsilon);

    BooleanBinding isNotEqualTo(final int other);

    BooleanBinding isNotEqualTo(final int other, double epsilon);

    // ===============================================================
    // IsGreaterThan

    BooleanBinding greaterThan(final ObservableNumberValue other);

    BooleanBinding greaterThan(final double other);

    BooleanBinding greaterThan(final float other);

    BooleanBinding greaterThan(final long other);

    BooleanBinding greaterThan(final int other);

    // ===============================================================
    // IsLesserThan

    BooleanBinding lessThan(final ObservableNumberValue other);

    BooleanBinding lessThan(final double other);

    BooleanBinding lessThan(final float other);

    BooleanBinding lessThan(final long other);

    BooleanBinding lessThan(final int other);

    // ===============================================================
    // IsGreaterThanOrEqualTo

    BooleanBinding greaterThanOrEqualTo(final ObservableNumberValue other);

    BooleanBinding greaterThanOrEqualTo(final double other);

    BooleanBinding greaterThanOrEqualTo(final float other);

    BooleanBinding greaterThanOrEqualTo(final long other);

    BooleanBinding greaterThanOrEqualTo(final int other);

    // ===============================================================
    // IsLessThanOrEqualTo

    BooleanBinding lessThanOrEqualTo(final ObservableNumberValue other);

    BooleanBinding lessThanOrEqualTo(final double other);

    BooleanBinding lessThanOrEqualTo(final float other);

    BooleanBinding lessThanOrEqualTo(final long other);

    BooleanBinding lessThanOrEqualTo(final int other);

    // ===============================================================
    // String conversions

    StringBinding asString();

    StringBinding asString(String format);

    StringBinding asString(Locale locale, String format);
}
