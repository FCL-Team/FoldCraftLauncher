package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.value.ObservableValue;

/**
 * Minimal reimplementation of {@code fakefx.beans.property.ReadOnlyStringProperty}.
 */
public abstract class ReadOnlyStringProperty implements ReadOnlyProperty<String>, ObservableValue<String> {

    public ReadOnlyStringProperty() {
    }

    @Override
    public String getValue() {
        return get();
    }

    public abstract String get();

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ReadOnlyStringProperty [");
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
