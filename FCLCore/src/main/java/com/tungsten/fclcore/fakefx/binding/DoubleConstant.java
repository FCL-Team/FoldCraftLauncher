package com.tungsten.fclcore.fakefx.binding;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.beans.value.ObservableDoubleValue;

/**
 * A simple DoubleExpression that represents a single constant value.
 */
public final class DoubleConstant implements ObservableDoubleValue {

    private final double value;

    private DoubleConstant(double value) {
        this.value = value;
    }

    public static DoubleConstant valueOf(double value) {
        return new DoubleConstant(value);
    }

    @Override
    public double get() {
        return value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void addListener(InvalidationListener observer) {
        // no-op
    }

    @Override
    public void addListener(ChangeListener<? super Number> listener) {
        // no-op
    }

    @Override
    public void removeListener(InvalidationListener observer) {
        // no-op
    }

    @Override
    public void removeListener(ChangeListener<? super Number> listener) {
        // no-op
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }
}
