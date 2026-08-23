package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.binding.MapExpression;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;

import java.util.Map;

public abstract class ReadOnlyMapProperty<K, V> extends MapExpression<K, V> implements ReadOnlyProperty<ObservableMap<K, V>>  {

    /**
     * The constructor of {@code ReadOnlyMapProperty}.
     */
    public ReadOnlyMapProperty() {
    }

    /**
     * Creates a bidirectional content binding of the {@link ObservableMap}, that is
     * wrapped in this {@code ReadOnlyMapProperty}, and another {@code ObservableMap}.
     * <p>
     * A bidirectional content binding ensures that the content of two {@code ObservableMaps} is the
     * same. If the content of one of the maps changes, the other one will be updated automatically.
     *
     * @param map the {@code ObservableMap} this property should be bound to
     * @throws NullPointerException if {@code map} is {@code null}
     * @throws IllegalArgumentException if {@code map} is the same map that this {@code ReadOnlyMapProperty} points to
     */
    public void bindContentBidirectional(ObservableMap<K, V> map) {
        Bindings.bindContentBidirectional(this, map);
    }

    /**
     * Deletes a bidirectional content binding between the {@link ObservableMap}, that is
     * wrapped in this {@code ReadOnlyMapProperty}, and another {@code Object}.
     *
     * @param object the {@code Object} to which the bidirectional binding should be removed
     * @throws NullPointerException if {@code object} is {@code null}
     * @throws IllegalArgumentException if {@code object} is the same map that this {@code ReadOnlyMapProperty} points to
     */
    public void unbindContentBidirectional(Object object) {
        Bindings.unbindContentBidirectional(this, object);
    }

    /**
     * Creates a content binding between the {@link ObservableMap}, that is
     * wrapped in this {@code ReadOnlyMapProperty}, and another {@code ObservableMap}.
     * <p>
     * A content binding ensures that the content of the wrapped {@code ObservableMaps} is the
     * same as that of the other map. If the content of the other map changes, the wrapped map will be updated
     * automatically. Once the wrapped list is bound to another map, you must not change it directly.
     *
     * @param map the {@code ObservableMap} this property should be bound to
     * @throws NullPointerException if {@code map} is {@code null}
     * @throws IllegalArgumentException if {@code map} is the same map that this {@code ReadOnlyMapProperty} points to
     */
    public void bindContent(ObservableMap<K, V> map) {
        Bindings.bindContent(this, map);
    }

    /**
     * Deletes a content binding between the {@link ObservableMap}, that is
     * wrapped in this {@code ReadOnlyMapProperty}, and another {@code Object}.
     *
     * @param object the {@code Object} to which the binding should be removed
     * @throws NullPointerException if {@code object} is {@code null}
     * @throws IllegalArgumentException if {@code object} is the same map that this {@code ReadOnlyMapProperty} points to
     */
    public void unbindContent(Object object) {
        Bindings.unbindContent(this, object);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Map))
            return false;
        Map<K,V> m = (Map<K,V>) obj;
        if (m.size() != size())
            return false;

        try {
            for (Entry<K,V> e : entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                if (value == null) {
                    if (!(m.get(key)==null && m.containsKey(key)))
                        return false;
                } else {
                    if (!value.equals(m.get(key)))
                        return false;
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }

        return true;
    }

    /**
     * Returns a hash code for this {@code ReadOnlyMapProperty} object.
     * @return a hash code for this {@code ReadOnlyMapProperty} object.
     */
    @Override
    public int hashCode() {
        int h = 0;
        for (Entry<K,V> e : entrySet()) {
            h += e.hashCode();
        }
        return h;
    }

    /**
     * Returns a string representation of this {@code ReadOnlyMapProperty} object.
     * @return a string representation of this {@code ReadOnlyMapProperty} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder(
                "ReadOnlyMapProperty [");
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
