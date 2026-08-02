package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.binding.Bindings;

/**
 * Minimal reimplementation of {@code fakefx.beans.property.ObjectProperty}.
 */
public abstract class ObjectProperty<T> extends ReadOnlyObjectProperty<T> implements Property<T> {

    public ObjectProperty() {
    }

    @Override
    public void setValue(T v) {
        set(v);
    }

    public abstract void set(T value);

    @Override
    public void bindBidirectional(Property<T> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override
    public void unbindBidirectional(Property<T> other) {
        Bindings.unbindBidirectional(this, other);
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ObjectProperty [");
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
