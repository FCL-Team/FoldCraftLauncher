package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.value.WritableDoubleValue;
import com.tungsten.fclcore.fakefx.binding.BidirectionalBinding;

import java.util.Objects;

public abstract class DoubleProperty extends ReadOnlyDoubleProperty implements
        Property<Number>, WritableDoubleValue {

    /**
     * Creates a default {@code DoubleProperty}.
     */
    public DoubleProperty() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Number v) {
        if (v == null) {
            set(0.0);
        } else {
            set(v.doubleValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindBidirectional(Property<Number> other) {
        Bindings.bindBidirectional(this, other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unbindBidirectional(Property<Number> other) {
        Bindings.unbindBidirectional(this, other);
    }

    /**
     * Returns a string representation of this {@code DoubleProperty} object.
     * @return a string representation of this {@code DoubleProperty} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder(
                "DoubleProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && (!name.equals(""))) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static DoubleProperty doubleProperty(final Property<Double> property) {
        Objects.requireNonNull(property, "Property cannot be null");
        return new DoublePropertyBase() {
            {
                BidirectionalBinding.bindNumber(this, property);
            }

            @Override
            public Object getBean() {
                return null; // Virtual property, no bean
            }

            @Override
            public String getName() {
                return property.getName();
            }
        };
    }

    @Override
    public ObjectProperty<Double> asObject() {
        return new ObjectPropertyBase<Double> () {
            {
                BidirectionalBinding.bindNumber(this, DoubleProperty.this);
            }

            @Override
            public Object getBean() {
                return null; // Virtual property, does not exist on a bean
            }

            @Override
            public String getName() {
                return DoubleProperty.this.getName();
            }
        };
    }
}
