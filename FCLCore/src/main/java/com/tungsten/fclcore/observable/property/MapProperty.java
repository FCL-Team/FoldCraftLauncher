package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.binding.Bindings;
import com.tungsten.fclcore.observable.collections.ObservableMap;

/**
 * Minimal reimplementation of {@code MapProperty}.
 */
public abstract class MapProperty<K, V> extends ReadOnlyMapProperty<K, V> implements Property<ObservableMap<K, V>> {

    public MapProperty() {
    }

    @Override
    public void setValue(ObservableMap<K, V> v) {
        set(v);
    }

    public abstract void set(ObservableMap<K, V> value);

    @Override
    public void bindBidirectional(Property<ObservableMap<K, V>> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override
    public void unbindBidirectional(Property<ObservableMap<K, V>> other) {
        Bindings.unbindBidirectional(this, other);
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("MapProperty [");
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
