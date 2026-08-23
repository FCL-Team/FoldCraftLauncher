package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.collections.MapChangeListener.Change;

public class MapAdapterChange<K, V> extends MapChangeListener.Change<K, V> {
    private final Change<? extends K, ? extends V> change;

    public MapAdapterChange(ObservableMap<K, V> map, Change<? extends K, ? extends V> change) {
        super(map);
        this.change = change;
    }

    @Override
    public boolean wasAdded() {
        return change.wasAdded();
    }

    @Override
    public boolean wasRemoved() {
        return change.wasRemoved();
    }

    @Override
    public K getKey() {
        return change.getKey();
    }

    @Override
    public V getValueAdded() {
        return change.getValueAdded();
    }

    @Override
    public V getValueRemoved() {
        return change.getValueRemoved();
    }

    @Override
    public String toString() {
        return change.toString();
    }

}
