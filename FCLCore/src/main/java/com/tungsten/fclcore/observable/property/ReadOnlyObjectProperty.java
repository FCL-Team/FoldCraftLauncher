package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.value.ObservableValue;

/**
 * Minimal reimplementation of {@code fakefx.beans.property.ReadOnlyObjectProperty}.
 */
public abstract class ReadOnlyObjectProperty<T> implements ReadOnlyProperty<T>, ObservableValue<T> {

    public ReadOnlyObjectProperty() {
    }

    @Override
    public T getValue() {
        return get();
    }

    public abstract T get();

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ReadOnlyObjectProperty [");
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
