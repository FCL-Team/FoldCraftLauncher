package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.binding.Bindings;

/**
 * Minimal reimplementation of {@code StringProperty}.
 */
public abstract class StringProperty extends ReadOnlyStringProperty implements Property<String> {

    public StringProperty() {
    }

    @Override
    public void setValue(String v) {
        set(v);
    }

    public abstract void set(String value);

    @Override
    public void bindBidirectional(Property<String> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override
    public void unbindBidirectional(Property<String> other) {
        Bindings.unbindBidirectional(this, other);
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("StringProperty [");
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
