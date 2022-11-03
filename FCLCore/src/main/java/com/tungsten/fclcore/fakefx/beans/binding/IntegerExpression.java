package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.value.ObservableIntegerValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;

public abstract class IntegerExpression extends NumberExpressionBase implements
        ObservableIntegerValue {

    /**
     * Creates a default {@code IntegerExpression}.
     */
    public IntegerExpression() {
    }

    @Override
    public int intValue() {
        return get();
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
        return (double) get();
    }

    @Override
    public Integer getValue() {
        return get();
    }

    public static IntegerExpression integerExpression(
            final ObservableIntegerValue value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return (value instanceof IntegerExpression) ? (IntegerExpression) value
                : new IntegerBinding() {
                    {
                        super.bind(value);
                    }

                    @Override
                    public void dispose() {
                        super.unbind(value);
                    }

                    @Override
                    protected int computeValue() {
                        return value.get();
                    }

                    @Override
                    public ObservableList<ObservableIntegerValue> getDependencies() {
                        return FXCollections.singletonObservableList(value);
                    }
                };
    }

    public static <T extends Number> IntegerExpression integerExpression(final ObservableValue<T> value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return (value instanceof IntegerExpression) ? (IntegerExpression) value
                : new IntegerBinding() {
            {
                super.bind(value);
            }

            @Override
            public void dispose() {
                super.unbind(value);
            }

            @Override
            protected int computeValue() {
                final T val = value.getValue();
                return val == null ? 0 : val.intValue();
            }

            @Override
            public ObservableList<ObservableValue<T>> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }


    @Override
    public IntegerBinding negate() {
        return (IntegerBinding) Bindings.negate(this);
    }

    @Override
    public DoubleBinding add(final double other) {
        return Bindings.add(this, other);
    }

    @Override
    public FloatBinding add(final float other) {
        return (FloatBinding) Bindings.add(this, other);
    }

    @Override
    public LongBinding add(final long other) {
        return (LongBinding) Bindings.add(this, other);
    }

    @Override
    public IntegerBinding add(final int other) {
        return (IntegerBinding) Bindings.add(this, other);
    }

    @Override
    public DoubleBinding subtract(final double other) {
        return Bindings.subtract(this, other);
    }

    @Override
    public FloatBinding subtract(final float other) {
        return (FloatBinding) Bindings.subtract(this, other);
    }

    @Override
    public LongBinding subtract(final long other) {
        return (LongBinding) Bindings.subtract(this, other);
    }

    @Override
    public IntegerBinding subtract(final int other) {
        return (IntegerBinding) Bindings.subtract(this, other);
    }

    @Override
    public DoubleBinding multiply(final double other) {
        return Bindings.multiply(this, other);
    }

    @Override
    public FloatBinding multiply(final float other) {
        return (FloatBinding) Bindings.multiply(this, other);
    }

    @Override
    public LongBinding multiply(final long other) {
        return (LongBinding) Bindings.multiply(this, other);
    }

    @Override
    public IntegerBinding multiply(final int other) {
        return (IntegerBinding) Bindings.multiply(this, other);
    }

    @Override
    public DoubleBinding divide(final double other) {
        return Bindings.divide(this, other);
    }

    @Override
    public FloatBinding divide(final float other) {
        return (FloatBinding) Bindings.divide(this, other);
    }

    @Override
    public LongBinding divide(final long other) {
        return (LongBinding) Bindings.divide(this, other);
    }

    @Override
    public IntegerBinding divide(final int other) {
        return (IntegerBinding) Bindings.divide(this, other);
    }

    public ObjectExpression<Integer> asObject() {
        return new ObjectBinding<Integer>() {
            {
                bind(IntegerExpression.this);
            }

            @Override
            public void dispose() {
                unbind(IntegerExpression.this);
            }

            @Override
            protected Integer computeValue() {
                return IntegerExpression.this.getValue();
            }
        };
    }
}
