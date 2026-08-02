package com.tungsten.fclcore.observable.collections;

/**
 * Minimal reimplementation of {@code fakefx.collections.MapChangeListener}.
 */
@FunctionalInterface
public interface MapChangeListener<K, V> {

    abstract class Change<K, V> {

        private final ObservableMap<K, V> map;

        public Change(ObservableMap<K, V> map) {
            this.map = map;
        }

        public ObservableMap<K, V> getMap() {
            return map;
        }

        public abstract K getKey();

        public abstract V getValueAdded();

        public abstract V getValueRemoved();

        public boolean wasAdded() {
            return getValueAdded() != null;
        }

        public boolean wasRemoved() {
            return getValueRemoved() != null;
        }
    }

    void onChanged(Change<? extends K, ? extends V> change);
}
