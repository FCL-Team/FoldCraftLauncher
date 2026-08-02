package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.binding.Bindings;
import com.tungsten.fclcore.observable.collections.ObservableList;

/**
 * Minimal reimplementation of {@code fakefx.beans.property.ListProperty}.
 */
public abstract class ListProperty<E> extends ReadOnlyListProperty<E> implements Property<ObservableList<E>> {

    public ListProperty() {
    }

    @Override
    public void setValue(ObservableList<E> v) {
        set(v);
    }

    public abstract void set(ObservableList<E> value);

    @Override
    public void bindBidirectional(Property<ObservableList<E>> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override
    public void unbindBidirectional(Property<ObservableList<E>> other) {
        Bindings.unbindBidirectional(this, other);
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ListProperty [");
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
