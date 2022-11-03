package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.value.WritableSetValue;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;

public abstract class SetProperty<E> extends ReadOnlySetProperty<E> implements
        Property<ObservableSet<E>>, WritableSetValue<E> {

    /**
     * Creates a default {@code SetProperty}.
     */
    public SetProperty() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(ObservableSet<E> v) {
        set(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindBidirectional(Property<ObservableSet<E>> other) {
        Bindings.bindBidirectional(this, other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unbindBidirectional(Property<ObservableSet<E>> other) {
        Bindings.unbindBidirectional(this, other);
    }

    /**
     * Returns a string representation of this {@code SetProperty} object.
     * @return a string representation of this {@code SetProperty} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder(
                "SetProperty [");
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
