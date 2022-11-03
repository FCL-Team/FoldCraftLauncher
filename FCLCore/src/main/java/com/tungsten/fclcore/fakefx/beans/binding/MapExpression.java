package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.value.ObservableMapValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.binding.StringFormatter;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.MapChangeListener;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;

import java.util.*;

public abstract class MapExpression<K, V> implements ObservableMapValue<K, V> {

    private static final ObservableMap EMPTY_MAP = new EmptyObservableMap();

    private static class EmptyObservableMap<K, V> extends AbstractMap<K, V> implements ObservableMap<K, V> {

        @Override
        public Set<Entry<K, V>> entrySet() {
            return Collections.emptySet();
        }

        @Override
        public void addListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            // no-op
        }

        @Override
        public void removeListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
            // no-op
        }

        @Override
        public void addListener(InvalidationListener listener) {
            // no-op
        }

        @Override
        public void removeListener(InvalidationListener listener) {
            // no-op
        }
    }

    @Override
    public ObservableMap<K, V> getValue() {
        return get();
    }

    /**
     * Creates a default {@code MapExpression}.
     */
    public MapExpression() {
    }

    public static <K, V> MapExpression<K, V> mapExpression(final ObservableMapValue<K, V> value) {
        if (value == null) {
            throw new NullPointerException("Map must be specified.");
        }
        return value instanceof MapExpression ? (MapExpression<K, V>) value
                : new MapBinding<K, V>() {
            {
                super.bind(value);
            }

            @Override
            public void dispose() {
                super.unbind(value);
            }

            @Override
            protected ObservableMap<K, V> computeValue() {
                return value.get();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    /**
     * The size of the map
     * @return the size
     */
    public int getSize() {
        return size();
    }

    /**
     * An integer property that represents the size of the map.
     * @return the property
     */
    public abstract ReadOnlyIntegerProperty sizeProperty();

    /**
     * A boolean property that is {@code true}, if the map is empty.
     * @return the {@code ReadOnlyBooleanProperty}
     */
    public abstract ReadOnlyBooleanProperty emptyProperty();

    /**
     * Creates a new {@link ObjectBinding} that contains the mapping of the specified key.
     *
     * @param key the key of the mapping
     * @return the {@code ObjectBinding}
     */
    public ObjectBinding<V> valueAt(K key) {
        return Bindings.valueAt(this, key);
    }

    /**
     * Creates a new {@link ObjectBinding} that contains the mapping of the specified key.
     *
     * @param key the key of the mapping
     * @return the {@code ObjectBinding}
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public ObjectBinding<V> valueAt(ObservableValue<K> key) {
        return Bindings.valueAt(this, key);
    }

    /**
     * Creates a new {@link BooleanBinding} that holds {@code true} if this map is equal to
     * another {@link ObservableMap}.
     *
     * @param other
     *            the other {@code ObservableMap}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if {@code other} is {@code null}
     */
    public BooleanBinding isEqualTo(final ObservableMap<?, ?> other) {
        return Bindings.equal(this, other);
    }

    /**
     * Creates a new {@link BooleanBinding} that holds {@code true} if this map is not equal to
     * another {@link ObservableMap}.
     *
     * @param other
     *            the other {@code ObservableMap}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if {@code other} is {@code null}
     */
    public BooleanBinding isNotEqualTo(final ObservableMap<?, ?> other) {
        return Bindings.notEqual(this, other);
    }

    /**
     * Creates a new {@link BooleanBinding} that holds {@code true} if the wrapped map is {@code null}.
     *
     * @return the new {@code BooleanBinding}
     */
    public BooleanBinding isNull() {
        return Bindings.isNull(this);
    }

    /**
     * Creates a new {@link BooleanBinding} that holds {@code true} if the wrapped map is not {@code null}.
     *
     * @return the new {@code BooleanBinding}
     */
    public BooleanBinding isNotNull() {
        return Bindings.isNotNull(this);
    }

    public StringBinding asString() {
        return (StringBinding) StringFormatter.convert(this);
    }

    @Override
    public int size() {
        final ObservableMap<K, V> map = get();
        return (map == null)? EMPTY_MAP.size() : map.size();
    }

    @Override
    public boolean isEmpty() {
        final ObservableMap<K, V> map = get();
        return (map == null)? EMPTY_MAP.isEmpty() : map.isEmpty();
    }

    @Override
    public boolean containsKey(Object obj) {
        final ObservableMap<K, V> map = get();
        return (map == null)? EMPTY_MAP.containsKey(obj) : map.containsKey(obj);
    }

    @Override
    public boolean containsValue(Object obj) {
        final ObservableMap<K, V> map = get();
        return (map == null)? EMPTY_MAP.containsValue(obj) : map.containsValue(obj);
    }

    @Override
    public V put(K key, V value) {
        final ObservableMap<K, V> map = get();
        return (map == null)? (V) EMPTY_MAP.put(key, value) : map.put(key, value);
    }

    @Override
    public V remove(Object obj) {
        final ObservableMap<K, V> map = get();
        return (map == null)? (V) EMPTY_MAP.remove(obj) : map.remove(obj);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> elements) {
        final ObservableMap<K, V> map = get();
        if (map == null) {
            EMPTY_MAP.putAll(elements);
        } else {
            map.putAll(elements);
        }
    }

    @Override
    public void clear() {
        final ObservableMap<K, V> map = get();
        if (map == null) {
            EMPTY_MAP.clear();
        } else {
            map.clear();
        }
    }

    @Override
    public Set<K> keySet() {
        final ObservableMap<K, V> map = get();
        return (map == null)? EMPTY_MAP.keySet() : map.keySet();
    }

    @Override
    public Collection<V> values() {
        final ObservableMap<K, V> map = get();
        return (map == null)? EMPTY_MAP.values() : map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        final ObservableMap<K, V> map = get();
        return (map == null)? EMPTY_MAP.entrySet() : map.entrySet();
    }

    @Override
    public V get(Object key) {
        final ObservableMap<K, V> map = get();
        return (map == null)? (V) EMPTY_MAP.get(key) : map.get(key);
    }

}
