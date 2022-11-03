package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.value.WritableListValue;
import com.tungsten.fclcore.fakefx.collections.ObservableList;

public abstract class ListProperty<E> extends ReadOnlyListProperty<E> implements
        Property<ObservableList<E>>, WritableListValue<E> {

    /**
     * Creates a default {@code ListProperty}.
     */
    public ListProperty() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(ObservableList<E> v) {
        set(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindBidirectional(Property<ObservableList<E>> other) {
        Bindings.bindBidirectional(this, other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unbindBidirectional(Property<ObservableList<E>> other) {
        Bindings.unbindBidirectional(this, other);
    }

    /**
     * Returns a string representation of this {@code ListProperty} object.
     * @return a string representation of this {@code ListProperty} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder(
                "ListProperty [");
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
