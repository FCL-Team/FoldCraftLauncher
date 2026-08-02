package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.WeakListener;
import com.tungsten.fclcore.observable.collections.MapChangeListener;
import com.tungsten.fclcore.observable.collections.ObservableMap;
import com.tungsten.fclcore.observable.value.ChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simplified reimplementation of {@code fakefx.binding.MapExpressionHelper}，语义对齐：
 * <ul>
 * <li>整体替换：invalidation 总是触发；change/map-change 仅在新旧引用不同（!=）时触发，
 * map-change 逐键先收旧条目 removed、再收新条目 added；</li>
 * <li>内容变更：invalidation 触发，change 收到 (current, current)，map-change 收到原始变更。</li>
 * </ul>
 */
final class MapPropertyHelper<K, V> {

    private final ReadOnlyMapProperty<K, V> property;
    private List<InvalidationListener> invalidationListeners;
    private List<ChangeListener<? super ObservableMap<K, V>>> changeListeners;
    private List<MapChangeListener<? super K, ? super V>> mapChangeListeners;
    private ObservableMap<K, V> currentValue;

    MapPropertyHelper(ReadOnlyMapProperty<K, V> property) {
        this.property = property;
    }

    void addListener(InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (invalidationListeners == null) {
            invalidationListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(invalidationListeners);
        invalidationListeners.add(listener);
    }

    void removeListener(InvalidationListener listener) {
        if (invalidationListeners != null) {
            invalidationListeners.remove(listener);
        }
    }

    void addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (changeListeners == null) {
            changeListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(changeListeners);
        if (changeListeners.isEmpty() && (mapChangeListeners == null || mapChangeListeners.isEmpty())) {
            currentValue = property.getValue();
        }
        changeListeners.add(listener);
    }

    void removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
        if (changeListeners != null) {
            changeListeners.remove(listener);
        }
    }

    void addListener(MapChangeListener<? super K, ? super V> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (mapChangeListeners == null) {
            mapChangeListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(mapChangeListeners);
        if (mapChangeListeners.isEmpty() && (changeListeners == null || changeListeners.isEmpty())) {
            currentValue = property.getValue();
        }
        mapChangeListeners.add(listener);
    }

    void removeListener(MapChangeListener<? super K, ? super V> listener) {
        if (mapChangeListeners != null) {
            mapChangeListeners.remove(listener);
        }
    }

    private static void purgeGarbageCollected(List<?> listeners) {
        listeners.removeIf(l -> l instanceof WeakListener && ((WeakListener) l).wasGarbageCollected());
    }

    void fireValueChangedEvent() {
        if (invalidationListeners != null) {
            for (InvalidationListener listener : new ArrayList<>(invalidationListeners)) {
                try {
                    listener.invalidated(property);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
        final boolean hasChange = changeListeners != null && !changeListeners.isEmpty();
        final boolean hasMapChange = mapChangeListeners != null && !mapChangeListeners.isEmpty();
        if (hasChange || hasMapChange) {
            final ObservableMap<K, V> oldValue = currentValue;
            currentValue = property.getValue();
            if (currentValue != oldValue) {
                if (hasChange) {
                    for (ChangeListener<? super ObservableMap<K, V>> listener : new ArrayList<>(changeListeners)) {
                        try {
                            listener.changed(property, oldValue, currentValue);
                        } catch (Exception e) {
                            Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                        }
                    }
                }
                if (hasMapChange) {
                    final List<MapChangeListener.Change<K, V>> changes = new ArrayList<>();
                    if (oldValue != null) {
                        for (Map.Entry<K, V> entry : oldValue.entrySet()) {
                            changes.add(new SimpleChange(entry.getKey(), null, entry.getValue()));
                        }
                    }
                    if (currentValue != null) {
                        for (Map.Entry<K, V> entry : currentValue.entrySet()) {
                            changes.add(new SimpleChange(entry.getKey(), entry.getValue(), null));
                        }
                    }
                    for (MapChangeListener.Change<K, V> change : changes) {
                        for (MapChangeListener<? super K, ? super V> listener : new ArrayList<>(mapChangeListeners)) {
                            try {
                                listener.onChanged(change);
                            } catch (Exception e) {
                                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                            }
                        }
                    }
                }
            }
        }
    }

    void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
        if (invalidationListeners != null) {
            for (InvalidationListener listener : new ArrayList<>(invalidationListeners)) {
                try {
                    listener.invalidated(property);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
        if (changeListeners != null) {
            for (ChangeListener<? super ObservableMap<K, V>> listener : new ArrayList<>(changeListeners)) {
                try {
                    listener.changed(property, currentValue, currentValue);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
        if (mapChangeListeners != null) {
            for (MapChangeListener<? super K, ? super V> listener : new ArrayList<>(mapChangeListeners)) {
                try {
                    listener.onChanged(change);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
    }

    private final class SimpleChange extends MapChangeListener.Change<K, V> {

        private final K key;
        private final V added;
        private final V removed;

        SimpleChange(K key, V added, V removed) {
            super(property);
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
    }
}
