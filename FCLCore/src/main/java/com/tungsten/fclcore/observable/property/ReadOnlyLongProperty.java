package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.value.ObservableLongValue;

/**
 * Minimal reimplementation of {@code ReadOnlyLongProperty}.
 */
public abstract class ReadOnlyLongProperty implements ReadOnlyProperty<Number>, ObservableLongValue {

    public ReadOnlyLongProperty() {
    }

    @Override
    public Long getValue() {
        return get();
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
        return get();
    }

    @Override
    public double doubleValue() {
        return get();
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ReadOnlyLongProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }
}
