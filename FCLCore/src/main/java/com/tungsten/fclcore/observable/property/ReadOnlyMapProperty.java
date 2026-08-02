package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.collections.ObservableMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Simplified reimplementation of {@code fakefx.beans.property.ReadOnlyMapProperty}
 * （合并 MapExpression 的委托方法）：本身即 ObservableMap 视图。
 */
public abstract class ReadOnlyMapProperty<K, V> implements ReadOnlyProperty<ObservableMap<K, V>>, ObservableMap<K, V> {

    public ReadOnlyMapProperty() {
    }

    @Override
    public ObservableMap<K, V> getValue() {
        return get();
    }

    public abstract ObservableMap<K, V> get();

    @Override
    public int size() {
        return get().size();
    }

    @Override
    public boolean isEmpty() {
        return get().isEmpty();
    }

    @Override
    public boolean containsKey(Object obj) {
        return get().containsKey(obj);
    }

    @Override
    public boolean containsValue(Object obj) {
        return get().containsValue(obj);
    }

    @Override
    public V get(Object key) {
        return get().get(key);
    }

    @Override
    public V put(K key, V value) {
        return get().put(key, value);
    }

    @Override
    public V remove(Object obj) {
        return get().remove(obj);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> elements) {
        get().putAll(elements);
    }

    @Override
    public void clear() {
        get().clear();
    }

    @Override
    public Set<K> keySet() {
        return get().keySet();
    }

    @Override
    public Collection<V> values() {
        return get().values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return get().entrySet();
    }

    @Override
    public boolean equals(Object other) {
        return get().equals(other);
    }

    @Override
    public int hashCode() {
        return get().hashCode();
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ReadOnlyMapProperty [");
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
