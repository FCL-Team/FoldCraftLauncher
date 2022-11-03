package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.value.ObservableLongValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;

public abstract class LongExpression extends NumberExpressionBase implements
        ObservableLongValue {

    /**
     * Creates a default {@code LongExpression}.
     */
    public LongExpression() {
    }

    @Override
    public int intValue() {
        return (int) get();
    }

    @Override
    public long longValue() {
        return get();
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
    public Long getValue() {
        return get();
    }

    public static LongExpression longExpression(final ObservableLongValue value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return (value instanceof LongExpression) ? (LongExpression) value
                : new LongBinding() {
                    {
                        super.bind(value);
                    }

                    @Override
                    public void dispose() {
                        super.unbind(value);
                    }

                    @Override
                    protected long computeValue() {
                        return value.get();
                    }

                    @Override
                    public ObservableList<ObservableLongValue> getDependencies() {
                        return FXCollections.singletonObservableList(value);
                    }
        };
    }

    public static <T extends Number> LongExpression longExpression(final ObservableValue<T> value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return (value instanceof LongExpression) ? (LongExpression) value
                : new LongBinding() {
            {
                super.bind(value);
            }

            @Override
            public void dispose() {
                super.unbind(value);
            }

            @Override
            protected long computeValue() {
                final T val = value.getValue();
                return val == null ? 0L : val.longValue();
            }

            @Override
            public ObservableList<ObservableValue<T>> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }


    @Override
    public LongBinding negate() {
        return (LongBinding) Bindings.negate(this);
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
    public LongBinding add(final int other) {
        return (LongBinding) Bindings.add(this, other);
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
    public LongBinding subtract(final int other) {
        return (LongBinding) Bindings.subtract(this, other);
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
    public LongBinding multiply(final int other) {
        return (LongBinding) Bindings.multiply(this, other);
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
    public LongBinding divide(final int other) {
        return (LongBinding) Bindings.divide(this, other);
    }

    public ObjectExpression<Long> asObject() {
        return new ObjectBinding<Long>() {
            {
                bind(LongExpression.this);
            }

            @Override
            public void dispose() {
                unbind(LongExpression.this);
            }

            @Override
            protected Long computeValue() {
                return LongExpression.this.getValue();
            }
        };
    }
}
