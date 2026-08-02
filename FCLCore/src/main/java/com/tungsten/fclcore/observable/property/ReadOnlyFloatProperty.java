package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.value.ObservableFloatValue;

/**
 * Minimal reimplementation of {@code ReadOnlyFloatProperty}.
 */
public abstract class ReadOnlyFloatProperty implements ReadOnlyProperty<Number>, ObservableFloatValue {

    public ReadOnlyFloatProperty() {
    }

    @Override
    public Float getValue() {
        return get();
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
        return get();
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ReadOnlyFloatProperty [");
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
