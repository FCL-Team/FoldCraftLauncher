package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.binding.Bindings;

/**
 * Minimal reimplementation of {@code LongProperty}.
 */
public abstract class LongProperty extends ReadOnlyLongProperty implements Property<Number> {

    public LongProperty() {
    }

    @Override
    public void setValue(Number v) {
        if (v == null) {
            set(0L);
        } else {
            set(v.longValue());
        }
    }

    public abstract void set(long value);

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
        final StringBuilder result = new StringBuilder("LongProperty [");
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
