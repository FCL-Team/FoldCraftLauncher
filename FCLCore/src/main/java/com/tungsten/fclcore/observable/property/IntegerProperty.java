package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.binding.Bindings;

/**
 * Minimal reimplementation of {@code fakefx.beans.property.IntegerProperty}.
 */
public abstract class IntegerProperty extends ReadOnlyIntegerProperty implements Property<Number> {

    public IntegerProperty() {
    }

    @Override
    public void setValue(Number v) {
        if (v == null) {
            // 与 fakefx 一致：setValue(null) 记日志后置 0
            set(0);
        } else {
            set(v.intValue());
        }
    }

    public abstract void set(int value);

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
        final StringBuilder result = new StringBuilder("IntegerProperty [");
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
