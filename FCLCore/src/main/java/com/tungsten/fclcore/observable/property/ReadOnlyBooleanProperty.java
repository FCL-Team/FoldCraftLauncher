package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.value.ObservableBooleanValue;

/**
 * Minimal reimplementation of {@code ReadOnlyBooleanProperty}.
 */
public abstract class ReadOnlyBooleanProperty implements ReadOnlyProperty<Boolean>, ObservableBooleanValue {

    public ReadOnlyBooleanProperty() {
    }

    @Override
    public Boolean getValue() {
        return get();
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ReadOnlyBooleanProperty [");
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
