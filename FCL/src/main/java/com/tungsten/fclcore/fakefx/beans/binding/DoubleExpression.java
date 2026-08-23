package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.value.ObservableDoubleValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableNumberValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;

public abstract class DoubleExpression extends NumberExpressionBase implements
        ObservableDoubleValue {

    /**
     * Creates a default {@code DoubleExpression}.
     */
    public DoubleExpression() {
    }

    @Override
    public int intValue() {
        return (int) get();
    }

    @Override
    public long longValue() {
        return (long) get();
    }

    @Override
    public float floatValue() {
        return (float) get();
    }

    @Override
    public double doubleValue() {
        return get();
    }

    @Override
    public Double getValue() {
        return get();
    }

    public static DoubleExpression doubleExpression(
            final ObservableDoubleValue value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return (value instanceof DoubleExpression) ? (DoubleExpression) value
                : new DoubleBinding() {
                    {
                        super.bind(value);
                    }

                    @Override
                    public void dispose() {
                        super.unbind(value);
                    }

                    @Override
                    protected double computeValue() {
                        return value.get();
                    }

                    @Override
                    public ObservableList<ObservableDoubleValue> getDependencies() {
                        return FXCollections.singletonObservableList(value);
                    }
                };
    }

    public static <T extends Number> DoubleExpression doubleExpression(final ObservableValue<T> value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return (value instanceof DoubleExpression) ? (DoubleExpression) value
                : new DoubleBinding() {
            {
                super.bind(value);
            }

            @Override
            public void dispose() {
                super.unbind(value);
            }

            @Override
            protected double computeValue() {
                final T val = value.getValue();
                return val == null ? 0.0 : val.doubleValue();
            }

            @Override
            public ObservableList<ObservableValue<T>> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    @Override
    public DoubleBinding negate() {
        return (DoubleBinding) Bindings.negate(this);
    }

    @Override
    public DoubleBinding add(final ObservableNumberValue other) {
        return (DoubleBinding) Bindings.add(this, other);
    }

    @Override
    public DoubleBinding add(final double other) {
        return Bindings.add(this, other);
    }

    @Override
    public DoubleBinding add(final float other) {
        return (DoubleBinding) Bindings.add(this, other);
    }

    @Override
    public DoubleBinding add(final long other) {
        return (DoubleBinding) Bindings.add(this, other);
    }

    @Override
    public DoubleBinding add(final int other) {
        return (DoubleBinding) Bindings.add(this, other);
    }

    @Override
    public DoubleBinding subtract(final ObservableNumberValue other) {
        return (DoubleBinding) Bindings.subtract(this, other);
    }

    @Override
    public DoubleBinding subtract(final double other) {
        return Bindings.subtract(this, other);
    }

    @Override
    public DoubleBinding subtract(final float other) {
        return (DoubleBinding) Bindings.subtract(this, other);
    }

    @Override
    public DoubleBinding subtract(final long other) {
        return (DoubleBinding) Bindings.subtract(this, other);
    }

    @Override
    public DoubleBinding subtract(final int other) {
        return (DoubleBinding) Bindings.subtract(this, other);
    }

    @Override
    public DoubleBinding multiply(final ObservableNumberValue other) {
        return (DoubleBinding) Bindings.multiply(this, other);
    }

    @Override
    public DoubleBinding multiply(final double other) {
        return Bindings.multiply(this, other);
    }

    @Override
    public DoubleBinding multiply(final float other) {
        return (DoubleBinding) Bindings.multiply(this, other);
    }

    @Override
    public DoubleBinding multiply(final long other) {
        return (DoubleBinding) Bindings.multiply(this, other);
    }

    @Override
    public DoubleBinding multiply(final int other) {
        return (DoubleBinding) Bindings.multiply(this, other);
    }

    @Override
    public DoubleBinding divide(final ObservableNumberValue other) {
        return (DoubleBinding) Bindings.divide(this, other);
    }

    @Override
    public DoubleBinding divide(final double other) {
        return Bindings.divide(this, other);
    }

    @Override
    public DoubleBinding divide(final float other) {
        return (DoubleBinding) Bindings.divide(this, other);
    }

    @Override
    public DoubleBinding divide(final long other) {
        return (DoubleBinding) Bindings.divide(this, other);
    }

    @Override
    public DoubleBinding divide(final int other) {
        return (DoubleBinding) Bindings.divide(this, other);
    }

    public ObjectExpression<Double> asObject() {
        return new ObjectBinding<Double>() {
            {
                bind(DoubleExpression.this);
            }

            @Override
            public void dispose() {
                unbind(DoubleExpression.this);
            }

            @Override
            protected Double computeValue() {
                return DoubleExpression.this.getValue();
            }
        };
    }
}
