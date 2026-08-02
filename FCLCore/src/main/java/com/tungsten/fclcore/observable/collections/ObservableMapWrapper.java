package com.tungsten.fclcore.observable.collections;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.WeakListener;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simplified reimplementation of {@code ObservableMapWrapper}：
 * put/remove/clear/putAll 逐键触发 MapChange 事件（wasAdded/wasRemoved 依值判空，
 * 与 JavaFX 一致）；分发前先发 invalidation 监听。
 */
public class ObservableMapWrapper<K, V> extends AbstractMap<K, V> implements ObservableMap<K, V> {

    private final Map<K, V> backingMap;
    private List<InvalidationListener> invalidationListeners;
    private List<MapChangeListener<? super K, ? super V>> mapChangeListeners;

    public ObservableMapWrapper(Map<K, V> map) {
        this.backingMap = map;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (invalidationListeners == null) {
            invalidationListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(invalidationListeners);
        invalidationListeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (invalidationListeners != null) {
            invalidationListeners.remove(listener);
        }
    }

    @Override
    public void addListener(MapChangeListener<? super K, ? super V> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (mapChangeListeners == null) {
            mapChangeListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(mapChangeListeners);
        mapChangeListeners.add(listener);
    }

    @Override
    public void removeListener(MapChangeListener<? super K, ? super V> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (mapChangeListeners != null) {
            mapChangeListeners.remove(listener);
        }
    }

    private static void purgeGarbageCollected(List<?> listeners) {
        listeners.removeIf(l -> l instanceof WeakListener && ((WeakListener) l).wasGarbageCollected());
    }

    private void fireChange(K key, V added, V removed) {
        if (invalidationListeners != null) {
            for (InvalidationListener listener : new ArrayList<>(invalidationListeners)) {
                try {
                    listener.invalidated(this);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
        if (mapChangeListeners != null) {
            final MapChangeListener.Change<K, V> change = new SimpleChange(key, added, removed);
            for (MapChangeListener<? super K, ? super V> listener : new ArrayList<>(mapChangeListeners)) {
                try {
                    listener.onChanged(change);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
    }

    private class SimpleChange extends MapChangeListener.Change<K, V> {

        private final K key;
        private final V added;
        private final V removed;

        SimpleChange(K key, V added, V removed) {
            super(ObservableMapWrapper.this);
            this.key = key;
            this.added = added;
            this.removed = removed;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValueAdded() {
            return added;
        }

        @Override
        public V getValueRemoved() {
            return removed;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (wasAdded()) {
                if (wasRemoved()) {
                    builder.append("replaced ").append(removed).append(" by ").append(added);
                } else {
                    builder.append("added ").append(added);
                }
            } else {
                builder.append("removed ").append(removed);
            }
            builder.append(" at key ").append(key);
            return builder.toString();
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return backingMap.entrySet();
    }

    @Override
    public V put(K key, V value) {
        V ret = backingMap.put(key, value);
        fireChange(key, value, ret);
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        if (!backingMap.containsKey(key)) {
            return null;
        }
        V ret = backingMap.remove(key);
        fireChange((K) key, null, ret);
        return ret;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (Entry<K, V> entry : new ArrayList<>(backingMap.entrySet())) {
            remove(entry.getKey());
        }
    }

    @Override
    public String toString() {
        return backingMap.toString();
    }
}
