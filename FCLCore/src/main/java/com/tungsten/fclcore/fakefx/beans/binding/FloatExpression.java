package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.value.ObservableFloatValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;

public abstract class FloatExpression extends NumberExpressionBase implements
        ObservableFloatValue {

    /**
     * Creates a default {@code FloatExpression}.
     */
    public FloatExpression() {
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
        return get();
    }

    @Override
    public double doubleValue() {
        return (double) get();
    }

    @Override
    public Float getValue() {
        return get();
    }

    public static FloatExpression floatExpression(
            final ObservableFloatValue value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return (value instanceof FloatExpression) ? (FloatExpression) value
                : new FloatBinding() {
                    {
                        super.bind(value);
                    }

                    @Override
                    public void dispose() {
                        super.unbind(value);
                    }

                    @Override
                    protected float computeValue() {
                        return value.get();
                    }

                    @Override
                    public ObservableList<ObservableFloatValue> getDependencies() {
                        return FXCollections.singletonObservableList(value);
                    }
                };
    }

    public static <T extends Number> FloatExpression floatExpression(final ObservableValue<T> value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return (value instanceof FloatExpression) ? (FloatExpression) value
                : new FloatBinding() {
            {
                super.bind(value);
            }

            @Override
            public void dispose() {
                super.unbind(value);
            }

            @Override
            protected float computeValue() {
                final T val = value.getValue();
                return val == null ? 0f :  val.floatValue();
            }

            @Override
            public ObservableList<ObservableValue<T>> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }


    @Override
    public FloatBinding negate() {
        return (FloatBinding) Bindings.negate(this);
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
    public FloatBinding add(final long other) {
        return (FloatBinding) Bindings.add(this, other);
    }

    @Override
    public FloatBinding add(final int other) {
        return (FloatBinding) Bindings.add(this, other);
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
    public FloatBinding subtract(final long other) {
        return (FloatBinding) Bindings.subtract(this, other);
    }

    @Override
    public FloatBinding subtract(final int other) {
        return (FloatBinding) Bindings.subtract(this, other);
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
    public FloatBinding multiply(final long other) {
        return (FloatBinding) Bindings.multiply(this, other);
    }

    @Override
    public FloatBinding multiply(final int other) {
        return (FloatBinding) Bindings.multiply(this, other);
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
    public FloatBinding divide(final long other) {
        return (FloatBinding) Bindings.divide(this, other);
    }

    @Override
    public FloatBinding divide(final int other) {
        return (FloatBinding) Bindings.divide(this, other);
    }

    /**
     * Creates an {@link javafx.beans.binding.ObjectExpression} that holds the value
     * of this {@code FloatExpression}. If the
     * value of this {@code FloatExpression} changes, the value of the
     * {@code ObjectExpression} will be updated automatically.
     *
     * @return the new {@code ObjectExpression}
     * @since JavaFX 8.0
     */
    public ObjectExpression<Float> asObject() {
        return new ObjectBinding<Float>() {
            {
                bind(FloatExpression.this);
            }

            @Override
            public void dispose() {
                unbind(FloatExpression.this);
            }

            @Override
            protected Float computeValue() {
                return FloatExpression.this.getValue();
            }
        };
    }
}
