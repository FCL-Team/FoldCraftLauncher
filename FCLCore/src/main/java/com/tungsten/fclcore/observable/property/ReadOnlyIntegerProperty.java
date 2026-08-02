package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.value.ObservableIntegerValue;

/**
 * Minimal reimplementation of {@code fakefx.beans.property.ReadOnlyIntegerProperty}.
 */
public abstract class ReadOnlyIntegerProperty implements ReadOnlyProperty<Number>, ObservableIntegerValue {

    public ReadOnlyIntegerProperty() {
    }

    @Override
    public Integer getValue() {
        return get();
    }

    @Override
    public int intValue() {
        return get();
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
        final StringBuilder result = new StringBuilder("ReadOnlyIntegerProperty [");
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
