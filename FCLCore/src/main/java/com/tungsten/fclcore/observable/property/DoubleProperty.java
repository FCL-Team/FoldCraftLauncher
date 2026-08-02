package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.binding.Bindings;

/**
 * Minimal reimplementation of {@code DoubleProperty}.
 */
public abstract class DoubleProperty extends ReadOnlyDoubleProperty implements Property<Number> {

    public DoubleProperty() {
    }

    @Override
    public void setValue(Number v) {
        if (v == null) {
            set(0.0);
        } else {
            set(v.doubleValue());
        }
    }

    public abstract void set(double value);

    @Override
    public void bindBidirectional(Property<Number> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override
    public void unbindBidirectional(Property<Number> other) {
        Bindings.unbindBidirectional(this, other);
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("DoubleProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && (!name.equals(""))) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }
}
